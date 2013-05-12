package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

import queue_game.View;
import queue_game.controller.Game;
import queue_game.model.GameState;
import queue_game.model.Player;

public class JPlayerList extends JPanel implements View {
	private static final long serialVersionUID = 6386690129053320221L;
	private GameState gameState; 
	private ArrayList<JPlayerInfo> list = new ArrayList<JPlayerInfo>();
	public JPlayerList(Game game) {
		super();
		this.gameState = game.getGameState();
		game.addView(this);
		FlowLayout layout = new FlowLayout();
		layout.setVgap(0);
		layout.setHgap(0);
		setLayout(layout);
		
		
		addFields();
	}
	
	private void addFields()
	{
		ArrayList<Player> players= gameState.getPlayersList();
		for (Player p : players)
		{
			JPlayerInfo temp = new JPlayerInfo(p);
			add(temp);
			list.add(temp);
		}
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(480, 360);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, 360);
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
	
	public void update(){
		removeAll();
		addFields();
		revalidate();
	}

}
