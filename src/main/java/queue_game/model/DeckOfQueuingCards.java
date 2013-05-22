/**
 * 
 */
package queue_game.model;

import java.util.Collection;

/**
 * @author michal
 *
 */
public interface DeckOfQueuingCards {
	public void addCards(Collection<QueuingCard> cardsOnHand);
	public void reset();
}
