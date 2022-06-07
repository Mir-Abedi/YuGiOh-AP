import controller.LoginMenuController;
import controller.database.ReadAndWriteDataBase;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import view.LoginMenu;
import view.duelMenu.DuelMenu;

public class DuelMenuTest extends TestEssentials {

    @Test
    public void DuelMenuTest1() {
        User mmd = ReadAndWriteDataBase.getUser("mmd.json");
        LoginMenuController.setCurrentUser(mmd);
        String command = "duel -n -s-p mioo -r 1\n" +
                "duel -n -s-p mir -r 1\n" +
                "dice\n" +
                "higher\n" +
                "surrender\n" +
                "duel -n -s-p mir -r 3\n" +
                "dice\n" +
                "higher\n" +
                "surrender\n" +
                "no\n" +
                "no\n" +
                "no\n" +
                "no\n" +
                "surrender\n" +
                "duel -n -s-p mir -r 5\n" +
                "sdlkfj\n" +
                "duel -n -a -r 1 -d mio\n" +
                "duel -n -a -r 1 -d hard\n" +
                "surrender\n" +
                "duel -n -a -r 1 -d easy\n" +
                "surrender\n" +
                "duel -n -a -r 1 -d normal\n" +
                "surrender\n" +
                "duel -n -a -r 2 -d mio\n" +
                "duel -n -a -r 3 -d mio\n" +
                "duel -n -a -r 3 -d hard\n" +
                "surrender\n" +
                "no\n" +
                "no\n" +
                "no\n" +
                "no\n" +
                "surrender\n" +
                "duel -n -a -r 3 -d easy\n" +
                "surrender\n" +
                "no\n" +
                "no\n" +
                "no\n" +
                "no\n" +
                "surrender\n" +
                "duel -n -a -r 3 -d normal\n" +
                "surrender\n" +
                "no\n" +
                "no\n" +
                "no\n" +
                "no\n" +
                "surrender\n" +
                "menu exit";
        setCommandInInputStream(command);
        DuelMenu duelMenu = DuelMenu.getInstance(LoginMenu.getInstance().getScanner());
        duelMenu.showHelp();
        try {
            duelMenu.run();
        } catch (CloneNotSupportedException ignored) { }
        Assertions.assertEquals(LoginMenuController.getCurrentUser(), mmd);
    }
}
