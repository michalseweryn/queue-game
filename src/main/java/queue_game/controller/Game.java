/**
 * 
 */
package queue_game.controller;

import queue_game.model.*;


/**
 * @author michal
 * 
 * Controller of game. Handles all players actions, verifies them, according to current game phase.   
 */
public class Game {
	private GameState gameState;
	
	public Game(){
		gameState = new GameState();
	}
	
	public GameState getGameState(){
		return gameState;
	}
	
	public void start(){
		
	}
	/**
	 * Method for handling players' product selections (e.g. on queuing up)
	 * @param playerNo ID of player selecting a product
	 * @param product selected product
	 */
	public void productSelected(int playerNo, ProductType product){
		System.out.println("Player " + playerNo + " selected " + product);
	}
	/**
	 * Method for handling players' pawn selections (e.g. on queue jumping)
	 * @param playerNo ID of player selecting a product
	 * @param product selected product
	 */
	public void pawnSelected(int playerNo, ProductType queueDestination, int position){
		
	}
}
