/**
 * 
 */
package queue_game.view;

import java.awt.Dimension;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import queue_game.controller.Game;
import queue_game.creator.LocalGameActionCreator;
import queue_game.model.GameState;
import queue_game.model.ProductType;

/**
 * @author michal
 * 
 * Container of Pawns
 */
public class JQueue extends JComponent implements MouseListener{

	private static final long serialVersionUID = 6097243419792508941L;
	private ProductType product;
	private boolean mouseFlag;
	private Game game;
	private LocalGameActionCreator localGameInputAdapter;
	
	public JQueue(ProductType product, LocalGameActionCreator localGameActionCreator){
		this.product = product;
		this.localGameInputAdapter = localGameActionCreator;
		addMouseListener(this);
	}
	
	public Shape getShape(){
		return getBounds();
	}
	
	public void mouseClicked(MouseEvent arg0) {}

	public void mouseEntered(MouseEvent arg0) {}
	
	public void mouseExited(MouseEvent arg0) {mouseFlag = false;}
	
	public void mousePressed(MouseEvent arg0) {mouseFlag = true;}
	
	public void mouseReleased(MouseEvent arg0) {
		if(mouseFlag)
			localGameInputAdapter.queueSelected(game.getGameState().getActivePlayer(), product);
	}

	/**
	 * @param game
	 */
	public void setGame(Game game) {
		this.game = game;
	}
	
}
