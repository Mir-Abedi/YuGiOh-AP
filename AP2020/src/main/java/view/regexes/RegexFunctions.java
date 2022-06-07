package view.regexes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class RegexFunctions {
    public static Matcher getCommandMatcher(String command, String regex) {
        Pattern commandPattern = Pattern.compile(regex);
        return commandPattern.matcher(command);
    }

}
