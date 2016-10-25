import java.util.concurrent.*;

/**
 * Class which contains two strings, one for the invited person, and one for the
 * person that invited.
 *
 */
public class InviteTable {

	private ConcurrentMap<String, String> inviteTable = new ConcurrentHashMap<String, String>();

	/**
	 * Add someone to the Map.
	 * 
	 * @param invited
	 *            The nickname of the user that is to be added to the Map.
	 */
	public void add(String invited) {
		inviteTable.put(invited, "");
	}

	/**
	 * Change the inviter.
	 * 
	 * @param invited
	 *            The invited.
	 * @param inviter
	 *            The new inviter.
	 */
	public void invited(String invited, String inviter) {
		inviteTable.replace(invited, inviter);
	}

	/**
	 * Returns the inviter of a specific user.
	 * 
	 * @param invited
	 *            The user whose inviter we need.
	 * @return The inviter.
	 */
	public String getInviter(String invited) {
		return inviteTable.get(invited);
	}

	/**
	 * Remove someone from the map.
	 * 
	 * @param nickname
	 *            The username to be removed from the map.
	 */
	public void remove(String nickname) {
		inviteTable.remove(nickname);
	}

}
