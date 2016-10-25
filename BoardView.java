import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * View of the Noughts and Crosses board.
 *
 */
public class BoardView extends JPanel implements Observer {
	private NoughtsCrossesModel model;
	private JButton[][] cell;
	private boolean myTurn;

	/**
	 * Constructor of the view.
	 * 
	 * @param model
	 *            The model of the Noughts and Crosses game.
	 * @param myTurn
	 *            A boolean value which indicates if it is the users turn to
	 *            play.
	 */
	public BoardView(NoughtsCrossesModel model, boolean myTurn) {
		super();

		// initialize model
		this.model = model;

		// initialize myTurn
		this.myTurn = myTurn;

		// create array of buttons
		cell = new JButton[3][3];

		// set layout of panel
		setLayout(new GridLayout(3, 3));

		// for each square in grid:create a button; place on panel
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cell[i][j] = new JButton(" ");
				final int x = i;
				final int y = j;
				cell[i][j].addActionListener(e -> model.turn(x, y));
				add(cell[i][j]);
			}
		}
	}

	/**
	 * Change the value of the boolean that indicates wheter or not it is the
	 * users turn to play.
	 * 
	 * @param myTurn
	 *            The value which indicates if it is the users turn to play.
	 */
	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cell[i][j].setEnabled(myTurn);
			}
		}
	}

	/**
	 * Returns a boolean value which indicates if it is the users turn to play.
	 * 
	 * @return A boolean value which indicates if it is the users turn to play.
	 */
	public boolean getMyTurn() {
		return myTurn;
	}

	/**
	 * Returns the model of the Noughts and Crosses game.
	 * 
	 * @return The model of the Noughts and Crosses game.
	 */
	public NoughtsCrossesModel getModel() {
		return model;
	}

	/**
	 * Update method that gets called when something changes in the model.
	 */
	public void update(Observable obs, Object obj) {
		// for each square do the following:
		// if it's a NOUGHT, put O on button
		// if it's a CROSS, put X on button
		// else put on button
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (model.get(i, j) == NoughtsCrosses.CROSS) {
					cell[i][j].setText("X");
					cell[i][j].setEnabled(false);
				} else if (model.get(i, j) == NoughtsCrosses.NOUGHT) {
					cell[i][j].setText("O");
					cell[i][j].setEnabled(false);
				} else {
					cell[i][j].setText(" ");
					if (myTurn == true) {
						boolean notOver = (model.whoWon() == NoughtsCrosses.BLANK);
						cell[i][j].setEnabled(notOver);
					} else {
						cell[i][j].setEnabled(myTurn); // this will disable
														// every button on
														// screen when it is not
														// the users turn to
														// play.
					}
				}
			}
		}
		repaint();
	}

}