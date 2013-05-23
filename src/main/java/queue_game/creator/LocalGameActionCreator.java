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
	private final String SELECT_QUEUE = "Wybierz kolejkę do której chcesz dostawić pionek";
	private final String SELECT_PAWN = "Wybierz pionek do przeniesienia/usuniecia";
	private final String OFFER_1_OF_1 = "Wybierz jeden ze swoich produktów";
	private final String OFFER_1_OF_2 = "Wybierz pierwszy ze swoich produktów";
	private final String OFFER_2_OF_2 = "Wybierz drugi ze swoich produktów";
	private final String SELECT_FROM_MARKET = "Wybierz produkt z bazaru.";

	GameState gameState;

	public LocalGameActionCreator(GameState gameState) {
		this.gameState = gameState;
	}
	public void addView(View view){
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
			break;
		case OPENING:
			break;
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
	private GameAction getPCTAction() throws InterruptedException {
		PawnParameters pawn = requestPawn(SELECT_PAWN);
		if(pawn.destination.ordinal()==-1)return new GameAction(GameActionType.PAWN_REMOVED_PASSED,gameState.getActivePlayer());
		else return new GameAction(GameActionType.PAWN_REMOVED,gameState.getActivePlayer(),pawn.destination,pawn.position);
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
	public synchronized QueuingCard requestQueuingCard()
			throws InterruptedException {
		expectedType = QueuingCard.class;
		updateViews();
		while (expectedType != null)
			wait();
		return selectedQueuingCard;
	}

	private synchronized PawnParameters requestPawn(String message)
			throws InterruptedException {
		expectedType = PawnParameters.class;
		messageForPlayer(message);
		updateViews();
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
		for(View view : views)
			view.update();
	}

}
