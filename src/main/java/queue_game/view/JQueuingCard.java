package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import queue_game.controller.Game;
import queue_game.model.DeckOfCards;
import queue_game.model.GameState;
import queue_game.model.Player;
import queue_game.model.QueuingCard;

/**
 * @author Jan
 * 
 * 
 */
public class JQueuingCard extends JPanel implements MouseListener{
	private static final long serialVersionUID = 6947197330462260996L;
	private QueuingCard card;
	private Player player;
	private Game game;
	public JQueuingCard(Player player, QueuingCard card,Game game) {
		this.card = card;
		this.player = player;
		this.game = game;
		addToolTip();
		addMouseListener(this);
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getPreferredSize() {
		if (getParent() == null)
			return null;
		Dimension size = getParent().getSize();
		return new Dimension(size.width / 6, size.height);
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Dimension size = getSize();
		Rectangle rect = new Rectangle(0, 0, size.width - 1, size.height - 1);
		int indexInColorArray = player.getID() + 1;
		if (indexInColorArray >= GameState.playerColors.length)
			throw new ArrayIndexOutOfBoundsException("players' list is "
					+ "longer than colors' number");
		g2d.setColor(GameState.playerColors[indexInColorArray]);
		g2d.fillRect(0, 0, size.width, size.height);
		g2d.setColor(Color.BLACK);
		g2d.draw(rect);

		if (card != null) {
			g.setFont(g.getFont().deriveFont(10f));
			g.drawString(card.toString(), size.width / 5, size.height / 2);
		}
	}
	public void mouseClicked(MouseEvent e) {
		game.queuingCardSelected(player.getID(), card); 
	}
	/**
	 * @author piotr
	 * ToolTips for QueuingCards
	 */
	private void addToolTip(){
		switch(card){
		case CLOSED_FOR_STOCKTAKING:
			this.setToolTipText("Wybierz sklep, który nie będzie " +
					"sprzedawał towaru w tej rundzie");
			break;
		case COMMUNITY_LIST:
			this.setToolTipText("Ustaw wybraną kolejkę tył na przód");
			break;
		case CRITISIZING_AUTHORITIES:
			this.setToolTipText("Przesuń wybrany pionek o dwa miejsca do tyłu");
			break;
		case DELIVERY_ERROR:
			this.setToolTipText("Przenieś towar z jednego sklepu do drugiego");
			break;
		case INCREASED_DELIVERY:
			this.setToolTipText("Dołóż jeden towar do sklepu, w którym" +
					" przed chwilą miała miejsce dostawa towaru");
			break;
		case LUCKY_STRIKE:
			this.setToolTipText("Przesuń swój pionek na drugie miejsce " +
					"do wybranej kolejki");
			break;
		case MOTHER_WITH_CHILD:
			this.setToolTipText("Przesuń swój pionek na początek kolejki");
			break;
		case NOT_YOUR_PLACE:
			this.setToolTipText("Przesuń swój pionek o jedno miejsce do przodu");
			break;
		case TIPPING_FRIEND:
			this.setToolTipText("Podejrzyj dwie karty dostawy towaru");
			break;
		case UNDER_THE_COUNTER_GOODS:
			this.setToolTipText("Jeżeli jesteś pierwszy w kolejce, " +
					"zabierz natychmiast towar" );
			break;
		default:
			break;
		
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseReleased(MouseEvent e) {}
}
