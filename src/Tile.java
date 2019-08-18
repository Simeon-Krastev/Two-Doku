import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.border.Border;

public class Tile {

	int xCoord;
	int yCoord;
	int color = 0;
	JLabel label;
	Border borderType;
	Domino dominoOnTile = null;
	final static Color emptyFieldColor = new Color(238,238,238);

	Tile(TwoDoku game, GUI gui, int x, int y, JLabel label, Border bordType) {
		this.xCoord = x;
		this.yCoord = y;
		this.label = label;
		this.borderType = bordType;
		this.label.setForeground(Color.ORANGE);
		addlistener(game, gui, this.label, this);
	}

	void setColor(int colorNumber) {
		Color color;
		switch (colorNumber) {
		case 0:
			color = emptyFieldColor;
			break;
		case 1:
			color = Color.RED;
			break;
		case 2:
			color = Color.BLUE;
			break;
		default:
			color = Color.MAGENTA;
			break;
		}
		this.color = colorNumber;
		this.label.setBackground(color);
	}

	void addlistener(TwoDoku game, GUI gui, JLabel label, Tile tile) {
		label.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt){

				if (game.stageOneCount<=35 && !label.getBackground().equals(Color.RED)) {
					label.setBackground(Color.RED);
					game.numberOfAvailableRedTiles++;
					Tile.this.color = 1;
					game.numberOfAvailableWhiteTiles--;
					game.stageOneCount++;
					gui.instruction.setText("<html>Player RED, please color squares on the field by<br>"
							+ "clicking on them. Squares left to color: " + Math.abs(game.stageOneCount-36) + ".</html>");
					if (game.stageOneCount == 36) gui.instruction.setText("<html>Player BLUE, please color "
							+ "squares on the field by<br>clicking on them. Squares "
							+ "left to color: " + (72-game.stageOneCount) + "</html>");
				}
				else if (game.stageOneCount>35 && game.stageOneCount<72 
						&& label.getBackground().equals(Color.RED)
						&& !label.getBackground().equals(Color.MAGENTA)) {
					label.setBackground(Color.MAGENTA);
					game.numberOfAvailableBlueTiles++;
					Tile.this.color = 3;
					game.stageOneCount++;
					gui.instruction.setText("<html>Player BLUE, please color squares on the field by<br>"
							+ "clicking on them. Squares left to color: " + (72-game.stageOneCount) + ".</html>");
				} else if (game.stageOneCount>35 && game.stageOneCount<72 && 
						!label.getBackground().equals(Color.BLUE)
						&& !label.getBackground().equals(Color.MAGENTA)) {
					label.setBackground(Color.BLUE);
					game.numberOfAvailableBlueTiles++;
					game.numberOfAvailableWhiteTiles--;
					Tile.this.color = 2;
					game.stageOneCount++;
					gui.instruction.setText("<html>Player BLUE, please color squares on the field by<br>"
							+ "clicking on them. Squares left to color: " + (72-game.stageOneCount) + ".</html>");
				}


				if (game.stageOneCount >= 72 && game.gameStage == 1) {
					gui.instruction.setText("<html>Player RED, please place four dominoes on white"
							+ "<br>fields by clicking on a domino and then on a square.</html>");
					game.wentThroughStage1 = true;
					game.gameStage = 2;
				}

				if (game.selectedDomino !=null && !game.lock && game.selectedDomino.movable) {

					switch (game.gameStage) {
					case 1:
						break;
					case 2: //Players placing their four dominoes on the white fields
						if (dominoOnTile==null && color == 0) {

							if (game.selectedDomino.currentPosition!=null) {
								game.selectedDomino.currentPosition.dominoOnTile = null;
							}
							game.selectedDomino.label.setBounds(5,5,20,20);
							label.add(game.selectedDomino.label);
							game.selectedDomino.currentPosition = Tile.this;
							game.selectedDomino.label.setBorder(null);
							game.selectedDomino.movable = false;
							dominoOnTile = game.selectedDomino;
							game.dominoesOnField++;
							game.addNumberToField(game, gui, tile, game.selectedDomino);
							game.selectedDomino = null;


							if (game.dominoesOnField == 4) {
								game.selectFourDominoes(game, gui);
								gui.instruction.setText("<html>Player BLUE, please place four dominoes on white"
										+ "<br>fields by clicking on a domino and then on a square.</html>");
							}
							if (game.dominoesOnField == 8) {
								game.gameStage = 3;
								game.wentThroughStage2 = true;
								gui.setupPassButton(gui, game);
								gui.redDominoesPanel.setVisible(true);
							}
						}
						break;
					case 3: //Game is setup and players get 40 dominoes.

						if (dominoOnTile == null) {

							if (game.selectedDomino.belongsToPlayer.equals(Color.RED)) {

								if (game.selectedDomino.belongsToPlayer.equals(Color.RED) && 
										(game.numberOfAvailableRedTiles > 0 && color != 1 &&
										color != 3) || (game.numberOfAvailableRedTiles == 0 &&
										game.numberOfAvailableWhiteTiles != 0 && color != 0)) {
									gui.instruction.setText("Wrong Color! Please click 'Done'.");
									game.lock = true;
									game.selectedDomino.label.setBorder(null);
									game.selectedDomino = null;
									gui.passButton.setText("Done");
									game.canTake = false;
									break;
								}		
							}

							if (game.selectedDomino.belongsToPlayer.equals(Color.BLUE)) {

								if ((game.numberOfAvailableBlueTiles > 0 && color != 2 &&
										color != 3) || (game.numberOfAvailableBlueTiles == 0 &&
										game.numberOfAvailableWhiteTiles != 0 && color != 0)) {
									gui.instruction.setText("Wrong Color! Please click 'Done'.");
									game.lock = true;
									game.selectedDomino = null;
									gui.passButton.setText("Done");
									game.canTake = false;
									break;
								}		
							}

							if (!game.addNumberToField(game, gui, tile, game.selectedDomino)) break;

							gui.instruction.setText("Domino Placed! Please click 'Done'.");
							game.lock = true;
							gui.passButton.setText("Done");
							game.canTake = false;

							switch (color) {
							case 1:
								game.numberOfAvailableRedTiles--;
								break;
							case 2:
								game.numberOfAvailableBlueTiles--;
								break;
							case 3: 
								game.numberOfAvailableRedTiles--;
								game.numberOfAvailableBlueTiles--;
								break;
							default:
								game.numberOfAvailableWhiteTiles--;
								break;
							}

							game.selectedDomino.label.setBounds(5,5,20,20);
							if (game.selectedDomino.belongsToPlayer.equals(Color.RED)) {
								game.redPot.remove(game.selectedDomino);
								game.availableSpotsRed.add(new int[] {game.selectedDomino.xCoord, 
										game.selectedDomino.yCoord});
							} else {
								game.bluePot.remove(game.selectedDomino);
								game.availableSpotsBlue.add(new int[] {game.selectedDomino.xCoord,
										game.selectedDomino.yCoord});
							}
							game.selectedDomino.belongsToPlayer = null;
							game.selectedDomino.movable = false;
							label.add(game.selectedDomino.label);
							game.selectedDomino.currentPosition = Tile.this;
							dominoOnTile = game.selectedDomino;
							game.dominoesOnField++;
						}
						break;
					case 4:
						break;
					}
				}
			}
		});
	}
}
