import controller.DeckMenuController;
import controller.LoginMenuController;
import controller.ShopController;
import controller.database.CSVInfoGetter;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import view.responses.DeckMenuResponses;

import java.util.ArrayList;

public class DeckMenuControllerTest {
    @Test
    @Order(1)
    @DisplayName("create and delete and activate a deck")
    public void deckTest() {
        User user = new User("testDeck", "123", "testDeck");
        LoginMenuController.setCurrentUser(user);
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL, DeckMenuController.createDeck("ali"));
        Assertions.assertEquals(DeckMenuResponses.DECK_ALREADY_EXISTS, DeckMenuController.createDeck("ali"));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL, DeckMenuController.activateDeck("ali"));
        Assertions.assertEquals(DeckMenuResponses.DECK_DOESNT_EXIST, DeckMenuController.activateDeck("alisdfsd"));
        Assertions.assertEquals("ali", user.getDecks().get(0).getDeckName());
        Assertions.assertTrue(user.doesDeckExist("ali"));
        Assertions.assertNotNull(user.getActiveDeck());
        Assertions.assertEquals(DeckMenuResponses.DECK_DOESNT_EXIST, DeckMenuController.deleteDeck("jd,fklad"));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL, DeckMenuController.deleteDeck("ali"));
        Assertions.assertNull(user.getActiveDeck());
        Assertions.assertNull(user.getDeckByName("ali"));
    }

    @Test
    @Order(2)
    public void addCardsTest() {
        User user = new User("testDeck", "123", "testDeck");
        LoginMenuController.setCurrentUser(user);
        String deckName = "test";
        DeckMenuController.createDeck(deckName);
        ShopController.addAllCards();
        ArrayList<String> cardNames = CSVInfoGetter.getCardNames();
        Assertions.assertEquals(74, cardNames.size());
        Assertions.assertEquals(DeckMenuResponses.DECK_DOESNT_EXIST,DeckMenuController.addCardToMainDeck(deckName+ deckName,"Battle OX"));
        Assertions.assertEquals(DeckMenuResponses.DECK_DOESNT_EXIST,DeckMenuController.addCardToSideDeck(deckName+ deckName,"Battle OX"));
        Assertions.assertEquals(DeckMenuResponses.CARD_DOESNT_EXIST,DeckMenuController.addCardToMainDeck(deckName,"ali"));
        Assertions.assertEquals(DeckMenuResponses.CARD_DOESNT_EXIST,DeckMenuController.addCardToSideDeck(deckName,"ali"));
        for (int i = 0; i < 60; i++) Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.addCardToMainDeck(deckName, cardNames.get(i)));
        for (int i = 60; i < 74; i++) Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.addCardToSideDeck(deckName, cardNames.get(i)));
        ShopController.addAllCards();
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.addCardToSideDeck(deckName, cardNames.get(0)));

        Assertions.assertEquals(DeckMenuResponses.MAIN_DECK_IS_FULL,DeckMenuController.addCardToMainDeck(deckName,cardNames.get(65)));
        Assertions.assertEquals(DeckMenuResponses.CARD_DOESNT_EXIST,DeckMenuController.addCardToMainDeck(deckName,cardNames.get(0)));
        Assertions.assertEquals(DeckMenuResponses.SIDE_DECK_IS_FULL,DeckMenuController.addCardToSideDeck(deckName,cardNames.get(65)));
        Assertions.assertEquals(DeckMenuResponses.CARD_DOESNT_EXIST,DeckMenuController.addCardToSideDeck(deckName,cardNames.get(0)));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.removeCardFromMainDeck(deckName,cardNames.get(0)));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.removeCardFromMainDeck(deckName,cardNames.get(1)));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.removeCardFromMainDeck(deckName,cardNames.get(2)));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.removeCardFromMainDeck(deckName,cardNames.get(3)));
        Assertions.assertEquals(DeckMenuResponses.CARD_DOESNT_EXIST,DeckMenuController.removeCardFromMainDeck(deckName,cardNames.get(0)));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.removeCardFromSideDeck(deckName,cardNames.get(0)));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.removeCardFromSideDeck(deckName,cardNames.get(61)));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.removeCardFromSideDeck(deckName,cardNames.get(62)));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.removeCardFromSideDeck(deckName,cardNames.get(63)));
        Assertions.assertEquals(DeckMenuResponses.CARD_DOESNT_EXIST,DeckMenuController.removeCardFromSideDeck(deckName,cardNames.get(0)));
        Assertions.assertEquals(DeckMenuResponses.DECK_DOESNT_EXIST,DeckMenuController.removeCardFromMainDeck(deckName + deckName,cardNames.get(1)));
        Assertions.assertEquals(DeckMenuResponses.DECK_DOESNT_EXIST,DeckMenuController.removeCardFromSideDeck(deckName + deckName,cardNames.get(1)));

        ShopController.addAllCards();
        ShopController.addAllCards();
        ShopController.addAllCards();
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.addCardToMainDeck(deckName, cardNames.get(0)));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.addCardToMainDeck(deckName, cardNames.get(0)));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.addCardToMainDeck(deckName, cardNames.get(0)));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.addCardToSideDeck(deckName, cardNames.get(1)));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.addCardToSideDeck(deckName, cardNames.get(1)));
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.addCardToSideDeck(deckName, cardNames.get(1)));

        Assertions.assertEquals(DeckMenuResponses.CANT_ADD_MORE_OF_THIS_CARD,DeckMenuController.addCardToMainDeck(deckName,cardNames.get(0)));
        Assertions.assertEquals(DeckMenuResponses.CANT_ADD_MORE_OF_THIS_CARD,DeckMenuController.addCardToSideDeck(deckName,cardNames.get(1)));
        Assertions.assertDoesNotThrow(() -> DeckMenuController.showMainDeck(deckName));
        Assertions.assertDoesNotThrow(() -> DeckMenuController.showSideDeck(deckName));
        int numberOfUserCards = user.getCards().size();
        int deckSize =  user.getDeckByName(deckName).getAllCards().size();
        Assertions.assertEquals(DeckMenuResponses.SUCCESSFUL,DeckMenuController.deleteDeck(deckName));
        Assertions.assertEquals(numberOfUserCards + deckSize, user.getCards().size());
        Assertions.assertDoesNotThrow(DeckMenuController::showAllDecks);
        Assertions.assertDoesNotThrow(() -> DeckMenuController.showMainDeck(deckName));
        Assertions.assertDoesNotThrow(() -> DeckMenuController.showSideDeck(deckName));
        Assertions.assertDoesNotThrow(DeckMenuController::showAllDecks);
        Assertions.assertDoesNotThrow(DeckMenuController::showAllCards);
    }

}
