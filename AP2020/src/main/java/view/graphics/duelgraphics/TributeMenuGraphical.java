package view.graphics.duelgraphics;

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
import model.game.Cell;
import model.game.Game;
import view.graphics.Menu;

import java.util.ArrayList;

public class TributeMenuGraphical {
    private final Game game;

    public TributeMenuGraphical(Game game) {
        this.game = game;
    }

    public int[] run(int numberOfTributes) {
        ImageView[] images = new ImageView[5];
        int[] tributes = new int[numberOfTributes];
        tributes[0] = -1;
        for (int i = 0; i < 5; i++) {
            images[i] = new ImageView();
        }
        Pane pane = (Pane) Main.stage.getScene().getRoot();
        ArrayList<Node> nodes = Menu.getRectangleAndButtonForGameMenus("Tribute!");
        pane.getChildren().add(nodes.get(0));
        Pane newPane = Menu.copyPane(pane);
        Stage newStage = Menu.copyStage(Main.stage);
        newStage.setScene(new Scene(newPane));
        ImageView iv = new ImageView(Menu.getImage("tribute menu", "png"));
        iv.setY(0);
        iv.setX(0);
        iv.setFitHeight(700);
        iv.setFitWidth(700);
        iv.setOpacity(0.18);
        newPane.getChildren().add(iv);
        Label label = new Label( "Please choose cards you want to tribute. If not your summon will be cancelled.");
        label.setStyle("-fx-background-color: transparent;-fx-font: 19px Chalkboard;-fx-text-fill: green;");
        label.setWrapText(true);
        label.setLayoutX(10);
        label.setLayoutY(100);
        newPane.getChildren().add(label);
        Button button = (Button) nodes.get(1);
        button.setDisable(true);
        Cell[] cells = game.getPlayerBoard().getMonsterZone();
        for (int i = 0; i < 5; i++) {
            if (cells[i].isOccupied()) {
                images[i] = Menu.getImageWithSizeForGame(cells[i].getCard().getCardName(), 200 + i * 80, 200);
                images[i].setOnMouseClicked(mouseEvent -> {
                    if (((ImageView)mouseEvent.getSource()).getEffect() != null) ((ImageView)mouseEvent.getSource()).setEffect(null);
                    else ((ImageView)mouseEvent.getSource()).setEffect(new DropShadow(40, Color.RED));
                    int temp = 0;
                    for (int j = 0; j < 5; j++) {
                        if (images[j].getEffect() != null) temp++;
                    }
                    button.setDisable(temp != numberOfTributes);
                });
                newPane.getChildren().add(images[i]);
            }
        }
        newPane.getChildren().add(button);
        button.setOnMouseClicked(mouseEvent -> {
            int temp = 0;
            for (int i = 0; i < 5; i++) {
                if (images[i].getEffect() != null) {
                    tributes[temp++] = i;
                }
            }
            newStage.close();
        });
        newStage.showAndWait();
        pane.getChildren().remove(nodes.get(0));
        if (tributes[0] == -1) return null;
        else return tributes;
    }
}