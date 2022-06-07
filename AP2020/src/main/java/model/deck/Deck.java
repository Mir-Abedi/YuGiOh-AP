package model.deck;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.card.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Deck implements Cloneable {
    private MainDeck mainDeck;
    private SideDeck sideDeck;
    private String deckName;


    public Deck(String deckName) {
        this.deckName = deckName;
        mainDeck = new MainDeck(deckName);
        sideDeck = new SideDeck(deckName);
    }

    @Override
    public Deck clone() {
        Deck outputDeck = new Deck(this.getDeckName());
        outputDeck.setMainDeck(this.getMainDeck().clone());
        outputDeck.setSideDeck(this.getSideDeck().clone());
        return outputDeck;
    }

    public void addCardToMainDeck(Card card) {
        mainDeck.addCard(card);
    }

    public void addCardToSideDeck(Card card) {
        sideDeck.addCard(card);
    }

    @JsonIgnore
    public int getNumberOfCardsByName(String cardName) {
        return mainDeck.getCardCount(cardName) + sideDeck.getCardCount(cardName);
    }

    public boolean canAddCardByName(String cardName) {
        return getNumberOfCardsByName(cardName) < 3;
    }

    public Card removeCardFromMainDeck(String cardName) {
        if (doesMainDeckHasCard(cardName))
            return mainDeck.removeCard(cardName);
        return null;
    }

    public Card removeCardFromSideDeck(String cardName) {
        if (doesSideDeckHasCard(cardName))
            return sideDeck.removeCard(cardName);
        return null;
    }

    public boolean doesMainDeckHasCard(String cardName) {
        return mainDeck.getCardCount(cardName) > 0;
    }

    public boolean doesSideDeckHasCard(String cardName) {
        return sideDeck.getCardCount(cardName) > 0;
    }


    public String allCardsToString() { // todo bayad bere too deck
        ArrayList<Card> cards = getAllCards();
        StringBuilder temp = new StringBuilder();
        HashMap<String, String> cardNameToDescription = new HashMap<>();
        for (Card card : cards) cardNameToDescription.put(card.getCardName(), card.getDescription());
        ArrayList<String> sortedCardNames = PrimaryDeck.sortCardsByName(cards);
        for (String tempString : sortedCardNames)
            temp.append(tempString + ":" + cardNameToDescription.get(tempString) + "\n");
        return temp.toString();
    }

    @JsonIgnore
    public ArrayList<Card> getAllCards() {
        ArrayList<Card> temp;
        ArrayList<Card> output = new ArrayList<>();
        temp = mainDeck.getCards();
        for (Card card : temp) output.add(card);
        temp = sideDeck.getCards();
        for (Card card : temp) output.add(card);
        return output;
    }

    public String getDeckName() {
        return deckName;
    }

    public String showMainDeck() {
        return mainDeck.toString();
    }

    public String showSideDeck() {
        return sideDeck.toString();
    }

    public MainDeck getMainDeck() {
        return this.mainDeck;
    }

    public SideDeck getSideDeck() {
        return this.sideDeck;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public void setMainDeck(MainDeck mainDeck) {
        this.mainDeck = mainDeck;
    }

    public void setSideDeck(SideDeck sideDeck) {
        this.sideDeck = sideDeck;
    }


    public Deck() {

    }

    @JsonIgnore
    public boolean isValid() {
        HashMap<String, Integer> temp = new HashMap<>();
        ArrayList<Card> tempCards = new ArrayList<>(mainDeck.cards);
        tempCards.addAll(sideDeck.cards);
        for (Card card : tempCards) {
            if (temp.containsKey(card.getCardName())) {
                temp.put(card.getCardName(), temp.get(card.getCardName()) + 1);
            } else {
                temp.put(card.getCardName(), 1);
            }
        }
        for (String key : temp.keySet()) if (temp.get(key) > 3) return false;
        return mainDeck.isValid() && sideDeck.isValid();
    }
}
