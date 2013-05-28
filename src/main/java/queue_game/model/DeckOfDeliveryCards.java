/**
 * 
 */
package queue_game.model;

import java.util.Collection;


/**
 * @author michal
 *
 */
public interface DeckOfDeliveryCards {
	Collection<DeliveryCard> removeThreeCards();
	Collection<DeliveryCard> peekTwoCards();
	void fill();
	
}
