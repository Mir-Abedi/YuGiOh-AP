package view.graphics;

import controller.LoginMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController extends Menu implements Initializable {

    @FXML
    private Label Title;
    @FXML
    private ImageView imageOfMenu;
    @FXML
    private ImageView gameMenu;
    @FXML
    private ImageView profileMenu;
    @FXML
    private ImageView scoreBoard;
    @FXML
    private ImageView deckMenu;
    @FXML
    private ImageView shop;
    @FXML
    private ImageView babeFace;
    boolean isHappy = false;

    public final static Image SETTING_MENU_IMG = getImage("SettingIcon", "png");
    public final static Image DECK_MENU_IMG = getImage("DeckMenuIcon", "png");
    public final static Image SHOP_MENU_IMG = getImage("ShopMenuIcon", "png");
    public final static Image PROFILE_MENU_IMG = getImage("ProfileMenuIcon", "png");
    public final static Image SCOREBOARD_MENU_IMG = getImage("ScoreboardMenuIcon", "png");
    public final static Image HAPPY_FACE_IMG = getImage("HappyFace", "png");
    public final static Image NORMAL_FACE_IMG = getImage("NormalFace", "png");
    public final static Image GAME_MENU_IMG = getImage("StartGameMenu", "png");
    public final static Image GAME_IMG = getImage("Game", "png");
    public final static Image SETTING_IMG = getImage("Setting", "png");
    public final static Image SHOP_IMG = getImage("Shop", "png");
    public final static Image SCOREBOARD_IMG = getImage("Scoreboard", "png");
    public final static Image PROFILE_IMG = getImage("Profile", "png");
    public final static Image DECK_IMG = getImage("Deck", "png");
    public final static Image SAD_COMPUTER_IMG = getImage("SadComputer", "png");


    public void changeFace(MouseEvent mouseEvent) {
        if (isHappy) babeFace.setImage(NORMAL_FACE_IMG);
        else babeFace.setImage(HAPPY_FACE_IMG);

        isHappy = !isHappy;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deckMenu.setImage(DECK_MENU_IMG);
        shop.setImage(SHOP_MENU_IMG);
        scoreBoard.setImage(SCOREBOARD_MENU_IMG);
        profileMenu.setImage(PROFILE_MENU_IMG);
        babeFace.setImage(NORMAL_FACE_IMG);
        gameMenu.setImage(GAME_MENU_IMG);
        imageOfMenu.setImage(SAD_COMPUTER_IMG);
    }

    public void changeGameMenu(MouseEvent mouseEvent) {
        changeFace(null);
        if (isHappy) {
            gameMenu.setEffect(new DropShadow());
            changeMainPicture("Game");
            changeLabel("Game Menu");
        } else {
            resetIcons(gameMenu);
        }
    }

    public void changeProfileMenu(MouseEvent mouseEvent) {
        changeFace(null);
        if (isHappy) {
            profileMenu.setEffect(new DropShadow());
            changeMainPicture("Profile");
            changeLabel("Profile Menu");
        } else {
            resetIcons(profileMenu);
        }
    }

    public void changeScoreboard(MouseEvent mouseEvent) {
        changeFace(null);
        if (isHappy) {
            scoreBoard.setEffect(new DropShadow());
            changeMainPicture("Scoreboard");
            changeLabel("Scoreboard Menu");
        } else {
            resetIcons(scoreBoard);
        }
    }

    public void changeDeckMenu(MouseEvent mouseEvent) {
        changeFace(null);
        if (isHappy) {
            deckMenu.setEffect(new DropShadow());
            changeMainPicture("Deck");
            changeLabel("Deck Menu");
        } else {
            resetIcons(deckMenu);
        }
    }

    public void changeShop(MouseEvent mouseEvent) {
        changeFace(null);
        if (isHappy) {
            shop.setEffect(new DropShadow());
            changeMainPicture("Shop");
            changeLabel("Shop Menu");
        } else {
            resetIcons(shop);
        }
    }

    public void changeLabel(String message) {
        if (Title.getText().equals("")) Title.setText(message);
        else Title.setText("");
    }

    private void resetIcons(ImageView currentIcon) {
        currentIcon.setEffect(null);
        changeMainPicture("");
        changeLabel("");
    }


    public void changeMainPicture(String nameOfMenu) {
        switch (nameOfMenu) {
            case "Shop":
                imageOfMenu.setImage(SHOP_IMG);
                break;
            case "Profile":
                imageOfMenu.setImage(PROFILE_IMG);
                break;
            case "Deck":
                imageOfMenu.setImage(DECK_IMG);
                break;
            case "Game":
                imageOfMenu.setImage(GAME_IMG);
                break;
            case "Scoreboard":
                imageOfMenu.setImage(SCOREBOARD_IMG);
                break;
            default:
                imageOfMenu.setImage(SAD_COMPUTER_IMG);
        }

    }

    public void goToGameMenu(MouseEvent mouseEvent) {
        goToMenu("Duel");
    }

    public void goToProfileMenu(MouseEvent mouseEvent) {
        goToMenu("Profile");
    }

    public void goToScoreboard(MouseEvent mouseEvent) {
        goToMenu("Scoreboard");
    }

    public void goToDeckMenu(MouseEvent mouseEvent) {
        goToMenu("Deck");

    }

    public void goToShop(MouseEvent mouseEvent) {
        goToMenu("Shop");
    }

    public void showAbout(ActionEvent actionEvent) {
        showAbout();
    }

    public void goToSetting(ActionEvent actionEvent) {
        goToSetting();
    }

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void logout(ActionEvent actionEvent) {
        LoginMenuController.logout();
        showAlert("LOGGED OUT!!!");
        goToMenu("Welcome");
    }
}
