/**
 * 
 */
package queue_game.nview;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import queue_game.controller.Game;

/**
 * @author michal
 *
 */
public class JGameArea extends JPanel implements queue_game.view.View{
	
	private static final long serialVersionUID = -1161332361871600948L;
	
	private Dimension defaultSize = new Dimension(640, 480);
	private ProductPanel productPanel;
	private JBoard board;
	private JCardsArea cardsArea;
	public JGameArea(Game game){
		super();
		this.productPanel = new ProductPanel(game, board);
		this.board = new JBoard(game);
		this.cardsArea = new JCardsArea(game);
		add(productPanel, BorderLayout.PAGE_START);
		add(board, BorderLayout.CENTER);
		add(cardsArea, BorderLayout.PAGE_END);
	}
	@Override
	public Dimension getPreferredSize(){
		return defaultSize;
		
	}
	public void update() {
		productPanel.repaint();
		board.update();
		cardsArea.update();
		
	}

}
