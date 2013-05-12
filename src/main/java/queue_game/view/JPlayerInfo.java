package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

import queue_game.model.GameState;
import queue_game.model.Player;

public class JPlayerInfo extends JPanel {
	private static final long serialVersionUID = 557224757921396872L;
	private JPlayerList playerList;
	private Player player;
	private Graphics graphics;
	public JPlayerInfo(JPlayerList playerList, Player player) {
		this.playerList = playerList;
		this.player = player;
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(480, 360);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(playerList.size().width, playerList.size().height/5);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(700, 700);
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Dimension size = getSize();
		Rectangle rect = new Rectangle(0, 0, size.width - 1, size.height - 1);
		g2d.draw(rect);
		graphics = g;
		paintingComponentActualFunction(g);
	}
	
	protected void paintingComponentActualFunction(Graphics g) {
		int indexInColorArray = player.getID()+1;
		if(indexInColorArray >= GameState.playerColors.length) throw new ArrayIndexOutOfBoundsException("players' list is " +
					"longer than colors' number");
		g.setColor(GameState.playerColors[indexInColorArray]);
		g.setFont(g.getFont().deriveFont(20f));
		g.drawString(player.getName(), 60, 27);
		int[] shList = player.getShoppingList();
		int[] bProd = player.getBoughtProducts();
		int i=0;
		for (Color c : GameState.productColors){
			g.setColor(c);
			g.drawString(bProd[i] + "/" + shList[i], 5 + 40*i, 52);
			i++;
		}
		g.setColor(GameState.playerColors[indexInColorArray]);
		g.drawString("Dostępne pionki: " + player.getNumberOfPawns(), 5, 77);
	}


}
