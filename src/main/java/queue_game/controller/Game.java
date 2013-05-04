/**
 * 
 */
package queue_game.controller;

import queue_game.model.*;
import queue_game.view.JBoard;

/**
 * @author michal
 * 
 *         Controller of game. Handles all players actions, verifies them,
 *         according to current game phase.
 */
public class Game {
	private GameState gameState;
	private JBoard view = null;
	public Game() {
		gameState = new GameState();
	}

	public GameState getGameState() {
		return gameState;
	}

	public void start(int nPlayers) {
		gameState.setNumberOfPlayers(nPlayers);
		gameState.setCurrentGamePhase(GamePhase.QUEUING_UP);
	}

	/**
	 * Method for handling players' queue selections (e.g. on queuing up)
	 * 
	 * @param playerNo
	 *            ID of player selecting a product
	 * @param product
	 *            queue destination type or null, when destination is outdoor
	 *            market.
	 */
	public void queueSelected(int playerNo, ProductType destination) {
		int id = gameState.getActivePlayer();
		int nPlayers = gameState.getNumberOfPlayers();
		gameState.setActivePlayer((id + 1) % nPlayers);
		if(gameState.getCurrentGamePhase().equals(GamePhase.QUEUING_UP)){
			gameState.getStore(destination).addPawn(id);
			view.update();
		}
		if (view != null){
			view.repaint();
		}
			
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

	/**
	 * Sets object to be informed about changes in model;
	 */
	public void addView(JBoard view) {
		this.view = view;
	}

}
