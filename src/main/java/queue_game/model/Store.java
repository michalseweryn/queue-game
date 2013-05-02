package queue_game.model;
/**
 * @author piotr
 * 
 * Store, which stores some kind of products, and has the queue,
 * where you have to wait to buy sth.
 */
import java.util.LinkedList;

public class Store {
	public final ProductType productType;
	public LinkedList<Integer> queue=new LinkedList<Integer>();
	private int numberOf[]=new int[5];
			
	/**
	 * 
	 * @param productType
	 * 
	 * Constructor, that sets the type of products stored in this shop
	 */
	public Store(ProductType productType){
		this.productType=productType;
	}
	/**
	 * 
	 * @param numberOfAddedProducts
	 * Adds so many products to the magazine, as you ask. This one adds only
	 * products that are typically stored in this shop
	 */
	public void addProducts(int numberOfAddedProducts){
		numberOf[productType.ordinal()]+=numberOfAddedProducts;
	}
	/**
	 * 
	 * @param type
	 * Adds one product of non-typical type to the shop
	 */
	public void addProduct(ProductType type){
		numberOf[type.ordinal()]+=1;
	}
	/**
	 * 
	 * @return
	 * returns number of typically stored products
	 */
	public int getNumberOf(){
		return numberOf[productType.ordinal()];
	}
	/**
	 * 
	 * @param type
	 * @return
	 * returns the number of products that are not typical for the store
	 */
	public int getNumberOf(ProductType type){
		return numberOf[type.ordinal()];
	}
	/**
	 * 
	 * @return
	 * returns true if and only if the shop stores products that are not 
	 * typical for it
	 */
	public boolean hasDiffrentProducts(){
		boolean hasIt=false;
		for(int i=0; i<5; i++){
			if(numberOf[i]!=0 && i!=productType.ordinal()){
				hasIt=true;
			}	
		}
		return hasIt;
	}
	/**
	 * 
	 * @param numberOfRemovedProducts
	 * removes declared number of typically stored products
	 */
	public void removeProducts(int numberOfRemovedProducts){
		numberOf[productType.ordinal()]-=numberOfRemovedProducts;
		if(numberOf[productType.ordinal()]<0)
			throw new RuntimeException("Not enough products");
	}
	/**
	 * 
	 * @param NumberOfRemovedProducts
	 * @param type
	 * removes declared number of products of declared type.
	 */
	public void removeProducts(int numberOfRemovedProducts, ProductType type){
		numberOf[type.ordinal()]-=numberOfRemovedProducts;
		if(numberOf[type.ordinal()]<0)
			throw new RuntimeException("Not enough products");
	}
}
