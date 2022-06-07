package view;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.database.CSVInfoGetter;
import model.card.Card;
import model.card.monster.Monster;
import model.card.spell_traps.Spell;
import model.card.spell_traps.Trap;
import java.util.ArrayList;
import java.util.Scanner;

public class ImportExportMenu {
    public void run(){
        String command;
        Scanner scanner = LoginMenu.getInstance().getScanner();
        System.out.println("Here you can convert your card from and to json .");
        while (true) {
            command = scanner.nextLine().trim();
            if (command.matches("^help$")) showHelp();
            else if (command.matches("menu exit")) {
                System.out.println("Returning to main menu");
                return;
            } else if (command.matches("menu show-current"))
                System.out.println("You are currently in the import export menu");
            else if (command.matches("export"))
                export();
            else if (command.matches("import"))
                importt();
            else System.out.println("Invalid command ..");
        }
    }

    public void export() {
        System.out.println("Please enter a card name :");
        String cardName = LoginMenu.getInstance().getScanner().nextLine();
        ArrayList<String> names = CSVInfoGetter.getCardNames();
        for (String name : names) {
            if (cardName.equalsIgnoreCase(name)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                System.out.println(gson.toJson(CSVInfoGetter.getCardByName(name)));
                return;
            }
        }
        System.out.println("Invalid Card name .. returning to import and export menu ..");
    }

    public void importt() {
        System.out.println("Please enter a valid json for card :");
        String cardJson = LoginMenu.getInstance().getScanner().nextLine();
        Gson gson = new GsonBuilder().create();
        Card card;
        try {
            card = gson.fromJson(cardJson, Monster.class);
        } catch (Exception e) {
            try {
                card = gson.fromJson(cardJson, Spell.class);
            } catch (Exception e1) {
                try {
                    card = gson.fromJson(cardJson, Trap.class);
                } catch (Exception e2) {
                    System.out.println("This json is not valid ! returning to import and export menu ..");
                    return;
                }
            }
        }
        System.out.println(card);
    }

    public static void showHelp() {
        System.out.println("import\nexport\nmenu show-current\nmenu exit\n");
    }
}
