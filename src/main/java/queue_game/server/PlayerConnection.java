package queue_game.server;

import java.awt.Desktop.Action;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

import queue_game.model.GameAction;
import queue_game.model.GameActionType;

public class PlayerConnection implements Runnable {
	private Socket connection;
	private Reader in;
	private Writer out;
	private String name;
	private Table table;
	private int myId = -1;
	private boolean ended = false;

	public PlayerConnection(Socket connection) throws IOException {
		this.connection = connection;
		this.in = new InputStreamReader(connection.getInputStream());
		this.out = new OutputStreamWriter(connection.getOutputStream());
	}

	public void sendAction(GameAction action) throws IOException {
		System.out.println("send " + action);
		action.write(out);
	}

	public void end() {
		ended = true;
	}

	public String getName() {
		return name;
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
						|| (myId = Table.getTables().get(tableId).join(this)) == -1) {
					Utilities.writeObject(out, GameActionType.ERROR);
					Utilities.finishWriting(out);
					continue;
				}
				table = Table.getTables().get(tableId);
				break;
			}
			while(!ended) {
				GameAction action = GameAction.read(in);
				table.handleAction(action, myId);
			}
		} catch (IOException e) {
			if(table != null) {
				table.unjoin(this);
			}
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
