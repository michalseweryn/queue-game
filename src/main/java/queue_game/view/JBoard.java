package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import queue_game.controller.Game;
import queue_game.model.GameState;
import queue_game.model.Store;

/**
 * @author michal
 * 
 *         User interface containing game board.
 */
public class JBoard extends JPanel {
	private static final long serialVersionUID = -2270325617374583365L;
	public static final Color[] defaultColorSet = new Color[] { Color.BLUE,
			Color.GREEN, Color.PINK, Color.ORANGE, Color.MAGENTA };
	private Game game = null;
	private GameState gameState = null;
	
	public JBoard(Game game) {
		super();
		this.game = game;
		this.gameState = game.getGameState();
		game.addView(this);
		FlowLayout layout = new FlowLayout();
		layout.setVgap(0);
		layout.setHgap(0);
		setLayout(layout);
		for (Store store : gameState.getStores()){
			add(new JStore(store, this));
		}
		for (Store store : gameState.getStores()){
			add(new JQueue(store, game));
		}
	}

	/**
	 * Sets currently played game. All input will be translated to game events
	 * and passed to this game.
	 * 
	 * @param game
	 */
	public void update(){
		removeAll();
		for (queue_game.model.Store store : gameState.getStores())
			add(new JStore(store, this));
		for (Store store : gameState.getStores()){
			add(new JQueue(store, game));
		}
		revalidate();
		if(gameState.isGameOver()){
			JOptionPane.showMessageDialog(this, "KONIEC GRY");
		}
			
	}
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(480, 360);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(640, 480);
	}

	@Override
	public Dimension getMaximumSize() {
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
	 * 
	 * @return
	 */
	Game getGame() {
		return game;
	}

	GameState getGameState() {
		return gameState;
	}

}
