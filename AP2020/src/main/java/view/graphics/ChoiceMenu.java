package view.graphics;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

abstract public class ChoiceMenu extends SearchMenu {
    @FXML
    public ScrollPane cardsPlace;
    @FXML
    public HBox choiceBox;
    @FXML
    public VBox decisionBox;
    protected HashSet<String> choiceNames = new HashSet<>();
    private int spacing = 5;
    private double width = 155;

    @Override
    public void search(String searchText) {
        searchText = searchText.trim().toLowerCase();
        if (searchText.equals("")) {
            resetChoiceBox();
            return;
        }
        HashSet<String> matchingChoices = new HashSet<>();
        for (String choice : choiceNames) if (choice.toLowerCase().contains(searchText)) matchingChoices.add(choice);
        addToChoiceBox(matchingChoices);
    }

    public void resetChoiceBox() {
        emptyChoiceBox();
        addToChoiceBox(new HashSet<>(choiceNames));
    }

    public void addToChoiceBox(HashSet<String> matchingCards) {
        if (matchingCards.isEmpty()) {
            emptyChoiceBox();
            return;
        }
        ArrayList<VBox> resultBoxes = getSearchResults(new ArrayList<>(matchingCards));
        choiceBox.getChildren().clear();
        double newWidth = width * resultBoxes.size() - spacing;
        choiceBox.setPrefWidth(newWidth);
        for (VBox box : resultBoxes) {
            choiceBox.getChildren().add(box);
        }
    }

    protected void emptyChoiceBox() {
        choiceBox.getChildren().removeAll(choiceBox.getChildren());
        choiceBox.setPrefWidth(0);
    }

    @Override
    protected ArrayList<VBox> getSearchResults(ArrayList<String> searchResults) {
        ArrayList<VBox> resultBoxes = new ArrayList<>();
        for (String result : searchResults) resultBoxes.add(getChoiceBox(result));
        return resultBoxes;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    protected abstract VBox getChoiceBox(String result);

    protected void addOptionToDecisionBox(Button button, Button... args) {
        decisionBox.getChildren().add(button);
        if (args.length != 0) decisionBox.getChildren().addAll(args);
    }

    protected void emptyDecisionBox() {
        decisionBox.getChildren().removeAll(decisionBox.getChildren());
    }

    protected void updateChoiceBox() {
        String text = searchField.getText();
        if (text.length() == 0) resetChoiceBox();
        else search(text);
    }


    public void setChoiceBox(HBox choiceBox) {
        this.choiceBox = choiceBox;
    }

    public void setChoiceNames(HashSet<String> choiceNames) {
        this.choiceNames = choiceNames;
    }

    public void setCardsPlace(ScrollPane cardsPlace) {
        this.cardsPlace = cardsPlace;
    }

    public void setDecisionBox(VBox decisionBox) {
        this.decisionBox = decisionBox;
    }

    public HashSet<String> getChoiceNames() {
        return choiceNames;
    }
}
