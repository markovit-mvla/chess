package model;

import java.util.ArrayList;
import java.util.List;

import view.ChessView;

public class ChessModel {

	public static final int
	ROWS = 8,
	COLS = 8;

	private Piece[][] board;
	private boolean gameStatus;
	PieceType promoteTo;
	private ChessMove promotionMove;
	private Color currentPlayer;
	private int selectedRow, selectedCol;
	private int countTilCheckmate;
	private ChessMove[] selectedLegalMoves;

	private ChessView view;
	
	public ChessModel(ChessView view) {
		board = new Piece[ROWS][COLS];
		spawn();
		currentPlayer = Color.WHITE;
		this.view = view;
	}
	
	public ChessModel() {
		board = new Piece[ROWS][COLS];
		spawn();
		currentPlayer = Color.WHITE;
	}

	public void spawn() {
		board[0][0] = new Rook(Color.BLACK);
		board[0][COLS-1] = new Rook(Color.BLACK);
		board[0][1] = new Knight(Color.BLACK);
		board[0][COLS-2] = new Knight(Color.BLACK);
		board[0][2] = new Bishop(Color.BLACK);
		board[0][COLS-3] = new Bishop(Color.BLACK);
		board[0][3] = new Queen(Color.BLACK);
		board[0][4] = new King(Color.BLACK);
		for (int c = 0; c < COLS; c++) {
			board[1][c] = new Pawn(Color.BLACK);
			board[ROWS-2][c] = new Pawn(Color.WHITE);
		}
		board[ROWS-1][0] = new Rook(Color.WHITE);
		board[ROWS-1][COLS-1] = new Rook(Color.WHITE);
		board[ROWS-1][1] = new Knight(Color.WHITE);
		board[ROWS-1][COLS-2] = new Knight(Color.WHITE);
		board[ROWS-1][2] = new Bishop(Color.WHITE);
		board[ROWS-1][COLS-3] = new Bishop(Color.WHITE);
		board[ROWS-1][3] = new Queen(Color.WHITE);
		board[ROWS-1][4] = new King(Color.WHITE);
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @return Piece at (row, col)
	 */
	
	public Piece pieceAt(int row, int col) {
		return exists(row, col) ? board[row][col] : null;
	}

	public void makeMove(ChessMove move) {
		if (getTypeAt(move.getInitialRow(), move.getInitialCol()) == PieceType.KING 
				&& move.getFinalCol() == move.getInitialCol()+2) {
			makeMove(move.getInitialRow(), move.getFinalCol()+1, move.getFinalRow(), move.getFinalCol()-1);
		}
		else if (getTypeAt(move.getInitialRow(), move.getInitialCol()) == PieceType.KING 
				&& move.getFinalCol() == move.getInitialCol()-2) {
			makeMove(move.getInitialRow(), move.getFinalCol()-2, move.getFinalRow(), move.getFinalCol()+1);
		}
		else if (getTypeAt(move.getInitialRow(), move.getInitialCol()) == PieceType.PAWN 
				&& (move.getFinalRow() == move.getInitialRow() + 1 || move.getFinalRow() == move.getInitialRow() - 1)
				&& move.getFinalCol() == move.getInitialCol() + 1) {
			Piece p = pieceAt(move.getInitialRow(), move.getInitialCol());
			Piece p2 = pieceAt(move.getInitialRow(), move.getInitialCol()+1);
			if (getTypeAt(move.getInitialRow(), move.getInitialCol()+1) == PieceType.PAWN &&
					p.getColor() != p2.getColor()) {
				if (move.getInitialRow() == 3 || move.getInitialRow() == 4) {
					board[move.getInitialRow()][move.getInitialCol()+1] = null;
				}
			}
		}
		else if (getTypeAt(move.getInitialRow(), move.getInitialCol()) == PieceType.PAWN 
				&& (move.getFinalRow() == move.getInitialRow() + 1 || move.getFinalRow() == move.getInitialRow() - 1)
				&& move.getFinalCol() == move.getInitialCol() - 1) {
			Piece p = pieceAt(move.getInitialRow(), move.getInitialCol());
			Piece p2 = pieceAt(move.getInitialRow(), move.getInitialCol()-1);
			if (getTypeAt(move.getInitialRow(), move.getInitialCol()-1) == PieceType.PAWN &&
					p.getColor() != p2.getColor()) {
				if (move.getInitialRow() == 3 || move.getInitialRow() == 4) {
					board[move.getInitialRow()][move.getInitialCol()-1] = null;
				}
			}
		}
		makeMove(move.getInitialRow(), move.getInitialCol(), move.getFinalRow(), move.getFinalCol());
		countTilCheckmate++;
		currentPlayer = Util.opponent(currentPlayer);
		Piece p = pieceAt(move.getFinalRow(), move.getFinalCol());
		p.setFirstMove(false);
	}

	void makeMove(int initialRow, int initialCol, int finalRow, int finalCol) {
		if (finalRow >= 0 && finalRow < ROWS && finalCol >= 0 && finalCol < COLS) {
			board[finalRow][finalCol] = board[initialRow][initialCol];
			board[initialRow][initialCol] = null;
		}
	}
	
	public void doPromotion(int r, int c, PieceType type) {
		promoteTo = type;
		Color promotionColor = Util.opponent(currentPlayer);
		
		switch(promoteTo) {
		case QUEEN:
			board[r][c] = new Queen(promotionColor);
			break;
		case ROOK:
			board[r][c] = new Rook(promotionColor);
			break;
		case BISHOP:
			board[r][c] = new Bishop(promotionColor);
			break;
		default:
			board[r][c] = new Knight(promotionColor);
			break;
		}
	}

	/**
	 * 
	 * @param player
	 * @return Set of legal ChessMove objects
	 */
	
	public ChessMove[] getLegalMovesForPlayer(Color player) {
		List<ChessMove> moves = new ArrayList<>();
		for (int row=0; row<ROWS; row++) {
			for (int col=0; col<COLS; col++) {
				if (board[row][col] != null && board[row][col].getColor() == player) {
					ChessMove[] pieceMoves = board[row][col].getLegalMoves(this, row, col);
					if (pieceMoves != null) {
						for (ChessMove move : pieceMoves) {
							if (!isStillUnderCheck(move)) {
								moves.add(move);
							}
						}						
					}
				}
			}
		}
		return moves.toArray(new ChessMove[0]);
	}
	
	/*
	 * Getters & Setters
	 */

	public boolean isGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(boolean gameStatus) {
		this.gameStatus = gameStatus;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public int getCurrentPieceInt(int r, int c) {
		if (pieceAt(r, c) == null) {
			return -1;
		}
		if (pieceAt(r, c).getColor() == Color.WHITE) {
			return 0;
		}
		else {
			return 1;
		}
	}

	public void setCurrentPlayer(Color currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public int getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}

	public int getSelectedCol() {
		return selectedCol;
	}

	public void setSelectedCol(int selectedCol) {
		this.selectedCol = selectedCol;
	}

	public int getCountTilCheckmate() {
		return countTilCheckmate;
	}

	public void setCountTilCheckmate(int countTilCheckmate) {
		this.countTilCheckmate = countTilCheckmate;
	}

	public Piece[][] getBoard() {
		return board;
	}

	public void setBoard(Piece[][] board) {
		this.board = board;
	}
	
	public PieceType getTypeAt(int r, int c) {
		if (!exists(r, c)) {
			return PieceType.EMPTY;
		}
		return board[r][c] == null ? PieceType.EMPTY : board[r][c].getType(); 
	}
	
	public boolean isColorAt(int r, int c, Color color) {
		return board[r][c] != null && board[r][c].getColor() == color;
	}

	/*
	 * Creates new Chess board and spawns in the pieces
	 */

	public ChessMove[] getSelectedLegalMoves() {
		return selectedLegalMoves;
	}

	public void setSelectedLegalMoves(ChessMove[] selectedLegalMoves) {
		this.selectedLegalMoves = selectedLegalMoves;
	}
	
	public ChessMove getSelectedLegalMove(int row, int col) {
		if (selectedLegalMoves != null) {
			for (ChessMove move : selectedLegalMoves) {
				if (move.getFinalRow() == row && move.getFinalCol() == col) {
					return move;
				}
			}
		}
		return null;
	}
	
	public boolean isUnderCheck(Color player) {
		Color opponent = Util.opponent(player);
		for (int r=0; r<ROWS; r++) {
			for (int c=0; c<COLS; c++) {
				if (isColorAt(r, c, opponent)) {
					Piece p = pieceAt(r, c);
					if (p.isCheck(this, r, c)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean isCheckMate(Color player) {
		ChessMove[] moves = getLegalMovesForPlayer(player);
		return isUnderCheck(player) && (moves == null || moves.length == 0);
	}
	
	public boolean isStalemate(Color player) {
		ChessMove[] moves = getLegalMovesForPlayer(player);
		return !isUnderCheck(player) && (moves == null || moves.length == 0);
	}
	
	public ChessModel copy() {
		ChessModel m = new ChessModel(view);
		for (int i=0; i<ROWS; i++) {
			for (int j=0; j<COLS; j++) {
				m.board[i][j] = board[i][j] == null ? null : board[i][j].copy();
			}
		}
		m.gameStatus = gameStatus;
		m.currentPlayer = currentPlayer;
		m.selectedRow = selectedRow;
		m.selectedCol = selectedCol;
		m.countTilCheckmate = countTilCheckmate;
		if (selectedLegalMoves != null) {
			m.selectedLegalMoves = new ChessMove[selectedLegalMoves.length];
			System.arraycopy(selectedLegalMoves, 0, m.selectedLegalMoves, 0, selectedLegalMoves.length);
		}
		return m;
	}

	/**
	 * Check if after applying the move, the current player is still under check hence making it illegal
	 * @param move
	 * @return
	 */
	public boolean isStillUnderCheck(ChessMove move) {
		ChessModel m = copy();
		m.makeMove(move.getInitialRow(), move.getInitialCol(), move.getFinalRow(), move.getFinalCol());
		return m.isUnderCheck(currentPlayer);
	}
	
	public ChessMove[] getLegalMoves(int row, int col) {
		Piece piece = pieceAt(row, col);
		if (piece == null) {
			return null;
		}
		List<ChessMove> legalMoves = new ArrayList<>();
		ChessMove[] moves = piece.getLegalMoves(this, row, col);
		if (moves != null) {
			for (ChessMove move : moves) {
				if (!isStillUnderCheck(move)) {
					legalMoves.add(move);
				}
			}
		}
		return legalMoves.toArray(new ChessMove[0]);
	}
	
	public void setView(ChessView view) {
		this.view = view;
	}

	public ChessMove getPromotionMove() {
		return promotionMove;
	}

	public void setPromotionMove(ChessMove promotionMove) {
		this.promotionMove = promotionMove;
	}
	
	public boolean exists(int r, int c) {
		return r >= 0 && r < ROWS && c >= 0 && c < COLS;
	}
}
