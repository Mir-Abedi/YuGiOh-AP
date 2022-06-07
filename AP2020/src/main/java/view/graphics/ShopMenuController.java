package view.graphics;

import controller.LoginMenuController;
import controller.ShopController;
import controller.database.CSVInfoGetter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.card.Card;
import model.card.monster.Monster;
import model.enums.Cursor;
import model.enums.VoiceEffects;
import model.graphicalModels.CardHolder;
import view.regexes.RegexFunctions;
import view.responses.ShopMenuResponses;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;

public class ShopMenuController extends SearchMenu implements Initializable {
    @FXML
    private BorderPane mainPane;
    @FXML
    private Label nameLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Button plusButton;
    @FXML
    private Button minusButton;
    @FXML
    private Button shopButton;
    @FXML
    private CardHolder cardHolder;

    private static final String advanceSearchRegex = "^(?<type>type:\\s*[\\w /]+)?\\s*(?<price>price:\\s*\\d+(?:-?\\d+)?)?\\s*(?<attack>attack:\\s*\\d+(?:-?\\d+)?)?\\s*(?<defend>defend:\\s*\\d+(?:-?\\d*)?)?$";

    public final ArrayList<String> cardNames = CSVInfoGetter.getCardNames();
    public final ArrayList<Card> cards = new ArrayList<>();
    public final HashMap<String, Integer> prices = new HashMap<>();
    {
        for (String cardName : cardNames) {
            cards.add(CSVInfoGetter.getCardByName(cardName));
            prices.put(cardName, CSVInfoGetter.getPriceByCardName(cardName));
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchField.textProperty().addListener((observableValue, s, t1) -> {
            playMedia(VoiceEffects.KEYBOARD_HIT);
            search(t1);
        });
        deActiveButton();
        justifyButton(plusButton, Cursor.RIGHT_ARROW);
        justifyButton(minusButton, Cursor.LEFT_ARROW);
        nameLabel.setText("None");
        typeLabel.setText("None");
        priceLabel.setText("0000");
        updateBalanceLabel();
        stageCounter.setText("-/-");
        searchBox.setSpacing(5);
    }

    private void updateBalanceLabel() {
        balanceLabel.setText(Integer.toString(LoginMenuController.getCurrentUser().getBalance()));
    }

    public void buy() {
        String cardName = nameLabel.getText();
        if (cardName.equals("None")) return;
        playMedia(VoiceEffects.COIN_DROP);
        ShopMenuResponses respond = ShopController.buyCard(cardName);
        showAlert(respond.toString().replace("_"," "));
        updateBalanceLabel();
        int price = Integer.parseInt(priceLabel.getText());
        if (price > LoginMenuController.getCurrentUser().getBalance()) deActiveButton();
    }

    private void activeButton() {
        shopButton.setText("Buy!");
        shopButton.setOnMouseClicked(mouseEvent -> {
            playMedia(VoiceEffects.COIN_DROP);
            buy();
        });
        justifyButton(shopButton,Cursor.ACCEPT);
    }

    private void deActiveButton() {
        shopButton.setText("Buy!");
        shopButton.setOnMouseClicked(mouseEvent -> playMedia(VoiceEffects.ERROR));
        justifyButton(shopButton,Cursor.CANCEL);
    }

    public void nextMenu(ActionEvent actionEvent) {
        next();
    }

    public void previousMenu(ActionEvent actionEvent) {
        previous();
    }


    protected void search(String searchText) {
        searchText = searchText.trim().toLowerCase();
        if (searchText.equals("")) {
            emptySearchBox();
            stageCounter.setText("-/-");
            return;
        }
        if (searchText.charAt(0) == '#') {
            advancedSearch(searchText);
            return;
        }
        ArrayList<String> matchingCards = new ArrayList<>();
        for (String card : cardNames) if (card.toLowerCase().contains(searchText)) matchingCards.add(card);


        ArrayList<VBox> resultBoxes = getSearchResults(matchingCards);
        searchResults = new ArrayList<>();
        searchResults.addAll(resultBoxes);
        if (searchResults.size() > 0) showVBox(0);
        else {
            emptySearchBox();
            stageCounter.setText("-/-");
        }
    }

    @Override
    protected ArrayList<VBox> getSearchResults(ArrayList<String> searchResults) {
        ArrayList<VBox> resultBoxes = new ArrayList<>();
        VBox currentBox = new VBox(2);
        for (String result : searchResults) {
            if (currentBox.getChildren().size() == 9) {
                resultBoxes.add(currentBox);
                currentBox = new VBox(2);
            }
            currentBox.getChildren().add(getOptionButton(result));
        }
        if (currentBox.getChildren().size() != 0) resultBoxes.add(currentBox);
        return resultBoxes;
    }

    private void advancedSearch(String searchText) {
        emptySearchBox();
        if (searchText.equals("#")) return;
        searchText = searchText.substring(1);
        if (searchText.matches(advanceSearchRegex)) {
            Matcher matcher = RegexFunctions.getCommandMatcher(searchText, advanceSearchRegex);
            if (matcher.find()) {
                String type = matcher.group("type");
                String defend = matcher.group("defend");
                String attack = matcher.group("attack");
                String price = matcher.group("price");
                HashMap<String, String> searchTypes = new HashMap<>();
                if (type != null) searchTypes.put("type", type);
                if (price != null) searchTypes.put("price", price);
                if (defend != null) searchTypes.put("defend", defend);
                if (attack != null) searchTypes.put("attack", attack);
                ArrayList<String> filteredCards = filterCards(searchTypes);
                ArrayList<VBox> searchBoxes = getSearchResults(filteredCards);
                searchResults = new ArrayList<>(searchBoxes);
                if (searchResults.size() > 0) showVBox(0);
            }
        } else if (searchText.equalsIgnoreCase("all")) {
            ArrayList<VBox> searchBoxes = getSearchResults(cardNames);
            searchResults = new ArrayList<>(searchBoxes);
            if (searchResults.size() > 0) showVBox(0);
        }

    }

    private ArrayList<String> filterCards(HashMap<String, String> searchTypes) {
        ArrayList<String> cardNames = (ArrayList<String>) this.cardNames.clone();
        if (searchTypes.containsKey("price")) filterPrice(cardNames, searchTypes.get("price"));
        if (searchTypes.containsKey("type")) filterType(cardNames, searchTypes.get("type"));
        if (searchTypes.containsKey("attack")) filterAttack(cardNames, searchTypes.get("attack"));
        if (searchTypes.containsKey("defend")) filterDefend(cardNames, searchTypes.get("defend"));
        return cardNames;
    }

    private void filterDefend(ArrayList<String> cardNames, String defendLimits) {
        int[] ints = getMinMax(defendLimits);
        for (int i = 0; i < cardNames.size(); i++) {
            Card card = getCardByName(cardNames.get(i));
            if (card instanceof Monster) {
                int defense = ((Monster)card).getDefense();
                if (!(defense >= ints[0] && defense <= ints[1])) cardNames.remove(cardNames.get(i--));
            } else cardNames.remove(cardNames.get(i--));
        }
    }

    private void filterType(ArrayList<String> cardNames, String typeLimit) {
        typeLimit = typeLimit.split(":")[1].trim();
        String[] typesArray = typeLimit.split("/");
        HashSet<String> types = new HashSet<>();
        for (int i = 0; i < typesArray.length; i++) types.add(typesArray[i].trim().toLowerCase());
        for (int i = 0; i < cardNames.size(); i++) {
            if (!types.contains(getCardByName(cardNames.get(i)).getCardType().toString().toLowerCase())) cardNames.remove(cardNames.get(i--));
        }
    }

    private void filterPrice(ArrayList<String> cardNames, String priceLimit) {
        int[] ints = getMinMax(priceLimit);
        for (int i = 0; i < cardNames.size(); i++) {
            int cardPrice = prices.get(cardNames.get(i));
            if (!(cardPrice >= ints[0] && cardPrice <= ints[1])) cardNames.remove(cardNames.get(i--));
        }
    }

    private void filterAttack(ArrayList<String> cardNames, String attackLimit) {
        int[] ints = getMinMax(attackLimit);
        for (int i = 0; i < cardNames.size(); i++) {
            Card card = getCardByName(cardNames.get(i));
            if (card instanceof Monster) {
                int cardAttack = ((Monster)card).getAttack();
                if (!(cardAttack >= ints[0] && cardAttack <= ints[1])) cardNames.remove(cardNames.get(i--));
            } else cardNames.remove(cardNames.get(i--));
        }
    }

    private int[] getMinMax(String limit){
        limit =  limit.split(":")[1].trim();
        int[] numbers = new int[2];
        if (!limit.contains("-")) {
            numbers[0] = numbers[1] = Integer.parseInt(limit);
        } else {
            String[] attacks = limit.split("-");
            numbers[0] = Integer.parseInt(attacks[0]);
            numbers[1] = Integer.parseInt(attacks[1]);
        }
        return numbers;
    }

    protected Button getOptionButton(String searchResult) {
        Button button = new Button(searchResult);
        button.setPrefHeight(28);
        button.setPrefWidth(120);
        button.setStyle("-fx-border-style: solid none solid");
        button.setOnAction(actionEvent -> {
            playMedia(VoiceEffects.CLICK);
            Card card = CSVInfoGetter.getCardByName(searchResult);
            cardHolder.setCardImage(getCard(searchResult));
            int price = CSVInfoGetter.getPriceByCardName(searchResult);
            priceLabel.setText(price + "");
            nameLabel.setText(card.getCardName());
            typeLabel.setText(card.getCardType().toString().toLowerCase());
            if (price > LoginMenuController.getCurrentUser().getBalance())  deActiveButton();
            else activeButton();
        });
        justifyButton(button, Cursor.SEARCH);
        return button;
    }

    private Card getCardByName(String name) {
        for (Card card : cards) if (card.getCardName().equals(name)) return card;
        return null;
    }

    public void goToMainMenu(ActionEvent actionEvent) {
        goToMainMenu();
    }

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void goToSetting(ActionEvent actionEvent) {
        playMedia(VoiceEffects.CLICK);
        goToSetting();
    }

    public void showAbout(ActionEvent actionEvent) {
        playMedia(VoiceEffects.CLICK);
        showAbout();
    }

    public void goToMonsterCreator(ActionEvent actionEvent) {
        playMedia(VoiceEffects.CLICK);
        showNodeAssPopUp(getNode("MonsterCreator"));
    }

    public void goToSpellCreator(ActionEvent actionEvent) {
        playMedia(VoiceEffects.CLICK);
        showNodeAssPopUp(getNode("SpellCreator"));
    }
}

