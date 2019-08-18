import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Domino {

	boolean movable = true;
	int value;
	int xCoord;
	int yCoord;
	JLabel label;
	Color belongsToPlayer;
	Tile currentPosition = null;
	Border borderType;
	final static Border selectBorder = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.YELLOW);

	public Domino(TwoDoku game, GUI gui, int x, int y, int number, JLabel label) {
		this.value = number;
		this.xCoord = x;
		this.yCoord = y;
		this.label = label;

		this.label.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				if (game.lock) return;
				label.setBorder(selectBorder);
				gui.potImage.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				if (game.selectedDomino != null && game.selectedDomino.equals(Domino.this)) {
					game.selectedDomino.label.setBorder(null);
					game.selectedDomino = null;
				} else if (game.selectedDomino!=null) {
					game.selectedDomino.label.setBorder(null);
					game.selectedDomino = Domino.this;
				} else game.selectedDomino = Domino.this;

				if (game.gameStage == 3) {
					if (game.selectedDomino != null && game.selectedDomino.label.getText().equals("")) {
						gui.passButton.setText("Pass");
						gui.instruction.setText("You can't take this domino yet.");
					} else if (game.selectedDomino != null && game.selectedDomino.movable == false && game.canTake) {
						gui.passButton.setText("Take");
						gui.instruction.setText("Click on 'Take' to take domino.");	
					} else if (game.selectedDomino != null && game.selectedDomino.movable == true && game.canTake) {
						gui.passButton.setText("Pass");
						gui.instruction.setText("Play domino or pass.");	
					} else if (game.selectedDomino != null && !game.canTake && game.selectedDomino.movable == false 
							&& Domino.this.currentPosition != null) {
						gui.passButton.setText("Pass");
						gui.instruction.setText("You can't take any more dominoes.");
					} else if (game.selectedDomino != null && game.selectedDomino.movable == true && !game.canTake) {
						gui.passButton.setText("Pass");
						gui.instruction.setText("Play domino, return to pot, or pass.");
					}
				}
			}
		});
	}

	public void revealDomino() {
		this.label.setText("" + value);
	}
}
