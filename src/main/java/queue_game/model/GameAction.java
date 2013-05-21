package queue_game.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

/**
 * Class containing actions players that may happen when playing, with additional information
 * 
 * @author Szymon
 */
public class GameAction {
	private GameActionType type;
	private int[] info;
	private int player;
	private String message;

	private GameAction() {
	}

	public GameAction(GameActionType type, int... info) {
		this.type = type;
		this.info = Arrays.copyOf(info, info.length);
	}
	
	public GameAction(int player, String message) {
		type = GameActionType.CHAT;
		this.player = player;
		this.message = message;
	}

	/**
	 * Writes current object to the specified OutputStream (most likely a Socket).
	 * 
	 * @param out The stream to write to
	 * @throws IOException when something goes wrong
	 */
	public void write(DataOutputStream out) throws IOException {
		out.write(type.ordinal());
		if(type == GameActionType.CHAT) {
			out.write(player);
			out.writeUTF(message);
		} else {
			out.write(info.length);
			for(int i = 0; i < info.length; ++i)
				out.write(info[i]);
			out.flush();
		}
	}

	private static int readInt(DataInputStream in) throws IOException {
		int i = in.read();
		if(i == -1)
			throw new EOFException("Unexpected end of stream");
		return i;
	}

	/**
	 * Reads an object from specified InputStream (most likely a socket).
	 * This function makes almost no attempt to verify whether the object is correct.
	 * 
	 * @param in the stream to read from
	 * @return the read GameAction object
	 * @throws IOException when something goes wrong
	 * @throws EOFException when the stream ends unexpectedly
	 */
	public static GameAction read(DataInputStream in) throws IOException {
		GameAction action = new GameAction();
		int ordinal = readInt(in);
		if(ordinal >= GameActionType.values().length)
			throw new IOException("Incorrect action numer");
		action.type = GameActionType.values()[ordinal];
		if(action.type == GameActionType.CHAT) {
			action.player = readInt(in);
			action.message = in.readUTF();
		} else {
			action.info = new int[readInt(in)];
			for(int i = 0; i < action.info.length; ++i)
				action.info[i] = readInt(in);
		}
		return action;
	}

	@Override
	public String toString() {
		switch(type) {
		case PAWN_PLACED:
			return "Gracz " + (info[0] + 1) + " dodał pionek do kolejki " + (info[1] + 1) + ".";
		case PRODUCT_DELIVERED:
			return "Dostawa do sklepu " + (info[0] + 1) + ".";
		case CARD_PLAYED:
			return "Gracz " + (info[0] + 1) + " zagrał kartę " + (QueuingCard.values()[info[1]]) + ".";
		case PASSED:
			return "Gracz " + (info[0] + 1) + " spasował.";
		case PRODUCT_BOUGHT:
			return "Gracz " + (info[0] + 1) + " kupił produkt " + (info[1] + 1) + ".";
		case GAME_OVER:
			return "Koniec gry";
		case CHAT:
			return "Czat.";
		default:
			throw new RuntimeException("Unimplemented action");
		}
	}
}
