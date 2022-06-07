import controller.database.CSVInfoGetter;
import model.card.Card;
import model.deck.Deck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DeckTest {
    @Test
    public void deckTest() {
        Deck deck = new Deck("test");
        Card card1 = CSVInfoGetter.getCardByName("Battle OX");
        Card card2 = CSVInfoGetter.getCardByName("Battle OX");
        Card card3 = CSVInfoGetter.getCardByName("Battle OX");
        Card card4 = CSVInfoGetter.getCardByName("Battle OX");
        deck.addCardToMainDeck(card1);
        deck.addCardToMainDeck(card2);
        deck.addCardToSideDeck(card3);

        Assertions.assertNotNull(card1);
        Assertions.assertNotNull(card2);
        Assertions.assertNotNull(card3);
        Assertions.assertNotNull(card4);
        Assertions.assertFalse(deck.canAddCardByName(card4.getCardName()));
        Assertions.assertTrue(deck.getMainDeck().getCards().contains(card1));
        Assertions.assertTrue(deck.getMainDeck().getCards().contains(card2));
        Assertions.assertTrue(deck.getSideDeck().getCards().contains(card3));
        Assertions.assertEquals(3,deck.getNumberOfCardsByName(card1.getCardName()));
        Assertions.assertFalse(deck.canAddCardByName(card1.getCardName()));
        Assertions.assertFalse(deck.doesMainDeckHasCard("ali"));
        Assertions.assertTrue(deck.doesMainDeckHasCard(card1.getCardName()));
        Assertions.assertTrue(deck.doesSideDeckHasCard(card1.getCardName()));
        Assertions.assertEquals("test",deck.getDeckName());
        Assertions.assertFalse(deck.isValid());


        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < 40; i++) cards.add(CSVInfoGetter.getCardByName("Command Knight"));
        deck.removeCardFromMainDeck(card1.getCardName());
        deck.removeCardFromSideDeck(card3.getCardName());
        deck.getMainDeck().getCards().addAll(cards);
        Deck deck1 = deck.clone();


        Assertions.assertFalse(deck.isValid());
        Assertions.assertTrue(deck.getMainDeck().isValid());
        Assertions.assertTrue(deck.getSideDeck().isValid());
        Assertions.assertFalse(deck.getSideDeck().getCards().contains(card3));
        Assertions.assertEquals(1,deck.getNumberOfCardsByName(card1.getCardName()));
        Assertions.assertEquals(40,deck.getNumberOfCardsByName(cards.get(0).getCardName()));
        Assertions.assertEquals(deck.getDeckName(),deck1.getDeckName());
        Assertions.assertEquals(deck.getAllCards().size(),deck1.getAllCards().size());
        Assertions.assertEquals(deck.getMainDeck().getCardCount(cards.get(0).getCardName()),deck1.getMainDeck().getCardCount(cards.get(0).getCardName()));
        Assertions.assertEquals(deck.getMainDeck().getCardCount(card1.getCardName()),deck1.getMainDeck().getCardCount(card1.getCardName()));
        Assertions.assertEquals(deck.getSideDeck().getCards().size(),deck1.getSideDeck().getCards().size());
    }

    @Test
    public void testDecksSetterAndGetter(){
        Deck testDeck = new Deck("testDeck");
        testDeck.setDeckName("test");
        testDeck.setMainDeck(null);
        testDeck.setSideDeck(null);
        Assertions.assertEquals("test", testDeck.getDeckName());
        Assertions.assertNull(testDeck.getMainDeck());
        Assertions.assertNull(testDeck.getSideDeck());

    }
}
