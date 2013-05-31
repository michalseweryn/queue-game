package queue_game.view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MenuApp {

	public MenuApp() {
		String str = JOptionPane.showInputDialog(null, "Podaj nick : ", "Nick",
				1);
		if (str != null) {
			JOptionPane.showMessageDialog(null, "Twój nick to : " + str,
					"Nick", 1);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Menu();
				}
			});
		} else
			JOptionPane.showMessageDialog(null, "Zrezygnowales.", "Nick", 1);
	}

	public void Menu(){
		JFrame frame = new JFrame("Menu");
		  JLabel label1 = new JLabel("Stol 1: Adam, Ewa, Ktos GRA W TRAKCIE");
		  JLabel label2 = new JLabel("Stol 2: Marek, Ela, Nikt GRA W TRAKCIE");
		  JLabel label3 = new JLabel("Stol 3: Adam, Ewa, Janek GRA W TRAKCIE");
		  JLabel label4 = new JLabel("Stol 4: Michał, Janek GRA W TRAKCIE");
		  JButton button1 = new JButton("Dołącz");
		  JButton button2 = new JButton("Dołącz");
		  JButton button3 = new JButton("Dołącz");
		  JButton button4 = new JButton("Dołącz");
		  JPanel panel1 = new JPanel();
		  JPanel panel2 = new JPanel();
		  JPanel panel3 = new JPanel();
		  JPanel panel4 = new JPanel();
		  panel1.add(label1);
		  panel1.add(button1);
		  panel2.add(label2);
		  panel2.add(button2);
		  panel3.add(label3);
		  panel3.add(button3);
		  panel4.add(label4);
		  panel4.add(button4);
		  frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		  frame.getContentPane().add(panel1);
		  frame.getContentPane().add(panel2);
		  frame.getContentPane().add(panel3);
		  frame.getContentPane().add(panel4);
		  frame.setSize(400, 400);
		  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  frame.setVisible(true);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MenuApp ma = new MenuApp();
	}

}
