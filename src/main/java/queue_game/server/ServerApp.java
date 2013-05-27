package queue_game.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerApp {
	public static void main(String[] args) {
		final int tableCount = 1;
		for(int i = 0; i < tableCount; ++i) {
			Thread tableThread = new Thread(new Table(i, 5));
			tableThread.setDaemon(true);
			tableThread.start();
		}
		try {
			ServerSocket listener = new ServerSocket(17373);
			while(!Thread.currentThread().isInterrupted()) {
				try {
					Thread playerThread = new Thread(new PlayerConnection(listener.accept()));
					playerThread.setDaemon(true);
					playerThread.run();
				} catch(IOException e) {
					System.out.println("Nieudane połączenie");
					e.printStackTrace();
				}
			}
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
