package queue_game.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DecksOfQueuingCardsBoxTest {
	private GameState gameState;
	private DecksOfQueuingCardsBox deck;
	
	@Before
	public void setUp() throws Exception {
		gameState = new GameState(Arrays.asList("Gracz 1", "Gracz 2", "Gracz 3", 
				"Gracz 4", "Gracz 5"));
		deck = new DecksOfQueuingCardsBox(gameState);
	}

	@Test
	public void getCardsToFillTheHandOfPlayerTest() {
		List<QueuingCard> tmp = deck.getCardsToFillTheHandOfPlayer(2);
		assertEquals(tmp.isEmpty(),false);
    }		

	@Test
	public void hasCardTest(){
		gameState.getPlayer(2).addCardsToHand(deck.getCardsToFillTheHandOfPlayer(2));
		assertEquals(true,deck.hasCard(2, QueuingCard.DELIVERY_ERROR));
		assertEquals(false,deck.hasCard(3, QueuingCard.DELIVERY_ERROR));
	}
	@Test
	public void removeTest(){
		deck.remove(2);
		assertEquals(false,deck.hasCard(2, QueuingCard.DELIVERY_ERROR));
	}
	
	@Test
    public void isEmptyTest() {
    	gameState.getPlayer(2).addCardsToHand(deck.getCardsToFillTheHandOfPlayer(2));
    	assertEquals(deck.isEmpty(2),false);
    	assertEquals(deck.isEmpty(3),true);
	}
	
}