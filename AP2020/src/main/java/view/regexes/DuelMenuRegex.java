package view.regexes;

import java.util.regex.Matcher;

abstract public class DuelMenuRegex {
    private static final String[] duelNewPlayer = new String[12];

    static {
        duelNewPlayer[0] = "duel --new --second-player (?<username>.+) --rounds (?<rounds>\\d+)";
        duelNewPlayer[1] = "duel --new --rounds (?<rounds>\\d+) --second-player (?<username>.+)";
        duelNewPlayer[2] = "duel --second-player (?<username>.+) --new --rounds (?<rounds>\\d+)";
        duelNewPlayer[3] = "duel --second-player (?<username>.+) --rounds (?<rounds>\\d+) --new";
        duelNewPlayer[4] = "duel --rounds (?<rounds>\\d+) --new --second-player (?<username>.+)";
        duelNewPlayer[5] = "duel --rounds (?<rounds>\\d+) --second-player (?<username>.+) --new";
        duelNewPlayer[6] = "duel -n -s-p (?<username>.+) -r (?<rounds>\\d+)";
        duelNewPlayer[7] = "duel -n -r (?<rounds>\\d+) -s-p (?<username>.+)";
        duelNewPlayer[8] = "duel -r (?<rounds>\\d+) -s-p (?<username>.+) -n";
        duelNewPlayer[9] = "duel -r (?<rounds>\\d+) -n -s-p (?<username>.+)";
        duelNewPlayer[10] = "duel -s-p (?<username>.+) -n -r (?<rounds>\\d+)";
        duelNewPlayer[11] = "duel -s-p (?<username>.+) -r (?<rounds>\\d+) -n";
    }

    private static final String[] duelNewAi = new String[48];

    static {
        duelNewAi[0] = "duel --new --ai --rounds (?<rounds>\\d+) --difficulty (?<difficulty>[A-Za-z]+)";
        duelNewAi[1] = "duel --new --ai --difficulty (?<difficulty>[A-Za-z]+) --rounds (?<rounds>\\d+)";
        duelNewAi[2] = "duel --new --rounds (?<rounds>\\d+) --ai --difficulty (?<difficulty>[A-Za-z]+)";
        duelNewAi[3] = "duel --new --rounds (?<rounds>\\d+) --difficulty (?<difficulty>[A-Za-z]+) --ai";
        duelNewAi[4] = "duel --new --difficulty (?<difficulty>[A-Za-z]+) --ai --rounds (?<rounds>\\d+)";
        duelNewAi[5] = "duel --new --difficulty (?<difficulty>[A-Za-z]+) --rounds (?<rounds>\\d+) --ai";

        duelNewAi[6] = "duel --difficulty (?<difficulty>[A-Za-z]+) --new --ai --rounds (?<rounds>\\d+)";
        duelNewAi[7] = "duel --difficulty (?<difficulty>[A-Za-z]+) --new --rounds (?<rounds>\\d+) --ai";
        duelNewAi[8] = "duel --difficulty (?<difficulty>[A-Za-z]+) --ai --new --rounds (?<rounds>\\d+)";
        duelNewAi[9] = "duel --difficulty (?<difficulty>[A-Za-z]+) --ai --rounds (?<rounds>\\d+) --new";
        duelNewAi[10] = "duel --difficulty (?<difficulty>[A-Za-z]+) --rounds (?<rounds>\\d+) --new --ai";
        duelNewAi[11] = "duel --difficulty (?<difficulty>[A-Za-z]+) --rounds (?<rounds>\\d+) --ai --new";

        duelNewAi[12] = "duel --ai --new --rounds (?<rounds>\\d+) --difficulty (?<difficulty>[A-Za-z]+)";
        duelNewAi[13] = "duel --ai --new --difficulty (?<difficulty>[A-Za-z]+) --rounds (?<rounds>\\d+)";
        duelNewAi[14] = "duel --ai --rounds (?<rounds>\\d+) --new --difficulty (?<difficulty>[A-Za-z]+)";
        duelNewAi[15] = "duel --ai --rounds (?<rounds>\\d+) --difficulty (?<difficulty>[A-Za-z]+) --new";
        duelNewAi[16] = "duel --ai --difficulty (?<difficulty>[A-Za-z]+) --new --rounds (?<rounds>\\d+)";
        duelNewAi[17] = "duel --ai --difficulty (?<difficulty>[A-Za-z]+) --rounds (?<rounds>\\d+) --new";

        duelNewAi[18] = "duel --rounds (?<rounds>\\d+) --new --ai --difficulty (?<difficulty>[A-Za-z]+)";
        duelNewAi[19] = "duel --rounds (?<rounds>\\d+) --new --difficulty (?<difficulty>[A-Za-z]+) --ai";
        duelNewAi[20] = "duel --rounds (?<rounds>\\d+) --ai --new --difficulty (?<difficulty>[A-Za-z]+)";
        duelNewAi[21] = "duel --rounds (?<rounds>\\d+) --ai --difficulty (?<difficulty>[A-Za-z]+) --new";
        duelNewAi[22] = "duel --rounds (?<rounds>\\d+) --difficulty (?<difficulty>[A-Za-z]+) --new --ai";
        duelNewAi[23] = "duel --rounds (?<rounds>\\d+) --difficulty (?<difficulty>[A-Za-z]+) --ai --new";

        duelNewAi[24] = "duel -n -a -r (?<rounds>\\d+) -d (?<difficulty>[A-Za-z]+)";
        duelNewAi[25] = "duel -n -a -d (?<difficulty>[A-Za-z]+) -r (?<rounds>\\d+)";
        duelNewAi[26] = "duel -n -r (?<rounds>\\d+) -a -d (?<difficulty>[A-Za-z]+)";
        duelNewAi[27] = "duel -n -r (?<rounds>\\d+) -d (?<difficulty>[A-Za-z]+) -a";
        duelNewAi[28] = "duel -n -d (?<difficulty>[A-Za-z]+) -a -r (?<rounds>\\d+)";
        duelNewAi[29] = "duel -n -d (?<difficulty>[A-Za-z]+) -r (?<rounds>\\d+) -a";

        duelNewAi[30] = "duel -d (?<difficulty>[A-Za-z]+) -n -a -r (?<rounds>\\d+)";
        duelNewAi[31] = "duel -d (?<difficulty>[A-Za-z]+) -n -r (?<rounds>\\d+) -a";
        duelNewAi[32] = "duel -d (?<difficulty>[A-Za-z]+) -a -n -r (?<rounds>\\d+)";
        duelNewAi[33] = "duel -d (?<difficulty>[A-Za-z]+) -a -r (?<rounds>\\d+) -n";
        duelNewAi[34] = "duel -d (?<difficulty>[A-Za-z]+) -r (?<rounds>\\d+) -n -a";
        duelNewAi[35] = "duel -d (?<difficulty>[A-Za-z]+) -r (?<rounds>\\d+) -a -n";

        duelNewAi[36] = "duel -a -n -r (?<rounds>\\d+) -d (?<difficulty>[A-Za-z]+)";
        duelNewAi[37] = "duel -a -n -d (?<difficulty>[A-Za-z]+) -r (?<rounds>\\d+)";
        duelNewAi[38] = "duel -a -r (?<rounds>\\d+) -n -d (?<difficulty>[A-Za-z]+)";
        duelNewAi[39] = "duel -a -r (?<rounds>\\d+) -d (?<difficulty>[A-Za-z]+) -n";
        duelNewAi[40] = "duel -a -d (?<difficulty>[A-Za-z]+) -n -r (?<rounds>\\d+)";
        duelNewAi[41] = "duel -a -d (?<difficulty>[A-Za-z]+) -r (?<rounds>\\d+) -n";

        duelNewAi[42] = "duel -r (?<rounds>\\d+) -n -a -d (?<difficulty>[A-Za-z]+)";
        duelNewAi[43] = "duel -r (?<rounds>\\d+) -n -d (?<difficulty>[A-Za-z]+) -a";
        duelNewAi[44] = "duel -r (?<rounds>\\d+) -a -n -d (?<difficulty>[A-Za-z]+)";
        duelNewAi[45] = "duel -r (?<rounds>\\d+) -a -d (?<difficulty>[A-Za-z]+) -n";
        duelNewAi[46] = "duel -r (?<rounds>\\d+) -d (?<difficulty>[A-Za-z]+) -n -a";
        duelNewAi[47] = "duel -r (?<rounds>\\d+) -d (?<difficulty>[A-Za-z]+) -a -n";
    }

    public static String[] getDuelNewPlayer() {
        return duelNewPlayer;
    }

    public static String[] getDuelNewAi() {
        return duelNewAi;
    }

    public static Matcher getRightMatcherForDuelNewPlayer(String command) {
        String[] duelNewPlayerRegex = getDuelNewPlayer();
        for (String newPlayerRegex : duelNewPlayerRegex) {
            if (command.matches(newPlayerRegex)) return RegexFunctions.getCommandMatcher(command, newPlayerRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForDuelNewAi(String command) {
        String[] duelNewAiRegex = getDuelNewAi();
        for (String newPlayerRegex : duelNewAiRegex) {
            if (command.matches(newPlayerRegex)) return RegexFunctions.getCommandMatcher(command, newPlayerRegex);
        }
        return null;
    }

    public static boolean doesItDuelNewPlayerCommand(String command) {
        String[] duelNewPlayerRegex = getDuelNewPlayer();
        for (String newPlayerRegex : duelNewPlayerRegex) {
            if (command.matches(newPlayerRegex)) return true;
        }
        return false;
    }

    public static boolean doesItDuelNewAiCommand(String command) {
        String[] duelNewAiRegex = getDuelNewAi();
        for (String newPlayerRegex : duelNewAiRegex) {
            if (command.matches(newPlayerRegex)) return true;
        }
        return false;
    }
}
