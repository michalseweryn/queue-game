package queue_game.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestApp {
	public static void main(String[] args) {
		String host = "127.0.0.1";
		if(args.length > 0) {
			host = args[0];
		}
		Socket connection = null;
		try {
			connection = new Socket(host, 17373);
			final Reader in = new InputStreamReader(connection.getInputStream());
			Writer out = new OutputStreamWriter(connection.getOutputStream());
			Reader sysIn = new InputStreamReader(System.in);
			Thread reader = new Thread(new Runnable() {
				boolean firstChar = true;
				public void run() {
					try {
						while(true) {
							int i = in.read();
							if(i == -1)
								break;
							char c = (char) i;
							if(firstChar){
								System.out.print(">");
								firstChar = false;
							}
							System.out.print(c);
							if(i == '\n')
								firstChar = true;
						}
						
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			});
			reader.setDaemon(true);
			reader.start();
			while(!Thread.currentThread().isInterrupted()) {
				out.write((char) sysIn.read());
				out.flush();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
