/**
 * 
 */
package queue_game.controller;

import queue_game.model.*;

/**
 * @author michal
 * 
 *         Controller of game. Handles all players actions, verifies them,
 *         according to current game phase.
 */
public class Game {
	private GameState gameState;

	public Game() {
		gameState = new GameState();
	}

	public GameState getGameState() {
		return gameState;
	}

	public void start(int nPlayers){
		gameState.setNumberOfPlayers(nPlayers);
		gameState.setCurrentGamePhase(GamePhase.QUEUING_UP);
	}

	/**
	 * Method for handling players' queue selections (e.g. on queuing up)
	 * 
	 * @param playerNo
	 *            ID of player selecting a product
	 * @param product
	 *            queue destination type or null, when destination is outdoor market.
	 */
	public void queueSelected(int playerNo, ProductType destination) {
		String queue = (destination != null) ? destination.toString()
				: "OUTDOOR_MARKET";
		System.out.println("Player " + playerNo + " selected queue to "
				+ queue + ".");
		int id = gameState.getActivePlayer();
		int nPlayers = gameState.getNumberOfPlayers();
		gameState.setActivePlayer((id + 1) % nPlayers);
				
	}

	/**
	 * Method for handling players' pawn selections (e.g. on queue jumping)
	 * 
	 * @param playerNo
	 *            ID of player selecting a product
	 * @param product
	 *            selected product
	 */
	public void pawnSelected(int playerNo, ProductType queueDestination,
			int position) {

	}
}
