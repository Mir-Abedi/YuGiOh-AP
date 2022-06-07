package view.graphics.deckmenu;

import controller.DeckMenuController;
import controller.LoginMenuController;
import controller.database.CSVInfoGetter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.card.Card;
import model.deck.Deck;
import model.deck.MainDeck;
import model.deck.PrimaryDeck;
import model.deck.SideDeck;
import model.enums.Cursor;
import model.enums.VoiceEffects;
import model.graphicalModels.CardHolder;
import view.graphics.ChoiceMenu;
import view.graphics.Menu;

import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

public class EditDeckMenu extends Menu implements Initializable {
    private final HashMap<String, VBox> cardBoxes = new HashMap<>();
    private final HashMap<String, VBox> cardBoxes1 = new HashMap<>();

    {
        for (String cardName : Card.getCardNames()) {
            VBox choiceBox = new VBox();
            choiceBox.setSpacing(10);
            CardHolder holder = new CardHolder(getCard(cardName));
            holder.scale(0.65);
            double width = holder.getWidth();
            double height = holder.getHeight();
            choiceBox = (VBox) setDimension(choiceBox, width, height + 50);
            Label name = getLabel(cardName, width, 15, 10);
            choiceBox.getChildren().add(name);
            choiceBox.getChildren().add(holder);
            holder.setOnMouseClicked(mouseEvent -> {
                playMedia(VoiceEffects.CLICK);
                addOptionsToDecisionBox(cardName, true);
            });
            cardBoxes.put(cardName, choiceBox);
        }

        for (String cardName : Card.getCardNames()) {
            VBox choiceBox = new VBox();
            choiceBox.setSpacing(10);
            CardHolder holder = new CardHolder(getCard(cardName));
            holder.scale(0.65);
            double width = holder.getWidth();
            double height = holder.getHeight();
            choiceBox = (VBox) setDimension(choiceBox, width, height + 50);
            Label name = getLabel(cardName, width, 15, 10);
            choiceBox.getChildren().add(name);
            choiceBox.getChildren().add(holder);
            holder.setOnMouseClicked(mouseEvent -> {
                playMedia(VoiceEffects.CLICK);
                addOptionsToDecisionBox(cardName, false);
            });
            cardBoxes1.put(cardName, choiceBox);
        }
    }

    private Deck deck;
    private SideDeck sideDeck;
    private MainDeck mainDeck;
    private ChoiceMenu mainChoiceMenu;
    private ChoiceMenu sideChoiceMenu;


    @FXML
    private Label nameLabel;
    @FXML
    private ToggleButton sideToggle;
    @FXML
    private ToggleButton mainToggle;
    @FXML
    private VBox sideDeckOptionBar;
    @FXML
    private VBox mainDeckOptionBar;
    @FXML
    private HBox mainChoiceBox;
    @FXML
    private HBox sideChoiceBox;
    @FXML
    private TextField searchField;

    protected VBox getChoiceBox(String result) {
        return cardBoxes.get(result);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initChoiceMenus();
        initToggleButtons();
        searchField.textProperty().addListener((observableValue, s, t1) -> {
            playMedia(VoiceEffects.KEYBOARD_HIT);
            search(t1);
        });
        mainChoiceMenu.resetChoiceBox();
        sideChoiceMenu.resetChoiceBox();
        mainDeckOptionBar.setSpacing(10);
        sideDeckOptionBar.setSpacing(10);
    }

    private void initToggleButtons() {
        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(mainToggle, sideToggle);
        mainToggle.setSelected(true);
        mainToggle.setEffect(new DropShadow(BlurType.ONE_PASS_BOX, Color.rgb(138, 138, 138, 1), 0.5, 0.0, 1, 0));
        mainToggle.setOnAction(actionEvent -> {
            playMedia(VoiceEffects.CLICK);
            onSelectToggle(mainToggle, group);
        });

        sideToggle.setOnAction(actionEvent -> {
            playMedia(VoiceEffects.CLICK);
            onSelectToggle(sideToggle, group);
        });
    }

    private void initChoiceMenus() {
        setChoiceMenu();
        mainChoiceMenu.setChoiceNames(new HashSet<>(Card.getCardNames()));
        sideChoiceMenu.setChoiceNames(new HashSet<>(Card.getCardNames()));

        mainChoiceMenu.setSpacing(5);
        mainChoiceBox.setSpacing(5);
        sideChoiceMenu.setSpacing(5);
        sideChoiceBox.setSpacing(5);
        mainChoiceMenu.setWidth(150 * 0.65 + 5);
        sideChoiceMenu.setWidth(150 * 0.65 + 5);

        mainChoiceMenu.setDecisionBox(mainDeckOptionBar);
        sideChoiceMenu.setDecisionBox(sideDeckOptionBar);

        mainChoiceMenu.setChoiceBox(mainChoiceBox);
        sideChoiceMenu.setChoiceBox(sideChoiceBox);
    }

    private void setChoiceMenu() {
        mainChoiceMenu = new ChoiceMenu() {
            @Override
            protected VBox getChoiceBox(String result) {
                return cardBoxes.get(result);
            }
        };
        sideChoiceMenu = new ChoiceMenu() {
            @Override
            protected VBox getChoiceBox(String result) {
                return cardBoxes1.get(result);
            }
        };
    }

    private void addOptionsToDecisionBox(String cardName, boolean isMainDeck) {
        Button button1 = null;
        Button button2 = null;
        PrimaryDeck deck1 = isMainDeck ? mainDeck : sideDeck;
        VBox box = isMainDeck ? mainDeckOptionBar : sideDeckOptionBar;
        box.getChildren().clear();
        int cardCount = deck1.getCardCount(cardName);
        Label name = getLabel(cardName, 100, 20, 10);
        Label count = getLabel("Count:", 100, 20);
        Label count1 = getLabel("" + cardCount, 100, 20);
        if (DeckMenuController.canAddCard(deck.getDeckName(),cardName) && deck1.hasCapacity() && LoginMenuController.getCurrentUser().getCardByName(cardName) != null)
            button1 = getButton(cardName, "Add", isMainDeck);
        if (cardCount != 0) button2 = getButton(cardName, "Remove", isMainDeck);
        box.getChildren().addAll(name, count, count1);
        if (button1 != null) box.getChildren().add(button1);
        if (button2 != null) box.getChildren().add(button2);
       ;
    }

    private Button getButton(String cardName, String text, boolean isMainDeck) {
        Button button = new Button(text);
        button.setPrefHeight(25);
        button.setPrefWidth(100);
        button.setStyle("-fx-border-style: solid none solid; -fx-border-radius: 10;");
        if (text.equals("Add")) {
            justifyButton(button, Cursor.ACCEPT);
            button.setOnAction(actionEvent -> {
                playMedia(VoiceEffects.CLICK);
                addCard(cardName, isMainDeck);
            });
        } else {
            justifyButton(button, Cursor.TRASH);
            button.setOnAction(actionEvent -> {
                playMedia(VoiceEffects.CLEAR_CHALK);
                removeCard(cardName, isMainDeck);
            });
        }
        return button;
    }

    private void addCard(String cardName, boolean toMainDeck) {
        if (toMainDeck) DeckMenuController.addCardToMainDeck(deck.getDeckName(), cardName);
        else DeckMenuController.addCardToSideDeck(deck.getDeckName(), cardName);
        addOptionsToDecisionBox(cardName, toMainDeck);
        VBox box = !toMainDeck ? mainDeckOptionBar : sideDeckOptionBar;
        if (box.getChildren().isEmpty()) return;
        addOptionsToDecisionBox(((Label) box.getChildren().get(0)).getText(), !toMainDeck);
    }

    private void removeCard(String cardName, boolean toMainDeck) {
        if (toMainDeck) DeckMenuController.removeCardFromMainDeck(deck.getDeckName(), cardName);
        else DeckMenuController.removeCardFromSideDeck(deck.getDeckName(), cardName);
        addOptionsToDecisionBox(cardName, toMainDeck);
        VBox box = !toMainDeck ? mainDeckOptionBar : sideDeckOptionBar;
        if (box.getChildren().isEmpty()) return;
        addOptionsToDecisionBox(((Label) box.getChildren().get(0)).getText(), !toMainDeck);
    }

    @Override
    protected void onSelectToggle(ToggleButton button, ToggleGroup group) {
        searchField.setText("");
        super.onSelectToggle(button, group);
        mainChoiceMenu.resetChoiceBox();
        sideChoiceMenu.resetChoiceBox();
    }

    protected void search(String searchText) {
        if (mainToggle.isSelected()) {
            mainChoiceMenu.search(searchText);
        } else if (sideToggle.isSelected()) {
            sideChoiceMenu.search(searchText);
        }
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
        mainDeck = deck.getMainDeck();
        sideDeck = deck.getSideDeck();
        nameLabel.setText(deck.getDeckName());
    }


}
