package queue_game.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

import queue_game.controller.Game;
import queue_game.creator.LocalGameActionCreator;
import queue_game.model.GameState;
import queue_game.model.ProductType;

/**
 * @author Helena
 * 
 */

public class JPawn extends JComponent implements MouseListener {

	private static final long serialVersionUID = 382939404028495740L;
	private Game game;
	private int position;
	private int playerId;
	private GeneralPath pawnShape;
	private ProductType destination;
	private double pawnHeight;
	private boolean mouseOver;
	private LocalGameActionCreator localGameActionCreator;
	private static double STROKE = 0.04;
	private static double LEG_HEIGHT = 1. / 3;
	private static double LEG_WIDTH = 1. / 6;
	private static double ARM_HEIGHT = 1. / 4;
	private static double ARM_WIDTH = 1. / 12;
	private static double SHOULDER_R = 1. / 6;
	private static double HEAD_R = 1. / 8;

	private void lineMove(double dx, double dy) {
		double x = pawnShape.getCurrentPoint().getX();
		double y = pawnShape.getCurrentPoint().getY();
		pawnShape.lineTo(x + dx * pawnHeight, y + dy * pawnHeight);
	}

	private void arcMove(double dx, double dy, double ex, double ey) {
		double x = pawnShape.getCurrentPoint().getX();
		double y = pawnShape.getCurrentPoint().getY();
		pawnShape.curveTo(x, y, x + dx * pawnHeight, y + dy * pawnHeight, x
				+ (dx + ex) * pawnHeight, y + (dy + ey) * pawnHeight);
	}

	private void generateShape() {
		resetHeight();
		pawnShape = new GeneralPath();
		double d = STROKE * pawnHeight;
		pawnShape.moveTo(d + (ARM_WIDTH + LEG_WIDTH) * pawnHeight, d + pawnHeight);
		lineMove(0, -LEG_HEIGHT);
		lineMove(0, LEG_HEIGHT);
		lineMove(LEG_WIDTH, 0);
		lineMove(0, -LEG_HEIGHT - ARM_HEIGHT);
		lineMove(0, ARM_HEIGHT);
		lineMove(ARM_WIDTH, 0);
		lineMove(0, -ARM_HEIGHT);
		arcMove(0, -SHOULDER_R, -SHOULDER_R, 0);
		lineMove(SHOULDER_R - ARM_WIDTH - LEG_WIDTH, 0);
		arcMove(HEAD_R, 0, 0, -HEAD_R);
		arcMove(0, -HEAD_R, -HEAD_R, 0);
		arcMove(-HEAD_R, 0, 0, HEAD_R);
		arcMove(0, HEAD_R, HEAD_R, 0);
		lineMove(SHOULDER_R - ARM_WIDTH - LEG_WIDTH, 0);
		arcMove(-SHOULDER_R, 0, 0, SHOULDER_R);
		lineMove(0, ARM_HEIGHT);
		lineMove(ARM_WIDTH, 0);
		lineMove(0, -ARM_HEIGHT);
		lineMove(0, LEG_HEIGHT + ARM_HEIGHT);
		lineMove(LEG_WIDTH, 0);
		pawnShape.closePath();

	}

	public JPawn(Game game, ProductType product, int playerId, int place,
			double pawnHeight, LocalGameActionCreator localGameActionCreator) {
		this.destination = product;
		this.game = game;
		this.playerId = playerId;
		this.position = place;
		this.pawnHeight = pawnHeight;
		this.localGameActionCreator = localGameActionCreator;
		generateShape();
		mouseOver = false;
		enableInputMethods(true);
		addMouseListener(this);
	}
/*
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (pawnHeight / 2), (int) pawnHeight);
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
*/
	private void resetHeight() {
		this.pawnHeight = getSize().getHeight();
		pawnHeight -= 4 * STROKE * pawnHeight;
	}
	
	public void setPlayerId(int i) {
		this.playerId = i;
	}
	@Override
	public boolean contains(int x, int y){
		return pawnShape.contains(x, y);
	}
	private Color lighter(Color c){
		return new Color((255 + 2 * c.getRed()) / 3, (255 + 2 * c.getGreen()) / 3, (255 + 2 * c.getBlue()) / 3);
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		//System.out.println("pionek" + getBounds());
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Color c = Color.black;
		if(playerId >= 0)
			c = GameState.playerColors[playerId];
		if(mouseOver)
			c = lighter(c);
		g2d.setColor(c);
		generateShape();
		g2d.fill(pawnShape);
		g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke((float) (pawnHeight * STROKE)));
		g2d.draw(pawnShape);
	}

	public void mouseClicked(MouseEvent e) {
		game.pawnSelected(game.getGameState().getActivePlayer(), destination, position);
	}

	public void mouseEntered(MouseEvent e) {
		mouseOver = true;
		repaint();
	}

	public void mouseExited(MouseEvent e) {
		mouseOver = false;
		repaint();
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
	
	public void setPosition(int j) {
		this.position = j;
		
	}

	/**
	 * @return
	 */
	public int getPlayerId() {
		return playerId;
	}

}
