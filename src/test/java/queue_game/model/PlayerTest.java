package queue_game.model;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
	
	boolean arraysEquals(int[] a, int []b) {
		return Arrays.toString(a) == Arrays.toString(b);
	}
	
	
	
	
	private Player player;

	@Before
	public void setUp() throws Exception {
		player = new Player(0, "Test player");
	}
	
	@Test
	public void setShoppingListTest() {
		int[] shoppingList = new int[] {0, 1, 2, 3, 4};
		player.setShoppingList(shoppingList);
		assert arraysEquals(shoppingList, 
				player.getShoppingList());
		shoppingList = new int[] {1, 2, 3, 4, 0};
		player.setShoppingList(shoppingList);
		assert arraysEquals(shoppingList, 
				player.getShoppingList());
		shoppingList = new int[] {2, 3, 4, 0, 1};
		player.setShoppingList(shoppingList);
		assert arraysEquals(shoppingList, 
				player.getShoppingList());
		shoppingList = new int[] {3, 4, 0, 1, 2};
		player.setShoppingList(shoppingList);
		assert arraysEquals(shoppingList, 
				player.getShoppingList());
		shoppingList = new int[] {4, 0, 1, 2, 3};
		player.setShoppingList(shoppingList);
		assert arraysEquals(shoppingList, 
				player.getShoppingList());
	}

	@Test(expected = IllegalArgumentException.class)
	public void setShoppingListTest2() {
		player.setShoppingList(new int[] {0, 1, 3, 2, 4});
	}

	@Test(expected = IllegalArgumentException.class)
	public void setShoppingListTest3() {
		player.setShoppingList(new int[] {0, 1, 2, 3});
	}

	@Test(expected = IllegalArgumentException.class)
	public void setShoppingListTest4() {
		player.setShoppingList(new int[] {0, 1, 2, 3, 5});
	}

	@Test(expected = IllegalArgumentException.class)
	public void setShoppingListTest5() {
		player.setShoppingList(new int[] {1, 2, 3, 4, 5});
	}

	@Test(expected = IllegalArgumentException.class)
	public void setShoppingListTest6() {
		player.setShoppingList(new int[] {-1, 0, 1, 2, 3});
	}
}
