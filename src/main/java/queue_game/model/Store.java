package queue_game.model;
/**
 * @author piotr
 * 
 * Store, which stores some kind of products, and has the queue,
 * where you have to wait to buy sth.
 */

public class Store {
	public final ProductType productType;
	public int queue[]=new int[10];
	public int queue_length=0;
	/**
	 * 
	 * @param productType
	 * 
	 * Sets the type of products stored in this shop
	 */
	public Store(ProductType productType){
		this.productType=productType;
	}
}
