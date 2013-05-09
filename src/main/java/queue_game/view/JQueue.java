/**
 * 
 */
package queue_game.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

import queue_game.controller.Game;
import queue_game.model.ProductType;
import queue_game.model.Store;

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
	
	public JQueue(Store store, Game game){
		this.game = game;
		mouseFlag = false;
		this.product = store.productType;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		int ind = 0;
		int length = store.getQueue().size();
		for(int p : store.getQueue()){
			add(new JPawn(store.productType, p, game, ind++, length));
		}
		addMouseListener(this);
	}
	
	@Override
	public Dimension getPreferredSize(){
		if(getParent() == null)
			return null;
		Dimension size = getParent().getSize();
		int x = size.width / 5;
		int y = (3 * (2 * x / 3) > size.height)?(2 * x / 3):size.height - (2 * (2 * x / 3));
		return new Dimension(x, y);
	}
	
	@Override
	public void paintComponent(Graphics g){
		Dimension size = getSize();
		g.drawRect(0, 0, size.width - 1, size.height - 1);
	}
	
	public void mouseClicked(MouseEvent arg0) {}

	public void mouseEntered(MouseEvent arg0) {}
	
	public void mouseExited(MouseEvent arg0) {mouseFlag = false;System.out.println("exit");}
	
	public void mousePressed(MouseEvent arg0) {mouseFlag = true;System.out.println("press");}
	
	public void mouseReleased(MouseEvent arg0) {
		if(mouseFlag)
			game.queueSelected(game.getGameState().getActivePlayer(), product);
	}
	
}
