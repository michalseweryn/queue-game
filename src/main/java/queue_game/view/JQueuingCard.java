package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import queue_game.controller.Game;
import queue_game.creator.LocalGameActionCreator;
import queue_game.model.GameState;
import queue_game.model.Player;
import queue_game.model.QueuingCard;

/**
 * @author Jan
 * 
 * 
 */
public class JQueuingCard extends JPanel implements MouseListener {
	private static final long serialVersionUID = 6947197330462260996L;
	private QueuingCard card;
	private Player player;
	private LocalGameActionCreator creator;

	public JQueuingCard(Player player, QueuingCard card, Game game, LocalGameActionCreator creator) {
		this.creator = creator;
		this.card = card;
		this.player = player;
		addMouseListener(this);
		addToolTip();
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
		return new Dimension(size.height / 6 , 4 * size.height / 18);
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Dimension size = getSize();
		Dimension arcs = new Dimension(size.width / 10, size.width / 10); 
		//if (JGameArea.ANTYALIASING)
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//RoundRectangle2D rect = new Rectangle(0, 0, size.width - 1, size.height - 1);
		//g2d.setStroke(new BasicStroke(size.height/50));
		int indexInColorArray = player.getID();
		if (indexInColorArray >= GameState.playerColors.length)
			throw new ArrayIndexOutOfBoundsException("players' list is "
					+ "longer than colors' number");
		g2d.setColor(GameState.playerColors[indexInColorArray]);
		
		g2d.fillRoundRect(1, 1, size.width - 4, size.height - 4, arcs.width, arcs.height);
		g2d.setColor(Color.black);
		g2d.drawRoundRect(0, 0, size.width-3, size.height-3, arcs.width, arcs.height);		
		//g2d.setColor(Color.black);
		//g2d.drawRoundRect(0, 0, size.width, size.height,arcs.width,arcs.height);
		if (card != null) {
			g2d.setColor(Color.BLACK);
			//g2d.setFont(g.getFont().deriveFont(10f));
			//g2d.drawString(card.toStringPL, size.width / 5, size.height / 2);
			g2d.setFont(new Font("Arial Black", Font.BOLD, (int) (size.height/11)));
			drawString(g2d,card.toStringPL, size.width/20, size.height/6);
		}
		
		//g2d.dispose();
	}
	
	private void drawString(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }
	
	private void addToolTip(){
		switch(card){
		case CLOSED_FOR_STOCKTAKING:
			this.setToolTipText("Wybierz sklep, który nie będzie " +
					"sprzedawał towaru w tej rundzie");
			break;
		case COMMUNITY_LIST:
			this.setToolTipText("Ustaw wybraną kolejkę tył na przód");
			break;
		case CRITICIZING_AUTHORITIES:
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
		/*case TIPPING_FRIEND:
			this.setToolTipText("Podejrzyj dwie karty dostawy towaru");
			break;*/
		case UNDER_THE_COUNTER_GOODS:
			this.setToolTipText("Jeżeli jesteś pierwszy w kolejce, " +
					"zabierz natychmiast towar" );
			break;
		default:
			break;
		
		}
	}
	

	public void mouseClicked(MouseEvent e) {
		creator.queuingCardSelected(player.getID(), card);
		
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
