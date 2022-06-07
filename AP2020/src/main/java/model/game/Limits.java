package model.game;

import model.card.Card;
import model.card.monster.Monster;
import model.card.monster.MonsterType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

public class Limits {

    int atkAddition = 0;
    int defAddition = 0;
    Hashtable<Card, Integer> atkBounders = new Hashtable<>();
    ArrayList<EffectLimitations> limitations = new ArrayList<>();
    HashMap<Integer, Integer> spellUsageLimit = new HashMap<>();
    HashMap<MonsterType, Integer> fieldZoneATKAddition = new HashMap<>();
    HashMap<MonsterType, Integer> fieldZoneDEFAddition = new HashMap<>();
    HashMap<Card, Card> equipMonster = new HashMap<>();
    HashMap<Card, Integer> equipGadgetATKAddition = new HashMap<>();
    HashMap<Card, Integer> equipGadgetDEFAddition = new HashMap<>();
    HashSet<Integer> cantAttackCells = new HashSet<>();
    ArrayList<Card> monstersWeDontHaveControl = new ArrayList<>();


    public void removeCardLimitOnATKBound(Card card) {
        atkBounders.remove(card);
    }

    public void addCardLimitOnATKBound(Card card, int bound) {
        if (!atkBounders.containsKey(card)) atkBounders.put(card, bound);
    }

    public Hashtable<Card, Integer> getAtkBounders() {
        return atkBounders;
    }

    public void equipMonsterToCard(Card spell, Card monster) {
        equipMonster.put(spell, monster);
    }

    public void unEquipMonster(Card spell) {
        equipMonster.remove(spell);
        equipGadgetATKAddition.remove(spell);
        equipGadgetDEFAddition.remove(spell);
    }

    public void equipGadgetATKAddition(Card spell, int amount) {
        equipGadgetATKAddition.put(spell, amount);
    }

    public void equipGadgetDEFAddition(Card spell, int amount) {
        equipGadgetDEFAddition.put(spell, amount);
    }


    public void loseControlOfMonster(Card card) {
        monstersWeDontHaveControl.add(card);
    }

    public boolean hasControlOnMonster(Card card) {
        return !monstersWeDontHaveControl.contains(card);
    }

    public void getControlBackOnMonster(Card card) {
        monstersWeDontHaveControl.remove(card);
    }

    public void addLimit(EffectLimitations limitation) {
        limitations.add(limitation);
    }

    public void removeLimit(EffectLimitations limitation) {
        limitations.remove(limitation);
    }

    public void addFieldZoneATK(MonsterType cardType, int amount) {
        fieldZoneATKAddition.put(cardType, amount);
    }

    public void addFieldZoneDEF(MonsterType cardType, int amount) {
        fieldZoneDEFAddition.put(cardType, amount);
    }

    public void removeFieldZoneATK(MonsterType cardType) {
        fieldZoneATKAddition.remove(cardType);
    }

    public void removeFieldZoneDEF(MonsterType cardType) {
        fieldZoneDEFAddition.remove(cardType);
    }

    public int getATKAddition(Card card) {
        int addition = 0;
        addition += atkAddition;
        model.card.monster.MonsterType monsterType = ((Monster) card).getMonsterType();
        if (fieldZoneATKAddition.containsKey(monsterType)) {
            addition += fieldZoneATKAddition.get(monsterType);
        }
        ArrayList<Card> spellsThatEquipped = getSpellsThatEquipped(card);
        for (Card spell : spellsThatEquipped) {
            addition += equipGadgetATKAddition.get(spell);
        }

        return addition;
    }

    public int getDEFAddition(Card card) {
        int addition = 0;
        addition += defAddition;
        model.card.monster.MonsterType monsterType = ((Monster) card).getMonsterType();
        if (fieldZoneDEFAddition.containsKey(monsterType)) {
            addition += fieldZoneDEFAddition.get(monsterType);
        }
        ArrayList<Card> spellsThatEquipped = getSpellsThatEquipped(card);
        for (Card spell : spellsThatEquipped) {
            addition += equipGadgetDEFAddition.get(spell);
        }
        return addition;
    }

    public ArrayList<Card> getSpellsThatEquipped(Card card) {
        ArrayList<Card> spellsThatEquipped = new ArrayList<>();
        for (Card spell : equipMonster.keySet()) {
            if (equipMonster.get(spell).equals(card)) spellsThatEquipped.add(spell);
        }
        return spellsThatEquipped;
    }

    public boolean doesItContainsLimit(EffectLimitations limitation) {
        return limitations.contains(limitation);
    }

    public void increaseATKAddition(int amount) {
        atkAddition += amount;
    }

    public void decreaseATKAddition(int amount) {
        atkAddition -= amount;
    }

    public void increaseDEFAddition(int amount) {
        defAddition += amount;
    }

    public void decreaseDEFAddition(int amount) {
        defAddition -= amount;
    }

    public boolean canAttackCell(int cellNumber) {
        return !cantAttackCells.contains(cellNumber);
    }

    public void banAttackingToCell(int cellNumber) {
        cantAttackCells.add(cellNumber);
    }

    public void unbanAttackingToCell(int cellNumber) {
        cantAttackCells.remove(cellNumber);
    }


    public int getAttackBound() {
        int max = 0;
        for (Integer integer : atkBounders.values()) if (integer > max) max = integer;
        return max;
    }

    public boolean canAttackByThisLimitations(Card card) {
        if (limitations.contains(EffectLimitations.CANT_ATTACK) || !hasControlOnMonster(card)) return false;
        else {
            int attackBound = getAttackBound();
            if (attackBound == 0) return true;
            else {

                return ((Monster) card).getAttack() + getATKAddition(card) >= attackBound;
            }
        }
    }

    public ArrayList<EffectLimitations> getLimitations() {
        return limitations;
    }

    public HashMap<Card, Card> getEquipMonster() {
        return equipMonster;
    }

    public ArrayList<Card> getMonstersWeDontHaveControl() {
        return monstersWeDontHaveControl;
    }

    public HashMap<Card, Integer> getEquipGadgetATKAddition() {
        return equipGadgetATKAddition;
    }

    public HashMap<Card, Integer> getEquipGadgetDEFAddition() {
        return equipGadgetDEFAddition;
    }

    public int getAtkAddition() {
        return atkAddition;
    }

    public HashMap<Integer, Integer> getSpellUsageLimit() {
        return spellUsageLimit;
    }

    public HashMap<MonsterType, Integer> getFieldZoneATKAddition() {
        return fieldZoneATKAddition;
    }

    public HashMap<MonsterType, Integer> getFieldZoneDEFAddition() {
        return fieldZoneDEFAddition;
    }

    public HashSet<Integer> getCantAttackCells() {
        return cantAttackCells;
    }

    public int getDefAddition() {
        return defAddition;
    }

    public void setAtkAddition(int atkAddition) {
        this.atkAddition = atkAddition;
    }

    public void setDefAddition(int defAddition) {
        this.defAddition = defAddition;
    }

    public void setAtkBounders(Board oldBoard, Board newBoard, Hashtable<Card, Integer> atkBounders) {
        Hashtable<Card, Integer> temp = new Hashtable<>();
        for (Card card : atkBounders.keySet()) {
            int cellNumber = getCellNumberMonster(oldBoard, card);
            if (cellNumber == -1) {
                cellNumber = getCellNumberSpell(oldBoard, card);
                if (cellNumber != -1) temp.put(newBoard.getSpellZone(cellNumber).getCard(), atkBounders.get(card));
            } else temp.put(newBoard.getMonsterZone(cellNumber).getCard(), atkBounders.get(card));
        }
        this.atkBounders = temp;
    }

    public void setLimitations(ArrayList<EffectLimitations> effectLimitations) {
        this.limitations = new ArrayList<>(effectLimitations);;
    }

    public void setSpellUsageLimit(HashMap<Integer, Integer> spellUsageLimit) {
        HashMap<Integer, Integer> output = new HashMap<>();
        for (int i : spellUsageLimit.keySet()) output.put(i, spellUsageLimit.get(i));
        this.spellUsageLimit = output;
    }

    public void setFieldZoneATKAddition(HashMap<MonsterType, Integer> fieldZoneATKAddition) {
        HashMap<MonsterType, Integer> output = new HashMap<>();
        for (MonsterType m : fieldZoneATKAddition.keySet()) output.put(m, fieldZoneATKAddition.get(m));
        this.fieldZoneATKAddition = output;
    }

    public void setFieldZoneDEFAddition(HashMap<MonsterType, Integer> fieldZoneDEFAddition) {
        HashMap<MonsterType, Integer> output = new HashMap<>();
        for (MonsterType m : fieldZoneDEFAddition.keySet()) output.put(m, fieldZoneDEFAddition.get(m));
        this.fieldZoneDEFAddition = output;
    }

    public void setEquipMonster(Board oldBoard, Board newBoard, HashMap<Card, Card> equipMonster) {
        HashMap<Card, Card> output = new HashMap<>();
        for (Card card : equipMonster.keySet()) {
            int spellNumber = getCellNumberSpell(oldBoard, card);
            int monsterNumber = getCellNumberMonster(oldBoard, card);
            if (spellNumber == -1 || monsterNumber == -1) {
                continue;
            }
            output.put(newBoard.getSpellZone(spellNumber).getCard(), newBoard.getMonsterZone(monsterNumber).getCard());
        }
        this.equipMonster = output;
    }

    public void setEquipGadgetATKAddition(Board oldBoard, Board newBoard, HashMap<Card, Integer> equipGadgetATKAddition) {
        this.equipGadgetATKAddition = getEquipGadget(oldBoard, newBoard, equipGadgetATKAddition);
    }

    public void setEquipGadgetDEFAddition(Board oldBoard, Board newBoard, HashMap<Card, Integer> equipGadgetDEFAddition) {
        this.equipGadgetDEFAddition = getEquipGadget(oldBoard, newBoard, equipGadgetDEFAddition);
    }

    private HashMap<Card, Integer> getEquipGadget(Board oldBoard, Board newBoard, HashMap<Card, Integer> equipGadget) {
        HashMap<Card, Integer> output = new HashMap<>();
        for (Card card : equipMonster.keySet()) {
            int spellNumber = getCellNumberSpell(oldBoard, card);
            if (spellNumber == -1) continue;
            output.put(newBoard.getSpellZone(spellNumber).getCard(), equipGadget.get(card));
        }
        return output;
    }

    public void setCantAttackCells(HashSet<Integer> cantAttackCells) {
        this.cantAttackCells = new HashSet<>(cantAttackCells);
    }

    public void setMonstersWeDontHaveControl(Board oldBoard, Board newBoard, ArrayList<Card> monstersWeDontHaveControl) {
        ArrayList<Card> output = new ArrayList<>();
        for (Card card : monstersWeDontHaveControl) {
            int monsterNumber = getCellNumberMonster(oldBoard, card);
            if (monsterNumber == -1) continue;
            output.add(newBoard.getMonsterZone(monsterNumber).getCard());
        }
        this.monstersWeDontHaveControl = output;
    }

    private int getCellNumberMonster(Board board, Card card) {
        Cell[] cells = board.getMonsterZone();
        for (int i = 0; i < 5; i++) {
            if (cells[i].getCard() == card) {
                return i;
            }
        }
        return -1;
    }

    private int getCellNumberSpell(Board board, Card card) {
        Cell[] cells = board.getSpellZone();
        for (int i = 0; i < 5; i++) {
            if (cells[i].getCard() == card) {
                return i;
            }
        }
        return -1;
    }

    public Limits cloneLimits(Board oldBoard, Board newBoard) {
        Limits output = new Limits();
        output.setAtkAddition(this.atkAddition);
        output.setDefAddition(this.defAddition);
        output.setAtkBounders(oldBoard, newBoard, this.atkBounders);
        output.setLimitations(this.limitations);
        output.setSpellUsageLimit(this.spellUsageLimit);
        output.setFieldZoneATKAddition(this.fieldZoneATKAddition);
        output.setFieldZoneDEFAddition(this.fieldZoneDEFAddition);
        output.setEquipMonster(oldBoard, newBoard, this.equipMonster);
        output.setEquipGadgetATKAddition(oldBoard, newBoard, this.equipGadgetATKAddition);
        output.setEquipGadgetDEFAddition(oldBoard, newBoard, this.equipGadgetDEFAddition);
        output.setCantAttackCells(this.cantAttackCells);
        output.setMonstersWeDontHaveControl(oldBoard, newBoard, this.monstersWeDontHaveControl);
        return output;
    }
}



