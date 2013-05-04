/**
 * 
 */
package queue_game.controller;

import java.util.ArrayList;

import queue_game.model.GamePhase;
import queue_game.model.GameState;
import queue_game.model.ProductType;
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
		gameState.setNumberOfPlayers(2);
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		for (int i = 0; i < gameState.getNumberOfPlayers(); i++)
			tmp.add(5);
		gameState.setAmountOfPawns(tmp);
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
		int nPlayers = gameState.getNumberOfPlayers();
		int id = gameState.getActivePlayer();
		int tmpid =id;
		Integer tmp = gameState.getAmountOfPawns().get(id);
		if (gameState.getCurrentGamePhase().equals(GamePhase.QUEUING_UP)) {
			if (!tmp.equals(new Integer(0))) {
				gameState.getStore(destination).addPawn(id);
				gameState.getAmountOfPawns().set(id, tmp - 1);
				id=id+1;
				id= id % nPlayers;
				while (gameState.getAmountOfPawns().get(id).equals(new Integer(0))) {
					id = id+1;
					id =id%nPlayers;
					if(tmpid==id){
						gameState.setCurrentGamePhase(GamePhase.DELIVERY);
						break;
					}
				}
				gameState.setActivePlayer((id) % nPlayers);
				view.update();

			} 
			
		}
		if (view != null) {
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
