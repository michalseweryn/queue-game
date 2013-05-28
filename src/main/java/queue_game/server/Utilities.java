package queue_game.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class Utilities {
	public static <T extends Enum<T>> T readEnum(Reader in, Class<T> enumType) throws IOException {
		while(true) {
			try {
				String name = readRawString(in);
				if(name.equals("null"))
					return null;
				return T.valueOf(enumType, name);
			} catch(IllegalArgumentException e) {
			}
		}
	}

	public static int readInt(Reader in) throws IOException {
		while(true) {
			try {
				return Integer.valueOf(readRawString(in));
			} catch(NumberFormatException e) {
			}
		}
	}

	public static String readRawString(Reader in) throws IOException {
		int i;
		char c;
		do {
			i = in.read();
			if(i == -1) {
				throw new EOFException("Unexpected end of stream");
			}
			c = (char) i;
		} while(Character.isWhitespace(c));
		StringBuilder sb = new StringBuilder().append(c);
		while(true) {
			i = in.read();
			c = (char) i;
			if(i == -1) {
				throw new EOFException("Unexpected end of stream");
			}
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
			int i = in.read();
			if(i == -1) {
				throw new EOFException("Unexpected end of stream");
			}
			sb.append((char) i);
		}
		return sb.toString();
	}

	public static void writeObject(Writer out, Object o) throws IOException {
		if(o == null)
			out.write("null");
		else
			out.write(o.toString());
		out.write(' ');
	}
	
	public static void writeRawString(Writer out, String e) throws IOException {
		out.write(e);
		out.write(' ');
	}

	public static void writeString(Writer out, String e) throws IOException {
		
		out.write(((Integer)e.length()).toString());
		out.write(' ');
		writeRawString(out, e);
	}
	
	public static void finishWriting(Writer out) throws IOException {
		out.write('\n');
		out.flush();
	}
}
