package view.graphics;

import controller.CardCreator;
import controller.database.CSVInfoGetter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.card.Attribute;
import model.card.Card;
import model.card.CardFeatures;
import model.card.monster.Monster;
import model.card.monster.MonsterCardType;
import model.card.monster.MonsterType;
import model.enums.Cursor;
import model.enums.VoiceEffects;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MonsterCreatorController extends Menu implements Initializable {
    private static ArrayList<String> effectMonsters = new ArrayList<>();

    {
        for (Card card : Card.getAllCards()) {
            if (card.isMonster() && ((Monster) card).hasEffect() && !((Monster) card).getFeatures().contains(CardFeatures.CUSTOM_CARD)) effectMonsters.add(card.getCardName());
        }
    }

    private static String[] monsterTypes = {"WARRIOR", "BEAST WARRIOR", "FIEND", "AQUA", "BEAST", "PYRO", "SPELLCASTER", "THUNDER", "DRAGON", "MACHINE", "ROCK", "INSECT", "CYBERSE", "FAIRY", "SEA SERPENT"};
    private static String[] attributes = {"DARK", "EARTH", "FIRE", "LIGHT", "WATER", "WIND"};
    @FXML
    private ChoiceBox typeBox;
    @FXML
    private ChoiceBox attributeBox;
    @FXML
    private Label nameLabel;
    @FXML
    private Label attackLabel;
    @FXML
    private Label defenceLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Button exitButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField attackField;
    @FXML
    private TextField defenceField;
    @FXML
    private ChoiceBox effects;
    @FXML
    private Button createButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTextFields();
        initButtons();
        initChoiceBoxes();
        attackLabel.setText("?!?");
        defenceLabel.setText("?!?");
        priceLabel.setText("?!?");
    }

    private void initChoiceBoxes() {
        effects.getItems().addAll("None", "Ritual");
        effects.getItems().addAll(effectMonsters);
        effects.selectionModelProperty().addListener((observableValue, o, t1) -> {
            playMedia(VoiceEffects.CLICK);
            priceLabel.setText(getPrice() + "");
        });
        typeBox.getItems().addAll(monsterTypes);
        typeBox.selectionModelProperty().addListener(((observableValue, o, t1) -> playMedia(VoiceEffects.CLICK)));
        attributeBox.getItems().addAll(attributes);
        attributeBox.selectionModelProperty().addListener(((observableValue, o, t1) -> playMedia(VoiceEffects.CLICK)));
        effects.setValue("None");
        typeBox.setValue("WARRIOR");
        attributeBox.setValue("DARK");
    }

    private void initButtons() {
        exitButton.setOnMouseEntered(mouseEvent -> changeCursor(Cursor.CANCEL, mouseEvent));
        exitButton.setOnMouseExited(mouseEvent -> changeCursor(Cursor.DEFAULT, mouseEvent));
        exitButton.setOnMouseClicked(mouseEvent -> close());
        justifyButton(createButton, Cursor.SWORD);
        createButton.setOnMouseClicked(mouseEvent -> {
            if (isDataValid()) {
                createMonster();
            } else playMedia(VoiceEffects.ERROR);
        });
    }

    private void initTextFields() {
        nameField.textProperty().addListener(((observableValue, s, t1) -> {
            playMedia(VoiceEffects.KEYBOARD_HIT);
            changeNameField(t1);
        }));
        attackField.textProperty().addListener(((observableValue, s, t1) -> {
            playMedia(VoiceEffects.KEYBOARD_HIT);
            changeAttackField(t1);
        }));
        defenceField.textProperty().addListener(((observableValue, s, t1) -> {
            playMedia(VoiceEffects.KEYBOARD_HIT);
            changeDefField(t1);
        }));
        descriptionField.textProperty().addListener(((observableValue, s, t1) -> playMedia(VoiceEffects.KEYBOARD_HIT)));
    }


    private void close() {
        playMedia(VoiceEffects.EXPLODE);
        ((Stage) createButton.getScene().getWindow()).close();
        goToMenu("Shop");
    }

    private void createMonster() {
        int atk = Integer.parseInt(attackField.getText());
        int def = Integer.parseInt(defenceField.getText());
        String cardName = nameField.getText();
        String description = descriptionField.getText();
        MonsterCardType type = MonsterCardType.NORMAL;
        String typeEffect = (String) effects.getSelectionModel().getSelectedItem();
        if (typeEffect != null && !typeEffect.equals("None")) {
            type = typeEffect.equals("Ritual") ? MonsterCardType.RITUAL : MonsterCardType.EFFECT;
        }
        int price = CardCreator.getMonsterPrice(atk, def, type);
        if (CardCreator.canCreate(price)) {
            if (typeEffect == null || typeEffect.equals("None") || typeEffect.equals("Ritual")) typeEffect = "";
            CardCreator.createMonster(cardName, description, atk, def, price, type, getAttribute(), getMonsterType(), typeEffect);
            playMedia(VoiceEffects.JINGLE);
            showAlert("SUCCESSFUL!!!");
            resetEveryThing();
        } else {
            playMedia(VoiceEffects.ERROR);
            showAlert("NOT ENOUGH MONEY Dude!!!");
        }
    }

    private void resetEveryThing() {
        effects.setValue("None");
        typeBox.setValue("WARRIOR");
        attributeBox.setValue("DARK");
        nameField.setText("");
        attackField.setText("");
        defenceField.setText("");
        attackLabel.setText("?!?");
        defenceLabel.setText("?!?");
        priceLabel.setText("?!?");
    }


    private void changeDefField(String t1) {
        if (t1.matches("^1?\\d{1,4}$")) {
            defenceLabel.setText(t1);
            int price = getPrice();
            String priceS = price == -1 ? "?!?" : price + "";
            priceLabel.setText(priceS);
        } else defenceLabel.setText("?!?");

    }

    private void changeAttackField(String t1) {
        if (t1.matches("^1?\\d{1,4}$")) {
            attackLabel.setText(t1);
            priceLabel.setText(getPrice() + "");
        } else attackLabel.setText("?!?");
    }

    private void changeNameField(String t1) {
        if (!Card.getCardNames().contains(t1)) nameLabel.setText(t1);
        else nameLabel.setText("MIO!");
    }


    private boolean isDataValid() {
        if (Card.getCardNames().contains(nameField.getText()) || nameField.getText().equals("")) {
            showAlert("ESM ET KHARABE!");
            return false;
        }
        if (!attackField.getText().matches("^1?\\d{1,4}$")) {
            showAlert("ATTACK ET KHARABE!!!");
            return false;
        }

        if (!defenceField.getText().matches("^1?\\d{1,4}$")) {
            showAlert("DEFENCE ET KHARABE!!!");
            return false;
        }
        return true;

    }

    private int getPrice() {
        try {
            int atk = Integer.parseInt(attackField.getText());
            int def = Integer.parseInt(defenceField.getText());
            MonsterCardType type = MonsterCardType.NORMAL;
            String typeEffect = (String) effects.getSelectionModel().getSelectedItem();
            if (typeEffect != null) {
                type = typeEffect.equals("Ritual") ? MonsterCardType.RITUAL : MonsterCardType.EFFECT;
            }
            return CardCreator.getMonsterPrice(atk, def, type);
        } catch (Exception e) {
            return -1;
        }
    }

    private MonsterType getMonsterType() {
        return MonsterType.valueOf(((String) typeBox.getSelectionModel().getSelectedItem()).replace(" ","_"));
    }

    private Attribute getAttribute() {
        return Attribute.valueOf((String) attributeBox.getSelectionModel().getSelectedItem());
    }
}
