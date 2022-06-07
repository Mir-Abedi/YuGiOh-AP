package view.duelMenu;

import controller.MiniGameController;
import model.game.MiniGame;

import java.util.Scanner;

public class MiniGamesMenu {
    private final Scanner scanner;
    private static MiniGamesMenu miniGameMenu;
    private final MiniGame miniGame;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private MiniGamesMenu(Scanner scanner, MiniGame miniGame) {
        this.miniGame = miniGame;
        this.scanner = scanner;
    }

    public static MiniGamesMenu getInstance(Scanner scanner, MiniGame miniGame) {
        return miniGameMenu = new MiniGamesMenu(scanner, miniGame);
    }

    public void run() {
        String command;
        System.out.println("chose a game to declare first player:\n" +
                "rock paper scissors\n" +
                "dice\n" +
                "throw coin");
        while (true) {
            command = scanner.nextLine().trim();
            if (command.matches("rock paper scissors")) {
                playRockPaperScissor();
                System.out.println(ANSI_YELLOW + ANSI_BLACK_BACKGROUND +
                        miniGame.getWinner().getUsername() + " won the mini game!" + ANSI_RESET);
                return;
            } else if (command.matches("dice")) {
                playDice();
                System.out.println(ANSI_YELLOW + ANSI_BLACK_BACKGROUND +
                        miniGame.getWinner().getUsername() + " won the mini game!" + ANSI_RESET);
                return;
            } else if (command.matches("throw coin")) {
                playCoin();
                System.out.println(ANSI_YELLOW + ANSI_BLACK_BACKGROUND +
                        miniGame.getWinner().getUsername() + " won the mini game!" + ANSI_RESET);
                return;
            } else if (command.matches("help"))
                showHelp();
            else if (command.matches("menu show-current"))
                System.out.println("mini games menu");
            else System.out.println("invalid command!");
        }
    }

    private void playDice() {
        System.out.println("higher or lower?");
        String userChoice;
        while (true){
            userChoice = scanner.nextLine().trim();
            if (userChoice.equals("higher"))
                break;
            else if (userChoice.equals("lower"))
                break;
            else
                System.out.println("please chose higher or lower!");
        }
        MiniGameController.playDice(miniGame , userChoice);
    }

    private void playCoin() {
        System.out.println("head or tale");
        String userChoice;
        while (true) {
            userChoice = scanner.nextLine().trim();
            if (userChoice.equals("head"))
                break;
            else if (userChoice.equals("tale"))
                break;
            else
                System.out.println("please chose head or tail!");
        }
        MiniGameController.playCoin(miniGame, userChoice);
    }

    public void playRockPaperScissor() {
        RockPaperScissors.getInstance(scanner, miniGame).run();
    }

    public void showHelp() {
        String help = ANSI_BLACK_BACKGROUND + ANSI_YELLOW +
                "rock paper scissors\n" +
                "dice\n" +
                "throw coin\n" +
                "menu show-current"
                + ANSI_RESET;
        System.out.println(help);
    }
}
