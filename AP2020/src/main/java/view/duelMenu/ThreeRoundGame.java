package view.duelMenu;

import controller.AI;
import controller.GameMenuController;
import controller.LoginMenuController;
import controller.database.ReadAndWriteDataBase;
import model.User;
import model.card.Card;
import model.deck.Deck;
import model.exceptions.WinnerException;
import view.regexes.ThreeRoundGameRegexes;
import view.responses.ThreeRoundGameResponses;

import java.util.Scanner;
import java.util.regex.Matcher;

public class ThreeRoundGame {
    private User firstUser;
    private User secondUser;
    private User winner;
    private User loser;
    private AI ai = null;
    private final Scanner scanner;
    private WinnerStatus firstRoundStatus;
    private WinnerStatus secondRoundStatus;
    private WinnerStatus thirdRoundStatus;

    public ThreeRoundGame(User firstUser, User secondUser, Scanner scanner) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.scanner = scanner;
    }
    public ThreeRoundGame(AI ai, Scanner scanner) {
        this.ai = ai;
        this.scanner = scanner;
    }

    public void run() throws CloneNotSupportedException {
        OneRoundGame firstRound;
        if (ai == null){
            firstRound = new OneRoundGame(firstUser, secondUser, scanner);
        }
        else {
            firstRound = new OneRoundGame(LoginMenuController.getCurrentUser(),ai,scanner);
        }
            try {
                firstRound.run();
            } catch (WinnerException firstRoundException) {
                firstRoundStatus = new WinnerStatus(firstRoundException.getWinner(),
                        firstRoundException.getLoser(),
                        firstRoundException.getWinnerLP(),
                        firstRoundException.getLoserLP());
                System.out.println(firstRoundStatus.getWinner().getNickname() + "won first match!");
            }
        askPlayersIfTheyWantToChangeDeck();
        askPlayersIfTheyWantToBringCardsFromMainToSide();
        OneRoundGame secondRound;
        if (ai == null){
            secondRound = new OneRoundGame(firstUser, secondUser, scanner);
        }
        else {
            secondRound = new OneRoundGame(LoginMenuController.getCurrentUser(),ai,scanner);
        }
        try {
            secondRound.run();
        } catch (WinnerException secondRoundException) {
            secondRoundStatus = new WinnerStatus(secondRoundException.getWinner(),
                    secondRoundException.getLoser(),
                    secondRoundException.getWinnerLP(),
                    secondRoundException.getLoserLP());
            System.out.println(secondRoundStatus.getWinner().getNickname() + "won second match!");
        }
        if (checkIfThirdRoundIsNeededOrNot()) {
            askPlayersIfTheyWantToChangeDeck();
            askPlayersIfTheyWantToBringCardsFromMainToSide();
            OneRoundGame thirdRound;
            if (ai == null){
                thirdRound = new OneRoundGame(firstUser, secondUser, scanner);
            }
            else {
                thirdRound = new OneRoundGame(LoginMenuController.getCurrentUser(),ai,scanner);
            }
            try {
                thirdRound.run();
            } catch (WinnerException thirdRoundException) {
                thirdRoundStatus = new WinnerStatus(thirdRoundException.getWinner(),
                        thirdRoundException.getLoser(),
                        thirdRoundException.getWinnerLP(),
                        thirdRoundException.getLoserLP());
                System.out.println(thirdRoundStatus.getWinner().getNickname() + "won third match!");
            }
            declareWinnerAndLoser(true);
            System.out.println(winner.getUsername() + " won the whole match with score: 3000-1000");
            GameMenuController.cashOut(calculateMaxLP(true), true, winner, loser);
            ReadAndWriteDataBase.updateUser(winner);
            ReadAndWriteDataBase.updateUser(loser);

        } else {
            declareWinnerAndLoser(false);
            System.out.println(winner.getUsername() + " won the whole match with score 3000-0");
            GameMenuController.cashOut(calculateMaxLP(false), true, winner, loser);
            ReadAndWriteDataBase.updateUser(winner);
            ReadAndWriteDataBase.updateUser(loser);
        }
    }

    public void askPlayersIfTheyWantToBringCardsFromMainToSide() {
        if (ai != null)
            askPlayerToBringCardsFromMainToSide(LoginMenuController.getCurrentUser());
        else {
            askPlayerToBringCardsFromMainToSide(firstUser);
            askPlayerToBringCardsFromMainToSide(secondUser);
        }
    }

    public void askPlayerToBringCardsFromMainToSide(User user) {
        String command;
        while (true) {
            System.out.println("do you want to swap cards " + user.getNickname() + "?");
            command = scanner.nextLine();
            if (command.matches("yes")) {
                Deck userActiveDeck = user.getActiveDeck().clone();
                user.setActiveDeck(userActiveDeck);
                getSwapCardCommands(user);
            } else if (command.matches("no"))
                break;
            else
                respond(ThreeRoundGameResponses.INVALID_COMMAND);
        }
    }

    public void getSwapCardCommands(User user) {
        String command;
        while (true) {
            command = scanner.nextLine().trim();
            if (ThreeRoundGameRegexes.doesItSwapCardsCommand(command)) {
                swapCard(user, command);
            } else if (command.matches("back"))
                return;
            else if (command.matches("help"))
                showHelp();
            else
                respond(ThreeRoundGameResponses.INVALID_COMMAND);
        }
    }

    public void swapCard(User user, String command) {
        Matcher matcher = ThreeRoundGameRegexes.getRightMatcherForSwapCards(command);
        assert matcher != null;
        String mainCard = matcher.group("mainCardName");
        String sideCard = matcher.group("sideCardName");
        if (doesCardWithThisNameExistsInMainDeck(user.getActiveDeck(), mainCard)) {
            if (doesCardWithThisNameExistsInSideDeck(user.getActiveDeck(), sideCard)) {
                user.getActiveDeck().getMainDeck().removeCard(mainCard);
                user.getActiveDeck().getMainDeck().addCard(user.getCardByName(sideCard));
                user.getActiveDeck().getSideDeck().addCard(user.getCardByName(mainCard));
                user.getActiveDeck().getSideDeck().removeCard(sideCard);
            } else
                respond(ThreeRoundGameResponses.CARD_WITH_THIS_NAME_DOES_NOT_EXIST_IN_MAIN_DECK);
        } else
            respond(ThreeRoundGameResponses.CARD_WITH_THIS_NAME_DOES_NOT_EXIST_IN_SIDE_DECK);
    }

    public boolean doesCardWithThisNameExistsInMainDeck(Deck deck, String cardName) {
        for (Card card : deck.getMainDeck().getCards()) {
            if (card.getCardName().equals(cardName))
                return true;
        }
        return false;
    }

    public boolean doesCardWithThisNameExistsInSideDeck(Deck deck, String cardName) {
        for (Card card : deck.getSideDeck().getCards()) {
            if (card.getCardName().equals(cardName))
                return true;
        }
        return false;
    }

    public void askPlayersIfTheyWantToChangeDeck() {
        if (ai != null)
            askPlayerToChangeDeck(LoginMenuController.getCurrentUser());
        else {
            askPlayerToChangeDeck(firstUser);
            askPlayerToChangeDeck(secondUser);
        }
    }

    private void askPlayerToChangeDeck(User user) {
        String command;
        while (true) {
            System.out.println("do you want to change your deck " + user.getNickname() + "?");
            command = scanner.nextLine().trim();
            if (command.matches("yes"))
                changeDeck(user);
            else if (command.matches("no"))
                break;
            else if (command.matches("next"))
                break;
            else
                respond(ThreeRoundGameResponses.INVALID_COMMAND);
        }
    }

    public void changeDeck(User user) {
        System.out.println("which deck do you want to use for this round " + user.getNickname() + "?");
        for (Deck deck : user.getDecks()) {
            if (deck.isValid()) {
                System.out.println(deck.getDeckName() + ":");
                System.out.println();
                System.out.println(deck.allCardsToString());
            }
        }
        String command;
        while (true) {
            command = scanner.nextLine().trim();
            if (command.matches("back"))
                return;
            else if (user.getDeckByName(command) == null)
                respond(ThreeRoundGameResponses.YOU_DONT_HAVE_DECK_WITH_THIS_NAME);
            else if (user.getDeckByName(command).isValid()) {
                user.setActiveDeck(user.getDeckByName(command));
                return;
            } else
                respond(ThreeRoundGameResponses.CHOSE_A_VALID_DECK);
        }
    }

    public boolean checkIfThirdRoundIsNeededOrNot() {
        return !firstRoundStatus.getWinner().equals(secondRoundStatus.getWinner());
    }

    public void declareWinnerAndLoser(boolean hasThirdRound) {
        if (hasThirdRound) {
            winner = thirdRoundStatus.getWinner();
            loser = thirdRoundStatus.getLoser();
        } else {
            winner = firstRoundStatus.getWinner();
            loser = firstRoundStatus.getLoser();
        }
    }

    public int calculateMaxLP(boolean hasThirdRound) {
        if (hasThirdRound) {
            if (thirdRoundStatus.getWinner().equals(secondRoundStatus.getWinner()))
                return Math.max(secondRoundStatus.getWinnerLP(), thirdRoundStatus.getWinnerLP());
            return Math.max(firstRoundStatus.getWinnerLP(), thirdRoundStatus.getWinnerLP());
        } else {
            return Math.max(firstRoundStatus.getWinnerLP(), secondRoundStatus.getWinnerLP());
        }
    }

    public void respond(ThreeRoundGameResponses responses) {
        if (responses.equals(ThreeRoundGameResponses.INVALID_COMMAND))
            System.out.println("invalid command!");
        else if (responses.equals(ThreeRoundGameResponses.CHOSE_A_VALID_DECK))
            System.out.println("chose a valid deck");
        else if (responses.equals(ThreeRoundGameResponses.CARD_WITH_THIS_NAME_DOES_NOT_EXIST_IN_MAIN_DECK))
            System.out.println("card with this name does not exist in main deck");
        else if (responses.equals(ThreeRoundGameResponses.CARD_WITH_THIS_NAME_DOES_NOT_EXIST_IN_SIDE_DECK))
            System.out.println("card with this name does not exist in side deck");
        else if (responses.equals(ThreeRoundGameResponses.YOU_DONT_HAVE_DECK_WITH_THIS_NAME))
            System.out.println("you don't have deck with this name");
    }

    public void showHelp() {
        String help = "swap --side --card <card name> --main --card <card name>\n" +
                "(if you don't want any further changes): back\n";
        System.out.println(help);
    }
}
