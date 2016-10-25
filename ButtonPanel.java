import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Panel that contains buttons for the Noughts and Crosses game.
 *
 */
public class ButtonPanel extends JPanel implements Observer {

	private NoughtsCrossesModel model;
	private JButton reset;
	private JButton exit;

	/**
	 * Constructor for the button panel.
	 * 
	 * @param model
	 *            Model for the Noughts and Crosses game.
	 */
	public ButtonPanel(NoughtsCrossesModel model) {
		super();

		this.model = model;

		this.reset = new JButton("New Game");
		reset.setEnabled(false);
		reset.addActionListener(e -> model.newGame());

		this.exit = new JButton("Exit");
		exit.setEnabled(false);
		exit.addActionListener(e -> {
			SwingUtilities.getWindowAncestor(this).dispose();
			model.exitGame();
		});

		add(reset);
		add(exit);
	}

	/**
	 * Method that gets called when something changes in the model. This make
	 * sure that the user cannot exit the game or start a new game without
	 * finishing the one it is already playing by disabling the New Game button
	 * and the Exit button until the game ends (either in a draw or a win for
	 * either side).
	 */
	@Override
	public void update(Observable o, Object arg) {
		boolean draw = model.isDraw();
		if (model.whoWon() != NoughtsCrosses.BLANK || draw == true) {
			reset.setEnabled(true);
			exit.setEnabled(true);
		} else {
			reset.setEnabled(false);
			exit.setEnabled(false);
		}

	}
}
