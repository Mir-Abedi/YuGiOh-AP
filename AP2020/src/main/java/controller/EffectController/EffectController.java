package controller.EffectController;

import model.User;
import model.deck.Deck;
import model.game.*;
import model.card.Card;

import java.util.ArrayList;

public class EffectController {
    public static boolean doesCardBelongsToPlayer(Game game, Card card) {
        Cell[] cells = game.getPlayerBoard().getMonsterZone();
        for (Cell cell : cells) {
            if (cell.isOccupied() && cell.getCard().equals(card)) return true;
        }
        cells = game.getPlayerBoard().getSpellZone();
        for (Cell cell : cells) {
            if (cell.isOccupied() && cell.getCard().equals(card)) return true;
        }
        if (game.getPlayerBoard().getGraveyard().getCards().size() != 0) {
            for (Card card1 : game.getPlayerBoard().getGraveyard().getCards()) {
                if (card1 == card) return true;
            }
        }
        if (game.getPlayerHandCards().size() != 0) {
            for (Card card1 : game.getPlayerHandCards()) {
                if (card1 == card) return true;
            }
        }
        return false;
    }

    public static boolean isCellNumberValid(int cellNumber) {
        return cellNumber >= 0 && cellNumber < 5;
    }

    public static State getStateOfCard(Game game, Card card) {
        State state;
        Board board;
        if (doesCardBelongsToPlayer(game, card)) board = game.getPlayerBoard();
        else board = game.getRivalBoard();
        for (Cell cell : board.getMonsterZone()) {
            if (cell.isOccupied() && cell.getCard().equals(card)) return cell.getState();
        }
        for (Cell cell : board.getSpellZone()) {
            if (cell.isOccupied() && cell.getCard().equals(card)) return cell.getState();
        }
        return null;
    }

    public static User getWinner(Game game, Card card) {
        if (doesCardBelongsToPlayer(game, card)) return game.getPlayer();
        else return game.getRival();
    }

    public static Board getBoard(Game game, Card card) {
        Board board;
        if (doesCardBelongsToPlayer(game, card)) board = game.getPlayerBoard();
        else board = game.getRivalBoard();
        return board;
    }
    public static Deck getDeck(Game game, Card card) {
        if (doesCardBelongsToPlayer(game,card)) return game.getPlayerDeck();
        else return game.getRivalDeck();
    }

    public static Limits getLimits(Game game,Card card) {
        if (doesCardBelongsToPlayer(game,card)) return game.getPlayerLimits();
        else return game.getRivalLimits();
    }

    public static ArrayList<Card> getCardsInHand(Game game, Card card) {
        if (doesCardBelongsToPlayer(game,card)) return game.getPlayerHandCards();
        else return game.getRivalHandCards();
    }
    public static ArrayList<Card> getRivalsCardsInHand(Game game, Card card) {
        if (doesCardBelongsToPlayer(game,card)) return game.getRivalHandCards();
        else return game.getPlayerHandCards();
    }

    public static Limits getRivalsLimits(Game game, Card card) {
        Limits limits;
        if (doesCardBelongsToPlayer(game, card)) limits = game.getRivalLimits();
        else limits = game.getPlayerLimits();
        return limits;
    }

    public static Board getRivalBoard(Game game, Card card) {
        Board rivalBoard;

        if (doesCardBelongsToPlayer(game, card)){
            rivalBoard = game.getRivalBoard();
        }
        else {
            rivalBoard = game.getPlayerBoard();
        }
        return rivalBoard;
    }

    //helping functions!
    static public int getCellNumberOfMonster(Game game, Card card) {
        Board board;
        if (doesCardBelongsToPlayer(game, card)) board = game.getPlayerBoard();
        else board = game.getRivalBoard();
        Cell[] cells = board.getMonsterZone();
        for (int i = 0; i < cells.length; i++) {
            if (cells[i].isOccupied() && cells[i].getCard().equals(card)) return i;
        }
        return 0;
    }
}
