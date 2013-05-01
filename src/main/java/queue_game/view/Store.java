package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public class Store extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7452536279840255740L;
	private Color color;
	
	@Override
	public Dimension getMinimumSize(){
		return new Dimension(80, 300);
	}
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(120, 420);
	}
	@Override
	public Dimension getMaximumSize(){
		return new Dimension(130, 640);
	}
	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
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
	public Store(Color color) {
		super();
		this.color = color;
	}
}
