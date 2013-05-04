package queue_game.model;

import junit.framework.TestCase;

public class StoreTest extends TestCase {
	public void testStore() {
		Store s = new Store(ProductType.KIOSK);
		System.out.println(s);
	}

	public void testAddPawn() {
		Store s = new Store(ProductType.KIOSK);
		System.out.println(s);
		s.addPawn(0);
		s.addPawn(1);
		System.out.println(s.queue.getFirst());
		System.out.println(s.queue.getLast());
	}

}
