package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import queue_game.controller.Game;
import queue_game.model.GameState;
import queue_game.model.ProductType;

/**
 * @author michal
 * 
 * User interface containing game board. 
 */
public class Board extends JPanel {
	private static final long serialVersionUID = -2270325617374583365L;
	private Game game = null;
	private GameState gameState = null;
	public Board(){
		super();
		// No layout allows us to set position of all components manually
		setLayout(null);
		Store s = new Store(ProductType.KIOSK, Color.BLUE, this);
		setPosition(s, 30, 30);
		add(s);
	}
	/**
	 * Sets currently played game. All input will be translated to game events and passed to this game. 
	 * @param game
	 */
	public void setGame(Game game){
		this.game = game;
		this.gameState = game.getGameState();
	}
	/**
	 * Sets Position of component on board. 
	 */
    void setPosition(JComponent component, int x, int y){
        Insets insets = getInsets();
        Dimension size = component.getPreferredSize();
        component.setBounds(insets.left + x, insets.top + y, size.width, size.height);
    }
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
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Dimension size = getParent().getSize();
		Rectangle rect = new Rectangle(0, 0, size.width - 1, size.height - 1);
		g2d.setColor(Color.BLACK);
		g2d.draw(rect);
	}
	/**
	 * Object for subcomponents to pass events 
	 * @return
	 */
	Game getGame() {
		return game;
	}
	GameState getGameState() {
		return gameState;
	}

}
