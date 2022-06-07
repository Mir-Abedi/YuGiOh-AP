package view.graphics;

import controller.CardCreator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.card.Card;
import model.card.spell_traps.Limitation;
import model.enums.VoiceEffects;

import java.util.ArrayList;

public class SpellCreatorController extends Menu{
    @FXML
    private ChoiceBox<String> typeBox;
    @FXML
    private ChoiceBox<String> attributeBox;
    @FXML
    private ChoiceBox<Object> effects;
    @FXML
    private ChoiceBox<String> limitBox;
    @FXML
    private Label nameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Button exitButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private CheckBox isTrap;
    @FXML
    private Button createButton;

    private static final String[] spellTypes = {"EQUIP","FIELD","QUICKPLAY","RITUAL","CONTINUOUS","NORMAL","COUNTER"};
    private static final String[] attributes = {"DARK", "EARTH", "FIRE", "LIGHT", "WATER", "WIND"};
    private static final String[] limits ={"UNLIMITED","LIMITED","FORBIDDEN","SEMI_LIMITED"};
    private static ArrayList<String> effectSpells = new ArrayList<>();
    static {
        for (Card card : Card.getAllCards()) {
            if (card.isSpell() || card.isTrap()) effectSpells.add(card.getCardName());
        }
    }
    public void initialize(){
        setCreateButton();
        setChoiceBoxes();
        setTextFields();
    }

    public void create() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        String syncedEffect = (String) effects.getValue();
        Limitation limitation = getLimit();
        assert limitation != null;
        CardCreator.createSpellOrTrap(name,description,syncedEffect,limitation,getPrice(),isTrap.isSelected());
    }
    private void setChoiceBoxes(){
        typeBox.setItems(FXCollections.observableArrayList(spellTypes));
        attributeBox.setItems(FXCollections.observableArrayList(attributes));
        effects.setItems(FXCollections.observableArrayList(effectSpells));
        effects.setOnAction(actionEvent -> priceLabel.setText(getPrice()+""));
        limitBox.setItems(FXCollections.observableArrayList(limits));
    }

    private void setCreateButton(){
        createButton.setOnMouseClicked(mouseEvent -> {
            if(checkStats())
                create();
        });
    }

    private void setTextFields(){
        nameField.textProperty().addListener(((observableValue, s, t1) -> {
            playMedia(VoiceEffects.KEYBOARD_HIT);
            changeNameField(t1);
        }));
        descriptionField.textProperty().addListener(((observableValue, s, t1) -> playMedia(VoiceEffects.KEYBOARD_HIT)));
    }

    private Limitation getLimit(){
        switch (limitBox.getValue()) {
            case "UNLIMITED":
                return Limitation.UNLIMITED;
            case "LIMITED":
                return Limitation.LIMITED;
            case "SEMI_LIMITED":
                return Limitation.SEMI_LIMITED;
            case "FORBIDDEN":
                return Limitation.FORBIDDEN;
            default:
                return null;
        }
    }

    private void changeNameField(String t1) {
        if (!Card.getCardNames().contains(t1)) nameLabel.setText(t1);
        else nameLabel.setText("MIO!");
    }

    private boolean checkStats(){
        if (effects.getValue() == null)
            return false;
        else if (limitBox.getValue() == null)
            return false;
        else if (nameField.getText() == null)
            return false;
        else if (descriptionField.getText() == null)
            return false;
        else return true;
    }

    private int getPrice(){
        try{
            return CardCreator.getSpellPrice((String) effects.getValue());
        }
        catch (Exception exception){
            return 0;
        }
    }

    public void exit() {
        playMedia(VoiceEffects.EXPLODE);
        ((Stage) createButton.getScene().getWindow()).close();
        goToMenu("Shop");
    }
}
