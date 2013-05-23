/**
 * 
 */
package queue_game;

import queue_game.model.GameAction;

/**
 * @author michal
 *
 */
public interface ActionGiver {
	GameAction getAction() throws InterruptedException;
}
