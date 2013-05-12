package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import queue_game.controller.Game;
import queue_game.model.DeckOfCards;

/**
 * @author Jan
 * 
 *         Panel for queuing cards.
 */
public class JCardsArea extends JComponent{
	private static final long serialVersionUID = -3804758455470286800L;
	public Color color;
	public DeckOfCards[] cards = new DeckOfCards[3];
	public JCardsArea(Game game,JBoard board){
		
		cards[0] = game.getGameState().getDeck(game.getGameState().getActivePlayer());
		cards[1] = game.getGameState().getDeck(game.getGameState().getActivePlayer());
		cards[2] = game.getGameState().getDeck(game.getGameState().getActivePlayer());		
		
	}
	@Override
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	
	@Override
	public Dimension getPreferredSize(){
		if (getParent() == null)
			return null;
		Dimension size = getParent().getSize();
		return new Dimension(size.width, size.height );
	}
	@Override
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Dimension size = getSize();
		Rectangle rect = new Rectangle(0,0 ,size.width ,size.height);		
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, size.width, size.height);
		g2d.setColor(Color.BLACK);
		g2d.draw(rect);
		g.setColor(Color.RED);
		g.fillRect(0, 0, size.width/4, size.height);
		g.setColor(Color.BLUE);
		g.fillRect(size.width/4, 0, size.width/4, size.height);
		g.setColor(Color.GREEN);
		g.fillRect(size.width/2, 0, size.width/4, size.height);
	}
	

}