package queue_game.server;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Table {
	private static List<Table> tables = new ArrayList<Table>();
	private final int id;
	private final int playerLimit;
	private List<PlayerConnection> players = new LinkedList<PlayerConnection>();

	public Table(int id, int playerLimit) {
		this.id = id;
		this.playerLimit = playerLimit;
		tables.add(this);
	}

	public static List<Table> getTables() {
		return tables;
	}

	public int getId() {
		return id;
	}
	
	public int getPlayerCount() {
		return players.size();
	}

	public static void writeList(Writer out) throws IOException {
		Utilities.writeObject(out, tables.size());
		out.write('\n');
		for(Table t : tables) {
			Utilities.writeObject(out, t.id);
			out.write('\n');
		}
		Utilities.finishWriting(out);
	}

	public synchronized boolean join (PlayerConnection player) {
		if(players.size() >= playerLimit)
			return false;
		players.add(player);
		return true;
	}
}
