import controller.database.ReadAndWriteDataBase;
import model.User;
import model.exceptions.WinnerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import view.LoginMenu;
import view.duelMenu.OneRoundGame;


public class OneRoundGameTest extends TestEssentials{

    @Test
    public void startTest() {
        User user = ReadAndWriteDataBase.getUser("mir.json");
        int points = user.getScore();
        User user2 = ReadAndWriteDataBase.getUser("mmd.json");
        int points2 = user2.getScore();
        String command = "select -h 1\n" +
                "select -m 1\n" +
                "select -m -op 1\n" +
                "select -m 2\n" +
                "select -s 1\n" +
                "send to graveyard\n" +
                "help\n" +
                "show table\n" +
                "summon\n" +
                "set\n" +
                "arigaaato\n" +
                "show phase\n" +
                "\n" +
                "next phase\n" +
                "select -h 1\n" +
                "select -m 1\n" +
                "select -m -op 1\n" +
                "select -m 2\n" +
                "select -s 1\n" +
                "send to graveyard\n" +
                "help\n" +
                "show table\n" +
                "summon\n" +
                "set\n" +
                "arigaaato\n" +
                "show phase\n" +
                "show graveyard -p\n" +
                "show graveyard\n" +
                "show table\n" +
                "help\n" +
                "select --hand 1\n" +
                "select --spell 1 -op\n" +
                "select -m 1\n" +
                "show all hand cards\n" +
                "\n" +
                "next phase\n" +
                "select -h 1\n" +
                "select -m 1\n" +
                "select -m -op 1\n" +
                "select -m 2\n" +
                "select -s 1\n" +
                "send to graveyard\n" +
                "help\n" +
                "show table\n" +
                "summon\n" +
                "set\n" +
                "arigaaato\n" +
                "show phase\n" +
                "show graveyard -p\n" +
                "show graveyard\n" +
                "show table\n" +
                "help\n" +
                "select --hand 1\n" +
                "select --spell 1 -op\n" +
                "select -m 1\n" +
                "show all hand cards\n" +
                "\n" +
                "next phase\n" +
                "select -h 1\n" +
                "select -m 1\n" +
                "select -m -op 1\n" +
                "select -m 2\n" +
                "select -s 1\n" +
                "send to graveyard\n" +
                "help\n" +
                "show table\n" +
                "summon\n" +
                "set\n" +
                "arigaaato\n" +
                "show phase\n" +
                "show graveyard -p\n" +
                "show graveyard\n" +
                "show table\n" +
                "help\n" +
                "select --hand 1\n" +
                "select --spell 1 -op\n" +
                "select -m 1\n" +
                "show all hand cards\n" +
                "select -m 1\n" +
                "active effect\n" +
                "\n" +
                "next phase\n" +
                "select -m 1\n" +
                "summon\n" +
                "select -h 1\n" +
                "summon\n" +
                "select -h 2\n" +
                "set\n" +
                "select -h 3\n" +
                "quit\n" +
                "select -u abbas\n" +
                "show table\n" +
                "select -d 1\n" +
                "select -d\n" +
                "select -m 1\n" +
                "attack direct\n" +
                "\n" +
                "next phase\n" +
                "next phase\n" +
                "\n" +
                "-- nobate mmd\n" +
                "show table\n" +
                "man nemikham\n" +
                "select -h 1\n" +
                "select -h 2\n" +
                "summon\n" +
                "select -h 3\n" +
                "set\n" +
                "\n" +
                "next phase\n" +
                "select -h 1\n" +
                "select -h 2\n" +
                "summon\n" +
                "select -h 3\n" +
                "set\n" +
                "select -h 1\n" +
                "card show -s\n" +
                "card show --selected\n" +
                "select -m 1\n" +
                "attack 1\n" +
                "attack direct\n" +
                "\n" +
                "next phase\n" +
                "select -h 1\n" +
                "select -h 2\n" +
                "summon\n" +
                "select -h 3\n" +
                "set\n" +
                "select -h 1\n" +
                "card show -s\n" +
                "card show --selected\n" +
                "select -m 1\n" +
                "attack 1\n" +
                "attack direct\n" +
                "select -s 1\n" +
                "select -s 1 -op\n" +
                "select -m 1\n" +
                "attack 1\n" +
                "attack direct\n" +
                "select -h 1\n" +
                "set --oposition defense\n" +
                "select -m 1\n" +
                "set --position attack\n" +
                "select -s 1\n" +
                "attack\n" +
                "card show -s\n" +
                "select\n" +
                "show phase\n" +
                "select -f -op\n" +
                "card show -s\n" +
                "select -f\n" +
                "card show -s\n" +
                "\n" +
                "next phase\n" +
                "show phase\n" +
                "select -m 1\n" +
                "card show -s\n" +
                "select -m 1 -op\n" +
                "card show -s\n" +
                "select -m 1\n" +
                "attack 1\n" +
                "select -m 2\n" +
                "attack direct\n" +
                "select -h 1\n" +
                "summon\n" +
                "select -h 2\n" +
                "set\n" +
                "show phase\n" +
                "select -s 1\n" +
                "card show -s\n" +
                "select -s 1 -op\n" +
                "card show -s\n" +
                "\n" +
                "im mirebozorg bro\n" +
                "\n" +
                "n\n" +
                "\n" +
                "im mirebozorg bro\n" +
                "\n" +
                "surrender\n";
        setCommandInInputStream(command);
        OneRoundGame oneRoundGame = new OneRoundGame(user, user2, LoginMenu.getInstance().getScanner());
        try {
            oneRoundGame.run();
        } catch (WinnerException e) {
            Assertions.assertTrue(e.getWinner() == user || e.getWinner() == user2);
        }
    }

    @Test
    public void DuelMenu() {

    }
}
