
/**
 * Map that contains usernames and their score in the following format: Wins(W) Draws(D) Losses(L).
 */
import java.util.concurrent.*;

public class ScoreTable {
	private ConcurrentMap<String, int[]> versusTable = new ConcurrentHashMap<String, int[]>();

	/**
	 * Add a new user to the map with a score of W0 D0 L0.
	 * 
	 * @param nickname
	 *            The new user.
	 */
	public void add(String nickname) {
		versusTable.put(nickname, new int[3]);
	}

	/**
	 * Add a win to the account of the user.
	 * 
	 * @param nickname
	 *            The user.
	 */
	public void addWin(String nickname) {
		versusTable.get(nickname)[0]++;
	}

	/**
	 * Add a draw to the account of the user.
	 * 
	 * @param nickname
	 *            The user.
	 */
	public void addDraw(String nickname) {
		versusTable.get(nickname)[1]++;
	}

	/**
	 * Add a loss to the account of the user.
	 * 
	 * @param nickname
	 *            The user.
	 */
	public void addLoss(String nickname) {
		versusTable.get(nickname)[2]++;
	}

	/**
	 * Returns the amount of wins the user has.
	 * 
	 * @param nickname
	 *            The user.
	 * @return The amount of wins.
	 */
	public int getWins(String nickname) {
		return versusTable.get(nickname)[0];
	}

	/**
	 * Returns the amount of draws the user has.
	 * 
	 * @param nickname
	 *            The user.
	 * @return The amount of draws.
	 */
	public int getDraws(String nickname) {
		return versusTable.get(nickname)[1];
	}

	/**
	 * Returns the amount of losses the user has.
	 * 
	 * @param nickname
	 *            The user.
	 * @return The amount of losses.
	 */
	public int getLosses(String nickname) {
		return versusTable.get(nickname)[2];
	}

	/**
	 * Removes an user from the map.
	 * 
	 * @param nickname
	 *            The user.
	 */
	public void remove(String nickname) {
		versusTable.remove(nickname);
	}

	/**
	 * Get the record of the user.
	 * 
	 * @param nickname
	 *            The user.
	 * @return The record.
	 */
	public String getRecord(String nickname) {
		return "W" + getWins(nickname) + " D" + getDraws(nickname) + " L" + getLosses(nickname);
	}

	/**
	 * Get the records of all the users in the map.
	 * 
	 * @return The records of all the users.
	 */
	public String printAllRecords() {
		String records = new String();
		for (ConcurrentMap.Entry<String, int[]> entry : versusTable.entrySet()) {
			records = records + " " + entry.getKey() + ": " + getRecord(entry.getKey());
		}
		return records;
	}

}