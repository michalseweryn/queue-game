package queue_game.nview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import queue_game.controller.Game;
import queue_game.model.GameState;
import queue_game.model.Player;

public class JPlayerList extends JPanel implements queue_game.view.View {
	private static final long serialVersionUID = 6386690129053320221L;
	private GameState gameState; 
	private ArrayList<JPlayerInfo> list = new ArrayList<JPlayerInfo>();
	public JPlayerList(Game game) {
		super();
		this.gameState = game.getGameState();
		FlowLayout layout = new FlowLayout();
		setLayout(layout);
		//setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
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
	public Dimension getPreferredSize() {
		Dimension size = getParent().getSize();
		return new Dimension(200, size.height);
	}
	
	public void update(){
		removeAll();
		addFields();
		revalidate();
	}

}
