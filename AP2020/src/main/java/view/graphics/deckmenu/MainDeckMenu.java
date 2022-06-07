package view.graphics.deckmenu;

import controller.DeckMenuController;
import controller.LoginMenuController;
import controller.ShopController;
import controller.database.CSVInfoGetter;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import model.card.Card;
import model.enums.Cursor;
import model.enums.VoiceEffects;
import model.graphicalModels.CardHolder;
import view.graphics.Menu;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainDeckMenu extends Menu implements Initializable {
    @FXML
    private BorderPane mainPane;
    @FXML
    private Button allDecksButton;
    @FXML
    private Button allCardsButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAllDecks();
        justifyButton(allCardsButton, Cursor.ALL_CARDS);
        justifyButton(allDecksButton, Cursor.CREATE_DECK);
        allDecksButton.setOnAction(actionEvent -> setAllDecks());
        allCardsButton.setOnAction(actionEvent -> setAllCards());
    }

    private void setAllCards() {
        playMedia(VoiceEffects.CLICK);
        mainPane.setCenter(getNode("AllCards"));
    }

    private void setAllDecks() {
        playMedia(VoiceEffects.CLICK);
        mainPane.setCenter(getNode("AllDecksMenu"));
    }

    public void goToMainMenu(ActionEvent actionEvent) {
        goToMainMenu();
    }

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void goToSetting(ActionEvent actionEvent) {
        goToSetting();
    }

    public void showAbout(ActionEvent actionEvent) {
        showAbout();
    }
}
