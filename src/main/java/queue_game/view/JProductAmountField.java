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
 * available.
 * By default it is invisible and is to be turned on by setVisible(true) when some products appear.
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
			return new Dimension(20, 40);
		Dimension size = getParent().getSize();
		return new Dimension(size.width / 5,  3 * size.height / 4);
	}
	@Override
	public Dimension getMaximumSize(){
		return new Dimension(130, 640);
	}
	@Override
    protected void paintComponent(Graphics g) {
		System.out.println("c");

		super.paintComponent(g);
		System.out.println(getParent());
		Dimension size = getSize();
		g.setColor(Color.cyan);
		g.fillRect(0, 0, size.width - 1, size.height / 5);
		int initialHeight = size.height / 5 + 2;
		int remainingHeight = size.height - initialHeight;
		//When there are more than 10 pawns, you should resize the rectangles
		for(int i = 0; i < 10; ++i) {
			g.fillRect(size.width / 3, initialHeight + i * (remainingHeight / 10 + 1), size.width / 3, remainingHeight / 10 - 2);
		}
	}
	/**
	 * 
	 * @param color 
	 * @param board
	 */
	public JProductAmountField (Color color, JStore store) {
		super();
		System.out.println("aaaaaaaaaaaaaa");this.store = store;
		this.color = color;
		System.out.println(getParent());
		addMouseListener(this);
		this.setVisible(true);
	}	

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
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
	
