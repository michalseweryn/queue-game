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

	}

	public void testResetShoppingList() {
	
	}
	public void testPutPawnofSpeculator() {
		fail("Not yet implemented");
	}

	public void testPutPlayerPawn() {
		fail("Not yet implemented");
	}

	public void testSell() {
		fail("Not yet implemented");
	}

	public void testObject() {
		fail("Not yet implemented");
	}

	public void testGetClass() {
		fail("Not yet implemented");
	}

	public void testHashCode() {
		fail("Not yet implemented");
	}

	

}
