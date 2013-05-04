package queue_game;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import queue_game.controller.Game;
import queue_game.view.Board;

/**
 * Main window of game.
 *
 */
public class App {
	/**
	 * 
	 */
	public static void createAndShowGUI(){
		JFrame frame = new JFrame("Kolejka");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        Board board = new Board();
        Game game = new Game();
        board.setGame(game);
        frame.add(board);
		frame.pack();
		frame.setVisible(true);
		
	}
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				createAndShowGUI();
			}
		});
    }
}
