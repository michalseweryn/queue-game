package queue_game.model;

import java.util.List;

public interface DecksOfQueuingCardsBoxInterface {
	public List<QueuingCard> getCardsToFillTheHandOfPlayer(int playerNr);
	public boolean hasCard(int playerNr, QueuingCard card);
	public QueuingCard remove(int player);
	public void resetAllDecks();
}
