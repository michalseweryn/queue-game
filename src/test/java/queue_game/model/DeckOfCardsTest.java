package queue_game.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class DeckOfCardsTest {
	private DeckOfCards deck;

	@Before
	public void setUp() throws Exception {
		deck = new DeckOfCards();
		deck.fill();
		deck.shuffle();
	}

	@Test
	public void getCardsTest() {
		assertEquals(10, deck.size());
		ArrayList<QueuingCard> cards = new ArrayList<QueuingCard>();
		deck.getCards(cards);
		assertEquals(7, deck.size());
		assertEquals(3, cards.size());
		cards.remove(cards.size() - 1);
		deck.getCards(cards);
		assertEquals(6, deck.size());
		assertEquals(3, cards.size());
	}

	@Test
	public void fillTest() {
		
		ArrayList<QueuingCard> temp;
		ArrayList<QueuingCard> all = new ArrayList<QueuingCard>();
		while(deck.size() > 0){
			deck.getCards(temp = new ArrayList<QueuingCard>());
			all.addAll(temp);
		}
		Collections.sort(all);
		assertEquals(all, Arrays.asList(QueuingCard.values()));
	}

}
