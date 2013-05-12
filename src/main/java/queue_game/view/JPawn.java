package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import queue_game.controller.Game;
import queue_game.model.GameState;
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
	private int length;
	private final static Color[] pawnColors = GameState.playerColors;
	
	public JPawn(ProductType product, int playerId, Game game, int place, int length) {
		this.game = game;
		this.color = pawnColors[playerId + 1];
		this.product = product;
		this.place = place;
		this.length = length;
		addMouseListener(this);
	}
	
	@Override
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	
	@Override
	public Dimension getPreferredSize(){
		if (getParent() == null)
			return new Dimension(3, 3);
		Dimension size = getParent().getSize();
		int diameter = size.width / 3;
		if(diameter * length > size.getHeight())
			diameter = (int) (size.getHeight() / length);
		return new Dimension(diameter,  diameter);
	}
	
	@Override
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	
	@Override
    protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				  RenderingHints.VALUE_ANTIALIAS_ON);
		Dimension size = getSize();
		int c = size.height / 15;
		if(c == 0)
			c = 1;
		g2d.setColor(Color.BLACK);
		g2d.fillOval(0, 0, size.width, size.height);
		g2d.setColor(color);
		g2d.fillOval(c, c, size.width - 2 * c, size.height - 2 * c);
	}
	
	public void mouseClicked(MouseEvent e) {
		if(game != null && game != null && game.getGameState() != null){
			game.pawnSelected(game.getGameState().getActivePlayer(), product, place);
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseReleased(MouseEvent e) {}

}

