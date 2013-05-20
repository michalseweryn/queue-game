/**
 * 
 */
package queue_game.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.lang.Math;

import org.ietf.jgss.MessageProp;

import queue_game.model.DeckOfDeliveryCards;
import queue_game.model.DeliveryCard;
import queue_game.model.GameAction;
import queue_game.model.GameActionType;
import queue_game.model.GamePhase;
import queue_game.model.GameState;
import queue_game.model.Player;
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
	private boolean iPass[] = new boolean[6];
	private boolean pass = false;
	private DeckOfDeliveryCards deckOfDeliveryCards = new DeckOfDeliveryCards();
	
	

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
		gameState.resetNumberOfProductsLeft();
		gameState.resetPlayers();
		gameState.resetCards();
		gameState.resetShoppingList();
		queuingUpPhase();
		gameState.putSpeculators();
		for (ProductType pt : ProductType.values()) {
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
					messageForPlayer("Wybierz kolejkę w której chcesz ustawić pionka");
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
	 * Second Phase of Day. Delivers 3
	 * products to stores according to delivery cards and decreases number of products.
	 * 
	 * @author krzysiek & Helena
	 */
	public void deliveryPhase() {
		System.out.println("Dostawa");
		List<DeliveryCard> tempDCList = deckOfDeliveryCards.removeThreeCards();
		ProductType type;
		for (DeliveryCard dC : tempDCList){
			type = dC.getProductType();
			Store deliveredStore = gameState.getStore(type);
			int numberOfProductsLeft =
					gameState.getNumberOfProductsLeft(type.ordinal());
			if(numberOfProductsLeft !=0 )
			{
				int amount = Math.min(dC.getAmount(), numberOfProductsLeft);
				gameState.transferProductToStore(type, amount);
				newAction(GameActionType.PRODUCT_DELIVERED,
							type.ordinal(), amount);
			}
		}
		gameState.setCurrentDeliveryList(tempDCList);
	}

	/**
	 * @author piotr
	 * 
	 * 
	 */


	private void PCTPhase() throws InterruptedException {
		gameState.setCurrentGamePhase(GamePhase.PCT);
		prepareToQueueJumping();
		openStores();
		pawnsTaking();
	}
/**
 * 
 * Set stores open.
 * 
 */
	private void openStores(){
		for(ProductType p : ProductType.values()){
			gameState.getStore(p).setClosed(false);
		}
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

		if (!gameState.wasDelivered(type)) {
			messageForPlayer("BŁĄD. Do tego sklepu nie było dostawy.");
			return false;
		}
		if (store.isClosed()) {
			messageForPlayer("BŁĄD. Ten sklep jest zamknięty.");
			return false;
		}
		store.addProduct(store.productType);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.INCREASED_DELIVERY.ordinal(),
				store.productType.ordinal());
		return true;
	}

	/**
	 * @return
	 */
	private boolean tippingFriend() {
		List<DeliveryCard> deliveryCards=deckOfDeliveryCards.peekTwoCards();
		messageForPlayer("Oto 2 karty dostawy:");
		messageForPlayer("Pierwsza : sklep - "+deliveryCards.get(0).getProductType()+" ilość - "+deliveryCards.get(0).getAmount());
		messageForPlayer("Druga : sklep - "+deliveryCards.get(1).getProductType()+" ilość - "+deliveryCards.get(1).getAmount());
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
		if (store.isClosed()) {
			System.out.println("BŁĄD. Ten sklep jest zamknięty.");
			return false;
		}
		gameState.sell(prod.store, prod.product);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.UNDER_THE_COUNTER_GOODS.ordinal());
		return true;

	}

	/**
	 * @return
	 */
	private boolean motherWithChild() throws InterruptedException{
		messageForPlayer("Wybierz twój pionek który ma być przesunięty");
		PawnParameters pawn  = requestPawn();
		int p = gameState.getStore(pawn.destination).getQueue()
				.get(pawn.position);
		if(p!=gameState.getActivePlayer()){
			messageForPlayer("BŁAD. To nie twój pionek.");
			return false;
		}
		if(pawn.position==0){
			messageForPlayer("BŁAD. Już jestes pierwszy w tej kolejce.");
			return false;
		}
		gameState.movePawn(pawn.destination, pawn.position, pawn.destination, 0);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.MOTHER_WITH_CHILD.ordinal());
		return true;
	}

	/**
	 * @return
	 */
	private boolean luckyStrike() throws InterruptedException{
		messageForPlayer("Wybierz twój pionek który ma byc przeniesiony");
		PawnParameters pawn  = requestPawn();
		int p = gameState.getStore(pawn.destination).getQueue()
				.get(pawn.position);
		ProductType dest = pawn.destination;
		if(p!=gameState.getActivePlayer()){
			messageForPlayer("BŁAD.To nie twój pionek.");
			return false;
		}
		messageForPlayer("Wybierz kolejke do której zostanie przeniesiony pionek");
		ProductType type = requestQueue();
		gameState.movePawn(pawn.destination, pawn.position, type, 1);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.LUCKY_STRIKE.ordinal());
		return true;
	}

	/**
	 * @return
	 */
	private boolean notYourPlace() throws InterruptedException{
		messageForPlayer("Wybierz twój pionek który ma zostać przesunięty");
		PawnParameters pawn = null;
		
			pawn = requestPawn();
		
		int p = gameState.getStore(pawn.destination).getQueue()
				.get(pawn.position);
		if(p!=gameState.getActivePlayer()){
			messageForPlayer("BŁAD.To nie twój pionek.");
			return false;
		}
		if(pawn.position==0){
			messageForPlayer("BŁAD.Już jestes pierwszy w tej kolejce.");
			return false;
		}
		gameState.movePawn(pawn.destination, pawn.position, pawn.destination, pawn.position-1);
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
		messageForPlayer("Wybierz towar, który ma być przeniesiony");
		ProductParameters prod = requestProduct();
		if (prod.store == null) {
			messageForPlayer("BŁĄD. Nie można przenieść towaru z bazaru.");
			return false;
		}
		Store store = gameState.getStore(prod.store);
		if (store.getNumberOf(prod.product) <= 0) {
			messageForPlayer("BŁĄD. Tego towaru nie ma w sklepie");
			return false;
		}
		if (store.isClosed()) {
			System.out.println("BŁĄD. Ten sklep jest zamknięty.");
			return false;
		}
		store.removeProducts(1, prod.product);
		messageForPlayer("Wybierz sklep w którym ma być dodany towar");
		Store store3 = gameState.getStore(requestQueue());
		store3.addProduct(prod.product);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.DELIVERY_ERROR.ordinal(),
				store.productType.ordinal(), store3.productType.ordinal());
		System.out.println("DELIVERY");
		return true;
	}

	/**
	 * @return
	 * 
	 */
	private boolean criticizingAuthorities() throws InterruptedException {
		messageForPlayer("Wybierz pionek innego gracza, który ma być przesunięty");
		PawnParameters pawn = requestPawn();
		if(pawn.position==gameState.getStore(pawn.destination).getQueue().size()-1 || pawn.position==gameState.getStore(pawn.destination).getQueue().size()-2){
			messageForPlayer("BŁAD. On już jest na końcu kolejki.");
			return false;
		}
		gameState.movePawn(pawn.destination, pawn.position, pawn.destination, pawn.position+2);
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
		messageForPlayer("Wybierz kolejke która ma zostać odwrócona");
		ProductType queue = requestQueue();
		if (queue == null){
			messageForPlayer("BŁAD. Ta kolejka jest pusta.");
			return false;
		}
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
		messageForPlayer("Wybierz sklep do zamkniecia");
		ProductType queue = requestQueue();
		gameState.getStore(queue).setClosed(true);
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
		for(Store store : gameState.getStores()){
			if(store.isClosed())
				continue;
			int queueLength = store.getQueue().size();
			while(queueLength-- > 0){
				for(ProductType product: ProductType.values()){
					if(store.getNumberOf(product) > 0 && !store.isClosed()){
						gameState.sell(store.productType, product);
						// no full information anyway.
						// newAction(GameActionType.PRODUCT_BOUGHT,
						//		gameState.sell(type) + 1, type.ordinal());
						break;
					}
				}
			}
		}
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
	public synchronized void pawnSelected(int player, ProductType destination, int position) {
		if (player != gameState.getActivePlayer())
			return;
		if (expectedType != PawnParameters.class) 
			return;
			PawnParameters p = new PawnParameters();
			p.destination = destination;
			p.position = position;
			selectedPawn = p;
			expectedType = null;
			notifyAll();
		//System.out.println("pionek " + player + " " + destination + " "+ position);

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
	 * 
	 */
	private void pawnsTaking() throws InterruptedException{
		int nPlayers=gameState.getNumberOfPlayers();
		int opening=gameState.getGameOpeningMarker();
		int player=opening;
		do{
			gameState.setActivePlayer(player);
			Player temp=gameState.getPlayersList().get(player);
			outer: while (temp.getNumberOfPawns() < 5) {
				messageForPlayer("Wybierz pionek ktory chcesz usunac");
				PawnParameters selectedPawn = requestPawn();
				if (selectedPawn.position == -1) {
					break;
				} else if (selectedPawn.destination == null) {
				} else {
					if (gameState.getStore(selectedPawn.destination).getQueue()
							.get(selectedPawn.position).equals(player)) {
						gameState
								.removePlayerPawn(player,
										selectedPawn.position,
										selectedPawn.destination);
					} else {
						continue outer;
					}
				}

			}
			player=(player+1)%nPlayers;
			
		}while(player!=opening);
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
		gameState.setMessage(s);
		System.out.println(s);
	}

}
