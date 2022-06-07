package view.graphics.duelgraphics;


import controller.LoginMenuController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import main.Main;
import model.User;
import model.enums.Cursor;
import view.graphics.Menu;

public class DuelMenu extends Menu {
    @FXML
    private Button MULTIPLAYER_1_ROUND;
    @FXML
    private Button MULTIPLAYER_3_ROUND;
    @FXML
    private Button SINGLE_PLAYER_1_ROUND;
    @FXML
    private Button SINGLE_PLAYER_3_ROUND;
    @FXML
    private Button back;

    public DuelMenu() {

    }

    public DuelMenu(int alaki) {
        AnchorPane anchorPane = (AnchorPane) Menu.getNode("DuelMenu");
        assert anchorPane != null;
        Main.stage.setScene(new Scene(anchorPane, 600, 400));
    }

    public void initialize() {
        justifyButton(SINGLE_PLAYER_1_ROUND, Cursor.SWORD);
        justifyButton(SINGLE_PLAYER_3_ROUND, Cursor.SWORD);
        justifyButton(MULTIPLAYER_1_ROUND, Cursor.SWORD);
        justifyButton(MULTIPLAYER_3_ROUND, Cursor.SWORD);
        back.setCursor(javafx.scene.Cursor.HAND);
    }

    public void singlePlayerOneRound() {
        if (checkPlayerDeck()) {

        }
    }

    public void singlePlayerThreeRound() {
        if (checkPlayerDeck()) {

        }
    }

    public void multiplayerOneRound() {
        if (checkPlayerDeck()) {
            WinnerExceptionHolder.gameMode = WinnerExceptionHolder.GameMode.ONE_ROUND;
            goToChooseRival();
        }
    }

    public void multiplayerThreeRound() {
        if (checkPlayerDeck()) {
            WinnerExceptionHolder.gameMode = WinnerExceptionHolder.GameMode.THREE_ROUND;
            goToChooseRival();
        }
    }

    public void goToChooseRival() {
        new ChooseRival(0);
    }


    public boolean checkPlayerDeck() {
        User player = LoginMenuController.getCurrentUser();
        if (player.getActiveDeck() == null) {
            Popup noActiveDeckPopup = new Popup();
            VBox vBox = new VBox(10);

            Label label = new Label("you don't have active deck!");
            return makePopUp(noActiveDeckPopup, vBox, label);
        } else if (!player.getActiveDeck().isValid()) {
            Popup noValidDeckPopUp = new Popup();
            VBox vBox = new VBox(10);
            Label label = new Label("OPS! your deck is invalid!");

            return makePopUp(noValidDeckPopUp, vBox, label);
        } else
            return true;
    }

    private boolean makePopUp(Popup noValidDeckPopUp, VBox vBox, Label label) {
        disableButtons(true);
        vBox.setStyle(" -fx-background-color: white;" +
                " -fx-background-radius: 13;" +
                " -fx-border-color: grey;" +
                " -fx-border-radius: 13;");
        vBox.setMinWidth(164);
        ImageView imageView = new ImageView();
        imageView.setImage(Menu.getImage("confusedNinja", "png"));
        label.setStyle(" -fx-background-color: transparent;" +
                " -fx-font-family: Chalkboard;" +
                " -fx-text-fill: black;" +
                " -fx-border-radius: 15");
        label.setMinWidth(80);
        label.setMinHeight(45);
        Button hide = new Button("hide");
        hide.setCursor(javafx.scene.Cursor.HAND);
        hide.setStyle("  -fx-background-radius: 75;" +
                " -fx-font-family: Chalkboard;" +
                " -fx-text-fill: white;" +
                " -fx-background-color: black;");
        hide.setMinWidth(45);
        hide.setMinHeight(45);
        hide.setTranslateX(68);
        hide.setTranslateY(-3);
        Button goToDeck = new Button("goto deck");
        goToDeck.setCursor(javafx.scene.Cursor.HAND);
        goToDeck.setMaxWidth(45);
        goToDeck.setStyle(" -fx-background-radius: 50;" +
                " -fx-font-family: Chalkboard;" +
                " -fx-text-fill: white;" +
                " -fx-background-color: black;" +
                " -fx-wrap-text: true");
        goToDeck.setTranslateX(3);
        goToDeck.setTranslateY(-3);
        hide.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                disableButtons(false);
                noValidDeckPopUp.hide();
            }
        });
        goToDeck.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                disableButtons(false);
                noValidDeckPopUp.hide();
                goToDeck();
            }
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(goToDeck, hide);
        vBox.getChildren().addAll(label,imageView, hBox);
        noValidDeckPopUp.getContent().add(vBox);
        noValidDeckPopUp.show(Main.stage);
        return false;
    }

    public void disableButtons(boolean isDisable){
        MULTIPLAYER_1_ROUND.setDisable(isDisable);
        MULTIPLAYER_3_ROUND.setDisable(isDisable);
        SINGLE_PLAYER_1_ROUND.setDisable(isDisable);
        SINGLE_PLAYER_3_ROUND.setDisable(isDisable);
        back.setDisable(isDisable);
    }

    public void goToDeck() {
        goToMenu("Deck");
    }

    public void goToMainMenu(MouseEvent mouseEvent) {
        goToMainMenu();
    }
}
