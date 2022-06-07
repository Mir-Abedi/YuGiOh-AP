package view.graphics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.User;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class ScoreboardOptionCreator {

    @FXML
    private Circle profileCircle;
    @FXML
    private Label username;
    @FXML
    private Label score;
    @FXML
    private Label rank;

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public void setRank(int rank) {
        this.rank.setText("#"+rank);
    }

    public void setScore(int score) {
        this.score.setText("" + score);
    }

    public void setProfileCircle(int profileNumber) {
        profileCircle.setFill(new ImagePattern(Menu.getProfilePhoto(profileNumber)));
    }


}
