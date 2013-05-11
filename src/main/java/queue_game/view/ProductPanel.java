package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class ProductPanel extends JComponent{
	@Override
	public Dimension getMinimumSize(){
		return new Dimension(480, 30);
	}
	
	@Override
	public Dimension getPreferredSize(){
		if (getParent() == null)
			return new Dimension(640, 30);
		Dimension size = getParent().getSize();
		return new Dimension(size.width*30, 30);
	}
	@Override
	public Dimension getMaximumSize(){
		return new Dimension(700, 30);
	}
	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Dimension size = getSize();
		Rectangle rect = new Rectangle(0, 0, size.width - 1, size.height - 1);
		int widthOfField=size.width/5;
		g2d.setColor(Color.BLACK);
		g2d.draw(rect);
	}
	

}
