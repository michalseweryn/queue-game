package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
	public Color[] kolory = new Color[]{
		Color.CYAN, Color.GREEN, Color.PINK, Color.YELLOW
	};
	public String[][] imiona = new String[][]{
			{new String("Adam"),new String("Ela")},{new String("Marek23"),new String("Nikt00")},
			{new String("Michal45"),new String("Jan33")},{new String("Asia23"),new String("Ewa34")}
		};
	public String[] stateOfTable = new String[]{
			new String("GRA TRWA"), new String("BRAK"),new String("GRA TRWA"),new String("BRAK")
	};
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
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		for(int i=1;i<5;i++){
			JLabel label = new JLabel("Stół "+i+":  "+stateOfTable[i-1]);
			 JPanel panela = new JPanel();
			 label.setFont(new Font("SERIF",1,15));
			 int j=0;
			for(int k=0;k<5;k++){
				JLabel labela;
				if(j<imiona[i-1].length) labela = new JLabel(imiona[i-1][j]);
				else labela = new JLabel(" ");
				j++;
				labela.setHorizontalAlignment(JLabel.CENTER);
				panela.add(labela);
				coloreLabel(labela,Color.WHITE);
				labela.setPreferredSize(new Dimension(70,40));
			}
			label.setHorizontalAlignment(JLabel.CENTER);
			  JButton button = new JButton("Dołącz");
			  JPanel panel = new JPanel();
			  coloreLabel(label,kolory[i-1]);
			  panel.add(label);
			  panel.add(button);
			  
			  frame.getContentPane().add(panel);
			  frame.getContentPane().add(panela);
		}
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
