import javax.swing.JFrame;

/**
 * The GUI of the Noughts and Crosses game.
 *
 */
public class NoughtsCrossesGUI {
	private NoughtsCrosses game;
	private NoughtsCrossesComponent comp;
	private JFrame frame;
	private int mySide = NoughtsCrosses.BLANK;

	/**
	 * The constructor for the GUI.
	 * 
	 * @param game
	 *            The Noughts and Crosses game.
	 * @param myTurn
	 *            Boolean that indicates whose turn it is.
	 */
	public NoughtsCrossesGUI(NoughtsCrosses game, boolean myTurn) {
		this.game = game;
		this.comp = new NoughtsCrossesComponent(this.game, myTurn);
	}

	/**
	 * Change what side the user is on (NoughtsCrosses.CROSS or
	 * NoughtsCrosses.NOUGHT).
	 * 
	 * @param x
	 *            The side the user is on.
	 */
	public void setSide(int x) {
		this.mySide = x;
	}

	/**
	 * Get the side the user is on.
	 * 
	 * @return The side of the user (NoughtsCrosses.CROSS or
	 *         NoughtsCrosses.NOUGHT).
	 */
	public int getSide() {
		return mySide;
	}

	/**
	 * Change whether or not it is the users turn.
	 * 
	 * @param myTurn
	 *            Whether or not it is the turn of the user.
	 */
	public void setMyTurn(boolean myTurn) {
		comp.setMyTurn(myTurn);
	}

	/**
	 * Get a boolean variable which indicates whether or not it is the users
	 * turn.
	 * 
	 * @return Whether or not it is the turn of the user.
	 */
	public boolean getMyTurn() {
		return comp.getMyTurn();
	}

	/**
	 * Get the board view.
	 * 
	 * @return The board view.
	 */
	public BoardView getView() {
		return comp.getView();
	}

	/**
	 * Close the frame.
	 */
	public void dispose() {
		frame.dispose();
	}

	/**
	 * Create the frame which contains the Noughts and Crosses component.
	 */
	public void run() {
		this.frame = new JFrame("Noughts and Crosses");
		frame.setSize(400, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.add(comp);

		frame.setVisible(true);
	}
}
