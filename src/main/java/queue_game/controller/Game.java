/**
 * 
 */
package queue_game.controller;

import java.util.Random;

import queue_game.model.GamePhase;
import queue_game.model.GameState;
import queue_game.model.ProductType;
import queue_game.model.Store;
import queue_game.view.JBoard;

/**
 * @author michal
 * 
 *         Controller of game. Handles all players actions, verifies them,
 *         according to current game phase.
 */
public class Game implements Runnable {
	private GameState gameState;
	private JBoard view = null;
	private Class<?> expectedType = null;
	private Product selectedProduct = null;
	private Pawn selectedPawn = null;
	private ProductType selectedQueue = null;

	/**
	 * Mini-class representing Products from stores selected by users.
	 * 
	 * @author michal
	 * 
	 */
	private static class Product {
		ProductType store;
		ProductType type;

		public Product(ProductType store, ProductType type) {
			this.store = store;
			this.type = type;
		}
	}

	/**
	 * Mini-class representing Pawns from queues selected by users.
	 * 
	 * @author michal
	 * 
	 */
	private static class Pawn {
		ProductType queue;
		int position;

		public Pawn(ProductType queue, int position) {
			this.queue = queue;
			this.position = position;
		}
	}

	public Game() {
		gameState = new GameState();
	}

	public GameState getGameState() {
		return gameState;
	}

	public void startGame(int nPlayers) {
		gameState.reset(nPlayers);
		new Thread(this).start();
	}
	private void updateViews(){
		if(view != null)
			view.update();
	}

	public void run() {
		for (int day = 0; !gameOver(); day++) {
			gameState.setDayNumber(day);
			queuingUpPhase();
			deliveryPhase();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			openingStoresPhase();
		}
		gameState.setGameOver();
		updateViews();
	}
	/**
	 *  Waits for selection of  queue by active player.
	 *  
	 * @return destination of selected queue.
	 */
	private synchronized ProductType requestQueue(){
		expectedType = ProductType.class;
		while(selectedQueue == null)
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		expectedType = null;
		ProductType queue = selectedQueue;
		selectedQueue = null;
		return queue;
		
	}
	/**
	 *  Waits for selection of product by active player.
	 *  
	 * @return selected product.
	 */
	private synchronized Product requestProduct(){
		expectedType = Product.class;
		while(selectedQueue == null)
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		expectedType = null;
		Product product = selectedProduct;
		selectedProduct = null;
		return product;
		
	}
	/**
	 *  Waits for selection of pawn by active player.
	 *  
	 * @return selected pawn.
	 */
	private synchronized Pawn requestPawn(){
		expectedType = Product.class;
		while(selectedQueue == null)
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		expectedType = null;
		Pawn pawn = selectedPawn;
		selectedPawn = null;
		return pawn;
		
	}
	/**
	 * First Phase of Day.
	 */
	private void queuingUpPhase() {
		gameState.setCurrentGamePhase(GamePhase.QUEUING_UP);
		int timeSinceLastPawnLocation = 0;
		outer: while (true) {
			for (int player = gameState.getGameOpeningMarker(); player < gameState
					.getNumberOfPlayers(); player = (player + 1)
					% gameState.getNumberOfPlayers()){
				if(gameState.getNumberOfPawns(player) > 0){
					gameState.setActivePlayer(player);
					gameState.putPlayerPawn(player, requestQueue());
					timeSinceLastPawnLocation = 0;
					updateViews();
				} else {
					timeSinceLastPawnLocation++;
				}
				if(timeSinceLastPawnLocation > gameState.getNumberOfPlayers())
					break outer;
			}
		}

	}

	/**
	 * @return true if game is over and false otherwise.
	 */
	private boolean gameOver() {
		return gameState.getDayNumber() > 5;
	}

	/*
	 * int nPlayers = gameState.getNumberOfPlayers(); int id =
	 * gameState.getActivePlayer(); int tmpid =id; Integer tmp =
	 * gameState.getAmountOfPawns().get(id); if
	 * (gameState.getCurrentGamePhase().equals(GamePhase.QUEUING_UP)) { if
	 * (!tmp.equals(new Integer(0))) {
	 * gameState.getStore(destination).addPawn(id);
	 * gameState.getAmountOfPawns().set(id, tmp - 1); id=id+1; id= id %
	 * nPlayers; while (gameState.getAmountOfPawns().get(id).equals(new
	 * Integer(0))) { id = id+1; id = id%nPlayers; if(tmpid==id){
	 * gameState.setCurrentGamePhase(GamePhase.DELIVERY); deliveryPhase(); }
	 * 
	 * 
	 * if(tmpid==id){ gameState.setCurrentGamePhase(GamePhase.OPENING);
	 * openingStoresPhase(); break; } } gameState.setActivePlayer((id) %
	 * nPlayers); view.update();
	 * 
	 * }
	 * 
	 * } if (view != null) { view.repaint(); }
	 * 
	 * }
	 */

	/**
	 * Randomizes 3 stores with repetitions and delivers products to them.
	 * 
	 * @author krzysiek
	 */
	public void deliveryPhase() {
		int rand;
		Random rG = new Random();
		for (int i = 0; i < 3; i++) {
			rand = rG.nextInt(5);
			Store deliveredStore = gameState
					.getStore(ProductType.values()[rand]);
			deliveredStore.addProducts(1);
		}
		view.update();
	}

	/**
	 * For each store with products, removes the right amount of product and
	 * pawns.
	 * 
	 * @author Jan
	 */
	public void openingStoresPhase() {
		for(ProductType type : ProductType.values())
			while(gameState.getStore(type).getQueue().size() > 0 && gameState.getStore(type).getNumberOf() > 0)
				gameState.sell(type);
		view.update();
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
	public synchronized void queueSelected(int playerNo, ProductType destination) {
		System.out.println("selected queue");
		if (playerNo != gameState.getActivePlayer())
			return;
		if (expectedType == ProductType.class) {
			
			selectedQueue = destination;
			try {
				return;
			} finally {notifyAll();}
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
		if (playerNo != gameState.getActivePlayer())
			return;
		
		if(expectedType == Pawn.class){
			selectedPawn = new Pawn(queueDestination, position);
			notifyAll();
		}

	}

	/**
	 * Method for handling players' product selections (e.g. on opening of stores)
	 * 
	 * @param playerNo
	 *            ID of player selecting a product
	 * @param product
	 *            selected product
	 */
	public void productSelected(int playerNo, ProductType type,
			ProductType store) {
		if (playerNo != gameState.getActivePlayer())
			return;
		
		if(expectedType == Pawn.class){
			selectedProduct = new Product(type, store);
			notifyAll();
		}

	}

	/**
	 * Sets object to be informed about changes in model;
	 */
	public void addView(JBoard view) {
		this.view = view;
	}

}
