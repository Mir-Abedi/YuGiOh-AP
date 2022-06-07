package controller;

import com.google.gson.Gson;
import controller.database.CSVInfoGetter;
import controller.database.ReadAndWriteDataBase;
import model.User;
import model.card.*;
import model.card.monster.MonsterCardType;
import model.card.monster.MonsterEffectType;
import model.card.monster.MonsterType;
import model.card.spell_traps.Limitation;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CardCreator {
    private static final String monsterFileName = System.getProperty("os.name").startsWith("Windows") ? "src\\resources\\card-infos\\Monster.csv" : "src/resources/card-infos/Monster.csv";
    private static final String spellTrapFileName = System.getProperty("os.name").startsWith("Windows") ? "src\\resources\\card-infos\\SpellTrap.csv" : "src/resources/card-infos/SpellTrap.csv";

    public static int getMonsterPrice(int attack, int defend, MonsterCardType monsterCardType) {
        double price = 0;
        if (attack < 500) price += 200;
        else if (attack < 1000) price += attack;
        else if (attack < 2000) price += attack + 400;
        else if (attack < 3000) price += 2 * attack;
        else price += 10000;

        if (defend < 500) price += 200;
        else if (defend < 1000) price += attack;
        else if (defend < 2000) price += attack + 400;
        else if (defend < 3000) price += 2 * attack;
        else price += 10000;

        if (monsterCardType == MonsterCardType.RITUAL) price *= 0.8;
        if (monsterCardType == MonsterCardType.EFFECT) price *= 1.5;

        return (int) Math.ceil(price);
    }

    public static int getSpellPrice(String syncedEffect) {
        double price = CSVInfoGetter.getPriceByCardName(syncedEffect) * 1.5;
        return (int) Math.ceil(price);
    }

    public static void createMonster(String cardName, String cardDescription, int attack, int defend, int price, MonsterCardType type, Attribute attribute, MonsterType monsterType, String syncedEffect) {
        FeatureWrapper features = new FeatureWrapper();
        features.setEffectMap("");
        features.addFeature(CardFeatures.CUSTOM_CARD);
        if (type == MonsterCardType.RITUAL) features.addFeature(CardFeatures.RITUAL_SUMMON);
        if (type == MonsterCardType.EFFECT) {
            ArrayList<CardFeatures> features1 = CSVInfoGetter.getCardByName(syncedEffect).getFeatures();
            features.addFeatures(features1);
            features.setEffectMap(syncedEffect);
        }
        if (type == MonsterCardType.NORMAL) features.addFeature(CardFeatures.NORMAL_SUMMON);

        try {
            FileWriter writer = new FileWriter(monsterFileName, true);
            writer.write("\n" + cardName + "," + (int) Math.ceil((double) price / 1000) + "," + attribute.toString() + "," + monsterType.toString() + "," + type.toString() + "," + attack + "," + defend + "," + cardDescription + "," + price);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Gson gson = new Gson();
            FileWriter fileWriter = new FileWriter(ReadAndWriteDataBase.cardsFeaturesAddr + cardName + ".json");
            gson.toJson(features, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reduceMoney(price);
        Card.getAllCards().add(CSVInfoGetter.getCardByName(cardName));
        Card.getCardNames().add(cardName);
    }

    public static void createSpellOrTrap(String cardName, String description, String syncedEffect, Limitation limitation, int price, boolean isTrap) {
        try {
            CardType type = isTrap ? CardType.TRAP : CardType.SPELL;
            FileWriter writer = new FileWriter(spellTrapFileName, true);
            writer.write("\n" + cardName + "," + type.toString() + "," + description + "," + limitation.toString() + "," + price);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FeatureWrapper features = new FeatureWrapper();
        features.addFeature(CardFeatures.CUSTOM_CARD);
        ArrayList<CardFeatures> features1 = CSVInfoGetter.getCardByName(syncedEffect).getFeatures();
        features.addFeatures(features1);
        features.setEffectMap(syncedEffect);
        writeFeatures(cardName, features);
        reduceMoney(price);
    }

    private static void writeFeatures(String cardName, FeatureWrapper features) {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(ReadAndWriteDataBase.cardsFeaturesAddr + cardName + ".json");
            gson.toJson(features, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean canCreate(int price) {
        return LoginMenuController.getCurrentUser().getBalance() >= Math.ceil((double) price / 10);
    }

    private static void reduceMoney(int price) {
        User user = LoginMenuController.getCurrentUser();
        user.decreaseBalance(price);
        ReadAndWriteDataBase.updateUser(user);
    }


}
