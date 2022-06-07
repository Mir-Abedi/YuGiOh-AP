package model.graphicalModels;

import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import model.card.Card;
import view.graphics.Menu;

public class CardHolder extends Pane {
    private final static double CARD_BOX_RATIO = (double) 4 / 3;
    private final static double CARD_RATION = (double) 188 / 138;
    private final static Image BORDER = Menu.getImage("CardPlace", "png");
    private final static Image BACK_OF_CARD = Menu.getCard("BACK");
    private final ImageView cardPlace;
   // private final ImageView cardBehind;
    public ImageView cardBox;
    private Card card;
    private Image cardImage;
    private boolean isFromMainDeck;

    private boolean isBack = false;

    public CardHolder(Card card) {
        setCard(card);
    }

    public CardHolder(Image card) {
        setCardImage(card);
    }

    public CardHolder() {
//        setCardImage(BACK_OF_CARD);
    }


    public void setCardImage(Image card) {
        cardImage = card;
        cardPlace.setImage(card);
    }

    {
        cardPlace = new ImageView();
        cardPlace.setLayoutX(6);
        cardPlace.setLayoutY(6);
        cardPlace.setFitHeight(188.0);
        cardPlace.setFitWidth(138.0);
        cardPlace.setImage(Menu.getCard("BACK"));
    }

    {
        cardBox = new ImageView();
        cardBox.setImage(BORDER);
        cardBox.setFitWidth(150.0);
        cardBox.setFitHeight(200.0);
    }

    {
//        cardBehind = new ImageView();
//        cardBehind.setLayoutX(6);
//        cardBehind.setLayoutY(6);
//        cardBehind.setFitHeight(188.0);
//        cardBehind.setFitWidth(138.0);
//        // cardBehind.getTransforms().add(new Rotate(180,new Point3D(1,0,0)));
//        cardBehind.setImage(BACK_OF_CARD);

    }

    {
        this.setHeight(250.0);
        this.setWidth(150);
       // this.getChildren().add(cardBehind);
        this.getChildren().add(cardPlace);
        this.getChildren().add(cardBox);

    }

    public void rotate(double angel) {
        cardPlace.setRotate(angel);
        cardBox.setRotate(angel);
    }

    public void setFromMainDeck(boolean isFromMainDeck){
        this.isFromMainDeck = isFromMainDeck;
    }

    public boolean getIsFromMainDeck(){
        return isFromMainDeck;
    }

    public void setCard(Card card) {
        setCardImage(Menu.getCard(card.getCardName()));
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public String getCardName() {
        if (card != null) return card.getCardName();
        String[] data = cardPlace.getImage().getUrl().split("/");
        return data[data.length - 1].split("\\.")[0];
    }

    public void flipCard() {
        if (isBack) cardPlace.setImage(cardImage);
        else cardPlace.setImage(BACK_OF_CARD);
        isBack = !isBack;
    }

    public void scale(double multiple) {
        cardPlace.setLayoutX(6 * multiple);
        cardPlace.setLayoutY(6 * multiple);
        cardPlace.setFitHeight(188.0 * multiple);
        cardPlace.setFitWidth(138.0 * multiple);
//        cardBehind.setLayoutX(6 * multiple);
//        cardBehind.setLayoutY(6 * multiple);
//        cardBehind.setFitHeight(188.0 * multiple);
//        cardBehind.setFitWidth(138.0 * multiple);
        cardBox.setFitWidth(150.0 * multiple);
        cardBox.setFitHeight(200.0 * multiple);
        super.setWidth(150 * multiple);
        super.setHeight(150 * multiple);
    }
}
