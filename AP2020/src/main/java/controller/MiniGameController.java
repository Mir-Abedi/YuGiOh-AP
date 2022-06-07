package controller;

import model.game.MiniGame;

public class MiniGameController {
    public static int dice() {
        int randomNumber;
        randomNumber = (int) (Math.random() * 1_000_000);
        return (randomNumber % 6) + 1;
    }
    public static void playDice(MiniGame miniGame, String playerChoice) {
        int firstUserDice = dice();
        int secondUserDice = dice();
        if (firstUserDice > secondUserDice){
            if (playerChoice.equals("higher"))
                miniGame.setWinner(miniGame.getFirstUser());
            else
                miniGame.setWinner(miniGame.getSecondUser());
        }
        else if (firstUserDice == secondUserDice)
            playDice(miniGame, playerChoice);
        else{
            if (playerChoice.equals("higher"))
                miniGame.setWinner(miniGame.getSecondUser());
            else
                miniGame.setWinner(miniGame.getFirstUser());
        }
    }
    public static void playCoin(MiniGame miniGame, String playerChoice) {
        String coin = throwCoin();
        if (playerChoice.equals(coin))
            miniGame.setWinner(miniGame.getFirstUser());
        else
            miniGame.setWinner(miniGame.getSecondUser());
    }
    public static String throwCoin() {
        int randomNumber;
        randomNumber = (int) (Math.random() * 1_000_000);
        int headOrTale = randomNumber % 2;
        if (headOrTale == 0)
            return "Head";
        else
            return "Tale";
    }
}
