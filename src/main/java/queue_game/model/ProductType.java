package queue_game.model;

/**
 * @author piotr
 * 
 * Enum describing the type of products stored in the shop and the type 
 * of DeliveryCards
 */

public enum ProductType {
	RTV_AGD("RTV-AGD"), FOOD("SPOŻYWCZY"), CLOTHES("ODZIEŻ"), KIOSK("KIOSK"), FURNITURE("MEBLE");
	public final String namePL; 
	ProductType(String namePL){
		this.namePL = namePL;
	}
}
