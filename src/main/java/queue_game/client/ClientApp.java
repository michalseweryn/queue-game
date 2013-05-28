package queue_game.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import queue_game.ActionCreator;
import queue_game.controller.Game;
import queue_game.creator.LocalGameActionCreator;
import queue_game.model.GameAction;
import queue_game.model.GameActionType;
import queue_game.model.GameState;
import queue_game.server.PlayerConnection;
import queue_game.server.Utilities;
import queue_game.view.JGameArea;
import queue_game.view.JPlayerList;

/**
 * Main window of game.
 * 
 */
public class ClientApp implements ActionCreator{
	private JGameArea gameArea;
	private JPlayerList playerList;
	private GameState gameState;
	private LocalGameActionCreator localCreator;
	private Game game;
	private Socket connection = null;
	private Reader in;
	private Writer out;
	private PlayerConnection player;
	private JButton startButton;
	private String name;
	private int nPlayers = 0;
	private int playerId;
	private String[] names;

	public ClientApp() {
		String host = "127.0.0.1";
		try {
			connection = new Socket(host, 17373);
			in = new InputStreamReader(connection.getInputStream());
			out = new OutputStreamWriter(connection.getOutputStream());
			player = new PlayerConnection(connection);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Random r = new Random();
		name = Arrays.asList("Adam", "Bob", "Carl", "Dan", "Eve").get(
				r.nextInt(5))
				+ r.nextInt(100);
		System.out.println( "Wylosowane imię " + name);
		try {
			Utilities.writeObject(out, "NAME " + name + " ");
			Utilities.finishWriting(out);
			int tables = Utilities.readInt(in);
			while(tables-- > 0)
				System.out.println(Utilities.readInt(in));
			Utilities.writeObject(out, "JOIN 0 ");
			Utilities.finishWriting(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			public void run() {
				while (true) {
					GameAction action;
					try {
						action = GameAction.read(in);
						handleAction(action);
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
			}

		}).start();

		gameState = new GameState(Arrays.asList(name));

		/*localCreator = new LocalGameActionCreator(gameState);

		playerList = new JPlayerList(gameState);
		gameArea = new JGameArea(gameState, localCreator);
		localCreator.addView(playerList);
		localCreator.addView(gameArea);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
		game = new Game(gameState, localCreator, null);
		game.addView(gameArea);
		game.addView(playerList);
		gameArea.setGame(game);
		// game.startGame(5, this);*/
	}

	private void handleAction(GameAction action) {
		System.out.println(action);
		if(action.getType() == GameActionType.JOIN){
			int id = (Integer)action.getInfo()[0];
			String name = (String)action.getInfo()[1];
			names[id] = name;
			gameState = new GameState(Arrays.asList(names));
			if(name.equals(this.name)){
				playerId = id;
				System.out.println("nasze id " + id);
			}
		}

	}

	private void createAndShowGUI() {
		JFrame frame = new JFrame("FIAO");
		frame.setPreferredSize(new Dimension(800, 500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		// gameState.addView(gameArea);
		// gameState.addView(playerList);

		frame.getContentPane().add(gameArea);
		frame.getContentPane().add(playerList, BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) throws InvocationTargetException,
			InterruptedException {
		new ClientApp();
	}

	/* (non-Javadoc)
	 * @see queue_game.ActionCreator#getAction()
	 */
	public GameAction getAction() throws InterruptedException {
		//if(gameState.getActivePlayer() == )
		return null;
	}
}
