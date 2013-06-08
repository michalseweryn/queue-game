/**
 * 
 */
package queue_game.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import queue_game.controller.Game;
import queue_game.creator.LocalGameActionCreator;
import queue_game.model.GameState;
import queue_game.model.ProductType;

/**
 * @author michal
 *
 */
public class JProductSquare extends JComponent implements MouseListener{
	private static final long serialVersionUID = 1L;
	private Game game;
	private ProductType product;
	private ProductType store;
	private int amount;
	private LocalGameActionCreator creator;
	private static final double STROKE = 0.1;
	public JProductSquare(Game game, ProductType product, int amount, ProductType store, LocalGameActionCreator creator){
		this.game = game;
		this.product = product;
		this.amount = amount;
		this.store = store;
		this.creator = creator;
		addMouseListener(this);
	}
	public boolean setAmount(int a){
		if(this.amount == a){
			
			return false;
		}
		this.amount = a;
		return true;
	}
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2d= (Graphics2D) g;
		//if (JGameArea.ANTYALIASING)
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		double side = getSize().height;
		g2d.setColor(GameState.productColors[product.ordinal()]);
		g2d.fillRect(0, 0, getSize().width, getSize().height);
		g2d.setColor(Color.BLACK);
		Font font = new Font("Arial Black", Font.BOLD, (int) (0.75 * side));
		g2d.setFont(font);
		g2d.drawString("" + amount, (int)(side / 5), (int)(getSize().height - side / 5));
		g2d.setStroke(new BasicStroke((float) (STROKE * getSize().height)));
		g2d.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
	}

	public boolean setType(ProductType productType) {
		if(this.product == productType)
			return false;
		this.product = productType;
		return true;
		
	}
	public void mouseClicked(MouseEvent arg0) {
		creator.productSelected(game.getGameState().getActivePlayer(), product, store);
		
	}
	
	public void mouseEntered(MouseEvent arg0) {
	
	}
	
	public void mouseExited(MouseEvent arg0) {
	
	}
	
	public void mousePressed(MouseEvent arg0) {
	
	}
	
	public void mouseReleased(MouseEvent arg0) {
	
	}
}
