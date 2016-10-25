
/**
 * Class that represents a Noughts and Crosses game.
 *
 */
public class NoughtsCrosses {
	public static final int BLANK = 0;
	public static final int CROSS = 1;
	public static final int NOUGHT = 2;

	private boolean crossTurn;
	private int[][] board;

	/**
	 * Create a new game with empty board
	 */
	public NoughtsCrosses() {
		board = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = BLANK;
			}
		}
		crossTurn = true;
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
		return board[i][j];
	}

	/**
	 * Get symbol at given location using only one variable.
	 * 
	 * @param x
	 *            The given location
	 * @return The symbol at that location.
	 */
	public int get(int x) {
		if (x <= 2) {
			return board[0][x];
		} else if (x <= 5) {
			return board[1][x - 3];
		} else {
			return board[2][x - 6];
		}
	}

	/**
	 * Is it cross's turn?
	 * 
	 * @return true if it is cross's turn, false for nought's turn
	 */
	public boolean isCrossTurn() {
		return crossTurn;
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
		if (board[i][j] == BLANK) {
			if (crossTurn) {
				board[i][j] = CROSS;
			} else {
				board[i][j] = NOUGHT;
			}
			crossTurn = !crossTurn;
		} else {
			throw new IllegalArgumentException("Board not empty at (" + i + ", " + j + ")");
		}
	}

	/**
	 * Let the player whose turn it is play at a particular location given
	 * through a single variable.
	 * 
	 * @param x
	 *            The location.
	 */
	public void turn(int x) {
		if (x <= 2) {
			if (board[0][x] == BLANK) {
				if (crossTurn) {
					board[0][x] = CROSS;
				} else {
					board[0][x] = NOUGHT;
				}
				crossTurn = !crossTurn;
			}
		} else if (x <= 5) {
			if (board[1][x - 3] == BLANK) {
				if (crossTurn) {
					board[1][x - 3] = CROSS;
				} else {
					board[1][x - 3] = NOUGHT;
				}
				crossTurn = !crossTurn;
			}
		} else {
			if (board[2][x - 6] == BLANK) {
				if (crossTurn) {
					board[2][x - 6] = CROSS;
				} else {
					board[2][x - 6] = NOUGHT;
				}
				crossTurn = !crossTurn;
			}
		}
	}

	/**
	 * Check if the game is a draw.
	 * 
	 * @return Whether the game is a draw or not.
	 */
	public boolean isDraw() {
		boolean draw = true;
		if (whoWon() == BLANK) {
			for (int i = 0; i < 9; i++) {
				if (get(i) == NoughtsCrosses.BLANK) {
					draw = false;
				}
			}
			return draw;
		} else {
			draw = false;
			return draw;
		}
	}

	private boolean winner(int player) {
		return (board[0][0] == player && board[0][1] == player && board[0][2] == player)
				|| (board[1][0] == player && board[1][1] == player && board[1][2] == player)
				|| (board[2][0] == player && board[2][1] == player && board[2][2] == player)
				|| (board[0][0] == player && board[1][0] == player && board[2][0] == player)
				|| (board[0][1] == player && board[1][1] == player && board[2][1] == player)
				|| (board[0][2] == player && board[1][2] == player && board[2][2] == player)
				|| (board[0][0] == player && board[1][1] == player && board[2][2] == player)
				|| (board[0][2] == player && board[1][1] == player && board[2][0] == player);
	}

	/**
	 * Determine who (if anyone) has won
	 * 
	 * @return CROSS if cross has won, NOUGHT if nought has won, otherwise BLANK
	 */
	public int whoWon() {
		if (winner(CROSS)) {
			return CROSS;
		} else if (winner(NOUGHT)) {
			return NOUGHT;
		} else {
			return BLANK;
		}
	}

	/**
	 * Start a new game
	 */
	public void newGame() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = BLANK;
			}
		}
		crossTurn = true;
	}

	/**
	 * Check to see if a user has exited the game.
	 * 
	 * @return Whether or not someone is still playing the game.
	 */
	public boolean hasExited() {
		boolean exit = true;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] != CROSS) {
					exit = false;
				}
			}
		}
		return exit;
	}

	/**
	 * Method that gets called when someone exits the game. It changes every
	 * spot on the board to the cross symbol.
	 */
	public void exitGame() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = CROSS;
			}
		}
	}

}