        import controller.ScoreboardController;
        import controller.database.ReadAndWriteDataBase;
//        import de.vandermeer.asciitable.AsciiTable;
        import model.User;
        import org.junit.jupiter.api.AfterEach;
        import org.junit.jupiter.api.Assertions;
        import org.junit.jupiter.api.Test;
        import view.LoginMenu;
        import java.io.*;
        import java.util.ArrayList;

        public class ViewTest {

            static PrintStream defaultStream = System.out;
    static InputStream defaultInputStream = System.in;

    @Test
    public void createAndLoginUserTest() {
        String command = "user create -u kasraForTest -p kasraForTest1380 -n kasiForTest\n" +
                "user login --username kasraForTest --password kasraForTest1380\n" +
                "logout\n" +
                "exit menu";
        setCommandInInputStream(command);
        LoginMenu.getInstance().run();
        User testUser = ReadAndWriteDataBase.getUser("kasraForTest.json");
        Assertions.assertNotNull(testUser);
        File testFile = new File("src/resources/Users/kasraForTest.json");
        testFile.delete();
    }

    @Test
    public void profileMenuViewChangePassTest(){
        String command ="user create -u kasraForTest -p kasraForTest1380 -n kasiForTest\n" +
                "user login --username kasraForTest --password kasraForTest1380\n" +
                "menu enter profile\n" +
                "profile change -p -c kasraForTest1380 -n kasiii\n" +
                "menu exit\n" +
                "logout\n" +
                "exit menu";
        setCommandInInputStream(command);
        LoginMenu.getInstance().run();
        User testUser = ReadAndWriteDataBase.getUser("kasraForTest.json");
        Assertions.assertEquals("kasiii", testUser.getPassword());
        File testFile = new File("src/resources/Users/kasraForTest.json");
        testFile.delete();
    }

    @Test
    public void profileMenuViewChangeNickTest(){
        String command ="user create -u kasraForTest -p kasraForTest1380 -n kasiForTest\n" +
                "user login --username kasraForTest --password kasraForTest1380\n" +
                "menu enter profile\n" +
                "profile change --nickname testingChangeNickname\n" +
                "menu exit\n" +
                "logout\n" +
                "exit menu";
        setCommandInInputStream(command);
        LoginMenu.getInstance().run();
        User testUser = ReadAndWriteDataBase.getUser("kasraForTest.json");
        Assertions.assertEquals("testingChangeNickname", testUser.getNickname());
        File testFile = new File("src/resources/Users/kasraForTest.json");
        testFile.delete();
    }

    @Test
    public void ScoreBoardTest() {
        User kasraTest = new User("kasraTest","kasramalihi","kasiTest");
        User kasraTest2 = new User("kasriTest","kasrimalihi","kasriTest");
        User amirTest = new User("amirTest","amirAbedi","mirTest");
        User siavashTest = new User("siavashTest","siavashShatranloo","siaTest");
        User nemidonamKiTest = new User("nemidonamKiTest","nemidonamKi","nemidonamKiTest");
        kasraTest.setScore(4000);
        kasraTest2.setScore(4000);
        amirTest.setScore(3000);
        siavashTest.setScore(2000);
        nemidonamKiTest.setScore(2000);
        ArrayList<User> users = new ArrayList<>();
        users.add(amirTest);
        users.add(siavashTest);
        users.add(nemidonamKiTest);
        users.add(kasraTest2);
        users.add(kasraTest);
        String testScoreBoard = ScoreboardController.getScoreBoard(users);
//        AsciiTable asciiTable = new AsciiTable();
//        asciiTable.addRule();
//        asciiTable.addRow("Rank", "Username", "Nickname", "Score");
//        asciiTable.addRule();
//        asciiTable.addRow(1, "kasraTest", "kasiTest", 4000);
//        asciiTable.addRule();
//        asciiTable.addRow(1, "kasriTest", "kasriTest", 4000);
//        asciiTable.addRule();
//        asciiTable.addRow(3, "amirTest", "mirTest", 3000);
//        asciiTable.addRule();
//        asciiTable.addRow(4, "nemidonamKiTest", "nemidonamKiTest", 2000);
//        asciiTable.addRule();
//        asciiTable.addRow(4, "siavashTest", "siaTest", 2000);
//        asciiTable.addRule();
//        String expectedScoreBoard = asciiTable.render();
//        Assertions.assertEquals(expectedScoreBoard, testScoreBoard);
    }

    @Test
    public void deckViewTest(){
        String command = "user create -u kasraForTest -p kasraForTest1380 -n kasiForTest\n" +
                "user login --username kasraForTest --password kasraForTest1380\n" +
                "menu enter shop\n" +
                "add all cards bro\n" +
                "menu exit\n" +
                "menu enter deck\n" +
                "help\n" +
                "deck create kasrasdeck\n" +
                "deck set-activate kasrasdeck\n" +
                "deck add-card --card Battle OX --deck kasrasdeck\n" +
                "deck add-card --card Axe Raider --deck kasrasdeck --side\n"+
                "deck show --deck-name kasrasdeck\n" +
                "deck show --deck-name kasrasdeck --side\n" +
                "deck show --all\n" +
                "deck delete kasrasdeck\n" +
                "menu exit\n" +
                "logout\n" +
                "exit menu";

        setCommandInInputStream(command);

        LoginMenu.getInstance().run();

        File testFile = new File("src/resources/Users/kasraForTest.json");
        testFile.delete();
    }

    @AfterEach
    public void afterEachTest() {
        resetStreams();
    }

    private ByteArrayOutputStream getOutPutStream() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(stream);
        System.setOut(printStream);
        return stream;
    }

    private void setCommandInInputStream(String command1) {
        try {
            System.in.reset();
        } catch (IOException ignored) {
        }
        ByteArrayInputStream stream1 = new ByteArrayInputStream(command1.getBytes());
        System.setIn(stream1);
    }

    private void resetStreams() {
        System.setIn(defaultInputStream);
        System.setOut(defaultStream);
    }

}