import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

public class GUI extends JFrame {

	static JLayeredPane panel = new JLayeredPane();
	JLayeredPane redDominoesPanel = new JLayeredPane();
	JLayeredPane blueDominoesPanel = new JLayeredPane();
	JLayeredPane tilePanel = new JLayeredPane();

	JLabel instruction = new JLabel();
	JLabel potImage = new JLabel();
	JLabel potText = new JLabel();
	JButton passButton = new JButton("Pass");

	public final static int frameWidth = 310;
	public final static int frameHeight = 550;

	final static Border selectBorder = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.YELLOW);
	final static Border selectBorderRED = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED);
	final static Border selectBorderBLUE = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE);
	final static Color emptyFieldColor = new Color(238,238,238);
	final static Font boldFont = new Font("Arial", Font.BOLD, 18);

	public GUI(TwoDoku game) {

		instruction.setText("<html>Player RED, please color squares on the field by<br>"
				+ "clicking on them. Squares left to color: " + Math.abs(game.stageOneCount-36) + ".</html>");

		potText.setText("Dominoes in the pot: " + game.numberOfDominoesInPot);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		tilePanel.setBounds(5, 35, frameWidth-18, 450);
		panel.setBounds(0, 0, frameWidth, frameHeight);
		tilePanel.setBorder(BorderFactory.createTitledBorder(
				"TwoDoku"));
		setTitle("TwoDoku");

		panel.setLayout(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().add(panel);
		panel.setOpaque(true);
		panel.add(tilePanel);
		redDominoesPanel.setLayout(null);
		blueDominoesPanel.setLayout(null);
		redDominoesPanel.setBounds(2, 350, 288, 96);
		blueDominoesPanel.setBounds(2, 350, 288, 96);
		redDominoesPanel.setVisible(false);
		blueDominoesPanel.setVisible(false);
		redDominoesPanel.setBorder(selectBorderRED);
		blueDominoesPanel.setBorder(selectBorderBLUE);
		redDominoesPanel.setOpaque(true);
		blueDominoesPanel.setOpaque(true);
		tilePanel.add(redDominoesPanel);
		tilePanel.add(blueDominoesPanel);
		tilePanel.setOpaque(true);

		instruction.setBounds(7, 470, 400, 60); //27, frameHeight-154, 500, 30
		panel.add(instruction);

		generateTiles(game, this, tilePanel);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, frameWidth, 30);
		JMenu menuGame = new JMenu("Game");
		menuBar.add(menuGame);
		JMenu menuHelp = new JMenu("Help");
		menuBar.add(menuHelp);

		// How to Play Menu
		JMenuItem howToPlay = new JMenuItem("How To Play");
		menuHelp.add(howToPlay);
		howToPlay.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				JOptionPane.showMessageDialog(null, "* The basic idea of the game is that there are two players (red and blue) and an empty Sudoku board and “dominos” with one digit on them with the numbers 1 to 9.  Because there are 81 squares on the board (9x9),\n"
						+ "there are 90 dominoes (10x9)—that way at the end there are at least 9 dominoes (1-9) that can’t be placed.\r\n" + 
						"* At the start of the game, the players take turns coloring squares (36 of them per player) to indicate which squares they intend to fill in.  It is ok for a square to have either more than one color (i.e. both players want to\nfill it in) or no color.  The players when playing their dominos will fill in the squares with their color on them first.\r\n" + 
						"* After the squares are colored, the players each are given 4 random dominos that they cannot see and place those face (number side) down on the board on squares that are not yet colored. Note that these dominos\ndo not need to follow the rules of Sudoku, e.g. two of the face down dominos might have the same number but be in the same row, column, or 3x3 square.\r\n" + 
						"* After placing the random dominos, the players each get 40 random dominos of their own, with 2 dominos being left over in the pot. The computer will make certain that the random dominos of each player add up to\nthe same number.\r\n" + 
						"* At this point, the game is setup and the main part of play begins, with the players alternating turns.\r\n" + 
						" \r\n" + 
						"Each turn consists of:\r\n" + 
						"1)  Optionally removing a domino from the board (or from the pot) and adding to the players own pile.\r\n" + 
						"2)  Playing a domino onto the board from their pile.  \r\n" + 
						"   a.  The domino played must obey the rules of Sudoku and not cause the row, column, or 3x3 square to have a duplicate number in it.\r\n" + 
						"         i.    If the domino played violates the rules because of dominos visible on the board, no other dominos are revealed\r\n" + 
						"         ii.   However, if the domino played is only in conflict with a face down domino (i.e. one of the ones originally played on white squares), the computer will reveal that face down domino.\r\n" + 
						"         iii.  If the domino played is in conflict with more than one face down domino, only one of those dominos is revealed (the computer will pick which one randomly from the conflicting ones).\r\n" + 
						"   b.  The player must also play their dominos only on squares they have colored in as long as there are those squares left.  Once the player has filled in all of their colored squares, they may next fill in white squares.\n"
						+ "        Finally, if the player has filled in all the squares marked with their own color and all the white squares, the player may play dominos onto the squares marked with the other players color.\r\n" + 
						"   c.  If the players domino breaks the rules of Sudoku or is played onto a square not properly colored, the domino is returned to the players pile and the player forfeits the right to play a domino that turn. \r\n" + 
						"3)  If the player has removed a domino from the pot and not placed a domino on the board, the player may put a domino from the players own pile back into the pot.\r\n" + 
						"4)  Or doing none of the above and “passing”.\r\n" + 
						" \r\n" + 
						"The game ends when one players pile is empty or when both players have not successfully played a domino onto the board (either by violating the rules above or by passing).  Note that the players can create unsolved\nSudoku puzzles where there are empty spots left, but no valid plays.  In fact, this should be common as the goal is not to solve the puzzle,  but to leave the other player with more valuable dominos than one has oneself.\r\n" + 
						" \r\n" + 
						"When the game is over, the dominos left in each players pile are added up.  The player with the lower score wins (with a score equal to his opponents score plus the dominos left in the pot).\r\n" + 
						" \r\n" + 
						"That’s it.");
			}
		});

		// New Game Option
		JMenuItem newGame = new JMenuItem("New Game");
		menuGame.add(newGame);
		newGame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt){
				JDialog.setDefaultLookAndFeelDecorated(true);
				int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to start a "
						+ "new game?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
				} else if (response == JOptionPane.YES_OPTION) {
					dispose();
					TwoDoku game1 = new TwoDoku();
				} else if (response == JOptionPane.CLOSED_OPTION) {
				}

			}
		});

		// Save Game Option
		JMenuItem saveGame = new JMenuItem("Save Game");
		menuGame.add(saveGame);
		saveGame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt){
				SaveAndLoad.save(game, GUI.this, panel);
			}
		});
		
		// Load Game Option
		JMenuItem loadGame = new JMenuItem("Load Game");
		menuGame.add(loadGame);
		loadGame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt){
				SaveAndLoad.load(game, GUI.this);
			}
		});

		// Exit
		JMenuItem exit = new JMenuItem("Exit");
		menuGame.add(exit);
		exit.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt){
				System.exit(0);		
			}
		});
		panel.add(menuBar);

		// Pot		
		potText.setOpaque(true);
		potText.setBounds(10,312,210,20);
		potText.setFont(new Font("Arial", Font.BOLD, 18));
		tilePanel.add(potText);

		// Pot Image
		ImageIcon imageIcon = new ImageIcon(new ImageIcon("pouch.png").getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		potImage.setIcon(imageIcon);
		potImage.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		potImage.setOpaque(true);
		potImage.setIcon(imageIcon);
		potImage.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				if (game.gameStage == 3) {

					if (game.selectedDomino != null && !game.lock && !game.canTake && game.selectedDomino.currentPosition == null) {
						ArrayList<int[]> playerAvailableSpots;
						ArrayList<Domino> playerPot;
						JLayeredPane playerPanel;

						if (game.turn.equals("RED")) {
							playerPot = game.redPot;
							playerPanel = redDominoesPanel;
							playerAvailableSpots = game.availableSpotsRed;

						} else {
							playerPot = game.bluePot;
							playerPanel = blueDominoesPanel;
							playerAvailableSpots = game.availableSpotsBlue;
						}


						game.dominoesInPot.add(game.selectedDomino);
						game.selectedDomino.belongsToPlayer = null;
						game.selectedDomino.label.setBounds(0,0,0,0);
						game.selectedDomino.label.setVisible(false);
						game.selectedDomino.label.repaint();
						game.selectedDomino.movable = false;
						game.numberOfDominoesInPot++;
						potText.setText("Dominoes in the pot: " + game.numberOfDominoesInPot);
						playerAvailableSpots.add((new int[] {game.selectedDomino.xCoord, 
								game.selectedDomino.yCoord}));
						game.lock = true;
						game.canTake = false;
						instruction.setText("Domino returned. Please click 'Done'");
						passButton.setText("Done");
						game.selectedDomino.xCoord = 0;
						game.selectedDomino.yCoord = 0;
						playerPot.remove(game.selectedDomino);
						playerPanel.remove(game.selectedDomino.label);
						game.selectedDomino = null;
						return;
					}
					if (!game.lock && game.canTake) {


						if (game.dominoesInPot.size() == 0) {
							instruction.setText("Pot is Empty!");
							return;
						}
						passButton.setText("Take");
						instruction.setText("Click on 'Take' to take domino.");
						Domino randomPotDomino = game.dominoesInPot.get((int)Math.random()*game.dominoesInPot.size());
						potImage.setBorder(selectBorder);
						if (game.selectedDomino != null && game.selectedDomino.equals(randomPotDomino)) {
							game.selectedDomino.label.setBorder(null);
							game.selectedDomino = null;
						} else if (game.selectedDomino!=null) {
							game.selectedDomino.label.setBorder(null);
							game.selectedDomino = randomPotDomino;
						} else game.selectedDomino = randomPotDomino;
					}
				} else {}
			}
		});
		potImage.setBounds(235,300,45,45);
		tilePanel.add(potImage);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}


	@Override
	public Dimension getPreferredSize() {
		return new Dimension(frameWidth, frameHeight);
	}

	void generateTiles(TwoDoku game, GUI gui, JLayeredPane tilePanel) {
		for (int i = 0; i <= 8; i++ ) {
			for (int j = 0; j <= 8; j++ ) {

				JLabel label = new JLabel("", JLabel.CENTER);
				label.setOpaque(true);

				// Borders
				final Border normalBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
				final Border rightBlack = BorderFactory.createMatteBorder(1, 1, 1, 3, Color.BLACK);
				final Border bottomBlack = BorderFactory.createMatteBorder(1, 1, 3, 1, Color.BLACK);
				final Border cornerBlack = BorderFactory.createMatteBorder(1, 1, 3, 3, Color.BLACK);

				label.setBorder(normalBorder);
				if (i==2 || i==5) label.setBorder(rightBlack);
				if (j==2 || j==5) label.setBorder(bottomBlack);
				if ((i==2 || i==5) && (j==2 || j==5)) label.setBorder(cornerBlack);

				// Create and add tile to Map
				Tile x = new Tile(game, gui, i, j, label, label.getBorder());
				game.tiles.put((i*10+j), x);

				// Add labels
				tilePanel.add(label);
				label.setBounds((frameWidth-288)/2+(i*30), 25+(j*30), 30, 30);
			}
		}

	}

	public void setupPassButton(GUI gui, TwoDoku game) {
		passButton.setBounds(220, 487, 70, 30);
		passButton.setVisible(true);
		panel.add(passButton);
		instruction.setBounds(7, 473, 220, 60);
		instruction.setText("It's player RED's turn.");
		passButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				if (!game.endGame && game.selectedDomino != null) game.selectedDomino.label.setBorder(null);
				potImage.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

				if (!game.endGame && passButton.getText().equals("Take")) {
					game.selectedDomino.label.setBorder(null);
					if (game.turn.equals("RED")) {
						if (game.redPot.size() == 40) {
							instruction.setText("Sorry, your pot is full!");
							passButton.setText("Pass");
						}
						else {
							int xCoord = game.availableSpotsRed.get(0)[0];
							int yCoord = game.availableSpotsRed.get(0)[1];
							game.availableSpotsRed.remove(game.availableSpotsRed.get(0));

							if (game.selectedDomino.currentPosition != null)	{
								game.removeNumberFromField(gui, game, game.selectedDomino.currentPosition);
								game.selectedDomino.currentPosition = null;
								passButton.setText("Done");
								instruction.setText("Domino taken. Please click 'Done'.");
								game.lock = true;
							} else { 
								instruction.setText("Domino taken. You can play or pass.");
								passButton.setText("Pass");
								game.dominoesInPot.remove(game.selectedDomino);
								game.numberOfDominoesInPot--;
								potText.setText("Dominoes in the pot: " + game.numberOfDominoesInPot);
							}
							game.selectedDomino.label.setBounds(xCoord,yCoord,20,20);
							game.selectedDomino.xCoord = xCoord;
							game.selectedDomino.yCoord = yCoord;
							game.selectedDomino.label.setVisible(true);
							game.selectedDomino.belongsToPlayer = Color.RED;
							game.redPot.add(game.selectedDomino);
							game.selectedDomino.movable = true;
							redDominoesPanel.add(game.selectedDomino.label);
							game.canTake = false;
							passButton.setText("Pass");
						}
					} else {
						if (game.bluePot.size() == 40) {
							instruction.setText("Sorry, your pot is full!");
							passButton.setText("Pass");
						}
						else {
							int xCoord = game.availableSpotsBlue.get(0)[0];
							int yCoord = game.availableSpotsBlue.get(0)[1];
							game.availableSpotsBlue.remove(game.availableSpotsBlue.get(0));

							if (game.selectedDomino.currentPosition != null)	{
								game.removeNumberFromField(gui, game, game.selectedDomino.currentPosition);
								game.selectedDomino.currentPosition = null;
								passButton.setText("Done");
								instruction.setText("Domino taken. Please click 'Done'.");
								game.lock = true;
							} else { 
								instruction.setText("Domino taken. You can play or pass.");
								passButton.setText("Pass");
								game.dominoesInPot.remove(game.selectedDomino);
								game.numberOfDominoesInPot--;
								potText.setText("Dominoes in the pot: " + game.numberOfDominoesInPot);
							}

							game.selectedDomino.label.setBounds(xCoord,yCoord,20,20);
							game.selectedDomino.xCoord = xCoord;
							game.selectedDomino.yCoord = yCoord;
							game.selectedDomino.label.setVisible(true);
							game.selectedDomino.belongsToPlayer = Color.BLUE;
							game.bluePot.add(game.selectedDomino);
							game.selectedDomino.movable = true;
							blueDominoesPanel.add(game.selectedDomino.label);
							instruction.setText("Domino taken! You can play or pass.");
							game.canTake = false;
							passButton.setText("Pass");
						}
					}
					game.selectedDomino = null;
					return;
				}
				game.selectedDomino = null;
				if (!game.endGame && (passButton.getText().equals("Done") || passButton.getText().equals("Pass"))) {
					if (passButton.getText().equals("Done")) {
						passButton.setText("Pass");
						if (game.passCount >= 0) game.passCount--;
					}
					else game.passCount++;

					game.lock = false;

					if (game.conflictingDomino != null) {
						game.conflictingDomino.label.setBackground(Color.BLACK);
						game.conflictingDomino = null;
					}
					if (game.wrongMoveOnTile != null) {
						game.wrongMoveOnTile.label.setText("");
						game.wrongMoveOnTile = null;
					}

					if (game.turn.equals("RED")) {
						game.turn = "BLUE";
						game.canTake = true;
						redDominoesPanel.setVisible(false);
						blueDominoesPanel.setVisible(true);
						instruction.setText("It's player BLUE's turn.");

					}
					else {
						game.turn = "RED";
						game.canTake = true;
						blueDominoesPanel.setVisible(false);
						redDominoesPanel.setVisible(true);
						instruction.setText("It's player RED's turn.");
					}
				}

				if (!game.endGame && (game.redPot.size() == 0 || game.bluePot.size() == 0 || game.passCount >= 4)) {
					game.wentThroughStage3 = true;
					game.gameStage = 4;
				}
			}
		});
	}
	
}
