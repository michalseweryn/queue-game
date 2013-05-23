package queue_game.view;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import queue_game.controller.Game;
import queue_game.model.GameAction;
import queue_game.model.GameState;
import queue_game.model.Player;

public class JPlayerList extends JPanel implements queue_game.view.View {
	private static final long serialVersionUID = 6386690129053320221L;
	private GameState gameState; 
	private ArrayList<JPlayerInfo> list = new ArrayList<JPlayerInfo>();
	JTextArea log = new JTextArea();
	public JPlayerList(GameState gameState) {
		super();
		this.gameState = gameState;
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		//setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		addFields();
	}
	
	private void addFields()
	{
		ArrayList<Player> players= gameState.getPlayersList();
		for (Player p : players)
		{
			JPlayerInfo temp = new JPlayerInfo(p);
			add(temp);
			add(Box.createRigidArea(new Dimension(0, 5)));
			list.add(temp);
		}
		log.setEditable(false);
		log.setLineWrap(true);
		log.setText(makeLog(gameState.getGameActions()));
		JScrollPane scrollingLog = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollingLog.setPreferredSize(new Dimension(150, 100));
		add(scrollingLog);
	}

	private String makeLog(List<GameAction> actions) {
		StringBuilder sb = new StringBuilder();
		for(GameAction action: actions) {
			sb.append(action).append('\n');
		}
		if(sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension size = getParent().getSize();
		return new Dimension(200, size.height);
	}
	
	public void update(){
		for(JPlayerInfo info : list)
			info.update();
		log.setText(makeLog(gameState.getGameActions()));
		revalidate();
	}

}
