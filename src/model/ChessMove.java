package model;

public class ChessMove {
	
	private final int initialRow, initialCol;
	private final int finalRow, finalCol;
	
	ChessMove(int r1, int c1, int r2, int c2) {
		this.initialRow = r1;
		this.initialCol = c1;
		this.finalRow = r2;
		this.finalCol = c2;
	}

	/*
	 * Getters & Setters
	 */
	
	public int getInitialRow() {
		return initialRow;
	}

	public int getInitialCol() {
		return initialCol;
	}

	public int getFinalRow() {
		return finalRow;
	}

	public int getFinalCol() {
		return finalCol;
	}

}
