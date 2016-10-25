import java.io.BufferedReader;
import java.io.IOException;

/**
 * Class that receives messages from the other clients 
 * via the ServerSender thread and prints them to the client.
 *
 */
public class ClientReceiver extends Thread {

	private BufferedReader server;
	private NoughtsCrossesGUI gameGUI;
	private Client master;
	private boolean quit = false;

	/**
	 * Constructor for the receiver.
	 * 
	 * @param server
	 *            Buffered reader that reads from the server sender.
	 * @param gameGUI
	 *            The Graphical User Interface of the Noughts and Crosses game.
	 * @param master
	 *            The client.
	 */
	ClientReceiver(BufferedReader server, NoughtsCrossesGUI gameGUI, Client master) {
		this.server = server;
		this.gameGUI = gameGUI;
		this.master = master;
	}

	/**
	 * Class that runs when the receiver is started.
	 */
	public void run() {
		try {
			// Loop, reading messages from the server.
			while (!quit) {
				// Read from the server.
				String sender = server.readLine(); // Read the sender.
				String s = server.readLine(); // Read the message.
				if (sender.equals("server") && s.equals("Welcome")) { // If the server sends
																		// "Welcome." it means that
																		// the server accepted the
																		// nickname for the user.
					
					master.setNickname(s); // We set the accepted nickname as
											// the nickname of the client.
					
					master.welcome(); // Calls the welcome method in the client
										// which notifies the sender that the
										// last nickname sent has been accepted.
					
				} else if (sender.equals("move")) { // If the sender of the
													// message is "move", it
													// means that we have
													// received a move from our
													// opponent.
					
					master.sendMove(Integer.parseInt(s)); // We send the move to
															// the client which
															// will in turn send
															// it to the
															// ClientSender.
					
				} else if (sender.equals("initgame")) { // If the sender of the
														// message is
														// "initgame", it means
														// that we are going to
														// play a new game of
														// Noughts and Crosses
														// against someone.
					
					gameGUI.getView().getModel().newGame(); // Start a new game
															// of Noughts and
															// Crosses.
					
					if (s.equals("true")) { // If the message is "true" it means
											// that this client will start
											// first.
					
						gameGUI.setMyTurn(true); // Notify the GUI that it is
													// the turn of this client.
						
						gameGUI.setSide(NoughtsCrosses.CROSS); // If this client
																// starts first,
																// then it will
																// be playing as
																// CROSS.
						
						gameGUI.run(); // Run the Graphical User Interface of
										// the Noughts and Crosses game.
					
					} else {
					
						gameGUI.setMyTurn(false); // If the message is "false"
													// it
													// means that this client
													// will start second and we
													// send this value to the
													// GUI.
						
						gameGUI.setSide(NoughtsCrosses.NOUGHT); // If this
																// client starts
																// second, it
																// means that it
																// will be
																// playing as
																// NOUGHT.
						
						gameGUI.run(); // Run the Graphical User Interface of
										// the Noughts and Crosses game.
					}
				} else if (sender.equals("server") && s.equals("quit")) { // If the sender of
																			// the message is
																			// "server" and the
																			// message is "quit"
																			// it means that the
																			// client wants to quit.
					
					quit = true; // Change the value of the boolean variable
									// quit to true, which will end the while
									// loop.
				} else if (sender.equals("server") && s.equals("exitgame")) { // If the sender is
																				// "server" and the message is
																				// "exitgame" it means that
																				// the opponent has closed
																				// the Noughts and Crosses game
					
					gameGUI.dispose(); // Close the clients Noughts and Crosses
										// GUI.

				} else if (sender.equals("server") && s.equals("newgame")) { // If the sender is
																				// "server" and the message is
																				// "newgame" it means that the
																				// opponent has started a new
																				// game of Noughts and Crosses.
					
					gameGUI.getView().getModel().newGame(); // Start a new game
															// of Noughts and
															// Crosses.
					
					if (gameGUI.getView().getMyTurn() == true) {
						gameGUI.setSide(NoughtsCrosses.CROSS); // If this client
																// goes first,
																// then this
																// client plays
																// as CROSS.
					} else {
						gameGUI.setSide(NoughtsCrosses.NOUGHT); // Otherwise it
																// plays as
																// NOUGHT
					}
				} else if (s != null) {
					System.out.println("From " + sender + ": " + s); // If the message does
																		// not match any of
																		// the previous cases,
																		// then it is just
																		// a message for the
																		// client, which means it
																		// will just be printed.
				} else {
					server.close(); // Probably no point.
					throw new IOException("Got null from server"); // Caught
																	// below.
				}
			}
			server.close(); // Close the Buffered Reader that reads from
							// the server sender.
		} catch (IOException e) {
			System.out.println("Server seems to have died " + e.getMessage());
			System.exit(1); // Give up.
		}
	}
}