package controller.database;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import model.User;
import model.card.FeatureWrapper;
import model.card.monster.Monster;
import model.card.spell_traps.Spell;
import model.card.spell_traps.Trap;
import model.deck.Deck;

import java.io.*;
import java.util.ArrayList;

public class ReadAndWriteDataBase {
    public static final String usersAddr = "src/resources/Users/";
    public static final String cardsFeaturesAddr = "src/resources/card-infos/features/";

    public static User getUser(String usersAddr) {
        File file = getUserFileByUserAddr(usersAddr);
        ObjectMapper mapper = new ObjectMapper();
        User user;
        try {
            user = mapper.readValue(file, User.class);
        } catch (IOException e) {
            return null;
        }
        ArrayList<Deck> decks = user.getDecks();
        for (Deck deck : decks) {
            deck.getMainDeck().setDeckName(deck.getDeckName());
            deck.getSideDeck().setDeckName(deck.getDeckName());
        }
        if (user.getActiveDeck() == null) return user;
        else {
            Deck activeDeck = user.getActiveDeck();
            for (Deck deck : decks) {
                if (deck.getDeckName().equals(activeDeck.getDeckName())) {
                    user.setActiveDeck(deck);
                    break;
                }
            }
            return user;
        }
    }


    public static void writeUserToUsersDirectory(User user) {
        try {
            FileWriter fileWriter = new FileWriter(usersAddr + user.getUsername() + ".json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(fileWriter, user);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("ERROR 404"); // todo remove println in controller : mir
        }
    }

    public static File getUserFileByUserAddr(String userAddr) {
        return new File(usersAddr + userAddr);
    }

    public static ArrayList<User> getAllUsers() {
        String[] userAddrs;
        ArrayList<User> users = new ArrayList<>();
        File usersDirectory = new File(usersAddr);
        userAddrs = usersDirectory.list();
        if (userAddrs == null) return users;
        for (String userAddr : userAddrs) {
            User user = ReadAndWriteDataBase.getUser(userAddr);
            if (user != null)users.add(user);
        }
        return users;
    }

    public static void updateUser(User user) {
        ReadAndWriteDataBase.writeUserToUsersDirectory(user);
    }

    public static FeatureWrapper getCardFeaturesByName(String cardName) {
        FeatureWrapper wrapper;
        Gson gson = new Gson();
        try {
            FileReader fileReader = new FileReader(cardsFeaturesAddr + cardName + ".json");
            wrapper = gson.fromJson(fileReader, FeatureWrapper.class);
        } catch (IOException e) {
            wrapper = new FeatureWrapper();
        }
        return wrapper;
    }



    public static void addMonsterToCSV(Monster monster) {
        //ToDo
    }

    public static void addSpellToCSV(Spell spell) {
        //ToDo
    }

    public static void addSpellToCSV(Trap trap) {
        //ToDo
    }


}
