package model.card.monster;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import controller.database.ReadAndWriteDataBase;
import controller.database.CSVInfoGetter;
import model.card.*;

import java.util.ArrayList;

@JsonIgnoreProperties({"description", "cardType", "cardID", "level", "attack", "permanentAttack", "permanentDefense", "defense", "monsterEffectType", "monsterType", "monsterCardType", "attribute", "numberOfTributes"})
public class Monster extends Card {
    protected int level;
    private int attack;
    private int permanentAttack;
    private int permanentDefense;
    private int defense;
    private MonsterEffectType monsterEffectType;
    private MonsterType monsterType;
    private MonsterCardType monsterCardType;
    private Attribute attribute;

    public Monster(String cardName) {
        setAttributesWithName(cardName);
    }

    public Monster() {
        features = new ArrayList<>();
    }

    public int getNumberOfTributes() {
        if (level == 5 || level == 6) return 1;
        if (level >= 7) return 2;
        return 0;
    }

    public int getLevel() {
        return this.level;
    }

    public void increaseLevel(int level) { // todo bekar nemiad fek konam
        this.level += level;
    }

    public void increaseAttack(int attack) {
        this.attack += attack;
    }

    public void decreaseAttack(int attack) {
        this.attack -= attack;
    }

    public int getAttack() {
        return this.attack;
    }

    public void resetAttack() {
        this.attack = permanentAttack;
    }

    public void increaseDefense(int defense) {
        this.defense += defense;
    }

    public void decreaseDefense(int defense) {
        this.defense -= defense;
    }

    public int getDefense() {
        return this.defense;
    }

    public void resetDefense() {
        this.defense = permanentDefense;
    }

    public MonsterType getMonsterType() {
        return this.monsterType;
    }

    public MonsterEffectType getMonsterEffectType() {
        return this.monsterEffectType;
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    @JsonIgnore
    public boolean isMonsterRitual() {
        return monsterCardType == model.card.monster.MonsterCardType.RITUAL;
    }

    public boolean hasEffect() {
        //return this.monsterEffectType != MonsterEffectType.NONE; //FIXME
        return this.monsterCardType == MonsterCardType.EFFECT;
    }

    public void setMonsterEffectType(MonsterEffectType monsterEffectType) {
        this.monsterEffectType = monsterEffectType;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String toString() {
        StringBuilder temp = new StringBuilder();
        temp.append("Name: " + cardName + "\n");
        temp.append("Level: " + level + "\n");
        temp.append("Type: " + monsterType + "\n");
        temp.append("ATK: " + attack + "\n");
        temp.append("DEF: " + defense + "\n");
        temp.append("Description: " + description);
        return temp.toString();
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setMonsterCardType(MonsterCardType monsterCardType) {
        this.monsterCardType = monsterCardType;
    }

    public void setMonsterType(MonsterType monsterType) {
        this.monsterType = monsterType;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    @JsonIgnore
    public MonsterCardType getMonsterCardType() {
        return monsterCardType;
    }

    @Override
    public void setCardName(String cardName) {
        this.cardName = cardName;
        setAttributesWithName(cardName);
    }

    public void setPermanentAttack(int permanentAttack) {
        this.permanentAttack = permanentAttack;
    }

    public void setPermanentDefense(int permanentDefense) {
        this.permanentDefense = permanentDefense;
    }



    public void setAttributesWithName(String cardName) {
        ArrayList<String> temp = CSVInfoGetter.monsterReadFromCSV(cardName);
        if (temp == null || temp.size() != 7) {
            return;
        }
        level = Integer.parseInt(temp.get(0));
        attribute = CSVInfoGetter.getAttribute(temp.get(1));
        monsterType = CSVInfoGetter.getMonsterType(temp.get(2));
        monsterCardType = CSVInfoGetter.getMonsterCardType(temp.get(3));
        attack = Integer.parseInt(temp.get(4));
        defense = Integer.parseInt(temp.get(5));
        permanentAttack = attack;
        permanentDefense = defense;
        description = temp.get(6);
        this.cardName = cardName;
        this.cardType = CardType.MONSTER;
        this.features = new ArrayList<>();
        FeatureWrapper wrapper = ReadAndWriteDataBase.getCardFeaturesByName(cardName);
        this.features.addAll(wrapper.features);
        this.syncedEffect = wrapper.getEffectMap();
    }
}
