package model;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

	public Bishop(Color color) {
		super(color);
	}
	
	@Override
	public ChessMove[] getLegalMoves(ChessModel model, int r, int c) {
		List<ChessMove> moves = new ArrayList<>();
		for (int i = 0; i < ChessModel.ROWS; i++) {
			if (canMove(model, r, c, r+i, c+i)) {
				moves.add(new ChessMove(r, c, r+i, c+i));
			}
			if (canMove(model, r, c, r+i, c-i)) {
				moves.add(new ChessMove(r, c, r+i, c-i));
			}
			if (canMove(model, r, c, r-i, c+i)) {
				moves.add(new ChessMove(r, c, r-i, c+i));
			}
			if (canMove(model, r, c, r-i, c-i) ) {
				moves.add(new ChessMove(r, c, r-i, c-i));
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
		int c = c1+1;
		if (r2 > r1 && c2 > c1) {
			for (int r=r1+1; r<r2; r++) {
				if (c >= ChessModel.COLS) {
					break;
				}
				if (model.pieceAt(r, c) != null) {
					return false;
				}
				c++;
			}
		}
		c = c1-1;
		if (r2 > r1 && c1 > c2) {
			for (int r = r1+1; r < r2; r++) {
				if (c < 0) {
					break;
				}
				if (model.pieceAt(r, c) != null) {
					return false;
				}
				c--;
			}
		}
		c = c1-1;
		if (r2 < r1 && c1 > c2) {
			for (int r = r1-1; r > r2; r--) {
				if (c < 0) {
					break;
				}
				if (model.pieceAt(r, c) != null) {
					return false;
				}
				c--;
			}
		}
		c = c1+1;
		if (r2 < r1 && c1 < c2) {
			for (int r = r1-1; r > r2; r--) {
				if (c >= ChessModel.COLS) {
					break;
				}
				if (model.pieceAt(r, c) != null) {
					return false;
				}
				c++;
			}
		}
		return true;
	}

	@Override
	public boolean isCheck(ChessModel model, int r, int c) {
		Color opponent = Util.opponent(color);
		
		int cTemp = c+1;
		for (int i = r+1; i < ChessModel.ROWS; i++) {
			if (cTemp >= ChessModel.COLS) {
				break;
			}
			if ((model.getTypeAt(i, cTemp) != PieceType.KING 
					|| model.getTypeAt(i, cTemp) == PieceType.KING && model.isColorAt(i, cTemp, color)) 
					&& model.getTypeAt(i, cTemp) != PieceType.EMPTY) {
				break;
			}
			if (model.isColorAt(i, cTemp, opponent) && model.getTypeAt(i, cTemp) == PieceType.KING) {
				return true;
			}
			cTemp++;
		}
		cTemp = c-1;
		for (int i = r+1; i < ChessModel.ROWS; i++) {
			if (cTemp < 0) {
				break;
			}
			if ((model.getTypeAt(i, cTemp) != PieceType.KING 
					|| model.getTypeAt(i, cTemp) == PieceType.KING && model.isColorAt(i, cTemp, color)) 
					&& model.getTypeAt(i, cTemp) != PieceType.EMPTY) {
				break;
			}
			if (model.isColorAt(i, cTemp, opponent) && model.getTypeAt(i, cTemp) == PieceType.KING) {
				return true;
			}
			cTemp--;
		}
		cTemp = c+1;
		for (int i = r-1; i >= 0; i--) {
			if (cTemp >= ChessModel.COLS) {
				break;
			}
			if ((model.getTypeAt(i, cTemp) != PieceType.KING 
					|| model.getTypeAt(i, cTemp) == PieceType.KING && model.isColorAt(i, cTemp, color)) 
					&& model.getTypeAt(i, cTemp) != PieceType.EMPTY) {
				break;
			}
			if (model.isColorAt(i, cTemp, opponent) && model.getTypeAt(i, cTemp) == PieceType.KING) {
				return true;
			}
			cTemp++;
		}
		cTemp = c-1;
		for (int i = r-1; i >= 0; i--) {
			if (cTemp < 0) {
				break;
			}
			if ((model.getTypeAt(i, cTemp) != PieceType.KING 
					|| model.getTypeAt(i, cTemp) == PieceType.KING && model.isColorAt(i, cTemp, color)) 
					&& model.getTypeAt(i, cTemp) != PieceType.EMPTY) {
				break;
			}
			if (model.isColorAt(i, cTemp, opponent) && model.getTypeAt(i, cTemp) == PieceType.KING) {
				return true;
			}
			cTemp--;
		}
		return false;
	}
	
	@Override
	public Piece copy() {
		return new Bishop(color);
	}

	public PieceType getType() {
		return PieceType.BISHOP;
	}
	
	@Override
	public boolean isAttacking(ChessModel model, int r1, int c1, int r2, int c2) {
		return canMove(model, r1, c1, r2, c2);
	}

	public int getScoreValue() {
		return 3;
	}

}
