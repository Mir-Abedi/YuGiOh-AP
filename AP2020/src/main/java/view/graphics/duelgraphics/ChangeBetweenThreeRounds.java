package view.graphics.duelgraphics;

import controller.LoginMenuController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Main;
import model.User;
import model.card.Card;
import model.deck.Deck;
import model.deck.MainDeck;
import model.deck.SideDeck;
import model.enums.VoiceEffects;
import model.graphicalModels.CardHolder;
import view.graphics.ChoiceMenu;
import view.graphics.Menu;

import java.util.HashMap;
import java.util.HashSet;

public class ChangeBetweenThreeRounds extends Menu {

    @FXML
    private Label username;
    @FXML
    private Button swap;
    @FXML
    private HBox mainChoiceBox;
    @FXML
    private HBox sideChoiceBox;

    private static User user;
    private static Deck changedDeck;
    private static Deck deck;
    private static SideDeck sideDeck;
    private static MainDeck mainDeck;
    private ChoiceMenu mainChoiceMenu;
    private ChoiceMenu sideChoiceMenu;
    private Card mainCard = null;
    private Card sideCard = null;

    private final HashMap<String, VBox> mainCardBoxes = new HashMap<>();
    private final HashMap<String, VBox> sideCardBoxes = new HashMap<>();

    public ChangeBetweenThreeRounds() {

    }

    public ChangeBetweenThreeRounds(User user){
        ChangeBetweenThreeRounds.user = user;
        Main.stage.setScene(new Scene(Menu.getNode("ChangeBetweenThreeRoundsMenu")));
    }

    public void initialize(){
        setUsername();
        setDeck();
        settingBoxes();
        swap.setDisable(true);
        initChoiceMenus();
        mainChoiceMenu.resetChoiceBox();
        sideChoiceMenu.resetChoiceBox();
    }

    private void settingBoxes(){
        setBoxes(mainDeck.getCardNames(), mainCardBoxes, true);
        setBoxes(sideDeck.getCardNames(), sideCardBoxes, false);
    }

    private void setBoxes(HashSet<String> cardNames, HashMap<String, VBox> CardBoxes, boolean isMain) {
        for (String cardName : cardNames) {
            VBox choiceBox = new VBox();
            choiceBox.setSpacing(10);
            CardHolder holder = new CardHolder(getCard(cardName));
            holder.scale(0.65);
            double width = holder.getWidth();
            double height = holder.getHeight();
            choiceBox = (VBox) setDimension(choiceBox, width, height + 50);
            Label name = getLabel(cardName, width, 15, 10);
            choiceBox.getChildren().add(name);
            choiceBox.getChildren().add(holder);
            holder.setFromMainDeck(isMain);
            setMouseClicked(holder , true);
            CardBoxes.put(cardName, choiceBox);
        }
    }

    private void setMouseClicked(CardHolder holder , boolean isFirstClick){
        holder.setOnMouseClicked(mouseEvent -> {
            holder.rotate(10);
            holder.setOnMouseClicked(mouseEvent1 -> {
                if (holder.getIsFromMainDeck())
                    mainCard = null;
                else
                    sideCard = null;
                holder.rotate(0);
                checkSwapButton();
                playMedia(VoiceEffects.CLICK);
                setMouseClicked(holder , !isFirstClick);
            });
            playMedia(VoiceEffects.CLICK);
            if (isFirstClick){
                if (holder.getIsFromMainDeck())
                    mainCard = holder.getCard();
                else
                    sideCard = holder.getCard();
            }

            checkSwapButton();
        });
    }

    private void checkSwapButton(){
        swap.setDisable(mainCard == null || sideCard == null);
    }

    private void initChoiceMenus() {
        setChoiceMenu();
        mainChoiceMenu.setChoiceNames(new HashSet<>(mainDeck.getCardNames()));
        sideChoiceMenu.setChoiceNames(new HashSet<>(sideDeck.getCardNames()));
        mainChoiceMenu.setSpacing(5);
        mainChoiceBox.setSpacing(5);
        sideChoiceMenu.setSpacing(5);
        sideChoiceBox.setSpacing(5);
        mainChoiceMenu.setWidth(150 * 0.65 + 5);
        sideChoiceMenu.setWidth(150 * 0.65 + 5);
        mainChoiceMenu.setChoiceBox(mainChoiceBox);
        sideChoiceMenu.setChoiceBox(sideChoiceBox);
    }

    private void setChoiceMenu() {
        mainChoiceMenu = new ChoiceMenu() {
            @Override
            protected VBox getChoiceBox(String result) {
                return mainCardBoxes.get(result);
            }
        };
        sideChoiceMenu = new ChoiceMenu() {
            @Override
            protected VBox getChoiceBox(String result) {
                return sideCardBoxes.get(result);
            }
        };
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUsername(){
        user = LoginMenuController.getCurrentUser();
        username.setText(user.getUsername());
    }

    public void setDeck() {
        deck = user.getActiveDeck();
        mainDeck = deck.getMainDeck();
        sideDeck = deck.getSideDeck();
    }

    public void done() {
        User player;
        User rival;
        if (WinnerExceptionHolder.winnerException2 == null) {
            player = WinnerExceptionHolder.winnerException1.getLoser();
            rival = WinnerExceptionHolder.winnerException1.getWinner();
        } else {
            player = WinnerExceptionHolder.winnerException2.getLoser();
            rival = WinnerExceptionHolder.winnerException2.getWinner();
        }
        new OneRoundGameGraphical(player, rival);
    }

    public void swap() {
        changedDeck = deck.clone();
        changedDeck.getMainDeck().addCard(sideCard);
        changedDeck.getMainDeck().removeCard(mainCard.getCardName());
        changedDeck.getSideDeck().addCard(mainCard);
        changedDeck.getSideDeck().removeCard(sideCard.getCardName());
    }
}
