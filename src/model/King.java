package model;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

	public King(Color color) {
		super(color);
	}

	@Override
	public ChessMove[] getLegalMoves(ChessModel model, int r, int c) {
		List<ChessMove> moves = new ArrayList<>();

		if (r-1 >= 0 && canMove(model, r, c, r-1, c)) {
			moves.add(new ChessMove(r, c, r-1, c));
		}
		if (r+1 < ChessModel.ROWS && canMove(model, r, c, r+1, c)) {
			moves.add(new ChessMove(r, c, r+1, c));
		}
		if (c-1 >= 0 && canMove(model, r, c, r, c-1)) {
			moves.add(new ChessMove(r, c, r, c-1));
		}
		if (c+1 < ChessModel.COLS && canMove(model, r, c, r, c+1)) {
			moves.add(new ChessMove(r, c, r, c+1));
		}
		if (r+1 < ChessModel.ROWS && c-1 >= 0 && canMove(model, r, c, r+1, c-1)) {
			moves.add(new ChessMove(r, c, r+1, c-1));
		}
		if (r+1 < ChessModel.ROWS && c+1 < ChessModel.COLS && canMove(model, r, c, r+1, c+1)) {
			moves.add(new ChessMove(r, c, r+1, c+1));
		}
		if (r-1 >= 0 && c-1 >= 0 && canMove(model, r, c, r-1, c-1)) {
			moves.add(new ChessMove(r, c, r-1, c-1));
		}
		if (r-1 >= 0 && c+1 < ChessModel.COLS && canMove(model, r, c, r-1, c+1)) {
			moves.add(new ChessMove(r, c, r-1, c+1));
		}
		
		castle(model, moves, r, c);

		if (moves.size() == 0) {
			return null;
		}
		return moves.toArray(new ChessMove[0]);
	}

	@Override
	public boolean isCheck(ChessModel model, int r, int c) {
		// King can't put other King to check
		return false;
	}

	@Override
	public boolean canMove(ChessModel model, int r1, int c1, int r2, int c2) {
		if (r2 < 0 || r2 >= ChessModel.ROWS || c2 < 0 || c2 >= ChessModel.COLS) {
			return false;
		}
		Color opponent = Util.opponent(color);
		ChessModel t = model.copy();
		t.makeMove(new ChessMove(r1, c1, r2, c2));
		Piece[][] aroundKing = new Piece[3][3];
		aroundKing[1][1] = t.pieceAt(r2, c2);
		aroundKing[0][0] = model.pieceAt(r2-1, c2-1);
		aroundKing[0][1] = model.pieceAt(r2-1, c2);
		aroundKing[0][2] = model.pieceAt(r2-1, c2+1);
		aroundKing[1][0] = model.pieceAt(r2, c2-1);
		aroundKing[1][2] = model.pieceAt(r2, c2+1);
		aroundKing[2][0] = model.pieceAt(r2+1, c2-1);
		aroundKing[2][1] = model.pieceAt(r2+1, c2);
		aroundKing[2][2] = model.pieceAt(r2+1, c2+1);
		for (int i = 0; i < aroundKing.length; i++) {
			for (int j = 0; j < aroundKing[i].length; j++) {
				if (aroundKing[i][j] != null) {
					if (aroundKing[i][j].getType() == PieceType.KING 
							&& aroundKing[i][j].getColor() == opponent 
							&& i != r2 && j != c2) {
						return false;
					}
				}
			}
		}
		if (model.getTypeAt(r2, c2) != PieceType.EMPTY && model.isColorAt(r2, c2, color)) {
			return false;
		}
		return true;
	}
	
	private void castle(ChessModel model, List<ChessMove> moves, int r, int c) {
		if (!firstMove) {
			return;
		}
		if (c+3 < ChessModel.COLS && model.getTypeAt(r, c+3) == PieceType.ROOK) {
			if (model.pieceAt(r, c+3).isFirstMove() 
					&& model.pieceAt(r, c+1) == null && model.pieceAt(r, c+2) == null) {
				for (int i = 0; i < ChessModel.ROWS; i++) {
					for (int j = 0; j < ChessModel.COLS; j++) {
						if (model.pieceAt(i, j) != null && model.pieceAt(i, j).isAttacking(model, i, j, r, c+1)) {
							break;
						}
						if (model.pieceAt(i, j) != null && model.pieceAt(i, j).isAttacking(model, i, j, r, c+2)) {
							break;
						}
					}
				}
				moves.add(new ChessMove(r, c, r, c+2));
			}
		}
		if (c-4 >= 0 && model.getTypeAt(r, c-4) == PieceType.ROOK) {
			if (model.pieceAt(r, c-4).isFirstMove() 
					&& model.pieceAt(r, c-1) == null && model.pieceAt(r, c-2) == null) {
				for (int i = 0; i < ChessModel.ROWS; i++) {
					for (int j = 0; j < ChessModel.COLS; j++) {
						if (model.pieceAt(i, j) != null && model.pieceAt(i, j).isAttacking(model, i, j, r, c-1)) {
							break;
						}
						if (model.pieceAt(i, j) != null && model.pieceAt(i, j).isAttacking(model, i, j, r, c-2)) {
							break;
						}
					}
				}
				moves.add(new ChessMove(r, c, r, c-2));
			}
		}
	}

	public PieceType getType() {
		return PieceType.KING;
	}

	@Override
	public Piece copy() {
		King k = new King(color);
		k.firstMove = firstMove;
		return k;
	}
	
	@Override
	public boolean isAttacking(ChessModel model, int r1, int c1, int r2, int c2) {
		return canMove(model, r1, c1, r2, c2);
	}

	public int getScoreValue() {
		return 0;
	}
}
