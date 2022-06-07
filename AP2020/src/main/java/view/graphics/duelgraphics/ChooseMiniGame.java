package view.graphics.duelgraphics;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import main.Main;
import model.User;
import model.game.MiniGame;
import view.graphics.Menu;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChooseMiniGame implements Initializable {
    private static User player;
    private static User rival;
    private static MiniGame miniGame;
    @FXML
    private Label playerName;
    @FXML
    private Label rivalName;
    private AnchorPane anchorPane;
    @FXML
    private Button go;
    @FXML
    private Label mode;
    @FXML
    private RadioButton radio;
    @FXML
    private Button chooseCoin;
    @FXML
    private Button chooseRock;
    @FXML
    private Button chooseDice;

    private ArrayList<Button> menuButtons = new ArrayList<>();

    public ChooseMiniGame() {

    }

    public ChooseMiniGame(User player, User rival) {
        miniGame = new MiniGame(player, rival);
        anchorPane = (AnchorPane) Menu.getNode("ChooseMiniGame");
        Main.stage.setScene(new Scene(anchorPane));
        // TODO: 7/3/2021 go back buttons for mini games
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getBackGroundForButtons();
        go.setDisable(true);
        playerName.setText(miniGame.getFirstUser().getNickname());
        rivalName.setText(miniGame.getSecondUser().getNickname());
        menuButtons.add(chooseRock);
        menuButtons.add(chooseCoin);
        menuButtons.add(chooseDice);
        for (Button button: menuButtons) {
            button.onMouseEnteredProperty().set(mouseEvent -> {
                button.setTranslateY(-30);
                setSize(button, 100);
                Glow glow = new Glow();
                glow.setLevel(0.3);
                button.setEffect(glow);
            });
            button.onMouseExitedProperty().set(mouseEvent -> {
                button.setTranslateY(-20);
                setSize(button, 80);
                button.setEffect(null);
            });
        }
        for (Button menuButton : menuButtons) menuButton.setCursor(Cursor.HAND);
        chooseCoin.setOnMouseClicked(mouseEvent -> {
            go.setDisable(false);
            radio.setDisable(true);
            radio.setVisible(false);
            mode.setText("Coin");
        });
        chooseRock.setOnMouseClicked(mouseEvent -> {
            go.setDisable(false);
            radio.setDisable(true);
            radio.setVisible(false);
            mode.setText("Rock Paper Scissors");
        });
        chooseDice.setOnMouseClicked(mouseEvent -> {
            go.setDisable(false);
            radio.setDisable(false);
            radio.setVisible(true);
            mode.setText("Dice");
        });
        go.setOnMouseClicked(mouseEvent -> goToMode());
    }


    private void goToMode() {
        go.setDisable(true);
        if (mode.getText().equals("Coin")) {
            new MiniGameCoin(miniGame);
        } else if (mode.getText().equals("Dice")) {
            new MiniGameDice(miniGame, radio.isSelected());
        } else {
            new MiniGameRockPaperScissors(miniGame);
        }
    }
    private void getBackGroundForButtons(){
        chooseRock.setBackground(Menu.getBackGround("rockPaperScissors","png",80,80));
        chooseRock.setTranslateX(330);
        chooseRock.setTranslateY(-20);
        setSize(chooseRock,80);
        chooseCoin.setBackground(Menu.getBackGround("throwCoin","png",80,80));
        chooseCoin.setTranslateX(30);
        chooseCoin.setTranslateY(-20);
        setSize(chooseCoin,80);
        chooseDice.setBackground(Menu.getBackGround("throwDice","png",80,80));
        chooseDice.setTranslateX(185);
        chooseDice.setTranslateY(-20);
        setSize(chooseDice,80);
    }
    private void setSize(Button button, int size){
        button.setMinWidth(size);
        button.setMinHeight(size);
        button.setMaxWidth(size);
        button.setMaxHeight(size);
    }
}
