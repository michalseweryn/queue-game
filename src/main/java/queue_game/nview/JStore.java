package queue_game.nview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import queue_game.controller.Game;
import queue_game.model.GameState;
import queue_game.model.ProductType;

public class JStore extends JComponent implements MouseListener{
	private static final long serialVersionUID = 7452536279840255740L;
	private ProductType product;
	private Game game;
	private double STROKE = 0.02;
	boolean mouseFlag = false;
    private void fillAndDraw(Graphics2D g, Shape shape) {
    	g.fill(shape);
    	Color c = g.getColor();
    	g.setColor(Color.BLACK);
    	g.draw(shape);
    	g.setColor(c);
    	addMouseListener(this);
    }
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d= (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		double d = getSize().getHeight() * STROKE;
		double height = getSize().height * (1 - 2 * STROKE);
		double width = getSize().width * (1 - 2 * STROKE);
		g2d.setStroke(new BasicStroke((float) d));
		
		Rectangle2D.Double wall = new Rectangle2D.Double(d, d + height / 4, width, 3 * height / 4);
		Rectangle2D.Double roof = new Rectangle2D.Double(d, d, width, height / 4);
		game.getGameState();
		g2d.setColor(GameState.productColors[product.ordinal()]);
		fillAndDraw(g2d, wall);
		g2d.setColor(Color.gray);
		fillAndDraw(g2d, roof);
		/*int initialHeight = size.height / 5 + 2;
		int remainingHeight = size.height - initialHeight;
		//When there are more than 10 pawns, you should resize the rectangles
		for(int i = 0; i < 10; ++i) {
			g.fillRect(size.width / 3, initialHeight + i * (remainingHeight / 10 + 1), size.width / 3, remainingHeight / 10 - 2);
		}*/
	}
	
	public JStore(Game game, ProductType product){
		this.game = game;
		this.product = product;
		addMouseListener(this);
	}
	
	public void mouseClicked(MouseEvent arg0) {}

	public void mouseEntered(MouseEvent arg0) {}
	
	public void mouseExited(MouseEvent arg0) {mouseFlag = false;}
	
	public void mousePressed(MouseEvent arg0) {mouseFlag = true;}
	
	public void mouseReleased(MouseEvent arg0) {
		
		if(mouseFlag)
			game.queueSelected(game.getGameState().getActivePlayer(), product);
		mouseFlag = false;
	}
	
}
