
/**
 * Map that contains every user and their current opponent or "" if they are not playing at the moment.
 */
import java.util.concurrent.*;

public class VersusTable {
	private ConcurrentMap<String, String> versusTable = new ConcurrentHashMap<String, String>();

	/**
	 * Add a new user to the map.
	 * @param nickname The new user.
	 */
	public void add(String nickname) {
		versusTable.put(nickname, "");
	}

	/**
	 * Change the opponent of a given user.
	 * @param nickname The user.
	 * @param opponent The new opponent.
	 */
	public void setOpponent(String nickname, String opponent) {
		versusTable.replace(nickname, opponent);
	}

	/**
	 * Returns the opponent of a given user.
	 * @param nickname The user.
	 * @return The opponent.
	 */
	public String getOpponent(String nickname) {
		return versusTable.get(nickname);
	}

	/**
	 * Remove a given user from the map.
	 * @param nickname The given user.
	 */
	public void remove(String nickname) {
		versusTable.remove(nickname);
	}

}