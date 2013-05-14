/**
 * 
 */
package queue_game.model;

import java.awt.Color;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * @author michal A model part of MVC. Illustrates current situation on Board.
 */
public class GameState {

	/**
	 * Counted from 0.
	 */
	public static final Color[] productColors = new Color[] { Color.BLUE,
			Color.GREEN, Color.PINK, Color.ORANGE, Color.MAGENTA };

	/**
	 * Counted from 1, 0 is speculator.
	 */
	public static final Color[] playerColors = new Color[] { Color.BLACK,
			Color.RED, Color.YELLOW, new Color(0, 128, 0),
			new Color(192, 128, 0), Color.BLUE };

	private int dayNumber;
	private int gameOpeningMarker;
	private int numberOfPlayers;
	private int activePlayer = 0;
	private boolean gameOver;
	private Store[] stores;
	private Integer numberOfProducts[] = new Integer[5];
	private ArrayList<Player> players = new ArrayList<Player>();
	private GamePhase currentGamePhase = null;

	public GameState() {
		stores = new Store[ProductType.values().length];
		int ind = 0;
		for (ProductType product : ProductType.values())
			stores[ind++] = new Store(product);
	}

	public void reset(int nPlayers) {
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
	}

	/**
	 * 
	 * Reset cards of all players.
	 * 
	 */
	public void resetCards() {
		for (Player pl : players) {
			pl.setDeck(new DeckOfCards());
			pl.getDeck().fill();
			pl.getDeck().shuffle();
		}
		for (Player p : players) {
			p.getDeck().getCards(p.getCardsOnHand());
		}
	}

	/**
	 * 
	 * Resets number of products with our favorite number.
	 * 
	 */
	public void resetNumberOfProducts() {
		for (ProductType i : ProductType.values()) {
			numberOfProducts[i.ordinal()] = 50;
		}
	}

	/**
	 * 
	 * Create new players and adds them pawns.
	 * 
	 */
	public void resetPlayers() {
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
	public void resetShoppingList() {
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

	
	public void setGameOver() {
		gameOver = true;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * @return the gameOpeningMarker
	 */
	public int getGameOpeningMarker() {
		return gameOpeningMarker;
	}

	/**
	 * @param gameOpeningMarker
	 *            the gameOpeningMarker to set
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
	 * @param dayNumber
	 *            the dayNumber to set
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

	public DeckOfCards getDeck(int playerNr) {
		return players.get(playerNr).getDeck();
	}

	public Store getStore(ProductType product) {
		return stores[product.ordinal()];
	}

	public void setNumberOfPlayers(int nPlayers) {
		numberOfPlayers = nPlayers;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public GamePhase getCurrentGamePhase() {
		return currentGamePhase;
	}

	public void setCurrentGamePhase(GamePhase phase) {
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

	public ArrayList<Player> getPlayersList() {
		return players;
	}

	public void setPlayersList(ArrayList<Player> players) {
		this.players = players;
	}

	/**
	 * 
	 * Puts black pawns of speculators to all queues.
	 * 
	 */
	public void putPawnofSpeculator(ProductType dest) {
		this.getStore(dest).getQueue().add(-1);
	}

	/**
	 * Puts pawn of one player to given queue
	 * 
	 * @param player
	 * @param destination
	 */

	public void putPlayerPawn(int player, ProductType destination) {
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
	 * @param type
	 */
	public void sell(ProductType type) {
		Store store = getStore(type);
		if (store.getQueue().isEmpty())
			throw new IllegalArgumentException("Empty queue");
		int player = getStore(type).getQueue().pop();
		store.removeProducts(1);
		if (player >= 0 && player < numberOfPlayers) {
			int nPawns = getNumberOfPawns(player);
			players.get(player).setNumberOfPawns(nPawns + 1);
			players.get(player).addProduct(type);
		}
		if (player == -1) {
			this.putPawnofSpeculator(type);
		}

	}

}
