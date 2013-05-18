package queue_game.model;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Piotr
 * Deck of Cards
 */
public class DeckOfCards {
	private List<QueuingCard> deck=new ArrayList<QueuingCard>();
	//private List<QueuingCard> cardsOnHand=new ArrayList<QueuingCard>();

	private void addToTheTop(QueuingCard card){
		deck.add(deck.size(), card);
	}
	/**
	 * 
	 * @return
	 *  Takes card from the top of the deck, and gives it to "on hand" list
	 */
	public void getCards(ArrayList<QueuingCard> cardsOnHand){
		for (int i = 0; i < 3 && cardsOnHand.size() < 3; i++) {
			if(size()==0)
				break;
			cardsOnHand.add(deck.get(deck.size() - 1));
			deck.remove(deck.size() - 1);
		}
	}
	/**
	 * Writes cards from hand to the deck
	 */
	public void iPass(ArrayList<QueuingCard> cardsOnHand){
		while(!cardsOnHand.isEmpty()){
			this.addToTheTop(cardsOnHand.remove(0));
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
		/*for (int i=0; i<10; i++)
			deck.add(QueuingCard.COMMUNITY_LIST);*/
		
		deck.addAll(Arrays.asList(QueuingCard.values()));
		
	}

}