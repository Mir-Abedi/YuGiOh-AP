package view.graphics.duelgraphics;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import main.Main;
import model.User;
import model.game.MiniGame;
import view.graphics.Menu;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class MiniGameDice implements Initializable {
    private static MiniGame miniGame;
    private static boolean higherWins;
    private User player;
    private User rival;
    private int playerNum = 0;
    private int rivalNum = 0;
    private boolean anyonePlayed = false;
    private AnchorPane anchorPane;
    @FXML
    private Button playerButton;
    @FXML
    private Button rivalButton;
    @FXML
    private Label playerName;
    @FXML
    private Label rivalName;
    @FXML
    private ImageView playerDice;
    @FXML
    private ImageView rivalDice;
    @FXML
    private Label result;

    public MiniGameDice() {

    }

    public MiniGameDice(MiniGame miniGame, boolean higherWins) {
        WinnerExceptionHolder.resetExceptions();
        MiniGameDice.miniGame = miniGame;
        MiniGameDice.higherWins = higherWins;
        anchorPane = (AnchorPane) Menu.getNode("MiniGameDice");
        Main.stage.setScene(new Scene(anchorPane, 600, 400));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventHandler<MouseEvent> event = mouseEvent -> {
            if (anyonePlayed) {
                handleSecond(mouseEvent);
                return;
            }
            anyonePlayed = true;
            handleFirst(mouseEvent);
        };
        playerButton.setOnMouseClicked(event);
        rivalButton.setOnMouseClicked(event);
        player = miniGame.getFirstUser();
        rival = miniGame.getSecondUser();
        rivalName.setText(miniGame.getSecondUser().getNickname());
        playerName.setText(miniGame.getFirstUser().getNickname());
    }

    public void handleSecond(MouseEvent event) {
        handleExtracted(event, false);
        if (rivalNum != playerNum) {
            playerButton.setDisable(true);
            rivalButton.setDisable(true);
            if (higherWins) {
                if (playerNum > rivalNum) miniGame.setWinner(player);
                else miniGame.setWinner(rival);
            } else {
                if (playerNum < rivalNum) miniGame.setWinner(player);
                else miniGame.setWinner(rival);
            }
            result.setText(miniGame.getWinner().getNickname() + " Won the mini game !");
            Main.stage.getScene().getRoot().setOnMouseClicked(mouseEvent -> new OneRoundGameGraphical(miniGame.getWinner(), miniGame.getWinner() == miniGame.getFirstUser() ? miniGame.getSecondUser() : miniGame.getFirstUser()));
        }
    }

    public void handleFirst(MouseEvent event) {
        handleExtracted(event, true);
    }

    private void handleExtracted(MouseEvent event, boolean disable) {
        Button source = (Button)event.getSource();
        if (source == playerButton) {
            playerNum = new Random().nextInt(6) + 1;
            playerDice.setImage(Menu.getImage("dice " + playerNum, "png"));
        } else {
            rivalNum = new Random().nextInt(6) + 1;
            rivalDice.setImage(Menu.getImage("dice " + rivalNum, "png"));
        }
        if (disable) source.setDisable(true);
    }
}
