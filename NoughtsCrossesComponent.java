import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * Class that represents the panel that contains the board view and the buttons.
 *
 */
public class NoughtsCrossesComponent extends JPanel {

	private BoardView board;

	/**
	 * Constructor for the component.
	 * 
	 * @param game
	 *            The Noughts and Crosses game.
	 * @param myTurn
	 *            A boolean that indicates whose turn it is.
	 */
	public NoughtsCrossesComponent(NoughtsCrosses game, boolean myTurn) {
		super();

		NoughtsCrossesModel model = new NoughtsCrossesModel(game);

		this.board = new BoardView(model, myTurn);

		ButtonPanel controls = new ButtonPanel(model);

		model.addObserver(board);
		model.addObserver(controls);

		setLayout(new BorderLayout());

		add(board, BorderLayout.CENTER);
		add(controls, BorderLayout.SOUTH);
	}

	/**
	 * Change the value of the boolean myTurn which indicates whose turn it is.
	 * 
	 * @param myTurn
	 *            The new value.
	 */
	public void setMyTurn(boolean myTurn) {
		board.setMyTurn(myTurn);
	}

	/**
	 * Returns whether or not it is the turn of the user.
	 * 
	 * @return Whether or not it is the turn of the user.
	 */
	public boolean getMyTurn() {
		return board.getMyTurn();
	}

	/**
	 * Returns the board view.
	 * 
	 * @return The board view.
	 */
	public BoardView getView() {
		return board;
	}

}