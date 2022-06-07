package view;

import view.regexes.RegexFunctions;
import view.regexes.TributeMenuRegex;

import java.util.Scanner;
import java.util.regex.Matcher;

public class TributeMenu {

    public static int[] run(int numberOfTributes) {
        Scanner scanner = LoginMenu.getInstance().getScanner();
        System.out.println("Please select " + numberOfTributes + " cards that you want to tribute." +
                "\nCard number must be between 1 and 5 and please select them in this format :1 2 3 ..");
        String command;
        while (true) {
            command = scanner.nextLine().trim().toLowerCase();
            if (command.matches(TributeMenuRegex.CANCEL_REGEX)) return null;
            Matcher matcher = RegexFunctions.getCommandMatcher(command, TributeMenuRegex.NUMBERS_REGEX);
            if (!matcher.find()) {
                System.out.println("Invalid Command");
                continue;
            }
            String[] stringArray = command.split(" ");
            int[] temp = new int[stringArray.length];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = Integer.parseInt(stringArray[i]) - 1;
            }
            if (temp.length == numberOfTributes) return temp;
            else System.out.println("Invalid Command");
        }
    }

    public static void invalidTributes() {
        System.out.println("You cant tribute this card(s) .. please select again or type cancel to abort");
    }
}
