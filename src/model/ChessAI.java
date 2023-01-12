package model;

/**
 * In order to make a Chess AI, I will be using a MiniMax algorithm
 * Learned from https://medium.com/@SereneBiologist/the-anatomy-of-a-chess-ai-2087d0d565
 * This current AI is O(2^n)
 * This AI is very naive but a real AI would use Alpha-Beta pruning to ignore branches with huge
 * max/min deltas and have many more complex metrics to grade a board on
 * 
 *
 */
public class ChessAI {		
	/*
	 * The tree has a set depth in term of searching boards
	 * If we go past 4 or 5, we will be dealing with too many boards to realistically handle
	 */
	private final int DEPTH = 2; // This number can be changed to make the AI smarter
	
	private class ChessNode {
		/**
		 * Our current board
		 */
		private ChessModel model;
		/**
		 * Our current depth in the tree
		 */
		private int depth;
		/**
		 * Are we a max or min node
		 */
		private boolean max;
		/**
		 * The move that got us here
		 */
		private ChessMove move;
		/**
		 * Create a node with model and depth
		 * @param model
		 * @param depth
		 */
		public ChessNode(ChessModel model, int depth, boolean max, ChessMove move) {
			this.model = model;
			this.depth = depth;
			this.max = max;
			this.move = move;
		}
	
		public ChessNode getOptimalMove() {
			if (depth == DEPTH) {
				return this;
			}
			int optimalValue = max ? -1000 : 1000;
			ChessNode optimalNode = null;
			ChessMove[] moves = model.getLegalMovesForPlayer(model.getCurrentPlayer());
			for (ChessMove move : moves) {
				ChessModel child = model.copy();
				child.makeMove(move);
				ChessNode childNode = new ChessNode(child, depth+1, !max, move);
				ChessNode optimalChild = childNode.getOptimalMove();
				int boardValue = optimalChild.getBoardValue();
				if (max && boardValue > optimalValue || !max && boardValue < optimalValue) {
					optimalValue = boardValue;
					optimalNode = optimalChild;
				}
			}
			return optimalNode;
		}
		
		public ChessMove getMove() {
			return move;
		}
		
		/**
		 * Scoring algorithm
		 * @return
		 */
		public int getBoardValue() {
			/*
			 * We will be using a point system based on the pieces that the
			 * AI has and the pieces the player has
			 * Also judging the king's defense
			 * Also judging center control
			 */
			int value = 0;
			int aiControl = 0;
			int playerControl = 0;
			for (int r = 0; r < ChessModel.ROWS; r++) {
				for (int c = 0; c < ChessModel.COLS; c++) {
					Piece p = model.pieceAt(r, c);
					if (p != null
							&& (((r == 3 || r == 4) && (c == 3 || c == 4))
							|| p.isAttacking(model, r, c, 3, 3) 
							|| p.isAttacking(model, r, c, 4, 3)
							|| p.isAttacking(model, r, c, 3, 4)
							|| p.isAttacking(model, r, c, 4, 4))) {
						if (p.getColor() == Color.BLACK) {
							aiControl++;
						} else {
							playerControl++;							
						}
					}
				}
			}
			if (aiControl > playerControl) {
				value += 1;
			}
			
			/*
			 * King defense scale:
			 * If our AI's king is in its beginning row and the other king is not, +2
			 * If both are, +1
			 * If our AI's king is not, +0
			 * If there is a piece in front of our king, +2
			 * If both have, +1
			 * If no piece, +0
			 */
			int aiKingScore = 0;
			int playerKingScore = 0;
			for (int c = 0; c < ChessModel.COLS; c++) {
				if (model.getTypeAt(0, c) == PieceType.KING && model.isColorAt(0, c, Color.BLACK)) {
					if (model.isColorAt(1, c, Color.BLACK)) {
						aiKingScore++;
					}
					aiKingScore++;
				}
				if (model.getTypeAt(ChessModel.ROWS-1, c) == PieceType.KING && model.isColorAt(ChessModel.ROWS-1, c, Color.WHITE)) {
					if (model.isColorAt(ChessModel.ROWS-2, c, Color.WHITE)) {
						playerKingScore++;
					}
					playerKingScore++;
				}
			}	
			if (aiKingScore > playerKingScore) {
				value += 1;
			}
			/*
			 * Next check values on board
			 * If the AI color has a higher overall value, +2
			 * If they have same value, +1
			 */
			int playerPawnCount = 0;
			int aiPawnCount = 0;
			for (int r = 0; r < ChessModel.ROWS; r++) {
				for (int c = 0; c < ChessModel.COLS; c++) {
					Piece p = model.pieceAt(r, c);
					if (p != null) {
						if (p.getColor() == Color.BLACK) {
							value += p.getScoreValue();
							if (p.getType() == PieceType.PAWN) {
								aiPawnCount++;
							}
						} else {
							value -= p.getScoreValue();
							if (p.getType() == PieceType.PAWN) {
								playerPawnCount++;
							}
						}
					}
				}
			}
			/*
			 * If a pawn is close to being promoted
			 */
			boolean playerPromotion = false;
			int playerCount = 0;
			boolean aiPromotion = false;
			int aiCount = 0;
			for (int j = 0; j < ChessModel.COLS; j++) {
				Piece p = model.pieceAt(1, j);
				if (p != null) {
					if (p.getColor() == Color.WHITE) {
						if (p.getType() == PieceType.PAWN) {
							playerPromotion = true;
							playerCount++;
						}
					}
				}
				p = model.pieceAt(ChessModel.ROWS-2, j); 
				if (p != null) {
					if (p.getColor() == Color.BLACK) {
						if (p.getType() == PieceType.PAWN) {
							aiPromotion = true;
							aiCount++;
						}
					}
				}
			}
			if (aiPromotion && !playerPromotion) {
				value += 2;
			}
			if (aiPromotion && playerPromotion) {
				if (playerCount > aiCount) {
					value -= 1;
				}
				if (aiCount > playerCount) {
					value += 1;
				}
			}
			/*
			 * More pawns is worse
			 */
			value += aiPawnCount > playerPawnCount ? -1 : aiPawnCount < playerPawnCount ? 1 : 0;
			return value;
		}
	}
	
	/**
	 * Returns the most optimal move node
	 * @return ChessMove obj
	 */
	
	public ChessMove getOptimalMove(ChessModel model) {
		ChessNode root = new ChessNode(model, 1, true, null);
		return root.getOptimalMove().getMove();
	}
}
