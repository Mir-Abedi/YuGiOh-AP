package view.regexes;

abstract public class ShopMenuRegexes {
    public static String[] shopShowCardsRegex = new String[2];
    public static final String shopCardRegex = "^shop buy (?<cardName>.+)$";
    public static final String showHelp = "^help$";

    static {
        shopShowCardsRegex[0] = "^shop show --all$";
        shopShowCardsRegex[1] = "^shop show -a$";
    }

    public static boolean isItShowAllCards(String command) {
        return command.matches(shopShowCardsRegex[0]) || command.matches(shopShowCardsRegex[1]);
    }

}
