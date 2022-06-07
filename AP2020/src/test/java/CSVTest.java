import controller.database.CSVInfoGetter;
import model.card.Card;
import model.card.monster.Monster;
import model.card.spell_traps.Spell;
import model.card.spell_traps.Trap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CSVTest {

    @Test
    public void testGetTrapAndSpell() {
        ArrayList<String> temp = CSVInfoGetter.trapAndSpellReadFromCSV("bla bla");
        Assertions.assertNull(temp);
        temp = CSVInfoGetter.trapAndSpellReadFromCSV("Umiiruka");
        Assertions.assertNotNull(temp);
        for (String s : temp) {
            Assertions.assertNotNull(s);
            Assertions.assertFalse(s.equals(""));
        }
    }

    @Test
    public void testMonsterRead() {
        ArrayList<String> temp = CSVInfoGetter.monsterReadFromCSV("bla bla");
        Assertions.assertNull(temp);
        temp = CSVInfoGetter.monsterReadFromCSV("Battle OX");
        Assertions.assertNotNull(temp);
        for (String s : temp) {
            Assertions.assertNotNull(s);
            System.out.println(s);
            Assertions.assertFalse(s.equals(""));
        }
    }

    @Test
    public void testCardNameExists() {
        Assertions.assertFalse(CSVInfoGetter.cardNameExists("bla bla"));
        Assertions.assertTrue(CSVInfoGetter.cardNameExists("Battle OX"));
    }

    @Test
    public void testGetAllNames() {
        ArrayList<String> temp = CSVInfoGetter.getCardNames();
        Assertions.assertNotNull(temp);
        for (String s : temp) {
            Assertions.assertNotNull(s);
            Assertions.assertNotEquals(s, "");
        }
    }

    @Test
    public void testAllCards() {
        ArrayList<String> temp = CSVInfoGetter.getCardNames();
        for (String s : temp) {
            Card tempCard = CSVInfoGetter.getCardByName(s);
            Assertions.assertNotNull(tempCard);
            if (tempCard.isMonster()) {
                Monster tempMonster = (Monster)tempCard;
                Assertions.assertAll("monster should have all the properties" ,
                        () -> Assertions.assertNotNull(tempMonster.getMonsterType()),
                        () -> Assertions.assertNotNull(tempMonster.getMonsterCardType()),
                        //() -> Assertions.assertNotNull(tempMonster.getMonsterEffectType()),
                        () -> Assertions.assertNotNull(tempMonster.getFeatures()),
                        () -> Assertions.assertNotNull(tempMonster.getCardName()),
                        () -> Assertions.assertNotNull(tempMonster.getCardType()),
                        () -> Assertions.assertNotNull(tempMonster.getDescription()));
            } else if (tempCard.isSpell()) {
                Spell tempSpell = (Spell) tempCard;
                Assertions.assertAll("spell or trap should have all the properties" ,
                        () -> Assertions.assertNotNull(tempSpell.getSpellType()),
                        () -> Assertions.assertNotNull(tempSpell.getCardName()),
                        () -> Assertions.assertNotNull(tempSpell.getCardType()),
                        () -> Assertions.assertNotNull(tempSpell.getDescription()),
                        () -> Assertions.assertNotNull(tempSpell.getLimit()));
            } else if (tempCard.isTrap()) {
                Trap tempTrap = (Trap) tempCard;
                Assertions.assertAll("spell or trap should have all the properties" ,
                        () -> Assertions.assertNotNull(tempTrap.getTrapType()),
                        () -> Assertions.assertNotNull(tempTrap.getCardName()),
                        () -> Assertions.assertNotNull(tempTrap.getCardType()),
                        () -> Assertions.assertNotNull(tempTrap.getDescription()),
                        () -> Assertions.assertNotNull(tempTrap.getLimit()));
            } else {
                Assertions.fail();
            }

        }
    }
}
