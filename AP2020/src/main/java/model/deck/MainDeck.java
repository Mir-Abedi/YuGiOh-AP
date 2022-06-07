package model.deck;

import controller.database.CSVInfoGetter;
import model.card.Card;

import java.util.ArrayList;

public class MainDeck extends PrimaryDeck {
    public MainDeck(String deckName) {
        maxCapacity = 60;
        minCapacity = 40;
        cards = new ArrayList<>();
        this.deckName = deckName;
    }

    public MainDeck() {
        maxCapacity = 60;
        minCapacity = 40;
    }

    @Override
    public String toString() {
        return "Deck: " + deckName + "\nmain.Main Deck: \n" + PrimaryDeck.sortCardsInDecks(cards);
    }

    @Override
    public MainDeck clone() {
        MainDeck output = new MainDeck(this.getDeckName());
        ArrayList<Card> temp = new ArrayList<>();
        for (Card card : this.getCards()) {
            temp.add(CSVInfoGetter.getCardByName(card.getCardName()));
        }
        output.setCards(temp);
        return output;
    }
}
