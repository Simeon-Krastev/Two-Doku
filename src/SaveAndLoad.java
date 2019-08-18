import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

public class SaveAndLoad {

	private static SimpleFileChooser fileChooser;

	public static void save(TwoDoku game, GUI gui, JLayeredPane panel) {
		fileChooser = new SimpleFileChooser();
		File selectedFile = fileChooser.getOutputFile();
		if (selectedFile == null) return;
		if (selectedFile.exists()) {  // Ask the user whether to replace the file.
			int response = JOptionPane.showConfirmDialog( panel,
					"The file \"" + selectedFile.getName()
					+ "\" already exists.\nDo you want to replace it?", 
					"Confirm Save",
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.WARNING_MESSAGE );
			if (response != JOptionPane.YES_OPTION)
				return;
		}
		PrintWriter out;
		try {
			out = new PrintWriter( selectedFile );
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(panel,
					"Sorry, but an error occurred while trying to open the file:\n" + e);
			e.printStackTrace(System.out);
			return;
		}
		try {
			if (game.tiles.size()!=0) {
				for (int i = 0 ; i <= 8 ; i ++) {

					for (int j = 0 ; j <= 8 ; j++) {

						out.print("Tile " + (i*10+j) + " color = " + game.tiles.get(i*10+j).color +
								" domino = " + Boolean.toString(game.tiles.get(i*10+j).dominoOnTile!=null));
						if (game.tiles.get(i*10+j).dominoOnTile!=null) {
							out.print(" value = " + game.tiles.get(i*10+j).dominoOnTile.value +
									" visible = " + Boolean.toString(!game.tiles.get(i*10+j).dominoOnTile.label.getText().equals("")) + "\n");
						} else out.print("\n");
					}
				}
			}
			out.println("\nlock = " + game.lock);
			out.println("canTake = " + game.canTake);
			out.println("endGame = " + game.endGame);
			out.println("gameStage = " + game.gameStage);
			out.println("passButtonText = " + gui.passButton.getText());
			out.println("passCount = " + game.passCount);
			out.println("stageOneCount = " + game.stageOneCount);
			out.println("numberOfDominoesInPot = " + game.numberOfDominoesInPot);
			out.println("dominoesOnField = " + game.dominoesOnField);
			out.println("numberOfAvailableRedTiles = " + game.numberOfAvailableRedTiles);
			out.println("numberOfAvailableBlueTiles = " + game.numberOfAvailableBlueTiles);
			out.println("numberOfAvailableWhiteTiles = " + game.numberOfAvailableWhiteTiles);
			out.println("playerRedSum = " + game.playerRedSum);
			out.println("playerBlueSum = " + game.playerBlueSum);
			out.println("potSum = " + game.potSum);
			out.println("instruction = " + gui.instruction.getText());
			out.println("field = \n" + Arrays.deepToString(game.field).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
			out.println("turn = " + game.turn);
			out.println("availableSpotsRedSize = " + game.availableSpotsRed.size());
			for (int i = 0 ; i< game.availableSpotsRed.size(); i ++ ) {
				out.println(game.availableSpotsRed.get(i)[0] + " " + game.availableSpotsRed.get(i)[1]);
			}
			out.println("availableSpotsBlueSize = " + game.availableSpotsBlue.size());
			for (int i = 0 ; i< game.availableSpotsBlue.size(); i ++ ) {
				out.println(game.availableSpotsBlue.get(i)[0] + " " + game.availableSpotsBlue.get(i)[1]);
			}
			out.println("redPotSize = " + game.redPot.size());
			for (Domino domino : game.redPot) {
				out.println(domino.value + " " + domino.xCoord + " " + domino.yCoord);
			}
			out.println("bluePotSize = " + game.bluePot.size());
			for (Domino domino : game.bluePot) {
				out.println(domino.value + " " + domino.xCoord + " " + domino.yCoord);
			}

			out.flush();
			out.close();
			if (out.checkError())   // (need to check for errors in PrintWriter)
				throw new IOException("Error occurred while trying to write file.");
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(panel,
					"Sorry, but an error occurred while trying to write the data:\n" + e);
			e.printStackTrace(System.out);
		}
	}

	public static void load(TwoDoku game, GUI gui) {

		fileChooser = new SimpleFileChooser();
		File selectedFile = fileChooser.getInputFile();
		if (selectedFile == null) return;

		try { // If an issue occurs here, changes are not applied.
			Scanner in = new Scanner(selectedFile);
			in.nextLine();
			int tileNumber;
			int tileColor;
			do {
				in.next();
				tileNumber = in.nextInt();
				read(in, 2);
				tileColor = in.nextInt();
				read(in, 2);
				if (in.nextBoolean()) {
					JLabel label = new JLabel("", JLabel.CENTER);
					label.setBackground(Color.BLACK);
					label.setForeground(Color.WHITE);
					label.setOpaque(true);
					read(in, 2);
					int value = in.nextInt();
					read(in, 2);
					if (in.nextBoolean()) label.setText(""+value);
					Domino domino = new Domino(game, gui, tileNumber/10, 
							tileNumber%10, value, label);
					
					game.tiles.get(tileNumber).label.add(domino.label);
					domino.label.setBounds(5,5,20,20);
				}
				Tile tile = game.tiles.get(tileNumber);
				tile.setColor(tileColor);
				in.nextLine();
				if (tileNumber == 88) break;
			} while (in.hasNext());
			read(in, 2);
			game.lock = in.nextBoolean();
			read(in, 2);
			game.canTake = in.nextBoolean();
			read(in, 2);
			game.endGame = in.nextBoolean();
			read(in, 2);
			game.gameStage = in.nextInt();
			switch (game.gameStage) {
			case 1:
			break;
			case 2:
				game.wentThroughStage1 = false;
			case 3:
				game.wentThroughStage1 = false;
				game.wentThroughStage2 = false;
			}
			read(in, 2);
			GUI.panel.remove(gui.passButton);
			gui.setupPassButton(gui, game);
			gui.passButton.setText(in.next());
			read(in, 2);
			game.passCount = in.nextInt();
			read(in, 2);
			game.stageOneCount = in.nextInt();
			read(in, 2);
			game.numberOfDominoesInPot = in.nextInt();
			read(in, 2);
			game.dominoesOnField = in.nextInt();
			read(in, 2);
			game.numberOfAvailableRedTiles = in.nextInt();
			read(in, 2);
			game.numberOfAvailableBlueTiles = in.nextInt();
			read(in, 2);
			game.numberOfAvailableWhiteTiles = in.nextInt();
			read(in, 2);
			game.playerRedSum = in.nextInt();
			read(in, 2);
			game.playerBlueSum = in.nextInt();
			read(in, 2);
			game.potSum = in.nextInt();	
			in.nextLine();
			gui.instruction.setText(in.nextLine().substring(14));
			in.nextLine();
			Read2DArrayFromFile(in, game.field);
			read(in, 2);
			game.turn = in.next();
			read(in, 2);
			game.availableSpotsRed.clear();
			int redSpotsAvailable = in.nextInt();
			for (int i = 0 ; i < redSpotsAvailable ; i ++) {
				int number1 = in.nextInt();
				int number2 = in.nextInt();
				game.availableSpotsRed.add(new int[] {number1, number2});
				
			}
			read(in, 2);
			game.availableSpotsBlue.clear();
			int blueSpotsAvailable = in.nextInt();
			for (int i = 0 ; i < blueSpotsAvailable ; i ++) {
				int number1 = in.nextInt();
				int number2 = in.nextInt();
				game.availableSpotsBlue.add(new int[] {number1, number2});
			}
			read(in, 2);
			int redPotSize = in.nextInt();
			game.redPot.clear();
			game.redPot = new ArrayList<Domino>();
			gui.redDominoesPanel.removeAll();
			for (int i = 0 ; i < redPotSize ; i++) {
				int value = in.nextInt();
				int xCoord = in.nextInt();
				int yCoord = in.nextInt();
				
				JLabel label = new JLabel("", JLabel.CENTER);
				label.setBackground(Color.BLACK);
				label.setForeground(Color.WHITE);
				label.setOpaque(true);
				label.setText(""+value);
				Domino domino = new Domino(game, gui, xCoord, 
						yCoord, value, label);
				game.redPot.add(domino);
				domino.belongsToPlayer = Color.RED;
				gui.redDominoesPanel.add(domino.label);
				domino.label.setBounds(xCoord,yCoord,20,20);
			}
			read(in, 2);
			int bluePotSize = in.nextInt();
			game.bluePot.clear();
			game.bluePot = new ArrayList<Domino>();
			gui.blueDominoesPanel.removeAll();
			for (int i = 0 ; i < bluePotSize ; i++) {
				int value = in.nextInt();
				int xCoord = in.nextInt();
				int yCoord = in.nextInt();
				
				JLabel label = new JLabel("", JLabel.CENTER);
				label.setBackground(Color.BLACK);
				label.setForeground(Color.WHITE);
				label.setOpaque(true);
				label.setText(""+value);
				Domino domino = new Domino(game, gui, xCoord, 
						yCoord, value, label);
				game.bluePot.add(domino);
				domino.belongsToPlayer = Color.BLUE;
				gui.blueDominoesPanel.add(domino.label);
				domino.label.setBounds(xCoord,yCoord,20,20);
			}
			
			
			
			
			
			
			in.close();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(GUI.panel,
					"Sorry, but an error occurred while trying to open the file:\n" + e);
			e.printStackTrace(System.out);
		} 

	}

	public static void Read2DArrayFromFile(Scanner in, int[][] field){
		for (int i = 0 ; i <= 8 ; i++) {
			String line = in.nextLine();
			for (int j = 1 ; j <= 25 ; j += 3 ) {
				field[i][j/3] = Integer.parseInt("" + line.charAt(j));
			}
		}
	}

	static void read(Scanner in, int i) {
		for (int a = 1; a <= i; a++) {
			in.next();
		}
	}
}
