/**
 * 
 */
package queue_game.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import queue_game.View;
import queue_game.model.DeckOfCards;
import queue_game.model.GamePhase;
import queue_game.model.GameState;
import queue_game.model.ProductType;
import queue_game.model.QueuingCard;
import queue_game.model.Store;

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
	private ProductType selectedQueue = null;
	private Thread gameThread = null;
	private QueuingCard selectedQueuingCard = null;
	private boolean iPass[]=new boolean[6];
	private boolean pass = false;

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
			queueJumping();
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
	 *  Waits for selection of queue by active player.
	 *  
	 * @return destination of selected queue.
	 * @throws InterruptedException 
	 */
	private synchronized ProductType requestQueue() throws InterruptedException{
		expectedType = ProductType.class;
		updateViews();
		while(selectedQueue == null)
				wait();
		expectedType = null;
		ProductType queue = selectedQueue;
		selectedQueue = null;
		return queue;
		
	}
	public synchronized QueuingCard requestQueuingCard(){
		expectedType = QueuingCard.class;
		while(selectedQueuingCard==null && !pass){
			try{
				wait();
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		expectedType=null;
		QueuingCard card=selectedQueuingCard;
		selectedQueuingCard=null;
		pass=false;
		return card;
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
					gameState.setActivePlayer(player);
					gameState.putPlayerPawn(player, requestQueue());
					timeSinceLastPawnLocation = 0;
				} else {
					timeSinceLastPawnLocation++;
				}
				if(timeSinceLastPawnLocation > gameState.getNumberOfPlayers())
					break outer;
			}
		}
	}
	
	public void  queueJumping(){
		gameState.setCurrentGamePhase(GamePhase.JUMPING);
		final int numOfPlayers=gameState.getNumberOfPlayers();
		QueuingCard current;
		while(true){
			boolean allPassed=true;
			for (int player=gameState.getGameOpeningMarker(),  i=0;
			i<numOfPlayers; 
			i++, player=(player+1)%numOfPlayers){
				DeckOfCards myDeck=this.getGameState().getDeck(player);
				if(!iPass[player] && myDeck.numOfCardsOnHand()>0){
					gameState.setActivePlayer(player);
					current=requestQueuingCard();
					if(current==null){
						myDeck.iPass();
						continue;
					}
					myDeck.iUseCard(current);
					allPassed=false;
					switch(current){
					case CLOSED_FOR_STOCKTAKING:
						break;
					case COMMUNITY_LIST:
						System.out.println("USING COMMUNITY LIST");
						break;
					case CRITISIZING_AUTHORITIES:
						break;
					case DELIVERY_ERROR:
						break;
					case INCREASED_DELIVERY:
						break;
					case LUCKY_STRIKE:
						break;
					case MOTHER_WITH_CHILD:
						break;
					case NOT_YOUR_PLACE:
						break;
					case TIPPING_FRIEND:
						break;
					case UNDER_THE_COUNTER_GOODS:
						break;
					default:
						break;

					}
				}
			}
			if(allPassed){
				break;
			}
		}
			
		//NA KONCU ODSWIERZYC LISTE IPASS[]. I ZEBY WSZYSTKO STYKALO
		for (int i=0; i<6; i++){
			iPass[i]=false;
		}
	}

	/**
	 * @return true if game is over and false otherwise.
	 */
	private boolean gameOver() {
		return gameState.getDayNumber() > 5;
	}

	/**
	 * Randomizes 3 stores with repetitions and delivers products to them
	 * and decreases number of products.
	 * 
	 * @author krzysiek & Helena
	 */
	public void deliveryPhase() {
		int rand;
		Random rG = new Random();
		for (int i = 0; i < 3; i++) {
			rand = rG.nextInt(5);
			ProductType type =ProductType.values()[rand];
			Store deliveredStore = gameState
					.getStore(type);
			deliveredStore.addProducts(1);
			Integer numberOfProducts[] = gameState.getNumberOfProducts();
			numberOfProducts[type.ordinal()]=numberOfProducts[type.ordinal()]-1;
			gameState.setNumberOfProducts(numberOfProducts);
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
		if (playerNo != gameState.getActivePlayer())
			return;
		
		if (expectedType == ProductType.class) {
			
			selectedQueue = destination;
			try {
				notifyAll();
				return;
			} finally {}
		}
	}
	public void queuingCardSelected(int playerNo, QueuingCard card){
		if(playerNo != gameState.getActivePlayer())
			return;
		if(expectedType==QueuingCard.class){
			if(card==null){
				iPass[playerNo]=true;
				pass=true;
			}
			selectedQueuingCard=card;
			try{
				return;
			}finally{
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

}
