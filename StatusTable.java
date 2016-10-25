
/**
 * Map that contains usernames and their current status (in game or not).
 */
import java.util.concurrent.*;

public class StatusTable {

	private ConcurrentMap<String, Boolean> statusTable = new ConcurrentHashMap<String, Boolean>();

	/**
	 * Add a new user to the map.
	 * 
	 * @param nickname
	 *            The new user.
	 */
	public void add(String nickname) {
		statusTable.put(nickname, false);
	}

	/**
	 * Set the status of the user.
	 * 
	 * @param nickname
	 *            The user.
	 * @param inGame
	 *            Whether or not the user is in game.
	 */
	public void setInGame(String nickname, boolean inGame) {
		statusTable.replace(nickname, inGame);
	}

	/**
	 * Returns the status of the user.
	 * 
	 * @param nickname
	 *            The user.
	 * @return The status.
	 */
	public Boolean getStatus(String nickname) {
		return statusTable.get(nickname);
	}

	/**
	 * Remove a specific user from the map.
	 * 
	 * @param nickname
	 *            The user to be removed.
	 */
	public void remove(String nickname) {
		statusTable.remove(nickname);
	}

	/**
	 * Returns the name of the users who are not in game at the moment.
	 * 
	 * @return The name of the users who are not in game at the moment.
	 */
	public String printAllAvailable() {
		String userList = new String();
		for (ConcurrentMap.Entry<String, Boolean> entry : statusTable.entrySet()) {
			if (entry.getValue().equals(false))
				userList = userList + " " + entry.getKey();
		}
		return userList;
	}

}