import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Class that holds a ConcurrentMap which contains usernames and their
 * respective message queue.
 *
 */
public class ClientTable {

	private ConcurrentMap<String, MessageQueue> queueTable = new ConcurrentHashMap<String, MessageQueue>();

	/**
	 * Checks if the following nickname is in the Map.
	 * 
	 * @param nickname
	 *            The users nickname.
	 * @return Whether or not it is in the Map.
	 */
	public boolean contains(String nickname) {
		return queueTable.containsKey(nickname);
	}

	/**
	 * Add a new user to the Map.
	 * 
	 * @param nickname
	 *            The users nickname.
	 */
	public void add(String nickname) {
		queueTable.put(nickname, new MessageQueue());
	}

	/**
	 * Returns the message queue of the provided username.
	 * 
	 * @param nickname
	 *            The username.
	 * @return The message queue associated with the given username.
	 */
	public MessageQueue getQueue(String nickname) {
		return queueTable.get(nickname);
	}

	/**
	 * Remove an username from the Map.
	 * 
	 * @param nickname
	 *            The username.
	 */
	public void remove(String nickname) {
		queueTable.remove(nickname);
	}

	/**
	 * Print every username from this map.
	 * 
	 * @return A string containing every username in the map, separated by " ".
	 */
	public String printAll() {
		String userList = new String();
		for (ConcurrentMap.Entry<String, MessageQueue> entry : queueTable.entrySet()) {
			userList = userList + " " + entry.getKey();
		}
		return userList;
	}

}
