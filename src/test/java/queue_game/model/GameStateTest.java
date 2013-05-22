package queue_game.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GameStateTest {
	private GameState gameState;
	private ArrayList<List<Integer>> lists = new ArrayList<List<Integer>>();
	
	@Before
	public void setUp() throws Exception {
		gameState = new GameState();
		lists.add(Arrays.asList( 4, 0, 2, 1, 3 )); 
		lists.add(Arrays.asList( 3, 4, 1, 0, 2 ));
		lists.add(Arrays.asList( 2, 3, 0, 4, 1 ));
		lists.add(Arrays.asList( 1, 2, 4, 3, 0 )); 
		lists.add(Arrays.asList( 0, 1, 3, 2, 4 ));
		gameState.initGame(Arrays.asList("Gracz 1", "Gracz 2", "Gracz 3", 
				"Gracz 4", "Gracz 5"), lists);
	}
	
	
	
	@Test
	public void initGameTest() {
		//reset()
		assertEquals(ProductType.values().length, gameState.getStores().length);
		
		//resetNumberOfProductsLeft()
		for(int n: gameState.getNumberOfProductsLeft())
			assertEquals(12, n);
		
		//resetPlayers()
		assertEquals(ProductType.values().length, gameState.getPlayersList().size());
		/*for(Player p: gameState.getPlayersList())
			assertEquals(10, p.getCardsOnHand().size());
		*/
		for(int i = 0; i < ProductType.values().length; ++i){
			assertEquals(5, gameState.getNumberOfPawns(i));
			assertEquals(5, gameState.getPlayersList().get(i).getNumberOfPawns());
		}
		
		//resetShoppingList(shoppingLists);
		int i=0;
		for (Player p : gameState.getPlayersList()){
			assertEquals(true, p.getShoppingList().equals(lists.get(i)));
			i++;
		}
	}
	
	
	
	/*
	UNUSED
	
	@Test
	public void resetCardsTest() {
		gameState.resetQueuingCards();
		for(Player p: gameState.getPlayersList())
			assertEquals(10, p.getDeck().size());
	}
	
	
	@Test
	public void resetNumberOfProductsTest2() {
		gameState.resetNumberOfProductsLeft();
		List<Integer> tab=gameState.getNumberOfProductsLeft();
		assertEquals(12,tab.get(2));
	}

	@Test
	public void resetPlayersTest2() {
		gameState.reset(5);
		gameState.resetPlayers();ale 
		assertEquals(5,gameState.getPlayersList().get(1).getNumberOfPawns());
	}

	*/


	@Test
	public void putPlayerPawnTest() {
		
		gameState.putPlayerPawn(0, ProductType.CLOTHES);
		gameState.putPlayerPawn(1, ProductType.CLOTHES);
		gameState.putPlayerPawn(3, ProductType.CLOTHES);
		gameState.putPlayerPawn(2, ProductType.CLOTHES);
		gameState.putPlayerPawn(4, ProductType.CLOTHES);
		gameState.putPlayerPawn(3, ProductType.FOOD);
		gameState.putPlayerPawn(2, ProductType.FOOD);
		gameState.putPlayerPawn(1, ProductType.FOOD);
		gameState.putPlayerPawn(0, ProductType.FURNITURE);
		gameState.putPlayerPawn(0, ProductType.FURNITURE);
		gameState.putPlayerPawn(4, ProductType.FURNITURE);
		gameState.putPlayerPawn(3, ProductType.KIOSK);
		gameState.putPlayerPawn(1, ProductType.KIOSK);
		gameState.putPlayerPawn(4, ProductType.KIOSK);
		gameState.putPlayerPawn(2, ProductType.RTV_AGD);
		gameState.putPlayerPawn(3, ProductType.RTV_AGD);
		gameState.putPlayerPawn(1, ProductType.RTV_AGD);
		assertEquals(new ArrayList<Integer>(Arrays.asList(0, 1, 3, 2, 4)), gameState.getStore(ProductType.CLOTHES).getQueue());
		assertEquals(new ArrayList<Integer>(Arrays.asList(3, 2, 1)), gameState.getStore(ProductType.FOOD).getQueue());
		assertEquals(new ArrayList<Integer>(Arrays.asList(0, 0, 4)), gameState.getStore(ProductType.FURNITURE).getQueue());
		assertEquals(new ArrayList<Integer>(Arrays.asList(3, 1, 4)), gameState.getStore(ProductType.KIOSK).getQueue());
		assertEquals(new ArrayList<Integer>(Arrays.asList(2, 3, 1)), gameState.getStore(ProductType.RTV_AGD).getQueue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void putPlayerPawnTest2() {
		gameState.putPlayerPawn(-1, ProductType.CLOTHES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void putPlayerPawnTest3() {
		gameState.putPlayerPawn(6, ProductType.CLOTHES);
	}
	

	@Test
	public void putSpeculatorsTest(){
		gameState.putSpeculators();
		for (Store store : gameState.getStores())
			assertEquals(-1, (int)store.getQueue().getLast());
		
	}
	
	
	@Test
	public void transferProductToStoreTest1(){
		for (int i=12; i>=2;)
		{
			gameState.transferProductToStore(ProductType.values()[4], 2);
			i-=2;
			assertEquals(i, gameState.getNumberOfProductsLeft(4));
			assertEquals(12-i, gameState.getStore(ProductType.values()[4]).getNumberOf());
		}
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void transferProductToStoreTest2Exception(){
			gameState.transferProductToStore(ProductType.values()[4], 5);
			int i=7;
			assertEquals(i, gameState.getNumberOfProductsLeft(4));
			assertEquals(12-i, gameState.getStore(ProductType.values()[4]).getNumberOf());	
			gameState.transferProductToStore(ProductType.values()[4], 5);
			i=2;
			assertEquals(i, gameState.getNumberOfProductsLeft(4));
			assertEquals(12-i, gameState.getStore(ProductType.values()[4]).getNumberOf());	
			gameState.transferProductToStore(ProductType.values()[4], 3);
	}
	
	@Test
	public void transferToAnotherStoreTest(){
		
		//1 piece 0->4
		gameState.transferProductToStore(ProductType.values()[0], 5);
		gameState.transferToAnotherStore(ProductType.values()[0],
				ProductType.values()[4], ProductType.values()[0]);
		assertEquals(4, gameState.getStore(ProductType.values()[0]).getNumberOf());
		assertEquals(0, gameState.getStore(ProductType.values()[4]).getNumberOf());
		assertEquals(1, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[0]));
		

		//2 pieces 1->4
		gameState.transferProductToStore(ProductType.values()[1], 5);
		gameState.transferToAnotherStore(ProductType.values()[1],
				ProductType.values()[4], ProductType.values()[1]);
		gameState.transferToAnotherStore(ProductType.values()[1],
				ProductType.values()[4], ProductType.values()[1]);
		assertEquals(3, gameState.getStore(ProductType.values()[1]).getNumberOf());
		assertEquals(0, gameState.getStore(ProductType.values()[4]).getNumberOf());
		assertEquals(2, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[1]));
		assertEquals(1, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[0]));
		
		//1 piece 2->4
		gameState.transferProductToStore(ProductType.values()[2], 5);
		gameState.transferToAnotherStore(ProductType.values()[2],
				ProductType.values()[4], ProductType.values()[2]);
		assertEquals(4, gameState.getStore(ProductType.values()[2]).getNumberOf());
		assertEquals(0, gameState.getStore(ProductType.values()[4]).getNumberOf());
		assertEquals(1, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[2]));
		assertEquals(2, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[1]));
		assertEquals(1, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[0]));
		
		//1 piece (number 1) 4->1
		gameState.transferToAnotherStore(ProductType.values()[4],
				ProductType.values()[1], ProductType.values()[1]);
		assertEquals(4, gameState.getStore(ProductType.values()[1]).getNumberOf());
		assertEquals(0, gameState.getStore(ProductType.values()[4]).getNumberOf());
		assertEquals(1, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[2]));
		assertEquals(1, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[1]));
		assertEquals(1, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[0]));
		
		//1 piece (number 0) 4->0
		gameState.transferToAnotherStore(ProductType.values()[4],
				ProductType.values()[0], ProductType.values()[0]);
		assertEquals(5, gameState.getStore(ProductType.values()[0]).getNumberOf());
		assertEquals(0, gameState.getStore(ProductType.values()[4]).getNumberOf());
		assertEquals(1, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[2]));
		assertEquals(1, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[1]));
		assertEquals(0, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[0]));
		
		//1 piece (number 1) 4->0
		gameState.transferToAnotherStore(ProductType.values()[4],
				ProductType.values()[0], ProductType.values()[1]);
		assertEquals(4, gameState.getStore(ProductType.values()[1]).getNumberOf());
		assertEquals(0, gameState.getStore(ProductType.values()[4]).getNumberOf());
		assertEquals(1, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[2]));
		assertEquals(0, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[1]));
		assertEquals(0, gameState.getStore(ProductType.values()[4]).getNumberOf(
				ProductType.values()[0]));
		assertEquals(5, gameState.getStore(ProductType.values()[0]).getNumberOf(
				ProductType.values()[0]));
		assertEquals(1, gameState.getStore(ProductType.values()[0]).getNumberOf(
				ProductType.values()[1]));
		
		
	}
	
	@Test
	public void closeOpenTest(){
		assertEquals(false, gameState.getStore(ProductType.values()[4]).isClosed());
		gameState.close(ProductType.values()[4]);
		assertEquals(true, gameState.getStore(ProductType.values()[4]).isClosed());
		gameState.openStores();
		assertEquals(false, gameState.getStore(ProductType.values()[4]).isClosed());
		
	}
	
	@Test
	public void movePawnAndRemovePawnTest(){
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.add(0);
		list.add(1);
		list.add(2);
		gameState.getStore(ProductType.values()[0]).setQueue(list);
		gameState.getStore(ProductType.values()[1]).setQueue(new LinkedList<Integer>(list));
		gameState.movePawn(ProductType.values()[0], 1, ProductType.values()[1], 1);
		
		assertEquals(true, (gameState.getStore(ProductType.values()[0]).getQueue().equals(
				Arrays.asList(0,2))));
		assertEquals(true, (gameState.getStore(ProductType.values()[1]).getQueue().equals(
				Arrays.asList(0,1,1,2))));
		
		gameState.getPlayersList().get(1).setNumberOfPawns(3);
		gameState.removePlayerPawn( 1, ProductType.values()[1]);
		
		assertEquals(4, gameState.getPlayersList().get(1).getNumberOfPawns());
		assertEquals(true, (gameState.getStore(ProductType.values()[1]).getQueue().equals(
				Arrays.asList(0,1,2))));
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void removePlayerPawnTestException1(){
		gameState.removePlayerPawn( 1, ProductType.values()[1]);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void removePlayerPawnTestException2(){
		gameState.getPlayersList().get(1).setNumberOfPawns(5);
		gameState.getStore(ProductType.values()[0]).setQueue(new LinkedList<Integer>());
		gameState.removePlayerPawn( 0, ProductType.values()[0]);
	}
	
	
	
/*
	@Test
	public void sellTest() {
		gameState.putPlayerPawn(0, ProductType.CLOTHES);
		gameState.putPlayerPawn(4, ProductType.CLOTHES);
		gameState.putPlayerPawn(2, ProductType.CLOTHES);
		gameState.sell(ProductType.CLOTHES, ProductType.CLOTHES);
		assertEquals(new ArrayList<Integer>(Arrays.asList(4, 2)), gameState.getStore(ProductType.CLOTHES).getQueue());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void sellTest2() {
		gameState.sell(ProductType.CLOTHES);
	}
*/

	@Test
	public void sellTest() {
		gameState.putPlayerPawn(0, ProductType.CLOTHES);
		gameState.putPlayerPawn(1, ProductType.CLOTHES);
		gameState.putPlayerPawn(3, ProductType.CLOTHES);
		gameState.putPlayerPawn(2, ProductType.CLOTHES);
		gameState.putPlayerPawn(4, ProductType.CLOTHES);
		gameState.getStore(ProductType.CLOTHES).addProducts(3);
		gameState.getStore(ProductType.CLOTHES).addProduct(ProductType.FOOD);
		gameState.getStore(ProductType.CLOTHES).addProduct(ProductType.FURNITURE);
		gameState.sell(ProductType.CLOTHES, ProductType.FURNITURE);
		assertEquals(new ArrayList<Integer>(Arrays.asList(1, 3, 2, 4)), gameState.getStore(ProductType.CLOTHES).getQueue());
		gameState.sell(ProductType.CLOTHES, ProductType.CLOTHES);
		assertEquals(new ArrayList<Integer>(Arrays.asList(3, 2, 4)), gameState.getStore(ProductType.CLOTHES).getQueue());
		gameState.sell(ProductType.CLOTHES, ProductType.CLOTHES);
		assertEquals(new ArrayList<Integer>(Arrays.asList(2, 4)), gameState.getStore(ProductType.CLOTHES).getQueue());
		gameState.sell(ProductType.CLOTHES, ProductType.CLOTHES);
		assertEquals(new ArrayList<Integer>(Arrays.asList(4)), gameState.getStore(ProductType.CLOTHES).getQueue());
		gameState.sell(ProductType.CLOTHES, ProductType.FOOD);
		assertEquals(new ArrayList<Integer>(), gameState.getStore(ProductType.CLOTHES).getQueue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void sellTest2() {
		gameState.sell(ProductType.CLOTHES, ProductType.CLOTHES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void sellTest3() {
		gameState.putPlayerPawn(0, ProductType.CLOTHES);
		gameState.getStore(ProductType.CLOTHES).addProducts(3);
		gameState.sell(ProductType.CLOTHES, ProductType.FOOD);
	}

	@Test
	public void reverseTest() {
		gameState.putPlayerPawn(0, ProductType.CLOTHES);
		gameState.putPlayerPawn(1, ProductType.CLOTHES);
		gameState.putPlayerPawn(3, ProductType.CLOTHES);
		gameState.putPlayerPawn(2, ProductType.CLOTHES);
		gameState.putPlayerPawn(4, ProductType.CLOTHES);
		gameState.reverse(ProductType.CLOTHES);
		gameState.reverse(ProductType.FOOD);
		gameState.reverse(ProductType.FURNITURE);
		gameState.reverse(ProductType.KIOSK);
		gameState.reverse(ProductType.RTV_AGD);
		assertEquals(new ArrayList<Integer>(Arrays.asList(4, 2, 3, 1, 0)), gameState.getStore(ProductType.CLOTHES).getQueue());
	}

	@Test
	public void setActivePlayerTest() {
		gameState.setActivePlayer(0);
		assertEquals(0, gameState.getActivePlayer());
		gameState.setActivePlayer(1);
		assertEquals(1, gameState.getActivePlayer());
		gameState.setActivePlayer(2);
		assertEquals(2, gameState.getActivePlayer());
		gameState.setActivePlayer(3);
		assertEquals(3, gameState.getActivePlayer());
		gameState.setActivePlayer(4);
		assertEquals(4, gameState.getActivePlayer());
	}

	@Test(expected = IllegalArgumentException.class)
	public void setActivePlayerTest2() {
		gameState.setActivePlayer(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setActivePlayerTest3() {
		gameState.setActivePlayer(5);
	}

	@Test
	public void setCurrentGamePhaseTest() {
		gameState.setCurrentGamePhase(GamePhase.QUEUING_UP);
		assertEquals(GamePhase.QUEUING_UP, gameState.getCurrentGamePhase());
		gameState.setCurrentGamePhase(GamePhase.DELIVERY);
		assertEquals(GamePhase.DELIVERY, gameState.getCurrentGamePhase());
		gameState.setCurrentGamePhase(GamePhase.JUMPING);
		assertEquals(GamePhase.JUMPING, gameState.getCurrentGamePhase());
		gameState.setCurrentGamePhase(GamePhase.OPENING);
		assertEquals(GamePhase.OPENING, gameState.getCurrentGamePhase());
		gameState.setCurrentGamePhase(GamePhase.EXCHANGING);
		assertEquals(GamePhase.EXCHANGING, gameState.getCurrentGamePhase());
		gameState.setCurrentGamePhase(GamePhase.PCT);
		assertEquals(GamePhase.PCT, gameState.getCurrentGamePhase());
	}

	@Test
	public void removePlayerPawnTest() {
		gameState.putPlayerPawn(0, ProductType.CLOTHES);
		gameState.putPlayerPawn(1, ProductType.CLOTHES);
		gameState.putPlayerPawn(3, ProductType.CLOTHES);
		gameState.putPlayerPawn(2, ProductType.CLOTHES);
		gameState.putPlayerPawn(4, ProductType.CLOTHES);
		gameState.removePlayerPawn(2, ProductType.CLOTHES);
		assertEquals(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 4)), gameState.getStore(ProductType.CLOTHES).getQueue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void removePlayerPawnTest2() {
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.add(0);
		list.add(1);
		list.add(2);
		gameState.getStore(ProductType.values()[0]).setQueue(list);
		gameState.getStore(ProductType.values()[1]).setQueue(list);
		gameState.movePawn(ProductType.values()[0], 1, ProductType.values()[1], 1);
		
		System.out.println(gameState.getStore(ProductType.values()[0]).getQueue());
		gameState.removePlayerPawn(0, ProductType.CLOTHES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removePlayerPawnTest3() {
		gameState.removePlayerPawn(0, ProductType.CLOTHES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removePlayerPawnTest4() {
		gameState.removePlayerPawn(-1, ProductType.CLOTHES);
	}

	/*@Test(expected = IllegalArgumentException.class)
	public void removePlayerPawnTest5() {
		gameState.putPlayerPawn(0, ProductType.CLOTHES);
		gameState.putPlayerPawn(1, ProductType.CLOTHES);
		gameState.putPlayerPawn(3, ProductType.CLOTHES);
		gameState.putPlayerPawn(2, ProductType.CLOTHES);
		gameState.putPlayerPawn(4, ProductType.CLOTHES);
		gameState.removePlayerPawn(3, ProductType.CLOTHES);
	}*/ //????

}
