import com.fasterxml.jackson.annotation.JsonTypeInfo;
import controller.EffectController.SpellEffectController;
import controller.GameMenuController;
import controller.database.CSVInfoGetter;
import model.card.Card;
import model.card.spell_traps.Spell;
import model.exceptions.GameException;
import model.exceptions.StopSpell;
import model.exceptions.WinnerException;
import model.game.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.Csv;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SpellEffectTest extends TestEssentials {
    @Test
    public void testMagicJammer() {
        ByteArrayOutputStream stream = getOutPutStream();
        ArrayList<Card> cards = game.getRivalHandCards();
        Board board = game.getRivalBoard();
        Card card1 = CSVInfoGetter.getCardByName("Battle OX");
        Card card = CSVInfoGetter.getCardByName("Magic Jammer");
        Assertions.assertNotNull(card);
        Executable executable = () -> SpellEffectController.MagicJammer(game, card);
        board.addCardToSpellZone(card);
        {
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals("You have no cards !", stream.toString().trim());
        }
        {
            setCommandInInputStream("1");
            cards.add(card1);
            Assertions.assertThrows(StopSpell.class, executable);
            Assertions.assertFalse(cards.contains(card1));
            Assertions.assertTrue(board.getGraveyard().getCards().contains(card1));
        }
    }

    @Test
    public void testSolemanWarning() {
        Board board = game.getRivalBoard();
        Card card = CSVInfoGetter.getCardByName("Solemn Warning");
        Assertions.assertNotNull(card);
        Executable executable = () -> SpellEffectController.SolemnWarning(game, card);
        board.addCardToSpellZone(card);

        {
            Assertions.assertThrows(StopSpell.class, executable);
            Assertions.assertEquals(6000, game.getRivalLP());
        }
        {
            Assertions.assertThrows(StopSpell.class, executable);
            Assertions.assertEquals(4000, game.getRivalLP());
        }
        {
            Assertions.assertThrows(StopSpell.class, executable);
            Assertions.assertEquals(2000, game.getRivalLP());
        }
        {
            Assertions.assertThrows(WinnerException.class, executable);
            Assertions.assertEquals(0, game.getRivalLP());

        }
    }

    @Test
    public void monsterRebornTest() {
        setCommandInInputStream("1\n4\n2\n");
        ByteArrayOutputStream outputStream = getOutPutStream();
        Card card = CSVInfoGetter.getCardByName("Monster Reborn");
        Card card1 = CSVInfoGetter.getCardByName("Battle OX");
        Card card2 = CSVInfoGetter.getCardByName("Battle OX");
        Board myBoard = game.getPlayerBoard();
        Board rivalBoard = game.getRivalBoard();
        addCardToPlayerSpellZone(card);
        Executable executable = () -> SpellEffectController.MonsterReborn(game, card);
        Assertions.assertDoesNotThrow(executable);
        GameMenuController.setSpellAndTrap(game, 1);
        myBoard.getGraveyard().addCard(CSVInfoGetter.getCardByName("Mind Crush"));
        myBoard.getGraveyard().addCard(card1);
        rivalBoard.getGraveyard().addCard(card2);
        rivalBoard.getGraveyard().addCard(CSVInfoGetter.getCardByName("Mind Crush"));
        Assertions.assertDoesNotThrow(executable);
        Assertions.assertNotNull(myBoard.getMonsterZoneCellByCard(card1));
        Assertions.assertFalse(myBoard.getGraveyard().getCards().contains(card1));
        myBoard.addCardToMonsterZone(CSVInfoGetter.getCardByName("Battle OX"));
        myBoard.addCardToMonsterZone(CSVInfoGetter.getCardByName("Battle OX"));
        myBoard.addCardToMonsterZone(CSVInfoGetter.getCardByName("Battle OX"));
        myBoard.addCardToMonsterZone(CSVInfoGetter.getCardByName("Battle OX"));
        Assertions.assertDoesNotThrow(executable);
        Assertions.assertEquals("You have no monsters !\n" +
                "Please select a card from this item(s) :\n" +
                "1 : Mind Crush\n" +
                "2 : Battle OX\n" +
                "3 : Battle OX\n" +
                "4 : Mind Crush\n" +
                "Please select monster !\n" +
                "Please select a card from this item(s) :\n" +
                "1 : Mind Crush\n" +
                "2 : Battle OX\n" +
                "3 : Battle OX\n" +
                "4 : Mind Crush\n" +
                "Please select monster !\n" +
                "Please select a card from this item(s) :\n" +
                "1 : Mind Crush\n" +
                "2 : Battle OX\n" +
                "3 : Battle OX\n" +
                "4 : Mind Crush\n" +
                "Monster zone is full !", outputStream.toString().trim());
        outputStream.reset();
    }

    @Test
    public void terraforming() {
        setCommandInInputStream("1\n4\n9");
        ByteArrayOutputStream outputStream = getOutPutStream();
        Card card = CSVInfoGetter.getCardByName("Terraforming");
        Assertions.assertNotNull(card);
        Card fieldCard = CSVInfoGetter.getCardByName("Yami");
        Assertions.assertNotNull(fieldCard);
        Executable executable = () -> SpellEffectController.Terraforming(game, card);
        addCardToPlayerSpellZone(card);
        Assertions.assertDoesNotThrow(executable);
        game.getPlayerDeck().getMainDeck().getCards().add(fieldCard);
        Assertions.assertDoesNotThrow(executable);
        Assertions.assertFalse(game.getPlayerDeck().getMainDeck().getCards().contains(fieldCard));
        Assertions.assertTrue(game.getPlayerHandCards().contains(fieldCard));
    }

    @Test
    public void potOfGridTest() {
        ByteArrayOutputStream outputStream = getOutPutStream();
        Card card = CSVInfoGetter.getCardByName("Pot of Greed");
        Assertions.assertNotNull(card);
        ArrayList<Card> cards = game.getPlayerHandCards();
        addCardToPlayerSpellZone(card);
        Executable executable = () -> SpellEffectController.PotofGreed(game, card);
        Assertions.assertDoesNotThrow(executable);
        Assertions.assertEquals(2, cards.size());
        Assertions.assertDoesNotThrow(executable);
        Assertions.assertEquals(4, cards.size());
        Assertions.assertDoesNotThrow(executable);
        Assertions.assertEquals(6, cards.size());
        Assertions.assertDoesNotThrow(executable);
        Assertions.assertEquals(8, cards.size());
        Assertions.assertThrows(WinnerException.class, executable);
    }

    @Test
    public void raigeki() {
        ByteArrayOutputStream outputStream = getOutPutStream();
        Board board = game.getRivalBoard();
        Card card = CSVInfoGetter.getCardByName("Raigeki");
        Assertions.assertNotNull(card);
        addCardToPlayerSpellZone(card);
        Card cards[] = new Card[5];
        for (int i = 0; i < 5; i++) {
            cards[i] = CSVInfoGetter.getCardByName("Battle OX");
            board.addCardToMonsterZone(cards[i]);
        }
        Executable executable = () -> SpellEffectController.Raigeki(game, card);
        Assertions.assertDoesNotThrow(executable);
        for (int i = 0; i < 5; i++) {
            Assertions.assertNull(board.getMonsterZoneCellByCard(cards[i]));
            Assertions.assertTrue(board.getGraveyard().getCards().contains(cards[i]));
        }

    }

    @Test
    public void changeOfHeartTest() {
        ByteArrayOutputStream outputStream = getOutPutStream();
        setCommandInInputStream("11\n7\n1");
        Board board = game.getRivalBoard();
        Limits limits = game.getRivalLimits();
        Card card = CSVInfoGetter.getCardByName("Change of Heart");
        Assertions.assertNotNull(card);
        addCardToPlayerSpellZone(card);
        Executable executable = () -> SpellEffectController.ChangeofHeart(game, card);
        Assertions.assertDoesNotThrow(executable);
        Card cards[] = new Card[5];
        for (int i = 0; i < 5; i++) {
            cards[i] = CSVInfoGetter.getCardByName("Battle OX");
            board.addCardToMonsterZone(cards[i]);
        }
        Assertions.assertDoesNotThrow(executable);
        Assertions.assertFalse(limits.hasControlOnMonster(cards[0]));
        Assertions.assertTrue(limits.hasControlOnMonster(cards[1]));
        Assertions.assertTrue(limits.hasControlOnMonster(cards[2]));
        Assertions.assertTrue(limits.hasControlOnMonster(cards[3]));
        Assertions.assertTrue(limits.hasControlOnMonster(cards[4]));

    }

    @Test
    public void harpiesFeatherTest() {
        ArrayList<Card> cardsInHand = game.getPlayerHandCards();
        Card card = CSVInfoGetter.getCardByName("Harpie's Feather Duster");
        Assertions.assertNotNull(card);
        game.getRivalBoard().getSpellZone()[0].addCard(card);
        game.getRivalBoard().getSpellZone()[0].setState(State.FACE_UP_SPELL);
        Card[] cards = new Card[5];
        for (int i = 0; i < 5; i++) {
            cards[i] = CSVInfoGetter.getCardByName("Mind Crush");
            game.addCardToHand(cards[i]);
            GameMenuController.setSpellAndTrap(game, 1);
        }
        Executable executable = () -> SpellEffectController.HarpiesFeatherDuster(game, card);
        Assertions.assertDoesNotThrow(executable);
        for (int i = 0; i < 5; i++) {
            Assertions.assertNull(game.getPlayerBoard().getSpellZoneCellByCard(cards[i]));
            Assertions.assertTrue(game.getPlayerBoard().getGraveyard().getCards().contains(cards[i]));
        }
    }

    @Test
    public void swordsOfRevealingLight() {
        Board rivalBoard = game.getRivalBoard();
        Limits rivalLimits = game.getRivalLimits();
        Card card = CSVInfoGetter.getCardByName("Swords of Revealing Light");
        addCardToPlayerSpellZone(card);
        Assertions.assertNotNull(card);
        Executable executable = () -> SpellEffectController.SwordsofRevealingLight(game, card);
        Card[] cards = new Card[5];
        for (int i = 0; i < 5; i++) {
            cards[i] = CSVInfoGetter.getCardByName("Battle OX");
            rivalBoard.addCardToMonsterZone(cards[i]);
            State state = State.FACE_UP_ATTACK;
            if (i % 2 == 0) state = State.FACE_DOWN_DEFENCE;
            rivalBoard.getMonsterZoneCellByCard(cards[i]).setState(state);
        }
        Assertions.assertDoesNotThrow(executable);
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(State.FACE_UP_ATTACK, rivalBoard.getMonsterZoneCellByCard(cards[i]).getState());
        }
        Assertions.assertTrue(rivalLimits.getLimitations().contains(EffectLimitations.CANT_ATTACK));
        game.changeTurn();
        Assertions.assertEquals(1, game.getRivalBoard().getSpellZoneCellByCard(card).getRoundCounter());
    }

    @Test
    public void DarkHoleTest() {
        Card card = CSVInfoGetter.getCardByName("Dark Hole");
        addCardToPlayerSpellZone(card);
        Card[] cards = new Card[5];
        Card[] cards1 = new Card[5];
        for (int i = 0; i < 5; i++) {
            cards[i] = CSVInfoGetter.getCardByName("Battle OX");
            cards1[i] = CSVInfoGetter.getCardByName("Battle OX");
            game.getRivalBoard().addCardToMonsterZone(cards1[i]);
            game.getRivalBoard().getMonsterZoneCellByCard(cards1[i]).setState(State.FACE_UP_ATTACK);
            game.getPlayerBoard().addCardToMonsterZone(cards[i]);
            game.getPlayerBoard().getMonsterZoneCellByCard(cards[i]).setState(State.FACE_UP_ATTACK);
        }
        Executable executable = () -> SpellEffectController.DarkHole(game, card);
        Assertions.assertDoesNotThrow(executable);
        for (int i = 0; i < 5; i++) {
            Assertions.assertNull(game.getPlayerBoard().getMonsterZoneCellByCard(cards[i]));
            Assertions.assertNull(game.getRivalBoard().getMonsterZoneCellByCard(cards1[i]));
            Assertions.assertTrue(game.getPlayerBoard().getGraveyard().getCards().contains(cards[i]));
            Assertions.assertTrue(game.getRivalBoard().getGraveyard().getCards().contains(cards1[i]));
        }
    }

    @Test
    public void SupplySquadTest() {
        Card card = CSVInfoGetter.getCardByName("Supply Squad");
        Assertions.assertNotNull(card);
        addCardToPlayerSpellZone(card);
        Executable executable = () -> SpellEffectController.SupplySquad(game, card);
        for (int i = 0; i < 8; i++) {
            Assertions.assertDoesNotThrow(executable);
            Assertions.assertEquals(i + 1,game.getPlayerHandCards().size());
        }
        Assertions.assertThrows(WinnerException.class,executable);
    }

    @Test
    public void SpellAbsorptionTest() {
        Card card = CSVInfoGetter.getCardByName("Spell Absorption");
        addCardToPlayerSpellZone(card);
        Executable executable = () -> SpellEffectController.SpellAbsorption(game,card);
        Assertions.assertDoesNotThrow(executable);
        Assertions.assertEquals(8500,game.getPlayerLP());
    }

    @Test
    public void testCalloftheHaunted() {
        Board board = game.getRivalBoard();
        Card card = CSVInfoGetter.getCardByName("Call of The Haunted");
        Card card1 = CSVInfoGetter.getCardByName("Battle OX");
        Card card2 = CSVInfoGetter.getCardByName("Battle OX");
        Assertions.assertNotNull(card);
        GameMenuController.setSpellAndTrap(game, 1);
        Executable executable = () -> SpellEffectController.SolemnWarning(game, card);
        board.addCardToSpellZone(card);

    }

    private void addCardToPlayerSpellZone(Card card) {
        game.getPlayerHandCards().add(card);
        GameMenuController.setSpellAndTrap(game, 1);
    }


}