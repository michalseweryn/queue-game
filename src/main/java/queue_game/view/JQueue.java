/**
 * 
 */
package queue_game.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import queue_game.controller.Game;
import queue_game.model.ProductType;
import queue_game.model.Store;

/**
 * @author michal
 *
 */
public class JQueue extends JComponent implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6097243419792508941L;
	private ProductType product;
	private Game game;
	@Override
	public Dimension getPreferredSize(){
		if(getParent() == null)
			return null;
		Dimension size = getParent().getSize();
		int x = size.width / 5;
		int y = (3 * (2 * x / 3) > size.height)?(2 * x / 3):size.height - (2 * (2 * x / 3));
		return new Dimension(x, y);
		
	}
	public JQueue(Store store, Game game){
		this.game = game;
		this.product = store.productType;
		setLayout(new FlowLayout());
		int ind = 0;
		for(int p : store.getQueue()){
			add(new JPawn(store.productType, p, game, ind++));
		}
		addMouseListener(this);
	}
	@Override
	public void paintComponent(Graphics g){
		Dimension size = getSize();
		g.drawRect(0, 0, size.width - 1, size.height - 1);
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent arg0) {
		game.queueSelected(game.getGameState().getActivePlayer(), product);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
