package view.regexes;

import java.util.regex.Matcher;

abstract public class ProfileMenuRegex {
    public static final String changeNicknameRegex = "^profile change --nickname (?<nickname>[\\w]+)$";
    public static final String changeNicknameRegexShort = "^profile change -n (?<nickname>[\\w]+)$";
    public static final String changePasswordRegexType1 = "^profile change --password --current (?<currentPassword>\\S+) --new (?<newPassword>\\S+)$";
    public static final String changePasswordRegexType2 = "^profile change --password --new (?<newPassword>\\S+) --current (?<currentPassword>\\S+)$";
    public static final String changePasswordRegexType1Short = "^profile change -p -c (?<currentPassword>\\S+) -n (?<newPassword>\\S+)$";
    public static final String changePasswordRegexType2Short = "^profile change -p -n (?<newPassword>\\S+) -c (?<currentPassword>\\S+)$";
    public static final String showHelp = "^help$";


    public static Matcher getRightMatcherForChangePassword(String command) {
        Matcher matcher;
        if (command.matches(changePasswordRegexType1))
            matcher = RegexFunctions.getCommandMatcher(command, changePasswordRegexType1);
        else if (command.matches(changePasswordRegexType1Short))
            matcher = RegexFunctions.getCommandMatcher(command, changePasswordRegexType1Short);
        else if (command.matches(changePasswordRegexType2))
            matcher = RegexFunctions.getCommandMatcher(command, changePasswordRegexType2);
        else if (command.matches(changePasswordRegexType2Short))
            matcher = RegexFunctions.getCommandMatcher(command, changePasswordRegexType2Short);
        else matcher = null;
        return matcher;
    }

    public static Matcher getRightMatcherForChangeNickname(String command) {
        Matcher matcher;
        if (command.matches(changeNicknameRegex))
            matcher = RegexFunctions.getCommandMatcher(command, changeNicknameRegex);
        else if (command.matches(changeNicknameRegexShort))
            matcher = RegexFunctions.getCommandMatcher(command, changeNicknameRegexShort);
        else matcher = null;
        return matcher;
    }
}
