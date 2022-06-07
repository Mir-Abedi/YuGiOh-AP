package view.regexes;

import java.util.regex.Matcher;

abstract public class LoginMenuRegex {
    private static final String[] createUserRegex = new String[12];
    private static final String[] loginRegex = new String[4];
    public static final String showHelp = "^help$";

    static {
        createUserRegex[0] = "^user create --username (?<username>.+) --nickname (?<nickname>.+) --password (?<password>\\S+)$";
        createUserRegex[1] = "^user create --nickname (?<nickname>.+) --username (?<username>.+) --password (?<password>\\S+)$";
        createUserRegex[2] = "^user create --nickname (?<nickname>.+) --password (?<password>\\S+) --username (?<username>.+)$";
        createUserRegex[3] = "^user create --password (?<password>\\S+) --nickname (?<nickname>.+) --username (?<username>.+)$";
        createUserRegex[4] = "^user create --password (?<password>\\S+) --username (?<username>.+) --nickname (?<nickname>.+)$";
        createUserRegex[5] = "^user create --username (?<username>.+) --password (?<password>\\S+) --nickname (?<nickname>.+)$";

        createUserRegex[6] = "^user create -u (?<username>.+) -n (?<nickname>.+) -p (?<password>\\S+)$";
        createUserRegex[7] = "^user create -n (?<nickname>.+) -u (?<username>.+) -p (?<password>\\S+)$";
        createUserRegex[8] = "^user create -n (?<nickname>.+) -p (?<password>\\S+) -u (?<username>.+)$";
        createUserRegex[9] = "^user create -p (?<password>\\S+) -n (?<nickname>.+) -u (?<username>.+)$";
        createUserRegex[10] = "^user create -p (?<password>\\S+) -u (?<username>.+) -n (?<nickname>.+)$";
        createUserRegex[11] = "^user create -u (?<username>.+) -p (?<password>\\S+) -n (?<nickname>.+)$";
    }

    static {
        loginRegex[0] = "^user login --password (?<password>\\S+) --username (?<username>.+)$";
        loginRegex[1] = "^user login --username (?<username>.+) --password (?<password>\\S+)$";
        loginRegex[2] = "^user login -u (?<username>.+) -p (?<password>\\S+)$";
        loginRegex[3] = "^user login -p (?<password>\\S+) -u (?<username>.+)$";
    }

    public static String[] getLoginRegex() {
        return loginRegex;
    }

    public static String[] getCreateUserRegex() {
        return createUserRegex;
    }

    public static Matcher getRightMatcherForCreateUser(String command) {
        String[] createUserRegex = getCreateUserRegex();
        for (String userRegex : createUserRegex) {
            if (command.matches(userRegex)) return RegexFunctions.getCommandMatcher(command, userRegex);
        }
        return null;
    }

    public static Matcher getRightMatcherForLogin(String command) {
        String[] loginRegex = getLoginRegex();
        for (String regex : loginRegex) {
            if (command.matches(regex)) return RegexFunctions.getCommandMatcher(command, regex);
        }
        return null;
    }

    public static boolean doesItLoginCommand(String command) {
        String[] loginRegex = getLoginRegex();
        for (String regex : loginRegex) {
            if (command.matches(regex)) return true;
        }
        return false;
    }

    public static boolean doesItCreateUserCommand(String command) {
        String[] createUserRegex = getCreateUserRegex();
        for (String userRegex : createUserRegex) {
            if (command.matches(userRegex)) return true;
        }
        return false;
    }

}
