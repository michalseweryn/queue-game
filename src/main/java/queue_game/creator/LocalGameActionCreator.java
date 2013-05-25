/**
 * 
 */
package queue_game.creator;

import java.util.ArrayList;
import java.util.List;

import queue_game.ActionCreator;
import queue_game.model.GameAction;
import queue_game.model.GameActionType;
import queue_game.model.GameState;
import queue_game.model.ProductType;
import queue_game.model.QueuingCard;
import queue_game.model.Store;
import queue_game.view.JPlayerList;
import queue_game.view.View;

/**
 * @author michal
 * 
 */
public class LocalGameActionCreator implements ActionCreator {
	private Class<?> expectedType = null;
	private PawnParameters selectedPawn = null;
	private ProductParameters selectedProduct = null;
	private ProductType selectedQueue = null;
	private QueuingCard selectedQueuingCard = null;
	private List<View> views = new ArrayList<View>();
	private static final String SELECT_QUEUE = "Wybierz kolejkę do której chcesz dostawić pionek";
	private static final String SELECT_CARD = "Wybierz kartę przepychanek, lub spasuj";
	private static final String SELECT_PAWN_TO_REMOVE = "Wybierz pionek do usuniecia";
	private static final String OFFER_1_OF_1 = "Wybierz jeden ze swoich produktów";
	private static final String OFFER_1_OF_2 = "Wybierz pierwszy ze swoich produktów";
	private static final String OFFER_2_OF_2 = "Wybierz drugi ze swoich produktów";
	private static final String SELECT_FROM_MARKET = "Wybierz produkt z bazaru";
	private static final String SELECT_STORE_INCREASED = "Wybierz sklep w którym ma być zwiększona dostawa";
	private static final String SELECT_PRODUCT_UNDER = "Wybierz towar, który chciałbyś dostać spod lady";
	private static final String SELECT_PAWN_TO_MOVE="Wybierz pionek który ma być przesunięty";
	private static final String SELECT_PRODUCT_TO_MOVE ="Wybierz towar, który ma być przeniesiony";
	private static final String SELECT_STORE_TO_ADD = "Wybierz sklep w którym ma być dodany towar";
	private static final String SELECT_STORE_REVERSED = "Wybierz kolejkę do odwrócenia";
	private static final String SELECT_STORE_TO_CLOSE = "Wybierz sklep do zamknięcia";

	GameState gameState;

	public LocalGameActionCreator(GameState gameState) {
		this.gameState = gameState;
	}

	public void addView(View view) {
		views.add(view);
	}

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

	public GameAction getAction() throws InterruptedException {
		switch (gameState.getCurrentGamePhase()) {
		case DELIVERY:
			throw new IllegalStateException("No actions for delivery.");
		case EXCHANGING:
			return getExchangingAction();
		case JUMPING:
			return getJumpingAction();
		case OPENING:
			throw new IllegalStateException("No actions for opening right now.");
		case PCT:
			return getPCTAction();
		case QUEUING_UP:
			return getQueuingUpAction();
		default:
			break;

		}
		return null;
	}

	/**
	 * @return
	 * @throws InterruptedException
	 */
	private GameAction getJumpingAction() throws InterruptedException {
		QueuingCard card = requestQueuingCard(SELECT_CARD);
		System.out.println(card);
		if (card == null) {
			return new GameAction(GameActionType.CARD_PLAYED_PASSED,
					gameState.getActivePlayer());
		} else {
			switch (card) {
			case DELIVERY_ERROR:
				ProductParameters prod = requestProduct(SELECT_PRODUCT_TO_MOVE);
				ProductType p=requestQueue(SELECT_STORE_TO_ADD);
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.DELIVERY_ERROR,prod.product,prod.store,p);
			case NOT_YOUR_PLACE:
				PawnParameters pawn1 = requestPawn(SELECT_PAWN_TO_MOVE);
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.NOT_YOUR_PLACE,pawn1.destination,pawn1.position);
			case LUCKY_STRIKE:
				PawnParameters pawn  = requestPawn(SELECT_PAWN_TO_MOVE);
				ProductType type = requestQueue(SELECT_QUEUE);
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.LUCKY_STRIKE,pawn.destination,pawn.position,type);
			case MOTHER_WITH_CHILD:
				PawnParameters pawn2  = requestPawn(SELECT_PAWN_TO_MOVE);
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.MOTHER_WITH_CHILD,pawn2.destination,pawn2.position);
			case UNDER_THE_COUNTER_GOODS:
				ProductParameters prod2 = requestProduct(SELECT_PRODUCT_UNDER);
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.UNDER_THE_COUNTER_GOODS, prod2.product,prod2.store);
			case TIPPING_FRIEND:
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.TIPPING_FRIEND);
			case CRITICIZING_AUTHORITIES:
				PawnParameters pawn4 = requestPawn(SELECT_PAWN_TO_MOVE);
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer() + 1,
						QueuingCard.CRITICIZING_AUTHORITIES,pawn4.destination,pawn4.position);
			case INCREASED_DELIVERY:
				ProductType type1 = requestQueue(SELECT_STORE_INCREASED);
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.INCREASED_DELIVERY, type1);
			case CLOSED_FOR_STOCKTAKING:
				type = requestQueue(SELECT_STORE_TO_CLOSE);
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.CLOSED_FOR_STOCKTAKING, type);
				
			case COMMUNITY_LIST:
				type = requestQueue(SELECT_STORE_REVERSED);
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.COMMUNITY_LIST, type);
			default:
				return new GameAction(GameActionType.ERROR);
				
			}
		}
	}

	/**
	 * @return
	 * @throws InterruptedException
	 */
	private GameAction getPCTAction() throws InterruptedException {
		PawnParameters pawn = requestPawn(SELECT_PAWN_TO_REMOVE);
		if (pawn.position == -1)
			return new GameAction(GameActionType.PAWN_REMOVED_PASSED,
					gameState.getActivePlayer());
		else
			return new GameAction(GameActionType.PAWN_REMOVED,
					gameState.getActivePlayer(), pawn.destination,
					pawn.position);
	}

	/**
	 * @return
	 * @throws InterruptedException
	 */
	private GameAction getQueuingUpAction() throws InterruptedException {
		ProductType destination = requestQueue(SELECT_QUEUE);
		return new GameAction(GameActionType.PAWN_PLACED,
				gameState.getActivePlayer(), destination);
	}

	/**
	 * @return
	 * @throws InterruptedException
	 */
	private GameAction getExchangingAction() throws InterruptedException {
		ProductParameters productToBuy = requestProduct(SELECT_FROM_MARKET);
		if (productToBuy.product == null)
			return new GameAction(GameActionType.PRODUCT_EXCHANGED_PASSED);
		if (gameState.getCheapProduct() == productToBuy.product) {
			ProductParameters offeredProduct1 = requestProduct(OFFER_1_OF_1);
			if (offeredProduct1.product == null)
				return new GameAction(GameActionType.PRODUCT_EXCHANGED_PASSED);
			return new GameAction(GameActionType.PRODUCT_EXCHANGED_ONE,
					productToBuy.product, offeredProduct1.product);

		}
		ProductParameters offeredProduct1 = requestProduct(OFFER_1_OF_2);
		if (offeredProduct1.product == null)
			return new GameAction(GameActionType.PRODUCT_EXCHANGED_PASSED);
		ProductParameters offeredProduct2 = requestProduct(OFFER_2_OF_2);
		if (offeredProduct2.product == null)
			return new GameAction(GameActionType.PRODUCT_EXCHANGED_PASSED);
		return new GameAction(GameActionType.PRODUCT_EXCHANGED_TWO,
				productToBuy.product, offeredProduct1.product,
				offeredProduct2.product);
	}

	private void messageForPlayer(String message) {
		gameState.setMessage(message);
		updateViews();
	}

	public synchronized ProductParameters requestProduct(String message)
			throws InterruptedException {
		expectedType = ProductParameters.class;
		messageForPlayer(message);
		while (expectedType != null) {
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
	private synchronized ProductType requestQueue(String message)
			throws InterruptedException {
		expectedType = ProductType.class;
		messageForPlayer(message);
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
	public synchronized QueuingCard requestQueuingCard(String message)
			throws InterruptedException {
		expectedType = QueuingCard.class;
		messageForPlayer(message);
		while (expectedType != null)
			wait();
		return selectedQueuingCard;
	}

	private synchronized PawnParameters requestPawn(String message)
			throws InterruptedException {
		expectedType = PawnParameters.class;
		messageForPlayer(message);
		while (selectedPawn == null)
			wait();
		expectedType = null;
		PawnParameters pawn = selectedPawn;
		selectedPawn = null;
		return pawn;

	}

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
	public synchronized void pawnSelected(int player, ProductType destination,
			int position) {
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
		// System.out.println("pionek " + player + " " + destination + " "+
		// position);

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
	 * Informs all views about changes in model.
	 */
	private void updateViews() {
		for (View view : views)
			view.update();
	}

}
