package queue_game;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import queue_game.view.Board;

/**
 * Hello world!
 *
 */
public class App {
	/**
	 * 
	 */
	public static void createAndShowGUI(){
		JFrame frame = new JFrame("Kolejka");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.add(new Board());
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
