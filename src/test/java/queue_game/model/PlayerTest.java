package queue_game.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
	private Player player;

	@Before
	public void setUp() throws Exception {
		player = new Player(0, "Test player");
	}

	@Test
	public void setShoppingListTest() {
		List<Integer> shoppingList = Arrays.asList(0, 1, 2, 3, 4);
		player.setShoppingList(shoppingList);
		assertEquals(shoppingList, player.getShoppingList());
		shoppingList = Arrays.asList(1, 2, 3, 4, 0);
		player.setShoppingList(shoppingList);
		assertEquals(shoppingList, player.getShoppingList());
		shoppingList = Arrays.asList(2, 3, 4, 0, 1);
		player.setShoppingList(shoppingList);
		assertEquals(shoppingList, player.getShoppingList());
		shoppingList = Arrays.asList(3, 4, 0, 1, 2);
		player.setShoppingList(shoppingList);
		assertEquals(shoppingList, player.getShoppingList());
		shoppingList = Arrays.asList(4, 0, 1, 2, 3);
		player.setShoppingList(shoppingList);
		assertEquals(shoppingList, player.getShoppingList());
	}

	@Test(expected = IllegalArgumentException.class)
	public void setShoppingListTest3() {
		player.setShoppingList(Arrays.asList(0, 1, 2, 3));
	}

	@Test(expected = IllegalArgumentException.class)
	public void setShoppingListTest6() {
		player.setShoppingList(Arrays.asList(-1, 0, 1, 2, 3));
	}
}
