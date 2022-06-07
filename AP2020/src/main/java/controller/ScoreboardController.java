package controller;

import controller.database.ReadAndWriteDataBase;
import model.User;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreboardController {

    private static ArrayList<User> sortUsers(ArrayList<User> users) {
        int numberOfUsers = users.size();
        for (int i = 0; i < numberOfUsers - 1; i++) {
            for (int j = 0; j < numberOfUsers - i - 1; j++) {
                if (users.get(j).getScore() > users.get(j + 1).getScore()) {
                    Collections.swap(users, j, j + 1);
                }
                if (users.get(j).getScore() == users.get(j + 1).getScore()) {
                    if (users.get(j).getNickname().compareToIgnoreCase(users.get(j + 1).getNickname()) < 0) {
                        Collections.swap(users, j, j + 1);
                    }
                }
            }
        }

        return users;
    }

    public static ArrayList<User> getSortedUsers() {
        return  sortUsers(ReadAndWriteDataBase.getAllUsers());
    }

    public static String getScoreBoard(ArrayList<User> users) {

//        ArrayList<User> sortedUsers = sortUsers(users);
//        AsciiTable asciiTable = new AsciiTable();
//        asciiTable.addRule();
//        asciiTable.addRow("Rank", "Username", "Nickname", "Score");
//        asciiTable.addRule();
//        int i = 1;
//        for (int j = sortedUsers.size(); j > 0; j--) {
//            if (j != sortedUsers.size()) {
//                if (sortedUsers.get(j - 1).getScore() != sortedUsers.get(j).getScore()) {
//                    i = sortedUsers.size() - j + 1;
//                }
//            }
//            asciiTable.addRow(i,
//                    sortedUsers.get(j - 1).getUsername(),
//                    sortedUsers.get(j - 1).getNickname(),
//                    sortedUsers.get(j - 1).getScore());
//            asciiTable.addRule();
//        }
//        return asciiTable.render();
        return null;
    }
}