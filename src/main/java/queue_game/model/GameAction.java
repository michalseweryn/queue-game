package queue_game.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Class containing actions players that may happen when playing, with additional information
 * 
 * @author Szymon
 */
public class GameAction {
	private GameActionType type;
	private int[] info;

	private GameAction() {
	}

	public GameAction(GameActionType type, int... info) {
		this.type = type;
		this.info = Arrays.copyOf(info, info.length);
	}

	/**
	 * Writes current object to the specified OutputStream (most likely a Socket).
	 * 
	 * @param out The stream to write to
	 * @throws IOException when something goes wrong
	 */
	public void write(OutputStream out) throws IOException {
		out.write(type.ordinal());
		out.write(info.length);
		for(int i = 0; i < info.length; ++i)
			out.write(info[i]);
		out.flush();
	}

	private static int readInt(InputStream in) throws IOException {
		int i = in.read();
		if(i == -1)
			throw new IOException("Unexpected end of stream");
		return i;
	}

	/**
	 * Reads an object from specified InputStream (most likely a socket).
	 * This function makes almost no attempt to verify whether the object is correct.
	 * 
	 * @param in the stream to read from
	 * @return the read GameAction object
	 * @throws IOException when something goes wrong
	 */
	public static GameAction read(InputStream in) throws IOException {
		GameAction action = new GameAction();
		int ordinal = readInt(in);
		if(ordinal >= GameActionType.values().length)
			throw new IOException("Incorrect action numer");
		action.type = GameActionType.values()[ordinal];
		action.info = new int[readInt(in)];
		for(int i = 0; i < action.info.length; ++i)
			action.info[i] = readInt(in);
		return action;
	}
}
