package model;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
		
	public Rook(Color color) {
		super(color);
	}

	@Override
	public ChessMove[] getLegalMoves(ChessModel model, int r, int c) {
		List<ChessMove> moves = new ArrayList<>();
		for (int i = 0; i < ChessModel.ROWS; i++) {
			if (canMove(model, r, c, r+i, c)) {
				moves.add(new ChessMove(r, c, r+i, c));
			}
			if (canMove(model, r, c, r-i, c)) {
				moves.add(new ChessMove(r, c, r-i, c));
			}
			if (canMove(model, r, c, r, c+i)) {
				moves.add(new ChessMove(r, c, r, c+i));
			}
			if (canMove(model, r, c, r, c-i)) {
				moves.add(new ChessMove(r, c, r, c-i));
			}
		}
		return moves.toArray(new ChessMove[0]);
	}
	
	public boolean canMove(ChessModel model, int r1, int c1, int r2, int c2) {
		if (r2 < 0 || r2 >= ChessModel.ROWS || c2 < 0 || c2 >= ChessModel.COLS) {
			return false;
		}
		if (model.isColorAt(r2, c2, color)) {
			return false;
		}
		if (model.getTypeAt(r2, c2) == PieceType.KING) {
			return false;
		}
		if (r1 < r2) {
			for (int r = r1+1; r1 < r2+1; r++) {
				if (r >= ChessModel.ROWS) {
					break;
				}
				if (r == r2) {
					return true;
				}
				if (model.pieceAt(r, c2) != null) {
					return false;
				}
			}
		}
		else if (r1 > r2) {
			for (int r = r1-1; r > r2-1; r--) {
				if (r <= 0) {
					break;
				}
				if (r == r2) {
					return true;
				}
				if (model.pieceAt(r, c2) != null) {
					return false;
				}
			}
		}
		if (c1 < c2) {
			for (int c = c1+1; c < c2+1; c++) {
				if (c >= ChessModel.COLS) {
					break;
				}
				if (c == c2) {
					return true;
				}
				if (model.pieceAt(r2, c) != null) {
					return false;
				}
			}
		}
		else if (c1 > c2) {
			for (int c = c1-1; c > c2-1; c--) {
				if (c <= 0) {
					break;
				}
				if (c == c2) {
					return true;
				}
				if (model.pieceAt(r2, c) != null) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean isCheck(ChessModel model, int r, int c) {
		Color opponent = Util.opponent(color);
		for (int i = r+1; i < ChessModel.ROWS; i++) {
			if ((model.getTypeAt(i, c) != PieceType.KING 
					|| model.isColorAt(i, c, color)) 
					&& model.getTypeAt(i, c) != PieceType.EMPTY) {
				break;
			}
			if (model.isColorAt(i, c, opponent) && model.getTypeAt(i, c) == PieceType.KING) {
				return true;
			}
		}
		for (int i = r-1; i >= 0; i--) {
			if ((model.getTypeAt(i, c) != PieceType.KING 
					|| model.isColorAt(i, c, color)) 
					&& model.getTypeAt(i, c) != PieceType.EMPTY) {
				break;
			}
			if (model.isColorAt(i, c, opponent) && model.getTypeAt(i, c) == PieceType.KING) {
				return true;
			}
		}
		for (int i = c+1; i < ChessModel.COLS; i++) {
			if ((model.getTypeAt(r, i) != PieceType.KING 
					|| model.isColorAt(r, i, color)) 
					&& model.getTypeAt(r, i) != PieceType.EMPTY) {
				break;
			}
			if (model.isColorAt(r, i, opponent) && model.getTypeAt(r, i) == PieceType.KING) {
				return true;
			}
		}
		for (int i = c-1; i >= 0; i--) {
			if ((model.getTypeAt(r, i) != PieceType.KING 
					|| model.isColorAt(r, i, color)) 
					&& model.getTypeAt(r, i) != PieceType.EMPTY) {
				break;
			}
			if (model.isColorAt(r, i, opponent) && model.getTypeAt(r, i) == PieceType.KING) {
				return true;
			}
		}
		return false;
	}
	
	public PieceType getType() {
		return PieceType.ROOK;
	}
	
	@Override
	public Piece copy() {
		Rook r = new Rook(color);
		r.setFirstMove(firstMove);
		return r;
	}
	
	@Override
	public boolean isAttacking(ChessModel model, int r1, int c1, int r2, int c2) {
		return canMove(model, r1, c1, r2, c2);
	}

	public int getScoreValue() {
		return 5;
	}
}
