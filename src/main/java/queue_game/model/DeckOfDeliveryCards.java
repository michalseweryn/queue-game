package queue_game.model;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author krzysiek
 */
public class DeckOfDeliveryCards {
	private LinkedList<DeliveryCard> deck=new LinkedList<DeliveryCard>();
	
	/**
	 * Creates new list and calls fill() function
	 */
	public DeckOfDeliveryCards()
	{
		fill();
	}
	
	
	/**
	 * Fills and shuffles
	 */
	public void fill(){
		deck.clear();
		for (ProductType pT : ProductType.values())
			for (int i=1; i<=3; i++)
				deck.add(new DeliveryCard(pT, i));
		shuffle();
	}

	
	/**
	 * Removes 3 cards from the top of the deck and returns them as a list.
	 * If there aren't 3 cards in the deck, the list contains less
	 * (including 0) cards.
	 */
	public List<DeliveryCard> removeThreeCards(){
		List<DeliveryCard> res = new LinkedList<DeliveryCard>();
		try {
			for (int i=0; i<3; i++)
				res.add(deck.removeFirst());
		}
		catch (NoSuchElementException e){
			//nothing
		}
		return res;
	}
	/**
	 * Returns and not removes 2 cards from the top of the deck
	 * and returns them as a list.
	 * If there aren't 2 cards in the deck, the list contains less
	 * (including 0) cards.
	 */
	public List<DeliveryCard> peekTwoCards(){
		List<DeliveryCard> res = new LinkedList<DeliveryCard>();
		try {
			res.add(deck.get(0));
			res.add(deck.get(1));
		}
		catch (IndexOutOfBoundsException e){
			//nothing
		}
		return res;
	}
	
	/**
	 * Shuffles the deck
	 */
	private void shuffle(){
		Collections.shuffle(deck);
	}
	
}