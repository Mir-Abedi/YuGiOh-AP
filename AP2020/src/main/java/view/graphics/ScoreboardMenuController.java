package view.graphics;

import controller.LoginMenuController;
import controller.ScoreboardController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.User;
import model.enums.Cursor;
import model.enums.VoiceEffects;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static view.graphics.MainMenuController.HAPPY_FACE_IMG;
import static view.graphics.MainMenuController.NORMAL_FACE_IMG;

public class ScoreboardMenuController extends SearchMenu implements Initializable {
    public static final Image SAD_BABE_IMG = getImage("SadBabe","png");
    private final ArrayList<User> users = ScoreboardController.getSortedUsers();
    private final ArrayList<String> usernames = new ArrayList<>();
    private final HashMap<String, Parent> userOptions = new HashMap<>();
    private final HashMap<String,User> userHashMap = new HashMap<>();
    public BorderPane mainPane;
    private HBox currentUserBox;
    private User currentUser = LoginMenuController.getCurrentUser();
    private int currentPoint = currentUser.getScore();
    private final static File FILE = new File("src/main/resources/Scenes/ScoreboardOption.fxml");

    @FXML private Button minusButton;
    @FXML private Button plusButton;
    @FXML private Circle imagePlace;
    @FXML private Label usernameLabel;
    @FXML private ImageView babeFace;
    @FXML private Label nicknameLabel;
    @FXML private Label scoreLabel;

    {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            usernames.add(user.getUsername());
            userHashMap.put(usernames.get(i),user);
        }
    }

    {
        for (int i = users.size() - 1; i >= 0; i--) {
            User user = users.get(i);
            userOptions.put(user.getUsername(), getScoreboardOption(user, users.size() - i));
            if (user.getUsername().equals(currentUser.getUsername())) {
                currentUserBox = (HBox) userOptions.get(user.getUsername());
                currentUserBox.getChildren().get(0).setStyle("-fx-background-color: #f8ea9d; -fx-background-radius: 10; -fx-border-color: #000000; -fx-border-radius: 10;");
            }
        }
    }

    public void changeFace(int score) {
        if (score < 0) babeFace.setImage(NORMAL_FACE_IMG);
        else if (score < currentPoint) babeFace.setImage(HAPPY_FACE_IMG);
        else if (score == currentPoint) babeFace.setImage(NORMAL_FACE_IMG);
        else babeFace.setImage(SAD_BABE_IMG);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMinSizeSearchBox(0);
        resetSearchBox();
        searchField.textProperty().addListener((observableValue, s, t1) -> {
            playMedia(VoiceEffects.KEYBOARD_HIT);
            search(t1);
        });
        plusButton.setOnAction(actionEvent -> next());
        minusButton.setOnAction(actionEvent -> previous());
        setProfileBox(currentUserBox);
        changeFace(-1);
        justifyButton(plusButton, Cursor.RIGHT_ARROW);
        justifyButton(minusButton, Cursor.LEFT_ARROW);
        mainPane.setOnMouseClicked(mouseEvent -> mainPane.requestFocus());
    }

    @Override
    protected void search(String searchText) {
        if (searchText.equals("")) {
            resetSearchBox();
            return;
        }
        ArrayList<String> matchingUsers = new ArrayList<>();
        for (String card : usernames) if (card.toLowerCase().contains(searchText)) matchingUsers.add(card);
        ArrayList<VBox> resultBoxes = getSearchResults(matchingUsers);
        searchResults = new ArrayList<>();
        searchResults.addAll(resultBoxes);
        if (searchResults.size() > 0) showVBox(0);
        else {
            emptySearchBox();
            stageCounter.setText("-/-");
        }
    }

    private void resetSearchBox() {
        searchResults = getSearchResults(usernames);
        showVBox(0);
    }

    @Override
    protected ArrayList<VBox> getSearchResults(ArrayList<String> searchResults) {
        ArrayList<String> usernames = new ArrayList<>(this.usernames);
        ArrayList<VBox> searchBoxes = new ArrayList<>();
        usernames.retainAll(searchResults);
        VBox currentBox = new VBox(3.5);
        for (int i = usernames.size() - 1; i >= 0; i--) {
            if (currentBox.getChildren().size() == 11) {
                searchBoxes.add(currentBox);
                currentBox = new VBox(3);
            }
            currentBox.getChildren().add(userOptions.get(usernames.get(i)));
        }
        if (currentBox.getChildren().size() != 0) searchBoxes.add(currentBox);
        return searchBoxes;
    }


    public Parent getScoreboardOption(User user, int rank) {
        try {
            FXMLLoader loader = new FXMLLoader(FILE.toURI().toURL());
            HBox parent = loader.load();
            ScoreboardOptionCreator controller = loader.getController();
            controller.setProfileCircle(user.getProfilePhoto());
            controller.setRank(rank);
            controller.setUsername(user.getUsername());
            controller.setScore(user.getScore());
            Node node = parent.getChildren().get(0);
            justifyScoreboardOption(user, parent, node);
            return parent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void justifyScoreboardOption(User user, HBox parent, Node node) {
        node.setOnMouseClicked(mouseEvent -> {
            playMedia(VoiceEffects.CLICK);
            setProfileBox(parent);
        });
        node.setOnMouseExited(mouseEvent -> changeFace(-1));
        node.setOnMouseEntered(mouseEvent -> changeFace(user.getScore()));
    }

    public void setProfileBox(Pane option) {
        List<Node> list =((HBox) option.getChildren().get(0)).getChildren();
        String username = ((Label)list.get(1)).getText();
        String score = ((Label)list.get(3)).getText();
        String nickName = userHashMap.get(username).getNickname();
        scoreLabel.setText(score);
        imagePlace.setFill(((Circle)list.get(0)).getFill());
        nicknameLabel.setText(nickName);
        usernameLabel.setText(username);
    }

    public void goToMainMenu(ActionEvent actionEvent) {
        goToMainMenu();
    }

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void goToSetting(ActionEvent actionEvent) {
        goToSetting();
    }

    public void showAbout(ActionEvent actionEvent) {
       showAbout();
    }
}
