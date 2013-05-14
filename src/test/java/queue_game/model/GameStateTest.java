package queue_game.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class GameStateTest {
	private GameState gameState;

	@Before
	public void setUp() throws Exception {
		gameState = new GameState();
	}

	@Test
	public void resetCardsTest() {
		gameState.resetCards();
		for(Player p: gameState.getPlayersList())
			assertEquals(10, p.getDeck().size());
	}

	@Test
	public void resetNumberOfProductsTest() {
		gameState.resetNumberOfProducts();
		for(int n: gameState.getNumberOfProducts())
			assertEquals(50, n);
	}

	@Test
	public void resetPlayersTest() {
		gameState.resetPlayers();
		assertEquals(5, gameState.getPlayersList().size());
		for(Player p: gameState.getPlayersList())
			assertEquals(10, p.getDeck().size());
		for(int i = 0; i < 5; ++i)
			assertEquals(5, gameState.getNumberOfPawns(i));
	}

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
		gameState.putPlayerPawn(5, ProductType.FURNITURE);
		gameState.putPlayerPawn(4, ProductType.FURNITURE);
		gameState.putPlayerPawn(3, ProductType.KIOSK);
		gameState.putPlayerPawn(1, ProductType.KIOSK);
		gameState.putPlayerPawn(4, ProductType.KIOSK);
		gameState.putPlayerPawn(2, ProductType.RTV_AGD);
		gameState.putPlayerPawn(3, ProductType.RTV_AGD);
		gameState.putPlayerPawn(1, ProductType.RTV_AGD);
		assertEquals(new ArrayList<Integer>(Arrays.asList(0, 1, 3, 2, 4)), gameState.getStore(ProductType.CLOTHES).getQueue());
		assertEquals(new ArrayList<Integer>(Arrays.asList(3, 2, 1)), gameState.getStore(ProductType.FOOD).getQueue());
		assertEquals(new ArrayList<Integer>(Arrays.asList(0, 5, 4)), gameState.getStore(ProductType.FURNITURE).getQueue());
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
	public void sellTest() {
		gameState.putPlayerPawn(0, ProductType.CLOTHES);
		gameState.putPlayerPawn(4, ProductType.CLOTHES);
		gameState.putPlayerPawn(2, ProductType.CLOTHES);
		gameState.sell(ProductType.CLOTHES);
		assertEquals(new ArrayList<Integer>(Arrays.asList(4, 2)), gameState.getStore(ProductType.CLOTHES).getQueue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void sellTest2() {
		gameState.sell(ProductType.CLOTHES);
	}

	@Test
	public void resetNumberOfProductsTest2() {
		GameState g= new GameState();
		g.resetNumberOfProducts();
		Integer tab[]=g.getNumberOfProducts();
		assertEquals(new Integer(50),tab[2]);
	}

	@Test
	public void resetPlayersTest2() {
		GameState g= new GameState();
		g.reset(5);
		g.resetPlayers();
		assertEquals(5,g.getPlayersList().get(1).getNumberOfPawns());
	}

	@Test
	public void putPawnofSpeculatorTest() {
		GameState g= new GameState();
		g.reset(5);
		g.putPawnofSpeculator(ProductType.FURNITURE);
		assertEquals(true,g.getStore(ProductType.FURNITURE).getQueue().contains(-1));
	}

}