package queue_game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import queue_game.controller.Game;
import queue_game.model.GameState;
import queue_game.view.JGameArea;
import queue_game.view.JPlayerList;

/**
 * Main window of game.
 *
 */
public class App {
	JGameArea gameArea;
	GameState gameState;
	Game game;
	public App(){
		gameState = new GameState(Arrays.asList("Adam", "Bob", "Carl", "Dan", "Eve"));

        gameArea = new JGameArea(gameState);
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
        game = new Game(gameState);
        game.addView(gameArea);
        gameArea.setGame(game);
        game.startGame(5);
	}
	private void createAndShowGUI(){
		JFrame frame = new JFrame("FIAO");
		frame.setPreferredSize(new Dimension(800, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        //gameState.addView(gameArea);
        
        JPlayerList playerList = new JPlayerList(gameState);
        //gameState.addView(playerList);
	
        frame.getContentPane().add(gameArea);
        frame.getContentPane().add(playerList, BorderLayout.EAST);
        frame.pack();
		frame.setVisible(true);
		}
    public static void main( String[] args ) throws InvocationTargetException, InterruptedException
    {
    	new App();
    }
}
