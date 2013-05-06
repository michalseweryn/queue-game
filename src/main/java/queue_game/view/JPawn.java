package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;

import queue_game.controller.Game;
import queue_game.model.ProductType;

/**
 *  @author Helena
 *  
 */

public class JPawn extends JComponent implements MouseListener{
	private static final long serialVersionUID = 382939404028495740L;
	private Color color;
	private Game game;
	private ProductType product;
	private Integer place;
	private static Color[] pawnColors = new Color[]{
		Color.BLACK, 
		Color.RED, 
		Color.YELLOW, 
		new Color(0, 128, 0), 
		new Color(192, 128, 0), 
		Color.BLUE};
	@Override
	public Dimension getMinimumSize(){
		return new Dimension(2, 2);
	}
	@Override
	public Dimension getPreferredSize(){
		if (getParent() == null)
			return new Dimension(3, 3);
		Dimension size = getParent().getSize();
		return new Dimension(size.width / 3,  size.width / 3);
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
	public JPawn(ProductType product, int playerId, Game game, int place) {
		this.game = game;
		this.color = pawnColors[playerId + 1];
		this.product = product;
		this.place = place;
		addMouseListener(this);
		//repaint();
	}
	
	public void mouseClicked(MouseEvent e) {
		if(game != null && game != null && game.getGameState() != null){
			game.pawnSelected(game.getGameState().getActivePlayer(), product, place); // numer trzeba zidentyfikowac
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

