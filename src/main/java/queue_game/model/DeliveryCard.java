package queue_game.model;

public class DeliveryCard {
	private ProductType productType;
	private int amount;
	
	public DeliveryCard(ProductType productType, int amount) {
		this.productType = productType;
		this.amount = amount;
	}
	
	public ProductType getProductType(){
		return productType;
	}
	
	public int getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return productType.toString() + ' ' + Integer.toString(amount);
	}
}
