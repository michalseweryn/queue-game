/**
 * 
 */
package queue_game.model;

/**
 * @author michal
 * A model part of MVC. Illustrates current situation on Board.
 */
public class GameState {
	private int numberOfPlayers;
	private int activePlayer = 0;
	private GamePhase currentGamePhase = null;

	public void setNumberOfPlayers(int nPlayers){
		numberOfPlayers = nPlayers;
	}
	
	public int getNumberOfPlayers(){
		return numberOfPlayers;
	}
	
	public GamePhase getCurrentGamePhase(){
		return currentGamePhase;
	}
	
	public void setCurrentGamePhase(GamePhase phase){
		currentGamePhase = phase;
	}

	/**
	 * @return ID of player whose turn is now.  
	 */
	public int getActivePlayer() {
		return activePlayer;
	}
	
	/**
	 * Sets ID of player whose turn is now.  
	 */
	public void setActivePlayer(int id) {
		activePlayer = id;
	}

}
