
// Usage:
//        java Server <port number>

import java.net.*;
import java.io.*;

public class Server {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: java Server <port number>");
			System.exit(1);
		}

		// Integer that stores the port.
		int port = Integer.parseInt(args[0]);

		// This will be shared by the server threads:
		ClientTable clientTable = new ClientTable();
		InviteTable inviteTable = new InviteTable();
		StatusTable statusTable = new StatusTable();
		VersusTable versusTable = new VersusTable();
		ScoreTable scoreTable = new ScoreTable();

		// Open a server socket:
		ServerSocket serverSocket = null;

		// We must try because it may fail with a checked exception:
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Couldn't listen on port " + port);
			System.exit(1);
		}

		// Good. We succeeded. But we must try again for the same reason:
		try {
			// We loop for ever, as servers usually do:
			while (true) {
				// Listen to the socket, accepting connections from new clients:
				Socket socket = serverSocket.accept();

				// This is so that we can use readLine():
				BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// We ask the client what its name is:
				PrintStream toClient = new PrintStream(socket.getOutputStream());
				String clientName = fromClient.readLine();
				// We loop until the name the client chooses is not part of the
				// forbidden nicknames list or is not already taken.
				while (clientTable.contains(clientName) || ForbiddenNicknames.contains(clientName)) {
					// If the name is unavailable we send a message from the
					// "server" informing the user of this.
					toClient.println("server");
					toClient.println("Name unavailable or already taken. Please choose a different one.");
					// The server waits for the next name the user chooses.
					clientName = fromClient.readLine();
				}
				// When the user has chosen an available name, the message
				// "Welcome" is sent from the server.
				toClient.println("server");
				toClient.println("Welcome");
				// For debugging:
				System.out.println(clientName + " connected");

				// We add the client to the tables:
				clientTable.add(clientName);
				inviteTable.add(clientName);
				statusTable.add(clientName);
				versusTable.add(clientName);
				scoreTable.add(clientName);

				// We create and start a new thread to read from the client:
				(new ServerReceiver(clientName, fromClient, clientTable, inviteTable, statusTable, versusTable,
						scoreTable)).start();

				// We create and start a new thread to write to the client:
				(new ServerSender(clientTable.getQueue(clientName), toClient)).start();

			}
		} catch (IOException e) {
			System.err.println("IO error " + e.getMessage());
		}
	}
}
