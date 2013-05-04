package queue_game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import queue_game.model.ProductType;
import queue_game.model.Store;

public class JStore extends JComponent implements MouseListener{
	private static final long serialVersionUID = 7452536279840255740L;
	private Color color;
	private JBoard board;
	private Store store;
	private ProductType product;
	private JProductAmountField prAmountField;
	
	@Override
	public Dimension getMinimumSize(){
		return new Dimension(80, 300);
	}
	@Override
	public Dimension getPreferredSize(){
		if (getParent() == null)
			return new Dimension(120, 420);
		Dimension size = getParent().getSize();
		return new Dimension(size.width / 5,  2 * size.width / 15);
	}
	@Override
	public Dimension getMaximumSize(){
		return new Dimension(130, 640);
	}
	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension size = getSize();
		g.setColor(color);
		g.fillRect(0, 0, size.width - 1, size.height / 5);
		int initialHeight = size.height / 5 + 2;
		int remainingHeight = size.height - initialHeight;
		//When there are more than 10 pawns, you should resize the rectangles
		for(int i = 0; i < 10; ++i) {
			g.fillRect(size.width / 3, initialHeight + i * (remainingHeight / 10 + 1), size.width / 3, remainingHeight / 10 - 2);
		}
	}
	
	/**
	 * @param store
	 */
	public JStore(queue_game.model.Store store, JBoard board) {
		this.product = store.productType;
		this.store = store;
		FlowLayout layout = new FlowLayout();
		layout.setVgap(0);
		layout.setHgap(0);
		setLayout(layout);
		this.board = board;
		this.color = JBoard.defaultColorSet[product.ordinal()];
		addMouseListener(this);
		addProductAmountField();
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	/**
	 * 
	 * Returns Store object equivalent to this JStore object. 
	 */

	public Store getStore() {
		return store;
	}
	
	protected void addProductAmountField() {
		prAmountField = new JProductAmountField(Color.CYAN, this);
		add(prAmountField);
		
	}
	
	public void setProductAmountFieldVisible(boolean b) {
		prAmountField.setVisible(b);
	}
	
	
	public void mouseClicked(MouseEvent e) {
		if(board != null && board.getGame() != null && board.getGameState() != null)
			board.getGame().queueSelected(board.getGameState().getActivePlayer(), product);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {}
	
}
