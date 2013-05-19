/**
 * 
 */
package queue_game.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
	private PawnParameters selectedPawn = null;
	private ProductParameters selectedProduct = null;
	private ProductType selectedQueue = null;
	private Thread gameThread = null;
	private QueuingCard selectedQueuingCard = null;

	private class PawnParameters {
		int position;
		ProductType destination;
	}

	private class ProductParameters {
		ProductType product;
		ProductType store;

		public ProductParameters(ProductType product, ProductType store) {
			this.product = product;
			this.store = store;
		}

	}

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
				if (day != 0) {
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
	 * 
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
					newAction(GameActionType.PAWN_PLACED, player + 1,
							queue.ordinal());
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
	private void PCTPhase() {
		gameState.setCurrentGamePhase(GamePhase.PCT);
		prepareToQueueJumping();
	}

	/**
	 * @author piotr Third Phase of Day. Each player either plays card or
	 *         passes. Phase is over when all players have passed or there are
	 *         no cards left.
	 */
	public void queueJumpingPhase() throws InterruptedException {
		gameState.setCurrentGamePhase(GamePhase.JUMPING);
		final int nPlayers = gameState.getNumberOfPlayers();
		boolean[] finished = new boolean[nPlayers];
		Arrays.fill(finished, false);
		int nFinished = 0;
		QueuingCard card;
		int player = gameState.getGameOpeningMarker();
		outer: while (true) {
			gameState.setActivePlayer(player);
			ArrayList<QueuingCard> cardsOnHand = gameState.getPlayersList()
					.get(player).getCardsOnHand();
			boolean success = false;
			do {
				messageForPlayer("Wybierz kartę przepychanek, lub spasuj.");
				card = requestQueuingCard();
				if (card == null) {
					finished[player] = true;
					success = true;
					nFinished++;
					if (nFinished == nPlayers)
						break outer;
				} else {
					if (!cardsOnHand.contains(card)) {
						messageForPlayer("Nie posiadasz tej karty.");
						continue;
					}
					switch (card) {
					case DELIVERY_ERROR:
						if (deliveryError())
							success = true;
						break;
					case NOT_YOUR_PLACE:
						if (notYourPlace())
							success = true;
						break;
					case LUCKY_STRIKE:
						if (luckyStrike())
							success = true;
						break;
					case MOTHER_WITH_CHILD:
						if (motherWithChild())
							success = true;
						break;
					case UNDER_THE_COUNTER_GOODS:
						if (underTheCounterGoods())
							success = true;
						break;
					case TIPPING_FRIEND:
						if (tippingFriend())
							success = true;
						break;
					case CRITICIZING_AUTHORITIES:
						if (criticizingAuthorities())
							success = true;
						break;
					case INCREASED_DELIVERY:
						if (increasedDelivery())
							success = true;
						break;
					case CLOSED_FOR_STOCKTAKING:
						if (closedForStocktaking())
							success = true;
						break;
					case COMMUNITY_LIST:
						if (communityList())
							success = true;
						break;
					}
				}
				if (success)
					cardsOnHand.remove(card);
				if (cardsOnHand.isEmpty()) {
					finished[player] = true;
					success = true;
					nFinished++;
					if (nFinished == nPlayers)
						break outer;
				}
			} while (!success);
			System.out.println("po sukcesie");
			do
				player = (player + 1) % nPlayers;
			while (finished[player]);
		}
	}

	/**
	 * @return
	 * @throws InterruptedException
	 */
	private boolean increasedDelivery() throws InterruptedException {
		messageForPlayer("Wybierz sklep w którym ma być zwiększona dostawa");
		ProductType type = requestQueue();
		if (type == null) {
			messageForPlayer("BŁĄD. Nie można zwiększyć dostawy w bazarze.");
			return false;
		}
		Store store = gameState.getStore(type);
		if (store == null) {
			messageForPlayer("BŁĄD. Nie można zwiększyć dostawy w bazarze.");
			return false;
		}
		if (store.getNumberOf() == 0) {
			System.out.println("BŁĄD. Do tego sklepu nie było dostawy.");
			return false;
		}
		store.addProduct(store.productType);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.INCREASED_DELIVERY.ordinal(),
				store.productType.ordinal());
		System.out.println("INCREASED");
		return true;
	}

	/**
	 * @return
	 */
	private boolean tippingFriend() {
		// TODO write action code
		return true;
	}

	/**
	 * @return
	 * @throws InterruptedException
	 */
	private boolean underTheCounterGoods() throws InterruptedException {
		messageForPlayer("Wybierz towar, który chciałbyś dostać spod lady");
		ProductParameters prod = requestProduct();
		if (prod.store == null) {
			messageForPlayer("BŁĄD. Nie można wziąć towaru spod lazy z bazaru.");
			return false;
		}
		Store store = gameState.getStore(prod.store);
		if (store.getNumberOf(prod.product) <= 0) {
			messageForPlayer("BŁĄD. Tego towaru nie ma w sklepie");
			return false;
		}
		if (!store.getQueue().get(0).equals(gameState.getActivePlayer())) {
			messageForPlayer("BŁĄD. Nie jesteś pierwszy w kolejce do tego sklepu.");
			return false;
		}
		store.removeProducts(1, prod.product);
		store.getQueue().remove(0);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.UNDER_THE_COUNTER_GOODS.ordinal());
		return true;

	}

	/**
	 * @return
	 */
	private boolean motherWithChild() {
		PawnParameters pawn = null;
		try {
			pawn = requestPawn();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int p = gameState.getStore(pawn.destination).getQueue()
				.get(pawn.position);
		gameState.getStore(pawn.destination).getQueue().remove(pawn.position);
		gameState.getStore(pawn.destination).getQueue().addFirst(p);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.MOTHER_WITH_CHILD.ordinal());
		return true;
	}

	/**
	 * @return
	 */
	private boolean luckyStrike() {
		// TODO write action code
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.LUCKY_STRIKE.ordinal());
		return true;
	}

	/**
	 * @return
	 */
	private boolean notYourPlace() {
		PawnParameters pawn = null;
		try {
			pawn = requestPawn();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int p = gameState.getStore(pawn.destination).getQueue()
				.get(pawn.position);
		gameState.getStore(pawn.destination).getQueue().remove(pawn.position);
		gameState.getStore(pawn.destination).getQueue()
				.add(pawn.position - 1, p);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.NOT_YOUR_PLACE.ordinal());
		return true;
	}

	/**
	 * @return
	 * @throws InterruptedException
	 * 
	 */
	private boolean deliveryError() throws InterruptedException {
		Store store2 = gameState.getStore(requestQueue());
		while (store2.getNumberOf() == 0) {
			store2 = gameState.getStore(requestQueue());
		}
		store2.removeProducts(1);
		Store store3 = gameState.getStore(requestQueue());
		store3.addProduct(store2.productType);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.DELIVERY_ERROR.ordinal(),
				store2.productType.ordinal(), store3.productType.ordinal());
		System.out.println("DELIVERY");
		return true;
	}

	/**
	 * @return
	 * 
	 */
	private boolean criticizingAuthorities() throws InterruptedException {
		PawnParameters pawn = null;
		try {
			pawn = requestPawn();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int p = gameState.getStore(pawn.destination).getQueue()
				.get(pawn.position);
		gameState.getStore(pawn.destination).getQueue().remove(pawn.position);
		gameState.getStore(pawn.destination).getQueue()
				.add(pawn.position + 2, p);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.CRITICIZING_AUTHORITIES.ordinal());
		System.out.println("AUTHORITIES");
		return true;
	}

	/**
	 * @return
	 * @throws InterruptedException
	 * 
	 */
	private boolean communityList() throws InterruptedException {
		ProductType queue = requestQueue();
		if (queue == null)
			return false;
		Collections.reverse(gameState.getStore(queue).getQueue());
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.COMMUNITY_LIST.ordinal(), queue.ordinal());
		System.out.println("USING COMMUNITY LIST");
		return true;
	}

	/**
	 * @return
	 * 
	 */
	private boolean closedForStocktaking() throws InterruptedException {
		// TODO Action code
		System.out.println("CLOSED");
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.CLOSED_FOR_STOCKTAKING.ordinal());
		return true;

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
				newAction(GameActionType.PRODUCT_BOUGHT,
						gameState.sell(type) + 1, type.ordinal());
	}

	/**
	 * Waits for selection of queue by active player and returns selected queue.
	 * 
	 * @return destination of selected queue.
	 * @throws InterruptedException
	 */
	private synchronized PawnParameters requestPawn()
			throws InterruptedException {
		expectedType = PawnParameters.class;
		updateViews();
		while (selectedPawn == null)
			wait();
		expectedType = null;
		PawnParameters pawn = selectedPawn;
		selectedPawn = null;
		System.out.println("pionek" + pawn.position + "do" + pawn.destination);
		return pawn;

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
		while (expectedType != null)
			wait();
		return selectedQueue;

	}

	/**
	 * Waits for selection of queue by active player and returns selected queue.
	 * 
	 * @return destination of selected queue.
	 * @throws InterruptedException
	 */
	public synchronized ProductParameters requestProduct()
			throws InterruptedException {
		expectedType = ProductParameters.class;
		updateViews();
		while (expectedType != null) {
			System.out.println("czekamy");
			wait();
		}

		return selectedProduct;
	}

	/**
	 * Waits for selection of queue by active player and returns selected queue.
	 * 
	 * @return destination of selected queue.
	 * @throws InterruptedException
	 */
	public synchronized QueuingCard requestQueuingCard()
			throws InterruptedException {
		expectedType = QueuingCard.class;
		updateViews();
		while (expectedType != null)
			wait();
		return selectedQueuingCard;
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

		if (expectedType != ProductType.class)
			return;

		selectedQueue = destination;
		expectedType = null;
		notifyAll();
	}

	public synchronized void queuingCardSelected(int playerNo, QueuingCard card) {
		if (playerNo != gameState.getActivePlayer())
			return;
		if (expectedType != QueuingCard.class)
			return;
		selectedQueuingCard = card;
		expectedType = null;
		notifyAll();
	}

	/**
	 * @param activePlayer
	 * @param destination
	 * @param position
	 */
	public void pawnSelected(int player, ProductType destination, int position) {
		if (player != gameState.getActivePlayer())
			return;
		if (expectedType == PawnParameters.class) {
			PawnParameters p = new PawnParameters();
			p.destination = destination;
			p.position = position;
			selectedPawn = p;
		}
		System.out.println("pionek " + player + " " + destination + " "
				+ position);

	}

	/**
	 * @param activePlayer
	 * @param product
	 * @param store
	 */
	public synchronized void productSelected(int activePlayer,
			ProductType product, ProductType store) {
		if (activePlayer != gameState.getActivePlayer())
			return;
		if (expectedType != ProductParameters.class)
			return;
		selectedProduct = new ProductParameters(product, store);
		expectedType = null;
		notifyAll();

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
	private void prepareToQueueJumping() {
		for (Player p : gameState.getPlayersList()) {
			p.getDeck().getCards(p.getCardsOnHand());
		}
	}

	/**
	 * Adds a PlayerAction to the list and writes it to a socket when network
	 * playing
	 * 
	 * @param action
	 *            action to be handled
	 */
	private void newAction(GameActionType type, int... info) {
		GameAction action = new GameAction(type, info);
		gameState.addGameAction(action);
	}

	private void messageForPlayer(String s) {
		System.out.println(s);
	}

}
