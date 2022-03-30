package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.am54.model.Constants.CARD_FOR_EACH_HAND;
import static org.junit.jupiter.api.Assertions.*;

class HandTest {
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

    @Test
    public void playedCardTest()
    {
        Hand hand = new Hand();
        Card played = hand.getAllCards().get(5);
        hand.setCardPlayed(played);
        assertEquals(played, hand.getCardPlayed());
    }

}