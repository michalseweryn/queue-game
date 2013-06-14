package queue_game.model;
/**
 * @author Piotr
 * Enum describing queuing cards
 */
public enum QueuingCard {
	DELIVERY_ERROR("Pomyłka \nw dostawie"), NOT_YOUR_PLACE("Pan tu\nnie stał"), 
	LUCKY_STRIKE("Szczęśliwy \ntraf"), MOTHER_WITH_CHILD("Matka z\ndzieckiem"), 
	UNDER_THE_COUNTER_GOODS("Towar spod\nlady"), //TIPPING_FRIEND("Kolega z\nkomitetu"),
	CRITICIZING_AUTHORITIES("Krytyka\nwładzy"), INCREASED_DELIVERY("Zwiększona\ndostawa"),
	CLOSED_FOR_STOCKTAKING("Remanent"), COMMUNITY_LIST("Lista \nspołeczna");
	
	public final String toStringPL; 	
	QueuingCard(String toStringPL){
		this.toStringPL = toStringPL;
	}
	//Pomylka w dostawie, Pan tu nie stal, Szczesliwy traf, Matka z dzieckiem na reku,
	//Towar spod lady, Kolega z komitetu, Krytyka wladzy,
	//Zwiekszona dostawa, Remanent, Lista spoleczna
}
