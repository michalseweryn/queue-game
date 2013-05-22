/**
 * 
 */
package queue_game;

import java.util.Collection;

import queue_game.model.DeliveryCard;

/**
 * @author michal
 *
 */
public interface DeckOfDeliveryCards {
	Collection<DeliveryCard> removeThreeCards();
	Collection<DeliveryCard> peekTwoCards();
	
}
