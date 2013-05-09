package queue_game.controller;

import queue_game.model.GamePhase;
import queue_game.model.ProductType;
import queue_game.view.JBoard;
import junit.framework.TestCase;

public class GameTest extends TestCase {
	Game g = new Game();
	JBoard b = new JBoard(g);
	public void testQueueSelected() {
		g.addView(b);
		g.startGame(2);
		g.queueSelected(0, ProductType.KIOSK);
		g.queueSelected(1, ProductType.RTV_AGD);
		g.queueSelected(0, ProductType.KIOSK);
		g.queueSelected(1, ProductType.RTV_AGD);
		g.queueSelected(0, ProductType.KIOSK);
		assertEquals(g.getGameState().getCurrentGamePhase(),(GamePhase.QUEUING_UP));
		g.queueSelected(1, ProductType.RTV_AGD);
		g.queueSelected(0, ProductType.KIOSK);
		g.queueSelected(1, ProductType.RTV_AGD);
		g.queueSelected(0, ProductType.KIOSK);
		g.queueSelected(1, ProductType.RTV_AGD);
		assertEquals(g.getGameState().getCurrentGamePhase(),(GamePhase.DELIVERY));
	}

}
