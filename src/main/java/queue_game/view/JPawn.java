package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import queue_game.model.ProductType;

/**
 *  @author Helena
 *  
 */

public class JPawn extends JComponent implements MouseListener{
	private static final long serialVersionUID = 382939404028495740L;
	private Color color;
	private JBoard board;
	private ProductType product;
	private Integer place;
	@Override
	public Dimension getMinimumSize(){
		return new Dimension(2, 2);
	}
	@Override
	public Dimension getPreferredSize(){
		if (getParent() == null)
			return new Dimension(3, 3);
		Dimension size = getParent().getSize();
		return new Dimension(size.width / 12,  size.width / 12);
	}
	@Override
	public Dimension getMaximumSize(){
		return new Dimension(100, 100);
	}
	@Override
    protected void paintComponent(Graphics g) {
		g.setColor(color);
		Dimension size = getSize();
		g.fillOval(0, 0, size.width, size.height);
	}
	/**
	 * @param args
	 */
	public JPawn(ProductType product, Color color, JBoard board, int place) {
		this.board = board;
		this.color = color;
		this.product = product;
		this.place = place;
		addMouseListener(this);
		//repaint();
	}
	
	public void mouseClicked(MouseEvent e) {
		if(board != null && board.getGame() != null && board.getGameState() != null){
			board.getGame().pawnSelected(board.getGameState().getActivePlayer(), product, 0); // numer trzeba zidentyfikowac
		}
			
		
	}
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}

