import controller.LoginMenuController;
import controller.ShopController;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import view.responses.ShopMenuResponses;

public class ShopControllerTest {
    @Test
    @Order(1)
    public void allCardsTest() {
        Assertions.assertNotNull(ShopController.showAllCards());
        User testUser = new User("testDude","1234","Test");
        LoginMenuController.setCurrentUser(testUser);
        ShopController.addAllCards();
        Assertions.assertEquals(74,testUser.getCards().size());
    }
    @Test
    @Order(2)
    public void buyCardsTest() {
        User testUser = new User("TestDude","1234","Dude");
        LoginMenuController.setCurrentUser(testUser);
        testUser.increaseBalance(20000);
        Assertions.assertEquals(ShopMenuResponses.INVALID_CARD_NAME,ShopController.buyCard("ali"));
        Assertions.assertEquals(ShopMenuResponses.INVALID_CARD_NUMBER,ShopController.buyCard("99"));
        Assertions.assertEquals(ShopMenuResponses.INVALID_CARD_NAME,ShopController.buyCard("999"));
        Assertions.assertEquals(ShopMenuResponses.SUCCESSFUL,ShopController.buyCard("Gate Guardian"));
        Assertions.assertEquals(ShopMenuResponses.USER_HAS_NOT_ENOUGH_BALANCE,ShopController.buyCard("Mind Crush"));
        Assertions.assertEquals(ShopMenuResponses.USER_HAS_NOT_ENOUGH_BALANCE,ShopController.buyCard("20"));
        testUser.increaseBalance(20000);
        Assertions.assertEquals(ShopMenuResponses.SUCCESSFUL,ShopController.buyCard("20"));


    }
}
