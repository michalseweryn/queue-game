package queue_game;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import queue_game.controller.Game;
import queue_game.view.JBoard;
import queue_game.view.JPlayerList;

/**
 * Main window of game.
 *
 */
public class App {
	public static void createAndShowGUI(Game game){
		JFrame frame = new JFrame("Kolejka");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        JBoard board = new JBoard(game);
        game.addView(board);
        
        JPlayerList playerList = new JPlayerList(game);
        game.addView(playerList);

        frame.getContentPane().add(board);
        frame.getContentPane().add(playerList, BorderLayout.EAST);
        frame.pack();
		frame.setVisible(true);
	}
    public static void main( String[] args )
    {
        final Game game = new Game();
        game.startGame(5);
        SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				createAndShowGUI(game);
			}
		});
    }
}
