package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import queue_game.controller.Game;
import queue_game.model.GameState;
import queue_game.model.ProductType;

/**
 * @author michal
 * 
 * User interface containing game board. 
 */
public class Board extends JComponent {
	private static final long serialVersionUID = -2270325617374583365L;
	private Game game = null;
	private GameState gameState = null;
	public Board(){
		super();
		FlowLayout layout = new FlowLayout();
		layout.setVgap(0);
		layout.setHgap(0);
		setLayout(layout);
		add(new Store(ProductType.KIOSK, Color.BLUE, this));
		add(new Store(ProductType.RTV_AGD, Color.GREEN, this));
		add(new Store(ProductType.CLOTHES, Color.PINK, this));
		add(new Store(ProductType.FOOD, Color.ORANGE, this));
		add(new Store(ProductType.FURNITURE, Color.MAGENTA, this));
	}
	/**
	 * Sets currently played game. All input will be translated to game events and passed to this game. 
	 * @param game
	 */
	public void setGame(Game game){
		this.game = game;
		this.gameState = game.getGameState();
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
		Dimension size = getSize();
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
