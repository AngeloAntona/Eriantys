package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.am54.model.Constants.CARD_FOR_EACH_HAND;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Set of testes used for checking functionality of class Hand
 */
public class HandTest {
    /**
     * Checks if Hand constructor generates correct card (values and moves)
     * @see  Hand#Hand()
     * @see  Hand#getAllCards()
     * @see Card
     */
    @Test
    public void getAllCardsTest()
    {

        List<Card> expected = new ArrayList<>();

        for(int i = 1; i <= CARD_FOR_EACH_HAND; i++) {
            Card newCard = new Card(i);
            expected.add(newCard);
        }

        Hand hand = new Hand();
        List<Card> actual = hand.getAllCards();
        int i =0;
        for (Card c: expected) {
            assertEquals(c.getValue(), actual.get(i).getValue());
            assertEquals(c.getMaxMoves(), actual.get(i).getMaxMoves());
            i++;
        }
    }

    /**
     * Tests if desired cards can be removed from hand
     * @see  Hand#removeFromCards(Card)
     */
    @Test
    public void removeTest(){
        int rem = 4;

        Hand hand = new Hand();
        List<Card> before = hand.getAllCards();
        hand.removeFromCards(before.get(rem));
        List<Card> after = hand.getAllCards();

        int offset = 0;

        for(int i = 0; i < after.size(); i++){
            if(i >= rem)
                offset = 1;  //when we reach card that is removed in original(before) list we need to skip it
            assertEquals(after.get(i).getValue(), before.get(i+offset).getValue());
            assertEquals(after.get(i).getMaxMoves(), before.get(i+offset).getMaxMoves());
        }
    }

    /**
     * Checks if setting of played card is done correctly
     * @see  Hand#setCardPlayed(Card)
     * @see  Hand#getCardPlayed()
     */
    @Test
    public void playedCardTest()
    {
        Hand hand = new Hand();
        Card played = hand.getAllCards().get(5);
        hand.setCardPlayed(played);
        assertEquals(played, hand.getCardPlayed());
    }

}