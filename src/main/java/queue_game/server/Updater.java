/**
 * 
 */
package queue_game.server;

import queue_game.model.GameAction;

/**
 * @author michal
 *
 */
public interface Updater {
	void update(GameAction action);
}
