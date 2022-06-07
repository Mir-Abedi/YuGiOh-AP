import controller.LoginMenuController;
import controller.database.ReadAndWriteDataBase;
import main.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MenusTest extends TestEssentials{

    @Test
    public void menusTest1() {
        String command = "user create -u -uasdf;lajsdlkf\n" +
                "mio\n" +
                "help\n" +
                "user create -u abbas123 -p 123 -n abbas\n" +
                "user loginasdf\n" +
                "user login -u nemidoonamkiki -p 123\n" +
                "user login -u abbas -p 1234\n" +
                "user create -u abbas123 -p 123 -n 123\n" +
                "user login -u abbas123 -p 123\n" +
                "help\n" +
                "abbas\n" +
                "menu enter duel\n" +
                "menu show-current\n" +
                "menu exit\n" +
                "menu enter shop\n" +
                "help\n" +
                "shop show -a\n" +
                "menu show-current\n" +
                "shop buy 22\n" +
                "shop buy 23\n" +
                "shop buy 71\n" +
                "increase my money bro 100000\n" +
                "shop buy 55\n" +
                "menu exit\n" +
                "menu enter importexport\n" +
                "import\n" +
                "Battle OX\n" +
                "import\n" +
                "mio\n" +
                "export\n" +
                "abbas\n" +
                "export\n" +
                "Battle OX\n" +
                "import\n" +
                "{" +
                "  \"level\": 4," +
                "  \"attack\": 1700," +
                "  \"permanentAttack\": 1700," +
                "  \"permanentDefense\": 1000," +
                "  \"defense\": 1000," +
                "  \"monsterEffectType\": null," +
                "  \"monsterType\": \"BEAST_WARRIOR\"," +
                "  \"monsterCardType\": \"NORMAL\"," +
                "  \"attribute\": null," +
                "  \"cardName\": \"Battle OX\"," +
                "  \"description\": \"A monster with tremendous power, it destroys enemies with a swing of its axe.\"," +
                "  \"cardType\": \"MONSTER\"," +
                "  \"cardID\": null," +
                "  \"features\": [" +
                "    \"NORMAL_SUMMON\"" +
                "  ]" +
                "}\n" +
                "menu show-current\n" +
                "help\n" +
                "help\n" +
                "menu exit\n" +
                "menu enter profile\n" +
                "help\n" +
                "profile change -n mio\n" +
                "prasdflkj\n" +
                "profile change -p --current 123 --new 1234\n" +
                "menu show-current\n" +
                "menu exit\n" +
                "help\n" +
                "menu enter scoreboard\n" +
                "help\n" +
                "asdf\n" +
                "show scoreboard\n" +
                "menu show-current\n" +
                "menu exit\n" +
                "logout\n" +
                "exit menu\n";
        setCommandInInputStream(command);
        Main.main(new String[]{"aaa"});
        LoginMenuController.setCurrentUser(ReadAndWriteDataBase.getUser("abbas123.json"));
        Assertions.assertEquals(LoginMenuController.getCurrentUser().getUsername(), "abbas123");
    }
}
