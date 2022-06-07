import controller.EffectController.MonsterEffectController;
import controller.GameMenuController;
import controller.LoginMenuController;
import controller.database.*;
import model.User;
import model.card.Card;
import model.card.monster.Monster;
import model.card.monster.MonsterEffectType;
import model.deck.Deck;
import model.deck.Graveyard;
import model.game.Board;
import model.game.EffectLimitations;
import model.game.Game;
import model.game.State;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class MonsterEffectsTest {
    static User user1, user2;
    static Game game;
    public static PrintStream defaultStream = System.out;
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

    @Test
    public void testCommandKnight() {
        Card card = CSVInfoGetter.getCardByName("Command Knight");
        HashSet<Integer> bannedCell = new HashSet<>();
        bannedCell.add(1);
        Assertions.assertNotNull(card);
        Assertions.assertNotNull(game.getPlayerBoard());
        Assertions.assertNotNull(game.getPlayerBoard().getMonsterZone(1));
        game.getPlayerBoard().getMonsterZone(1).addCard(card);
        MonsterEffectController.CommandKnight(game, card);
        Assertions.assertNotNull(game.getPlayerLimits());
        Assertions.assertNotNull(game.getRivalLimits().getCantAttackCells());
        Assertions.assertArrayEquals(game.getRivalLimits().getCantAttackCells().toArray(), bannedCell.toArray());
    }

    @Test
    public void testManEaterBug() {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Card card = CSVInfoGetter.getCardByName("Man-Eater Bug");
        Card card1 = CSVInfoGetter.getCardByName("Battle OX");
        Card card2 = CSVInfoGetter.getCardByName("Raigeki");
        Assertions.assertNotNull(card);
        Assertions.assertNotNull(card1);
        Assertions.assertNotNull(card2);
        game.getPlayerBoard().getMonsterZone(1).addCard(card);
        game.getRivalBoard().getSpellZone(1).addCard(card2);
        Executable executable = () -> MonsterEffectController.ManEaterBug(game, card);

        {
            String command = "2\n66\n3";
            setCommandInInputStream(command);
            PrintStream printStream = new PrintStream(stream);
            System.setOut(printStream);
        }
        {
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("You have no monsters !", stream.toString().trim());
            stream.reset();
        }
        {
            game.getRivalBoard().getMonsterZone(2).addCard(card1);
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("Please enter 1 cell numbers .. \n" +
                            "Invalid Cell number !\n" +
                            "Please enter 1 cell numbers .. \n" +
                            "Invalid Command .. \n" +
                            "Please enter 1 cell numbers ..",
                    stream.toString().trim());
        }
        Assertions.assertNull(game.getRivalBoard().getMonsterZone(2).getCard());
        Assertions.assertTrue(game.getRivalBoard().getGraveyard().getCards().contains(card1));
    }

    @Test
    public void testGateGuardian() {
        ByteArrayOutputStream stream = getOutPutStream();

        Card card = CSVInfoGetter.getCardByName("Gate Guardian");
        Card card1 = CSVInfoGetter.getCardByName("Battle OX");
        Card card2 = CSVInfoGetter.getCardByName("Battle OX");
        Card card3 = CSVInfoGetter.getCardByName("Battle OX");
        Executable executable = () -> MonsterEffectController.GateGuardian(game, card);
        {
            Assertions.assertNotNull(card);
            Assertions.assertNotNull(card1);
            Assertions.assertNotNull(card2);
            Assertions.assertNotNull(card3);
        }
        {
            game.getPlayerBoard().getMonsterZone(0).addCard(card1);
            game.getPlayerHandCards().add(card);

            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("You have no monsters !", stream.toString().trim());
            stream.reset();
        }
        {
            setCommandInInputStream("1 2 2\n1 2\n3 4 5\n1 2 3");
            game.getPlayerBoard().getMonsterZone(1).addCard(card2);
            game.getPlayerBoard().getMonsterZone(2).addCard(card3);
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("Please select 3 cards that you want to tribute.\n" +
                    "Card number must be between 1 and 5 and please select them in this format :1 2 3 ..\n" +
                    "Invalid Cell number !\n" +
                    "Please select 3 cards that you want to tribute.\n" +
                    "Card number must be between 1 and 5 and please select them in this format :1 2 3 ..\n" +
                    "Invalid Command\n" +
                    "Invalid Cell number !\n" +
                    "Please select 3 cards that you want to tribute.\n" +
                    "Card number must be between 1 and 5 and please select them in this format :1 2 3 ..",stream.toString().trim());
            Assertions.assertNotNull(game.getPlayerBoard().getMonsterZone(0).getCard());
            Assertions.assertNull(game.getPlayerBoard().getMonsterZone(1).getCard());
            Assertions.assertNull(game.getPlayerBoard().getMonsterZone(2).getCard());
            Assertions.assertFalse(game.getPlayerHandCards().contains(card));
            Assertions.assertTrue(game.getPlayerBoard().getGraveyard().getCards().contains(card1));
            Assertions.assertTrue(game.getPlayerBoard().getGraveyard().getCards().contains(card2));
            Assertions.assertTrue(game.getPlayerBoard().getGraveyard().getCards().contains(card3));
            Assertions.assertNotNull(game.getPlayerBoard().getMonsterZoneCellByCard(card));
        }
    }

    @Test
    public void testHeraldofCreation() {
        ByteArrayOutputStream stream = getOutPutStream();
        Card card = CSVInfoGetter.getCardByName("Herald of Creation");
        Card card1 = CSVInfoGetter.getCardByName("Gate Guardian");
        Card card2 = CSVInfoGetter.getCardByName("Battle OX");
        Card card3 = CSVInfoGetter.getCardByName("Battle OX");
        game.getPlayerBoard().addCardToMonsterZone(card);
        Executable executable = () -> MonsterEffectController.HeraldofCreation(game,card);

        {
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("You have no cards !", stream.toString().trim());
            stream.reset();
        }

        {
            game.getPlayerHandCards().add(card2);
            game.getPlayerBoard().getGraveyard().getCards().add(card1);
            game.getPlayerBoard().getGraveyard().getCards().add(card3);
            setCommandInInputStream("2\n1\n2\n1");
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("please select a card number from your hand\n" +
                    "Please enter a valid number ..\n" +
                    "Please select a card from this item(s) :\n" +
                    "1 : Gate Guardian\n" +
                    "2 : Battle OX\n" +
                    "Please select level 7 or more monster !\n" +
                    "Please select a card from this item(s) :\n" +
                    "1 : Gate Guardian\n" +
                    "2 : Battle OX",stream.toString().trim());

            Assertions.assertTrue(game.getPlayerHandCards().contains(card1));
            Assertions.assertFalse(game.getPlayerHandCards().contains(card2));
            Assertions.assertFalse(game.getPlayerBoard().getGraveyard().getCards().contains(card1));
            Assertions.assertTrue(game.getPlayerBoard().getGraveyard().getCards().contains(card2));
            Assertions.assertTrue(game.getPlayerBoard().getGraveyard().getCards().contains(card3));

        }
    }

    @Test
    public void testMarshmallon() {
        Card card = CSVInfoGetter.getCardByName("Marshmallon");
        Assertions.assertNotNull(card);
        Assertions.assertNotNull(game.getPlayerBoard());
        Assertions.assertNotNull(game.getPlayerBoard().getMonsterZone(1));
        int health = game.getRivalLP();
        Executable executable = () -> MonsterEffectController.Marshmallon(game, card);
        game.getPlayerBoard().getMonsterZone(1).addCard(card);
        Assertions.assertDoesNotThrow(executable);
        Assertions.assertNotEquals(game.getRivalLP(), health);
        Assertions.assertEquals(1000, health - game.getRivalLP());
    }

    @Test
    public void testTheCalculator() {
        Card card = CSVInfoGetter.getCardByName("The Calculator");
        Card card1 = CSVInfoGetter.getCardByName("Battle OX");
        Card card2 = CSVInfoGetter.getCardByName("Battle OX");
        Card card3 = CSVInfoGetter.getCardByName("Battle OX");

        Assertions.assertNotNull(card);
        Assertions.assertNotNull(card1);
        Assertions.assertNotNull(card2);
        Assertions.assertNotNull(card3);

        game.getPlayerBoard().getMonsterZone(1).addCard(card);
        game.getPlayerBoard().getMonsterZone(1).setState(State.FACE_UP_ATTACK);
        game.getPlayerBoard().getMonsterZone(2).addCard(card1);
        game.getPlayerBoard().getMonsterZone(2).setState(State.FACE_DOWN_DEFENCE);
        game.getPlayerBoard().getMonsterZone(3).addCard(card2);
        game.getPlayerBoard().getMonsterZone(3).setState(State.FACE_UP_DEFENCE);
        game.getPlayerBoard().getMonsterZone(4).addCard(card3);
        game.getPlayerBoard().getMonsterZone(4).setState(State.FACE_UP_ATTACK);

        Executable executable = () -> MonsterEffectController.TheCalculator(game, card);
        Assertions.assertDoesNotThrow(executable);
        Monster monster = (Monster) card;
        Assertions.assertEquals(3000, monster.getAttack());
        Assertions.assertEquals(0, monster.getDefense());

        game.getPlayerBoard().getMonsterZone(2).setState(State.FACE_UP_ATTACK);

        Assertions.assertDoesNotThrow(executable);
        Assertions.assertEquals(4200, monster.getAttack());
        Assertions.assertEquals(0, monster.getDefense());

        game.getPlayerBoard().getMonsterZone(2).removeCard();
        Assertions.assertNull(game.getPlayerBoard().getMonsterZone(2).getCard());

        Assertions.assertDoesNotThrow(executable);
        Assertions.assertEquals(3000, monster.getAttack());
        Assertions.assertEquals(0, monster.getDefense());
    }

    @Test
    public void testMirageDragon() {
        Card card = CSVInfoGetter.getCardByName("Mirage Dragon");
        game.getRivalBoard().addCardToMonsterZone(card);
        game.getRivalBoard().getMonsterZoneCellByCard(card).setState(State.FACE_UP_ATTACK);
        Executable executable = () -> MonsterEffectController.MirageDragon(game, card);
        Assertions.assertDoesNotThrow(executable);
        Assertions.assertNotNull(game.getPlayerLimits().getLimitations());
        Assertions.assertTrue(game.getPlayerLimits().getLimitations().contains(EffectLimitations.CANT_ACTIVATE_TRAP));
    }

    @Test
    public void theTricky(){
        ByteArrayOutputStream stream = getOutPutStream();
        Card card = CSVInfoGetter.getCardByName("The Tricky");
        Card card1 = CSVInfoGetter.getCardByName("Battle OX");
        Card card2 = CSVInfoGetter.getCardByName("Battle OX");
        Card card3 = CSVInfoGetter.getCardByName("Battle OX");
        Card card4 = CSVInfoGetter.getCardByName("Battle OX");
        Card card5 = CSVInfoGetter.getCardByName("Battle OX");
        Card card6 = CSVInfoGetter.getCardByName("Battle OX");
        game.addCardToHand(card);
        Executable executable = () -> MonsterEffectController.TheTricky(game,card);
        {
            game.getPlayerBoard().addCardToMonsterZone(card1);
            game.getPlayerBoard().addCardToMonsterZone(card2);
            game.getPlayerBoard().addCardToMonsterZone(card3);
            game.getPlayerBoard().addCardToMonsterZone(card4);
            game.getPlayerBoard().addCardToMonsterZone(card5);
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("Monster zone is full !", stream.toString().trim());
            stream.reset();
        }
        {
            game.getPlayerBoard().removeCardFromMonsterZone(card5);
            game.getPlayerBoard().sendToGraveYard(card5);
            Assertions.assertTrue(game.getPlayerBoard().getGraveyard().getCards().contains(card5));
            Assertions.assertFalse(game.getRivalBoard().getGraveyard().getCards().contains(card5));
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("You have no cards !",stream.toString().trim());
            stream.reset();
        }
        {
            game.getPlayerHandCards().add(card6);
            setCommandInInputStream("9\n1\n2");
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("please select a card number from your hand\n" +
                    "Please enter a valid number ..\n" +
                    "Please select a valid number !\n" +
                    "please select a card number from your hand",stream.toString().trim());
            Assertions.assertNotNull(game.getPlayerBoard().getMonsterZone(4));;
            Assertions.assertTrue(game.getPlayerBoard().isMonsterZoneFull());;
            Assertions.assertFalse(game.getPlayerHandCards().contains(card));
            Assertions.assertTrue(game.getPlayerBoard().getGraveyard().getCards().contains(card6));
        }
    }

    @Test
    public void testTexChanger() {
        Board board = game.getPlayerBoard();
        Graveyard graveyard = board.getGraveyard();
        ArrayList<Card> cards = game.getPlayerHandCards();
        Deck deck = game.getPlayerDeck();
        ByteArrayOutputStream stream = getOutPutStream();

        Card card = CSVInfoGetter.getCardByName("Texchanger");
        Card card1 = CSVInfoGetter.getCardByName("Texchanger");
        Card card2 = CSVInfoGetter.getCardByName("Leotron");
        Card card11 = CSVInfoGetter.getCardByName("Leotron");
        Card card12 = CSVInfoGetter.getCardByName("Leotron");
        Card card13 = CSVInfoGetter.getCardByName("Leotron");
        Card card14 = CSVInfoGetter.getCardByName("Leotron");
        Card card15 = CSVInfoGetter.getCardByName("Leotron");
        Card tempCard = CSVInfoGetter.getCardByName("Battle OX");
        Card tempSpell = CSVInfoGetter.getCardByName("Magnum Shield");
        Executable executable = () -> MonsterEffectController.Texchanger(game, card);
        setCommandInInputStream("yes\n" + "yes\n" + "yes\n4\n7\n14\n1");
        //Not Null test
        {
            Assertions.assertNotNull(card);
            Assertions.assertNotNull(card1);
            Assertions.assertNotNull(card2);
            Assertions.assertNotNull(card11);
            Assertions.assertNotNull(card12);
            Assertions.assertNotNull(card13);
            Assertions.assertNotNull(card14);
            Assertions.assertNotNull(card15);
            Assertions.assertNotNull(tempCard);
            Assertions.assertNotNull(tempSpell);
        }
        //preparing
        {
            ((Monster) card).setMonsterEffectType(MonsterEffectType.TRIGGER);
            ((Monster) card1).setMonsterEffectType(MonsterEffectType.TRIGGER);
            ((Monster) card2).setMonsterEffectType(MonsterEffectType.NONE);
            ((Monster) card11).setMonsterEffectType(MonsterEffectType.NONE);
            ((Monster) card12).setMonsterEffectType(MonsterEffectType.NONE);
            ((Monster) card13).setMonsterEffectType(MonsterEffectType.NONE);
            ((Monster) card14).setMonsterEffectType(MonsterEffectType.NONE);
            ((Monster) card15).setMonsterEffectType(MonsterEffectType.NONE);
            ((Monster) tempCard).setMonsterEffectType(MonsterEffectType.NONE);
            board.addCardToMonsterZone(card);
            deck.addCardToMainDeck(tempCard);
            deck.addCardToMainDeck(tempSpell);
        }

        //first situation test
        {
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("do you want to summon a normal cyberse card?\n" +
                    "You have no monsters !", stream.toString().trim());
            stream.reset();
        }
        //second condition test
        {
            board.addCardToMonsterZone(card11);
            board.addCardToMonsterZone(card12);
            board.addCardToMonsterZone(card13);
            board.addCardToMonsterZone(card14);
            deck.addCardToMainDeck(card1);
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("do you want to summon a normal cyberse card?\n" +
                    "Monster zone is full !", stream.toString().trim());
            stream.reset();
        }
        //third condition test
        {
            GameMenuController.sendToGraveYard(game, card11);
            GameMenuController.sendToGraveYard(game, card12);
            GameMenuController.sendToGraveYard(game, card13);
            Assertions.assertDoesNotThrow(executable);
        }
        Assertions.assertNotNull(board.getMonsterZoneCellByCard(card11));
        Assertions.assertFalse(board.getGraveyard().getCards().contains(card11));
    }

    @Test
    public void testBeastKingBarbaros() {
        Board board = game.getPlayerBoard();
        ArrayList<Card> cards = game.getPlayerHandCards();
        ByteArrayOutputStream stream = getOutPutStream();
        Card card = CSVInfoGetter.getCardByName("Beast King Barbaros");
        Card card11 = CSVInfoGetter.getCardByName("Leotron");
        Card card12 = CSVInfoGetter.getCardByName("Leotron");
        Card card13 = CSVInfoGetter.getCardByName("Leotron");
        Card card14 = CSVInfoGetter.getCardByName("Leotron");
        Card card15 = CSVInfoGetter.getCardByName("Leotron");
        cards.add(card);
        Executable executable = () -> MonsterEffectController.BeastKingBarbaros(game,card);
        setCommandInInputStream("1\n2\n"+"2\n"+"1 2 3\n"+"3 4 5\n");
        {
            board.addCardToMonsterZone(card11);
            board.addCardToMonsterZone(card12);
            board.addCardToMonsterZone(card13);
            board.addCardToMonsterZone(card14);
            board.addCardToMonsterZone(card15);
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("Monster zone is full !",stream.toString().trim());
        }

        {
            board.removeCardFromMonsterZone(card11);
            board.removeCardFromMonsterZone(card12);
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("Monster zone is full !\n" +
                    "You can summon cards in these ways .. \n" +
                    "1. Summon normally with 1900 ATK .. \n" +
                    "2. Summon with 3 tributes ..\n" +
                    "You can type back to cancel ..\n" +
                    "Please choose a way :\n" +
                    "Please choose a card state to summon your card ..\n" +
                    "1. Face up attack \n" +
                    "2. face up defense\n" +
                    "3. face down defenseYou can type back to cancel ..\n" +
                    "Please choose a way :",stream.toString().trim());
            Assertions.assertEquals(board.getMonsterZone(0).getCard(),card);
            Assertions.assertFalse(cards.contains(card));
            Assertions.assertEquals(1900,((Monster)card).getAttack());
            stream.reset();
        }

        {
            ((Monster) card).setAttack(3000);
            board.getMonsterZone(0).removeCard();
            cards.add(card);
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("You can summon cards in these ways .. \n" +
                    "1. Summon normally with 1900 ATK .. \n" +
                    "2. Summon with 3 tributes ..\n" +
                    "You can type back to cancel ..\n" +
                    "Please choose a way :\n" +
                    "Please enter 3 cell numbers .. \n" +
                    "Invalid Cell number !\n" +
                    "Please enter 3 cell numbers ..",stream.toString().trim());
            Assertions.assertEquals(game.getPlayerBoard().getMonsterZone(0).getCard(), card);
            Assertions.assertTrue(board.getGraveyard().getCards().contains(card13));
            Assertions.assertTrue(board.getGraveyard().getCards().contains(card14));
            Assertions.assertTrue(board.getGraveyard().getCards().contains(card15));
            Assertions.assertFalse(board.getMonsterZone(2).isOccupied());
            Assertions.assertFalse(board.getMonsterZone(3).isOccupied());
            Assertions.assertFalse(board.getMonsterZone(4).isOccupied());
            Assertions.assertEquals(3000,((Monster) card).getAttack());
        }
    }

    @Test
    public void testTerratigertheEmpoweredWarrior() {
        Board board = game.getPlayerBoard();
        ArrayList<Card> cards = game.getPlayerHandCards();
        ByteArrayOutputStream stream = getOutPutStream();
        Card card = CSVInfoGetter.getCardByName("Terratiger, the Empowered Warrior");
        Card card11 = CSVInfoGetter.getCardByName("Leotron");
        Card card12 = CSVInfoGetter.getCardByName("Leotron");
        Card card13 = CSVInfoGetter.getCardByName("Leotron");
        Card card14 = CSVInfoGetter.getCardByName("Leotron");
        Card card15 = CSVInfoGetter.getCardByName("Gate Guardian");
        cards.add(card15);
        board.addCardToMonsterZone(card);
        Executable executable = () -> MonsterEffectController.TerratigertheEmpoweredWarrior(game,card);
        setCommandInInputStream("yes\n"+"yes\n"+"1\n2\n");

        //Not Null test
        {
            Assertions.assertNotNull(card);
            Assertions.assertNotNull(card11);
            Assertions.assertNotNull(card12);
            Assertions.assertNotNull(card13);
            Assertions.assertNotNull(card14);
            Assertions.assertNotNull(card15);
        }
        //preparing
        {
            ((Monster) card).setMonsterEffectType(MonsterEffectType.TRIGGER);
            ((Monster) card11).setMonsterEffectType(MonsterEffectType.NONE);
            ((Monster) card12).setMonsterEffectType(MonsterEffectType.NONE);
            ((Monster) card13).setMonsterEffectType(MonsterEffectType.NONE);
            ((Monster) card14).setMonsterEffectType(MonsterEffectType.NONE);
            ((Monster) card15).setMonsterEffectType(MonsterEffectType.NONE);
        }
        {
            board.addCardToMonsterZone(card11);
            board.addCardToMonsterZone(card12);
            board.addCardToMonsterZone(card13);
            board.addCardToMonsterZone(card14);
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("do you want to summon a normal monster with level 4 or less?\n" +
                    "Monster zone is full !",stream.toString().trim());
            stream.reset();

        }
        {
            board.removeCardFromMonsterZone(card11);
            cards.add(card11);
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("do you want to summon a normal monster with level 4 or less?\n" +
                    "please select a card number from your hand\n" +
                    "Please select a valid monster !\n" +
                    "please select a card number from your hand",stream.toString().trim());
            Assertions.assertNotNull(board.getMonsterZoneCellByCard(card11));
            Assertions.assertFalse(cards.contains(card11));
        }

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
