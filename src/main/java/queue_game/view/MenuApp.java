package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import queue_game.client.ClientApp;
import queue_game.server.PlayerConnection;
import queue_game.server.Utilities;

public class MenuApp {
	private String nickname;
	private Socket connection = null;
	private Reader in;
	private Writer out;
	private PlayerConnection player;

	public String[][] imiona = new String[][]{
			{new String("Adam"),new String("Ela")},{new String("Marek23"),new String("Nikt00")},
			{new String("Michal45"),new String("Jan33")},{new String("Asia23"),new String("Ewa34")}
		};
	public boolean[] stateOfTable = new boolean[]{
			true, false,true,false
	};
	public MenuApp() {
		nickname = JOptionPane.showInputDialog(null, "Podaj nick : ", "Nick",
				1);
		if (nickname != null) {
			if(!nickname.isEmpty() && !nickname.contains(" ")){
				JOptionPane.showMessageDialog(null, "Twój nick to : " + nickname,
					"Nick", 1);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Menu();
				}
			});
			}
			else{
				JOptionPane.showMessageDialog(null, "Błędny nick.", "Nick", 1);
			}
		} else
			JOptionPane.showMessageDialog(null, "Zrezygnowałeś.", "Nick", 1);
	}

	public void Menu(){
		
		JFrame frame = new JFrame("Menu");
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		for(int i=1;i<5;i++){
			JLabel label;
			if(stateOfTable[i-1]) label=new JLabel("Stół "+i+":  GRA TRWA");
			else label=new JLabel("Stół "+i+":  BRAK");
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
				panela.setOpaque(false);
				coloreLabel(labela,new Color(0x4CC9E6));
				labela.setPreferredSize(new Dimension(70,40));
			}
			label.setHorizontalAlignment(JLabel.CENTER);
			  JButton button = new JButton("Dołącz");
			  final int tmp=i;
			  button.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						join(tmp);
					}
				});
			  JPanel panel = new JPanel();
			  coloreLabel(label,Color.WHITE);
			  panel.add(label);
			  panel.add(button);
			  panel.setOpaque(false);
			  frame.getContentPane().add(panel);
			  frame.getContentPane().add(panela);
		}
		  frame.setSize(400, 450);
		  frame.getContentPane().setBackground(new Color(0xD8F2DE));
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
	private void join(int i){
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

		try {
			Utilities.writeObject(out, "NAME " + nickname + " ");
			Utilities.finishWriting(out);
			int tables = Utilities.readInt(in);
			while(tables-- > 0)
				Utilities.readInt(in);
			Utilities.writeObject(out, "JOIN "+ i + " ");
			Utilities.finishWriting(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			new ClientApp(connection,nickname);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MenuApp ma = new MenuApp();
	}

}
