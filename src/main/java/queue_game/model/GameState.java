/**
 * 
 */
package queue_game.model;

import java.util.ArrayList;

/**
 * @author michal
 * A model part of MVC. Illustrates current situation on Board.
 */
public class GameState {
	
	private int numberOfPlayers;
	private int activePlayer = 0;
	private Store[] stores;
	private ArrayList<Integer> amountOfPawns=new ArrayList<Integer>();
	private GamePhase currentGamePhase = null;
	public GameState(){
		stores = new Store[ProductType.values().length];
		int ind = 0;
		for(ProductType product : ProductType.values())
			stores[ind++] = new Store(product);
	}

	/**
	 * @return the stores
	 */
	public Store[] getStores() {
		return stores;
	}

	public Store getStore(ProductType product){
		return stores[product.ordinal()];
	}
	public void setNumberOfPlayers(int nPlayers){
		amountOfPawns = new ArrayList<Integer>();
		for(int i = 0; i < nPlayers; i++)
			amountOfPawns.add(5);
		numberOfPlayers = nPlayers;
	}
	
	public int getNumberOfPlayers(){
		return numberOfPlayers;
	}
	
	public GamePhase getCurrentGamePhase(){
		return currentGamePhase;
	}
	
	public void setCurrentGamePhase(GamePhase phase){
		currentGamePhase = phase;
	}

	/**
	 * @return ID of player whose turn is now.  
	 */
	public int getActivePlayer() {
		return activePlayer;
	}
	
	/**
	 * Sets ID of player whose turn is now.  
	 */
	public void setActivePlayer(int id) {
		activePlayer = id;
	}

	public ArrayList<Integer> getAmountOfPawns() {
		return amountOfPawns;
	}

	public void setAmountOfPawns(ArrayList<Integer> amountOfPawns) {
		this.amountOfPawns = amountOfPawns;
	}

	

}
