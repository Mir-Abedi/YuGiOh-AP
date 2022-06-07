package view;

import controller.DeckMenuController;
import view.regexes.DeckMenuRegex;
import view.regexes.RegexFunctions;
import view.responses.DeckMenuResponses;

import java.util.Scanner;
import java.util.regex.Matcher;

public class DeckMenu {
    private static DeckMenu deckMenu;
    private final Scanner scanner;


    private DeckMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public static DeckMenu getInstance(Scanner scanner) {
        if (deckMenu == null) deckMenu = new DeckMenu(scanner);
        return deckMenu;
    }

    public static void showMessage(String message) {
        System.out.println(message);
    }


    public void run() {
        String command;
        while (true) {
            command = scanner.nextLine().trim();
            if (command.matches(DeckMenuRegex.CREATE_DECK_REGEX))
                creatDeck(command);
            else if (command.matches(DeckMenuRegex.DELETE_DECK_REGEX))
                deleteDeck(command);
            else if (DeckMenuRegex.showMainDeck(command))
                showMainDeck(command);
            else if (DeckMenuRegex.doesItShowSideDeckCommand(command))
                showSideDeck(command);
            else if (DeckMenuRegex.showAllDecks(command))
                showAllDecks();
            else if (DeckMenuRegex.showAllCards(command))
                showAllCards();
            else if (DeckMenuRegex.doesItAddCardToMainDeckCommand(command))
                addCardToMainDeck(command);
            else if (DeckMenuRegex.doesItAddCardToSideDeckCommand(command))
                addCardToSideDeck(command);
            else if (DeckMenuRegex.doesItRemoveCardFromMainDeckCommand(command))
                removeCardFromMainDeck(command);
            else if (DeckMenuRegex.doesItRemoveCardFromSideDeckCommand(command))
                removeCardFromSideDeck(command);
            else if (command.matches(DeckMenuRegex.ACTIVE_DECK_REGEX))
                activeDeck(command);
            else if (command.matches(DeckMenuRegex.SHOW_HELP))
                showHelp();
            else if (command.matches("menu show-current"))
                respond(DeckMenuResponses.CURRENT_MENU_DECK_MENU);
            else if (command.matches("menu exit")) {
                System.out.println("Entering main menu");
                return;
            } else respond(DeckMenuResponses.INVALID_COMMAND);
        }
    }

    private void creatDeck(String command) {
        Matcher matcher = RegexFunctions.getCommandMatcher(command, DeckMenuRegex.CREATE_DECK_REGEX);
        if (matcher.find()) {
            String deckName = matcher.group("deckName");
            DeckMenuResponses response = DeckMenuController.createDeck(deckName);
            respond(response);
        }
    }

    private void deleteDeck(String command) {
        Matcher matcher = RegexFunctions.getCommandMatcher(command, DeckMenuRegex.DELETE_DECK_REGEX);
        if (matcher.find()) {
            String deckName = matcher.group("deckName");
            DeckMenuResponses response = DeckMenuController.deleteDeck(deckName);
            respond(response);
        }
    }

    private void activeDeck(String command) {
        Matcher matcher = RegexFunctions.getCommandMatcher(command, DeckMenuRegex.ACTIVE_DECK_REGEX);
        if (matcher.find()) {
            String deckName = matcher.group("deckName");
            DeckMenuResponses response = DeckMenuController.activateDeck(deckName);
            respond(response);
        }
    }

    private void addCardToMainDeck(String command) {
        Matcher matcher = DeckMenuRegex.getRightMatcherForAddCardToMainDeck(command);
        if (matcher.find()) {
            String deckName = matcher.group("deckName");
            String cardName = matcher.group("cardName");
            DeckMenuResponses response = DeckMenuController.addCardToMainDeck(deckName, cardName);
            respond(response);
        }
    }

    private void addCardToSideDeck(String command) {
        Matcher matcher = DeckMenuRegex.getRightMatcherForAddCardToSideDeck(command);
        if (matcher.find()) {
            String deckName = matcher.group("deckName");
            String cardName = matcher.group("cardName");
            DeckMenuResponses response = DeckMenuController.addCardToSideDeck(deckName, cardName);
            respond(response);
        }
    }

    private void removeCardFromMainDeck(String command) {
        Matcher matcher = DeckMenuRegex.getRightMatcherForRemoveCardFromMainDeck(command);
        if (matcher.find()) {
            String deckName = matcher.group("deckName");
            String cardName = matcher.group("cardName");
            DeckMenuResponses response = DeckMenuController.removeCardFromMainDeck(deckName, cardName);
            respond(response);
        }
    }

    private void removeCardFromSideDeck(String command) {
        Matcher matcher = DeckMenuRegex.getRightMatcherForRemoveCardFromSideDeck(command);
        if (matcher.find()) {
            String deckName = matcher.group("deckName");
            String cardName = matcher.group("cardName");
            DeckMenuResponses response = DeckMenuController.removeCardFromSideDeck(deckName, cardName);
            respond(response);
        }
    }

    private void showAllCards() {
        String response = DeckMenuController.showAllCards();
        showMessage(response);
    }

    private void showAllDecks() {
        String response = DeckMenuController.showAllDecks();
        showMessage(response);
    }

    private void showMainDeck(String command) {
        Matcher matcher = RegexFunctions.getCommandMatcher(command, DeckMenuRegex.SHOW_MAIN_DECK_REGEX[0]);
        if (matcher.find()) {
            String deckName = matcher.group("deckName");
            DeckMenuResponses response = DeckMenuController.showMainDeck(deckName);
            respond(response);
        } else {
            matcher = RegexFunctions.getCommandMatcher(command, DeckMenuRegex.SHOW_MAIN_DECK_REGEX[1]);
            if (matcher.find()) {
                String deckName = matcher.group("deckName");
                DeckMenuResponses response = DeckMenuController.showMainDeck(deckName);
                respond(response);
            }
        }
    }

    private void showSideDeck(String command) {
        Matcher matcher = DeckMenuRegex.getRightMatcherForShowSideDeck(command);
        if (matcher.find()) {
            String deckName = matcher.group("deckName");
            DeckMenuResponses response = DeckMenuController.showSideDeck(deckName);
            respond(response);
        }
    }

    private void respond(DeckMenuResponses response) {
        if (response.equals(DeckMenuResponses.SUCCESSFUL))
            System.out.println("successful!"); //ToDo
        else if (response.equals(DeckMenuResponses.CANT_ADD_MORE_OF_THIS_CARD))
            System.out.println("there are already three cards with this name in the selected deck!");
        else if (response.equals(DeckMenuResponses.DECK_DOESNT_EXIST))
            System.out.println("there is no such deck with this name!");
        else if (response.equals(DeckMenuResponses.CARD_DOESNT_EXIST))
            System.out.println("there isn't such a card with this name");
        else if (response.equals(DeckMenuResponses.DECK_ALREADY_EXISTS))
            System.out.println("deck with this name already exists!");
        else if (response.equals(DeckMenuResponses.MAIN_DECK_IS_FULL))
            System.out.println("main deck is full!");
        else if (response.equals(DeckMenuResponses.SIDE_DECK_IS_FULL))
            System.out.println("side deck is full!");
        else if (response.equals(DeckMenuResponses.CURRENT_MENU_DECK_MENU))
            System.out.println("you are in deck menu");
        else if (response.equals(DeckMenuResponses.INVALID_COMMAND))
            System.out.println("invalid command!");
    }

    public void showHelp() {
        String help = "deck create <deckName>\n";
        help += "deck delete <deckName>\n";
        help += "deck set-activate <deckName>\n";
        help += "deck add-card --card <card name> --deck <deck name> --side(optional)\n";
        help += "deck rm-card --card <card name> --deck <deck name> --side(optional)\n";
        help += "deck show --all\n";
        help += "deck show --deck-name <deck name> --side(optional)\n";
        help += "deck show --cards\n";
        help += "menu show-current\n";
        help += "menu exit";
        System.out.println(help);
    }

}
