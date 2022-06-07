package view.graphics.duelgraphics;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import main.Main;
import model.game.MiniGame;
import view.graphics.Menu;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class MiniGameCoin implements Initializable {
    private static MiniGame miniGame;
    private AnchorPane anchorPane;
    @FXML
    private Button button;
    @FXML
    private Label result;
    @FXML
    private ImageView imageView;

    public MiniGameCoin() {

    }

    public MiniGameCoin(MiniGame miniGame) {
        WinnerExceptionHolder.resetExceptions();
        MiniGameCoin.miniGame = miniGame;
        anchorPane = (AnchorPane) Menu.getNode("MiniGameCoin");
        Main.stage.setScene(new Scene(anchorPane, 600, 400));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventHandler<MouseEvent> eventHandler = mouseEvent -> {
            int rand = new Random().nextInt(2);
            if (rand == 0) tail();
            else head();
            button.setDisable(true);
        };
        button.setOnMouseClicked(eventHandler);
        button.setCursor(Cursor.HAND);
    }

    private void tail() {
        imageView.setImage(Menu.getImage("tail coin", "png"));
        miniGame.setWinner(miniGame.getSecondUser());
        result.setText(miniGame.getWinner().getNickname() + " won the mini game !");
        Main.stage.getScene().getRoot().setOnMouseClicked(mouseEvent -> new OneRoundGameGraphical(miniGame.getWinner(), miniGame.getWinner() == miniGame.getFirstUser() ? miniGame.getSecondUser() : miniGame.getFirstUser()));
    }

    private void head() {
        imageView.setImage(Menu.getImage("head coin", "png"));
        miniGame.setWinner(miniGame.getFirstUser());
        result.setText(miniGame.getWinner().getNickname() + " won the mini game !");
        Main.stage.getScene().getRoot().setOnMouseClicked(mouseEvent -> new OneRoundGameGraphical(miniGame.getWinner(), miniGame.getWinner() == miniGame.getFirstUser() ? miniGame.getSecondUser() : miniGame.getFirstUser()));
    }
}
