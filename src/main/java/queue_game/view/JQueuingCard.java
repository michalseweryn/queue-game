package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

import queue_game.model.DeckOfCards;
import queue_game.model.GameState;
import queue_game.model.Player;
/**
 * @author Jan
 * 
 *         
 */
public class JQueuingCard extends JPanel{
	private static final long serialVersionUID = 6947197330462260996L;
	private DeckOfCards card;
	private Player player;
	public JQueuingCard(Player player) {
		this.player = player;
	}
	
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getPreferredSize() {
		if (getParent() == null)
			return null;
		Dimension size = getParent().getSize();
		return new Dimension(size.width/6, size.height);
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Dimension size = getSize();
		Rectangle rect = new Rectangle(0, 0, size.width - 1, size.height - 1);
		int indexInColorArray = player.getID()+1;
		if(indexInColorArray >= GameState.playerColors.length) throw new ArrayIndexOutOfBoundsException("players' list is " +
					"longer than colors' number");
		g2d.setColor(GameState.playerColors[indexInColorArray]);
		g2d.fillRect(0, 0, size.width, size.height);
		g2d.setColor(Color.BLACK);
		g2d.draw(rect);
		DeckOfCards card = player.getDeck();
		if(card!=null){
		g.setFont(g.getFont().deriveFont(40f));		
		g.drawString(card.toString(), size.width/3, size.height/2);
		}
	}
}
