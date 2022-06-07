import controller.LoginMenuController;
import controller.database.ReadAndWriteDataBase;
import model.User;
import model.game.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import view.LoginMenu;

import java.io.*;

public class TestEssentials {
    static User user1, user2;
    static Game game;
    static PrintStream defaultStream = System.out;
    static InputStream defaultInputStream = System.in;

    @BeforeEach
    public void createTestArea() {
        user1 = ReadAndWriteDataBase.getUser("sia.json");
        user2 = ReadAndWriteDataBase.getUser("ali.json");
        LoginMenuController.login("sia", "1234");
        Assertions.assertNotNull(LoginMenuController.getCurrentUser());
        Assertions.assertNotNull(user1);
        Assertions.assertNotNull(user2);
        Assertions.assertNotNull(user1.getActiveDeck().getMainDeck());
        Assertions.assertNotNull(user1.getActiveDeck().getMainDeck().getCards());
        Assertions.assertNotNull(user1.getActiveDeck().getMainDeck().getCards().get(5));
        try {
            game = new Game(user1, user2);
        } catch (CloneNotSupportedException e) {
            Assertions.fail();
        }
    }


    @AfterEach
    public void afterEachTest() {
        resetStreams();
    }

    public static ByteArrayOutputStream getOutPutStream() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(stream);
        System.setOut(printStream);
        return stream;
    }

    public static void setCommandInInputStream(String command1) {
        try {
            System.in.reset();
        } catch (IOException ignored) {
        }
        ByteArrayInputStream stream1 = new ByteArrayInputStream(command1.getBytes());
        System.setIn(stream1);
    }

    public static void resetStreams() {
        System.setIn(defaultInputStream);
        System.setOut(defaultStream);
    }


}
