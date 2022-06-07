package view.graphics.duelgraphics;

import controller.GameMenuController;
import controller.GraphicalGameController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import main.Main;
import model.User;
import model.game.Game;
import model.graphicalModels.ActionsWrapper;
import view.graphics.Menu;

import java.net.URL;
import java.util.ResourceBundle;

import model.graphicalModels.ActionsWrapper.*;

public class OneRoundGameGraphical implements Initializable {
    private static Game game;
    private static GraphicalGameController graphicalGameController;
    @FXML
    private Pane pane;
    @FXML
    private ImageView playerMonster0;
    @FXML
    private ImageView playerMonster1;
    @FXML
    private ImageView playerMonster2;
    @FXML
    private ImageView playerMonster3;
    @FXML
    private ImageView playerMonster4;
    private ImageView[] playerMonsters = new ImageView[5];

    @FXML
    private ImageView playerSpell0;
    @FXML
    private ImageView playerSpell1;
    @FXML
    private ImageView playerSpell2;
    @FXML
    private ImageView playerSpell3;
    @FXML
    private ImageView playerSpell4;
    private ImageView[] playerSpells = new ImageView[5];

    @FXML
    private ImageView rivalSpell0;
    @FXML
    private ImageView rivalSpell1;
    @FXML
    private ImageView rivalSpell2;
    @FXML
    private ImageView rivalSpell3;
    @FXML
    private ImageView rivalSpell4;
    private ImageView[] rivalSpells = new ImageView[5];
    @FXML
    private ImageView rivalMonster0;
    @FXML
    private ImageView rivalMonster1;
    @FXML
    private ImageView rivalMonster2;
    @FXML
    private ImageView rivalMonster3;
    @FXML
    private ImageView rivalMonster4;
    private ImageView[] rivalMonsters = new ImageView[5];
    @FXML
    private HBox playerCardBox;
    @FXML
    private HBox rivalCardBox;
    @FXML
    private VBox buttonsMenu;
    @FXML
    private ImageView playerFieldSpell;
    @FXML
    private ImageView playerGraveYard;
    @FXML
    private ImageView rivalFieldSpell;
    @FXML
    private ImageView rivalGraveYard;
    @FXML
    private ImageView background;
    @FXML
    private Label playerName;
    @FXML
    private Label playerHealth;
    @FXML
    private Label rivalName;
    @FXML
    private Label rivalHealth;
    @FXML
    private Label phase;
    @FXML
    private Circle playerPic;
    @FXML
    private Circle rivalPic;

    public OneRoundGameGraphical() {

    }

    public OneRoundGameGraphical(User player, User rival) {
        try {
            OneRoundGameGraphical.game = new Game(player, rival);
            GameMenuController.firstDraw(game);
        } catch (CloneNotSupportedException ignored) {
        }
        pane = (Pane) Menu.getNode("OneRoundGameGraphical");
        Main.stage.setScene(new Scene(pane));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeArrays(playerMonsters, playerMonster0, playerMonster1, playerMonster2, playerMonster3, playerMonster4,
                playerSpells, playerSpell0, playerSpell1, playerSpell2, playerSpell3, playerSpell4);
        makeArrays(rivalSpells, rivalSpell0, rivalSpell1, rivalSpell2, rivalSpell3, rivalSpell4,
                rivalMonsters, rivalMonster0, rivalMonster1, rivalMonster2, rivalMonster3, rivalMonster4);
        graphicalGameController = new GraphicalGameController(playerMonsters, playerSpells, rivalMonsters, rivalSpells,
                playerCardBox, rivalCardBox, buttonsMenu, playerFieldSpell, playerGraveYard, rivalFieldSpell,
                rivalGraveYard, game, pane, playerName, playerHealth, rivalName, rivalHealth, phase, background, playerPic, rivalPic);
    }

    private void makeArrays(ImageView[] rivalSpells, ImageView rivalSpell0, ImageView rivalSpell1, ImageView rivalSpell2,
                            ImageView rivalSpell3, ImageView rivalSpell4, ImageView[] rivalMonsters, ImageView rivalMonster0,
                            ImageView rivalMonster1, ImageView rivalMonster2, ImageView rivalMonster3, ImageView rivalMonster4) {
        rivalSpells[0] = rivalSpell0;
        rivalSpells[1] = rivalSpell1;
        rivalSpells[2] = rivalSpell2;
        rivalSpells[3] = rivalSpell3;
        rivalSpells[4] = rivalSpell4;
        rivalMonsters[0] = rivalMonster0;
        rivalMonsters[1] = rivalMonster1;
        rivalMonsters[2] = rivalMonster2;
        rivalMonsters[3] = rivalMonster3;
        rivalMonsters[4] = rivalMonster4;
    }

    public void settings(MouseEvent mouseEvent) {
        Menu.goToSetting();
    }
}
