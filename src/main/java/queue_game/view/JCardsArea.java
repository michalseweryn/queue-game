package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import queue_game.View;
import queue_game.controller.Game;
import queue_game.model.GameState;
import queue_game.model.Player;
import queue_game.model.QueuingCard;

/**
 * @author Jan
 * 
 *         Panel for queuing cards.
 */
public class JCardsArea extends JPanel implements View{
	private static final long serialVersionUID = -3804758455470286800L;
	private GameState gameState;
	private Game game;
	public JCardsArea(Game game){
		super();
		this.gameState = game.getGameState();
		this.game = game;
		game.addView(this);
		FlowLayout layout = new FlowLayout();
		layout.setVgap(0);
		layout.setHgap(0);
		setLayout(layout);	
		addCards();
	}
	private void addCards(){
		//for(int i = 0; i < 3; i++)
		//	cards[i] = gameState.getDeck(gameState.getActivePlayer());
		//for(int i = 0; i < 3; i++){			
		List<QueuingCard> cards = gameState.getPlayersList().get((gameState.getActivePlayer())).getCardsOnHand();
		Player player = gameState.getPlayersList().get((gameState.getActivePlayer()));
		if(cards.size() > 0){
			JButton button = new JButton("PASS");
			button.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					game.queuingCardSelected(gameState.getActivePlayer(), null); 
				}
			});
			add(button);
		}
		for(QueuingCard i: gameState.getPlayersList().get((gameState.getActivePlayer())).getCardsOnHand()){
			JQueuingCard temp = new JQueuingCard(player,i,game);
			add(temp);
		}
	}
	@Override
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	
	@Override
	public Dimension getPreferredSize(){
		if (getParent() == null)
			return null;
		Dimension size = getParent().getSize();
		return new Dimension(size.width, 150 );
	}
	@Override
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Dimension size = getSize();
		Rectangle rect = new Rectangle(0,0 ,size.width ,size.height);		
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, size.width, size.height);
		g2d.setColor(Color.BLACK);
		g2d.draw(rect);
	}
	
	public void update() {
		removeAll();
		addCards();
		revalidate();
		repaint();
	}
	

}