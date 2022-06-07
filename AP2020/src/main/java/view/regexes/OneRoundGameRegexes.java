package view.regexes;

import java.util.regex.Matcher;

abstract public class OneRoundGameRegexes {
    public static final String deselectCard = "select -d";
    public static final String nextPhase = "next phase";
    public static final String summon = "summon";
    public static final String set = "set";
    public static final String SHOW_TABLE = "^show table$";
    public static final String SHOW_PHASE = "^show phase$";
    public static final String flipSummon = "flip-summon";
    public static final String attackToOpponentMonster = "attack (?<cellNumber>\\d+)";
    public static final String SHOW_GRAVEYARD_RIVAL = "show graveyard -r";
    public static final String attackDirect = "attack direct";
    public static final String activeEffect = "active effect";
    public static final String showGraveyard = "show graveyard";
    public static final String showHandCards = "show all hand cards";
    public static final String[] showCard = new String[2];
    static {
        showCard[0] = "card show --selected";
        showCard[1] = "card show -s";
    }
    public static final String surrender = "surrender";


    private static final String[] selectMyMonsterCell = new String[2];

    static {
        selectMyMonsterCell[0] = "select --monster (?<cellNumber>\\d+)";
        selectMyMonsterCell[1] = "select -m (?<cellNumber>\\d+)";
    }

    private static final String[] selectMySpellCell = new String[2];

    static {
        selectMySpellCell[0] = "select --spell (?<cellNumber>\\d+)";
        selectMySpellCell[1] = "select -s (?<cellNumber>\\d+)";
    }

    private static final String[] selectMyFieldZone = new String[2];

    static {
        selectMyFieldZone[0] = "select --field";
        selectMyFieldZone[1] = "select -f";
    }

    private static final String[] selectCardFromMyHand = new String[2];

    static {
        selectCardFromMyHand[0] = "select --hand (?<cellNumber>\\d+)";
        selectCardFromMyHand[1] = "select -h (?<cellNumber>\\d+)";
    }

    private static final String[] selectOpponentMonsterCell = new String[4];

    static {
        selectOpponentMonsterCell[0] = "select --monster (?<cellNumber>\\d+) --opponent";
        selectOpponentMonsterCell[1] = "select --opponent --monster (?<cellNumber>\\d+)";
        selectOpponentMonsterCell[2] = "select -m (?<cellNumber>\\d+) -op";
        selectOpponentMonsterCell[3] = "select -op -m (?<cellNumber>\\d+)";
    }

    private static final String[] selectOpponentSpellCell = new String[4];

    static {
        selectOpponentSpellCell[0] = "select --spell (?<cellNumber>\\d+) --opponent";
        selectOpponentSpellCell[1] = "select --opponent --spell (?<cellNumber>\\d+)";
        selectOpponentSpellCell[2] = "select -s (?<cellNumber>\\d+) -op";
        selectOpponentSpellCell[3] = "select -op -s (?<cellNumber>\\d+)";
    }

    private static final String[] selectOpponentFieldZone = new String[4];

    static {
        selectOpponentFieldZone[0] = "select --field --opponent";
        selectOpponentFieldZone[1] = "select --opponent --field";
        selectOpponentFieldZone[2] = "select -f -op";
        selectOpponentFieldZone[3] = "select -op -f";
    }

    private static final String[] setAttack = new String[2];

    static {
        setAttack[0] = "set --position attack";
        setAttack[1] = "set -p attack";
    }

    private static final String[] setDefense = new String[2];

    static {
        setDefense[0] = "set --position defense";
        setDefense[1] = "set -p defence";
    }

    public static String[] getSelectMyMonsterCell() {
        return selectMyMonsterCell;
    }

    public static String[] getSelectMySpellCell() {
        return selectMySpellCell;
    }

    public static String[] getSelectMyFieldZone() {
        return selectMyFieldZone;
    }

    public static String[] getSelectCardFromMyHand() {
        return selectCardFromMyHand;
    }

    public static String[] getSelectOpponentMonsterCell() {
        return selectOpponentMonsterCell;
    }

    public static String[] getSelectOpponentSpellCell() {
        return selectOpponentSpellCell;
    }

    public static String[] getSelectOpponentFieldZone() {
        return selectOpponentFieldZone;
    }

    public static String[] getSetAttack() {
        return setAttack;
    }

    public static String[] getSetDefense() {
        return setDefense;
    }

    public static boolean doesItSelectMyMonsterCellCommand(String command) {
        String[] selectMyMonsterCellRegex = getSelectMyMonsterCell();
        for (String myMonsterCellRegex : selectMyMonsterCellRegex) {
            if (command.matches(myMonsterCellRegex)) return true;
        }
        return false;
    }

    public static boolean doesItSelectMySpellCellCommand(String command) {
        String[] selectMySpellCellRegex = getSelectMySpellCell();
        for (String mySpellCellRegex : selectMySpellCellRegex) {
            if (command.matches(mySpellCellRegex)) return true;
        }
        return false;
    }

    public static boolean doesItSelectMyFieldZoneCommand(String command) {
        String[] selectMyFieldZoneRegex = getSelectMyFieldZone();
        for (String myFieldZoneRegex : selectMyFieldZoneRegex) {
            if (command.matches(myFieldZoneRegex)) return true;
        }
        return false;
    }

    public static boolean doesItSelectCardFromMyHandCommand(String command) {
        String[] selectCardFromMyHandRegex = getSelectCardFromMyHand();
        for (String cardFromMyHandRegex : selectCardFromMyHandRegex) {
            if (command.matches(cardFromMyHandRegex)) return true;
        }
        return false;
    }

    public static boolean doesItSelectOpponentMonsterCellCommand(String command) {
        String[] selectOpponentMonsterCellRegex = getSelectOpponentMonsterCell();
        for (String opponentMonsterCellRegex : selectOpponentMonsterCellRegex) {
            if (command.matches(opponentMonsterCellRegex)) return true;
        }
        return false;
    }

    public static boolean doesItSelectOpponentSpellCellCommand(String command) {
        String[] selectOpponentSpellCellRegex = getSelectOpponentSpellCell();
        for (String opponentSpellCellRegex : selectOpponentSpellCellRegex) {
            if (command.matches(opponentSpellCellRegex)) return true;
        }
        return false;
    }

    public static boolean doesItSelectOpponentFieldZoneCommand(String command) {
        String[] selectOpponentFieldZoneRegex = getSelectOpponentFieldZone();
        for (String opponentFieldZoneRegex : selectOpponentFieldZoneRegex) {
            if (command.matches(opponentFieldZoneRegex)) return true;
        }
        return false;
    }

    public static boolean doesItSetAttackCommand(String command) {
        String[] setAttackRegex = getSetAttack();
        for (String attackRegex : setAttackRegex) {
            if (command.matches(attackRegex)) return true;
        }
        return false;
    }

    public static boolean doesItSetDefenseCommand(String command) {
        String[] setDefenseRegex = getSetDefense();
        for (String defenseRegex : setDefenseRegex) {
            if (command.matches(defenseRegex)) return true;
        }
        return false;
    }

    public static Matcher getRightMatcherForSelectMyMonsterCell(String command) {
        String[] selectMyMonsterCellRegex = getSelectMyMonsterCell();
        for (String myMonsterCellRegex : selectMyMonsterCellRegex) {
            if (command.matches(myMonsterCellRegex))
                return RegexFunctions.getCommandMatcher(command, myMonsterCellRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForSelectMySpellCell(String command) {
        String[] selectMySpellCellRegex = getSelectMySpellCell();
        for (String mySpellCellRegex : selectMySpellCellRegex) {
            if (command.matches(mySpellCellRegex)) return RegexFunctions.getCommandMatcher(command, mySpellCellRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForSelectMyFieldZone(String command) {
        String[] selectMyFieldZoneRegex = getSelectMyFieldZone();
        for (String myFieldZoneRegex : selectMyFieldZoneRegex) {
            if (command.matches(myFieldZoneRegex)) return RegexFunctions.getCommandMatcher(command, myFieldZoneRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForSelectCardFromMyHand(String command) {
        String[] selectCardFromMyHandRegex = getSelectCardFromMyHand();
        for (String cardFromMyHandRegex : selectCardFromMyHandRegex) {
            if (command.matches(cardFromMyHandRegex))
                return RegexFunctions.getCommandMatcher(command, cardFromMyHandRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForSelectOpponentMonsterCell(String command) {
        String[] selectOpponentMonsterCellRegex = getSelectOpponentMonsterCell();
        for (String opponentMonsterCellRegex : selectOpponentMonsterCellRegex) {
            if (command.matches(opponentMonsterCellRegex))
                return RegexFunctions.getCommandMatcher(command, opponentMonsterCellRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForSelectOpponentSpellCell(String command) {
        String[] selectOpponentSpellCellRegex = getSelectOpponentSpellCell();
        for (String opponentSpellCellRegex : selectOpponentSpellCellRegex) {
            if (command.matches(opponentSpellCellRegex))
                return RegexFunctions.getCommandMatcher(command, opponentSpellCellRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForSelectOpponentFieldZone(String command) {
        String[] selectOpponentFieldZoneRegex = getSelectOpponentFieldZone();
        for (String opponentFieldZoneRegex : selectOpponentFieldZoneRegex) {
            if (command.matches(opponentFieldZoneRegex))
                return RegexFunctions.getCommandMatcher(command, opponentFieldZoneRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForSetAttack(String command) {
        String[] setAttackRegex = getSetAttack();
        for (String attackRegex : setAttackRegex) {
            if (command.matches(attackRegex)) return RegexFunctions.getCommandMatcher(command, attackRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForSetDefense(String command) {
        String[] setDefenseRegex = getSetDefense();
        for (String defenseRegex : setDefenseRegex) {
            if (command.matches(defenseRegex)) return RegexFunctions.getCommandMatcher(command, defenseRegex);
        }
        return null;
    }

    public static boolean showSelectedCard(String command) {
        return command.matches(showCard[0]) || command.matches(showCard[1]);
    }
}
