package queue_game.model;

import java.awt.Color;
import java.util.HashMap;

/**
 *  @author Helena
 *  
 */
public class PlayerColor {

	/**
	 * @param args
	 */
	HashMap<Integer,Color> playerscolor = new HashMap<Integer, Color>();
	{
		playerscolor.put(0, Color.RED);
		playerscolor.put(1, Color.YELLOW);
	}
	public Color colorOfPlayer(int playerNo){
		return playerscolor.get(playerNo);
	}
}