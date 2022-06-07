package view.graphics.deckmenu;

import controller.DeckMenuController;
import controller.LoginMenuController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import main.Main;
import model.card.Card;
import model.deck.Deck;
import model.enums.Cursor;
import model.enums.VoiceEffects;
import model.graphicalModels.CardHolder;
import org.jetbrains.annotations.NotNull;
import view.graphics.ChoiceMenu;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;

public class AllDecksMenu extends ChoiceMenu implements Initializable {
    private final static Image DECK_PIC1 = getImage("DeckPicture1", "png");
    private final static Image DECK_PIC2 = getImage("DeckPicture2", "png");


    @FXML
    private ImageView lightBulb;
    @FXML
    private TextArea deckNameField;
    @FXML
    private Button createDeck;

    @FXML
    private HBox previewBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        previewBox.setPrefWidth(0);
        choiceBox.setSpacing(5);
        setWidth(105);
        for (Deck deck : LoginMenuController.getCurrentUser().getDecks()) choiceNames.add(deck.getDeckName());
        addToChoiceBox(choiceNames);
        searchField.textProperty().addListener((observableValue, s, t1) -> {
            playMedia(VoiceEffects.KEYBOARD_HIT);
            search(t1);
        });
        decisionBox.setSpacing(10);
        lightBulb.setImage(getImage("Bulb", "png"));
        deckNameField.textProperty().addListener((observableValue, s, t1) -> {
            playMedia(VoiceEffects.KEYBOARD_HIT);
            setCreateButton(t1);
        });

        setCreateButton("");
    }

    private void setCreateButton(String deckName) {
        if (deckName.equals("") || LoginMenuController.getCurrentUser().doesDeckExist(deckName)) {
            createDeck.setOnAction(actionEvent -> mio());
            createDeck.setText("Mio");
            justifyButton(createDeck,Cursor.CANCEL);
            return;
        }
        createDeck.setText("Create!");
        createDeck.setOnAction(actionEvent -> {
            playMedia(VoiceEffects.JINGLE);
            createDeck(deckName);
        });
        justifyButton(createDeck,Cursor.ACCEPT);
    }

    private void createDeck(String deckName) {
        DeckMenuController.createDeck(deckName);
        choiceNames.add(deckName);
        updateChoiceBox();
        emptyDecisionBox();
        deckNameField.setText("");
        setCreateButton("");
    }

    @Override
    public void search(String searchText) {
        resetBoxProperties(previewBox);
        super.search(searchText);
    }

    @Override
    public void addToChoiceBox(HashSet<String> matchingCards) {
        super.addToChoiceBox(matchingCards);
        VBox activeBox = getActiveDeckBox();
        if (activeBox == null) return;
        activeBox.setStyle("-fx-background-color: #fcf58c; -fx-background-radius: 10;-fx-border-radius: 10; -fx-border-style: Dashed; -fx-border-color: #404040");
    }

    @Override
    protected VBox getChoiceBox(String result) {
        Deck deck = LoginMenuController.getCurrentUser().getDeckByName(result);
        VBox box = new VBox();
        box.setSpacing(5);
        box = (VBox) setDimension(box, 100, 210);
        Label deckName = getLabel(result);
        Label side = getLabel("Side Deck:");
        Label sideSize = getLabel("#" + deck.getSideDeck().getCards().size());
        Label main = getLabel("Main Deck:");
        Label mainSize = getLabel("#" + deck.getMainDeck().getCards().size());
        Node[] array = {deckName, getImageView(100, 100, DECK_PIC1), main, mainSize, side, sideSize};
        box.getChildren().addAll(Arrays.asList(array));
        setOnClickAction(box);
        box.setStyle("-fx-border-radius: 10; -fx-border-style: Dashed; -fx-border-color: #404040");
        return box;
    }

    private void setOnClickAction(VBox box) {
        box.setOnMouseClicked(mouseEvent -> {
            ImageView view = (ImageView) box.getChildren().get(1);
            if (view.getImage().equals(DECK_PIC1)) {
                playMedia(VoiceEffects.SHOOW_1);
                resetSelectOthers();
                view.setImage(DECK_PIC2);
                setOptionsInDecisionBox(box);
                addPreviewBox(getDeckName(box));
            } else {
                playMedia(VoiceEffects.SHOOW_2);
                view.setImage(DECK_PIC1);
                emptyDecisionBox();
                resetBoxProperties(previewBox);
            }
        });
    }

    private void setOptionsInDecisionBox(VBox box) {
        emptyDecisionBox();
        String deckName = ((Label) box.getChildren().get(0)).getText();
        Deck deck = LoginMenuController.getCurrentUser().getActiveDeck();
        String activeDeck = deck != null ? deck.getDeckName() : "";
        boolean isDeckActive = deckName.equals(activeDeck);
        Button delete = getButton("Delete");
        Button edit = getButton("Edit");
        Button activate = getButton(isDeckActive ? "DeActivate" : "Activate");
        if (!isDeckActive) {
            activate.setOnAction(actionEvent -> {
                playMedia(VoiceEffects.JINGLE);
                activateDeck(box);
            });
            addOptionToDecisionBox(activate);
        }
        delete.setOnAction(actionEvent -> {
            playMedia(VoiceEffects.EXPLODE);
            deleteDeck(deckName);
        });
        edit.setOnAction(actionEvent -> goToEditMenu(deckName));
        addOptionToDecisionBox(edit, delete);
    }

    private void goToEditMenu(String deckName) {
        Deck deck = LoginMenuController.getCurrentUser().getDeckByName(deckName);
        try {
            FXMLLoader loader = new FXMLLoader(new File("src/main/resources/Scenes/EditDeckMenu.fxml").toURI().toURL());
            AnchorPane pane = loader.load();
            ((EditDeckMenu) loader.getController()).setDeck(deck);
            ((BorderPane) Main.stage.getScene().getRoot()).setCenter(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void activateDeck(VBox box) {
        DeckMenuController.activateDeck(((Label) box.getChildren().get(0)).getText());
        updateChoiceBox();
        setOptionsInDecisionBox(box);
        resetBoxProperties(previewBox);
    }

    private void deleteDeck(String deckName) {
        DeckMenuController.deleteDeck(deckName);
        choiceNames.remove(deckName);
        updateChoiceBox();
        emptyDecisionBox();
        resetBoxProperties(previewBox);
    }

    private VBox getActiveDeckBox() {
        Deck deck = LoginMenuController.getCurrentUser().getActiveDeck();
        if (deck == null) {
            return null;
        }
        String activeDeck = deck.getDeckName();
        if (choiceBox.getChildren().size() == 0) return null;
        for (Node box : choiceBox.getChildren())
            if (((Label) ((VBox) box).getChildren().get(0)).getText().equals(activeDeck)) return (VBox) box;
        return null;
    }

    private Button getButton(String text) {
        Button button = new Button(text);
        button.setPrefHeight(25);
        button.setPrefWidth(100);
        button.setStyle("-fx-border-style: solid none solid; -fx-border-radius: 10;");
        if (text.equals("Edit")) justifyButton(button, Cursor.EDIT);
        else if (text.equals("Delete")) justifyButton(button, Cursor.TRASH);
        else if (text.equals("Activate")) justifyButton(button, Cursor.ACCEPT);
        else justifyButton(button, Cursor.CANCEL);
        return button;
    }

    private void resetSelectOthers() {
        for (Node box : choiceBox.getChildren()) {
            ((ImageView) ((VBox) box).getChildren().get(1)).setImage(DECK_PIC1);
        }
    }

    @NotNull
    private Label getLabel(String text) {
        Label label = new Label(text);
        label.setPrefHeight(15);
        label.setPrefWidth(100);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private ImageView getImageView(double height, double width, Image image) {
        ImageView view = new ImageView();
        view.setFitWidth(width);
        view.setFitHeight(height);
        view.setImage(image);
        return view;
    }

    private void addPreviewBox(String deckName) {
        resetBoxProperties(previewBox);
        ArrayList<CardHolder> holders = new ArrayList<>();
        ArrayList<Card> mainCards = LoginMenuController.getCurrentUser().getDeckByName(deckName).getMainDeck().getSortedCards();
        ArrayList<Card> sideCards = LoginMenuController.getCurrentUser().getDeckByName(deckName).getSideDeck().getSortedCards();
        mainCards.addAll(sideCards);
        for (Card card : mainCards) {
            CardHolder holder = new CardHolder(card);
            holder.scale(0.4);
            holders.add(holder);
        }
        previewBox.setSpacing(5);
        if (holders.size() == 0) return;
        previewBox.setPrefWidth((holders.get(0).getWidth() + 5) * holders.size() - 5);
        previewBox.setPrefHeight(holders.get(0).getHeight() + 12);
        previewBox.getChildren().addAll(holders);
    }

    private String getDeckName(VBox box) {
        return ((Label) box.getChildren().get(0)).getText();
    }

    private void resetBoxProperties(Pane box) {
        box.getChildren().removeAll(box.getChildren());
        box.setPrefWidth(0);
        box.setPrefHeight(0);
    }
}
