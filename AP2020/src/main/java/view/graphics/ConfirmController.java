package view.graphics;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.enums.VoiceEffects;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmController implements Initializable {
    private static boolean result = false;
    @FXML
    private Button denyButton;
    @FXML
    private Button acceptButton;
    @FXML
    private Label text;

    public static boolean getResult() {
        return result;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acceptButton.setOnMouseClicked(mouseEvent -> {
            result = true;
            close();
        });
        denyButton.setOnMouseClicked(mouseEvent -> {
            result = false;
            close();
        });
    }
    public void setText(String text) {
        this.text.setText(text);
    }

    private void close() {
        Menu.setCurrentScene(Menu.getSceneBuffer());
        Menu.setSceneBuffer(null);
        Menu.playMedia(VoiceEffects.EXPLODE);
        ((Stage) acceptButton.getScene().getWindow()).close();
    }
}
