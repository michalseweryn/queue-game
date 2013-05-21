package queue_game.nview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import queue_game.model.GameState;
import queue_game.model.Player;

public class JPlayerInfo extends JPanel {
	private static final long serialVersionUID = 557224757921396872L;
	private Player player;

	public JPlayerInfo(Player player) {
		this.player = player;
		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel panel1 = new JPanel();
		panel1.setOpaque(false);
		//panel1.setLayout(new BoxLayout(panel1, BoxLayout.LINE_AXIS));
		JLabel colorLabel = createColoredLabel(GameState.playerColors[player.getID()], new Point(0, 0));
		panel1.add(colorLabel);
		panel1.add(new JLabel(player.getName()));
		JLabel label2 = new JLabel("Dostępne pionki: " +player.getNumberOfPawns());
		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayout(1, 5));
		for (int i = 0; i < 5; i++) {
			JLabel productLabel = new JLabel("" + player.getBoughtProducts()[i]
					+ "/" + player.getShoppingList().get(i));
			productLabel.setHorizontalAlignment(JLabel.CENTER);
			productLabel.setBackground(GameState.productColors[i]);
			productLabel.setOpaque(true);
			productLabel.setForeground(Color.black);
			panel3.add(productLabel, 0, i);
		}
		panel1.setAlignmentX(Component.LEFT_ALIGNMENT);
		label2.setAlignmentX(LEFT_ALIGNMENT);
		panel3.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(panel1);
		add(label2);
		add(panel3);
		setPreferredSize(new Dimension(150, 80));

	}

	private JLabel createColoredLabel(Color color, Point origin) {
		JLabel label = new JLabel();
		label.setOpaque(true);
		label.setBackground(color);
		label.setBorder(BorderFactory.createLineBorder(Color.black));
		label.setMinimumSize(new Dimension(15, 15));
		label.setPreferredSize(new Dimension(15, 15));
		return label;
	}

	protected void paintingComponentActualFunction(Graphics g) {
		int indexInColorArray = player.getID() + 1;
		if (indexInColorArray >= GameState.playerColors.length)
			throw new ArrayIndexOutOfBoundsException("players' list is "
					+ "longer than colors' number");
		g.setColor(GameState.playerColors[indexInColorArray]);
		g.setFont(g.getFont().deriveFont(20f));
		g.drawString(player.getName(), 60, 27);
		List<Integer> shList = player.getShoppingList();
		int[] bProd = player.getBoughtProducts();
		int i = 0;
		for (Color c : GameState.productColors) {
			g.setColor(c);
			g.drawString(bProd[i] + "/" + shList.get(i), 5 + 40 * i, 52);
			i++;
		}
		g.setColor(GameState.playerColors[indexInColorArray]);
		g.drawString("Dostępne pionki: " + player.getNumberOfPawns(), 5, 77);
	}

}
