package queue_game.model;

import junit.framework.TestCase;

public class StoreTest extends TestCase {
	public void testAddPawn() {
		Store s = new Store(ProductType.KIOSK);
		s.addPawn(0);
		s.addPawn(1);
		assertEquals(new Integer(0),s.getQueue().getFirst());
		assertEquals(new Integer(1),s.getQueue().getLast());
	}

}
