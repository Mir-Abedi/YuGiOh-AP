package model.game;

import model.card.Card;
import model.card.monster.Monster;
import model.deck.Graveyard;

public class Board {
    private Cell[] monsterZone = new Cell[5];
    private Cell[] spellZone = new Cell[5];
    private Cell fieldZone = new Cell();
    private Graveyard graveyard = new Graveyard();

    public Board() {
        for (int i = 0; i < monsterZone.length; ++i) {
            monsterZone[i] = new Cell();
        }
        for (int i = 0; i < spellZone.length; ++i) {
            spellZone[i] = new Cell();
        }
    }

    public boolean isMonsterZoneFull() {
        for (int i = 0; i < 5; i++) {
            if (!monsterZone[i].isOccupied()) return false;
        }
        return true;
    }

    public boolean isSpellZoneFull() {
        for (int i = 0; i < 5; i++) {
            if (!spellZone[i].isOccupied()) return false;
        }
        return true;
    }

    public Cell getMonsterZone(int cellNumber) {
        return monsterZone[cellNumber];
    }

    public void addCardToMonsterZone(Card card) {
        for (int i = 0; i < 5; i++) {
            if (!monsterZone[i].isOccupied() && card.isMonster()) {
                monsterZone[i].addCard(card);
                break;
            }
        }
    }

    public Cell getMonsterZoneCellByCard(Card card) {
        for (int i = 0; i < 5; i++) {
            if (monsterZone[i].getCard() == card) return monsterZone[i];
        }
        return null;
    }

    public Cell getSpellZoneCellByCard(Card card) {
        for (int i = 0; i < 5; i++) {
            if (spellZone[i].isOccupied() && spellZone[i].getCard().equals(card)) return spellZone[i];
        }
        return null;
    }

    public void addCardToSpellZone(Card card) {
        for (int i = 0; i < 5; i++) {
            if (!spellZone[i].isOccupied() && (card.isSpell() || card.isTrap())) {
                spellZone[i].addCard(card);
                break;
            }
        }
    }

    public void sendToGraveYard(Card card) {
        graveyard.addCard(card);
    }


    public Card removeCardFromMonsterZone(Card card) {
        for (int i = 0; i < 5; i++) {
            if (monsterZone[i].getCard() != card) {
                continue;
            }
            sendToGraveYard(monsterZone[i].getCard());
            return monsterZone[i].removeCard();
        }
        return null;
    }

    public Card removeCardFromSpellZone(Card card) {
        for (int i = 0; i < 5; i++) {
            if (spellZone[i].getCard() != card) {
                continue;
            }
            sendToGraveYard(spellZone[i].getCard());
            return spellZone[i].removeCard();
        }
        return null;
    }

    public Card removeCardFromGraveYard(Card card) {
        return graveyard.removeCard(card.getCardName());
    }

    public void addCardToFieldZone(Card card) {
        if (fieldZone.isOccupied()) removeCardFromFieldZone(fieldZone.getCard());
        fieldZone.addCard(card);
    }

    public Card removeCardFromFieldZone(Card card) {
        if (fieldZone.getCard() == card) {
            sendToGraveYard(fieldZone.getCard());
            return fieldZone.removeCard();
        }
        return null;
    }

    public int getSumLevel(int[] cellNumbers) {
        int sumLevel = 0;
        for (int i : cellNumbers) {
            if (monsterZone[i - 1].isOccupied()) {
                sumLevel += ((Monster) monsterZone[i - 1].getCard()).getLevel();
            }
        }
        return sumLevel;
    }

    public Cell[] getMonsterZone() {
        return monsterZone;
    }

    public int getNumberOfMonstersInMonsterZone() {
        int count = 0;
        for (Cell cell : monsterZone) {
            if (cell.isOccupied()) count++;
        }
        return count;
    }

    public Cell[] getSpellZone() {
        return spellZone;
    }

    public Graveyard getGraveyard() {
        return this.graveyard;
    }

    public Cell getFieldZone() {
        return fieldZone;
    }

    public Cell getSpellZone(int spellZoneNumber) {
        return spellZone[spellZoneNumber];
    }

    @Override
    public Board clone() {
        Board outputBoard = new Board();
        outputBoard.setMonsterZone(cloneCells(this.getMonsterZone()));
        outputBoard.setSpellZone(cloneCells(this.getSpellZone()));
        outputBoard.setGraveyard(this.graveyard.clone());
        outputBoard.setFieldZone(this.fieldZone.clone());
        return outputBoard;
    }

    private static Cell[] cloneCells(Cell[] cells) {
        Cell[] outputCells = new Cell[5];
        for (int i = 0; i < 5; i++) {
            outputCells[i] = new Cell();
            if (cells[i].isOccupied()) {
                outputCells[i] = cells[i].clone();
            }
        }
        return outputCells;
    }

    public void setMonsterZone(Cell[] monsterZone) {
        this.monsterZone = monsterZone;
    }

    public void setSpellZone(Cell[] spellZone) {
        this.spellZone = spellZone;
    }

    public void setFieldZone(Cell fieldZone) {
        this.fieldZone = fieldZone;
    }

    public void setGraveyard(Graveyard graveyard) {
        this.graveyard = graveyard;
    }
}
