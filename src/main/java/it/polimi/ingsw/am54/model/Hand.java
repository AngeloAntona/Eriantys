package it.polimi.ingsw.am54.model;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.am54.model.Constants.CARD_FOR_EACH_HAND;

public class Hand {
    private final List<Card> cards;
    private Card cardPlayed;

    public Hand() {
        cards = new ArrayList<>();
        fillHand();
    }

    public Card getCardPlayed() {
        return cardPlayed;
    }

    public void setCardPlayed(Card card) {
        this.cardPlayed = card;
    }

    public void removeFromCards(Card card)
    {
        cards.remove(card);
    }

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
