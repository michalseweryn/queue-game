package queue_game.model;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class DeckOfDeliveryCardsTest {
	private StandardDeckOfDeliveryCards testDeck;
	
	@Before
	public void setUp() throws Exception {
		testDeck = new StandardDeckOfDeliveryCards();
	}
	
	@Test
	public void ConstructorTest() {
		ListIsFullTest();
	}
	
	@Test
	public void FillTest() {
		testDeck.fill();
		ListIsFullTest();
		testDeck.removeThreeCards();
		testDeck.peekTwoCards();
		testDeck.removeThreeCards();
		testDeck.fill();
		ListIsFullTest();
	}
	
	private void ListIsFullTest()
	{
		assertEquals(15, testDeck.getDeck().size());
		for (ProductType pT : ProductType.values())
			for (int i=1; i<=3; i++){
				boolean contains = false;
				for (DeliveryCard dC : testDeck.getDeck())
					if(dC.getProductType() == pT && dC.getAmount() == i)
						contains = true;
				assertEquals(true, contains);	
			}
	}
	
	@Test
	public void removeThreeCardsTest1() {
		testDeck.fill();
		for (ProductType pT : ProductType.values()){
			Collection<DeliveryCard> temp = testDeck.removeThreeCards();
			int i=1;
			for (DeliveryCard dC: temp){
				assertEquals(pT, dC.getProductType());
				assertEquals(i, dC.getAmount());
				i++;
			}
		}
		assertEquals(true, testDeck.getDeck().isEmpty());
			
	}
	
	@Test(expected = NoSuchElementException.class)
	public void removeThreeCardsTest2Exception(){
		for (ProductType pT : ProductType.values()){
			testDeck.removeThreeCards();
		}
		testDeck.removeThreeCards();
	}

	
	@Test
	public void peekTwoCardsTest1ReterningRightCards() {
		testDeck.fill();
		for (ProductType pT : ProductType.values()){
			List<DeliveryCard> temp = testDeck.peekTwoCards();
			int i=1;
			for (DeliveryCard dC: temp){
				if(i==3) break;
				assertEquals(temp.get(i-1).getProductType(), dC.getProductType());
				assertEquals(temp.get(i-1).getAmount(), dC.getAmount());
				i++;
			}
			testDeck.removeThreeCards();
		}
		assertEquals(true, testDeck.getDeck().isEmpty());
	}
	

	@Test
	public void peekTwoCardsTest2NotChangingTheDeck() {
		testDeck.fill();
		realTest();
		testDeck.fill();
		testDeck.shuffle();
		realTest();
		realTest();
		testDeck.removeThreeCards();
		testDeck.removeThreeCards();
		realTest();
		testDeck.removeThreeCards();
		testDeck.removeThreeCards();
		realTest();
		
	}
	private void realTest(){
		List<DeliveryCard> temp = testDeck.peekTwoCards();
		List<DeliveryCard> temp1 = testDeck.peekTwoCards();
		assertEquals(true, temp.equals(temp1));
	}
	
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void peekTwoCardsTest3Exception(){
		for (ProductType pT : ProductType.values()){
			testDeck.removeThreeCards();
		}
		testDeck.peekTwoCards();
	}

}
