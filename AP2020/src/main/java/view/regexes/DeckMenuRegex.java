package view.regexes;

import java.util.regex.Matcher;

abstract public class DeckMenuRegex {
    public static final String CREATE_DECK_REGEX = "^deck create (?<deckName>\\w+)$";
    public static final String DELETE_DECK_REGEX = "^deck delete (?<deckName>\\w+)$";
    public static final String ACTIVE_DECK_REGEX = "^deck set-activate (?<deckName>\\w+)$";
    public static final String[] SHOW_ALL_DECKS_REGEX = new String[2];

    static {
        SHOW_ALL_DECKS_REGEX[0] = "^deck show --all$";
        SHOW_ALL_DECKS_REGEX[1] = "^deck show -a$";
    }

    public static final String[] SHOW_ALL_CARDS_REGEX = new String[2];

    static {
        SHOW_ALL_CARDS_REGEX[0] = "^deck show --cards$";
        SHOW_ALL_CARDS_REGEX[1] = "^deck show -c$";
    }

    public static final String[] SHOW_MAIN_DECK_REGEX = new String[2];

    static {
        SHOW_MAIN_DECK_REGEX[0] = "^deck show --deck-name (?<deckName>\\w+)$";
        SHOW_MAIN_DECK_REGEX[1] = "^deck show -d (?<deckName>\\w+)$";
    }

    public static final String SHOW_HELP = "^help$";
    private static final String[] ADD_CARD_TO_MAIN_DECK_REGEX = new String[4];

    static {
        ADD_CARD_TO_MAIN_DECK_REGEX[0] = "^deck add-card --card (?<cardName>[\\w ,\\-']+) --deck (?<deckName>\\w+)$";
        ADD_CARD_TO_MAIN_DECK_REGEX[1] = "^deck add-card --deck (?<deckName>\\w+) --card (?<cardName>[\\w \\-,']+)$";
        ADD_CARD_TO_MAIN_DECK_REGEX[2] = "^deck add-card -c (?<cardName>[\\w ,\\-']+) -d (?<deckName>\\w+)$";
        ADD_CARD_TO_MAIN_DECK_REGEX[3] = "^deck add-card -d (?<deckName>\\w+) -c (?<cardName>[\\w \\-,']+)$";
    }

    private static final String[] ADD_CARD_TO_SIDE_DECK_REGEX = new String[12];

    static {
        ADD_CARD_TO_SIDE_DECK_REGEX[0] = "^deck add-card --card (?<cardName>[\\w \\-,']+) --deck (?<deckName>\\w+) --side$";
        ADD_CARD_TO_SIDE_DECK_REGEX[1] = "^deck add-card --card (?<cardName>[\\w \\-,']+) --side --deck (?<deckName>\\w+)$";
        ADD_CARD_TO_SIDE_DECK_REGEX[2] = "^deck add-card --side --card (?<cardName>[\\w \\-,']+) --deck (?<deckName>\\w+)$";
        ADD_CARD_TO_SIDE_DECK_REGEX[3] = "^deck add-card --side --deck (?<deckName>\\w+) --card (?<cardName>[\\w \\-,']+)$";
        ADD_CARD_TO_SIDE_DECK_REGEX[4] = "^deck add-card --deck (?<deckName>\\w+) --side --card (?<cardName>[\\w \\-,']+)$";
        ADD_CARD_TO_SIDE_DECK_REGEX[5] = "^deck add-card --deck (?<deckName>\\w+) --card (?<cardName>[\\w \\-,']+) --side$";
        ADD_CARD_TO_SIDE_DECK_REGEX[6] = "^deck add-card -c (?<cardName>[\\w \\-,']+) -d (?<deckName>\\w+) -s$";
        ADD_CARD_TO_SIDE_DECK_REGEX[7] = "^deck add-card -c (?<cardName>[\\w \\-,']+) -s -d (?<deckName>\\w+)$";
        ADD_CARD_TO_SIDE_DECK_REGEX[8] = "^deck add-card -s -c (?<cardName>[\\w \\-,']+) -d (?<deckName>\\w+)$";
        ADD_CARD_TO_SIDE_DECK_REGEX[9] = "^deck add-card -s -d (?<deckName>\\w+) -c (?<cardName>[\\w \\-,']+)$";
        ADD_CARD_TO_SIDE_DECK_REGEX[10] = "^deck add-card -d (?<deckName>\\w+) -s -c (?<cardName>[\\w \\-,']+)$";
        ADD_CARD_TO_SIDE_DECK_REGEX[11] = "^deck add-card -d (?<deckName>\\w+) -c (?<cardName>[\\w \\-,']+) -s$";
    }

    private static final String[] REMOVE_CARD_FROM_MAIN_DECK_REGEX = new String[4];

    static {
        REMOVE_CARD_FROM_MAIN_DECK_REGEX[0] = "^deck rm-card --card (?<cardName>[\\w \\-,']+) --deck (?<deckName>\\w+)$";
        REMOVE_CARD_FROM_MAIN_DECK_REGEX[1] = "^deck rm-card --deck (?<deckName>\\w+) --card (?<cardName>[\\w \\-,']+)$";
        REMOVE_CARD_FROM_MAIN_DECK_REGEX[2] = "^deck rm-card -c (?<cardName>[\\w \\-,']+) -d (?<deckName>\\w+)$";
        REMOVE_CARD_FROM_MAIN_DECK_REGEX[3] = "^deck rm-card -d (?<deckName>\\w+) -c (?<cardName>[\\w \\-,']+)$";
    }

    private static final String[] REMOVE_CARD_FROM_SIDE_DECK_REGEX = new String[12];

    static {
        REMOVE_CARD_FROM_SIDE_DECK_REGEX[0] = "^deck rm-card --card (?<cardName>[\\w \\-,']+) --deck (?<deckName>\\w+) --side$";
        REMOVE_CARD_FROM_SIDE_DECK_REGEX[1] = "^deck rm-card --card (?<cardName>[\\w \\-,']+) --side --deck (?<deckName>\\w+)$";
        REMOVE_CARD_FROM_SIDE_DECK_REGEX[2] = "^deck rm-card --side --card (?<cardName>[\\w \\-,']+) --deck (?<deckName>\\w+)$";
        REMOVE_CARD_FROM_SIDE_DECK_REGEX[3] = "^deck rm-card --side --deck (?<deckName>\\w+) --card (?<cardName>[\\w \\-,']+)$";
        REMOVE_CARD_FROM_SIDE_DECK_REGEX[4] = "^deck rm-card --deck (?<deckName>\\w+) --card (?<cardName>[\\w \\-,']+) --side$";
        REMOVE_CARD_FROM_SIDE_DECK_REGEX[5] = "^deck rm-card --deck (?<deckName>\\w+) --side --card (?<cardName>[\\w \\-,']+)$";
        REMOVE_CARD_FROM_SIDE_DECK_REGEX[6] = "^deck rm-card -c (?<cardName>[\\w \\-,']+) -d (?<deckName>\\w+) -s$";
        REMOVE_CARD_FROM_SIDE_DECK_REGEX[7] = "^deck rm-card -c (?<cardName>[\\w \\-,']+) -s -d (?<deckName>\\w+)$";
        REMOVE_CARD_FROM_SIDE_DECK_REGEX[8] = "^deck rm-card -s -c (?<cardName>[\\w \\-,']+) -d (?<deckName>\\w+)$";
        REMOVE_CARD_FROM_SIDE_DECK_REGEX[9] = "^deck rm-card -s -d (?<deckName>\\w+) -c (?<cardName>[\\w \\-,']+)$";
        REMOVE_CARD_FROM_SIDE_DECK_REGEX[10] = "^deck rm-card -d (?<deckName>\\w+) -c (?<cardName>[\\w \\-,']+) -s$";
        REMOVE_CARD_FROM_SIDE_DECK_REGEX[11] = "^deck rm-card -d (?<deckName>\\w+) -s -c (?<cardName>[\\w \\-,']+)$";
    }

    private static final String[] SHOW_SIDE_DECK_REGEX = new String[8];

    static {
        SHOW_SIDE_DECK_REGEX[0] = "^deck show --deck-name (?<deckName>\\w+) --side$";
        SHOW_SIDE_DECK_REGEX[1] = "^deck show --side --deck-name (?<deckName>\\w+)$";
        SHOW_SIDE_DECK_REGEX[2] = "^deck show --deck-name (?<deckName>\\w+) -s$";
        SHOW_SIDE_DECK_REGEX[3] = "^deck show -s --deck-name (?<deckName>\\w+)$";
        SHOW_SIDE_DECK_REGEX[4] = "^deck show -d (?<deckName>\\w+) --side$";
        SHOW_SIDE_DECK_REGEX[5] = "^deck show --side -d (?<deckName>\\w+)$";
        SHOW_SIDE_DECK_REGEX[6] = "^deck show -d (?<deckName>\\w+) -s$";
        SHOW_SIDE_DECK_REGEX[7] = "^deck show -s -d (?<deckName>\\w+)$";
    }

    public static String[] getAddCardToMainDeck() {
        return ADD_CARD_TO_MAIN_DECK_REGEX;
    }

    public static String[] getAddCardToSideDeck() {
        return ADD_CARD_TO_SIDE_DECK_REGEX;
    }

    public static String[] getRemoveCardFromMainDeck() {
        return REMOVE_CARD_FROM_MAIN_DECK_REGEX;
    }

    public static String[] getRemoveCardFromSideDeck() {
        return REMOVE_CARD_FROM_SIDE_DECK_REGEX;
    }

    public static String[] getShowSideDeck() {
        return SHOW_SIDE_DECK_REGEX;
    }

    public static boolean doesItAddCardToMainDeckCommand(String command) {
        String[] addCardToMainDeckRegex = getAddCardToMainDeck();
        for (String cardToMainDeckRegex : addCardToMainDeckRegex) {
            if (command.matches(cardToMainDeckRegex)) return true;
        }
        return false;
    }

    public static boolean doesItAddCardToSideDeckCommand(String command) {
        String[] addCardToSideDeckRegex = getAddCardToSideDeck();
        for (String cardToSideDeckRegex : addCardToSideDeckRegex) {
            if (command.matches(cardToSideDeckRegex)) return true;
        }
        return false;
    }

    public static boolean doesItRemoveCardFromMainDeckCommand(String command) {
        String[] removeCardFromMainDeckRegex = getRemoveCardFromMainDeck();
        for (String cardFromMainDeckRegex : removeCardFromMainDeckRegex) {
            if (command.matches(cardFromMainDeckRegex)) return true;
        }
        return false;
    }

    public static boolean doesItRemoveCardFromSideDeckCommand(String command) {
        String[] removeCardFromSideDeckRegex = getRemoveCardFromSideDeck();
        for (String cardFromSideDeckRegex : removeCardFromSideDeckRegex) {
            if (command.matches(cardFromSideDeckRegex)) return true;
        }
        return false;
    }

    public static boolean doesItShowSideDeckCommand(String command) {
        String[] showSideDeckRegex = getShowSideDeck();
        for (String sideDeckRegex : showSideDeckRegex) {
            if (command.matches(sideDeckRegex)) return true;
        }
        return false;
    }

    public static Matcher getRightMatcherForAddCardToMainDeck(String command) {
        String[] addCardToMainDeckRegex = getAddCardToMainDeck();
        for (String cardToMainDeckRegex : addCardToMainDeckRegex) {
            if (command.matches(cardToMainDeckRegex))
                return RegexFunctions.getCommandMatcher(command, cardToMainDeckRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForAddCardToSideDeck(String command) {
        String[] addCardToSideDeckRegex = getAddCardToSideDeck();
        for (String cardToSideDeckRegex : addCardToSideDeckRegex) {
            if (command.matches(cardToSideDeckRegex))
                return RegexFunctions.getCommandMatcher(command, cardToSideDeckRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForRemoveCardFromMainDeck(String command) {
        String[] removeCardFromMainDeckRegex = getAddCardToMainDeck();
        for (String cardFromMainDeckRegex : removeCardFromMainDeckRegex) {
            if (command.matches(cardFromMainDeckRegex))
                return RegexFunctions.getCommandMatcher(command, cardFromMainDeckRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForRemoveCardFromSideDeck(String command) {
        String[] removeCardFromSideDeckRegex = getRemoveCardFromSideDeck();
        for (String cardFromSideDeckRegex : removeCardFromSideDeckRegex) {
            if (command.matches(cardFromSideDeckRegex))
                return RegexFunctions.getCommandMatcher(command, cardFromSideDeckRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForShowSideDeck(String command) {
        String[] showSideDeckRegex = getShowSideDeck();
        for (String sideDeckRegex : showSideDeckRegex) {
            if (command.matches(sideDeckRegex)) return RegexFunctions.getCommandMatcher(command, sideDeckRegex);
        }
        return null;
    }

    public static boolean showMainDeck(String command) {
        return command.matches(SHOW_MAIN_DECK_REGEX[0]) || command.matches(SHOW_MAIN_DECK_REGEX[1]);
    }

    public static boolean showAllCards(String command) {
        return command.matches(SHOW_ALL_CARDS_REGEX[0]) || command.matches(SHOW_ALL_CARDS_REGEX[1]);
    }

    public static boolean showAllDecks(String command) {
        return command.matches(SHOW_ALL_DECKS_REGEX[0]) || command.matches(SHOW_ALL_DECKS_REGEX[1]);
    }
}
