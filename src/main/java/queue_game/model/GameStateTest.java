package queue_game.model;

import junit.framework.TestCase;

public class GameStateTest extends TestCase {

	public void testResetNumberOfProducts() {
		GameState g= new GameState();
		g.resetNumberOfProducts();
		Integer tab[]=g.getNumberOfProducts();
		assertEquals(new Integer(50),tab[2]);
	}

	public void testResetPlayers() {
		GameState g= new GameState();
		g.reset(5);
		g.resetPlayers();
		assertEquals(5,g.getPlayersList().get(1).getNumberOfPawns());
	}


	public void testPutPawnofSpeculator() {
		GameState g= new GameState();
		g.reset(5);
		g.putPawnofSpeculator(ProductType.FURNITURE);
		assertEquals(true,g.getStore(ProductType.FURNITURE).getQueue().contains(-1));
	}

	

}
