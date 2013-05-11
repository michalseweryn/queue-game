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
	private int dayNumber;
	private int gameOpeningMarker;
	private int numberOfPlayers;
	private int initialNumberOfPawns = 5;
	private int activePlayer = 0;
	private boolean gameOver;
	private Store[] stores;
	private Integer numberOfProducts[]= new Integer[5];
	private ArrayList<Player> players=new ArrayList<Player>();
	private GamePhase currentGamePhase = null;
	
	public GameState(){
		stores = new Store[ProductType.values().length];
		int ind = 0;
		for(ProductType product : ProductType.values())
			stores[ind++] = new Store(product);
	}
	
	public void reset(int nPlayers){
		dayNumber = 0;
		gameOpeningMarker = 0;
		activePlayer = 0;
		numberOfPlayers = nPlayers;
		currentGamePhase = null;
		gameOver = false;
		stores = new Store[ProductType.values().length];
		int ind = 0;
		for(ProductType product : ProductType.values())
			stores[ind++] = new Store(product);
		
		for(ProductType i : ProductType.values()){
			numberOfProducts[i.ordinal()]=50;
		}
		for(int i = 0; i < numberOfPlayers; i++){
			players.add(new Player());
			players.get(i).setNumberOfPawns(initialNumberOfPawns);
		}
	}
	
	public void setGameOver(){
		gameOver = true;
	}
	
	public boolean isGameOver(){
		return gameOver;
	}
	/**
	 * @return the gameOpeningMarker
	 */
	public int getGameOpeningMarker() {
		return gameOpeningMarker;
	}
	
	
	/**
	 * @param gameOpeningMarker the gameOpeningMarker to set
	 */
	public void setGameOpeningMarker(int gameOpeningMarker) {
		this.gameOpeningMarker = gameOpeningMarker;
	}
	
	/**
	 * @return the dayNumber
	 */
	public int getDayNumber() {
		return dayNumber;
	}

	/**
	 * @param dayNumber the dayNumber to set
	 */
	public void setDayNumber(int dayNumber) {
		this.dayNumber = dayNumber;
	}

	/**
	 * @return the stores
	 */
	public Store[] getStores() {
		return stores;
	}
	
	public DeckOfCards getDeck(int playerNr){
		return players.get(playerNr).getDeck();
	}

	public Store getStore(ProductType product){
		return stores[product.ordinal()];
	}
	public void setNumberOfPlayers(int nPlayers){
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

	public int getNumberOfPawns(int player) {
		return players.get(player).getNumberOfPawns();
	}
	
	public Integer[] getNumberOfProducts() {
		return numberOfProducts;
	}
	public void setNumberOfProducts(Integer[] numberOfProducts) {
		this.numberOfProducts = numberOfProducts;
	}
	/**
	 *  Puts pawn of one player to given queue
	 * @param player
	 * @param destination
	 */

	public void putPlayerPawn(int player, ProductType destination) {
		if(player < 0 || player >= numberOfPlayers)
			throw new IllegalArgumentException("No such Player: " + player);
		int nPawns = getNumberOfPawns(player);
		if(nPawns == 0)
			throw new IllegalArgumentException("Player has no more pawns: " + player);
		players.get(player).setNumberOfPawns(nPawns - 1);
		this.getStore(destination).getQueue().add(player);
	}
	/**
	 * @param type
	 */
	public void sell(ProductType type) {
		Store store = getStore(type);
		if(store.getQueue().isEmpty())
			throw new IllegalArgumentException("Empty queue");
		int player = getStore(type).getQueue().pop();
		store.removeProducts(1);
		if(player >= 0 && player < numberOfPlayers){
			int nPawns = getNumberOfPawns(player);
			players.get(player).setNumberOfPawns(nPawns + 1);
		}
			
			
		
	}
	

}
