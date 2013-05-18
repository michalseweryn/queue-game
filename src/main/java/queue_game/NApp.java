package queue_game;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import queue_game.controller.Game;
import queue_game.nview.JGameArea;
import queue_game.nview.JPlayerList;

/**
 * Main window of game.
 *
 */
public class NApp {
	public static void createAndShowGUI(Game game){
		JFrame frame = new JFrame("FIAO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        JGameArea gameArea = new JGameArea(game);
        game.addView(gameArea);
        
        JPlayerList playerList = new JPlayerList(game);
        game.addView(playerList);
	
        frame.getContentPane().add(gameArea);
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
