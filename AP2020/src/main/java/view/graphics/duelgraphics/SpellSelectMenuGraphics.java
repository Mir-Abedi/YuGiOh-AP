package view.graphics.duelgraphics;

import controller.GameMenuController;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.Main;
import model.User;
import model.card.Card;
import model.game.Cell;
import model.game.Game;
import view.graphics.Menu;

import java.util.ArrayList;

public class SpellSelectMenuGraphics {
    private final Game game;
    private final User playerToChoose;

    public SpellSelectMenuGraphics(Game game, User PlayerToChoose) {
        this.game = game;
        this.playerToChoose = PlayerToChoose;
    }

    public Card run(int speed) {
        Cell chosenCell = new Cell();
        ArrayList<Node> nodes = Menu.getRectangleAndButtonForGameMenus("Active Effect!");
        Pane pane = (Pane)Main.stage.getScene().getRoot();
        pane.getChildren().add(nodes.get(0));
        Pane newPane = Menu.copyPane(pane);
        ImageView iv = new ImageView(Menu.getImage("yu-gi-ohs-best-and-worst-girl-role-models", "png"));
        iv.setY(0);
        iv.setX(0);
        iv.setFitHeight(700);
        iv.setFitWidth(700);
        iv.setOpacity(0.18);
        newPane.getChildren().add(iv);
        newPane.getChildren().add(nodes.get(1));
        Button button = (Button) nodes.get(1);
        Label label = new Label(playerToChoose.getNickname() + " Choose a spell to activate. if you close the window it will be canceled!");
        label.setStyle("-fx-background-color: transparent;-fx-font: 19px Chalkboard;-fx-text-fill: green;");
        label.setWrapText(true);
        label.setLayoutX(10);
        label.setLayoutY(100);
        newPane.getChildren().add(label);
        Cell[] cells = playerToChoose == game.getPlayer() ? game.getPlayerBoard().getSpellZone() : game.getRivalBoard().getSpellZone();
        int temp = 0;
        for (int j = 0; j < 5; j++) {
            if (cells[j].isOccupied()) {
                temp++;
            }
        }
        if (temp == 0) {
            pane.getChildren().remove(nodes.get(0));
            return null;
        }
        ImageView[] images = new ImageView[5];
        Stage newStage = Menu.copyStage(Main.stage);
        newStage.setScene(new Scene(newPane));
        for (int i = 0; i < 5; i++) {
            if (!cells[i].isOccupied()) continue;
            images[i] = Menu.getImageWithSizeForGame(cells[i].getCard().getCardName(), 200 + i * 80, 200);
            newPane.getChildren().add(images[i]);
            images[i].setOnMouseClicked(mouseEvent -> {
                for (int j = 0; j < 5; j++) {
                    if (cells[j].isOccupied()) {
                        images[j].setEffect(null);
                    }
                }
                ((ImageView)mouseEvent.getSource()).setEffect(new DropShadow(40, Color.RED));
            });
        }
        button.setOnMouseClicked(mouseEvent -> {
            int i = -1;
            for (int i1 = 0; i1 < images.length; i1++) {
                if (cells[i1].isOccupied()) {
                    if (images[i1].getEffect() != null) {
                        i = i1;
                        break;
                    }
                }
            }
            if (i == -1) return;
            chosenCell.addCard(game.getPlayerBoard().getSpellZone(i).getCard());
            newStage.close();
        });
        newStage.showAndWait();
        pane.getChildren().remove(nodes.get(0));
        if (chosenCell.getCard() == null) return null;
        int cardSpeed = GameMenuController.getSpeed(chosenCell.getCard().getFeatures());
        if (cardSpeed < speed) return null;
        return chosenCell.getCard();
    }
}