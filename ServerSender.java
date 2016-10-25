import java.io.PrintStream;

// Continuously reads from message queue for a particular client,
// forwarding to the client.

public class ServerSender extends Thread {
	private MessageQueue queue;
	private PrintStream client;

	public ServerSender(MessageQueue q, PrintStream c) {
		queue = q;
		client = c;
	}

	public void run() {
		while (true) {
			Message msg = queue.take();
			if (msg.getSender() == "server" && msg.getText() == "quit") {
				// If the message is quit, then we print this to the client and
				// then we close the print stream and end the loop.
				client.println(msg);
				break;
			} else {
				// Otherwise we simply print the message.
				client.println(msg);
			}
		}
//		client.close();
	}
}