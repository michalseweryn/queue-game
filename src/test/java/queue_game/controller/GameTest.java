package queue_game.controller;

import queue_game.View;
import queue_game.model.GamePhase;
import queue_game.model.ProductType;
import queue_game.view.JBoard;
import junit.framework.TestCase;

public class GameTest extends TestCase {
	static class FakeView implements View{
		private Game game;

		private int updatesCounter = 0;
		
		FakeView(Game game){
			this.game = game;
		}
		private void selectQueue(int player, ProductType type){
			assertEquals(game.getGameState().getCurrentGamePhase(), GamePhase.QUEUING_UP);
			game.queueSelected(player, type);
		}
		public void update() {
<<<<<<< HEAD
=======
			System.out.println("update " + updatesCounter);
>>>>>>> branch 'master' of https://github.com/michalseweryn/queue-game.git
			switch(updatesCounter){
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				if(updatesCounter%2 == 0)
					selectQueue(0, ProductType.KIOSK);
				else
					selectQueue(1, ProductType.RTV_AGD);
				break;
			default:
				Thread.currentThread().interrupt();
				
			}
			updatesCounter++;
			
		}
		public int getUpdates(){
			return updatesCounter;
		}
		
	}
	public void testQueueSelected() throws InterruptedException {
		Game game = new Game();
		FakeView fakeView = new FakeView(game);
		game.addView(fakeView);
		game.startGame(2);
		game.getGameThread().join();
		assertEquals(11, fakeView.getUpdates());
	}

}
