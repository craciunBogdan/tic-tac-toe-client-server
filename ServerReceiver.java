import java.io.BufferedReader;
import java.io.IOException;

/**
 * Gets messages from client and puts them in a queue, for another thread to
 * forward to the appropriate client.
 *
 */
public class ServerReceiver extends Thread {

	// The name of the client that sent the message.
	private String myClientsName;
	// BufferedReader that receives the messages from the client.
	private BufferedReader myClient;

	private ClientTable clientTable;
	private InviteTable inviteTable;
	private StatusTable statusTable;
	private VersusTable versusTable;
	private ScoreTable scoreTable;

	/**
	 * Constructor for the ServerReceiver.
	 * 
	 * @param myClientsName
	 *            The name of the client that sent the message.
	 * @param myClient
	 *            The BufferedReader that receives the messages from the client.
	 * @param clientTable
	 *            The client table.
	 * @param inviteTable
	 *            The invite table.
	 * @param statusTable
	 *            The status table.
	 * @param versusTable
	 *            The versus table.
	 * @param scoreTable
	 *            The score table.
	 */
	public ServerReceiver(String myClientsName, BufferedReader myClient, ClientTable clientTable,
			InviteTable inviteTable, StatusTable statusTable, VersusTable versusTable, ScoreTable scoreTable) {
		this.myClientsName = myClientsName;
		this.myClient = myClient;
		this.clientTable = clientTable;
		this.inviteTable = inviteTable;
		this.statusTable = statusTable;
		this.versusTable = versusTable;
		this.scoreTable = scoreTable;
	}

	/**
	 * Method that checks whether a given string is a number.
	 * 
	 * @param s
	 *            The String.
	 * @return Boolean value that indicates whether or not a string is a number.
	 */
	public boolean isNumeric(String s) {
		return s.matches("[-+]?\\d*\\.?\\d+");
	}

	/**
	 * Method that runs when the thread starts.
	 */
	public void run() {
		try {
			while (true) {
				// We read the first line of the message.
				String recipient = myClient.readLine();
				// If the first line of the message is "quit", that means that
				// the client wants to disconnect from the server.
				if (recipient.equals("quit")) {
					// We create the Message. The sender is "server" and the
					// text is "quit".
					Message msg = new Message("server", "quit");
					// We get the message queue of the same client.
					MessageQueue recipientsQueue = clientTable.getQueue(myClientsName);
					// We place the Message on the message queue so that we can
					// signal ClientReceiver to close.
					recipientsQueue.offer(msg);
					// We remove the client from every Map.
					clientTable.remove(myClientsName);
					inviteTable.remove(myClientsName);
					statusTable.remove(myClientsName);
					versusTable.remove(myClientsName);
					scoreTable.remove(myClientsName);
					// We print a message on the server to let us know that the
					// user has disconnected.
					System.out.println(myClientsName + " has disconnected");
					// We end the loop so that ServerReceiver can also end.
					break;

					// If the first line of the message is "yes" (or close to
					// that) and the inviter for the client is not "", it means
					// that the message is a response to an invitation.
				} else if ((recipient.equals("yes") || recipient.equals("Yes") || recipient.equals("YES")
						|| recipient.equals("yEs") || recipient.equals("yES") || recipient.equals("YeS")
						|| recipient.equals("YEs") || recipient.equals("y") || recipient.equals("Y"))
						&& !(inviteTable.getInviter(myClientsName)).equals("")) {
					// We set the status of the client to true (which means that
					// the client is in-game).
					statusTable.setInGame(myClientsName, true);
					// We do the same for his opponent.
					statusTable.setInGame(inviteTable.getInviter(myClientsName), true);
					// We make a message which signals the ClientReceiver to
					// start the game and be the first one to play.
					Message msg = new Message("initgame", "true");
					// We place the message in the clients message queue.
					clientTable.getQueue(myClientsName).offer(msg);
					// We make a message which signals the ClientReceiver of the
					// opponent to start the game and be the second one to play.
					Message msg2 = new Message("initgame", "false");
					// We place the message in the opponents message queue.
					clientTable.getQueue(inviteTable.getInviter(myClientsName)).offer(msg2);
					// We set the opponent of the client as the one who has
					// invited him.
					versusTable.setOpponent(myClientsName, inviteTable.getInviter(myClientsName));
					// We set the opponent of the inviter as the client.
					versusTable.setOpponent(inviteTable.getInviter(myClientsName), myClientsName);
					// We set the inviter of the client as "" because the game
					// started.
					inviteTable.invited(myClientsName, "");

					// If the first line of the message is "no" (or close to
					// that) and the inviter for the client is not "", it means
					// that the message is a response to an invitation.
				} else if ((recipient.equals("no") || recipient.equals("No") || recipient.equals("nO")
						|| recipient.equals("NO") || recipient.equals("n") || recipient.equals("N"))
						&& !(inviteTable.getInviter(myClientsName)).equals("")) {
					// We make a message that declines the invitation.
					Message msg = new Message("server", myClientsName + " has declined your invitation to play.");
					// We place the message on the message queue of the inviter.
					clientTable.getQueue(inviteTable.getInviter(myClientsName)).offer(msg);
					// We set the inviter for the client as "".
					inviteTable.invited(myClientsName, "");

					// If the first line of the message is "win" it means that
					// the client has won a game of noughts and crosses.
				} else if (recipient.equals("win") && statusTable.getStatus(myClientsName)) {
					// We add one win to the score of the client.
					scoreTable.addWin(myClientsName);
					// We make a message for the client that notifies him that
					// he won and shows him his updated record.
					Message msg = new Message("server", "You won against " + versusTable.getOpponent(myClientsName)
							+ ". Your new record is: " + scoreTable.getRecord(myClientsName));
					// We send the message to the client.
					clientTable.getQueue(myClientsName).offer(msg);

					// If the first line of the message is "draw" it means that
					// the client has drawn a game of noughts and crosses.
				} else if (recipient.equals("draw") && statusTable.getStatus(myClientsName)) {
					// We add one draw to the score of the client.
					scoreTable.addDraw(myClientsName);
					// We make a message for the client that notifies him that
					// he drew and shows him his updated record.
					Message msg = new Message("server", "You drew with " + versusTable.getOpponent(myClientsName)
							+ ". Your new record is: " + scoreTable.getRecord(myClientsName));
					// We send the message to the client.
					clientTable.getQueue(myClientsName).offer(msg);

					// If the first line of the message is "loss" it means that
					// the client has lost a game of noughts and crosses.
				} else if (recipient.equals("loss") && statusTable.getStatus(myClientsName)) {
					// We add one loss to the score of the client.
					scoreTable.addLoss(myClientsName);
					// We make a message for the client that notifies him that
					// he lost and shows him his updated record.
					Message msg = new Message("server", "You lost against " + versusTable.getOpponent(myClientsName)
							+ ". Your new record is: " + scoreTable.getRecord(myClientsName));
					// We send the message to the client.
					clientTable.getQueue(myClientsName).offer(msg);

					// If the first line of the message is "exitgame" it means
					// that the client has pressed the Exit button on the
					// Noughts and Crosses GUI.
				} else if (recipient.equals("exitgame") && !(versusTable.getOpponent(myClientsName)).equals("")) {
					// We make a message to notify his opponent of this.
					Message msg = new Message("server", "exitgame");
					// We get the message queue of the opponent.
					MessageQueue recipientsQueue = clientTable.getQueue(versusTable.getOpponent(myClientsName));
					// We send the message to the opponent.
					recipientsQueue.offer(msg);
					// We change the status of the client to false (not in
					// game).
					statusTable.setInGame(myClientsName, false);
					// We change the status of the opponent to false (not in
					// game).
					statusTable.setInGame(versusTable.getOpponent(myClientsName), false);
					// We change the opponent of the opponent to "" (no
					// opponent).
					versusTable.setOpponent(versusTable.getOpponent(myClientsName), "");
					// We change the opponent of the client to "" (no opponent).
					versusTable.setOpponent(myClientsName, "");

					// If the first line of the message is "newgame" it means
					// that the client has pressed the New Game button on the
					// GUI.
				} else if (recipient.equals("newgame") && !(versusTable.getOpponent(myClientsName)).equals("")) {
					// We make a message to notify the opponent of this.
					Message msg = new Message("server", "newgame");
					// We get the message queue of the opponent.
					MessageQueue recipientsQueue = clientTable.getQueue(versusTable.getOpponent(myClientsName));
					// We send the message to the opponent.
					recipientsQueue.offer(msg);

					// If the string is a number and it is between 0 and 8, it
					// means that the client is sending a move to the server.
				} else if (isNumeric(recipient)
						&& ((Integer.parseInt(recipient) >= 0) || (Integer.parseInt(recipient) <= 8))
						&& (statusTable.getStatus(myClientsName))) {
					// We make a message to notify the opponent of this.
					Message msg = new Message("move", recipient);
					// We get the message queue for the opponent.
					MessageQueue recipientsQueue = clientTable.getQueue(versusTable.getOpponent(myClientsName));
					// We send the message to the opponent.
					recipientsQueue.offer(msg);

				} else {
					// If none of the above apply conditions apply to the
					// message, it means that it is a two line message.
					// Therefore, we read the second line.
					String text = myClient.readLine();

					// If the second line is "play" it means that the recipient
					// is invited by the client to a game of Noughts and
					// Crosses.
					if (recipient != null && text.equals("play")) {
						// We check to see if the recipient is not already in a
						// game.
						if (statusTable.getStatus(recipient) == false) {
							// We make a message for the recipient to ask him if
							// he is willing to play a game of noughts and
							// crosses for the client.
							Message msg = new Message("server",
									"Would you like to play a game of Noughts and Crosses with " + myClientsName + "?");
							// We change the inviter for the recipient to the
							// client.
							inviteTable.invited(recipient, myClientsName);
							// We get the queue of the recipient.
							MessageQueue recipientsQueue = clientTable.getQueue(recipient);
							// We send the message to the recipient.
							if (recipientsQueue != null)
								recipientsQueue.offer(msg);
							else
								System.err.println("Message for unexistent client " + recipient + ": " + text);
						} else {
							// If the status of the recipient is true (already
							// in game), then we inform the client of this.
							// We make a message to inform the client.
							Message msg = new Message("server", recipient + " is playing a game at the moment");
							// We get the message queue of the client.
							MessageQueue recipientsQueue = clientTable.getQueue(myClientsName);
							// We send the message to the client.
							if (recipientsQueue != null)
								recipientsQueue.offer(msg);
							else
								System.err.println("Message for unexistent client " + recipient + ": " + text);
						}

						// If the first line is "show", it means that the client
						// wants to find out something from the server.
					} else if (recipient.equals("show")) {
						// If the second line is "all", it means that the client
						// wants to find out the name of all the users connected
						// to the server.
						if (text.equals("all")) {
							// We make a message containing the names of all the
							// users connected to the server.
							Message msg = new Message("server", clientTable.printAll());
							// We get the message queue of the client.
							MessageQueue recipientsQueue = clientTable.getQueue(myClientsName);
							// We send the message to the client.
							recipientsQueue.offer(msg);

							// If the second line is "available", it means that
							// the client wants to find out the name of all the
							// users that are not in a game of noughts and
							// crosses at the moment.
						} else if (text.equals("available")) {
							// We make a message containing the names of all the
							// users that are available to play.
							Message msg = new Message("server", statusTable.printAllAvailable());
							// We get the message queue of the client.
							MessageQueue recipientsQueue = clientTable.getQueue(myClientsName);
							// We send the message to the client.
							recipientsQueue.offer(msg);

							// If the second line is "scoreboard", it means that
							// the client wants to find out the scores of all
							// the users connected to the server.
						} else if (text.equals("scoreboard")) {
							// We make a message containing the information
							// desired by the user.
							Message msg = new Message("server", scoreTable.printAllRecords());
							// We get the queue of the client.
							MessageQueue recipientsQueue = clientTable.getQueue(myClientsName);
							// We send the message to the client.
							recipientsQueue.offer(msg);

							// If the second line is "record", it means that the
							// user wants to find out his record.
						} else if (text.equals("record")) {
							// We make a message containing the information
							// desired by the user.
							Message msg = new Message("server", scoreTable.getRecord(myClientsName));
							// We get the queue of the client.
							MessageQueue recipientsQueue = clientTable.getQueue(myClientsName);
							// We send the message to the client.
							recipientsQueue.offer(msg);
						}

						// If none of the above apply, it means that the input
						// of the user is just a message for another user.
						// Therefore we just send it.
					} else if (recipient != null && text != null) {
						// Make the message.
						Message msg = new Message(myClientsName, text);
						// We get the queue of the recipient.
						MessageQueue recipientsQueue = clientTable.getQueue(recipient);
						// We send the message.
						if (recipientsQueue != null)
							recipientsQueue.offer(msg);
						else
							System.err.println("Message for unexistent client " + recipient + ": " + text);

					} else {
						myClient.close();
						return;
					}
				}
			}
			// We close the BufferedReader.
			myClient.close();
		} catch (

		IOException e)

		{
			System.err.println("Something went wrong with the client " + myClientsName + " " + e.getMessage());
			// No point in trying to close sockets. Just give up.
			// We end this thread (we don't do System.exit(1)).
		}
	}
}
