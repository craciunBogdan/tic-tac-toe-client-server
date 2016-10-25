
// Usage:
//        java Client <user nickname> <port number> <machine name>
//
// After initializing and opening appropriate sockets, we start two
// client threads, one to send messages, and another one to get
// messages.
//
// Another limitation is that there is no provision to terminate when
// the server dies.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class that creates a client.
 *
 */
class Client {

	private String nickname;
	private int port;
	private String hostname;
	private NoughtsCrosses game;
	private NoughtsCrossesGUI gameGUI;
	private PrintStream toServer = null;
	private BufferedReader fromServer = null;
	private Socket server = null;
	private ClientSender sender;
	private ClientReceiver receiver;

	/**
	 * Constructor for the client.
	 * 
	 * @param args
	 *            An array of strings. The first element is the nickname of the
	 *            user. The second element is the port. The third element is the
	 *            address of the host.
	 */
	public Client(String[] args) {
		nickname = args[0];
		port = Integer.parseInt(args[1]);
		hostname = args[2];

		// Initialize the Noughts and Crosses game.
		this.game = new NoughtsCrosses();
		// Initialize the Graphic User Interface of the Noughts and Crosses game.
		this.gameGUI = new NoughtsCrossesGUI(game, false);

		try {
			server = new Socket(hostname, port);
			toServer = new PrintStream(server.getOutputStream());
			fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Unknown host: " + hostname);
			System.exit(1); // Give up.
		} catch (IOException e) {
			System.err.println("The server doesn't seem to be running " + e.getMessage());
			System.exit(1); // Give up.
		}

		// Create two client threads:
		this.sender = new ClientSender(nickname, toServer, gameGUI);
		this.receiver = new ClientReceiver(fromServer, gameGUI, this);

		// Add the sender as an observer to the model.
		gameGUI.getView().getModel().addObserver(sender);

		// Run them in parallel:
		sender.start();
		receiver.start();

		// Wait for them to end and close sockets.
		try {
			sender.join();
			toServer.close();
			receiver.join();
			fromServer.close();
			server.close();
		} catch (IOException e) {
			System.err.println("Something wrong " + e.getMessage());
			System.exit(1); // Give up.
		} catch (InterruptedException e) {
			System.err.println("Unexpected interruption " + e.getMessage());
			System.exit(1); // Give up.
		}
	}

	/**
	 * Method that changes the nickname of the client. This is used when the
	 * user connects with a name that is restricted or is already taken.
	 * 
	 * @param nickname
	 *            The new nickname.
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Method that signals the sender that the client has chosen a name that is
	 * allowed.
	 */
	public void welcome() {
		sender.welcome();
	}

	/**
	 * Method that makes the connection between the receiver and the sender
	 * during a game of Noughts and Crosses.
	 * 
	 * @param x
	 *            The button that the user has pressed.
	 */
	public void sendMove(int x) {
		sender.makeMove(x);
	}

	public static void main(String[] args) {

		// Check correct usage:
		if (args.length != 3) {
			System.err.println("java Client <user nickname> <port number> <machine name>");
			System.exit(1); // Give up.
		}

		Client client = new Client(args);
		System.exit(0);
	}
}
