package queue_game.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GameStateTest {
	private GameState gameState;

	@Before
	public void setUp() throws Exception {
		gameState = new GameState();
		ArrayList<List<Integer>> lists = new ArrayList<List<Integer>>();
		lists.add(Arrays.asList( 4, 0, 2, 1, 3 )); 
		lists.add(Arrays.asList( 3, 4, 1, 0, 2 ));
		lists.add(Arrays.asList( 2, 3, 0, 4, 1 ));
		lists.add(Arrays.asList( 1, 2, 4, 3, 0 )); 
		lists.add(Arrays.asList( 0, 1, 3, 2, 4 ));
		gameState.initGame(Arrays.asList("Gracz 1", "Gracz 2", "Gracz 3", 
				"Gracz 4", "Gracz 5"), lists);
	}

	/*@Test
	public void resetCardsTest() {
		gameState.resetQueuingCards();
		for(Player p: gameState.getPlayersList())
			assertEquals(10, p.getDeck().size());
	}

		@Test
	public void resetNumberOfProductsTest() {
		gameState.resetNumberOfProductsLeft();
		for(int n: gameState.getNumberOfProductsLeft())
			assertEquals(12, n);
	}

	@Test
	public void resetPlayersTest() {
		gameState.resetPlayers();
		assertEquals(5, gameState.getPlayersList().size());
		for(Player p: gameState.getPlayersList())
			assertEquals(10, p.getDeck().size());
		for(int i = 0; i < 5; ++i)
			assertEquals(5, gameState.getNumberOfPawns(i));
	}*/

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
//temporarily out of use
	/*

	@Test
	public void sellTest() {
		gameState.putPlayerPawn(0, ProductType.CLOTHES);
		gameState.putPlayerPawn(4, ProductType.CLOTHES);
		gameState.putPlayerPawn(2, ProductType.CLOTHES);
		gameState.sell(ProductType.CLOTHES);
		assertEquals(new ArrayList<Integer>(Arrays.asList(4, 2)), gameState.getStore(ProductType.CLOTHES).getQueue());
	}@Test(expected = IllegalArgumentException.class)
	public void sellTest2() {
		gameState.sell(ProductType.CLOTHES);
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

	@Test
	public void putPawnofSpeculatorTest() {
		gameState.initGame(5);
		gameState.putSpeculators();
		assertEquals(true,gameState.getStore(ProductType.FURNITURE).getQueue().contains(-1));
	}*/

}