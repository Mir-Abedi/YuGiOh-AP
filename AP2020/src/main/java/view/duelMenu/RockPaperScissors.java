package view.duelMenu;

import model.User;
import model.game.MiniGame;

import java.util.Scanner;

public class RockPaperScissors {
    private int firstUsersScore;
    private int secondUsersScore;
    private final Scanner scanner;
    private static RockPaperScissors rockPaperScissors;
    private final User firstUser;
    private final User secondUser;
    private final MiniGame miniGame;

    private RockPaperScissors(Scanner scanner, MiniGame miniGame) {
        this.miniGame = miniGame;
        this.firstUser = miniGame.getFirstUser();
        this.secondUser = miniGame.getSecondUser();
        this.scanner = scanner;
        firstUsersScore = 0;
        secondUsersScore = 0;
    }

    public static RockPaperScissors getInstance(Scanner scanner, MiniGame miniGame) {
        return rockPaperScissors = new RockPaperScissors(scanner, miniGame);
    }

    public void run() {
        while (firstUsersScore < 3 && secondUsersScore < 3) {
            String firstUsersChoice;
            while (true) {
                firstUsersChoice = scanner.nextLine().trim();
                if (firstUsersChoice.matches("rock"))
                    break;
                else if (firstUsersChoice.matches("paper"))
                    break;
                else if (firstUsersChoice.matches("scissors"))
                    break;
                else System.out.println("invalid command!\n" + "please chose 'rock' , 'paper' or 'scissors'");
            }
            String secondUsersChoice;
            while (true) {
                secondUsersChoice = scanner.nextLine().trim();
                if (secondUsersChoice.matches("rock"))
                    break;
                else if (secondUsersChoice.matches("paper"))
                    break;
                else if (secondUsersChoice.matches("scissors"))
                    break;
                else System.out.println("invalid command!\n" + "please chose 'rock' , 'paper' or 'scissors'");
            }
            checkThisRoundWinner(firstUsersChoice, secondUsersChoice);
            System.out.println(firstUser.getNickname() + ":" + firstUsersChoice + "\t"
                    + secondUser.getNickname() + ":" + secondUsersChoice + "\n"
                    + firstUsersScore + ":" + secondUsersScore);
        }
        if (firstUsersScore == 3) {
            miniGame.setWinner(firstUser);
        } else {
            miniGame.setWinner(secondUser);
        }
    }

    public void checkThisRoundWinner(String firstUsersChoice, String secondUsersChoice) {
        if (firstUsersChoice.equals("rock") && secondUsersChoice.equals("scissors"))
            firstUsersScore++;
        else if (firstUsersChoice.equals("rock") && secondUsersChoice.equals("paper"))
            secondUsersScore++;
        else if (firstUsersChoice.equals("scissors") && secondUsersChoice.equals("paper"))
            firstUsersScore++;
        else if (firstUsersChoice.equals("scissors") && secondUsersChoice.equals("rock"))
            secondUsersScore++;
        else if (firstUsersChoice.equals("paper") && secondUsersChoice.equals("rock"))
            firstUsersScore++;
        else if (firstUsersChoice.equals("paper") && secondUsersChoice.equals("scissors"))
            secondUsersScore++;
    }
}
