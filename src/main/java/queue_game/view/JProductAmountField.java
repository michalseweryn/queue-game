package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import queue_game.model.ProductType;
/**
 * A class which generates a square on each store (by now all of them cyan, cause 
 * stores' colors are not chosen yet), which contains the amount of this-store-products
 * available. Invisible if there is no products.
 * 
 * @author KK
 */

public class JProductAmountField extends JComponent implements MouseListener{
	
	private static final long serialVersionUID = 7459675797433335680L;
	private Color color;
	private JStore store;
	private ProductType product;
	@Override
	public Dimension getMinimumSize(){
		return new Dimension(1, 1);
	}
	
	///////////////////////////////////////////////
	
	@Override
	public Dimension getPreferredSize(){
		if (getParent() == null)
			return new Dimension(120, 420);
		System.out.println(getParent());
		Dimension size = getParent().getSize();
		return new Dimension(size.width, size.height);
	}
	@Override
	public Dimension getMaximumSize(){
		return new Dimension(130, 640);
	}
	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension size = getSize();
		g.setColor(Color.cyan);
		g.fillRect(size.width/4, size.height /6, 2*size.width/4, 4*size.height/ 6);
		g.setColor(Color.BLACK);
		g.setFont(g.getFont().deriveFont(50f));
		g.drawString(Integer.toString(store.getStore().getNumberOf()), 50, 60);
	}
	/**
	 * 
	 * @param color 
	 * @param board
	 */
	public JProductAmountField (Color color, JStore store) {
		super();
		this.color = color;
		this.store = store;
		addMouseListener(this);
		if(store.getStore().getNumberOf()>0)
			this.setVisible(true);
		else this.setVisible(false);
	}	

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		store.mouseClicked(e);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {}
}
	
