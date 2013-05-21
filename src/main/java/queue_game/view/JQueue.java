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
import queue_game.model.ProductType;

/**
 * @author michal
 * 
 * Container of Pawns
 */
public class JQueue extends JComponent implements MouseListener{

	private static final long serialVersionUID = 6097243419792508941L;
	private ProductType product;
	private Game game;
	private boolean mouseFlag;
	
	public JQueue(Game game, ProductType product){
		this.game = game;
		this.product = product;
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
			game.queueSelected(game.getGameState().getActivePlayer(), product);
	}
	
}
