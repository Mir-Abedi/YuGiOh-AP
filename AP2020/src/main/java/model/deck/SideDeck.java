package model.deck;

import controller.database.CSVInfoGetter;
import model.card.Card;

import java.util.ArrayList;

public class SideDeck extends PrimaryDeck {
    public SideDeck(String deckName){
        maxCapacity = 15;
        minCapacity = 0;
        cards = new ArrayList<>();
        this.deckName = deckName;
    }

    public SideDeck() {
        maxCapacity = 15;
        minCapacity = 0;
    }

    @Override
    public String toString(){
        return "Deck: " + deckName + "\nSide Deck: \n" + PrimaryDeck.sortCardsInDecks(cards);
    }

    @Override
    public SideDeck clone() {
        SideDeck output = new SideDeck(this.getDeckName());
        ArrayList<Card> temp = new ArrayList<>();
        for (Card card : this.getCards()) {
            temp.add(CSVInfoGetter.getCardByName(card.getCardName()));
        }
        output.setCards(temp);
        return output;
    }
}
