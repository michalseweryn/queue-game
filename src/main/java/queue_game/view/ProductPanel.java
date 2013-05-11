package queue_game.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import queue_game.controller.Game;

public class ProductPanel extends JComponent{
	private Integer numberOfProducts[]= new Integer[5];
	public Color[] colors;
	public ProductPanel(Game game,JBoard board){
		numberOfProducts = game.getGameState().getNumberOfProducts();
		colors=board.defaultColorSet;
	}
	@Override
	public Dimension getMinimumSize(){
		return new Dimension(480, 30);
	}
	
	@Override
	public Dimension getPreferredSize(){
		if (getParent() == null)
			return null;
		Dimension size = getParent().getSize();
		return new Dimension(size.width, 30);
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
		g2d.setColor(colors[0]);
		g2d.fillRect(0, 0, widthOfField, size.height-1);
		g2d.setColor(colors[1]);
		g2d.fillRect(widthOfField, 0, widthOfField, size.height-1);
		g2d.setColor(colors[2]);
		g2d.fillRect(widthOfField*2, 0, widthOfField, size.height-1);
		g2d.setColor(colors[3]);
		g2d.fillRect(widthOfField*3, 0, widthOfField, size.height-1);
		g2d.setColor(colors[4]);
		g2d.fillRect(widthOfField*4, 0, widthOfField, size.height-1);
		g2d.setColor(Color.BLACK);
		g2d.draw(rect);
		g.setFont(new Font("default", Font.BOLD, 14));
		g2d.drawString(numberOfProducts[0].toString(), widthOfField/2-5, 20);
		g2d.drawString(numberOfProducts[1].toString(), (widthOfField/2)*3-5, 20);
		g2d.drawString(numberOfProducts[2].toString(), (widthOfField/2)*5-5, 20);
		g2d.drawString(numberOfProducts[3].toString(), (widthOfField/2)*7-5, 20);
		g2d.drawString(numberOfProducts[4].toString(), (widthOfField/2)*9-5, 20);
	}
	

}