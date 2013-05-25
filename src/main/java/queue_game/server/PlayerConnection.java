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
			Utilities.expectString(in, "NAME");
			name = Utilities.readRawString(in);
			while(true) {
				Table.writeList(out);
				Utilities.expectString(in, "JOIN");
				int tableId = Utilities.readInt(in);
				if(tableId < 0 || tableId >= Table.getTables().size()
						|| !Table.getTables().get(tableId).join(this)) {
					Utilities.writeRawString(out, "NOPE");
					Utilities.finishWriting(out);
					continue;
				}
				Utilities.writeRawString(out, "JOINED");
				Utilities.finishWriting(out);
				break;
			}
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
