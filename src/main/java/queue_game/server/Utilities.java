package queue_game.server;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class Utilities {
	public static <T extends Enum<T>> T readEnum(Reader in, Class<T> enumType) throws IOException {
		return T.valueOf(enumType, readRawString(in));
	}

	public static int readInt(Reader in) throws NumberFormatException, IOException {
		return Integer.valueOf(readRawString(in));
	}

	public static String readRawString(Reader in) throws IOException {
		char c;
		do {
			c = (char) in.read();
		} while(Character.isWhitespace(c));
		StringBuilder sb = new StringBuilder();
		while(true) {
			c = (char) in.read();
			if(Character.isWhitespace(c)) {
				break;
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	public static void expectString(Reader in, String s) throws IOException {
		String r;
		do {
			r = readRawString(in);
		} while(!s.equalsIgnoreCase(r));
	}

	public static String readString(Reader in) throws IOException {
		int length = readInt(in);
		StringBuilder sb = new StringBuilder(length);
		while(length-- > 0) {
			sb.append((char) in.read());
		}
		return sb.toString();
	}

	public static void writeObject(Writer out, Object o) throws IOException {
		out.write(o.toString());
		out.write(' ');
	}
	
	public static void writeRawString(Writer out, String e) throws IOException {
		out.write(e);
		out.write(' ');
	}

	public static void writeString(Writer out, String e) throws IOException {
		out.write(e.length());
		out.write(' ');
		writeRawString(out, e);
	}
	
	public static void finishWriting(Writer out) throws IOException {
		out.write('\n');
		out.flush();
	}
}
