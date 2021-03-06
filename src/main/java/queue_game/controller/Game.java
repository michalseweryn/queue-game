/**
 * 
 */
package queue_game.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import queue_game.creator.ActionCreator;
import queue_game.model.DeckOfDeliveryCards;
import queue_game.model.DecksOfQueuingCardsBoxInterface;
import queue_game.model.DeliveryCard;
import queue_game.model.GameAction;
import queue_game.model.GameActionType;
import queue_game.model.GamePhase;
import queue_game.model.GameState;
import queue_game.model.Player;
import queue_game.model.ProductType;
import queue_game.model.QueuingCard;
import queue_game.model.Store;
import queue_game.server.Updater;
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
	private Thread gameThread = null;
	private DeckOfDeliveryCards deckOfDeliveryCards;
	private DecksOfQueuingCardsBoxInterface decks;
	private ActionCreator actionGiver;
	private Updater updater;


	public Game(GameState gameState, ActionCreator giver, Updater updater) {
		this.actionGiver = giver;
		this.gameState = gameState;
		this.updater = updater;
	}

	/**
	 * Creates new thread for the game.
	 */
	public void startGame(int nPlayers, DeckOfDeliveryCards deckOfDeliveryCards, DecksOfQueuingCardsBoxInterface decks) {
		this.nPlayers = nPlayers;
		this.deckOfDeliveryCards = deckOfDeliveryCards;
		this.decks = decks;
		gameThread = new Thread(this);
		gameThread.start();
	}
	public void endGame(){
		gameThread.interrupt();
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
				exchangingPhase();
				PCTPhase();
			}
			gameState.setGameOver();
			updateViews();
			update(new GameAction(GameActionType.END_GAME));			
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
		//List<String> names = generateNames();
		gameState.setActivePlayer(-1);
		ArrayList<List<Integer>> lists = generateShoppingLists();
		gameState.initGame(lists.subList(0, nPlayers));
		resetQueuingCards();
		queuingUpPhase();
		gameState.putSpeculators();
		for (ProductType pt : ProductType.values()) {
			newAction(GameActionType.PAWN_PLACED, 0, pt.ordinal());
		}
	}

	/*private List<String> generateNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < nPlayers; i++)
			names.add("Gracz " + (i + 1));
		return names;
	}*/

	private ArrayList<List<Integer>> generateShoppingLists() {
		ArrayList<List<Integer>> lists = new ArrayList<List<Integer>>();
		lists.add(Arrays.asList(4, 0, 2, 1, 3));
		lists.add(Arrays.asList(3, 4, 1, 0, 2));
		lists.add(Arrays.asList(2, 3, 0, 4, 1));
		lists.add(Arrays.asList(1, 2, 4, 3, 0));
		lists.add(Arrays.asList(0, 1, 3, 2, 4));
		return lists;
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
			int[] pawnsLeft = new int[5];
			int player;
			for (player = 0; player < gameState.getNumberOfPlayers(); player++)
				pawnsLeft[player] = gameState.getNumberOfPawns(player); 
			for (player = gameState.getGameOpeningMarker(); player < gameState
					.getNumberOfPlayers(); player = (player + 1)
					% gameState.getNumberOfPlayers()) {
				if (pawnsLeft[player]-- > 0) {
					gameState.setActivePlayer(player);
					GameAction action;
					do {
						action = actionGiver.getAction();
						if(action.getType() != GameActionType.PAWN_PLACED || 
						   (Integer)action.getInfo()[0] != player)
							update(new GameAction(GameActionType.ERROR, player));
						else break;
					} while (true);
					ProductType queue = (ProductType) action.getInfo()[1];
					gameState.putPlayerPawn(player, queue);
					if (queue == null)
						newAction(GameActionType.PAWN_PLACED, player + 1, -1);
					else
						newAction(GameActionType.PAWN_PLACED, player + 1,
								queue.ordinal());
					update(action);
					timeSinceLastPawnLocation = 0;
				} else {
					timeSinceLastPawnLocation++;
				}
				if (timeSinceLastPawnLocation > gameState.getNumberOfPlayers())
					break outer;
			}
		}
	}
	public void update(GameAction action) {
		if (updater != null)
			updater.update(action);
	}

	/**
	 * Second Phase of Day. Delivers 3 products to stores according to delivery
	 * cards and decreases number of products.
	 * 
	 * @author krzysiek & Helena
	 */
	public void deliveryPhase() {
		gameState.setActivePlayer(-1);

		Collection<DeliveryCard> tempDCList = deckOfDeliveryCards
				.removeThreeCards();
		ProductType type;
		for (DeliveryCard dC : tempDCList) {
			type = dC.getProductType();
			int numberOfProductsLeft = gameState.getNumberOfProductsLeft(type
					.ordinal());
			if (numberOfProductsLeft != 0) {
				int amount = Math.min(dC.getAmount(), numberOfProductsLeft);
				gameState.transferProductToStore(type, amount);
			}
		}
		Object[] deliveries = tempDCList.toArray();
		newAction(GameActionType.PRODUCT_DELIVERED, deliveries);
		update(new GameAction(GameActionType.PRODUCT_DELIVERED, deliveries));
		gameState.setCurrentDeliveryList(tempDCList);
	}

	/**
	 * @author piotr
	 * 
	 * 
	 */

	private void PCTPhase() throws InterruptedException {
		gameState.setActivePlayer(-1);
		gameState.setCurrentGamePhase(GamePhase.PCT);
		if (gameState.getDayNumber() % 5 == 4)
			SaturdayPhase();
		prepareToQueueJumping();
		gameState.openStores();
		pawnsTaking();

		gameState.setGameOpeningMarker(
				(gameState.getGameOpeningMarker()+1)
				% gameState.getNumberOfPlayers());
	}

	/**
	 * Doesn't work yet!
	 */
	private void SaturdayPhase() {
		deckOfDeliveryCards.fill();
		resetQueuingCardsOnSaturday();
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
			List<QueuingCard> cardsOnHand = gameState.getPlayersList()
					.get(player).getCardsOnHand();
			boolean success = false;/*
			if(decks.isEmpty(player)){
				finished[player]=true;
				nFinished++;
				if(nFinished==nPlayers){
					break outer;
				}
			}
			else */{
				//messageForPlayer("Wybierz kartę przepychanek, lub spasuj.");
				do {
					GameAction action = actionGiver.getAction();
					if(action.getType()== GameActionType.CARD_PLAYING_CANCELLED)
						continue;
					else if (action.getType() == GameActionType.CARD_PLAYED_PASSED) {
						finished[player] = true;
						success = true;
						nFinished++;
						update(action);
						if (nFinished == nPlayers) {
							break outer;
						}
					} else {
						System.out.println(action);
						card = (QueuingCard) action.getInfo()[1];
						if (!decks.hasCard(player, card)) {
							errorMessageForPlayer("Nie posiadasz  tej karty.");
							continue;
						}
						switch (card) {
						case DELIVERY_ERROR:
							if (deliveryError(action))
								success = true;
							break;
						case NOT_YOUR_PLACE:
							if (notYourPlace(action))
								success = true;
							break;
						case LUCKY_STRIKE:
							if (luckyStrike(action))
								success = true;
							break;
						case MOTHER_WITH_CHILD:
							if (motherWithChild(action))
								success = true;
							break;
						case UNDER_THE_COUNTER_GOODS:
							if (underTheCounterGoods(action))
								success = true;
							break;
						/*case TIPPING_FRIEND:
							if (tippingFriend(action))
								success = true;
							break;*/
						case CRITICIZING_AUTHORITIES:
							if (criticizingAuthorities(action))
								success = true;
							break;
						case INCREASED_DELIVERY:
							if (increasedDelivery(action))
								success = true;
							break;
						case CLOSED_FOR_STOCKTAKING:
							if (closedForStocktaking(action))
								success = true;
							break;
						case COMMUNITY_LIST:
							if (communityList(action))
								success = true;
							break;
						}
					}
					if (action.getType() == GameActionType.CARD_PLAYED
							&& success){
						cardsOnHand.remove((QueuingCard) action.getInfo()[1]);
						update(action);
					}/*
					if (cardsOnHand.isEmpty()) {
						finished[player] = true;
						success = true;
						nFinished++;
						if (nFinished == nPlayers)
							break outer;
					}*/
				} while (!success);
			}
			do
				player = (player + 1) % nPlayers;
			while (finished[player]);
		}
	}

	/**
	 * Fourth Phase of Day. For each store with products, removes the right
	 * amount of product and pawns.
	 * 
	 * @author Jan and Piotr
	 */
	public void openingStoresPhase() throws InterruptedException {
		//gameState.setActivePlayer(-1);
		gameState.setCurrentGamePhase(GamePhase.OPENING);
		GameAction action;
		for (Store store : gameState.getStores()) {
			if (store.isClosed())
				continue;
			boolean sold;
			int queueLength=store.getQueue().size();
			while (queueLength-- > 0) {
				sold = false;
				if (store.getQueue().get(0) == -1) {
					if (store.getNumberOf() != 0) {
						gameState.sellProduct(store, store.productType);
						sold = true;
					} else {
						for (ProductType prod : ProductType.values()) {
							if (store.getNumberOf(prod) != 0) {
								gameState.sellProduct(store, prod);
								sold = true;
								break;
							}
						}
					}
				} else if (store.hasAlternative()) {
					gameState.setActivePlayer(store.getQueue().get(0));
					do {
						action = actionGiver.getAction();		
					} while (action == null
							|| action.getType() != GameActionType.PRODUCT_BOUGHT
							|| !(((Integer) action.getInfo()[1])
									.equals(store.productType.ordinal()))
							|| store.getNumberOf((ProductType) action.getInfo()[2]) == 0);
					gameState.sellProduct(store,
							(ProductType) action.getInfo()[2]);
					sold = true;
					update(action);
					//gameState.setActivePlayer(-1);
				} else {
					// jest tu tylko jeden produkt wiec konczymy
					for (ProductType type : ProductType.values()) {
						while (store.getNumberOf(type) > 0
								&& !store.getQueue().isEmpty()) {
							gameState.sellProduct(store, type);
							sold = true;
						}
					}
				}
				if (!sold)
					break;
			}
		}
	}

	/**
	 * Fifth Phase of Day. Players can exchange products on market.
	 * 
	 * @author Jan
	 */
	public void exchangingPhase() throws InterruptedException {

		gameState.setCurrentGamePhase(GamePhase.EXCHANGING);
		Store market = gameState.getOutDoorMarket();
		LinkedList<Integer> queue = market.getQueue();
		boolean wasTrade = false;
		int index = 0;
		for (int pawn : queue) {
			index++;
			gameState.setActivePlayer(pawn);
			GameAction action;
			do {
				do {
					action = actionGiver.getAction();
				} while (!succesfulTransaction(action));
				if(action.getType() == GameActionType.EXCHANGING_CANCELLED)
					continue;
				if (action.getType() == GameActionType.PRODUCT_EXCHANGED_ONE) {
					ProductType sold = (ProductType) action.getInfo()[1];
					ProductType offered = (ProductType) action.getInfo()[2];
					gameState.trade(sold, Arrays.asList(offered));
					wasTrade = true;
				}
				if (action.getType() == GameActionType.PRODUCT_EXCHANGED_TWO) {
					ProductType sold = (ProductType) action.getInfo()[1];
					ProductType offered1 = (ProductType) action.getInfo()[2];
					ProductType offered2 = (ProductType) action.getInfo()[3];
					gameState.trade(sold, Arrays.asList(offered1, offered2));
					wasTrade = true;
				}
				if (action.getType() == GameActionType.PRODUCT_EXCHANGED_PASSED) {
					if (wasTrade) {
						queue.remove(index - 1);
						update(action);
						return;
					}
					update(action);
					break;
				}
				update(action);
			} while (action.getType() != GameActionType.PRODUCT_EXCHANGED_PASSED);
			
		}
	}

	/**
	 * @param action
	 * @return
	 */
	private boolean succesfulTransaction(GameAction action) {
		GameActionType type = action.getType();
		if (type == GameActionType.PRODUCT_EXCHANGED_PASSED)
			return true;
		int player = (Integer) action.getInfo()[0];		
		if (type == GameActionType.PRODUCT_EXCHANGED_ONE) {
			ProductType sold = (ProductType) action.getInfo()[1];
			ProductType offered = (ProductType) action.getInfo()[2];
			if (sold != gameState.getCheapProduct());
			if (gameState.getOutDoorMarket().getNumberOf(sold) == 0)
				return false;
			if (gameState.getPlayer(player).getBoughtProducts()[offered
					.ordinal()] == 0)
				return false;
			return true;
		}
		if (type == GameActionType.PRODUCT_EXCHANGED_TWO) {
			ProductType sold = (ProductType) action.getInfo()[1];
			ProductType offered1 = (ProductType) action.getInfo()[2];
			ProductType offered2 = (ProductType) action.getInfo()[3];
			if (sold != gameState.getCheapProduct());
			if (gameState.getOutDoorMarket().getNumberOf(sold) == 0)
				return false;
			if (offered1 == offered2
					&& gameState.getPlayer(player).getBoughtProducts()[offered1
							.ordinal()] < 2)
				return false;
			if (offered1 != offered2
					&& (gameState.getPlayer(player).getBoughtProducts()[offered1
							.ordinal()] == 0 || gameState.getPlayer(player)
							.getBoughtProducts()[offered1.ordinal()] == 0))
				return false;
			return true;

		}
		return false;
	}

	/**
	 * @param action
	 * @return
	 * @throws InterruptedException
	 */
	private boolean increasedDelivery(GameAction action)
			throws InterruptedException {
		ProductType type = (ProductType) action.getInfo()[2];
		if (type == null) {
			errorMessageForPlayer("BŁĄD. Nie można zwiększyć dostawy w bazarze.");
			return false;
		}
		Store store = gameState.getStore(type);

		if (!gameState.wasDelivered(type)) {
			errorMessageForPlayer("BŁĄD. Do tego sklepu nie było dostawy.");
			return false;
		}
		if (store.isClosed()) {
			errorMessageForPlayer("BŁĄD. Ten sklep jest zamknięty.");
			return false;
		}
		if(gameState.getNumberOfProductsLeft()[type.ordinal()]==0){
			errorMessageForPlayer("BŁĄÐ. Brakuje poduktu, którego dostawe zwiększono.");
			return false;
		}
		gameState.transferProductToStore(type, 1);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.INCREASED_DELIVERY.ordinal(),
				store.productType.ordinal());
		return true;
	}

	/**
	 * @param action
	 * @return
	 */
	private boolean tippingFriend(GameAction action) {
		if(gameState.getDayNumber()%5==4){
			errorMessageForPlayer("BŁĄÐ. W piątek nie można podejrzeć dostawy.");
			return false;
		}
		/*Collection<DeliveryCard> deliveryCards = deckOfDeliveryCards.peekTwoCards();
		System.out.println("Oto 2 karty dostawy:");
		for(DeliveryCard card: deliveryCards) {
		System.out.println("Sklep - "
				+ card.getProductType() + " ilość - "
				+ card.getAmount());
		}*/
		return true;
	}

	/**
	 * @param action
	 * @return
	 * @throws InterruptedException
	 */
	private boolean underTheCounterGoods(GameAction action)
			throws InterruptedException {
		ProductType type = (ProductType) action.getInfo()[2];
		ProductType store0 = (ProductType) action.getInfo()[3];
		//messageForPlayer("Wybierz towar, który chciałbyś dostać spod lady");
		if (store0 == null) {

			errorMessageForPlayer("BŁĄD. Nie można wziąć towaru spod lady z bazaru.");
			return false;
		}
		Store store = gameState.getStore(store0);
		if (store.getNumberOf(type) <= 0) {
			errorMessageForPlayer("BŁĄD. Tego towaru nie ma w sklepie");
			return false;
		}
		if (!store.getQueue().get(0).equals(gameState.getActivePlayer())) {
			errorMessageForPlayer("BŁĄD. Nie jesteś pierwszy w kolejce do tego sklepu.");
			return false;
		}
		if (store.isClosed()) {
			errorMessageForPlayer("BŁĄD. Ten sklep jest zamknięty.");
			return false;
		}
		gameState.sellProduct(gameState.getStore(store0), type);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.UNDER_THE_COUNTER_GOODS.ordinal());
		return true;

	}

	/**
	 * @param action
	 * @return
	 */
	private boolean motherWithChild(GameAction action)
			throws InterruptedException {
		ProductType destination = (ProductType) action.getInfo()[2];
		int position = (Integer) action.getInfo()[3];
		int p = gameState.getStore(destination).getQueue().get(position);
		if (p != gameState.getActivePlayer()) {
			errorMessageForPlayer("BŁĄD. To nie twój pionek.");
			return false;
		}
		if (position == 0) {
			errorMessageForPlayer("BŁĄD. Już jestes pierwszy w tej kolejce.");
			return false;
		}
		if(destination==null){
			errorMessageForPlayer("BŁĄD. Nie możesz sie przesunąć w kolejce do bazaru.");
			return false;
		}
		gameState.movePawn(destination, position, destination, 0);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.MOTHER_WITH_CHILD.ordinal());
		return true;
	}

	/**
	 * @param action
	 * @return
	 */
	private boolean luckyStrike(GameAction action) throws InterruptedException {
		ProductType destination = (ProductType) action.getInfo()[2];
		int position = (Integer) action.getInfo()[3];
		ProductType newdest = (ProductType) action.getInfo()[4];
		int p = gameState.getStore(destination).getQueue().get(position);
		if (p != gameState.getActivePlayer()) {
			errorMessageForPlayer("BŁĄD.To nie twój pionek.");
			return false;
		}
		if (destination == null) {
			errorMessageForPlayer("BŁĄD. Nie można przenieść pionka z kolejki w bazarze.");
			return false;
		}
		if (newdest == null) {
			errorMessageForPlayer("BŁĄD. Nie można przenieść pionka do kolejki w bazarze.");
			return false;
		}
		if(destination==newdest){
			errorMessageForPlayer("BŁĄD. Nie można przenieśc pionka do tego samego sklepu");
			return false;
		}
		gameState.movePawn(destination, position, newdest, 1);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.LUCKY_STRIKE.ordinal());
		return true;
	}

	/**
	 * @param action
	 * @return
	 */
	private boolean notYourPlace(GameAction action) throws InterruptedException {
		ProductType destination = (ProductType) action.getInfo()[2];
		int position = (Integer) action.getInfo()[3];
		int p = gameState.getStore(destination).getQueue().get(position);
		if (p != gameState.getActivePlayer())
			return false;
		if (position == 0)
			return false;
		if (destination == null) {
			errorMessageForPlayer("BŁĄD. Nie możesz się przesunąć w kolejce do bazaru.");
			return false;
		}
		gameState.movePawn(destination, position, destination, position - 1);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.NOT_YOUR_PLACE.ordinal());
		return true;
	}

	/**
	 * @return
	 * @throws InterruptedException
	 * 
	 */
	private boolean deliveryError(GameAction action)
			throws InterruptedException {
		ProductType type = (ProductType) action.getInfo()[2];
		ProductType store1 = (ProductType) action.getInfo()[3];
		ProductType store2 = (ProductType) action.getInfo()[4];
		if (store1 == null){
			errorMessageForPlayer("BŁĄD. Nie można przenieść produktu z bazaru");
			return false;
		}
		Store store = gameState.getStore(store1);
		if (store.getNumberOf(type) <= 0){
			errorMessageForPlayer("BŁĄD. Nie ma produktów w tym sklepie.");
			return false;
		}
		if (store.isClosed()){
			errorMessageForPlayer("BŁĄD. Produkt brany z zamkniętego sklepu.");
			return false;
		}
		if (store2 == null){
			errorMessageForPlayer("BŁĄÐ. Nie można przenieść produktu na bazar");
			return false;
		}
		if(store1 == store2){
			errorMessageForPlayer("BŁĄÐ. Wybrałeś dwa razy ten sam sklep");
			return false;
		}
		if(gameState.getStore(store2).isClosed()){
			errorMessageForPlayer("BŁĄÐ. Sklep zamknięty");
			return false;
		}
		gameState.transferToAnotherStore(store1, store2, type);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.DELIVERY_ERROR.ordinal(),
				store.productType.ordinal());
		return true;
	}

	/**
	 * @param action
	 * @return
	 * 
	 */
	private boolean criticizingAuthorities(GameAction action)
			throws InterruptedException {
		ProductType destination = (ProductType) action.getInfo()[2];
		Integer position = (Integer) action.getInfo()[3];
		if (position == gameState.getStore(destination).getQueue().size() - 1) {
			errorMessageForPlayer("BŁĄD. Ten pionek już jest na końcu kolejki.");
			return false;
		}
		if (position == gameState.getStore(destination).getQueue()
				.size() - 2){
			errorMessageForPlayer("BŁĄD. Ten pionek jest przedostatni, nie moze sie cofnać o dwa miejsca.");
			return false;
		}
		if (destination == null) {
			errorMessageForPlayer("BŁĄD. Nie możesz przesunąć pionka w kolejce do bazaru.");
			return false;
		}
		gameState.movePawn(destination, position, destination, position + 2);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.CRITICIZING_AUTHORITIES.ordinal());
		return true;
	}

	/**
	 * @param action
	 * @return
	 * @throws InterruptedException
	 * 
	 */
	private boolean communityList(GameAction action)
			throws InterruptedException {
		//messageForPlayer("Wybierz kolejke która ma zostać odwrócona");
		ProductType queue = (ProductType) action.getInfo()[2];
		if (queue == null) {
			errorMessageForPlayer("BŁĄD. Nie możesz odwrócić kolejki na bazarze.");
			return false;
		}
		if (gameState.getStore(queue).getQueue().size() == 1) {
			errorMessageForPlayer("BŁĄD. Tu jest tylko jeden samotny spekulant.");
			return false;
		}
		gameState.reverse(queue);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.COMMUNITY_LIST.ordinal(), queue.ordinal());
		return true;
	}

	/**
	 * @param action
	 * @return
	 * 
	 */
	private boolean closedForStocktaking(GameAction action)
			throws InterruptedException {
		ProductType queue = (ProductType) action.getInfo()[2];
		if (gameState.getStore(queue).isClosed()) {
			errorMessageForPlayer("BŁĄD.Ten sklep jest już zamnknięty.");
			return false;
		}
		if (queue == null) {
			errorMessageForPlayer("BŁĄD. Nie możesz zamknąć bazaru.");
			return false;
		}
		gameState.close(queue);
		newAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
				QueuingCard.CLOSED_FOR_STOCKTAKING.ordinal());
		return true;

	}
	/**
	 * @return true if game is over and false otherwise. returns true if and
	 *         only if some player has all of required products or there is no
	 *         product that can be delivered
	 */
	private boolean gameOver() {
		boolean finished;
		for (Player pl : gameState.getPlayersList()) {
			int nProductTypes = ProductType.values().length;
			int boughtProducts[] = pl.getBoughtProducts();
			List<Integer> shoppingList = pl.getShoppingList();
			finished = true;
			for (int i = 0; i < nProductTypes; i++) {
				if (boughtProducts[i] < shoppingList.get(i)) {
					finished = false;
					break;
				}
			}
			if (finished) {
				return true;
			}
		}
		finished = true;
		for (int productsLeft : gameState.getNumberOfProductsLeft()) {
			if (productsLeft != 0) {
				finished = false;
				break;
			}
		}
		if (finished) {
			return true;
		}
		return false;
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
		addCardsToHands();
	}

	/**
	 * 
	 */
	private void pawnsTaking() throws InterruptedException {
		int nPlayers = gameState.getNumberOfPlayers();
		int opening = gameState.getGameOpeningMarker();
		int player = opening;
		do {
			gameState.setActivePlayer(player);
			Player temp = gameState.getPlayersList().get(player);
			int numberOfPawns = temp.getNumberOfPawns();
			while (numberOfPawns++ < 5) {
				GameAction action = actionGiver.getAction();
				if (action.getType() == GameActionType.PAWN_REMOVED_PASSED){
					update(action);
					break;
				}
				ProductType destination = (ProductType) action.getInfo()[1];
				int position = (Integer) action.getInfo()[2];
				if (gameState.getStore(destination).getQueue()
						.get(position).equals(player)) {
					gameState.removePlayerPawn(position,
							destination);
					update(action);
				}
				else numberOfPawns--;
			}
			player = (player + 1) % nPlayers;

		} while (player != opening);
	}

	/**
	 * Adds a GameAction to the list and writes it to a socket when network
	 * playing
	 * 
	 * @param type
	 *            type of action
	 * @param info
	 *            additional action info
	 */
	private void newAction(GameActionType type, Object... info) {
		GameAction action = new GameAction(type, info);
		gameState.addGameAction(action);
	}

	/*private void messageForPlayer(String s) {
		gameState.setMessage(s);
		gameState.setErrorMessage("");
	}*/
	
	private void errorMessageForPlayer(String s) {
		gameState.setErrorMessage(s);
	}

	/**
	 * 
	 * Reset cards of all players.
	 * 
	 */
	public synchronized void resetQueuingCards() {
		decks.resetAllDecks();
		addCardsToHands();
	}
	private void addCardsToHands(){
		int nPlayers = gameState.getNumberOfPlayers();
		for (int i = 0; i < nPlayers; i++) {
			List<QueuingCard> cards = decks.getCardsToFillTheHandOfPlayer(i);
			gameState.getPlayersList().get(i)
					.addCardsToHand(cards);
			for(QueuingCard card : cards)
				update(new GameAction(GameActionType.DRAW_CARD, i, card));
		}
		
	}

	/**
	 * Fills the deck with new cards at the end of the week
	 */
	public synchronized void resetQueuingCardsOnSaturday() {
		int nPlayers = gameState.getNumberOfPlayers();
		decks.resetAllDecks();
		for (int i = 0; i < nPlayers; i++) {
			gameState.getPlayer(i).setCardsOnHand(new ArrayList<QueuingCard>());
			List<QueuingCard> cards = decks.getCardsToFillTheHandOfPlayer(i); 
			gameState.getPlayersList().get(i)
					.addCardsToHand(cards);
			for(QueuingCard card : cards)
				update(new GameAction(GameActionType.DRAW_CARD, i, card));
		}
	}
	/*
	 * public synchronized StandardDeckOfQueuingCards getDeck(int playerNr) {
	 * return decks.getDeck(); }
	 */

	/**
	 * @param adapter
	 */
	public void setActionGiver(ActionCreator giver) {
		this.actionGiver = giver;

	}

}
