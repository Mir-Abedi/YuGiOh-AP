package view.graphics.profile;

import controller.ProfileController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import model.enums.Cursor;
import model.enums.VoiceEffects;
import view.responses.ProfileMenuResponses;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePassMenu extends ChangeMenu implements Initializable{

    @FXML private PasswordField newPass;
    @FXML private PasswordField oldPass;

    @Override
    public void change() {
        ProfileMenuResponses responses = ProfileController.changePassword(newPass.getText(),oldPass.getText());
        if (responses != ProfileMenuResponses.PASSWORD_CHANGED_SUCCESSFULLY) playMedia(VoiceEffects.ERROR);
        else playMedia(VoiceEffects.CLICK);
        showAlert(responses.toString().replace("_"," "));
    }

    public void enterChangeButton(MouseEvent mouseEvent) {
        enterButton(changeButton, Cursor.ACCEPT,mouseEvent);
    }

    public void exitChangeButton(MouseEvent mouseEvent) {
        exitButton(changeButton,mouseEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newPass.textProperty().addListener(((observableValue, s, t1) -> playMedia(VoiceEffects.KEYBOARD_HIT)));
        oldPass.textProperty().addListener(((observableValue, s, t1) -> playMedia(VoiceEffects.KEYBOARD_HIT)));
        changeButton.setOnMouseClicked(mouseEvent -> change());
    }
}
