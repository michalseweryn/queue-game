/**
 * 
 */
package queue_game.creator;

import java.util.ArrayList;
import java.util.List;

import queue_game.ActionCreator;
import queue_game.model.ButtonType;
import queue_game.model.GameAction;
import queue_game.model.GameActionType;
import queue_game.model.GameState;
import queue_game.model.ProductType;
import queue_game.model.QueuingCard;
import queue_game.view.JCardsArea;
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
	private boolean cancelled = false;
	private JCardsArea cardsArea;
	private static final String SELECT_QUEUE = "Wybierz kolejkę do której chcesz dostawić pionek";
	private static final String SELECT_CARD = "Wybierz kartę przepychanek lub spasuj";
	private static final String SELECT_PAWN_TO_REMOVE = "Wybierz pionek do usuniecia";
	private static final String OFFER_1_OF_1 = "Wybierz jeden ze swoich produktów";
	private static final String OFFER_1_OF_2 = "Wybierz pierwszy ze swoich produktów";
	private static final String OFFER_2_OF_2 = "Wybierz drugi ze swoich produktów";
	private static final String SELECT_FROM_MARKET = "Wybierz produkt z bazaru lub spasuj";
	private static final String SELECT_STORE_INCREASED = "Wybierz sklep w którym ma być zwiększona dostawa";
	private static final String SELECT_PRODUCT_UNDER = "Wybierz towar, który chciałbyś dostać spod lady";
	private static final String SELECT_PAWN_TO_MOVE="Wybierz pionek który ma być przesunięty";
	private static final String SELECT_PRODUCT_TO_MOVE ="Wybierz towar, który ma być przeniesiony";
	private static final String SELECT_STORE_TO_ADD = "Wybierz sklep w którym ma być dodany towar";
	private static final String SELECT_STORE_REVERSED = "Wybierz kolejkę do odwrócenia";
	private static final String SELECT_STORE_TO_CLOSE = "Wybierz sklep do zamknięcia";
	private static final String SELECT_PRODUCT_TO_BUY = "Wybierz produkt który chcesz kupić";
	GameState gameState;

	public LocalGameActionCreator(GameState gameState) {
		this.gameState = gameState;
	}

	public void addView(View view) {
		views.add(view);
	}

	public void setCardsArea(JCardsArea cardsArea){
		this.cardsArea = cardsArea;
	}
	private class PawnParameters {
		int position;
		ProductType destination;
	}

	private class ProductParameters {
		ProductType product;
		ProductType store;
		boolean onHand;
		boolean inMarket;
		
		public ProductParameters(ProductType product, ProductType store, boolean onHand,
				boolean inMarket) {
			this.product = product;
			this.store = store;
			this.onHand = onHand;
			this.inMarket = inMarket;
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
			return getOpeningAction();
		case PCT:
			return getPCTAction();
		case QUEUING_UP:
			return getQueuingUpAction();
		default:
			break;

		}
		return null;
	}
	
	private GameAction getOpeningAction() throws InterruptedException{
		ProductParameters product = requestProduct(SELECT_PRODUCT_TO_BUY, false, false);
		
		/*System.out.println(gameState + ""
				+product.store +""+ product.product);*/
		return new GameAction(GameActionType.PRODUCT_BOUGHT, gameState.getActivePlayer(),
				product.store.ordinal(), product.product);
	}

	private GameAction cardPlayingCancelled()
	{
		cancelled = false;
		return new GameAction(GameActionType.CARD_PLAYING_CANCELLED,
				gameState.getActivePlayer());
	}
	
	
	/**
	 * @return
	 * @throws InterruptedException
	 */
	private GameAction getJumpingAction() throws InterruptedException {
		cardsArea.setButtonType(ButtonType.PASS);
		QueuingCard card = requestQueuingCard(SELECT_CARD);
		//System.out.println(card);
		if (card == null) {
			return new GameAction(GameActionType.CARD_PLAYED_PASSED,
					gameState.getActivePlayer());
		} else {
			cardsArea.setButtonType(ButtonType.CANCEL);
			switch (card) {
			case DELIVERY_ERROR:
				ProductParameters prod = requestProduct(SELECT_PRODUCT_TO_MOVE, false, false);
				if(cancelled) return cardPlayingCancelled();
				ProductType p=requestQueue(SELECT_STORE_TO_ADD);
				if(cancelled) return cardPlayingCancelled();
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.DELIVERY_ERROR,prod.product,prod.store,p);
			case NOT_YOUR_PLACE:
				PawnParameters pawn1 = requestPawn(SELECT_PAWN_TO_MOVE);
				if(cancelled) return cardPlayingCancelled();
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.NOT_YOUR_PLACE,pawn1.destination,pawn1.position);
			case LUCKY_STRIKE:
				PawnParameters pawn = requestPawn(SELECT_PAWN_TO_MOVE);
				if(cancelled) return cardPlayingCancelled();
				ProductType type = requestQueue(SELECT_QUEUE);
				if(cancelled) return cardPlayingCancelled();
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.LUCKY_STRIKE,pawn.destination,pawn.position,type);
			case MOTHER_WITH_CHILD:
				PawnParameters pawn2  = requestPawn(SELECT_PAWN_TO_MOVE);
				if(cancelled) return cardPlayingCancelled();
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.MOTHER_WITH_CHILD,pawn2.destination,pawn2.position);
			case UNDER_THE_COUNTER_GOODS:
				ProductParameters prod2 = requestProduct(SELECT_PRODUCT_UNDER, false, false);
				if(cancelled) return cardPlayingCancelled();
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.UNDER_THE_COUNTER_GOODS, prod2.product,prod2.store);
			case TIPPING_FRIEND:
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.TIPPING_FRIEND);
			case CRITICIZING_AUTHORITIES:
				PawnParameters pawn4 = requestPawn(SELECT_PAWN_TO_MOVE);
				if(cancelled) return cardPlayingCancelled();
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.CRITICIZING_AUTHORITIES,pawn4.destination,pawn4.position);
			case INCREASED_DELIVERY:
				ProductType type1 = requestQueue(SELECT_STORE_INCREASED);
				if(cancelled) return cardPlayingCancelled();
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.INCREASED_DELIVERY, type1);
			case CLOSED_FOR_STOCKTAKING:
				type = requestQueue(SELECT_STORE_TO_CLOSE);
				if(cancelled) return cardPlayingCancelled();
				return new GameAction(GameActionType.CARD_PLAYED, gameState.getActivePlayer(),
						QueuingCard.CLOSED_FOR_STOCKTAKING, type);
				
			case COMMUNITY_LIST:
				type = requestQueue(SELECT_STORE_REVERSED);
				if(cancelled) return cardPlayingCancelled();
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
		cardsArea.setButtonType(ButtonType.PASS);
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
		cardsArea.setButtonType(ButtonType.PASS);
		ProductParameters productToBuy = requestProduct(SELECT_FROM_MARKET, false, true);
		if (productToBuy.product == null)
		{
			return new GameAction(GameActionType.PRODUCT_EXCHANGED_PASSED, gameState.getActivePlayer());
		}
		cardsArea.setButtonType(ButtonType.CANCEL);
		
		if (gameState.getCheapProduct() == productToBuy.product) {
			ProductParameters offeredProduct1 = requestProduct(OFFER_1_OF_1, true, false);
			if(cancelled) return exchangingCancelled();
			return new GameAction(GameActionType.PRODUCT_EXCHANGED_ONE,
					gameState.getActivePlayer(), productToBuy.product, offeredProduct1.product);
		}
		ProductParameters offeredProduct1 = requestProduct(OFFER_1_OF_2, true, false);
		if(cancelled) return exchangingCancelled();
		
		ProductParameters offeredProduct2 = requestProduct(OFFER_2_OF_2, true, false);
		if(cancelled) return exchangingCancelled();
		return new GameAction(GameActionType.PRODUCT_EXCHANGED_TWO,
				gameState.getActivePlayer(), productToBuy.product, offeredProduct1.product,
				offeredProduct2.product);
	}

	private GameAction exchangingCancelled(){
		cancelled = false;
		return new GameAction(GameActionType.EXCHANGING_CANCELLED, gameState.getActivePlayer());
	}
	
	private void messageForPlayer(String message) {
		gameState.setMessage(message);
		//gameState.setErrorMessage("");
		updateViews();
	}
	private void errorMessageForPlayer(String errorMessage) {
		gameState.setErrorMessage(errorMessage);
		//gameState.setErrorMessage("");
		updateViews();
	}

	public synchronized ProductParameters requestProduct(String message, boolean onHand,
				boolean inMarket)
			throws InterruptedException {
		expectedType = ProductParameters.class;
		messageForPlayer(message);
		errorMessageForPlayer("");
		while(true){
			while (expectedType != null && cancelled==false) {
				wait();
			}
			if (cancelled || selectedProduct.product == null || (selectedProduct.onHand == onHand
					  && selectedProduct.inMarket == inMarket))
				break;
			else expectedType = ProductParameters.class;
			
		}
		return selectedProduct;
	}

/*	public synchronized ProductParameters requestProduct(String message, boolean fromMarket)
																		//false == fromHand
			throws InterruptedException {
		expectedType = ProductParameters.class;
		messageForPlayer(message);
		while (expectedType != null && cancelled==false && selectedProduct.) {
			wait();
		}
		return selectedProduct;
	}
*/
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
		errorMessageForPlayer("");
		while (expectedType != null && cancelled==false)
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
		errorMessageForPlayer("");
		while (selectedPawn == null && cancelled==false)
			wait();
		expectedType = null;
		PawnParameters pawn = selectedPawn;
		selectedPawn = null;
		return pawn;

	}
	
	public synchronized void cancel(int playerNo){
		if (playerNo != gameState.getActivePlayer())
			return;
		if(expectedType == QueuingCard.class) return;
		cancelled = true;
		notifyAll();
	
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
			ProductType product, ProductType store, boolean onHand, boolean inMarket) {
		if (activePlayer != gameState.getActivePlayer())
			return;
		if (expectedType != ProductParameters.class)
			return;
		selectedProduct = new ProductParameters(product, store, onHand, inMarket);
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
