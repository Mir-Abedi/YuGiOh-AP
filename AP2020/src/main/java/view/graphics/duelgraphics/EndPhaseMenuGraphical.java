package view.graphics.duelgraphics;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.Main;
import model.card.Card;
import model.game.Game;
import view.graphics.Menu;

import java.util.ArrayList;
import java.util.Random;

public class EndPhaseMenuGraphical {
    private final Game game;
    ArrayList<ImageView> images = new ArrayList<>();

    public EndPhaseMenuGraphical(Game game) {
        this.game = game;
    }

    public void run() {
        Pane pane = (Pane) Main.stage.getScene().getRoot();
        ArrayList<Node> nodes = Menu.getRectangleAndButtonForGameMenus("Send to Graveyard!");
        pane.getChildren().add(nodes.get(0));
        Pane newPane = Menu.copyPane(pane);
        Stage newStage = Menu.copyStage(Main.stage);
        newStage.setScene(new Scene(newPane));
        ImageView iv = new ImageView(Menu.getImage("SadBabe", "jpg"));
        iv.setY(0);
        iv.setX(0);
        iv.setFitHeight(700);
        iv.setFitWidth(700);
        iv.setOpacity(0.20);
        newPane.getChildren().add(iv);
        newPane.getChildren().add(nodes.get(1));
        addCards(newPane);
        Label label = new Label("Please remove cards until there are 6 left. If not they will be randomly removed!");
        label.setStyle("-fx-background-color: transparent;-fx-font: 19px Chalkboard;-fx-text-fill: green;");
        label.setWrapText(true);
        label.setLayoutX(10);
        label.setLayoutY(100);
        newPane.getChildren().add(label);
        nodes.get(1).setOnMouseClicked(mouseEvent -> {
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).getEffect() != null) {
                    images.get(i).setDisable(true);
                    game.getPlayerBoard().getGraveyard().getCards().add(game.getPlayerHandCards().remove(i));
                    break;
                }
            }
            for (ImageView image : images) {
                image.setEffect(null);
            }
            if (game.getPlayerHandCards().size() == 6) {
                newStage.close();
            }
        });
        newStage.showAndWait();
        if (game.getPlayerHandCards().size() > 6) randomlyRemove();
        pane.getChildren().remove(nodes.get(0));
    }

    private void addCards(Pane newPane) {
        ArrayList<Card> cards = game.getPlayerHandCards();
        for (int i = 0; i < cards.size(); i++) {
            ImageView imageView = Menu.getImageWithSizeForGame(cards.get(i).getCardName(), 50 + i*75, 200);
            images.add(imageView);
            imageView.setOnMouseClicked(mouseEvent -> {
                for (ImageView image : images) {
                    image.setEffect(null);
                    imageView.setEffect(new DropShadow(40, Color.GREEN));
                }
            });
            newPane.getChildren().add(imageView);
        }
    }

    private void randomlyRemove() {
        while(game.getPlayerHandCards().size() != 6) {
            int rand = new Random().nextInt(game.getPlayerHandCards().size());
            game.getPlayerHandCards().remove(rand);
        }
    }
}
