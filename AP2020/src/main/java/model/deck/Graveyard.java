package model.deck;

import controller.database.CSVInfoGetter;
import model.card.Card;

import java.util.ArrayList;


public class Graveyard extends PrimaryDeck {
    public Graveyard() {
        cards = new ArrayList<>();
        maxCapacity = 0;
        minCapacity = 0;
    }

    public String toString(String deckName) {
        return null;
    } //ToDo in che kossherie??

    public String toString() {
        if (cards.size() == 0) {
            return "Graveyard is Empty";
        }
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < cards.size(); i++)
            temp.append((i + 1) + ". " + cards.get(i).getCardName() + " : " + cards.get(i).getDescription());
        return temp.toString();
    }

    @Override
    public Graveyard clone() {
        Graveyard output = new Graveyard();
        ArrayList<Card> temp = new ArrayList<>();
        for (Card card : this.getCards()) {
            temp.add(CSVInfoGetter.getCardByName(card.getCardName()));
        }
        output.setCards(temp);
        return output;
    }
}
