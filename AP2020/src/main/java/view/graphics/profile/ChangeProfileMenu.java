package view.graphics.profile;

import controller.LoginMenuController;
import controller.ProfileController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.enums.Cursor;
import model.enums.VoiceEffects;

import java.net.URL;
import java.util.ResourceBundle;

import static model.User.NUMBER_OF_PROFILES;

public class ChangeProfileMenu extends ChangeMenu implements Initializable {
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label counter;
    @FXML
    private Circle profilePlace;

    private int currentProfile = 0;

    @Override
    public void change() {
        playMedia(VoiceEffects.JINGLE);
        ProfileController.changeProfilePhoto(currentProfile);
        setProfileInCircle((Circle) ((VBox) ((BorderPane) getCurrentScene().getRoot()).getLeft()).getChildren().get(0));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentProfile = LoginMenuController.getCurrentUser().getProfilePhoto();
        setLabel();
        setProfileInCircle(profilePlace);
    }

    private void setLabel() {
        counter.setText((currentProfile + 1) + "/" + NUMBER_OF_PROFILES);
    }

    private void setProfileInCircle(Circle circle) {
        Image currentProfileIMG = getProfilePhoto(currentProfile);
        circle.setFill(new ImagePattern(currentProfileIMG));
    }


    public void previousProfile(ActionEvent actionEvent) {
        playMedia(VoiceEffects.CLICK);
        currentProfile--;
        if (currentProfile == -1) currentProfile = NUMBER_OF_PROFILES - 1;
        setProfileInCircle(profilePlace);
        setLabel();
    }

    public void nextProfile(ActionEvent actionEvent) {
        playMedia(VoiceEffects.CLICK);
        currentProfile++;
        if (currentProfile == NUMBER_OF_PROFILES) currentProfile = 0;
        setProfileInCircle(profilePlace);
        setLabel();
    }


    public void enterPrevious(MouseEvent mouseEvent) {
        enterButton(previousButton, Cursor.LEFT_ARROW, mouseEvent);
    }

    public void exitPrevious(MouseEvent mouseEvent) {
        exitButton(previousButton, mouseEvent);
    }

    public void enterNext(MouseEvent mouseEvent) {
        enterButton(nextButton, Cursor.RIGHT_ARROW, mouseEvent);
    }

    public void exitNext(MouseEvent mouseEvent) {
        exitButton(nextButton, mouseEvent);
    }
}
