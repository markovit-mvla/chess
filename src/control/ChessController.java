package control;

import java.awt.event.*;

import model.ChessModel;
import model.ChessMove;
import model.Color;
import model.Piece;
import view.ChessView;

public class ChessController implements ActionListener, MouseListener {

	private ChessModel model;
	private ChessView view;
	private AIThread ai;
	private boolean aiThinking;
	
	public ChessController(ChessModel m, ChessView v) {
		this.model = m;
		this.view = v;
		ai = new AIThread(this);
		new Thread(ai).start();
	}

	public void actionPerformed(ActionEvent evt) { 
        String command = evt.getActionCommand();
        if (command.equals(view.getNewGameButtonText())) {
        	doNewGame();
        }
        else if (command.equals(view.getResignButtonText())) {
            doResign();
        }
    }
	
	public void doNewGame() {
		if (model.isGameStatus()) {
			view.setMessageText("Finish the current game first!");
			return;
		}
		model.spawn();
		model.setCurrentPlayer(Color.WHITE); 
		model.setSelectedRow(-1);  
		view.setMessageText("White:  Make your move.");
		model.setGameStatus(true);
		view.getNewGameButton().setEnabled(false);
		view.getResignButton().setEnabled(true);
		view.repaint();
	}
	
	void doResign() {
		if (!model.isGameStatus()) {
			view.setMessageText("There is no game in progress!");
		}
		if (model.getCurrentPlayer() == Color.WHITE) {
			gameOver("WHITE resigns. BLACK wins.");
		}
		else {
			gameOver("BLACK resigns. WHITE wins.");
		}
		model.setBoard(new Piece[ChessModel.ROWS][ChessModel.COLS]);
	}
	
	void gameOver(String text) {
		view.setMessageText(text);
		view.getNewGameButton().setEnabled(true);
		view.getResignButton().setEnabled(false);
    	model.setGameStatus(false);
	}
	
	/**
	 * If there was a selected piece already, check if this is legal move for it otherwise select a piece
	 * @param row
	 * @param col
	 */
	synchronized void doClickSquare(int row, int col) {
		if (!aiThinking) { 
			Piece piece = model.pieceAt(row, col);
			if (model.getSelectedRow() >= 0) { // If we already have a piece selected
				if (piece == null || piece.getColor() != model.getCurrentPlayer()) {
					ChessMove move = model.getSelectedLegalMove(row, col);
					if (move != null) {
						doMakeMove(move);
					}
					return;
				}
			}
			if (piece == null) {
				return;
			}
			if (piece.getColor() != model.getCurrentPlayer()) {
				return;
			}
			ChessMove[] legalMoves = model.getLegalMoves(row, col);
			if (legalMoves == null || legalMoves.length == 0) {
				if (model.isUnderCheck(model.getCurrentPlayer())) {
					view.setMessageText("You are under check and the piece you selected cannot block the check");
				} else {
					view.setMessageText("No legal moves for the piece you selected");
				}
				return;
			}
			model.setSelectedRow(row);
			model.setSelectedCol(col);
			model.setSelectedLegalMoves(legalMoves);
			view.setMessageText("Click the square you want to move to.");
			view.repaint();
		}
	}
	
	synchronized void doMakeMove(ChessMove move) {		
		model.makeMove(move);
		String colorString1 = model.getCurrentPlayer() == Color.WHITE ? Color.BLACK.name() : Color.WHITE.name();
		String colorString2 = model.getCurrentPlayer().name();
		if (model.isCheckMate(model.getCurrentPlayer())) {
			gameOver(colorString1 + " wins by checkmate.");			
		} else if (model.isStalemate(model.getCurrentPlayer())) {
			gameOver("Stalemate! There is no winner.");
		} else {
			if (model.isUnderCheck(model.getCurrentPlayer())) {
				view.setMessageText(colorString2 + ": Under check.");
			} else {
				view.setMessageText(colorString2 + ":  Make your move.");
			}
			// Let the AI decide on a move
			if (view.isAiMode()) {
				ai.calculate(model);
				aiThinking = true;
			}
		}
		model.setSelectedRow(-1);
		view.repaint();
	}
	
	synchronized void moveMade() {
		aiThinking = false;
		view.repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent evt) {
		if (!model.isGameStatus())
			view.setMessageText("Click \"New Game\" to start a new game.");
		else {
			int col = (evt.getX() - 2) / 20;
			int row = (evt.getY() - 2) / 20;
			if (col >= 0 && col < 8 && row >= 0 && row < 8) {      
				doClickSquare(row, col);
			}
		}
	}
	
	public void mouseClicked(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
	
}
