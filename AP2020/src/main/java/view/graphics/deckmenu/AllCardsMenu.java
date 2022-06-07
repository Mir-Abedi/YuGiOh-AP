package view.graphics.deckmenu;

import controller.LoginMenuController;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.card.Card;
import model.enums.VoiceEffects;
import model.graphicalModels.CardHolder;
import org.jetbrains.annotations.NotNull;
import view.graphics.ChoiceMenu;
import view.graphics.SearchMenu;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

public class AllCardsMenu extends ChoiceMenu implements Initializable {
    @FXML
    public ScrollPane cardsPlace;
    @FXML
    public HBox choiceBox;
    @FXML
    public VBox decisionBox;

    private ArrayList<Card> cards;

    private int getNumberOfCards(String cardName) {
        ArrayList<Card> allCards = (ArrayList<Card>) cards.clone();
        return (int) allCards.stream().filter(card -> card.getCardName().equals(cardName)).count();

    }

    @NotNull
    protected VBox getChoiceBox(String cardName) {
        VBox box = new VBox();
        box.setSpacing(10);
        CardHolder holder = new CardHolder(getCard(cardName));
        Label name = getLabel(cardName, 150, 20);
        Label count = getLabel("#" + getNumberOfCards(cardName), 150, 20);
        box.setPrefHeight(200);
        box.setMinHeight(200);
        box.setPrefWidth(150);
        box.setMinWidth(150);
        box.getChildren().add(name);
        box.getChildren().add(holder);
        box.getChildren().add(count);
        setOnCardClicked(box);
        return box;
    }

    private void setOnCardClicked(VBox box) {
        CardHolder holder = (CardHolder) box.getChildren().get(1);
        holder.setOnMouseClicked(mouseEvent -> {
            playMedia(VoiceEffects.CARD_FLIP);
            holder.flipCard();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.setSpacing(5);
        setWidth(155);
        resetChoiceBox();
        cards = LoginMenuController.getCurrentUser().getCards();
        for (Card card : cards) choiceNames.add(card.getCardName());
        searchField.textProperty().addListener((observableValue, s, t1) ->  {
            playMedia(VoiceEffects.KEYBOARD_HIT);
            search(t1);
        });
        resetChoiceBox();
    }

}
