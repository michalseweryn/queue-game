package queue_game.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Piotr
 * Deck of Cards
 */
public class DeckOfCards {
	private List<QueuingCard> deck=new ArrayList<QueuingCard>();
	
	public void addToTheTop(QueuingCard card){
		deck.add(deck.size(), card);
	}
	public QueuingCard getCard(){
		return deck.remove(deck.size()-1);
	}
	public int size(){
		return deck.size();
	}
	public void shuffle(){
		Collections.shuffle(deck);
	}
	
	public void fill(){
		for (int i=0; i<10; i++)
			deck.add(QueuingCard.COMMUNITY_LIST);
		/*
		deck.addAll(Arrays.asList(QueuingCard.values()));
		*/
		 
	}
}
