package queue_game.model;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

import queue_game.server.Utilities;

/**
 * Class containing actions players that may happen when playing, with additional information
 * 
 * @author Szymon
 */
public class GameAction {
	private GameActionType type;
	private Object[] info;

	private GameAction() {
	}

	public GameAction(GameActionType type, Object... info) {
		this.type = type;
		this.info = Arrays.copyOf(info, info.length);
	}

	/**
	 * Writes current object to the specified Writer.
	 * 
	 * @param out The stream to write to
	 * @throws IOException when something goes wrong
	 */
	public void write(Writer out) throws IOException {
		System.out.println(this);
		Utilities.writeObject(out, type);
		for(Object o: info) {
			System.out.println("piszemy " + o);
			if(o instanceof String) {
				Utilities.writeString(out, (String) o);
			} else {
				Utilities.writeObject(out, o);
			}
		}
		Utilities.finishWriting(out);
	}

	/**
	 * Reads an object from the specified Reader.
	 * This function makes almost no attempt to verify whether the object is correct.
	 * 
	 * @param in the stream to read from
	 * @return the read GameAction object
	 * @throws IOException when something goes wrong
	 * @throws EOFException when the stream ends unexpectedly
	 */
	public static GameAction read(Reader in) throws IOException {
		GameAction action = new GameAction();
		action.type = Utilities.readEnum(in, GameActionType.class);
		switch(action.type) {
		case JOIN:
			action.info = new Object[2];
			action.info[0] = Utilities.readInt(in); //numer gracza
			action.info[1] = Utilities.readString(in); //imie gracza
		case START_GAME:
			action.info = new Object[0];
			break;
		case ERROR:
			action.info = new Object[1];
			action.info[0] = Utilities.readInt(in); //numer gracza
			break;
		case DRAW_CARD:
			action.info = new Object[2];
			action.info[0] = Utilities.readInt(in); //ktory gracz
			action.info[1] = Utilities.readEnum(in, QueuingCard.class); //ktora karta
			break;
		case PAWN_PLACED:
			action.info = new Object[2];
			action.info[0] = Utilities.readInt(in); //ktory gracz
			action.info[1] = Utilities.readEnum(in, ProductType.class); //ktora kolejka
			break;
		case PRODUCT_DELIVERED:
		case PRODUCT_BOUGHT:
			action.info = new Object[2];
			action.info[0] = Utilities.readInt(in); //ktory sklep
			action.info[1] = Utilities.readEnum(in, ProductType.class); //ktory produkt
			break;
		case CARD_PLAYED:
			int player = Utilities.readInt(in); //ktory gracz
			QueuingCard card = Utilities.readEnum(in, QueuingCard.class); //ktora karta
			switch(card) {
			case DELIVERY_ERROR:
				action.info = new Object[5];
				action.info[2] = Utilities.readEnum(in, ProductType.class); //ktory sklep
				action.info[3] = Utilities.readEnum(in, ProductType.class); //sklep docelowy
				action.info[4] = Utilities.readEnum(in, ProductType.class); //typ produktu
				break;
			case MOTHER_WITH_CHILD:
			case NOT_YOUR_PLACE:
			case CRITICIZING_AUTHORITIES:
				action.info = new Object[4];
				action.info[2] = Utilities.readEnum(in, ProductType.class); //ktory sklep
				action.info[3] = Utilities.readInt(in); //ktory pionek
				break;
			case LUCKY_STRIKE:
				action.info = new Object[5];
				action.info[2] = Utilities.readEnum(in, ProductType.class); //ktory sklep
				action.info[3] = Utilities.readInt(in); //ktory pionek
				action.info[4] = Utilities.readEnum(in, ProductType.class); //sklep docelowy
				break;
			case TIPPING_FRIEND:
				action.info = new Object[2];
				break;
			case CLOSED_FOR_STOCKTAKING:
			case INCREASED_DELIVERY:
			case COMMUNITY_LIST:
				action.info = new Object[3];
				action.info[2] = Utilities.readEnum(in, ProductType.class); //ktory sklep
				break;
			case UNDER_THE_COUNTER_GOODS:
				action.info = new Object[4];
				action.info[2] = Utilities.readEnum(in, ProductType.class); // ktory produkt
				action.info[3] = Utilities.readEnum(in, ProductType.class); // ktory sklep
			default:
				throw new RuntimeException("Unimplemented card");
			}
			action.info[0] = player;
			action.info[1] = card;
			break;
		case CARDS_PEEKED:
			action.info = new Object[2];
			ProductType product = Utilities.readEnum(in, ProductType.class);
			int amount = Utilities.readInt(in);
			action.info[0] = new DeliveryCard(product, amount);
			product = Utilities.readEnum(in, ProductType.class);
			amount = Utilities.readInt(in);
			action.info[1] = new DeliveryCard(product, amount); //2 karty dostawy
			//nie ma numeru gracza bo ta akcja zawsze trafia tylko do gracza ktory podejrzal karty
			break;
		case PAWN_REMOVED:
			action.info = new Object[3];
			action.info[0] = Utilities.readInt(in); //ktory gracz
			action.info[1] = Utilities.readInt(in); //ktora kolejka
			action.info[2] = Utilities.readInt(in); //ktory pionek
			break;
		case PRODUCT_EXCHANGED_ONE:
			action.info = new Object[3];
			action.info[0] = Utilities.readInt(in); //ktory gracz
			action.info[1] = Utilities.readEnum(in, ProductType.class); //ktory produkt dostal
			action.info[2] = Utilities.readEnum(in, ProductType.class); //ktory produkt oddal
			break;
		case PRODUCT_EXCHANGED_TWO:
			action.info = new Object[4];
			action.info[0] = Utilities.readInt(in); //ktory gracz
			action.info[1] = Utilities.readEnum(in, ProductType.class); //ktory produkt dostal
			action.info[2] = Utilities.readEnum(in, ProductType.class);
			action.info[3] = Utilities.readEnum(in, ProductType.class); //ktore 2 produkty oddal
			break;
		case CHAT:
			action.info = new Object[2];
			action.info[0] = Utilities.readInt(in); //ktory gracz
			action.info[1] = Utilities.readString(in); //wiadomosc
			break;
		case CARD_PLAYED_PASSED:
		case PAWN_REMOVED_PASSED:
		case PRODUCT_EXCHANGED_PASSED:
			action.info = new Object[1];
			action.info[0] = Utilities.readInt(in); //ktory gracz
		default:
			throw new RuntimeException("Action unimplemented");
		}
		return action;
	}

	@Override
	public String toString() {
		return type + " " + Arrays.asList(info);
	}

	/**
	 * @return
	 */
	public GameActionType getType() {
		return type;
	}

	/**
	 * @return
	 */
	public Object[] getInfo() {
		return info;
	}
}
