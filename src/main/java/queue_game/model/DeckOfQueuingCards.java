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
public class DeckOfQueuingCards {
	private List<QueuingCard> deck=new ArrayList<QueuingCard>();
	//private List<QueuingCard> cardsOnHand=new ArrayList<QueuingCard>();

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
	 * @author krzysiek
	 * Removes the first occurrence of the specified element from this list,
	 * if it is present.
	 * If this list does not contain the element, it is unchanged. 
	 * @return true if this list contained the specified element
	 */
	public boolean remove(QueuingCard queuingCard){
		return deck.remove(queuingCard);
	}
	
	/**
	 * @author krzysiek
	 * A function similar to addAll function. 
	 */
	public void addListToTheEnd(List<QueuingCard> list){
		try {
			deck.addAll(list);
		} catch (NullPointerException e){
			//nothing
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