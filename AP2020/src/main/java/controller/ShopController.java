package controller;

import controller.database.ReadAndWriteDataBase;
import controller.database.CSVInfoGetter;
import model.User;
import model.card.Card;
import view.responses.ShopMenuResponses;

import java.util.ArrayList;

public class ShopController {

    public static String showAllCards() {
        return null;
//        ArrayList<String> cardNames = CSVInfoGetter.getCardNames();
//        ArrayList<Card> cards = new ArrayList<>();
//        for (String cardName : cardNames)
//            cards.add(CSVInfoGetter.getCardByName(cardName));
//        AsciiTable asciiTable = new AsciiTable();
//        asciiTable.addRule();
//        asciiTable.addRow("No.","Name","Description","Price");
//        int counter = 1;
//        for (Card card : cards) {
//            asciiTable.addRow(counter,
//                    card.getCardName(),
//                    card.getDescription(),
//                    CSVInfoGetter.getPriceByCardName(card.getCardName()));
//            asciiTable.addRule();
//            counter++;
//        }
//        return asciiTable.render();
    }

    public static void addAllCards() {
        User user = LoginMenuController.getCurrentUser();
        ArrayList<String> names = CSVInfoGetter.getCardNames();
        for (String name : names) {
            user.addCard(CSVInfoGetter.getCardByName(name));
        }
        ReadAndWriteDataBase.updateUser(user);
    }

    public static ShopMenuResponses buyCard(String cardName) {
        User user = LoginMenuController.getCurrentUser();
        if (CSVInfoGetter.cardNameExists(cardName)) {
            if (user.hasEnoughBalance(CSVInfoGetter.getPriceByCardName(cardName))) {
                user.addCard(CSVInfoGetter.getCardByName(cardName));
                user.decreaseBalance(CSVInfoGetter.getPriceByCardName(cardName));
                ReadAndWriteDataBase.updateUser(user);
                return ShopMenuResponses.SUCCESSFUL;
            } else return ShopMenuResponses.USER_HAS_NOT_ENOUGH_BALANCE;
        } else if (cardName.matches("^[\\d]{1,2}$")) {
            int cardNumber = Integer.parseInt(cardName);
            if (cardNumber > 74 || cardNumber < 1) return ShopMenuResponses.INVALID_CARD_NUMBER;
            String cardNameByCardNumber = CSVInfoGetter.getCardNames().get(cardNumber - 1);
            if (user.hasEnoughBalance(CSVInfoGetter.getPriceByCardName(cardNameByCardNumber))) {
                user.addCard(CSVInfoGetter.getCardByName(cardNameByCardNumber));
                user.decreaseBalance(CSVInfoGetter.getPriceByCardName(cardNameByCardNumber));
                ReadAndWriteDataBase.updateUser(user);
                return ShopMenuResponses.SUCCESSFUL;
            } else return ShopMenuResponses.USER_HAS_NOT_ENOUGH_BALANCE;
        } else return ShopMenuResponses.INVALID_CARD_NAME;
    }

}
