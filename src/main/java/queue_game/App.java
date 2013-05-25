package queue_game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import queue_game.controller.Game;
import queue_game.creator.LocalGameActionCreator;
import queue_game.model.DecksOfQueuingCardsBox;
import queue_game.model.GameState;
import queue_game.model.StandardDeckOfQueuingCards;
import queue_game.view.JGameArea;
import queue_game.view.JPlayerList;

/**
 * Main window of game.
 *
 */
public class App {
	JGameArea gameArea;
	JPlayerList playerList;
	GameState gameState;
	LocalGameActionCreator creator;
	Game game;
	DecksOfQueuingCardsBox queuingCardsDecks;
	
	public App(){
		gameState = new GameState(Arrays.asList("Adam", "Bob", "Carl", "Dan", "Eve"));
        
		creator = new LocalGameActionCreator(gameState);
        
		playerList = new JPlayerList(gameState);
        gameArea = new JGameArea(gameState, creator);
        creator.addView(playerList);
        creator.addView(gameArea);
        queuingCardsDecks = new DecksOfQueuingCardsBox(gameState); 
        
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
        game = new Game(gameState, creator);
        game.addView(gameArea);
        game.addView(playerList);
        gameArea.setGame(game);
        /*try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        game.startGame(5, queuingCardsDecks);
	}
	private void createAndShowGUI(){
		JFrame frame = new JFrame("FIAO");
		frame.setPreferredSize(new Dimension(800, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        //gameState.addView(gameArea);
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
