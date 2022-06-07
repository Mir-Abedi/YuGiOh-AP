package view.graphics;

import controller.LoginMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.enums.Cursor;
import model.enums.VoiceEffects;
import view.responses.LoginMenuResponses;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class SignUpMenu extends Menu implements Initializable {

    public final static Image SAD_CUBE = getImage("SadCube","png");
    public final static Image HAPPY_CUBE = getImage("HappyCube","png");

    @FXML private Button submitButton;
    @FXML private AnchorPane pane;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField passwordAgainField;
    @FXML private TextField nicknameField;
    @FXML private TextField usernameField;
    @FXML private ImageView cubeFace;


    public void signUp() {
        playMedia(VoiceEffects.CLICK);
        String password = passwordField.getText();
        String passwordAgain = passwordAgainField.getText();
        String username = usernameField.getText();
        String nickname = nicknameField.getText();
        LoginMenuResponses respond;
        if (!(nickname.equals("") || password.equals("") || passwordAgain.equals("") || username.equals(""))) {
            if (password.equals(passwordAgain)) {
                respond = LoginMenuController.createUser(username, nickname, password);
            } else respond = LoginMenuResponses.PASSWORDS_DIDNT_MATCH;
        } else respond = LoginMenuResponses.PLEASE_FILL_ALL_OF_THE_FIELDS;
        showAlert(respond.toString().replace("_"," "));
        if (respond != LoginMenuResponses.USER_CREATED_SUCCESSFULLY) {
            playMedia(VoiceEffects.ERROR);
            changeBabeFace(Face.SAD);
            activeTimer();
        }
        else {
            changeBabeFace(Face.HAPPY);
            goToMenu("Welcome");
        }
        nicknameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        passwordAgainField.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordField.textProperty().addListener(((observableValue, s, t1) -> playMedia(VoiceEffects.KEYBOARD_HIT)));
        passwordAgainField.textProperty().addListener(((observableValue, s, t1) -> playMedia(VoiceEffects.KEYBOARD_HIT)));
        usernameField.textProperty().addListener(((observableValue, s, t1) -> playMedia(VoiceEffects.KEYBOARD_HIT)));
        nicknameField.textProperty().addListener(((observableValue, s, t1) -> playMedia(VoiceEffects.KEYBOARD_HIT)));
        changeBabeFace(Face.SAD);
        activeButton();
    }

    private void activeButton() {
        justifyButton(submitButton, Cursor.ACCEPT);
        submitButton.setOnAction(actionEvent -> signUp());
        submitButton.setText("Sign Up!");
        changeBabeFace(Face.HAPPY);
    }

    private void activeTimer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                changeBabeFace(Face.HAPPY);
            }
        };
        timer.schedule(task,1500);
    }

    public void changeBabeFace(Face face) {
        if (face == Face.SAD) cubeFace.setImage(SAD_CUBE);
        if (face == Face.HAPPY) cubeFace.setImage(HAPPY_CUBE);
    }

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void goToLoginMenu(ActionEvent actionEvent) {
        goToMenu("Login");
    }

    private enum Face{
        SAD,HAPPY;
    }
}


