/**
 * 
 */
package queue_game.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import queue_game.View;
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
	private List<View> views = new LinkedList<View>();
	private Class<?> expectedType = null;
	private Product selectedProduct = null;
	private Pawn selectedPawn = null;
	private ProductType selectedQueue = null;
	private Thread gameThread = null;

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

	/**
	 * @return the gameThread
	 */
	public Thread getGameThread() {
		return gameThread;
	}

	public void startGame(int nPlayers) {
		gameState.reset(nPlayers);
		gameThread = new Thread(this);
		gameThread.start();
	}
	private void updateViews(){
		for(View view : views)
			view.update();
	}

	public void run() {
		for (int day = 0; !gameOver(); day++) {
			gameState.setDayNumber(day);
			try {
				queuingUpPhase();
			} catch (InterruptedException e) {
				return;
			}
			deliveryPhase();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return;
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
	 * @throws InterruptedException 
	 */
	private synchronized ProductType requestQueue() throws InterruptedException{
		expectedType = ProductType.class;
<<<<<<< HEAD
=======
		System.out.println(expectedType);
>>>>>>> branch 'master' of https://github.com/michalseweryn/queue-game.git
		updateViews();
		while(selectedQueue == null)
				wait();
<<<<<<< HEAD
=======
		System.out.println("jest");
>>>>>>> branch 'master' of https://github.com/michalseweryn/queue-game.git
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
	 * @throws InterruptedException 
	 */
	private void queuingUpPhase() throws InterruptedException {
		gameState.setCurrentGamePhase(GamePhase.QUEUING_UP);
		int timeSinceLastPawnLocation = 0;
		outer: while (true) {
			for (int player = gameState.getGameOpeningMarker(); player < gameState
					.getNumberOfPlayers(); player = (player + 1)
					% gameState.getNumberOfPlayers()){
				if(gameState.getNumberOfPawns(player) > 0){
					System.out.println("kolejkę od " + player);
					gameState.setActivePlayer(player);
					gameState.putPlayerPawn(player, requestQueue());
					System.out.println("mam");
					timeSinceLastPawnLocation = 0;
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
		updateViews();
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
		updateViews();
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
<<<<<<< HEAD
		if (playerNo != gameState.getActivePlayer())
=======
		System.out.println("selected queue");
		if (playerNo != gameState.getActivePlayer()){
			System.out.println("Zły");
>>>>>>> branch 'master' of https://github.com/michalseweryn/queue-game.git
			return;
<<<<<<< HEAD
=======
		}
>>>>>>> branch 'master' of https://github.com/michalseweryn/queue-game.git
		
		if (expectedType == ProductType.class) {
			
			selectedQueue = destination;
			try {
<<<<<<< HEAD
=======
				System.out.println("notify");
>>>>>>> branch 'master' of https://github.com/michalseweryn/queue-game.git
				notifyAll();
				return;
			} finally {}
		}
		else System.out.println("chciałem " + expectedType);
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
	public void addView(View fakeView) {
		views.add(fakeView);
	}

}
