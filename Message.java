/**
 * Class that represents a message.
 *
 */
public class Message {

	private final String sender;
	private final String text;

	/**
	 * Constructor for the message.
	 * 
	 * @param sender
	 *            The sender of the message.
	 * @param text
	 *            The text of the message.
	 */
	Message(String sender, String text) {
		this.sender = sender;
		this.text = text;
	}

	/**
	 * Get the sender of the message.
	 * 
	 * @return The sender of the message.
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Get the text of the message.
	 * 
	 * @return The text of the message.
	 */
	public String getText() {
		return text;
	}

	public String toString() {
		return sender + "\n" + text;
	}
}
