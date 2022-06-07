package view;

import controller.LoginMenuController;
import view.regexes.LoginMenuRegex;
import view.responses.LoginMenuResponses;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;

public class LoginMenu {
    private Scanner scanner;
    private static LoginMenu loginMenu;

    private LoginMenu() {
        scanner = new Scanner(System.in);
    }

    public static LoginMenu getInstance() {
        if (loginMenu == null)
            loginMenu = new LoginMenu();
        return loginMenu;
    }

    public void run() {
        String command;
        while (true) {
            command = scanner.nextLine().trim();
            if (LoginMenuRegex.doesItLoginCommand(command))
                login(command);
            else if (LoginMenuRegex.doesItCreateUserCommand(command))
                createUser(command);
            else if (command.matches("help"))
                showHelp();
            else if (command.matches("menu show-current"))
                respond(LoginMenuResponses.CURRENT_MENU_LOGIN_MENU);
            else if (command.matches("exit menu")) return;
            else respond(LoginMenuResponses.INVALID_COMMAND);
        }

    }

    private void login(String command) {
        HashMap<String, String> data = parseLoginData(command);
        String username = data.get("username");
        String password = data.get("password");
        LoginMenuResponses response = LoginMenuController.login(username, password);
        respond(response);
        if (response.equals(LoginMenuResponses.USER_LOGIN_SUCCESSFUL)) {
            MainMenu mainMenu = MainMenu.getInstance(scanner);
            mainMenu.run();
        }
    }

    private void createUser(String command) {
        HashMap<String, String> data = parseCreateUserData(command);
        String username = data.get("username");
        String password = data.get("password");
        String nickname = data.get("nickname");
        LoginMenuResponses response = LoginMenuController.createUser(username, nickname, password);
        respond(response);
    }

    private void respond(LoginMenuResponses response) {
        if (response.equals(LoginMenuResponses.INVALID_COMMAND))
            System.out.println("invalid command!");
        else if (response.equals(LoginMenuResponses.USER_LOGIN_SUCCESSFUL))
            System.out.println("user login successful!");
        else if (response.equals(LoginMenuResponses.PASSWORD_AND_USERNAME_DIDNT_MATCH))
            System.out.println("password and username didnt match! try again");
        else if (response.equals(LoginMenuResponses.THERE_IS_NOT_A_USER_WITH_THIS_USERNAME))
            System.out.println("There is not a user with this username!");
        else if (response.equals(LoginMenuResponses.USER_CREATED_SUCCESSFULLY))
            System.out.println("user created successfully!");
        else if (response.equals(LoginMenuResponses.USER_WITH_THIS_NICKNAME_EXITS))
            System.out.println("user with this nickname already exists!");
        else if (response.equals(LoginMenuResponses.USER_WITH_THIS_USERNAME_EXITS))
            System.out.println("user with this username already exists!");
        else if (response.equals(LoginMenuResponses.CURRENT_MENU_LOGIN_MENU))
            System.out.println("you are in login menu");
    }

    private HashMap<String, String> parseLoginData(String command) {
        HashMap<String, String> parsedInfo = new HashMap<>();
        Matcher matcher = LoginMenuRegex.getRightMatcherForLogin(command);
        if (matcher.find()) {
            parsedInfo.put("username", matcher.group("username"));
            parsedInfo.put("password", matcher.group("password"));
        }
        return parsedInfo;
    }

    private HashMap<String, String> parseCreateUserData(String command) {
        HashMap<String, String> parsedInfo = new HashMap<>();
        Matcher matcher = LoginMenuRegex.getRightMatcherForCreateUser(command);
        if (matcher.find()) {
            parsedInfo.put("username", matcher.group("username"));
            parsedInfo.put("password", matcher.group("password"));
            parsedInfo.put("nickname", matcher.group("nickname"));
        }
        return parsedInfo;
    }

    public void showHelp() {
        String help = "user create --username <username> --nickname <nickname> --password <password>\n";
        help += "user login --username <username> --password <password>\n";
        help += "menu show-current\n";
        help += "exit menu";
        System.out.println(help);
    }

    public Scanner getScanner() {
        return this.scanner;
    }

}
