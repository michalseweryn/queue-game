package queue_game.server;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import queue_game.ActionCreator;
import queue_game.Updater;
import queue_game.controller.Game;
import queue_game.model.DecksOfQueuingCardsBox;
import queue_game.model.GameAction;
import queue_game.model.GameActionType;
import queue_game.model.GameState;
import queue_game.model.StandardDeckOfDeliveryCards;

public class Table implements Runnable, ActionCreator, Updater {

	private static List<Table> tables = new ArrayList<Table>();
	private final int id;
	private final int playerLimit;
	private List<PlayerConnection> players = new LinkedList<PlayerConnection>();
	private List<GameAction> actions = new LinkedList<GameAction>();
	private List<Integer> sources = new LinkedList<Integer>();
	private boolean isReady[];
	private GameAction recentAction[];// problems with concurrency
	private boolean gameOnRun = false;

	// Game Logic stuff
	GameState gameState;
	Game game;
	DecksOfQueuingCardsBox queuingCardsDecks;

	public Table(int id, int playerLimit) {
		this.id = id;
		this.playerLimit = playerLimit;
		recentAction = new GameAction[playerLimit];
		isReady = new boolean[playerLimit];
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
		for (Table t : tables) {
			Utilities.writeObject(out, t.id);
			out.write('\n');
		}
		Utilities.finishWriting(out);
	}

	public int join(PlayerConnection player) {
		synchronized (players) {
			if (players.size() >= playerLimit)
				return -1;
			int ind = 0;
			for(PlayerConnection playerToo : players)
				try {
					player.sendAction(new GameAction(GameActionType.JOIN, ind++, playerToo.getName()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			players.add(player);
			GameAction action = new GameAction(GameActionType.JOIN,
					players.size() - 1, player.getName());
			for (PlayerConnection p : players) {
				try {
					p.sendAction(action);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return players.size() - 1;
		}
	}
	
	public void unjoin(PlayerConnection player) {
		synchronized(players) {
			if(players.remove(player)) {
				//TODO jesli gra sie zaczela to nalezy ja zakonczyc bo stracilismy polaczenie z graczem
			}
		}
	}

	public void handleAction(GameAction action, int source) {
		synchronized (actions) {
			actions.add(action);
			sources.add(source);
			actions.notifyAll();
		}
	}

	public void run() {
		GameAction action;
		int source;
		while (true) {
			synchronized (actions) {
				while (actions.isEmpty()) {
					try {
						actions.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						tables.remove(this);
						for (PlayerConnection p : players) {
							p.end();
						}
						return;
					}
				}
				action = actions.remove(0);
				source = sources.remove(0);
			}
			if (action.getType() == GameActionType.CHAT)
				if ((Integer) action.getInfo()[0] != source)
					continue;
				else
					chatMessage(action);
			if (gameOnRun)
				gameHandleAction(action, source);
			else if (action.getType() == GameActionType.START_GAME) {
				isReady[source] = true;
				boolean allReady = true;
				for (int i = 0; i < players.size(); i++)
					if (!isReady[i])
						allReady = false;
				if (allReady)
					startGame();
			}
			action = null;
		}
	}

	private synchronized void gameHandleAction(GameAction action, int source) {
		if (gameState.getActivePlayer() != source)
			try {
				//System.out.println(gameState.getActivePlayer());
				players.get(source).sendAction(
						new GameAction(GameActionType.ERROR));
			} catch (IOException e) {
				e.printStackTrace();
			}
		recentAction[source] = action;
		notifyAll();

	}

	private List<String> generateNamesList() {
		LinkedList<String> names = new LinkedList<String>();
		for (PlayerConnection player : players)
			names.add(player.getName());
		return names;
	}

	private void startGame() {
		List<String> names = generateNamesList();
		gameState = new GameState(names);
		game = new Game(gameState, this, this);
		queuingCardsDecks = new DecksOfQueuingCardsBox(gameState);
		gameOnRun = true;
		for(PlayerConnection player : players)
			try {
				player.sendAction(new GameAction(GameActionType.START_GAME));
			} catch (IOException e) {
				e.printStackTrace();
			}
		game.startGame(names.size(), new StandardDeckOfDeliveryCards(), queuingCardsDecks);
	}

	private void chatMessage(GameAction action) {
		if (action.getType() != GameActionType.CHAT)
			throw new IllegalArgumentException("Not a chat action");
		for (PlayerConnection player : players)
			try {
				player.sendAction(action);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see queue_game.ActionCreator#getAction()
	 */
	public synchronized GameAction getAction() throws InterruptedException {
		int activePlayer = gameState.getActivePlayer();
		while (recentAction[activePlayer] == null)
			wait();
		GameAction action = recentAction[activePlayer];
		recentAction[activePlayer] = null;
		return action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see queue_game.Updater#update(queue_game.model.GameAction)
	 */
	public void update(GameAction action) {
		try {
			if (action.getType() == GameActionType.ERROR) {
				players.get(gameState.getActivePlayer()).sendAction(action);
				return;
			}
			if (action.getType() == GameActionType.DRAW_CARD) {
				players.get((Integer) action.getInfo()[0]).sendAction(action);
				return;
			}
			if(action.getType() == GameActionType.CARDS_PEEKED){
				players.get(gameState.getActivePlayer()).sendAction(action);
			}
			int ind = 0;
			for (PlayerConnection player : players){
				if(ind++ != gameState.getActivePlayer())
					player.sendAction(action);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
