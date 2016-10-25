import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;

/**
 * Repeatedly reads from the user in two separate lines, sending them to the
 * server (read by ServerReceiver thread).
 **/
public class ClientSender extends Thread implements Observer {

	private String nickname;
	private PrintStream server;
	private NoughtsCrossesGUI gameGUI;
	// board is used to update the GUI with the moves of the opponent.
	private int[][] board = new int[3][3];
	private NoughtsCrossesModel gameModel;
	private boolean connected = false;
	private boolean quit = false;

	/**
	 * Constructor for the ClientSender.
	 * 
	 * @param nickname
	 *            The clients chosen nickname.
	 * @param server
	 *            PrintStream that sends the lines to the ServerReceiver.
	 * @param gameGUI
	 *            The Graphical User Interface of the Noughts and Crosses game.
	 */
	ClientSender(String nickname, PrintStream server, NoughtsCrossesGUI gameGUI) {
		this.nickname = nickname;
		this.server = server;
		this.gameGUI = gameGUI;
		// The model used in the GUI.
		this.gameModel = gameGUI.getView().getModel();
	}

	public void run() {
		// So that we can use the method readLine:
		BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

		try {
			// Prints the nickname chosen by the user.
			server.println(nickname);
			// To get around the time it takes for Client to call the welcome
			// method.
			Thread.sleep(500);
			// Connected will become true once the Client calls the welcome
			// method. That means that the chosen username is allowed.
			if (connected == false) {
				while (connected == false) {
					// The user has to choose a different nickname.
					nickname = user.readLine();
					// This new nickname is printed to the server.
					server.println(nickname);
					// To get around the time it makes for Client too call the
					// welcome method.
					Thread.sleep(500);
				}
				// Once the user chooses an appropriate username, the following
				// message will be printed.
				System.out.println("Succesfully connected.");
			} else {
				// The following message will be printed, which means the user
				// has chosen an appropriate username and is connected to the
				// server.
				System.out.println("Succesfully connected.");
			}
			// Then loop, sending messages to recipients via the server:
			while (!quit) {
				// Most messages, apart from some commands such as quit, are
				// composed of 2 lines which are represented in this class by
				// recipient and text.
				// This line reads the first line of the users input.
				String recipient = user.readLine();
				// It is then printed to the server.
				server.println(recipient);
				// If the line printed to the server is the command quit, this
				// will change the value of the boolean quit to true, which
				// means the loop will stop.
				if (recipient.equals("quit")) {
					quit = true;
				}
				// We check to see if the line printed to the server isn't the
				// answer to an invitation to play a game of Noughts and
				// Crosses. If it isn't, then we will wait for the second line
				// of the users input.
				else if (!(recipient.equals("yes") || recipient.equals("Yes") || recipient.equals("YES")
						|| recipient.equals("yEs") || recipient.equals("yES") || recipient.equals("YeS")
						|| recipient.equals("YEs") || recipient.equals("Y") || recipient.equals("y")
						|| recipient.equals("no") || recipient.equals("No") || recipient.equals("nO")
						|| recipient.equals("NO") || recipient.equals("n") || recipient.equals("N")
						|| recipient.equals("quit"))) {
					String text = user.readLine();
					server.println(text);
				}
				Thread.sleep(500);

			}
			// Close the print stream.
			server.close();
		} catch (IOException | InterruptedException e) {
			System.err.println("Communication broke in ClientSender" + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Method that changes the boolean connected to true, which signifies the
	 * fact the nickname chosen by the user is appropriate.
	 */
	public void welcome() {
		connected = true;
	}

	/**
	 * Method that executes a move.
	 * 
	 * @param x
	 *            Integer that represents the button which is pressed by the
	 *            user
	 */
	public void makeMove(int x) {
		gameModel.turn(x);
	}

	/**
	 * Method that gets called when something changes in the model.
	 */
	@Override
	public void update(Observable o, Object arg) {
		// The Noughts and Crosses model used by the GUI.
		NoughtsCrossesModel model = gameGUI.getView().getModel();
		// Check if the user is still in the game.
		if (model.hasExited()) {
			// If the user exited from the game, print "exitgame" to the server.
			String text = "exitgame";
			server.println(text);
		} else {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					// Check if the board of the ClientSender is different from
					// the board of the model.
					if (board[i][j] != model.get(i, j)) {
						// If this position is BLANK in our board but occupied
						// in the model it means a move has been made.
						if (model.get(i, j) != NoughtsCrosses.BLANK) {
							// Transform the move from 2 variables to one with
							// the formula 3 * i + j.
							String text = String.valueOf(3 * i + j);
							// Set the variable myTurn in the GUI to the
							// opposite of what it was.
							gameGUI.setMyTurn(!gameGUI.getMyTurn());
							// Print the move to the server.
							server.println(text);
							// Make the change in our board so that it is the
							// same as the one in the model.
							board[i][j] = model.get(i, j);
							// Check to see if the game ended and it is not a
							// draw.
							if (model.whoWon() != NoughtsCrosses.BLANK && !model.isDraw()) {
								// If there is, and it the side of the winner
								// matches the one of the GUI, it means that
								// this client has won the game. Therefore we
								// print "win" to the server.
								if (model.whoWon() == gameGUI.getSide()) {
									server.println("win");
								} else {
									// Otherwise, print "loss".
									server.println("loss");
								}
							} else if (model.isDraw()) {
								// If the game ended in a draw, print "draw".
								server.println("draw");
							}
						} else { // If this position is occupied in our board
									// but BLANK in the model it means a new
									// game has been started.
							for (int i2 = 0; i2 < 3; i2++) {
								for (int j2 = 0; j2 < 3; j2++) {
									// Therefore, we set our board to the BLANK
									// one.
									board[i2][j2] = NoughtsCrosses.BLANK;
								}
							}
							// Set the variable myTurn in the GUI to the
							// opposite.
							gameGUI.getView().setMyTurn(!gameGUI.getView().getMyTurn());
							// We print "newgame" to the server to notify the
							// beginning of a new game.
							String text = "newgame";
							server.println(text);
							// We end the for loops.
							i = 4;
							j = 4;
						}
					}
				}
			}
		}
	}
}
