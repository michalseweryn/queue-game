package queue_game.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class which every object contains all the data about the particular player. 
 * 
 * @author krzysiek
 *
 */
public class Player {
	private int numberOfPawns;
	private int[] shoppingList = null;
	private int[] boughtProducts = {0, 0, 0, 0, 0};
	private ArrayList<QueuingCard> cardsOnHand=new ArrayList<QueuingCard>();
	private String name;
	private int ID;
	
	/**
	 * 
	 * @param index from 0 to number of players-1
	 * @param name
	 */
	public Player(int ID, String name) {
		this.name = name;
		this.ID = ID;
	}
	
	public String getName(){
		return name;
	}
	
	public int getID(){
		return ID;
	}
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
	
	public void setCardsOnHand(ArrayList<QueuingCard> cards){
		cardsOnHand=cards;
	}
	public ArrayList<QueuingCard> getCardsOnHand(){
		return cardsOnHand;
	}
	
	public void addPawn(){
		numberOfPawns++;
	}
	public void setNumberOfPawns(int numberOfPawns) {
		this.numberOfPawns = numberOfPawns;
	}
	
	public int getNumberOfPawns(){
		return numberOfPawns;
	}
	
	
	public void addProduct(ProductType type){
		boughtProducts[type.ordinal()]++;
	}
	
	public int[] getBoughtProducts() {
		return boughtProducts;
	}
	/**
	 * 
	 * @param ProductType
	 * @author Jan
	 */
	public void removeProduct(ProductType type){
		if(boughtProducts[type.ordinal()] > 0)
			boughtProducts[type.ordinal()]--;
		else
			throw new IllegalArgumentException("Player doesn't have such product.");
	}
	/**
	 * 
	 * @param Collection<ProductType>
	 * @author Jan
	 */
	public void removeProducts(Collection<ProductType> typeCollection){
		int[] products = {0,0,0,0,0};
		for(ProductType i: typeCollection)
			products[i.ordinal()]++;
		
		for(int i = 0; i < products.length; i++){
			if(products[i] > boughtProducts[i])
				throw new IllegalArgumentException("Player doesn't have such product.");
		}
		
		for(int i = 0; i < products.length; i++)
			boughtProducts[i] -= products[i];
	}
}
