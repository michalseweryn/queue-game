/**
 * 
 */
package queue_game.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author michal A model part of MVC. Illustrates current situation on Board.
 */
public class GameState {

	/**
	 * Counted from 0.
	 */
	public static final Color[] productColors = new Color[] { new Color(0x5DC049), 
		new Color(0xFFB451), new Color(0xDF574E), new Color(0x57ACB0), Color.MAGENTA };

	/**
	 * Counted from 0
	 */
	public static final Color[] playerColors = new Color[] {new Color(0xff0000), new Color(0xffff00), new Color(0, 128, 0),
			new Color(192, 128, 0), new Color(0x0670C7) };

	private int dayNumber;
	private int gameOpeningMarker;
	private int numberOfPlayers;
	private int activePlayer = 0;
	private boolean gameOver;
	private Store[] stores;
	private int[] numberOfProductsLeft = new int[5];
	private ArrayList<Player> players = new ArrayList<Player>();
	private GamePhase currentGamePhase = null;
	private ArrayList<GameAction> actions = new ArrayList<GameAction>();
	private List<DeliveryCard> currentDeliveryList;
	private Store market;
	private String message = "";
	
	public GameState(){
		stores = new Store[ProductType.values().length];
		int ind = 0;
		for (ProductType product : ProductType.values())
			stores[ind++] = new Store(product);
		market = new Store(null);
	}

	public synchronized void reset(int nPlayers) {
		dayNumber = 0;
		gameOpeningMarker = 0;
		activePlayer = 0;
		numberOfPlayers = nPlayers;
		currentGamePhase = null;
		gameOver = false;
		stores = new Store[ProductType.values().length];
		int ind = 0;
		for (ProductType product : ProductType.values())
			stores[ind++] = new Store(product);
		market = new Store(null);
	}

	/**
	 * 
	 * Reset cards of all players.
	 * 
	 */
	public synchronized void resetQueuingCards() {
		for (Player pl : players) {
			pl.setDeck(new DeckOfQueuingCards());
			pl.getDeck().fill();
			pl.getDeck().shuffle();
		}
		for (Player p : players) {
			p.getDeck().getCards(p.getCardsOnHand());
		}
	}
	
	/**
	 * It doesn't work yet!
	 * 
	 * Resets cards of all players due to saturday rules, which means
	 * that cards which a player is now holding on hand are to be set
	 * on the end of the lst. 
	 */
	public synchronized void resetQueuingCardsOnSaturday() {
		for (Player pl : players) {
			List<QueuingCard> tempList = pl.getCardsOnHand();
			pl.setDeck(new DeckOfQueuingCards());
			System.out.println(pl.getDeck().size());
			pl.getDeck().fill();
			for (QueuingCard dC : tempList)
				pl.getDeck().remove(dC);
			pl.getDeck().shuffle();
			pl.getDeck().addListToTheEnd(tempList);
			
		}
		for (Player pl : players) {
			pl.setCardsOnHand(new ArrayList<QueuingCard>());
			pl.getDeck().getCards(pl.getCardsOnHand());
		}
	}

	/**
	 * 
	 * Resets number of products with our favorite number.
	 * 
	 */
	public synchronized void resetNumberOfProductsLeft() {
		for (ProductType i : ProductType.values()) {
			numberOfProductsLeft[i.ordinal()] = 12;
		}
	}

	/**
	 * 
	 * Create new players and adds them pawns.
	 * 
	 */
	public synchronized void resetPlayers() {
		players.clear();
		int initialNumberOfPawns = 5;
		for (int i = 0; i < numberOfPlayers; i++) {
			players.add(new Player(i, "Gracz " + (i + 1)));  
			players.get(i).setNumberOfPawns(initialNumberOfPawns);
		}
	}

	/**
	 * 
	 * Create random shopping list and set gameOpeningMarker.
	 * 
	 */
	public synchronized void resetShoppingList() {
		int lists[][]= new int[][] {{4,0,2,1,3},{3,4,1,0,2},{2,3,0,4,1},{1,2,4,3,0},{0,1,3,2,4}};
		Random r = new Random();
		int rand=0;
		rand=r.nextInt(6-numberOfPlayers);
		int tmp[][]=Arrays.copyOfRange(lists,rand, rand+numberOfPlayers);
		Collections.shuffle(Arrays.asList(tmp));
		for (int i = 0; i < numberOfPlayers; i++) {
			players.get(i).setShoppingList(tmp[i]);
		}
		int marker=0;
		for(int i=0;i<numberOfPlayers;i++){
			int tab[] = new int[]{0,1,3,2,4};
			if(Arrays.equals(tmp[i],tab)){
				marker=i;
				break;
			}
		}
		this.setGameOpeningMarker(marker);
	}

	
	public synchronized void setGameOver() {
		gameOver = true;
	}

	public synchronized boolean isGameOver() {
		return gameOver;
	}

	/**
	 * @return the gameOpeningMarker
	 */
	public synchronized int getGameOpeningMarker() {
		return gameOpeningMarker;
	}

	/**
	 * @param gameOpeningMarker
	 *            the gameOpeningMarker to set
	 */
	public synchronized void setGameOpeningMarker(int gameOpeningMarker) {
		this.gameOpeningMarker = gameOpeningMarker;
	}

	/**
	 * @return the dayNumber
	 */
	public synchronized int getDayNumber() {
		return dayNumber;
	}

	/**
	 * @param dayNumber
	 *            the dayNumber to set
	 */
	public synchronized void setDayNumber(int dayNumber) {
		this.dayNumber = dayNumber;
	}

	/**
	 * @return the stores
	 */
	public synchronized Store[] getStores() {
		return stores;
	}
	
	/**
	 * @return the market
	 */
	public synchronized Store getOutDoorMarket() {
		return market;
	}
	public synchronized DeckOfQueuingCards getDeck(int playerNr) {
		return players.get(playerNr).getDeck();
	}

	public synchronized Store getStore(ProductType product) {
		if(product == null)
			return market;
		return stores[product.ordinal()];
	}

	public synchronized void setNumberOfPlayers(int nPlayers) {
		numberOfPlayers = nPlayers;
	}

	public synchronized int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public synchronized GamePhase getCurrentGamePhase() {
		return currentGamePhase;
	}

	public synchronized void setCurrentGamePhase(GamePhase phase) {
		currentGamePhase = phase;
	}

	public synchronized void setCurrentDeliveryList (
			List<DeliveryCard> currentDeliveryList) {
	this.currentDeliveryList = currentDeliveryList;
	}
	
	public synchronized List<DeliveryCard> getCurrentDeliveryList () {
		return currentDeliveryList;
	}
	
	/**
	 * @return ID of player whose turn is now.
	 */
	public synchronized int getActivePlayer() {
		return activePlayer;
	}

	/**
	 * Sets ID of player whose turn is now.
	 */
	public synchronized void setActivePlayer(int id) {
		activePlayer = id;
	}

	public synchronized int getNumberOfPawns(int player) {
		return players.get(player).getNumberOfPawns();
	}

	public synchronized int[] getNumberOfProductsLeft() {
		return numberOfProductsLeft;
	}
	
	public synchronized int getNumberOfProductsLeft(int numberOfStore) {
		return numberOfProductsLeft[numberOfStore];
	}

	public synchronized void setNumberOfProductsLeft(int[] numberOfProductsLeft) {
		this.numberOfProductsLeft = numberOfProductsLeft;
	}
	
	public synchronized void setNumberOfProductsLeft(int index, int numberOfProductsLeft) {
		this.numberOfProductsLeft[index] = numberOfProductsLeft;
	}

	public synchronized ArrayList<Player> getPlayersList() {
		return players;
	}

	public synchronized void setPlayersList(ArrayList<Player> players) {
		this.players = players;
	}

	public synchronized void addGameAction(GameAction action) {
		actions.add(action);
	}

	public synchronized List<GameAction> getPlayerActions() {
		return actions;
	}
	public void close(ProductType type){
		getStore(type).setClosed(true);
	}
	public void reverse(ProductType type){
		Collections.reverse(getStore(type).getQueue());
	}
	/**
	 * 
	 * Set stores open.
	 * 
	 */
		public void openStores(){
			for(ProductType p : ProductType.values()){
				getStore(p).setClosed(false);
			}
		}
	/**
	 * 
	 * @param orig - Store from product has been taken
	 * @param dest - store where product is give
	 * @param product - type of product which is moved
	 */
	public void transferToAnotherStore(ProductType orig,ProductType dest, ProductType product){
		getStore(orig).removeProduct(product);
		getStore(dest).addProduct(product);
	}
	/**
	 * Moves pawn.
	 * @param orig - original store of this pawn
	 * @param pos - position in original queue
	 * @param dest - destination of pawn
	 * @param npos - new position of pawn
	 */
	public void movePawn(ProductType orig, int pos, ProductType dest, int npos){
		Integer pawn = getStore(orig).getQueue().remove(pos);
		getStore(dest).getQueue().add(npos, pawn);
	}

	/**
	 * 
	 * Puts black pawns of speculators to all queues.
	 * 
	 */
	public synchronized void putSpeculators() {
		for(Store store : stores)
			store.getQueue().add(-1);
	}
	
	/**
	 *  Transfers given amount of product to corresponding store.
	 * @param product type of delivered product
	 * @throws IllegalArgumentException too large amount of product.
	 */
	public void transferProductToStore(ProductType product, int amount) throws IllegalArgumentException{
		if(numberOfProductsLeft[product.ordinal()] == 0)
			throw new IllegalArgumentException("No more pieces of product left: " + product);
		numberOfProductsLeft[product.ordinal()] -= amount;
		stores[product.ordinal()].addProducts(amount);
	}

	/**
	 * Puts pawn of one player to given queue
	 * 
	 * @param player
	 * @param destination
	 */

	public synchronized void putPlayerPawn(int player, ProductType destination) {
		if (player < 0 || player >= numberOfPlayers)
			throw new IllegalArgumentException("No such Player: " + player);
		int nPawns = getNumberOfPawns(player);
		if (nPawns == 0)
			throw new IllegalArgumentException("Player has no more pawns: "
					+ player);
		players.get(player).setNumberOfPawns(nPawns - 1);
		this.getStore(destination).getQueue().add(player);
	}
	/**
	 * 
	 */
	public void removePlayerPawn(int player, int position,
			ProductType destination) {
		if(player<0 || player>=numberOfPlayers)
			throw new IllegalArgumentException("No such player"+ player);
		int nPawns=getNumberOfPawns(player);
		if(nPawns==5){
			throw new IllegalArgumentException("Player has no Pawn on Map"+ player);
		}
		players.get(player).setNumberOfPawns(nPawns+1);
		this.getStore(destination).getQueue().remove(position);
	}

	/**
	 * @param type
	 * 
	 */
	public synchronized void sell(ProductType offeredProduct, ProductType soldProduct) throws IllegalArgumentException{
		if (getStore(offeredProduct).getQueue().isEmpty())
			throw new IllegalArgumentException("Empty queue: " + offeredProduct);
		int player = getStore(offeredProduct).getQueue().peek();
		getStore(offeredProduct).removeProduct(soldProduct);
		
		if (player >= 0 && player < numberOfPlayers) {
			getStore(offeredProduct).getQueue().remove();
			
			players.get(player).addPawn();
			players.get(player).addProduct(soldProduct);
		}
		if (player == -1) {
			int queueLength = getStore(offeredProduct).getQueue().size();
			movePawn(offeredProduct, 0, offeredProduct, queueLength - 1);
		}
	}
	
	/**
	 * Method for outdoormarket.
	 * 
	 * @author Jan
	 */
	public synchronized boolean trade(ProductType soldProduct, Collection<ProductType> offeredProducts) 
			throws IllegalArgumentException{
		
		int player = getActivePlayer();
		getOutDoorMarket().removeProduct(soldProduct);
		players.get(player).addPawn();
		players.get(player).addProduct(soldProduct);
		players.removeAll(offeredProducts);
		for(ProductType i: offeredProducts)
				getOutDoorMarket().addProduct(i);
		
		
		return false;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	public String getMessage(){
		return message;
	}

	/**
	 * @param type
	 * @return
	 */
	public boolean wasDelivered(ProductType type) {
		for(DeliveryCard card : currentDeliveryList)
			if(card.getProductType().equals(type))
				return true;
		return false;
	}

}
