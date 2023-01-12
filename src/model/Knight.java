package model;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

	public Knight(Color color) {
		super(color);
	}

	@Override
	public ChessMove[] getLegalMoves(ChessModel model, int r, int c) {
		List<ChessMove> moves = new ArrayList<>();
		if (r-1 >= 0 && c-2 >= 0 && !model.isColorAt(r-1, c-2, color)) {
			moves.add(new ChessMove(r, c, r-1, c-2));
		}
		if (r+1 < ChessModel.ROWS && c-2 >= 0 && !model.isColorAt(r+1, c-2, color)) {
			moves.add(new ChessMove(r, c, r+1, c-2));
		}
		if (r-2 >= 0 && c+1 < ChessModel.COLS && !model.isColorAt(r-2, c+1, color)) {
			moves.add(new ChessMove(r, c, r-2, c+1));
		}
		if (r-2 >= 0 && c-1 >= 0 && !model.isColorAt(r-2, c-1, color)) {
			moves.add(new ChessMove(r, c, r-2, c-1));
		}
		if (r+2 < ChessModel.ROWS && c-1 >= 0 && !model.isColorAt(r+2, c-1, color)) {
			moves.add(new ChessMove(r, c, r+2, c-1));
		}
		if (r+2 < ChessModel.ROWS && c+1 < ChessModel.COLS && !model.isColorAt(r+2, c+1, color)) {
			moves.add(new ChessMove(r, c, r+2, c+1));
		}
		if (r-1 >= 0 && c+2 < ChessModel.COLS && !model.isColorAt(r-1, c+2, color)) {
			moves.add(new ChessMove(r, c, r-1, c+2));
		}
		if (r+1 < ChessModel.ROWS && c+2 < ChessModel.COLS && !model.isColorAt(r+1, c+2, color)) {
			moves.add(new ChessMove(r, c, r+1, c+2));
		}
		return moves.toArray(new ChessMove[0]);
	}

	@Override
	public boolean isCheck(ChessModel model, int r, int c) {
		Color opponent = Util.opponent(color);
		if (r-1 >= 0 && c-2 >= 0 
				&& model.getTypeAt(r-1, c-2) == PieceType.KING 
				&& model.isColorAt(r-1, c-2, opponent)) {
			return true;
		}
		if (r+1 < ChessModel.ROWS && c-2 >= 0 
				&& model.getTypeAt(r+1, c-2) == PieceType.KING 
				&& model.isColorAt(r+1, c-2, opponent)) {
			return true;
		}
		if (r-2 >= 0 && c-1 >= 0 
				&& model.getTypeAt(r-2, c-1) == PieceType.KING 
				&& model.isColorAt(r-2, c-1, opponent)) {
			return true;
		}
		if (r-2 >= 0 && c+1 < ChessModel.COLS 
				&& model.getTypeAt(r-2, c+1) == PieceType.KING 
				&& model.isColorAt(r-2, c+1, opponent)) {
			return true;
		}
		if (r+2 < ChessModel.ROWS && c-1 >= 0 
				&& model.getTypeAt(r+2, c-1) == PieceType.KING 
				&& model.isColorAt(r+2, c-1, opponent)) {
			return true;
		}
		if (r+2 < ChessModel.ROWS && c+1 < ChessModel.COLS 
				&& model.getTypeAt(r+2, c+1) == PieceType.KING 
				&& model.isColorAt(r+2, c+1, opponent)) {
			return true;
		}
		if (r-1 >= 0 && c+2 < ChessModel.COLS
				&& model.getTypeAt(r-1, c+2) == PieceType.KING 
				&& model.isColorAt(r-1, c+2, opponent)) {
			return true;
		}
		if (r+1 < ChessModel.ROWS && c+2 < ChessModel.COLS 
				&& model.getTypeAt(r+1, c+2) == PieceType.KING 
				&& model.isColorAt(r+1, c+2, opponent)) {
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

	public PieceType getType() {
		return PieceType.KNIGHT;
	}

	@Override
	public Piece copy() {
		return new Knight(color);
	}
	
	@Override
	public boolean isAttacking(ChessModel model, int r1, int c1, int r2, int c2) {
		if ((r2 == r1-1 || r2 == r1+1) && (c2 == c1-2 || c2 == c1+2)) {
			return true;
		}
		if ((r2 == r1-2 || r2 == r1+2) && (c2 == c1-1 || c2 == c1+1)) {
			return true;
		}
		return false;
	}

	public int getScoreValue() {
		return 3;
	}
}
