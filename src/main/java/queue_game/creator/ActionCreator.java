/**
 * 
 */
package queue_game.creator;

import queue_game.model.GameAction;

/**
 * @author michal
 *
 */
public interface ActionCreator {
	GameAction getAction() throws InterruptedException;
}
