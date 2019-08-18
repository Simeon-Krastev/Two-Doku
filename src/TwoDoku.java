import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
//System.out.println(Arrays.deepToString(field).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
public class TwoDoku implements Runnable{

	boolean lock = false;
	boolean canTake = true;
	boolean endGame = false;
	boolean wentThroughStage1 = false;
	boolean wentThroughStage2 = false;
	boolean wentThroughStage3 = false;
	int gameStage = 1;
	int passCount = 0;
	int stageOneCount = 0;
	int numberOfDominoesInPot = 90;
	int dominoesOnField = 0;
	int numberOfAvailableRedTiles = 0;
	int numberOfAvailableBlueTiles = 0;
	int numberOfAvailableWhiteTiles = 81;
	int playerRedSum = 0;
	int playerBlueSum = 0;
	int potSum = 0;	
	int[][] field = new int[9][9];
	String turn = "RED";
	Tile selectedTile = null;
	Tile wrongMoveOnTile = null;
	Domino selectedDomino = null;
	Domino conflictingDomino = null;
	ArrayList<int[]> availableSpotsRed = new ArrayList<int[]>();
	ArrayList<int[]> availableSpotsBlue = new ArrayList<int[]>();
	ArrayList<Integer> allDominoes = new ArrayList<Integer>();
	ArrayList<Integer> eightRandomDominoes = new ArrayList<Integer>();
	ArrayList<Integer> twoLeftOverDominoes = new ArrayList<Integer>();
	ArrayList<Integer> redDominoes = new ArrayList<Integer>();
	ArrayList<Integer> blueDominoes = new ArrayList<Integer>();
	ArrayList<Domino> redPot = new ArrayList<Domino>();
	ArrayList<Domino> bluePot = new ArrayList<Domino>();
	ArrayList<Domino> dominoesInPot = new ArrayList<Domino>();
	HashMap<Integer, Tile> tiles = new HashMap<Integer, Tile>();

	public TwoDoku() {

		new Thread (this).start();
	}

	@Override
	public void run() {

		GUI fieldGUI = new GUI(this);

		while (this.gameStage == 1) {try {
			Thread.sleep(200);
		} catch (InterruptedException e) {}
		}
		if (wentThroughStage1) {
		createDominoValues(this);
		selectFourDominoes(this, fieldGUI);
		createTwoLeftOverDominoes(this, fieldGUI);
		}
		while (this.gameStage == 2) {try {
			Thread.sleep(200);
		} catch (InterruptedException e) {}
		}
		if (wentThroughStage2)	createPlayerDominoes(this, fieldGUI);

		while (this.gameStage == 3) {try {
			Thread.sleep(200);
		} catch (InterruptedException e) {}
		}

		if (wentThroughStage3) gameOver(fieldGUI, this);

	}


	public static void main(String[] args) {

		TwoDoku game = new TwoDoku();

	}

	// This method generates the tile objects , dominoes and the labels on the field

	private void createDominoValues(TwoDoku game) {

		for (int i = 1; i <= 9; i++ ) {
			for (int j = 1; j<=10; j++ ) {
				game.allDominoes.add(i);
			}
		}

		Random randomNumber = new Random();

		for (int i = 1; i<=8; i++ ) {
			int randomDomino = randomNumber.nextInt(9)+1;
			game.eightRandomDominoes.add(randomDomino);
			game.allDominoes.remove(new Integer(randomDomino));
		}
		for (int i = 1; i<=2; i++ ) {
			int randomPosition = randomNumber.nextInt(game.allDominoes.size()-1)+1;
			int randNumber = game.allDominoes.get(randomPosition);
			game.twoLeftOverDominoes.add(randNumber);
			game.allDominoes.remove(randNumber);
		}
		int leftoversum = 0;
		for (int number : game.eightRandomDominoes) leftoversum += number;
		for (int number : game.twoLeftOverDominoes) leftoversum += number;
		leftoversum = 450 - leftoversum;
		int redSum = 0;
		int blueSum = 0;

		do {
			int number = game.allDominoes.get(0);
			game.redDominoes.add(number);
			redSum += number;
			game.allDominoes.remove(0);
			number = game.allDominoes.get(0);
			game.blueDominoes.add(number);
			blueSum += number;
			game.allDominoes.remove(0);
		} while (game.redDominoes.size() <= 39);

		int difference = Math.abs(blueSum - redSum);

		if (difference > 1) {
			int change = difference/2;
			if (blueSum > redSum) {
				game.redDominoes.remove(0);
				game.redDominoes.add(change+1);
				game.blueDominoes.add(1);
				game.blueDominoes.remove(new Integer(change+1));
			}
			else {
				game.blueDominoes.remove(0);
				game.blueDominoes.add(change+1);
				game.redDominoes.add(1);
				game.redDominoes.remove(new Integer(change+1));
			}

		}
	}

	private void createPlayerDominoes(TwoDoku game, GUI gui) {

		// Create dominoes
		JLayeredPane panelHolder;
		Color playerColor;
		ArrayList<Integer> dominoNumbersArrayHolder;
		ArrayList<Domino> dominoArrayHolder;
		for (int player = 1; player<=2 ; player++) {
			if (player == 1) {
				playerColor = Color.RED;
				panelHolder = gui.redDominoesPanel;
				dominoNumbersArrayHolder = game.redDominoes;
				dominoArrayHolder = game.redPot;
			}
			else {
				playerColor = Color.BLUE;
				panelHolder = gui.blueDominoesPanel;
				dominoNumbersArrayHolder = game.blueDominoes;
				dominoArrayHolder = game.bluePot;
			}

			for (int i = 0; i<=9; i++ ) {
				for (int j =0; j<=3; j++ ) {

					JLabel label = new JLabel("", JLabel.CENTER);
					label.setBackground(Color.BLACK);
					label.setOpaque(true);
					int x = (8)+(i*28);
					int y = 5+(j*22);
					label.setBounds(x, y, 20, 20);
					panelHolder.add(label);
					int position = (int)(Math.random()*dominoNumbersArrayHolder.size());
					int value = dominoNumbersArrayHolder.get(position);
					dominoNumbersArrayHolder.remove(new Integer(value));
					label.setForeground(Color.WHITE);
					label.setText(""+value);
					Domino dom = new Domino(game, gui, x, y, value, label);
					dom.belongsToPlayer = playerColor;
					dominoArrayHolder.add(dom);
					game.numberOfDominoesInPot--;
					gui.potText.setText("Dominoes in the pot: " + game.numberOfDominoesInPot);
				}
			}
		}
	}

	void createTwoLeftOverDominoes(TwoDoku game, GUI gui) {

		for (int i = 0; i <= 1; i++ ) {
			int value = game.twoLeftOverDominoes.get(i);
			JLabel label = new JLabel("", JLabel.CENTER);
			label.setBackground(Color.BLACK);
			label.setVisible(false);
			label.setOpaque(true);
			gui.tilePanel.add(label);
			label.setBounds(0, 0, 20, 20);
			label.setForeground(Color.WHITE);
			label.setText(""+value);
			Domino dom = new Domino(game, gui, 0, 0, value, label);
			game.dominoesInPot.add(dom);
		}
	}

	void selectFourDominoes(TwoDoku game, GUI gui) {

		Color playerColor = Color.RED;

		for (int i = 0; i <= 3; i++ ) {

			if (game.eightRandomDominoes.size() == 4) {
				playerColor = Color.BLUE;
				gui.instruction.setText("<html>Player BLUE, please place four dominoes on white"
						+ "<br>fields by clicking on a Domino and then on a square.</html>");
			}

			JLabel label = new JLabel("", JLabel.CENTER);
			label.setBackground(Color.BLACK);
			label.setForeground(Color.WHITE);
			label.setOpaque(true);
			int x = (20)+(i*70);
			int y = 365;
			label.setBounds(x, y, 40, 40);

			gui.tilePanel.add(label);

			int value = game.eightRandomDominoes.get(0);
			game.eightRandomDominoes.remove(0);
			label.setText("");
			game.numberOfDominoesInPot--;
			gui.potText.setText("Dominoes in the pot: " + game.numberOfDominoesInPot);
			Domino dom = new Domino(game, gui, x, y, value, label);
			dom.belongsToPlayer = playerColor;
		}

	}

	public boolean addNumberToField(TwoDoku game, GUI gui, Tile tilePlaced, Domino dominoPlaced) {
		int xCoord = tilePlaced.xCoord;
		int yCoord = tilePlaced.yCoord;

		if (game.gameStage > 2 && !checkField(game, dominoPlaced, xCoord, yCoord)) {
			gui.instruction.setText("Invalid Move! Please click 'Done'.");
			game.passCount++;
			gui.passButton.setText("Done");
			game.lock = true;
			game.canTake = false;
			return false;
		}

		game.field[xCoord][yCoord] = dominoPlaced.value;

		return true;
	}

	void removeNumberFromField(GUI gui, TwoDoku game, Tile removedFromTile) {
		int xCoord = removedFromTile.xCoord;
		int yCoord = removedFromTile.yCoord;
		removedFromTile.dominoOnTile = null;
		game.selectedDomino.currentPosition = null;
		gui.tilePanel.remove(game.selectedDomino.label);
		switch (removedFromTile.color) {
		case 0:
			game.numberOfAvailableWhiteTiles++;
			break;
		case 1:
			game.numberOfAvailableRedTiles++;
			break;
		case 2:
			game.numberOfAvailableBlueTiles++;
			break;
		case 3:
			game.numberOfAvailableRedTiles++;
			game.numberOfAvailableBlueTiles++;
			break;
		}
		game.field[xCoord][yCoord] = 0;
	}

	public boolean checkField(TwoDoku game, Domino dominoPlaced, int row, int column) {
		int number = dominoPlaced.value;
		for (int i = 0; i < 9; i++) {

			if (number == game.field[i][column]) {
				Domino domino = game.tiles.get(i*10+column).dominoOnTile;
				if (domino.label.getText().equals("")) {
					domino.label.setText(""+domino.value);

				}
				game.conflictingDomino = domino;
				game.conflictingDomino.label.setBackground(Color.ORANGE);
				game.wrongMoveOnTile = game.tiles.get(row*10+column);
				game.wrongMoveOnTile.label.setText(""+dominoPlaced.value);
				return false;
			}

			if (number == game.field[row][i]) {
				Domino domino = game.tiles.get(row*10+i).dominoOnTile;
				if (domino.label.getText().equals("")) {
					domino.label.setText(""+domino.value);

				}
				game.conflictingDomino = domino;
				game.conflictingDomino.label.setBackground(Color.ORANGE);
				game.wrongMoveOnTile = game.tiles.get(row*10+column);
				game.wrongMoveOnTile.label.setText(""+dominoPlaced.value);
				return false;

			}

			if (game.neighborCheck(game, number, (row/3)*3, (column/3)*3)!=null) {
				int wrongRow = (row/3)*3 + game.neighborCheck(game, number, (row/3)*3, (column/3)*3)[0];
				int wrongColumn = (column/3)*3 + game.neighborCheck(game, number, (row/3)*3, (column/3)*3)[1];
				Domino domino = game.tiles.get(wrongRow*10+wrongColumn).dominoOnTile;
				if (domino.label.getText().equals("")) {
					domino.label.setText(""+domino.value);

				}
				game.conflictingDomino = domino;
				game.conflictingDomino.label.setBackground(Color.ORANGE);
				game.wrongMoveOnTile = game.tiles.get(row*10+column);
				game.wrongMoveOnTile.label.setText(""+dominoPlaced.value);
				return false;
			}
		}
		return true;
	}

	public int[] neighborCheck(TwoDoku game, int number, int row, int column) {
		if  (number == game.field[row][column])  return new int[]{0,0};
		if	(number == game.field[row][column+1])  return new int[]{0,1};
		if	(number == game.field[row][column+2])  return new int[]{0,2};
		if	(number == game.field[row+1][column])  return new int[]{1,0};
		if	(number == game.field[row+1][column+1])  return new int[]{1,1};
		if	(number == game.field[row+1][column+2])  return new int[]{1,2};
		if	(number == game.field[row+2][column])  return new int[]{2,0};
		if	(number == game.field[row+2][column+1])  return new int[]{2,1};
		if	(number == game.field[row+2][column+2]) return new int[]{2,2};
		return null;
	}

	void gameOver(GUI gui, TwoDoku game) {
		String player;
		this.lock = true;
		this.endGame = true;
		if (game.redPot.size()!=0) {
			for (Domino dom : game.redPot) {
				game.playerRedSum = game.playerRedSum + dom.value;
			}
		}
		if (game.bluePot.size()!=0) {
			for (Domino dom : game.bluePot) {
				game.playerBlueSum = game.playerBlueSum + dom.value;
			}
		}  
		if (game.dominoesInPot.size()!=0) {
			for (Domino dom : game.dominoesInPot) {
				game.potSum = game.potSum + dom.value;
			}
		}

		int RedScore = game.playerBlueSum + game.potSum;
		int BlueScore = game.playerRedSum + game.potSum;

		if (RedScore>BlueScore) {
			player = "Player RED wins! Congratulations!";
			gui.instruction.setText("Game Over! Player RED wins!");
		} else if (RedScore<BlueScore) {
			player = "Player Blue wins! Congratulations!";
			gui.instruction.setText("Game Over! Player BLUE wins!");
		} else {
			player = "It's a tie!";
			gui.instruction.setText("Game Over! It's a tie!");
		}
		
		JOptionPane.showMessageDialog(null, "Game Over! Here are the results:\n\n"
				+ "Player RED:          " + RedScore + " pts."
				+ "\nPlayer BLUE:        " + BlueScore + " pts.\n\n" + player);
	}



}
