package queue_game;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import queue_game.view.Board;

/**
 * Hello world!
 *
 */
public class App extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1820499848125435414L;
	public App(){
		super("Kolejka");
		add(new Board());
		pack();
		setVisible(true);
	}
	public static void createAndShowGUI(){
		new App();
		
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
