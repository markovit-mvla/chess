package model;

public abstract class Piece {
	
	protected Color color;
	
	protected boolean firstMove = true;
	
	public Piece(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public abstract PieceType getType();

	public abstract ChessMove[] getLegalMoves(ChessModel model, int r, int c);
	
	public abstract boolean isCheck(ChessModel model, int r, int c);
	
	public abstract boolean isAttacking(ChessModel model, int r1, int c1, int r2, int c2);
	
	public abstract boolean canMove(ChessModel model, int r1, int c1, int r2, int c2);
	
	public abstract Piece copy();	
	
	/*
	 * https://www.chess.com/terms/chess-piece-value#:~:text=Chess%20Piece%20Values,-As%20mentioned%2C%20each&text=A%20pawn%20is%20worth%20one,t%20have%20a%20point%20value.
	 */
	public abstract int getScoreValue();
	
	public boolean isFirstMove() {
		return firstMove;
	}
	
	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}
	
}
