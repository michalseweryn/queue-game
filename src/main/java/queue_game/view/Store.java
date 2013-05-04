package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import queue_game.model.ProductType;

public class Store extends JComponent implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7452536279840255740L;
	private Color color;
	private Board board;
	private ProductType product;
	@Override
	public Dimension getMinimumSize(){
		return new Dimension(80, 300);
	}
	@Override
	public Dimension getPreferredSize(){
		if (getParent() == null)
			return new Dimension(120, 420);
		Dimension size = getParent().getSize();
		return new Dimension(size.width / 5, 3 * size.height / 4);
	}
	@Override
	public Dimension getMaximumSize(){
		return new Dimension(130, 640);
	}
	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setSize(getPreferredSize());
		Dimension size = getSize();
		g.setColor(color);
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
	 * @param product offered type of products
	 * @param color 
	 * @param board
	 */
	public Store(ProductType product, Color color, Board board) {
		super();
		this.product = product;
		this.board = board;
		this.color = color;
		addMouseListener(this);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		if(board != null && board.getGame() != null && board.getGameState() != null)
			board.getGame().productSelected(board.getGameState().getActivePlayerNo(), product);
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
