package queue_game.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

public class PlayerConnection implements Runnable {
	private Socket connection;
	private Reader in;
	private Writer out;
	private String name;

	public PlayerConnection(Socket connection) throws IOException {
		this.connection = connection;
		this.in = new InputStreamReader(connection.getInputStream());
		this.out = new OutputStreamWriter(connection.getOutputStream());
	}

	public void run() {
		try {
			Utilities.expectString(in, "name");
			name = Utilities.readRawString(in);
		} catch (IOException e) {
			System.out.println("Nieudane połączenie");
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
