
package model.game;

import controller.DeckMenuController;
import controller.GameMenuController;
import controller.database.CSVInfoGetter;
import model.User;
import model.card.Card;
import model.card.CardFeatures;
import model.card.monster.Monster;
import model.deck.Deck;
import model.deck.Graveyard;
import model.deck.MainDeck;
import model.exceptions.WinnerException;

import java.util.ArrayList;

public class Game {
    private User player;
    private User rival;
    private User winner;
    private Deck playerDeck;
    private Deck rivalDeck;
    private ArrayList<Card> playerHandCards;
    private ArrayList<Card> rivalHandCards;
    private int playerLP;
    private int rivalLP;
    private int roundCounter;
    private boolean canSummonCard;
    private Board playerBoard;
    private Board rivalBoard;
    private Limits playerLimits;
    private Limits rivalLimits;

    public Game() {
        rivalLP = 0;
        playerLP = 0;
        winner = null;
        playerDeck = null;
        rivalDeck = null;
        playerHandCards = null;
        rivalHandCards = null;
        playerLimits = null;
        rivalLimits = null;
        playerBoard = null;
        rivalBoard = null;
    }

    public Game(User player, User rival) throws CloneNotSupportedException {
        rivalLP = 8000;
        playerLP = 8000;
        winner = null;
        this.player = player;
        this.rival = rival;
        playerDeck = player.getActiveDeck().clone();
        playerDeck.getMainDeck().shuffle();
        rivalDeck = rival.getActiveDeck().clone();
        rivalDeck.getMainDeck().shuffle();
        playerHandCards = new ArrayList<>();
        rivalHandCards = new ArrayList<>();
        playerLimits = new Limits();
        rivalLimits = new Limits();
        playerBoard = new Board();
        rivalBoard = new Board();
        canSummonCard = true;
        for (Cell cell : playerBoard.getMonsterZone()) {
            cell.setCanAttack(false);
        }
    }

    public void changeTurn() {
        canSummonCard = true;
        roundCounter++;
        switchReferences();
        updateCellData(playerBoard);
        updateCellData(rivalBoard);
        deleteUsedEnumsFromGame();
    }

    private void updateCellData(Board board) {
        Cell[] temp = board.getMonsterZone();

        for (Cell cell : temp) {
            cell.setChangedPosition(false);
            if (cell.isOccupied()) cell.increaseRoundCounter();
            cell.setCanAttack(true);
        }

        temp = board.getSpellZone();
        for (Cell cell : temp) {
            if (cell.isOccupied()) cell.increaseRoundCounter();
        }
    }

    private void deleteUsedEnumsFromGame() {
        deleteUsedEnumsForBoard(getPlayerBoard());
        deleteUsedEnumsForBoard(getRivalBoard());
    }

    private void deleteUsedEnumsForBoard(Board board) {
        for (Cell cell : board.getMonsterZone()) {
            if (cell.isOccupied()) deleteUsedEnums(cell.getCard());
        }
        for (Cell cell : board.getSpellZone()) {
            if (cell.isOccupied()) deleteUsedEnums(cell.getCard());
        }
    }

    private void deleteUsedEnums(Card card) {
        if (!card.getFeatures().contains(CardFeatures.ONE_USE_ONLY)) {
            while (card.getFeatures().contains(CardFeatures.USED_EFFECT)) {
                card.getFeatures().remove(CardFeatures.USED_EFFECT);
            }
        }
    }

    public void switchReferences() {
        User tempUser;
        tempUser = player;
        player = rival;
        rival = tempUser;
        Board tempBoard;
        tempBoard = playerBoard;
        playerBoard = rivalBoard;
        rivalBoard = tempBoard;
        ArrayList<Card> tempCard;
        tempCard = playerHandCards;
        playerHandCards = rivalHandCards;
        rivalHandCards = tempCard;
        Deck tempDeck;
        tempDeck = playerDeck;
        playerDeck = rivalDeck;
        rivalDeck = tempDeck;
        Limits tempLimits;
        tempLimits = playerLimits;
        playerLimits = rivalLimits;
        rivalLimits = tempLimits;
        int tempLP = playerLP;
        playerLP = rivalLP;
        rivalLP = tempLP;
    }

    public void playerDrawCard() {
        MainDeck tempDeck = playerDeck.getMainDeck();
        if (tempDeck.getNumberOfAllCards() != 0)
            playerHandCards.add(tempDeck.removeCard(tempDeck.getCards().get(0).getCardName()));
    }

    public void addCardToHand(Card card) {
        playerHandCards.add(card);
    }

    public void rivalDrawCard() {
        if (rivalHasCapacityToDraw()) {
            rivalHandCards.add(rivalDeck.getMainDeck().getCards().get(0));
            rivalDeck.getMainDeck().getCards().remove(0);
        }
    }

    public boolean rivalHasCapacityToDraw() {
        return getNumberOfCardsInHandFromRival() < 6;
    }

    public int getNumberOfCardsInHandFromRival() {
        return rivalHandCards.size();
    }

    public boolean isMonsterZoneFull() {
        return playerBoard.isMonsterZoneFull();
    }

    public boolean isSpellZoneFull() {
        return playerBoard.isSpellZoneFull();
    }

    public void increaseHealth(int amount) {
        playerLP += amount;
    }

    public void decreaseHealth(int amount) throws WinnerException {
        if (playerLP - amount <= 0) {
            playerLP = 0;
            setWinner(rival);
        } else playerLP -= amount;
    }

    public void increaseRivalHealth(int amount) {
        rivalLP += amount;
    }

    public void decreaseRivalHealth(int amount) throws WinnerException {
        if (rivalLP - amount <= 0) {
            rivalLP = 0;
            setWinner(player);
        } else rivalLP -= amount;
    }

    public void directAttack(int cellNumber) throws WinnerException {
        Monster tempMonster = (Monster) playerBoard.getMonsterZone(cellNumber).getCard();
        decreaseRivalHealth(tempMonster.getAttack());
    }

    public void setWinner(User user) throws WinnerException {
        if (this.winner == null)
            this.winner = user;
        User loser;
        int winnerLP, loserLP;
        if (winner == player) {
            loser = rival;
            winnerLP = playerLP;
            loserLP = rivalLP;
        } else {
            loser = player;
            loserLP = playerLP;
            winnerLP = rivalLP;
        }
        throw new WinnerException(winner, loser, winnerLP, loserLP);
    }

    public String showTable() {
        String dis = "\t\t\t\t";
        StringBuilder table = new StringBuilder("\n\n" + dis);
        table.append(rival.getNickname()).append(" : ").append(rivalLP).append("\n").append(dis);
        ArrayList<Card> temp = rivalHandCards;
        for (Card card : temp) table.append("    c");
        table.append("\n").append(dis).append(rivalDeck.getMainDeck().getNumberOfAllCards()).append("\n\n").append(dis);
        Cell[] tempCellArray = rivalBoard.getSpellZone();
        table.append(tempCellArray[4].isOccupied() ? (tempCellArray[4].isFaceUp() ? "    O" : "    H") : "    E");
        table.append(tempCellArray[2].isOccupied() ? (tempCellArray[2].isFaceUp() ? "    O" : "    H") : "    E");
        table.append(tempCellArray[0].isOccupied() ? (tempCellArray[0].isFaceUp() ? "    O" : "    H") : "    E");
        table.append(tempCellArray[1].isOccupied() ? (tempCellArray[1].isFaceUp() ? "    O" : "    H") : "    E");
        table.append(tempCellArray[3].isOccupied() ? (tempCellArray[3].isFaceUp() ? "    O\n" : "    H\n") : "    E\n").append(dis);
        tempCellArray = rivalBoard.getMonsterZone();
        table.append(monsterStateToString(tempCellArray[4]));
        table.append(monsterStateToString(tempCellArray[2]));
        table.append(monsterStateToString(tempCellArray[0]));
        table.append(monsterStateToString(tempCellArray[1]));
        table.append(monsterStateToString(tempCellArray[3]));
        table.append("\n\n").append(dis);
        table.append(rivalBoard.getGraveyard().getNumberOfAllCards()).append("\t\t\t\t\t\t").append(rivalBoard.getFieldZone().isOccupied() ? "O\n" : "E\n");
        table.append("\n----------------------------------------------------------------\n\n").append(dis);
        table.append(playerBoard.getFieldZone().isOccupied() ? "O" : "E").append("\t\t\t\t\t\t" + playerBoard.getGraveyard().getNumberOfAllCards() + "\n\n").append(dis);
        tempCellArray = playerBoard.getMonsterZone();
        table.append(monsterStateToString(tempCellArray[3]));
        table.append(monsterStateToString(tempCellArray[1]));
        table.append(monsterStateToString(tempCellArray[0]));
        table.append(monsterStateToString(tempCellArray[2]));
        table.append(monsterStateToString(tempCellArray[4]));
        table.append("\n").append(dis);
        tempCellArray = playerBoard.getSpellZone();
        table.append(tempCellArray[3].isOccupied() ? (tempCellArray[3].isFaceUp() ? "    O" : "    H") : "    E");
        table.append(tempCellArray[1].isOccupied() ? (tempCellArray[1].isFaceUp() ? "    O" : "    H") : "    E");
        table.append(tempCellArray[0].isOccupied() ? (tempCellArray[0].isFaceUp() ? "    O" : "    H") : "    E");
        table.append(tempCellArray[2].isOccupied() ? (tempCellArray[2].isFaceUp() ? "    O" : "    H") : "    E");
        table.append(tempCellArray[4].isOccupied() ? (tempCellArray[4].isFaceUp() ? "    O\n\n" : "    H\n\n") : "    E\n\n");
        table.append(dis).append("\t\t\t\t\t\t").append(playerDeck.getMainDeck().getNumberOfAllCards()).append("\n").append(dis);
        temp = playerHandCards;
        for (Card card : temp) table.append("    c");
        table.append("\n").append(dis);
        table.append(player.getNickname()).append(" : ").append(playerLP).append("\n\n");
        return table.toString();
    }

    private String monsterStateToString(Cell cell) {
        if (!cell.isOccupied()) return "    E";
        State state = cell.getState();
        if (state == State.FACE_UP_ATTACK) return "   OO";
        else if (state == State.FACE_UP_DEFENCE) return "   DO";
        return "   DH";
    }

    public boolean canSummon() {
        return canSummonCard;
    }

    public void setCanSummonCard(boolean canSummonCard) {
        this.canSummonCard = canSummonCard;
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public Board getRivalBoard() {
        return rivalBoard;
    }

    public User getPlayer() {
        return player;
    }

    public User getRival() {
        return rival;
    }

    public int getRivalLP() {
        return rivalLP;
    }

    public int getPlayerLP() {
        return playerLP;
    }

    public ArrayList<Card> getPlayerHandCards() {
        return playerHandCards;
    }

    public ArrayList<Card> getRivalHandCards() {
        return rivalHandCards;
    }

    public Deck getPlayerDeck() {
        return playerDeck;
    }

    public Deck getRivalDeck() {
        return rivalDeck;

    }

    public Limits getPlayerLimits() {
        return playerLimits;
    }

    public Limits getRivalLimits() {
        return rivalLimits;
    }

    public int getRoundCounter() {
        return roundCounter;
    }

    @Override
    public Game clone() {
        Game outputGame;
        outputGame = new Game();
        outputGame.setPlayer(this.getPlayer());
        outputGame.setRival(this.getRival());
        outputGame.setPlayerLP(this.getPlayerLP());
        outputGame.setRivalLP(this.getRivalLP());
        try {
            outputGame.setPlayerDeck(this.getPlayerDeck().clone());
            outputGame.setRivalDeck(this.getRivalDeck().clone());
        } catch (Exception e) {
            return null;
        }
        outputGame.setPlayerHandCards(cloneArraylistOfCards(this.getPlayerHandCards()));
        outputGame.setRivalHandCards(cloneArraylistOfCards(this.getRivalHandCards()));
        outputGame.setPlayerBoard(this.getPlayerBoard().clone());
        outputGame.setRivalBoard(this.getRivalBoard().clone());
        outputGame.setRoundCounter(this.getRoundCounter());
        outputGame.setRivalLimits(this.getRivalLimits().cloneLimits(this.getRivalBoard(), outputGame.getRivalBoard()));
        outputGame.setPlayerLimits(this.getPlayerLimits().cloneLimits(this.getPlayerBoard(), outputGame.getPlayerBoard()));
        return outputGame;
    }

    private ArrayList<Card> cloneArraylistOfCards(ArrayList<Card> cards) {
        ArrayList<Card> outputCards = new ArrayList<>();
        for (Card card : cards) {
            outputCards.add(CSVInfoGetter.getCardByName(card.getCardName()));
        }
        return outputCards;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public void setRival(User rival) {
        this.rival = rival;
    }

    public void setPlayerLP(int playerLP) {
        this.playerLP = playerLP;
    }

    public void setRivalLP(int rivalLP){
        this.rivalLP = rivalLP;
    }

    public void setPlayerDeck(Deck playerDeck) {
        this.playerDeck = playerDeck;
    }

    public void setRivalDeck(Deck rivalDeck) {
        this.rivalDeck = rivalDeck;
    }

    public void setPlayerHandCards(ArrayList<Card> playerHandCards) {
        this.playerHandCards = playerHandCards;
    }

    public void setPlayerBoard(Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    public void setRivalBoard(Board rivalBoard) {
        this.rivalBoard = rivalBoard;
    }

    public void setRivalHandCards(ArrayList<Card> rivalHandCards) {
        this.rivalHandCards = rivalHandCards;
    }

    public void setRoundCounter(int roundCounter) {
        this.roundCounter = roundCounter;
    }

    public void setPlayerLimits(Limits playerLimits) {
        this.playerLimits = playerLimits;
    }

    public void setRivalLimits(Limits rivalLimits) {
        this.rivalLimits = rivalLimits;
    }
}
