package queue_game.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.BitSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import queue_game.controller.Game;
import queue_game.creator.LocalGameActionCreator;
import queue_game.model.ButtonType;
import queue_game.model.GamePhase;
import queue_game.model.GameState;
import queue_game.model.Player;
import queue_game.model.ProductType;
import queue_game.model.QueuingCard;

/**
 * @author Jan
 * 
 *         Panel for queuing cards.
 */
public class JCardsArea extends JPanel {
	private static final long serialVersionUID = -3804758455470286800L;
	private GameState gameState;
	private Game game;
	private JLabel messageLabel;
	private LocalGameActionCreator creator;
	private int playerId; //-1 while hot-seat game
	private ButtonType buttonType = null;
	public JCardsArea(GameState gameState, LocalGameActionCreator creator){
		super();
		this.creator = creator; 
		this.gameState = gameState;
		setLayout(new BorderLayout());
		addCards();
	}
	

	public void addGame(Game game){
		this.game = game;
	}
	

	public synchronized void setButtonType(ButtonType buttonType){
		if(this.buttonType != buttonType)
		{
			this.buttonType = buttonType;
			update();
		}
	}
	
	private void addCards(){
		if(gameState.getActivePlayer() >=0){
			Player activePlayer = gameState.getPlayersList().get(gameState.getActivePlayer());
			if(playerId == -1 || activePlayer.getID() == playerId)
				messageLabel = new JLabel(activePlayer.getName() +": " + gameState.getErrorMessage()
						+ " " + gameState.getMessage());
			else messageLabel = new JLabel("Ruch gracza " + activePlayer.getName());
			add(messageLabel, BorderLayout.PAGE_START);
			JPanel contentPanel = new JPanel();
			Player player = gameState.getPlayersList().get((gameState.getActivePlayer()));
			if(gameState.getCurrentGamePhase() == GamePhase.JUMPING){
				if(buttonType == ButtonType.PASS){
					JButton passButton = new JButton("PASS");
					passButton.addActionListener(new ActionListener(){
	
						public void actionPerformed(ActionEvent e) {
							creator.queuingCardSelected(gameState.getActivePlayer(), null);
						}
					});
					contentPanel.add(passButton);
				}
				if(buttonType == ButtonType.CANCEL){
					JButton cancelButton = new JButton("ANULUJ");
					cancelButton.addActionListener(new ActionListener(){
		
						public void actionPerformed(ActionEvent e) {
							creator.cancel(gameState.getActivePlayer());
						}
					});
					contentPanel.add(cancelButton);
				}
			}
			if(gameState.getCurrentGamePhase() == GamePhase.PCT){
				JButton passButton = new JButton("PASS");
				passButton.addActionListener(new ActionListener(){
	
					public void actionPerformed(ActionEvent e) {
						creator.pawnSelected(game.getGameState().getActivePlayer(), null, -1);
					}
				});
				contentPanel.add(passButton);
			}
			if(gameState.getCurrentGamePhase()!= null && gameState.getCurrentGamePhase().ordinal() < 3)
			for(QueuingCard i: gameState.getPlayersList().get((gameState.getActivePlayer())).getCardsOnHand()){
				JQueuingCard temp = new JQueuingCard(player,i,game, creator);
				contentPanel.add(temp);
			}
			if(gameState.getCurrentGamePhase() == GamePhase.EXCHANGING){
				int id = gameState.getActivePlayer();
				if(playerId == -1 || id==playerId){
					for(ProductType type : ProductType.values()){
						int bought = gameState.getPlayer(id).getBoughtProducts()[type.ordinal()];
						if(bought > 0){
							JProductSquare square = new JProductSquare(game, type, bought, null, creator,
									true, false);
							square.setMinimumSize(new Dimension(30, 30));
							square.setPreferredSize(new Dimension(30, 30));
							contentPanel.add(square);
						}
					}
					if(buttonType == ButtonType.PASS){
						JButton passButton = new JButton("PASS");
						passButton.addActionListener(new ActionListener(){
		
							public void actionPerformed(ActionEvent e) {
								creator.productSelected(game.getGameState().getActivePlayer(), null, null,
										false, false);
							}
						});
						contentPanel.add(passButton);
					}
					if(buttonType == ButtonType.CANCEL){
						JButton cancelButton = new JButton("ANULUJ");
						cancelButton.addActionListener(new ActionListener(){
			
							public void actionPerformed(ActionEvent e) {
								creator.cancel(gameState.getActivePlayer());
							}
						});
						contentPanel.add(cancelButton);
					}
				}	
			}
			add(contentPanel, BorderLayout.CENTER);
		}

	}
	public void setGame(Game game){
		this.game = game;
	}
	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}
	/*private void addExchangeComponents(){
		
	}
	*/
	/*
	@Override
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}*/
	
	@Override
	public Dimension getPreferredSize(){
		if (getParent() == null)
			return null;
		return getParent().getSize();
	}
	/*@Override
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}*/
	/*@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		/*Graphics2D g2d = (Graphics2D) g;
		Dimension size = getSize();
		Rectangle rect = new Rectangle(0,0 ,size.width ,size.height);		
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, size.width, size.height);
		g2d.setColor(Color.BLACK);
		g2d.draw(rect);
	}*/
	
	public void update() {
		removeAll();
		addCards();
		revalidate();
		repaint();
	}
	

}
