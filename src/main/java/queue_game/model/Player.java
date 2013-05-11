package queue_game.model;
/**
 * A class which every object contains all the data about the particular player. 
 * 
 * @author krzysiek
 *
 */
public class Player {
	private int numberOfPawns;
	private DeckOfCards deck=null;
	private int[] shoppingList = null;
	
	
	
	
	/**
	 * 
	 * @param shoppingList an array with the number of needed products from
	 * all stores. The array inside the class will be a clone of this array.
	 * 
	 * @throws IllegalArgumentException if argument's length differs
	 * ProductType.values().length
	 */
	public void setShoppingList(int[] shoppingList){
		if(shoppingList.length != ProductType.values().length)
			throw new IllegalArgumentException();
		this.shoppingList = shoppingList.clone();
	}
	
	public int[] getShoppingList(){
		return shoppingList;
	}
	

	public void setDeck(DeckOfCards deck) {
		this.deck = deck;
	}
	
	public DeckOfCards getDeck() {
		return deck;
	}
	
	
	public void setNumberOfPawns(int numberOfPawns) {
		this.numberOfPawns = numberOfPawns;
	}
	
	public int getNumberOfPawns(){
		return numberOfPawns;
	}
	
	
}