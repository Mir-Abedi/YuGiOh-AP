package view.duelMenu;

import controller.AI;
import controller.GameMenuController;
import controller.LoginMenuController;
import controller.database.ReadAndWriteDataBase;
import model.User;
import model.exceptions.WinnerException;
import model.game.MiniGame;
import org.jetbrains.annotations.NotNull;
import view.LoginMenu;
import view.regexes.DuelMenuRegex;
import view.responses.StartingGameResponses;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;

public class DuelMenu {
    private final Scanner scanner;
    private static DuelMenu duelMenu;
    private WinnerException oneRoundGameException;

    private DuelMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public static DuelMenu getInstance(Scanner scanner) {
        if (duelMenu == null) duelMenu = new DuelMenu(scanner);
        return duelMenu;
    }

    public void run() throws CloneNotSupportedException {
        String command;
        while (true) {
            command = scanner.nextLine().trim();
            if (DuelMenuRegex.doesItDuelNewPlayerCommand(command))
                duelNewPlayer(command);
            else if (DuelMenuRegex.doesItDuelNewAiCommand(command))
                duelNewAi(command);
            else if (command.matches("help"))
                showHelp();
            else if (command.matches("menu show-current"))
                respond(StartingGameResponses.CURRENT_MENU_DUEL_MENU);
            else if (command.matches("menu exit")) {
                System.out.println("Now Entering main.Main Menu");
                return;
            } else respond(StartingGameResponses.INVALID_COMMAND);
        }
    }

    private void duelNewPlayer(String command) throws CloneNotSupportedException {
        HashMap<String, String> data = parseDuelNewPlayerData(command);
        String username = data.get("username");
        String rounds = data.get("rounds");
        User currentUser = LoginMenuController.getCurrentUser();
        if (currentUser.getUsername().equals(username))
            respond(StartingGameResponses.THERE_IS_NO_PLAYER_WITH_THIS_USERNAME);
        else {
            User winnerOfMiniGame;
            User rivalUser = ReadAndWriteDataBase.getUser(username + ".json");
            if (rivalUser == null) respond(StartingGameResponses.THERE_IS_NO_PLAYER_WITH_THIS_USERNAME);
            else if (rivalUser.getActiveDeck() == null)
                System.out.println(username + " has no active deck");
            else if (!rivalUser.getActiveDeck().isValid())
                System.out.println(username + "'s deck is invalid");
            else if (rounds.equals("1")) {
                MiniGame miniGame = new MiniGame(currentUser, rivalUser);
                MiniGamesMenu.getInstance(scanner, miniGame).run();
                winnerOfMiniGame = miniGame.getWinner();
                if (winnerOfMiniGame.equals(currentUser))
                    singleRoundGame(currentUser, rivalUser);
                else
                    singleRoundGame(rivalUser, currentUser);
            } else if (rounds.equals("3")) {
                MiniGame miniGame = new MiniGame(currentUser, rivalUser);
                MiniGamesMenu.getInstance(scanner, miniGame).run();
                winnerOfMiniGame = miniGame.getWinner();
                if (winnerOfMiniGame.equals(currentUser))
                    tripleRoundGame(currentUser, rivalUser);
                else
                    tripleRoundGame(rivalUser, currentUser);
            } else respond(StartingGameResponses.NUMBER_OF_ROUNDS_IS_NOT_SUPPORTED);
        }

    }

    public void duelNewAi(String command) throws CloneNotSupportedException {
        Matcher matcher = DuelMenuRegex.getRightMatcherForDuelNewAi(command);
        matcher.find();
        int numberOfRounds = Integer.parseInt(matcher.group("rounds"));
        String difficulty = matcher.group("difficulty");
        if (numberOfRounds == 1) {
            switch (difficulty) {
                case "easy": {
                    AI ai = new AI(AI.AIState.EASY);
                    singlePlayerOneRoundGame(LoginMenuController.getCurrentUser(), ai);
                    break;
                }
                case "normal": {
                    AI ai = new AI(AI.AIState.NORMAL);
                    singlePlayerOneRoundGame(LoginMenuController.getCurrentUser(), ai);
                    break;
                }
                case "hard": {
                    AI ai = new AI(AI.AIState.HARD);
                    singlePlayerOneRoundGame(LoginMenuController.getCurrentUser(), ai);
                    break;
                }
                default:
                    respond(StartingGameResponses.DIFFICULTY_IS_NOT_SUPPORTED);
                    break;
            }
        }
        else if (numberOfRounds == 3){
            switch (difficulty) {
                case "easy": {
                    singlePlayerThreeRoundGame(AI.AIState.EASY);
                    break;
                }
                case "normal": {
                    singlePlayerThreeRoundGame(AI.AIState.NORMAL);
                    break;
                }
                case "hard": {
                    singlePlayerThreeRoundGame(AI.AIState.HARD);
                    break;
                }
                default:
                    respond(StartingGameResponses.DIFFICULTY_IS_NOT_SUPPORTED);
                    break;
            }
        }
        else
            respond(StartingGameResponses.NUMBER_OF_ROUNDS_IS_NOT_SUPPORTED);
    }

    public void singleRoundGame(User player, User rival) {
        try {
            new OneRoundGame(player, rival, LoginMenu.getInstance().getScanner()).run();
        } catch (WinnerException winnerException) {
            System.out.println(winnerException.getWinner().getUsername()
                    + " Won the game and the score is :\n"
                    + winnerException.getWinner().getUsername() + " 1000\n"
                    + winnerException.getLoser().getUsername() + " 0");
            GameMenuController.cashOut(winnerException.getWinnerLP(), false, winnerException.getWinner()
                    , winnerException.getLoser());
            ReadAndWriteDataBase.updateUser(player);
            ReadAndWriteDataBase.updateUser(rival);
        }
    }

    public void tripleRoundGame(User player, User rival) throws CloneNotSupportedException {
        ThreeRoundGame threeRoundGame = new ThreeRoundGame(player, rival, scanner);
        threeRoundGame.run();
    }

    public void singlePlayerOneRoundGame(User player, AI ai) {
        try {
            new OneRoundGame(player, ai, LoginMenu.getInstance().getScanner()).run();
        }
        catch (WinnerException winnerException){
            System.out.println(winnerException.getWinner().getUsername()
                    + " Won the game\nAnd the score is :\n"
                    + winnerException.getWinner().getUsername() + " 1000\n"
                    + winnerException.getLoser().getUsername() + " 0");
            GameMenuController.cashOut(winnerException.getWinnerLP(), false, winnerException.getWinner()
                    , winnerException.getLoser());
            ReadAndWriteDataBase.updateUser(player);
        }
    }

    public void singlePlayerThreeRoundGame(AI.AIState difficulty) throws CloneNotSupportedException {
        AI ai = new AI(difficulty);
        ThreeRoundGame threeRoundGame = new ThreeRoundGame(ai,scanner);
        threeRoundGame.run();
    }

    private void respond(@NotNull StartingGameResponses response) {
        if (response.equals(StartingGameResponses.INVALID_COMMAND))
            System.out.println("invalid command!");
        else if (response.equals(StartingGameResponses.CURRENT_MENU_DUEL_MENU))
            System.out.println("duel menu");
        else if (response.equals(StartingGameResponses.NUMBER_OF_ROUNDS_IS_NOT_SUPPORTED))
            System.out.println("number of rounds is not supported");
        else if (response.equals(StartingGameResponses.THERE_IS_NO_PLAYER_WITH_THIS_USERNAME))
            System.out.println("there is no player with this username");
        else if (response.equals(StartingGameResponses.DIFFICULTY_IS_NOT_SUPPORTED))
            System.out.println("difficulty is not supported");
    }

    @NotNull
    private HashMap<String, String> parseDuelNewPlayerData(String command) {
        HashMap<String, String> parsedData = new HashMap<>();
        Matcher matcher = DuelMenuRegex.getRightMatcherForDuelNewPlayer(command);
        assert matcher != null;
        if (matcher.find()) {
            parsedData.put("username", matcher.group("username"));
            parsedData.put("rounds", matcher.group("rounds"));
        }
        return parsedData;
    }

    public void showHelp() {
        String help = "duel --new --second-player <player2 username> --rounds <1/3>\n" +
                "duel --new --ai --rounds <1/3> --difficulty <easy/normal/hard>\n" +
                "menu show-current\n" +
                "menu exit\n" +
                "\n" +
                "shortcut:\n" +
                "duel -n -s-p <player2 username> -r <1/3>\n" +
                "duel -n -a -r <1/3> -d <easy/normal/hard>";
        System.out.println(help);
    }
}
