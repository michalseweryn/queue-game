package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
		  JLabel label1 = new JLabel("Stol 1: GRA W TRAKCIE");
		  JLabel label1a = new JLabel("Adam   Ewa   Ktos");
		  JLabel label2 = new JLabel("Stol 2: GRA W TRAKCIE");
		  JLabel label2a = new JLabel("Marek   Ela   Nikt");
		  JLabel label3 = new JLabel("Stol 3: GRA W TRAKCIE");
		  JLabel label3a = new JLabel("Adam   Ewa   Janek");
		  JLabel label4 = new JLabel("Stol 4: GRA W TRAKCIE");
		  JLabel label4a = new JLabel("Michał   Janek");
		  JButton button1 = new JButton("Dołącz");
		  JButton button2 = new JButton("Dołącz");
		  JButton button3 = new JButton("Dołącz");
		  JButton button4 = new JButton("Dołącz");
		  JPanel panel1 = new JPanel();
		  JPanel panel1a = new JPanel();
		  JPanel panel2 = new JPanel();
		  JPanel panel2a = new JPanel();
		  JPanel panel3 = new JPanel();
		  JPanel panel3a = new JPanel();
		  JPanel panel4 = new JPanel();
		  JPanel panel4a = new JPanel();
		  coloreLabel(label1,Color.CYAN);
		  coloreLabel(label1a,Color.WHITE);
		  label1a.setPreferredSize(new Dimension(350,50));
		  coloreLabel(label2,Color.GREEN);
		  coloreLabel(label2a,Color.WHITE);
		  label2a.setPreferredSize(new Dimension(350,50));
		  coloreLabel(label3,Color.PINK);
		  coloreLabel(label3a,Color.WHITE);
		  label3a.setPreferredSize(new Dimension(350,50));
		  coloreLabel(label4,Color.YELLOW);
		  coloreLabel(label4a,Color.WHITE);
		  label4a.setPreferredSize(new Dimension(350,50));
		  panel1.add(label1);
		  panel1.add(button1);
		  panel1a.add(label1a);
		  panel2.add(label2);
		  panel2.add(button2);
		  panel2a.add(label2a);
		  panel3.add(label3);
		  panel3.add(button3);
		  panel3a.add(label3a);
		  panel4.add(label4);
		  panel4.add(button4);
		  panel4a.add(label4a);
		  frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		  frame.getContentPane().add(panel1);
		  frame.getContentPane().add(panel1a);
		  frame.getContentPane().add(panel2);
		  frame.getContentPane().add(panel2a);
		  frame.getContentPane().add(panel3);
		  frame.getContentPane().add(panel3a);
		  frame.getContentPane().add(panel4);
		  frame.getContentPane().add(panel4a);
		  frame.setSize(400, 450);
		  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  frame.setVisible(true);
	}
	private JLabel coloreLabel(JLabel label,Color color) {
		label.setOpaque(true);
		label.setBackground(color);
		label.setBorder(BorderFactory.createLineBorder(Color.black));
		label.setPreferredSize(new Dimension(200, 25));
		return label;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MenuApp ma = new MenuApp();
	}

}
