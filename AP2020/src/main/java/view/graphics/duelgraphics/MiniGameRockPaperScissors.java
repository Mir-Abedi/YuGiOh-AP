package view.graphics.duelgraphics;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import main.Main;
import model.User;
import model.game.MiniGame;
import view.graphics.Menu;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MiniGameRockPaperScissors implements Initializable {
    private static MiniGame miniGame;
    private User player;
    private User rival;
    private AnchorPane anchorPane;
    private boolean playerChose = false;
    private boolean rivalChose = false;
    private int playerNum = -1;
    private int rivalNum = -1;
    private int playerPoints = 0;
    private int rivalPoints = 0;
    @FXML
    private Button rivalPaper;
    @FXML
    private Button rivalRock;
    @FXML
    private Button rivalScissors;
    @FXML
    private Button playerPaper;
    @FXML
    private Button playerRock;
    @FXML
    private Button playerScissors;
    @FXML
    private Label rivalName;
    @FXML
    private Label playerName;
    @FXML
    private Label rivalPoint;
    @FXML
    private Label playerPoint;
    @FXML
    private Label result;
    private ArrayList<Button> buttons;

    public MiniGameRockPaperScissors() {

    }

    public MiniGameRockPaperScissors(MiniGame miniGame) {
        WinnerExceptionHolder.resetExceptions();
        MiniGameRockPaperScissors.miniGame = miniGame;
        anchorPane = (AnchorPane) Menu.getNode("MiniGameRockPaperScissors");
        Main.stage.setScene(new Scene(anchorPane, 600, 400));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadRockButton(rivalRock);
        loadRockButton(playerRock);
        rivalRock.setRotate(180);
        loadPaperButton(rivalPaper);
        loadPaperButton(playerPaper);
        rivalPaper.setRotate(180);
        loadScissorsButton(playerScissors);
        loadScissorsButton(rivalScissors);
        rivalScissors.setRotate(180);
        buttons = new ArrayList<>();
        buttons.add(rivalPaper);
        buttons.add(rivalScissors);
        buttons.add(rivalRock);
        buttons.add(playerRock);
        buttons.add(playerPaper);
        buttons.add(playerScissors);
        player = miniGame.getFirstUser();
        rival = miniGame.getSecondUser();
        playerName.setText(player.getNickname());
        rivalName.setText(rival.getNickname());
        for (Button button : buttons) button.setCursor(Cursor.HAND);
        addFunctions();
    }

    private void addFunctions() {
        EventHandler<MouseEvent> rivalEvent = mouseEvent -> {
            rivalScissors.setDisable(true);
            rivalRock.setDisable(true);
            rivalPaper.setDisable(true);
            rivalChose = true;
            if (mouseEvent.getSource() == rivalPaper) rivalNum = 0;
            else if (mouseEvent.getSource() == rivalRock) rivalNum = 1;
            else rivalNum = 2;
            setShadow((Button) mouseEvent.getSource());
            checkScores();
        };
        rivalScissors.setOnMouseClicked(rivalEvent);
        rivalPaper.setOnMouseClicked(rivalEvent);
        rivalRock.setOnMouseClicked(rivalEvent);
        EventHandler<MouseEvent> playerEvent = mouseEvent -> {
            playerRock.setDisable(true);
            playerPaper.setDisable(true);
            playerScissors.setDisable(true);
            playerChose = true;
            if (mouseEvent.getSource() == playerPaper) playerNum = 0;
            else if (mouseEvent.getSource() == playerRock) playerNum = 1;
            else playerNum = 2;
            setShadow((Button) mouseEvent.getSource());
            checkScores();
        };
        playerScissors.setOnMouseClicked(playerEvent);
        playerPaper.setOnMouseClicked(playerEvent);
        playerRock.setOnMouseClicked(playerEvent);
    }

    private void setShadow(Button source) {
        DropShadow ds = new DropShadow(30, Color.GREEN);
        source.setEffect(ds);
    }

    private void checkScores() {
        if (!(playerChose && rivalChose)) return;
        playerChose = false;
        rivalChose = false;
        for (Button button : buttons) {
            button.setDisable(false);
            button.setEffect(null);
        }
        if ((playerNum + 1) % 3 == rivalNum) playerPoints++;
        else if ((playerNum + 2) % 3 == rivalNum) rivalPoints++;
        updatePoints();
    }

    private void updatePoints() {
        playerPoint.setText(String.valueOf(playerPoints));
        rivalPoint.setText(String.valueOf(rivalPoints));
        checkWin();
    }

    private void checkWin() {
        if (rivalPoints == 3 || playerPoints == 3) {
            for (Button button : buttons) {
                button.setDisable(true);
            }
            User winner = rivalPoints == 3 ? rival : player;
            result.setText(winner.getNickname() + " is the winner !");
            miniGame.setWinner(winner);
            Main.stage.getScene().getRoot().setOnMouseClicked(mouseEvent -> new OneRoundGameGraphical(miniGame.getWinner(), miniGame.getWinner() == miniGame.getFirstUser() ? miniGame.getSecondUser() : miniGame.getFirstUser()));
        }
    }

    private void loadScissorsButton(Button button) {
        button.setBackground(Menu.getBackGround("scissors", "png", 30, 30));
    }

    private void loadPaperButton(Button button) {
        button.setBackground(Menu.getBackGround("paper", "png", 30, 30));
    }

    private void loadRockButton(Button button) {
        button.setBackground(Menu.getBackGround("rock", "png", 30, 30));
    }
}