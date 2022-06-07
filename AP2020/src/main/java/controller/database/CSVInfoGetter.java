package controller.database;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import model.card.Attribute;
import model.card.Card;
import model.card.monster.Monster;
import model.card.monster.MonsterCardType;
import model.card.monster.MonsterType;
import model.card.spell_traps.*;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVInfoGetter {
    private static final String monsterFileName = System.getProperty("os.name").startsWith("Windows") ? "src\\resources\\card-infos\\Monster.csv" : "src/resources/card-infos/Monster.csv";
    private static final String spellTrapFileName = System.getProperty("os.name").startsWith("Windows") ? "src\\resources\\card-infos\\SpellTrap.csv" : "src/resources/card-infos/SpellTrap.csv";

    public static ArrayList<String> trapAndSpellReadFromCSV(String cardName) {
        ArrayList<String> tempArraylist = new ArrayList<String>();
        List<String[]> listOfStrings = readFromFile(spellTrapFileName);
        if (listOfStrings == null) {
            return null;
        }
        for (String[] temp : listOfStrings) {
            if (temp[0].equalsIgnoreCase(cardName)) {
                for (int i = 1; i < 5; i++) {
                    tempArraylist.add(temp[i]);
                }
                return tempArraylist;
            }
        }
        return null;
    }

    public static ArrayList<String> monsterReadFromCSV(String monsterName) {
        ArrayList<String> tempArraylist = new ArrayList<String>();
        List<String[]> listOfStrings = readFromFile(monsterFileName);
        if (listOfStrings == null) {
            return null;
        }
        for (String[] temp : listOfStrings) {
            if (temp[0].equalsIgnoreCase(monsterName)) {
                for (int i = 1; i <= 7; i++) {
                    tempArraylist.add(temp[i]);
                }
                return tempArraylist;
            }
        }
        return null;
    }

    private static List<String[]> readFromFile(String fileName) {
        try {
            CSVReader reader = new CSVReader(new FileReader(fileName));
            List<String[]> output = reader.readAll();
            reader.close();
            return output;

        } catch (IOException | CsvException e) {
            return null;
        }
    }

    public static Attribute getAttribute(String attribute) {
        if (attribute.equalsIgnoreCase("dark")) return Attribute.DARK;
        else if (attribute.equalsIgnoreCase("earth")) return Attribute.EARTH;
        else if (attribute.equalsIgnoreCase("fire")) return Attribute.FIRE;
        else if (attribute.equalsIgnoreCase("light")) return Attribute.LIGHT;
        else if (attribute.equalsIgnoreCase("water")) return Attribute.WATER;
        else if (attribute.equalsIgnoreCase("wind")) return Attribute.WIND;
        return null;
    }

    public static MonsterCardType getMonsterCardType(String monsterCardType) {
        if (monsterCardType.equalsIgnoreCase("normal")) return MonsterCardType.NORMAL;
        else if (monsterCardType.equalsIgnoreCase("effect")) return MonsterCardType.EFFECT;
        else if (monsterCardType.equalsIgnoreCase("ritual")) return MonsterCardType.RITUAL;
        return null;
    }

    public static MonsterType getMonsterType(String monsterType) {
        if (monsterType.equalsIgnoreCase("warrior")) return MonsterType.WARRIOR;
        else if (monsterType.equalsIgnoreCase("beast-warrior")) return MonsterType.BEAST_WARRIOR;
        else if (monsterType.equalsIgnoreCase("aqua")) return MonsterType.AQUA;
        else if (monsterType.equalsIgnoreCase("fiend")) return MonsterType.FIEND;
        else if (monsterType.equalsIgnoreCase("pyro")) return MonsterType.PYRO;
        else if (monsterType.equalsIgnoreCase("spellcaster")) return MonsterType.SPELLCASTER;
        else if (monsterType.equalsIgnoreCase("thunder")) return MonsterType.THUNDER;
        else if (monsterType.equalsIgnoreCase("dragon")) return MonsterType.DRAGON;
        else if (monsterType.equalsIgnoreCase("machine")) return MonsterType.MACHINE;
        else if (monsterType.equalsIgnoreCase("insect")) return MonsterType.INSECT;
        else if (monsterType.equalsIgnoreCase("rock")) return MonsterType.ROCK;
        else if (monsterType.equalsIgnoreCase("fairy")) return MonsterType.FAIRY;
        else if (monsterType.equalsIgnoreCase("cyberse")) return MonsterType.CYBERSE;
        else if (monsterType.equalsIgnoreCase("sea serpent")) return MonsterType.SEA_SERPENT;
        else if (monsterType.equalsIgnoreCase("beast")) return MonsterType.BEAST;
        return null;
    }

    public static int getPriceByCardName(String cardName) {
        List<String[]> temp = readFromFile(monsterFileName);
        if (temp == null) {
            return -1;
        }
        for (String[] tempStringArray : temp) {
            for (String tempString : tempStringArray) {
                if (tempString.equalsIgnoreCase(cardName)) {
                    return Integer.parseInt(tempStringArray[8]);
                }
            }
        }
        temp = readFromFile(spellTrapFileName);
        if (temp == null) {
            return -1;
        }
        for (String[] tempStringArray : temp) {
            for (String tempString : tempStringArray) {
                if (tempString.equalsIgnoreCase(cardName)) {
                    return Integer.parseInt(tempStringArray[5]);
                }
            }
        }
        return -1;
    }

    public static TrapType getTrapType(String trapType) {
        if (trapType.equalsIgnoreCase("normal")) return TrapType.NORMAL;
        else if (trapType.equalsIgnoreCase("counter")) return TrapType.COUNTER;
        else if (trapType.equalsIgnoreCase("continuous")) return TrapType.CONTNUOUS;
        return null;
    }

    public static Limitation getLimitation(String limit) {
        if (limit.equalsIgnoreCase("unlimited")) return Limitation.UNLIMITED;
        else if (limit.equalsIgnoreCase("limited")) return Limitation.LIMITED;
        else if (limit.equalsIgnoreCase("forbidden")) return Limitation.FORBIDDEN;
        return null;
    }

    public static SpellType getSpellType(String spellName) {
        if (spellName.equalsIgnoreCase("quick-play")) return SpellType.QUICKPLAY;
        else if (spellName.equalsIgnoreCase("continuous")) return SpellType.CONTINUOUS;
        else if (spellName.equalsIgnoreCase("field")) return SpellType.FIELD;
        else if (spellName.equalsIgnoreCase("ritual")) return SpellType.RITUAL;
        else if (spellName.equalsIgnoreCase("equip")) return SpellType.EQUIP;
        return SpellType.NORMAL;
    }

    public static boolean cardNameExists(String cardName) {
        List<String[]> temp = readFromFile(monsterFileName);
        if (temp == null) {
            return false;
        }
        for (int i = 1; i < temp.size(); i++)
            if (cardName.equals(temp.get(i)[0])) return true;
        temp = readFromFile(spellTrapFileName);
        if (temp == null) {
            return false;
        }
        for (int i = 1; i < temp.size(); i++)
            if (cardName.equals(temp.get(i)[0])) return true;
        return false;
    }

    public static ArrayList<String> getCardNames() {
        ArrayList<String> outputArraylist = new ArrayList<String>();
        List<String[]> temp = readFromFile(monsterFileName);
        if (temp != null) {
            for (int i = 1; i < temp.size(); i++) {
                outputArraylist.add(temp.get(i)[0]);
            }
        }
        temp = readFromFile(spellTrapFileName);
        if (temp != null) {
            for (int i = 1; i < temp.size(); i++) {
                outputArraylist.add(temp.get(i)[0]);
            }
        }
        return outputArraylist;
    }

    public static Card getCardByName(String cardName) {
        List<String[]> temp = readFromFile(monsterFileName);
        if (temp != null) {
            for (int i = 1; i < temp.size(); i++) {
                if (temp.get(i)[0].equals(cardName)) return new Monster(cardName);
            }
        }
        temp = readFromFile(spellTrapFileName);
        if (temp != null) {
            for (int i = 1; i < temp.size(); i++) {
                if (temp.get(i)[0].equals(cardName)) {
                    if (temp.get(i)[1].equalsIgnoreCase("trap")) return new Trap(cardName);
                    return new Spell(cardName);
                }
            }
        }
        return null;
    }

}
