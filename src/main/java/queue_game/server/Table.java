package queue_game.server;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import queue_game.model.GameAction;

public class Table implements Runnable {
	private static List<Table> tables = new ArrayList<Table>();
	private final int id;
	private final int playerLimit;
	private List<PlayerConnection> players = new LinkedList<PlayerConnection>();
	private List<GameAction> actions = new LinkedList<GameAction>();

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

	public boolean join (PlayerConnection player) {
		synchronized(players) {
			if(players.size() >= playerLimit)
				return false;
			players.add(player);
			return true;
		}
	}

	public void handleAction(GameAction action) {
		synchronized(actions) {
			actions.add(action);
			actions.notifyAll();
		}
	}

	public void run() {
		GameAction action;
		while(true) {
			synchronized(actions) {
				while(actions.isEmpty()) {
					try {
						actions.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						tables.remove(this);
						for(PlayerConnection p: players) {
							p.end();
						}
						return;
					}
				}
				action = actions.remove(0);
			}
			//w tym miejscu obsluz akcje
			action = null;
		}
	}
}
