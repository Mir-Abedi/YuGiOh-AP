package view;

import controller.LoginMenuController;
import view.duelMenu.DuelMenu;
import view.regexes.CheatRegex;
import view.regexes.RegexFunctions;
import view.responses.MainMenuResponses;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu {
    private final Scanner scanner;
    private static MainMenu mainMenu;

    private MainMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public static MainMenu getInstance(Scanner scanner) {
        if (mainMenu == null) mainMenu = new MainMenu(scanner);
        return mainMenu;
    }

    public void run() {
        String command;
        while (true) {
            command = scanner.nextLine().trim().toLowerCase();
            if (command.matches("^menu enter (?<menuName>\\w+)$"))
                gotoMenu(command);
            else if (command.matches("menu show-current"))
                respond(MainMenuResponses.CURRENT_MENU_MAIN_MENU);
            else if (command.matches("logout")) {
                logout();
                return;
            } else if (command.matches("help")) showHelp();
            else if (command.matches(CheatRegex.INCREASE_MONEY))
                LoginMenuController.getCurrentUser().increaseBalance(getInt(command));
            else respond(MainMenuResponses.INVALID_COMMAND);
        }
    }

    public static int getInt(String string) {
        Matcher matcher = RegexFunctions.getCommandMatcher(string, "(\\d+)");
        matcher.find();
        return Integer.parseInt(matcher.group(1));
    }

    private void gotoMenu(String command) {
        Matcher matcher = RegexFunctions.getCommandMatcher(command, "^menu enter (?<menuName>\\w+)$");
        if (matcher.find()) {
            String menuName = matcher.group("menuName");
            try {
                Method method = MainMenu.class.getDeclaredMethod(menuName);
                method.invoke(this);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                respond(MainMenuResponses.INVALID_MENU);
                e.printStackTrace(); // todo remove
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void respond(MainMenuResponses response) {
        if (response.equals(MainMenuResponses.CURRENT_MENU_MAIN_MENU))
            System.out.println("you are in main menu");
        else if (response.equals(MainMenuResponses.INVALID_COMMAND))
            System.out.println("invalid command!");
        else if (response.equals(MainMenuResponses.INVALID_MENU))
            System.out.println("menu navigation is not possible");
        else if (response.equals(MainMenuResponses.LOGOUT_SUCCESSFUL))
            System.out.println("user logged out successfully!");
    }

    private void shop(){
        System.out.println("Now Entering shop ..");
        ShopMenu shopMenu = ShopMenu.getInstance(scanner);
        shopMenu.run();
    }

    public void scoreboard() {
        System.out.println("Now Entering scoreboard menu ..");
        ScoreboardMenu scoreboardMenu = ScoreboardMenu.getInstance(scanner);
        scoreboardMenu.run();
    }

    private void profile(){
        System.out.println("Now Entering profile menu ..");
        ProfileMenu profileMenu = ProfileMenu.getInstance(scanner);
        profileMenu.run();
    }

    private void deck(){
        System.out.println("Now Entering deck menu ..");
        DeckMenu deckMenu = DeckMenu.getInstance(scanner);
        deckMenu.run();
    }

    private void importexport() {
        System.out.println("Now entering import export ..");
        new ImportExportMenu().run();
    }

    private void duel() {
        System.out.println("Now Entering duel menu  ..");
        try {
            DuelMenu.getInstance(LoginMenu.getInstance().getScanner()).run();
        } catch (CloneNotSupportedException ignored) {
            System.out.println("problem occurred .. please try again");
        }
    }

    private void logout() {
        LoginMenuController.logout();
        respond(MainMenuResponses.LOGOUT_SUCCESSFUL);
    }

    public void showHelp() {
        String help = "logout\n";
        help += "menu show-current\n";
        help += "menu enter <menu name>\nAvailable menus : \n\tduel\n\tshop\n\tscoreboard\n\tdeck\n\tprofile\n\timportexport";
        System.out.println(help);
    }

}
