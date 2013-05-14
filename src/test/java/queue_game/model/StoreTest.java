package queue_game.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StoreTest {
	private Store store;

	@Before
	public void setUp() throws Exception {
		store = new Store(ProductType.CLOTHES);
	}

	@Test
	public void addPawnTest() {
		store.addPawn(0);
		store.addPawn(1);
		assertEquals(0, store.getQueue().getFirst());
		assertEquals(1, store.getQueue().getLast());
		store.addPawn(2);
		assertEquals(2, store.getQueue().getLast());
	}

	@Test(expected = IllegalArgumentException.class)
	public void addPawnTest2() {
		store.addPawn(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addPawnTest3() {
		store.addPawn(6);
	}

	@Test
	public void productsTest() {
		store.addProducts(2);
		assertEquals(false, store.hasDiffrentProducts());
		assertEquals(2, store.getNumberOf());
		store.addProduct(ProductType.FOOD);
		assertEquals(true, store.hasDiffrentProducts());
		assertEquals(2, store.getNumberOf());
		assertEquals(1, store.getNumberOf(ProductType.FOOD));
		store.removeProducts(1, store.productType);
		assertEquals(true, store.hasDiffrentProducts());
		assertEquals(1, store.getNumberOf());
		assertEquals(1, store.getNumberOf(ProductType.FOOD));
		store.removeProducts(1, ProductType.FOOD);
		assertEquals(false, store.hasDiffrentProducts());
		assertEquals(1, store.getNumberOf());
		assertEquals(0, store.getNumberOf(ProductType.FOOD));
	}

	@Test(expected = IllegalArgumentException.class)
	public void productsTest2() {
		store.addProducts(2);
		store.removeProducts(4);
	}

	@Test(expected = IllegalArgumentException.class)
	public void productsTest3() {
		store.addProducts(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void productsTest4() {
		store.removeProducts(-1);
	}

}
