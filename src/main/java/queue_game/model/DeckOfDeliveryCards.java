package queue_game.model;


import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * @author krzysiek
 */
public class DeckOfDeliveryCards {
	private LinkedList<Integer> deck=new LinkedList<Integer>();
	private static final int defaultNumberOfDC = 2;
	
	/**
	 * Creates new list and calls fill() function
	 */
	public DeckOfDeliveryCards()
	{
		fill();
	}
	
	/**
	 * Creates new list and calls fill(numberOfCardsOfEachKind) function
	 */
	
	public DeckOfDeliveryCards(int numberOfCardsOfEachKind)
	{
		fill(numberOfCardsOfEachKind);
	}
	
	
	/**
	 * Clears the list, refills it with default number of cards (12) of each kind and shuffles
	 */
	public void fill(){
		fill(defaultNumberOfDC);
	}
	/**
	 * Clears the list, refills it with given number of cards of each kind and shuffles
	 */
	
	public void fill(int numberOfCardsOfEachKind)
	{
		deck.clear();
		for (int i=0; i<5; i++)
			for (int j=0; j<numberOfCardsOfEachKind; j++)
				deck.add(i);
		shuffle();
	}

	
	
	
	
	public void add(int card){
		deck.add(card);
	}
	
	/**
	 * @throws NoSuchElementException if deck is empty.
	 */
	public int getAndRemoveFirst(){
		int res = deck.getFirst();
		deck.removeFirst();
		return res;
	}
	/**
	 * 
	 * @throws IndexOutOfBoundsException
	 */
	public int get(int index){
		return deck.get(index);
	}
	
	/**
	 * Shuffles the deck
	 */
	private void shuffle(){
		Collections.shuffle(deck);
	}
	
}