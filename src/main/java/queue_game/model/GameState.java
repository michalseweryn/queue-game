/**
 * 
 */
package queue_game.model;

/**
 * @author michal
 * A model part of MVC. Illustrates current situation on Board.
 */
public class GameState {
	private int activePlayer = 0;

	/**
	 * @return ID of player whose turn is now.  
	 */
	public int getActivePlayerNo() {
		return activePlayer;
	}

}
