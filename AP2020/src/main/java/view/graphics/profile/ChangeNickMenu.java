package view.graphics.profile;

import controller.LoginMenuController;
import controller.ProfileController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import model.enums.Cursor;
import model.enums.VoiceEffects;
import view.responses.ProfileMenuResponses;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangeNickMenu extends ChangeMenu implements Initializable {

    @FXML
    private TextField newNick;


    @Override
    public void change() {
        String newNickName = newNick.getText();
        if (newNickName.equals("")) {
            playMedia(VoiceEffects.ERROR);
            return;
        }
        ProfileMenuResponses responses = ProfileController.changeNickname(newNickName);
        if (responses != ProfileMenuResponses.NICKNAME_CHANGED_SUCCESSFULLY) playMedia(VoiceEffects.ERROR);
        else {
            ProfileMenu.staticNickName.setText(newNickName);
            newNick.setText("");
            playMedia(VoiceEffects.JINGLE);
        }
        showAlert(responses.toString().replace("_"," "));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newNick.textProperty().addListener(((observableValue, s, t1) -> {
            playMedia(VoiceEffects.KEYBOARD_HIT);
            if (newNick.getText().equals("")) justifyButton(changeButton, Cursor.CANCEL);
            else justifyButton(changeButton,Cursor.ACCEPT);
        }));
        justifyButton(changeButton, Cursor.CANCEL);
    }
}
