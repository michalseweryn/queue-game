package queue_game.server;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import queue_game.model.GameAction;
import queue_game.model.GameActionType;

public class Table implements Runnable {
	private static List<Table> tables = new ArrayList<Table>();
	private final int id;
	private final int playerLimit;
	private List<PlayerConnection> players = new LinkedList<PlayerConnection>();
	private List<GameAction> actions = new LinkedList<GameAction>();
	private List<Integer> sources = new LinkedList<Integer>();

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

	public int join (PlayerConnection player) {
		synchronized(players) {
			if(players.size() >= playerLimit)
				return -1;
			players.add(player);
			GameAction action = new GameAction(GameActionType.JOIN, players.size() - 1, player.getName());
			for(PlayerConnection p: players) {
				try {
					p.sendAction(action);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return players.size() - 1;
		}
	}

	public void handleAction(GameAction action, int source) {
		synchronized(actions) {
			actions.add(action);
			sources.add(source);
			actions.notifyAll();
		}
	}

	public void run() {
		GameAction action;
		int source;
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
				source = sources.remove(0);
			}
			//w tym miejscu obsluz akcje
			action = null;
		}
	}
}
