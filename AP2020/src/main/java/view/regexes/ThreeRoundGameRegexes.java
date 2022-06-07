package view.regexes;

import java.util.regex.Matcher;

abstract public class ThreeRoundGameRegexes {
    private static final String[] SWAP_CARDS_REGEX = new String[4];

    static {
        SWAP_CARDS_REGEX[0] = "swap --side --card (?<sideCardName>[\\w \\-,']+) --main --card (?<mainCardName>[\\w \\-,']+)";
        SWAP_CARDS_REGEX[1] = "swap --side --main --card (?<mainCardName>[\\w \\-,']+) --card (?<sideCardName>[\\w \\-,']+)";
        SWAP_CARDS_REGEX[2] = "swap -s -c (?<sideCardName>[\\w \\-,']+) -m -c (?<mainCardName>[\\w \\-,']+)";
        SWAP_CARDS_REGEX[3] = "swap -m -c (?<mainCardName>[\\w \\-,']+) -s -c (?<sideCardName>[\\w \\-,']+)";
    }

    public static String[] getSwapCards() {
        return SWAP_CARDS_REGEX;
    }

    public static boolean doesItSwapCardsCommand(String command) {
        String[] swapCardsRegex = getSwapCards();
        for (String swapCardRegex : swapCardsRegex) {
            if (command.matches(swapCardRegex)) return true;
        }
        return false;
    }

    public static Matcher getRightMatcherForSwapCards(String command) {
        String[] swapCardsRegex = getSwapCards();
        for (String swapCardRegex : swapCardsRegex) {
            if (command.matches(swapCardRegex))
                return RegexFunctions.getCommandMatcher(command, swapCardRegex);
        }
        return null;
    }
}
