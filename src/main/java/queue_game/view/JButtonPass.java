package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import queue_game.controller.Game;
import queue_game.model.GameState;
import queue_game.model.Player;
import queue_game.model.QueuingCard;


/**
 * @author Jan
 * 
 * 
 */
public class JButtonPass extends JPanel implements MouseListener{
	private static final long serialVersionUID = 6947197330462260996L;
	private Player player;
	private Game game;
	public JButtonPass(Player player, Game game) {
		this.player = player;
		this.game = game;
		addMouseListener(this);
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
		return new Dimension(size.width / 14, size.width / 14);
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
		
		g2d.setColor(Color.WHITE);		
		g2d.fillOval(0, 0, size.width, size.height);
		g2d.setColor(Color.BLACK);
		g2d.drawOval(0, 0, size.width, size.height);
		g.setFont(new Font("default", Font.CENTER_BASELINE, 14));
		g.drawString("PASS", size.width / 5, size.height /2);
		
	}
	public void mouseClicked(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseReleased(MouseEvent e) {}
}

