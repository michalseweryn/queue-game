package queue_game.view;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MenuApp {

	public MenuApp(){
		String str = JOptionPane.showInputDialog(null, "Enter some text : ", 
				"Roseindia.net", 1);
					  if(str != null){
					  JOptionPane.showMessageDialog(null, "You entered the name : " + str, 
					"Roseindia.net", 1);
					  SwingUtilities.invokeLater((Runnable) new Menu()); 
					  }
					  else
					  JOptionPane.showMessageDialog(null, "You pressed cancel button.", 
					"Roseindia.net", 1);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MenuApp ma = new MenuApp();
	}

}
