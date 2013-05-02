package queue_game.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
import javax.swing.JComponent;
/**
 * @author piotr
 * 
 *	Handler, that knows when u click the Store.
 * 
 */
public class Handler extends MouseAdapter {
	private queue_game.view.Board myBoard;
	public Handler(queue_game.view.Board board){
		myBoard=board;
	}
	@Override
	public void mouseClicked(MouseEvent e){
		//JComponent clicked;
		System.out.println("kliknieto sklep: " +e.getComponent());
		
	}
}
