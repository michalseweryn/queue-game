package queue_game.model;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class DecksOfQueuingCardsBox implements DecksOfQueuingCardsBoxInterface {
	private StandardDeckOfQueuingCards[] decks = new StandardDeckOfQueuingCards[5];
	private GameState gameState;
	
	
	public DecksOfQueuingCardsBox(GameState gameState){
		this.gameState = gameState;
		for (int i=0; i<5; i++)
			decks[i] = new StandardDeckOfQueuingCards();
	}
	
	
	public List<QueuingCard> getCardsToFillTheHandOfPlayer(int playerNr){
		int amount = 3 - gameState.getPlayer(playerNr).getCardsOnHand().size();
		List<QueuingCard> res = new LinkedList<QueuingCard>();
		try {
			while(amount-- > 0)
				res.add(decks[playerNr].remove());
		}
		catch (NoSuchElementException e){
			//nothing
		}
		return res;
	}
	
	public boolean hasCard(int playerNr, QueuingCard card){
		return decks[playerNr].hasCard(card);
	}
	
	public QueuingCard remove(int player){
		return decks[player].remove();
	}
	
	public void resetAllDecks(){
		for (int i=0; i<gameState.getNumberOfPlayers(); i++)
			decks[i].reset();
	}
	
}
