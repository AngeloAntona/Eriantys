package it.polimi.ingsw.am54.model;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.am54.model.Constants.CARD_FOR_EACH_HAND;

/**
 * Class Hand serves as container for Assistant cards available to player.<br>
 * It also contains method that allows player to select next card to play.
 */
public class Hand {
    private final List<Card> cards;
    private Card cardPlayed;

    /**
     * Constructs Hand and initializes list of card, and fills it whit cards
     */
    public Hand() {
        cards = new ArrayList<>();
        fillHand();
    }

    /**
     * Reruns card that has been played
     * @return Assistant card
     */
    public Card getCardPlayed() {
        return cardPlayed;
    }

    /**
     * Allows setting card to be played
     * @param card Assistant card
     */
    public void setCardPlayed(Card card) {
        this.cardPlayed = card;
    }

    /**
     * Removes card from the list of available cards
     * @param card Assistant card
     */
    public void removeFromCards(Card card)
    {
        cards.remove(card);
    }

    /**
     * Returns list of all available cards
     * @return list of cards
     */
    public List<Card> getAllCards() {
        return List.copyOf(cards);
    }

    private void fillHand()
    {
        for(int i = 1; i <= CARD_FOR_EACH_HAND; i++) {
            Card newCard = new Card(i);
            cards.add(newCard);
        }
    }
}
