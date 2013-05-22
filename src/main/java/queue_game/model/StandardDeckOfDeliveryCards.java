package queue_game.model;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * @author krzysiek
 */
public class StandardDeckOfDeliveryCards implements DeckOfDeliveryCards{
	private LinkedList<DeliveryCard> deck=new LinkedList<DeliveryCard>();
	
	/**
	 * Creates new list and calls fill() function
	 */
	public StandardDeckOfDeliveryCards()
	{
		fill();
		shuffle();
	}
	
	/**
	 * Fills (but not shuffles) the list.
	 */
	public void fill(){
		deck.clear();
		for (ProductType pT : ProductType.values())
			for (int i=1; i<=3; i++)
				deck.add(new DeliveryCard(pT, i));
	}

	
	/**
	 * Removes 3 cards from the top of the deck and returns them as a list.
	 * @throws NoSuchElementException if the deck is empty
	 */
	public Collection<DeliveryCard> removeThreeCards(){
		List<DeliveryCard> res = new LinkedList<DeliveryCard>();
		for (int i=0; i<3; i++)
			res.add(deck.removeFirst());
		return (Collection<DeliveryCard>) res;
	}
	/**
	 * Returns and not removes 2 cards from the top of the deck
	 * and returns them as a list.
	 * @throws IndexOutOfBoundsException if the deck is empty.
	 */
	
	public List<DeliveryCard> peekTwoCards(){
		List<DeliveryCard> res = new LinkedList<DeliveryCard>();
		res.add(deck.get(0));
		res.add(deck.get(1));
		return res;
	}
	
	/**
	 * Shuffles the deck
	 */
	public void shuffle(){
		Collections.shuffle(deck);
	}
	
	List<DeliveryCard> getDeck(){
		return deck;
	}
}