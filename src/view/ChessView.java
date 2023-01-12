package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import control.ChessController;
import model.ChessModel;
import model.ChessMove;
import model.PieceType;


public class ChessView extends JPanel {

	/*
	 * MVC Principle
	 * Reference to the Model and Controller
	 */

	private static final long serialVersionUID = 1L;

	private ChessModel model;
	private ChessController controller;

	private JButton newGameButton;
	private JButton resignButton;
	private JLabel message;
	
	private boolean aiMode;
	private PieceType promotion;

	static final int WIDTH = 350, HEIGHT = 250;

	public void setMessageText(String text) {
		this.message.setText(text);
	}

	public ChessView(ChessModel model) {

		this.model = model;
		this.controller = new ChessController(model, this);

		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		setBackground(new Color(0, 0, 70));

		resignButton = new JButton("Resign");
		resignButton.addActionListener(controller);

		newGameButton = new JButton("New Game");
		newGameButton.addActionListener(controller);

		message = new JLabel("",JLabel.CENTER);
		message.setFont(new  Font("Serif", Font.BOLD, 14));
		message.setForeground(Color.green);

		Chessboard board = new Chessboard();

		board.addMouseListener(controller);
		board.setBounds(20,20,164,164); // Note:  size MUST be 164-by-164 !
		newGameButton.setBounds(210, 60, 120, 30);
		resignButton.setBounds(210, 120, 120, 30);
		message.setBounds(0, 200, 350, 30);

		this.add(board, "Board");
		this.add(newGameButton, "New Game Button"); 
		this.add(resignButton, "Resign Button");
		this.add(message, "Message");

		aiOrNo();
		
	}
	
	private void aiOrNo() {
		Object[] options = {"No.", "Yes!"};
		int answer = JOptionPane.showOptionDialog(this, "Welcome! Would you like to play AI mode?",
				 "Chess", JOptionPane.YES_NO_OPTION,
				 JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (answer == 1) {
			aiMode = true;
		}
		else {
			aiMode = false;
		}
	}
	
	public void promotion() {
		Object[] options = {"Knight", "Bishop", "Rook", "Queen"};
		int answer = JOptionPane.showOptionDialog(this, "Select your pawn promotion.", 
				"Chess", JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (answer == 0) {
			promotion = PieceType.KNIGHT;
		}
		if (answer == 1) {
			promotion = PieceType.BISHOP;
		}
		if (answer == 2) {
			promotion = PieceType.ROOK;
		}
		if (answer == 3) {
			promotion = PieceType.QUEEN;
		}
	}

	private class Chessboard extends JPanel {

		private static final long serialVersionUID = 1L;

		Chessboard() {
			setBackground(Color.BLACK);
			controller.doNewGame();
		}

		public void paintComponent(Graphics g) {

			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g.setColor(Color.black);
			g.drawRect(0,0,getSize().width-1,getSize().height-1);
			g.drawRect(1,1,getSize().width-3,getSize().height-3);

			int rows = ChessModel.ROWS;
			int cols = ChessModel.COLS;

			for (int r=0; r<rows; r++) {
				for (int c=0; c<cols; c++) {
					if (r%2 == c%2) {
						g.setColor(Color.WHITE);
					}
					else {
						g.setColor(Color.GRAY);
					}
					g.fillRect(2+c*20, 2+r*20, 20, 20);
					if (model.pieceAt(r, c) != null) {
						switch(model.pieceAt(r, c).getType()) {
						case KING:
							if (model.getCurrentPieceInt(r, c) == 0) {
								g.drawImage(new ImageIcon("King.png").getImage(), 4 + c*20, 4 + r*20, 15, 15, null);
							}
							else {
								g.drawImage(new ImageIcon("BlackKing.png").getImage(), 4 + c*20, 4 + r*20, 15, 15, null);
							}
							break;
						case PAWN:
							if (model.getCurrentPieceInt(r, c) == 0) {
								g.drawImage(new ImageIcon("Pawn.png").getImage(), 4 + c*20, 4 + r*20, 15, 15, null);
							}
							else {
								g.drawImage(new ImageIcon("BlackPawn.png").getImage(), 4 + c*20, 4 + r*20, 15, 15, null);
							}
							break;
						case KNIGHT:
							if (model.getCurrentPieceInt(r, c) == 0) {
								g.drawImage(new ImageIcon("Knight.png").getImage(), 4 + c*20, 4 + r*20, 15, 15, null);
							}
							else {
								g.drawImage(new ImageIcon("BlackKnight.png").getImage(), 4 + c*20, 4 + r*20, 15, 15, null);
							}
							break;
						case BISHOP:
							if (model.getCurrentPieceInt(r, c) == 0) {
								g.drawImage(new ImageIcon("Bishop.png").getImage(), 4 + c*20, 4 + r*20, 15, 15, null);
							}
							else {
								g.drawImage(new ImageIcon("BlackBishop.png").getImage(), 4 + c*20, 4 + r*20, 15, 15, null);
							}
							break;
						case ROOK:
							if (model.getCurrentPieceInt(r, c) == 0) {
								g.drawImage(new ImageIcon("Rook.png").getImage(), 4 + c*20, 4 + r*20, 15, 15, null);
							}
							else {
								g.drawImage(new ImageIcon("BlackRook.png").getImage(), 4 + c*20, 4 + r*20, 15, 15, null);
							}
							break;
						case QUEEN:
							if (model.getCurrentPieceInt(r, c) == 0) {
								g.drawImage(new ImageIcon("Queen.png").getImage(), 4 + c*20, 4 + r*20, 15, 15, null);
							}
							else {
								g.drawImage(new ImageIcon("BlackQueen.png").getImage(), 4 + c*20, 4 + r*20, 15, 15, null);
							}
							break;
						default:
							break;
						}
					}
				}
			}
			if (model.isGameStatus()) {
				ChessMove[] legalMoves = model.getLegalMovesForPlayer(model.getCurrentPlayer());
				int selectedRow = model.getSelectedRow();
				int selectedCol = model.getSelectedCol();

				g.setColor(Color.CYAN);
				for (int i = 0; i < legalMoves.length; i++) {
					g.drawRect(2 + legalMoves[i].getInitialCol()*20, 2 + legalMoves[i].getInitialRow()*20, 19, 19);
					g.drawRect(3 + legalMoves[i].getInitialCol()*20, 3 + legalMoves[i].getInitialRow()*20, 17, 17);
				}

				if (selectedRow >= 0) {
					g.setColor(Color.white);
					g.drawRect(2 + selectedCol*20, 2 + selectedRow*20, 19, 19);
					g.drawRect(3 + selectedCol*20, 3 + selectedRow*20, 17, 17);
					g.setColor(Color.green);
					for (ChessMove move : model.getSelectedLegalMoves()) {
						g.drawRect(2 + move.getFinalCol()*20, 2 + move.getFinalRow()*20, 19, 19);
						g.drawRect(3 + move.getFinalCol()*20, 3 + move.getFinalRow()*20, 17, 17);
					}
				}
			}
			for (int c = 0; c < ChessModel.COLS; c++) {
				if (model.getTypeAt(0, c) == PieceType.PAWN) {
					promotion();
					model.doPromotion(0, c, promotion);
				}
				if (model.getTypeAt(ChessModel.ROWS-1, c) == PieceType.PAWN) {
					promotion();
					model.doPromotion(ChessModel.ROWS-1, c, promotion);
				}
			}
		}
	}

	// borrowed from RPGGame
	public JLabel createJLabelWithImageAndText(String fileName, String text, Color color, int fontSize) {
		JLabel label = null;  
		try {
			Image i = ImageIO.read(new File(fileName));
			label = new JLabel(new ImageIcon(i.getScaledInstance(WIDTH, HEIGHT, 0)));
		} catch (IOException e) {
			e.printStackTrace();
			label = new JLabel(text);
		}
		label.setFont(new Font("Courier", Font.BOLD, fontSize));
		label.setForeground(color);
		label.setText(text);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.CENTER);
		return label;
	}

	public JButton getNewGameButton() {
		return newGameButton;
	}

	public void setNewGameButton(JButton newGameButton) {
		this.newGameButton = newGameButton;
	}

	public JButton getResignButton() {
		return resignButton;
	}

	public void setResignButton(JButton resignButton) {
		this.resignButton = resignButton;
	}

	public String getNewGameButtonText() {
		return newGameButton.getText();
	}

	public String getResignButtonText() {
		return resignButton.getText();
	}
	
	public boolean isAiMode() {
		return aiMode;
	}

	public PieceType getPromotion() {
		return promotion;
	}

}
