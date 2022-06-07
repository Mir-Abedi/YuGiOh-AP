import controller.AI;
import controller.database.CSVInfoGetter;
import controller.database.ReadAndWriteDataBase;
import model.User;
import model.card.Card;
import model.exceptions.WinnerException;
import model.game.Game;
import model.game.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AITest {

    private User userTest1 = ReadAndWriteDataBase.getUser("mir.json");

    @Test
    public void testAIRunWithEasyLoad() throws WinnerException, CloneNotSupportedException {
        AI aiTest = new AI(AI.AIState.EASY);
        Game gameTest = new Game(userTest1,aiTest.getAI());
        aiTest.run(gameTest);
        if (gameTest.getPlayerBoard().getMonsterZone(1) != null){
            Assertions.assertNotNull(gameTest.getPlayerBoard().getMonsterZone(1));
        }
        else {
            Assertions.assertNotNull(gameTest.getPlayerBoard().getSpellZone(1));
        }
    }
    @Test
    public void testAIRunWithNormalLoad() throws WinnerException, CloneNotSupportedException {
        AI aiTest = new AI(AI.AIState.NORMAL);
        Game gameTest = new Game(userTest1,aiTest.getAI());
        aiTest.run(gameTest);
        if (gameTest.getPlayerBoard().getMonsterZone(1) != null){
            Assertions.assertNotNull(gameTest.getPlayerBoard().getMonsterZone(1));
        }
        else {
            Assertions.assertNotNull(gameTest.getPlayerBoard().getSpellZone(1));
        }
    }
    @Test
    public void testAIRunWithHardLoad() throws WinnerException, CloneNotSupportedException {
        AI aiTest = new AI(AI.AIState.HARD);
        Game gameTest = new Game(userTest1,aiTest.getAI());
        aiTest.run(gameTest);
        if (gameTest.getPlayerBoard().getMonsterZone(1) != null){
            Assertions.assertNotNull(gameTest.getPlayerBoard().getMonsterZone(1));
        }
        else {
            Assertions.assertNotNull(gameTest.getPlayerBoard().getSpellZone(1));
        }

    }
    @Test
    public void testRearrangeMonsters() throws CloneNotSupportedException {
        AI aiTest = new AI(AI.AIState.EASY);

        Game gameTest = new Game(userTest1,aiTest.getAI());

        aiTest.setGame(gameTest);

        Card card1 = CSVInfoGetter.getCardByName("Battle OX");
        Card card2 = CSVInfoGetter.getCardByName("Axe Raider");
        Card card3 = CSVInfoGetter.getCardByName("Horn Imp");
        Card card4 = CSVInfoGetter.getCardByName("Slot Machine");
        Card card5 = CSVInfoGetter.getCardByName("Bitron");

        assert card1 != null;
        gameTest.getPlayerBoard().getMonsterZone(0).addCard(card1);
        assert card2 != null;
        gameTest.getPlayerBoard().getMonsterZone(1).addCard(card2);
        assert card3 != null;
        gameTest.getPlayerBoard().getMonsterZone(2).addCard(card3);
        assert card4 != null;
        gameTest.getPlayerBoard().getMonsterZone(3).addCard(card4);
        assert card5 != null;
        gameTest.getPlayerBoard().getMonsterZone(4).addCard(card5);

        gameTest.getPlayerBoard().getMonsterZone(0).setState(State.FACE_UP_ATTACK);
        gameTest.getPlayerBoard().getMonsterZone(1).setState(State.FACE_UP_ATTACK);
        gameTest.getPlayerBoard().getMonsterZone(2).setState(State.FACE_UP_ATTACK);
        gameTest.getPlayerBoard().getMonsterZone(3).setState(State.FACE_UP_ATTACK);
        gameTest.getPlayerBoard().getMonsterZone(4).setState(State.FACE_UP_ATTACK);

        aiTest.rearrangeMonsters();

        Assertions.assertEquals(gameTest.getPlayerBoard().getMonsterZone(0).getState(), State.FACE_UP_ATTACK);
        Assertions.assertEquals(gameTest.getPlayerBoard().getMonsterZone(1).getState(), State.FACE_UP_ATTACK);
        Assertions.assertEquals(gameTest.getPlayerBoard().getMonsterZone(2).getState(), State.FACE_UP_ATTACK);
        Assertions.assertEquals(gameTest.getPlayerBoard().getMonsterZone(3).getState(), State.FACE_UP_ATTACK);
        Assertions.assertEquals(gameTest.getPlayerBoard().getMonsterZone(4).getState(), State.FACE_UP_ATTACK);

    }
}
