package view.graphics;


import controller.LoginMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.enums.Cursor;
import model.enums.VoiceEffects;
import view.MainMenu;
import view.responses.LoginMenuResponses;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeMenuController extends Menu implements Initializable {

    @FXML
    private ImageView babeFace;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private MenuBar menuBar;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private Button loginButton;


    public void goToSignInMenu(ActionEvent actionEvent) {
        goToMenu("Welcome");
    }

    public void login(ActionEvent actionEvent) {
        String password = passwordField.getText();
        String username = usernameField.getText();
        if (password.equals("") || username.equals("")) {
            showAlert("Please Fill all the Fields!");
            playMedia(VoiceEffects.ERROR);
        } else {
            LoginMenuResponses respond = LoginMenuController.login(username, password);
            showAlert(respond.toString().replace("_", " ") + "!");
            if (respond == LoginMenuResponses.USER_LOGIN_SUCCESSFUL) goToMainMenu();
            else playMedia(VoiceEffects.ERROR);
        }
    }

    public static void goToMainMenu() {
        goToMenu("Main");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        babeFace.setImage(getImage("animatedDragon","GIF"));
        setFocus();
        justifyButton(loginButton, Cursor.ACCEPT);
        mainPane.setOnMouseClicked(mouseEvent -> setFocus());
        usernameField.textProperty().addListener(((observableValue, s, t1) -> playMedia(VoiceEffects.KEYBOARD_HIT)));
        passwordField.textProperty().addListener(((observableValue, s, t1) -> playMedia(VoiceEffects.KEYBOARD_HIT)));
    }

    public void setFocus() {
        mainPane.requestFocus();
    }

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void showAbout(ActionEvent actionEvent) {
        showAbout();
    }
}
