package queue_game.model;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Piotr
 * Deck of Cards
 */
public class DeckOfCards {
	private List<QueuingCard> deck=new ArrayList<QueuingCard>();
	private List<QueuingCard> cardsOnHand=new ArrayList<QueuingCard>();

	private void addToTheTop(QueuingCard card){
		deck.add(deck.size(), card);
		if(cardsOnHand.remove(card));
		else{
			throw new RuntimeException("Karty w rece sie chrzania");
		}
	}
	/**
	 * 
	 * @param card
	 * Removes Card from the list of Cards "on hand"
	 */
	public void iUseCard(QueuingCard card){
		cardsOnHand.remove(card);
	}
	/**
	 * 
	 * @return
	 *  Takes card from the top of the deck, and gives it to "on hand" list
	 */
	public QueuingCard getCard(){
		cardsOnHand.add(deck.get(deck.size()-1));
		return deck.remove(deck.size()-1);
	}
	/**
	 * Writes cards from hand to the deck
	 */
	public void iPass(){
		while(!cardsOnHand.isEmpty()){
			this.addToTheTop(cardsOnHand.get(0));
		}
	}
	/**
	 * 
	 * @return
	 * returns the size of the deck
	 */
	public int size(){
		return deck.size();
	}
	/**
	 * Shuffles the deck
	 */
	public void shuffle(){
		Collections.shuffle(deck);
	}
	/**
	 * fills the deck of cards
	 */
	public void fill(){
		for (int i=0; i<10; i++)
			deck.add(QueuingCard.COMMUNITY_LIST);
		/*
		deck.addAll(Arrays.asList(QueuingCard.values()));
		*/
	}
	/**
	 * 
	 * @return
	 * return the number of cards on hand
	 */
	public int numOfCardsOnHand(){
		return cardsOnHand.size();
	}

}