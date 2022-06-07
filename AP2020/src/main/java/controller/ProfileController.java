package controller;

import controller.database.ReadAndWriteDataBase;
import model.User;
import view.responses.ProfileMenuResponses;

public class ProfileController {
    public static void changeProfilePhoto(int numberOfProfile) {
        User user = LoginMenuController.getCurrentUser();
        if (numberOfProfile > -1 && numberOfProfile < User.NUMBER_OF_PROFILES) {
            user.setProfilePhoto(numberOfProfile);
            ReadAndWriteDataBase.updateUser(user);
        }
    }
    public static ProfileMenuResponses changePassword(String oldPassword, String newPassword) {
        User user = LoginMenuController.getCurrentUser();
        if (user.getPassword().equals(oldPassword)) {
            if (!oldPassword.equals(newPassword)) {
                user.changePassword(newPassword);
                ReadAndWriteDataBase.updateUser(user);
                return ProfileMenuResponses.PASSWORD_CHANGED_SUCCESSFULLY;
            } else return ProfileMenuResponses.PLEASE_ENTER_A_NEW_PASSWORD;
        } else return ProfileMenuResponses.CURRENT_PASSWORD_IS_INVALID;
    }

    public static ProfileMenuResponses changeNickname(String nickname){
        User user = LoginMenuController.getCurrentUser();
        if (!LoginMenuController.doesNicknameExists(nickname)){
            user.changeNickname(nickname);
            ReadAndWriteDataBase.updateUser(user);
            return ProfileMenuResponses.NICKNAME_CHANGED_SUCCESSFULLY;
        } else return ProfileMenuResponses.USER_WITH_NICKNAME_ALREADY_EXISTS;
    }
}
