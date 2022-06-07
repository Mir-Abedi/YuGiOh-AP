package controller;
import controller.database.ReadAndWriteDataBase;
import controller.database.CSVInfoGetter;
import model.card.spell_traps.Limitation;
import model.card.spell_traps.Spell;
import model.card.spell_traps.Trap;
import view.responses.DeckMenuResponses;
import model.*;
import model.deck.*;
import model.card.*;
import view.DeckMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DeckMenuController {

    public static DeckMenuResponses createDeck(String deckName) {
        User user = LoginMenuController.getCurrentUser();
        Deck tempDeck = user.getDeckByName(deckName);
        if (tempDeck != null) return DeckMenuResponses.DECK_ALREADY_EXISTS;
        user.createDeck(deckName);
        ReadAndWriteDataBase.updateUser(user);
        return DeckMenuResponses.SUCCESSFUL;
    }

    public static DeckMenuResponses deleteDeck(String deckName){
        User user = LoginMenuController.getCurrentUser();
        if (!user.doesDeckExist(deckName)) return DeckMenuResponses.DECK_DOESNT_EXIST;
        user.removeDeck(deckName);
        ReadAndWriteDataBase.updateUser(user);
        return DeckMenuResponses.SUCCESSFUL;
    }

    public static DeckMenuResponses activateDeck(String deckName){
        User user = LoginMenuController.getCurrentUser();
        if (!user.doesDeckExist(deckName)) return DeckMenuResponses.DECK_DOESNT_EXIST;
        user.activeDeck(deckName);
        ReadAndWriteDataBase.updateUser(user);
        return DeckMenuResponses.SUCCESSFUL;
    }

    public static DeckMenuResponses addCardToMainDeck(String deckName, String cardName) {
        User user = LoginMenuController.getCurrentUser();
        if (user.doesDeckExist(deckName)) {
            PrimaryDeck primaryDeck = user.getDeckByName(deckName).getMainDeck();
            Deck deck = user.getDeckByName(deckName);
            return DeckMenuController.addCardToDeck(deckName, cardName, primaryDeck, deck);
        } else return DeckMenuResponses.DECK_DOESNT_EXIST;
    }

    public static DeckMenuResponses addCardToSideDeck(String deckName, String cardName) {
        User user = LoginMenuController.getCurrentUser();
        if (!user.doesDeckExist(deckName)) return DeckMenuResponses.DECK_DOESNT_EXIST;
        return addCardToDeck(deckName, cardName, user.getDeckByName(deckName).getSideDeck(), user.getDeckByName(deckName));
    }

    private static DeckMenuResponses addCardToDeck(String deckName, String cardName, PrimaryDeck primaryDeck, Deck deck) {
        User user = LoginMenuController.getCurrentUser();
        if (!CSVInfoGetter.cardNameExists(cardName)) return DeckMenuResponses.CARD_DOESNT_EXIST;
        if (!arrayContainsCard(cardName, user.getCards())) return DeckMenuResponses.CARD_DOESNT_EXIST;
        if (!user.doesDeckExist(deckName)) return DeckMenuResponses.DECK_DOESNT_EXIST;
        if (!primaryDeck.hasCapacity()) {
            if (primaryDeck instanceof MainDeck) return DeckMenuResponses.MAIN_DECK_IS_FULL;
            else return DeckMenuResponses.SIDE_DECK_IS_FULL;
        }
        if (!canAddCard(deckName,cardName)) return DeckMenuResponses.CANT_ADD_MORE_OF_THIS_CARD;
        Card card = user.removeCard(cardName);
        primaryDeck.addCard(card);
        ReadAndWriteDataBase.updateUser(user);
        return DeckMenuResponses.SUCCESSFUL;
    }

    private static boolean arrayContainsCard(String cardName, ArrayList<Card> cards) {
        for (Card card : cards) if (card.getCardName().equals(cardName)) return true;
        return false;
    }

    public static DeckMenuResponses removeCardFromMainDeck(String deckName, String cardName) {
        User user = LoginMenuController.getCurrentUser();
        if (!user.doesDeckExist(deckName)) return DeckMenuResponses.DECK_DOESNT_EXIST;
        return removeCardFromDeck(deckName, cardName, user.getDeckByName(deckName).getMainDeck(), user.getDeckByName(deckName));
    }

    public static DeckMenuResponses removeCardFromSideDeck(String deckName, String cardName) {
        User user = LoginMenuController.getCurrentUser();
        if (!user.doesDeckExist(deckName)) return DeckMenuResponses.DECK_DOESNT_EXIST;
        return removeCardFromDeck(deckName, cardName, user.getDeckByName(deckName).getSideDeck(), user.getDeckByName(deckName));
    }

    public static String showAllDecks() {
        User user = LoginMenuController.getCurrentUser();
        ArrayList<Deck> temp = user.getSortedDecks();
        StringBuilder outputString = new StringBuilder("Decks:\nActive Deck:\n");
        Deck activeDeck = user.getActiveDeck();
        if (activeDeck != null) {
            outputString.append(deckToString(activeDeck) + "\n");
        }
        outputString.append("Other decks :\n");
        for (Deck deck : temp) {
            outputString.append(deckToString(deck) + "\n");
        }
        return outputString.toString();
    }

    public static DeckMenuResponses showSideDeck(String deckName) {
        User user = LoginMenuController.getCurrentUser();
        if (!user.doesDeckExist(deckName)) return DeckMenuResponses.DECK_DOESNT_EXIST;
        DeckMenu.showMessage(user.getDeckByName(deckName).getSideDeck().toString());
        return DeckMenuResponses.SUCCESSFUL;
    }

    public static DeckMenuResponses showMainDeck(String deckName) {
        User user = LoginMenuController.getCurrentUser();
        if (!user.doesDeckExist(deckName)) return DeckMenuResponses.DECK_DOESNT_EXIST;
        DeckMenu.showMessage(user.getDeckByName(deckName).getMainDeck().toString());
        return DeckMenuResponses.SUCCESSFUL;
    }

    public static String showAllCards() {
        StringBuilder outputString = new StringBuilder();
        User user = LoginMenuController.getCurrentUser();
        ArrayList<Card> cards = user.getCards();
        Collections.sort(cards, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                String tempName1 = c1.getCardName(), tempName2 = c2.getCardName();
                return compareStrings(tempName1, tempName2);
            }
        }); //ToDo
        for (Card card : cards) outputString.append(card.getCardName() + " : " + card.getDescription() + "\n");
        return outputString.toString();
    }

    public static int compareStrings(String tempName1, String tempName2) {
        if (tempName1.equals(tempName2)) return 0;
        ArrayList<String> temp = new ArrayList<>();
        temp.add(tempName1);
        temp.add(tempName2);
        Collections.sort(temp);
        if (temp.get(0).equals(tempName1)) return 1;
        return -1;
    }

    private static DeckMenuResponses removeCardFromDeck(String deckName, String cardName, PrimaryDeck primaryDeck, Deck deck) {
        User user = LoginMenuController.getCurrentUser();
        if (!user.doesDeckExist(deckName)) return DeckMenuResponses.DECK_DOESNT_EXIST;
        if (!CSVInfoGetter.cardNameExists(cardName)) return DeckMenuResponses.CARD_DOESNT_EXIST;
        if (!arrayContainsCard(cardName, primaryDeck.getCards())) return DeckMenuResponses.CARD_DOESNT_EXIST;
        Card card = primaryDeck.removeCard(cardName);
        user.addCard(card);
        ReadAndWriteDataBase.updateUser(user);
        return DeckMenuResponses.SUCCESSFUL;
    }

    private static String deckToString(Deck deck) {
        StringBuilder outputString = new StringBuilder();
        outputString.append(deck.getDeckName() + ": main.Main Deck " + deck.getMainDeck().getNumberOfAllCards());
        outputString.append(", side deck " + deck.getSideDeck().getNumberOfAllCards() + ", ");
        outputString.append(deck.isValid() ? "valid":"invalid");
        return outputString.toString();
    }

    public static boolean canAddCard(String deckName, String cardName) {
        int numberOfCard = LoginMenuController.getCurrentUser().getDeckByName(deckName).getNumberOfCardsByName(cardName);
        Card card = CSVInfoGetter.getCardByName(cardName);
        if (card.isMonster()) return numberOfCard < 3;
        else {
            Limitation limitation;
            if (card.isSpell()) limitation = ((Spell) card).getLimit();
            else limitation = ((Trap) card).getLimit();
            if (limitation.equals(Limitation.LIMITED)) return numberOfCard < 1;
            else if (limitation.equals(Limitation.SEMI_LIMITED)) return numberOfCard < 2;
            else return numberOfCard < 3;
        }
    }

}
