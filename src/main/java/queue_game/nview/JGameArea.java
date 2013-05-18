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
	private JBoard board;
	private JCardsArea cardsArea;
	public JGameArea(Game game){
		super();
		this.board = new JBoard(game);
		this.cardsArea = new JCardsArea(game);
		add(board);
		add(cardsArea, BorderLayout.SOUTH);
	}
	@Override
	public Dimension getPreferredSize(){
		return defaultSize;
		
	}
	public void update() {
		board.update();
		cardsArea.update();
		
	}

}
