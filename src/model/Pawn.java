package model;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

	/*
	 * Handled by model - promotion allows the user to pick what piece
	 * they want to switch the pawn for
	 */

	public Pawn(Color color) {
		super(color);
	}
	
	@Override
	public ChessMove[] getLegalMoves(ChessModel model, int r, int c) {
		List<ChessMove> moves = new ArrayList<>();	
		int direction = color == Color.WHITE ? -1 : 1;
		if (firstMove
				&& model.pieceAt(r+direction, c) == null 
				&& model.pieceAt(r+(2*direction), c) == null) {
			moves.add(new ChessMove(r, c, r+(2*direction), c));
		}
		if (r+direction >= 0 && r+direction < ChessModel.ROWS && model.pieceAt(r+direction, c) == null) {
			moves.add(new ChessMove(r, c, r+direction, c));
		}
		if (c+1 < ChessModel.COLS && r+direction >= 0 && r+direction < ChessModel.ROWS 
				&& !model.isColorAt(r+direction, c+1, color) 
				&& model.getTypeAt(r+direction, c+1) != PieceType.KING
				&& model.pieceAt(r+direction, c+1) != null) {
			moves.add(new ChessMove(r, c, r+direction, c+1));
		}
		if (c-1 >= 0 && r+direction >= 0 && r+direction < ChessModel.ROWS 
				&& !model.isColorAt(r+direction, c-1, color) 
				&& model.getTypeAt(r+direction, c-1) != PieceType.KING
				&& model.pieceAt(r+direction, c-1) != null) {
			moves.add(new ChessMove(r, c, r+direction, c-1));
		}
		enPassante(model, moves, r, c, color);
		return moves.toArray(new ChessMove[0]);
	}
	
	private void enPassante(ChessModel model, List<ChessMove> moves, int r, int c, Color color) {
		if (r != 3 && r != 4) {
			return;
		}
		if (model.pieceAt(r, c).getColor() == Color.WHITE && c-1 >= 0
				&& model.isColorAt(r, c-1, Color.BLACK)
				&& model.getTypeAt(r, c-1) == PieceType.PAWN) {
			moves.add(new ChessMove(r, c, r-1, c-1));
		}
		if (model.pieceAt(r, c).getColor() == Color.WHITE && c+1 < ChessModel.COLS
				&& model.isColorAt(r, c+1, Color.BLACK)
				&& model.getTypeAt(r, c+1) == PieceType.PAWN) {
			moves.add(new ChessMove(r, c, r-1, c+1));
		}
		if (model.pieceAt(r, c).getColor() == Color.BLACK && c-1 >= 0
				&& model.isColorAt(r, c-1, Color.WHITE)
				&& model.getTypeAt(r, c-1) == PieceType.PAWN) {
			moves.add(new ChessMove(r, c, r+1, c-1));
		}
		if (model.pieceAt(r, c).getColor() == Color.BLACK && c+1 < ChessModel.COLS
				&& model.isColorAt(r, c+1, Color.WHITE)
				&& model.getTypeAt(r, c+1) == PieceType.PAWN) {
			moves.add(new ChessMove(r, c, r+1, c+1));
		}
	}
	
	@Override
	public boolean isCheck(ChessModel model, int r, int c) {
		Color opponent = Util.opponent(color);
		if (color == Color.WHITE && r-1 >= 0 && c-1 >= 0 
				&& model.isColorAt(r-1, c-1, opponent) 
				&& model.getTypeAt(r-1, c-1) == PieceType.KING) {
			return true;
		}
		if (color == Color.WHITE && r-1 >= 0 && c+1 < ChessModel.COLS 
				&& model.isColorAt(r-1, c+1, opponent) 
				&& model.getTypeAt(r-1, c+1) == PieceType.KING) {
			return true;
		}
		if (color == Color.BLACK && r+1 < ChessModel.ROWS && c-1 >= 0 
				&& model.isColorAt(r+1, c-1, opponent) 
				&& model.getTypeAt(r+1, c-1) == PieceType.KING) {
			return true;
		}
		if (color == Color.BLACK && r+1 < ChessModel.ROWS && c+1 < ChessModel.COLS
				&& model.isColorAt(r+1, c+1, opponent) 
				&& model.getTypeAt(r+1, c+1) == PieceType.KING) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canMove(ChessModel model, int r1, int c1, int r2, int c2) {
		for (ChessMove move : getLegalMoves(model, r1, c1)) {
			if (move.getFinalRow() == r2 && move.getFinalCol() == c2) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkPromotion(ChessModel model, int r1, int c1, int r2, int c2) {
		if (model.isColorAt(r1, c1, Color.WHITE)) {
			if (r2 == 0) {
				return true;
			}
		}
		else {
			if (r2 == ChessModel.ROWS - 1) {
				return true;
			}
		}
		return false;
	}
		
	public PieceType getType() {
		return PieceType.PAWN;
	}
	
	@Override
	public Piece copy() {
		Pawn p = new Pawn(color);
		p.setFirstMove(firstMove);
		return p;
	}
	
	@Override
	public boolean isAttacking(ChessModel model, int r1, int c1, int r2, int c2) {
		return (r2 == r1-1 || r2 == r1+1) && (c2 == c1+1 || c2 == c1-1);
	}

	public int getScoreValue() {
		return 1;
	}
	
}
