import java.util.Observable;

/**
 * Model of the NoughtsCrosses class.
 *
 */
public class NoughtsCrossesModel extends Observable {
	private NoughtsCrosses oxo;

	/**
	 * Constructor of the model.
	 * 
	 * @param oxo
	 *            The Noughts and Crosses game.
	 */
	public NoughtsCrossesModel(NoughtsCrosses oxo) {
		super();
		this.oxo = oxo;
	}

	/**
	 * Get symbol at given location
	 * 
	 * @param i
	 *            the row
	 * @param j
	 *            the column
	 * @return the symbol at that location
	 */
	public int get(int i, int j) {
		return oxo.get(i, j);
	}

	/**
	 * Get symbol at given location using only one variable.
	 * 
	 * @param x
	 *            The given location
	 * @return The symbol at that location.
	 */
	public int get(int x) {
		return oxo.get(x);
	}

	/**
	 * Get the Noughts and Crosses game.
	 * 
	 * @return The NoughtsCrosses game.
	 */
	public NoughtsCrosses getGame() {
		return oxo;
	}

	/**
	 * Is it cross's turn?
	 * 
	 * @return true if it is cross's turn, false for nought's turn
	 */
	public boolean isCrossTurn() {
		return oxo.isCrossTurn();
	}

	/**
	 * Let the player whose turn it is play at a particular location
	 * 
	 * @param i
	 *            the row
	 * @param j
	 *            the column
	 */
	public void turn(int i, int j) {
		oxo.turn(i, j);
		setChanged();
		notifyObservers();
	}

	/**
	 * Let the player whose turn it is play at a particular location given
	 * through a single variable.
	 * 
	 * @param x
	 *            The location.
	 */
	public void turn(int x) {
		oxo.turn(x);
		setChanged();
		notifyObservers();
	}

	/**
	 * Determine who (if anyone) has won
	 * 
	 * @return CROSS if cross has won, NOUGHT if nought has won, oetherwise
	 *         BLANK
	 */
	public int whoWon() {
		return oxo.whoWon();
	}

	/**
	 * Check if the game is a draw.
	 * 
	 * @return Whether the game is a draw or not.
	 */
	public boolean isDraw() {
		return oxo.isDraw();
	}

	/**
	 * Check to see if a user has exited the game.
	 * 
	 * @return Whether or not someone is still playing the game.
	 */
	public boolean hasExited() {
		return oxo.hasExited();
	}

	/**
	 * Method that gets called when someone exits the game. It changes every
	 * spot on the board to the cross symbol.
	 */
	public void exitGame() {
		oxo.exitGame();
		setChanged();
		notifyObservers();
	}

	/**
	 * Start a new game
	 */
	public void newGame() {
		oxo.newGame();
		setChanged();
		notifyObservers();
	}
}