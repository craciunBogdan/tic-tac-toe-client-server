/**
 * Class which contains an array of forbidden usernames.
 *
 */
public class ForbiddenNicknames {

	/**
	 * The array.
	 */
	private static final String[] forbiddenNicknames = { "server", "yes", "yeS", "yEs", "yES", "Yes", "YeS", "YES",
			"no", "nO", "No", "NO", "quit", "initgame", "newgame", "exitgame", "move", "win", "draw", "loss" };

	/**
	 * Method which returns whether or not the given username is part of the
	 * "blacklist".
	 * 
	 * @param nickname
	 *            The given username.
	 * @return Whether or not the given username is in the array.
	 */
	public static boolean contains(String nickname) {
		for (int i = 0; i < forbiddenNicknames.length; i++) {
			if (nickname.equals(forbiddenNicknames[i])) {
				return true;
			}
		}
		return false;
	}

}
