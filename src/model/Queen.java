package model;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

	public Queen(Color color) {
		super(color);
	}
	
	@Override
	public ChessMove[] getLegalMoves(ChessModel model, int r, int c) {
		List<ChessMove> moves = new ArrayList<>();
		Rook rook = new Rook(color);
		Bishop bishop = new Bishop(color);
		for (int i = 0; i < rook.getLegalMoves(model, r, c).length; i++) {
			moves.add(rook.getLegalMoves(model, r, c)[i]);
		}
		for (int i = 0; i < bishop.getLegalMoves(model, r, c).length; i++) {
			moves.add(bishop.getLegalMoves(model, r, c)[i]);
		}
		return moves.toArray(new ChessMove[0]);
	}

	@Override
	public boolean isCheck(ChessModel model, int r, int c) {
		Bishop bishop = new Bishop(color);
		Rook rook = new Rook(color);
		return bishop.isCheck(model, r, c) || rook.isCheck(model, r, c);
	}
	
	@Override
	public boolean canMove(ChessModel model, int r1, int c1, int r2, int c2) {
		if (r2 < 0 || r2 >= ChessModel.ROWS || c2 < 0 || c2 >= ChessModel.COLS) {
			return false;
		}
		if (model.pieceAt(r2, c2) != null && model.pieceAt(r2, c2).getType() == PieceType.KING) {
			return false;
		}
		if (c1 == c2 || r1 == r2) {
			return new Rook(this.color).canMove(model, r1, c1, r2, c2);
		}
		return new Bishop(this.color).canMove(model, r1, c1, r2, c2);
	}
	
	public PieceType getType() {
		return PieceType.QUEEN;
	}

	@Override
	public Piece copy() {
		return new Queen(color);
	}
	
	@Override
	public boolean isAttacking(ChessModel model, int r1, int c1, int r2, int c2) {
		Bishop bishop = new Bishop(color);
		Rook rook = new Rook(color);
		return bishop.canMove(model, r1, c1, r2, c2) || rook.canMove(model, r1, c1, r2, c2);
	}
	
	public int getScoreValue() {
		return 9;
	}
}
