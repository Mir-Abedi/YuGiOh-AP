package view;

import controller.ScoreboardController;
import controller.database.ReadAndWriteDataBase;

import java.util.Scanner;

public class ScoreboardMenu {
    private final Scanner scanner;
    private static ScoreboardMenu scoreboardMenu;

    public void run() {
        String command;
        while (true) {
            command = scanner.nextLine().trim().toLowerCase();
            if (command.equals("menu exit")) {
                System.out.println("Entering main menu");
                return;
            } else if (command.matches("show scoreboard"))
                showScoreBoard();
            else if (command.matches("help"))
                showHelp();
            else if (command.matches("menu show-current"))
                System.out.println("you are in score board menu");
            else System.out.println("invalid command");
        }
    }

    public static ScoreboardMenu getInstance(Scanner scanner) {
        if (scoreboardMenu == null) scoreboardMenu = new ScoreboardMenu(scanner);
        return scoreboardMenu;
    }

    private ScoreboardMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    private void showScoreBoard() {
        String scoreboard = ScoreboardController.getScoreBoard(ReadAndWriteDataBase.getAllUsers());
        System.out.println(scoreboard);
    }

    public void showHelp() {
        String help = "show scoreboard\n";
        help += "menu show-current\n";
        help += "menu exit";
        System.out.println(help);
    }

}
