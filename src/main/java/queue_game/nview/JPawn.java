package queue_game.nview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

import queue_game.controller.Game;
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
	private double pawnDistance;
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
			double pawnHeight) {
		this.destination = product;
		this.game = game;
		this.playerId = playerId;
		this.position = place;
		this.pawnHeight = pawnHeight;
		this.pawnDistance = 3 * pawnHeight / 8;
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
	protected void paintComponent(Graphics g) {
		//System.out.println("pionek" + getBounds());
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(GameState.playerColors[playerId + 1]);
		generateShape();
		g2d.fill(pawnShape);
		g2d.setColor(Color.black);
		g2d.setStroke(new BasicStroke((float) (pawnHeight * STROKE)));
		g2d.draw(pawnShape);
	}

	public void mouseClicked(MouseEvent e) {
		if (pawnShape.contains(e.getPoint())){
			//System.out.println("Kliknięty " + position + " w kolejce do " + destination);
			//PAWN  (position) SELECTED, you should inform about it the controller.
			return;
		}
		if(position == 0)
			return;
		AffineTransform aT = AffineTransform.getTranslateInstance(0, pawnDistance);
		Point p = new Point();
		if (pawnShape.contains(aT.transform(e.getPoint(), p))){
			//System.out.println("Kliknięty " + (position - 1) + " w kolejce do " + destination);
			//PAWN  (position - 1) SELECTED, you should inform about it the controller.
			return;
		}
		if(position == 1)
			return;
		e.getPoint().translate(0, (int) pawnDistance);
		if (pawnShape.contains(e.getPoint())){
			//System.out.println("Kliknięty " + (position - 2) + " w kolejce do " + destination);
			//PAWN  (position - 2) SELECTED, you should inform about it the controller.
			return;
		}
		game.pawnSelected(game.getGameState().getActivePlayer(), destination,position);
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
	
	public void setPosition(int j) {
		this.position = j;
		
	}
	
	public void setPawnDistace(double dist) {
		this.pawnDistance = dist;
		
	}

}
