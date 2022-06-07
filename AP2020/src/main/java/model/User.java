package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.card.Card;
import model.deck.Deck;
import controller.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class User {
    @JsonIgnore
    public static final int NUMBER_OF_PROFILES = 5;
    private String username;
    private String nickname;
    private String password;
    private int balance;
    private int score;
    private int profilePhoto;
    private ArrayList<Card> cards;
    private ArrayList<Deck> decks;
    private Deck activeDeck;
    
    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.balance = 0;
        this.score = 0;
        this.nickname = nickname;
        this.profilePhoto = 0;
        this.cards = new ArrayList<>();
        this.decks = new ArrayList<>();
        activeDeck  = null;
    }

    public User(){

    }

    public void setActiveDeck(Deck activeDeck) {
        this.activeDeck = activeDeck;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setProfilePhoto(int profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Deck> getDecks(){
        return decks;
    }

    public Deck getActiveDeck() {
        return activeDeck;
    }

    public int getScore() {
        return score;
    }

    public int getBalance() {
        return balance;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public int getProfilePhoto() {
        return profilePhoto;
    }

    public boolean isPasswordCorrect(String password) {
        return this.password.equals(password);
    }

    public void activeDeck(String deckName) {
        if (getDeckByName(deckName) != null)
            activeDeck = getDeckByName(deckName);
        else activeDeck = null;
    }

    public Deck getDeckByName(String deckName) {
        for (Deck deck : decks) {
            if (deck.getDeckName().equals(deckName)) return deck;
        }
        return null;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public Card removeCard(String cardName){
        Card card = getCardByName(cardName);
        cards.remove(card);
        return card;
    }

    @JsonIgnore
    public Card getCardByName(String cardName) {
        if (cardName == null) return null;
        ArrayList<Card> cards = getCards();
        for (Card card : cards) {
            if (cardName.equals(card.getCardName())) return card;
        }
        return null;
    }

    public void createDeck(String deckName) {
        decks.add(new Deck(deckName));
    }

    public void changePassword(String newPassword) {
        password = newPassword;
    }

    public void changeNickname(String newNickname) {
        nickname = newNickname;
    }

    public void increaseBalance(int amount) {
        balance += amount;
    }

    public void decreaseBalance(int amount) {
        balance -= amount;
    }



    public boolean hasEnoughBalance(int amount) {
        return balance >= amount;
    }

    public void addCardToMainDeck(Card card, String deckName) {
        Deck deck = getDeckByName(deckName);
        if (deck.canAddCardByName(card.getCardName())) {
           deck.addCardToMainDeck(card);
        }
    }

    public void addCardToSideDeck(Card card, String deckName) {
        Deck deck = getDeckByName(deckName);
        if (deck.canAddCardByName(card.getCardName()))
            deck.addCardToSideDeck(card);
    }

    public void removeCardFromMainDeck(String cardName,String deckName){
        if (doesDeckExist(deckName)){
            Deck deck = getDeckByName(deckName);
            if (deck.doesMainDeckHasCard(cardName)) {
                Card card = deck.removeCardFromMainDeck(cardName);
                cards.add(card);
            }
        }
    }

    public void removeCardFromSideDeck(String cardName,String deckName){
        if (doesDeckExist(deckName)){
            Deck deck = getDeckByName(deckName);
            if (deck.doesSideDeckHasCard(cardName)) {
                Card card = deck.removeCardFromSideDeck(cardName);
                cards.add(card);
            }
        }
    }

    public void removeDeck(String deckName) {
        Deck deck = getDeckByName(deckName);
        if (deck != null) {
            ArrayList<Card> cardsOdThisDeck = deck.getAllCards();
            cards.addAll(cardsOdThisDeck);
            decks.remove(deck);
            if (deck == activeDeck) activeDeck = null;
        }
    }

    public boolean doesDeckExist(String deckName) {
        return getDeckByName(deckName) != null;
    }

    @JsonIgnore
    public ArrayList<Deck> getSortedDecks() {
        ArrayList<Deck> decks = (ArrayList<Deck>) this.decks.clone();
        decks.remove(activeDeck);
        Collections.sort(decks, new sortDeckByName());
        return decks;
    }



    class sortDeckByName implements Comparator<Deck> {
        @Override
        public int compare(Deck deck1, Deck deck2) {
            String tempName1 = deck1.getDeckName(), tempName2 = deck2.getDeckName();
            return DeckMenuController.compareStrings(tempName1, tempName2);
        }
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }



}
