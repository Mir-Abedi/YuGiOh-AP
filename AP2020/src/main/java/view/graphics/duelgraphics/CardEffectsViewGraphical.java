package view.graphics.duelgraphics;

import controller.database.CSVInfoGetter;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.Main;
import model.card.Card;
import model.deck.Deck;
import model.deck.Graveyard;
import model.game.Cell;
import model.game.State;
import view.LoginMenu;
import view.graphics.Menu;
import view.responses.CardEffectsResponses;
import view.responses.HowToSummon;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class CardEffectsViewGraphical {

    public static  boolean doYouWantTo(String message) {
        return false;
        // TODO: 7/10/2021
    }

     public static void respond(CardEffectsResponses response) {
        if (response == CardEffectsResponses.CANT_RITUAL_SUMMON)
            Menu.showAlert("You cant ritual summon !");
        else if (response == CardEffectsResponses.HAVE_NO_CARDS)
            Menu.showAlert("You have no cards !");
        else if (response == CardEffectsResponses.INVALID_CELL_NUMBER)
            Menu.showAlert("Invalid Cell number !");
        else if (response == CardEffectsResponses.MONSTER_ZONE_IS_FULL)
            Menu.showAlert("Monster zone is full !");
        else if (response == CardEffectsResponses.PLEASE_SELECT_A_FIELD_SPELL)
            Menu.showAlert("Please select a field spell !");
        else if (response == CardEffectsResponses.PLEASE_SELECT_AN_SPELL)
            Menu.showAlert("Please select a spell !");
        else if (response == CardEffectsResponses.PLEASE_SELECT_A_VALID_TYPE)
            Menu.showAlert("Please select a valid type ! ");
        else if (response == CardEffectsResponses.YOU_DONT_HAVE_ANY_FIELD_SPELL)
            Menu.showAlert("You dont have any field spell !");
        else if (response == CardEffectsResponses.NO_MONSTERS)
            Menu.showAlert("You have no monsters !");
        else if (response == CardEffectsResponses.PLEASE_SELECT_MONSTER)
            Menu.showAlert("Please select monster !");
        else if (response == CardEffectsResponses.PLEASE_SELECT_A_VALID_NUMBER)
            Menu.showAlert("Please select a valid number !");
        else if (response == CardEffectsResponses.SPECIAL_SUMMON_NOW)
            Menu.showAlert("You have to special summon now !");
        else if (response == CardEffectsResponses.PLEASE_SELECT_LEVEL_7_OR_MORE)
            Menu.showAlert("Please select level 7 or more monster !");
        else if (response == CardEffectsResponses.PLEASE_SELECT_A_VALID_MONSTER)
            Menu.showAlert("Please select a valid monster !");
        else if (response == CardEffectsResponses.CANT_ACTIVATE_TRAP)
            Menu.showAlert("You cant activate trap !");
    }

    private static Card chooseCardFromList(ArrayList<Card> cards, String message) {
        if (cards.size() == 0) return null;
        Cell chosenCell = new Cell();
        ArrayList<Node> nodes = Menu.getRectangleAndButtonForGameMenus("Choose!");
        Pane pane = (Pane) Main.stage.getScene().getRoot();
        pane.getChildren().add(nodes.get(0));
        Pane newPane = Menu.copyPane(pane);
        ImageView iv = new ImageView(Menu.getImage("yu-gi-ohs-best-and-worst-girl-role-models", "png"));
        iv.setY(0);
        iv.setX(0);
        iv.setFitHeight(700);
        iv.setFitWidth(700);
        iv.setOpacity(0.18);
        Label label = new Label(message);
        newPane.getChildren().add(nodes.get(1));
        label.setStyle("-fx-background-color: transparent;-fx-font: 19px Chalkboard;-fx-text-fill: green;");
        label.setWrapText(true);
        label.setLayoutX(10);
        label.setLayoutY(100);
        newPane.getChildren().add(label);
        Stage newStage = Menu.copyStage(Main.stage);
        newStage.setScene(new Scene(newPane));
        Label labelForNumCards = new Label("1/" + cards.size());
        newPane.getChildren().add(nodes.get(1));
        labelForNumCards.setStyle("-fx-background-color: transparent;-fx-font: 19px Chalkboard;-fx-text-fill: green;");
        labelForNumCards.setWrapText(true);
        labelForNumCards.setLayoutX(10);
        labelForNumCards.setLayoutY(100);
        newPane.getChildren().add(label);
        ImageView cardPlace = new ImageView(Menu.getCard(cards.get(0).getCardName()));
        cardPlace.setFitHeight(300);
        cardPlace.setFitWidth(210);
        cardPlace.setX(245);
        cardPlace.setY(300);
        cardPlace.setOnMouseClicked(mouseEvent -> {
            cardPlace.setImage(Menu.getCard(cards.get(Integer.parseInt(labelForNumCards.getText().split("/")[0]) % cards.size()).getCardName()));
            labelForNumCards.setText(((Integer.parseInt(labelForNumCards.getText().split("/")[0]) % cards.size()) + 1) + "/" + cards.size());
        });
        Button button = (Button) nodes.get(1);
        button.setOnMouseClicked(mouseEvent -> {
            chosenCell.addCard(cards.get(Integer.parseInt(labelForNumCards.getText().split("/")[0]) - 1));
            newStage.close();
        });
        newStage.showAndWait();
        pane.getChildren().remove(nodes.get(0));
        if (chosenCell.getCard() == null) return cards.get(new Random().nextInt(cards.size()));
        return chosenCell.getCard();
    }

    static public int getCellNumber() {
        return getCellNumbers(1)[0];
    }

    static public Card getCardFrom(Graveyard graveyard, Deck deck, ArrayList<Card> cards) {
        ArrayList<Card> output = new ArrayList<>(cards);
        output.addAll(graveyard.getCards());
        output.addAll(deck.getMainDeck().getCards());
        return chooseCardFromList(output, "Please Choose from list of cards. if not it will be randomly choosed!");
    }

    private static Card selectCardFromArrayList(ArrayList<Card> cards) {
        Scanner scanner = LoginMenu.getInstance().getScanner();
        String command;
        while (true) {
            System.out.println("Please select one of the following cards : ");
            for (int i = 0; i < cards.size(); i++) {
                System.out.println((i + 1) + " : " + cards.get(i).getCardName());
            }
            command = scanner.nextLine().trim();
            if (command.matches("^[\\d]{1,3}$")) {
                int temp = Integer.parseInt(command);
                if (temp <= cards.size()) {
                    return cards.get(temp - 1);
                }
            }
            System.out.println("Invalid input .. ");
        }
    }

    static public String getCardName(){
        ArrayList<String> names = CSVInfoGetter.getCardNames();
        ArrayList<Card> cards = new ArrayList<>();
        for (String name : names) {
            cards.add(CSVInfoGetter.getCardByName(name));
        }
        Card card = chooseCardFromList(cards, "Please Choose a card from the list of cards. if not it will be randomly choosed!");
        if (card == null) return names.get(0);
        else return card.getCardName();
    }

    static public int getNumberOfCardInHand(ArrayList<Card> cards) {
        System.out.println("please select a card number from your hand");
        Scanner scanner = LoginMenu.getInstance().getScanner();
        String command;
        while (true) {
            command = scanner.nextLine().trim();
            if (command.matches("^[\\d]{1,2}$")) {
                int temp = Integer.parseInt(command);
                if (!(temp < 1 || temp > cards.size())) {
                    return temp;
                }
            }
            System.out.println("Please enter a valid number ..");
        }
    }

    static public int[] getCellNumbers(int count) {
        while (true) {
            int[] temp = getCellNumberExtracted(count);
            if (temp.length == count) return temp;
            System.out.println("Invalid Command .. please enter " + count + " cell number(s)");
        }
    }

    static public int[] getCellNumbers() {
        return getCellNumberExtracted(0);
    }

    private static int[] getCellNumberExtracted(int count) {
        String command;
        Scanner scanner = LoginMenu.getInstance().getScanner();
        while (true) {
            if (count == 0) {
                System.out.println("Please enter cell numbers .. ");
            } else {
                System.out.println("Please enter " + count + " cell numbers .. ");
            }
            command = scanner.nextLine().trim();
            if (command.matches("^([1-5])( [1-5])*$")) {
                ArrayList<Integer> temp = new ArrayList<>();
                String[] stringArray = command.split(" ");
                for (String s : stringArray) temp.add(Integer.parseInt(s));
                int[] output = new int[temp.size()];
                for (int i = 0 ;i < temp.size(); i++) {
                    output[i] = temp.get(i);
                }
                return output;
            }
            System.out.println("Invalid Command .. ");
        }
    }

    static public Card getCardFromGraveyard(Graveyard graveyard) {
        ArrayList<Card> temp = graveyard.getCards();
        return getCardFromList(temp);
    }

    static public Card getCardFromBothGraveyards(Graveyard playerGraveyard, Graveyard rivalGraveyard) {
        ArrayList<Card> cards = (ArrayList<Card>) playerGraveyard.getCards().clone();
        cards.addAll(rivalGraveyard.getCards());
        return getCardFromList(cards);
    }

    static public Card getCardFromDeck(Deck deck) {
        return getCardFromList(deck.getMainDeck().getCards());
    }

    private static  Card getCardFromList(ArrayList<Card> cards) {
        if (cards.size() == 0) return null;
        Scanner scanner = LoginMenu.getInstance().getScanner();
        while (true) {
            System.out.println("Please select a card from this item(s) :");
            for (int i = 0; i < cards.size(); i++) {
                System.out.println((i + 1) + " : " + cards.get(i).getCardName());
            }
            String command = scanner.nextLine().trim();
            if (command.matches("^[\\d]{1,2}$")){
                int temp = Integer.parseInt(command);
                if (temp >= 1 && temp <= cards.size()) return cards.get(temp - 1);
            }
            System.out.println("Invalid number ..");
        }
    }

    static public Card getCardFromBothBoards(Cell[] playerSpellZone, Cell[] rivalSpellZone) {
        ArrayList<Card> cards = new ArrayList<>();
        for (Cell cell : playerSpellZone) {
            if (cell.isOccupied()) {
                cards.add(cell.getCard());
            }
        }
        for (Cell cell : rivalSpellZone) {
            if (cell.isOccupied()) {
                cards.add(cell.getCard());
            }
        }
        return selectCardFromArrayList(cards);
    }

    static public HowToSummon howToSpecialNormalSummon() {
        Scanner scanner = LoginMenu.getInstance().getScanner();
        while (true) {
            System.out.println("You can summon cards in these ways .. \n1. Summon normally with 1900 ATK .. \n2. Summon with 3 tributes ..\n" +
                    "You can type back to cancel ..\nPlease choose a way :");
            String command = scanner.nextLine().trim();
            if (command.equals("1")) return HowToSummon.SPECIAL_NORMAL_TYPE1;
            if (command.equals("2")) return  HowToSummon.SPECIAL_NORMAL_TYPE2;
            if (command.equals("back")) return HowToSummon.BACK;
            System.out.println("Invalid command ..");
        }
    }

    static public State getStateOfSummon() {
        Scanner scanner = LoginMenu.getInstance().getScanner();
        while (true) {
            System.out.println("Please choose a card state to summon your card ..\n1. Face up attack \n2. face up defense\n3. face down defense" +
                    "You can type back to cancel ..\nPlease choose a way :");
            String command = scanner.nextLine().trim();
            if (command.equals("1")) return State.FACE_UP_ATTACK;
            if (command.equals("2")) return  State.FACE_UP_DEFENCE;
            if (command.equals("3")) return State.FACE_DOWN_DEFENCE;
            System.out.println("Invalid command ..");
        }
    }
}
