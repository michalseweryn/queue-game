package queue_game.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Menu extends JPanel{
	private static final long serialVersionUID = 2905345673253421L;
    
	public Menu(){
		  JFrame frame = new JFrame("Menu");
		  JButton button1 = new JButton("Stol 1");
		  JButton button2 = new JButton("Stol 2");
		  button1.addActionListener(new ActionListener(){
		  public void actionPerformed(ActionEvent ae){
		  }
		  });
		  button2.addActionListener(new ActionListener(){
			  public void actionPerformed(ActionEvent ae){
			  }
			  });
		  JPanel panel = new JPanel();
		  panel.add(button2);
		  panel.add(button1);
		  frame.add(panel);
		  frame.setSize(400, 400);
		  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  frame.setVisible(true);
		  synchronized(this){
			  try {
				wait(3600);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
	}
	
	

	
}
