import controller.MiniGameController;
import model.User;
import model.game.MiniGame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import view.LoginMenu;
import view.duelMenu.RockPaperScissors;
import view.duelMenu.WinnerStatus;

public class MiniGameTest extends TestEssentials {

    @Test
    public void miniGameTest1() {
        int i = MiniGameController.dice();
        Assertions.assertTrue(i <= 6 && i >= 1);

        User user1 = new User("a", "a", "a");
        User user2 = new User("b", "b", "b");
        MiniGame miniGame = new MiniGame(user1 ,user2);
        MiniGameController.playDice(miniGame, "higher");
        Assertions.assertTrue(miniGame.getWinner() == user1 || miniGame.getWinner() == user2);

        MiniGameController.playDice(miniGame, "abbas");
        Assertions.assertTrue(miniGame.getWinner() == user1 || miniGame.getWinner() == user2);

        Assertions.assertNotNull(miniGame.getFirstUser());
        Assertions.assertNotNull(miniGame.getSecondUser());

        String string = MiniGameController.throwCoin();
        Assertions.assertTrue(string.equals("Head") || string.equals("Tale"));
    }

    @Test
    public void winnerStatusTest() {
        User user1 = new User("a", "a", "a");
        User user2 = new User("b", "b", "b");
        WinnerStatus winnerStatus = new WinnerStatus(user1, user2, 1000, 0);
        winnerStatus.setWinner(user1);
        winnerStatus.setLoser(user2);
        winnerStatus.setWinnerLP(1000);
        winnerStatus.setLoserLP(0);
        Assertions.assertEquals(user1, winnerStatus.getWinner());
        Assertions.assertEquals(user2, winnerStatus.getLoser());
        Assertions.assertTrue(winnerStatus.getWinnerLP() == 1000
                && winnerStatus.getLoserLP() == 0);
    }

    @Test
    public void rockPaperTest() {
        User user1 = new User("a", "a", "a");
        User user2 = new User("b", "b", "b");
        MiniGame miniGame1 = new MiniGame(user1, user2);
        MiniGame miniGame2 = new MiniGame(user1, user2);
        String command = "rock\n" +
                "scissors\n" +
                "mio\n" +
                "rock\n" +
                "paper\n" +
                "scissors\n" +
                "paper\n" +
                "scissors\n" +
                "rock\n" +
                "paper\n" +
                "rock\n" +
                "rock\n" +
                "scissors\n" +
                "rock\n" +
                "paper\n" +
                "scissors\n" +
                "paper\n" +
                "scissors\n" +
                "rock\n" +
                "paper\n" +
                "mio\n" +
                "scissors";
        setCommandInInputStream(command);
        RockPaperScissors rockPaperScissors = RockPaperScissors.getInstance(LoginMenu.getInstance().getScanner(),
                miniGame1);
        rockPaperScissors.run();
        Assertions.assertEquals(miniGame1.getWinner(), user1);
        rockPaperScissors = RockPaperScissors.getInstance(LoginMenu.getInstance().getScanner(),
                miniGame2);
        rockPaperScissors.run();
        Assertions.assertEquals(miniGame2.getWinner(), user2);
    }
}
