import controller.LoginMenuController;
import model.User;
import org.apache.commons.logging.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import view.responses.LoginMenuResponses;

public class LoginControllerTest {

    @Test
    @Order(1)
    public void testDoesExistsFunctions() {
        Assertions.assertTrue(LoginMenuController.doesNicknameExists("siasor88"));
        Assertions.assertFalse(LoginMenuController.doesNicknameExists(String.valueOf(System.currentTimeMillis())));
        Assertions.assertFalse(LoginMenuController.doesUsernameExists(String.valueOf(System.currentTimeMillis())));
        Assertions.assertTrue(LoginMenuController.doesUsernameExists("sia"));
    }
    @Test
    @Order(2)
    public void setterGetterTest() {
        LoginMenuController.setCurrentUser((User) null);
        Assertions.assertNull(LoginMenuController.getCurrentUser());
    }

    @Test
    @Order(3)
    public void loginTest( ) {
        Assertions.assertFalse(LoginMenuController.isPasswordCorrect("skdj,fhnld.sf","1234"));
        Assertions.assertEquals(LoginMenuResponses.PASSWORD_AND_USERNAME_DIDNT_MATCH,LoginMenuController.login("sia","123kjej,fhn4"));
        Assertions.assertEquals(LoginMenuResponses.PASSWORD_AND_USERNAME_DIDNT_MATCH,LoginMenuController.login("sidsh.ksdflsd.f","123kjej,fhn4"));
        Assertions.assertEquals(LoginMenuResponses.USER_LOGIN_SUCCESSFUL,LoginMenuController.login("sia","1234"));
        Assertions.assertNotNull(LoginMenuController.getCurrentUser());
    }
    @Test
    @Order(4)
    public void createUser() {
        Assertions.assertEquals(LoginMenuResponses.USER_WITH_THIS_USERNAME_EXITS,LoginMenuController.createUser("sia","baaa","1234"));
        Assertions.assertEquals(LoginMenuResponses.USER_WITH_THIS_NICKNAME_EXITS,LoginMenuController.createUser("siadsdf.df,","siasor88","1234"));
        Assertions.assertEquals(LoginMenuResponses.USER_CREATED_SUCCESSFULLY,LoginMenuController.createUser(String.valueOf(System.currentTimeMillis()),String.valueOf(System.currentTimeMillis()),"1234"));
    }
    @Test
    @Order(5)
    public void logoutTest() {
        LoginMenuController.logout();
        Assertions.assertNull(LoginMenuController.getCurrentUser());
    }

}
