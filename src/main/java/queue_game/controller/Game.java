/**
 * 
 */
package queue_game.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import queue_game.model.DeckOfCards;
import queue_game.model.GamePhase;
import queue_game.model.GameState;
import queue_game.model.Player;
import queue_game.model.GameAction;
import queue_game.model.GameActionType;
import queue_game.model.ProductType;
import queue_game.model.QueuingCard;
import queue_game.model.Store;
import queue_game.view.View;

/**
 * @author michal
 * 
 *         Controller of game. Handles all players actions, verifies them,
 *         according to current game phase.
 */
public class Game implements Runnable {
	private GameState gameState;
	private int nPlayers;
	private List<View> views = new LinkedList<View>();
	private Class<?> expectedType = null;
	private ProductType selectedQueue = null;
	private Thread gameThread = null;
	private QueuingCard selectedQueuingCard = null;
	private boolean iPass[] = new boolean[6];
	private boolean pass = false;

	public Game() {
		gameState = new GameState();
	}

	/**
	 * Creates new thread for the game.
	 */
	public void startGame(int nPlayers) {
		gameState.reset(nPlayers);
		this.nPlayers = nPlayers;
		gameThread = new Thread(this);
		gameThread.start();
	}

	/**
	 * All Phases of all days.
	 */
	public void run() {
		try {
			PreparingToGamePhase();
			for (int day = 0; !gameOver(); day++) {
				gameState.setDayNumber(day);
				if (day != 0){
					queuingUpPhase();
				}
				deliveryPhase();
				queueJumpingPhase();
				openingStoresPhase();
				PCTPhase();
			}
			gameState.setGameOver();
			newAction(GameActionType.GAME_OVER);
			updateViews();
		} catch (InterruptedException e) {
			return;
		}
	}

	/**
	 * 
	 * Prepares game to GamePhase: players,products,pawns, shopping_list.
	 * @throws InterruptedException 
	 * 
	 */
	public void PreparingToGamePhase() throws InterruptedException {
		gameState.reset(nPlayers);
		gameState.resetNumberOfProducts();
		gameState.resetPlayers();
		gameState.resetCards();
		gameState.resetShoppingList();
		queuingUpPhase();
		for (ProductType pt : ProductType.values()) {
			gameState.putPawnofSpeculator(pt);
			newAction(GameActionType.PAWN_PLACED, 0, pt.ordinal());
		}
	}

	/**
	 * First Phase of Day. Players select queues to place their pawns while
	 * there are any pawns left.
	 * 
	 * @throws InterruptedException
	 */
	private void queuingUpPhase() throws InterruptedException {
		gameState.setCurrentGamePhase(GamePhase.QUEUING_UP);
		int timeSinceLastPawnLocation = 0;
		outer: while (true) {
			for (int player = gameState.getGameOpeningMarker(); player < gameState
					.getNumberOfPlayers(); player = (player + 1)
					% gameState.getNumberOfPlayers()) {
				if (gameState.getNumberOfPawns(player) > 0) {
					gameState.setActivePlayer(player);
					ProductType queue = requestQueue();
					gameState.putPlayerPawn(player, queue);
					newAction(GameActionType.PAWN_PLACED, player + 1, queue.ordinal());
					timeSinceLastPawnLocation = 0;
				} else {
					timeSinceLastPawnLocation++;
				}
				if (timeSinceLastPawnLocation > gameState.getNumberOfPlayers())
					break outer;
			}
		}
	}

	/**
	 * Second Phase of Day. Randomizes 3 stores with repetitions and delivers
	 * products to them and decreases number of products.
	 * 
	 * @author krzysiek & Helena
	 */
	public void deliveryPhase() {
		int rand;
		Random rG = new Random();
		for (int i = 0; i < 3; i++) {
			rand = rG.nextInt(5);
			ProductType type = ProductType.values()[rand];
			Store deliveredStore = gameState.getStore(type);
			deliveredStore.addProducts(1);
			newAction(GameActionType.PRODUCT_DELIVERED, type.ordinal(), 1);
			int[] numberOfProducts = gameState.getNumberOfProducts();
			numberOfProducts[type.ordinal()] = numberOfProducts[type.ordinal()] - 1;
			gameState.setNumberOfProducts(numberOfProducts);
		}
	}
	/**
	 * @author piotr
	 * 
	 * 
	 */
	private void PCTPhase(){
		gameState.setCurrentGamePhase(GamePhase.PCT);
		prepareToQueueJumping();
	}

	/**
	 * @author piotr
	 * Third Phase of Day. Each player either plays card or passes. Phase is
	 * over when all players have passed or there are no cards left.
	 */
	public void queueJumpingPhase() throws InterruptedException{
		gameState.setCurrentGamePhase(GamePhase.JUMPING);
		final int numOfPlayers = gameState.getNumberOfPlayers();
		ArrayList<QueuingCard> cardsOnHand;
		QueuingCard current;
		while (true) {
			boolean allPassed = true;
			for (int player = gameState.getGameOpeningMarker(), i = 0; i < numOfPlayers; i++, player = (player + 1)
					% numOfPlayers) {
				cardsOnHand = this.getGameState().getPlayersList().get(player)
						.getCardsOnHand();
				DeckOfCards myDeck = this.getGameState().getDeck(player);
				if (!iPass[player] && cardsOnHand.size() > 0) {
					gameState.setActivePlayer(player);
					current = requestQueuingCard();
					if (current == null) {
						myDeck.iPass(cardsOnHand);
						newAction(GameActionType.PASSED, player + 1);
						continue;
					}
					cardsOnHand.remove(current);
					allPassed = false;
					switch (current) {
					case CLOSED_FOR_STOCKTAKING:
						System.out.println("CLOSED");
						newAction(GameActionType.CARD_PLAYED, player + 1, current.ordinal());
						break;
					case COMMUNITY_LIST:
						ProductType queue = requestQueue();
						Collections.reverse(gameState.getStore(queue).getQueue());
						newAction(GameActionType.CARD_PLAYED, player + 1, current.ordinal(), queue.ordinal());
						System.out.println("USING COMMUNITY LIST");
						break;
					case CRITISIZING_AUTHORITIES:
						newAction(GameActionType.CARD_PLAYED, player + 1, current.ordinal());
						System.out.println("AUTHORITIES");
						break;
					case DELIVERY_ERROR:
						newAction(GameActionType.CARD_PLAYED, player + 1, current.ordinal());
						System.out.println("DELIVERY");
						break;
					case INCREASED_DELIVERY:
						Store store = gameState.getStore(requestQueue());
						while(store.getNumberOf()==0){
							store =gameState.getStore(requestQueue());
						}
						store.addProduct(store.productType);
						newAction(GameActionType.CARD_PLAYED, player + 1, current.ordinal(), store.productType.ordinal());
						System.out.println("INCREASED");
						break;
					case LUCKY_STRIKE:
						newAction(GameActionType.CARD_PLAYED, player + 1, current.ordinal());
						System.out.println("LUCKY");
						break;
					case MOTHER_WITH_CHILD:
						newAction(GameActionType.CARD_PLAYED, player + 1, current.ordinal());
						System.out.println("Mother");
						break;
					case NOT_YOUR_PLACE:
						newAction(GameActionType.CARD_PLAYED, player + 1, current.ordinal());
						System.out.println("PLACE");
						break;
					case TIPPING_FRIEND:
						newAction(GameActionType.CARD_PLAYED, player + 1, current.ordinal());
						System.out.println("TIPING");
						break;
					case UNDER_THE_COUNTER_GOODS:
						newAction(GameActionType.CARD_PLAYED, player + 1, current.ordinal());
						System.out.println("GOODS");
						break;
					default:
						break;

					}
				}
			}
			if (allPassed) {
				System.out.println("all");
				break;
			}
		}

		// NA KONCU ODSWIEŻYC LISTE IPASS[]. I ZEBY WSZYSTKO STYKALO
		for (int i = 0; i < 6; i++) {
			iPass[i] = false;
		}
	}

	/**
	 * Fourth Phase of Day. For each store with products, removes the right
	 * amount of product and pawns.
	 * 
	 * @author Jan
	 */
	public void openingStoresPhase() {
		for (ProductType type : ProductType.values())
			while (gameState.getStore(type).getQueue().size() > 0
					&& gameState.getStore(type).getNumberOf() > 0)
				newAction(GameActionType.PRODUCT_BOUGHT, gameState.sell(type) + 1, type.ordinal());
	}

	/**
	 * Waits for selection of queue by active player and returns selected queue.
	 * 
	 * @return destination of selected queue.
	 * @throws InterruptedException
	 */
	private synchronized ProductType requestQueue() throws InterruptedException {
		expectedType = ProductType.class;
		updateViews();
		while (selectedQueue == null)
			wait();
		expectedType = null;
		ProductType queue = selectedQueue;
		selectedQueue = null;
		return queue;

	}

	/**
	 * Waits for selection of queue by active player and returns selected queue.
	 * 
	 * @return destination of selected queue.
	 * @throws InterruptedException
	 */
	public synchronized QueuingCard requestQueuingCard() throws InterruptedException {
		expectedType = QueuingCard.class;
		updateViews();
		while (selectedQueuingCard == null && !pass) 
				wait();
		expectedType = null;
		QueuingCard card = selectedQueuingCard;
		selectedQueuingCard = null;
		pass = false;
		return card;
	}

	/**
	 * @return true if game is over and false otherwise.
	 */
	private boolean gameOver() {
		return gameState.getDayNumber() > 5;
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
		if (playerNo != gameState.getActivePlayer())
			return;

		if (expectedType == ProductType.class) {

			selectedQueue = destination;
			try {
				notifyAll();
				return;
			} finally {
			}
		}
	}

	public synchronized void queuingCardSelected(int playerNo, QueuingCard card) {
		if (playerNo != gameState.getActivePlayer())
			return;
		if (expectedType == QueuingCard.class) {
			if (card == null) {
				iPass[playerNo] = true;
				pass = true;
			}
			selectedQueuingCard = card;
			try {
				return;
			} finally {
				notifyAll();
			}
		}
	}

	/**
	 * Sets object to be informed about changes in model;
	 */
	public void addView(View view) {
		views.add(view);
	}

	/**
	 * Informs all views about changes in model.
	 */
	private void updateViews() {
		for (View view : views)
			view.update();
	}

	/**
	 * @return state of the game (model).
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * @return main thread of game.
	 */
	public Thread getGameThread() {
		return gameThread;
	}
	/**
	 * prepares cards to play
	 */
	private void prepareToQueueJumping(){
		for (Player p : gameState.getPlayersList()) {
			p.getDeck().getCards(p.getCardsOnHand());
		}
	}
	/**
	 * Adds a PlayerAction to the list and writes it to a socket when network playing
	 * @param action action to be handled
	 */
	private void newAction(GameActionType type, int... info) {
		GameAction action = new GameAction(type, info);
		gameState.addPlayerAction(action);
	}
	

}
