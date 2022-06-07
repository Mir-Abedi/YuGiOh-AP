import controller.LoginMenuController;
import controller.ProfileController;
import controller.ScoreboardController;
import controller.database.ReadAndWriteDataBase;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import view.responses.ProfileMenuResponses;

import java.util.ArrayList;

public class ProfileControllerTest {
    @Test
    public void changePass() {
        LoginMenuController.login("sia","1234");
        Assertions.assertEquals(ProfileMenuResponses.PASSWORD_CHANGED_SUCCESSFULLY, ProfileController.changePassword("1234","123"));
        Assertions.assertEquals(ProfileMenuResponses.CURRENT_PASSWORD_IS_INVALID, ProfileController.changePassword("1234","123"));
        Assertions.assertEquals(ProfileMenuResponses.PLEASE_ENTER_A_NEW_PASSWORD, ProfileController.changePassword("123","123"));
        Assertions.assertEquals(ProfileMenuResponses.PASSWORD_CHANGED_SUCCESSFULLY, ProfileController.changePassword("123","1234"));
    }
    @Test
    public void changeNickName() {
        LoginMenuController.login("sia","1234");
        Assertions.assertEquals(ProfileMenuResponses.USER_WITH_NICKNAME_ALREADY_EXISTS, ProfileController.changeNickname("siasor88"));
        Assertions.assertEquals(ProfileMenuResponses.NICKNAME_CHANGED_SUCCESSFULLY, ProfileController.changeNickname("sia"));
        Assertions.assertEquals(ProfileMenuResponses.NICKNAME_CHANGED_SUCCESSFULLY, ProfileController.changeNickname("siasor88"));
    }
    @Test
    public void scoreBoardTest() {
        ArrayList<User> allUsers = ReadAndWriteDataBase.getAllUsers();
        Assertions.assertDoesNotThrow(() -> ScoreboardController.getScoreBoard(allUsers));
    }
}
