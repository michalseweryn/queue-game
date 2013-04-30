package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

/**
 * @author michal
 * 
 * User interface containing players panel and game board. 
 */
public class Board extends JComponent {

	/**
	 * As a JComponent must have UID.
	 */
	private static final long serialVersionUID = -2270325617374583365L;
	@Override
	public Dimension getMinimumSize(){
		return new Dimension(480, 360);
	}
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(640, 480);
	}
	@Override
	public Dimension getMaximumSize(){
		return new Dimension(700, 700);
	}
	@Override
    protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Dimension size = getSize();
		int r = 2;
		Rectangle rect = new Rectangle(0, 0, size.width - 1, size.height - 1);
		g2d.setColor(Color.BLACK);
		g2d.draw(rect);
		g2d.drawString("Plansza", r, r + g2d.getFont().getSize());
	}

}
