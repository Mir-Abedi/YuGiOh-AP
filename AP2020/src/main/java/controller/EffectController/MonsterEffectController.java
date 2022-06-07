package controller.EffectController;

import controller.GameMenuController;
import model.card.Card;
import model.card.CardFeatures;
import model.card.monster.Monster;
import model.card.monster.MonsterType;
import model.deck.Deck;
import model.deck.Graveyard;
import model.exceptions.GameException;
import model.exceptions.WinnerException;
import model.game.*;
import view.CardEffectsView;
import view.TributeMenu;
import view.graphics.duelgraphics.TributeMenuGraphical;
import view.responses.CardEffectsResponses;
import view.responses.HowToSummon;

import java.util.ArrayList;

public class MonsterEffectController extends EffectController {

    public static void CommandKnight(Game game, Card card) {
        Limits limits = getLimits(game,card);
        limits.increaseATKAddition(400);
        int cellNumber = getCellNumberOfMonster(game, card);
        limits = getRivalsLimits(game, card);
        limits.banAttackingToCell(cellNumber);
    }

    public static void ManEaterBug(Game game, Card card) {
        Board board = getRivalBoard(game,card);
        if (board.getNumberOfMonstersInMonsterZone() == 0) {
            CardEffectsView.respond(CardEffectsResponses.NO_MONSTERS);
            return;
        }
        while (true) {
            int cellNumber = CardEffectsView.getCellNumber() - 1;
            if (!isCellNumberValid(cellNumber)) CardEffectsView.respond(CardEffectsResponses.INVALID_CELL_NUMBER);
            else {
                Cell cell = board.getMonsterZone(cellNumber);
                if (cell.isOccupied()) {
                    board.removeCardFromMonsterZone(cell.getCard());
                    return;
                } else CardEffectsView.respond(CardEffectsResponses.INVALID_CELL_NUMBER);
            }
        }
    }

    public static void GateGuardian(Game game, Card card) throws GameException {
        Board board;
        if (doesCardBelongsToPlayer(game, card)) board = game.getPlayerBoard();
        else board = game.getRivalBoard();
        if (board.getNumberOfMonstersInMonsterZone() < 3) CardEffectsView.respond(CardEffectsResponses.NO_MONSTERS);
        else {
            while (true) {
                int[] cellNumbers = new TributeMenuGraphical(game).run(3);
                if (cellNumbers == null) return;
                for (int i = 0; i < 3; i++) cellNumbers[i]--;
                if (cellNumbers[0] != cellNumbers[1] &&
                        cellNumbers[0] != cellNumbers[2] &&
                        cellNumbers[1] != cellNumbers[2]) {
                    Cell[] cells = new Cell[3];
                    cells[0] = board.getMonsterZone(cellNumbers[0]);
                    cells[1] = board.getMonsterZone(cellNumbers[1]);
                    cells[2] = board.getMonsterZone(cellNumbers[2]);
                    if (cells[0].isOccupied() && cells[1].isOccupied() && cells[2].isOccupied()) {

                        GameMenuController.tribute(game, cellNumbers);
                        setMonster(game, card, State.FACE_UP_ATTACK);
                        break;
                    } else CardEffectsView.respond(CardEffectsResponses.INVALID_CELL_NUMBER);
                } else CardEffectsView.respond(CardEffectsResponses.INVALID_CELL_NUMBER);
            }
        }
    }

    public static void Scanner(Game game, Card card) throws GameException {
        Board board;

        if (!card.getCardName().equals("Scanner")) {
            card.destroy(game);
            for (CardFeatures feature : card.getFeatures()) {
                if (feature == CardFeatures.SCANNER || feature == CardFeatures.NORMAL_SUMMON) ;
                else card.getFeatures().remove(feature);
            }
        }

        if (doesCardBelongsToPlayer(game, card)) board = game.getRivalBoard();
        else board = game.getPlayerBoard();

        Graveyard graveyard = board.getGraveyard();
        if (graveyard.getCards().size() == 0) return;
        Card card1;

        while (true) {
            card1 = CardEffectsView.getCardFromGraveyard(graveyard);
            if (card1 == null) CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_MONSTER);
            else if (!(card1 instanceof Monster)) CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_MONSTER);
            else break;
        }

        Monster monster = (Monster) card;
        Monster monster1 = (Monster) card1;
        duplicateMonster(monster, monster1);
        if (monster.getFeatures().contains(CardFeatures.VARIABLE_ATK_DEF_NUMBERS)) {
            try {
                monster.activeEffect(game);
            } catch (GameException e) {
                if (e instanceof WinnerException) throw e;
            }
        }
    }

    public static void Marshmallon(Game game, Card card) throws WinnerException {
        if (doesCardBelongsToPlayer(game, card)) {
            game.decreaseRivalHealth(1000);
        } else game.decreaseHealth(1000);
    }

    public static void BeastKingBarbaros(Game game, Card card) throws GameException {
        ArrayList<Card> hand = getCardsInHand(game,card);
        Board board = getBoard(game, card);
        if (board.isMonsterZoneFull()) {
            CardEffectsView.respond(CardEffectsResponses.MONSTER_ZONE_IS_FULL);
            return;
        }
        while (true) {
            HowToSummon howToSummon = CardEffectsView.howToSpecialNormalSummon();
            if (howToSummon == HowToSummon.SPECIAL_NORMAL_TYPE1) {
                ((Monster) card).setAttack(1900);
                State state = CardEffectsView.getStateOfSummon();
                for (int i = 0; i < hand.size(); ++i) {
                    if (hand.get(i).equals(card)) {
                        setMonster(game, card, state);
                        return;
                    }
                }
            } else if (howToSummon == HowToSummon.SPECIAL_NORMAL_TYPE2) {
                main:
                while (true) {
                    int[] cellNumbers = CardEffectsView.getCellNumbers(3);
                    Cell[] cell = game.getPlayerBoard().getMonsterZone();
                    cellNumbers[0] -= 1;
                    cellNumbers[1] -= 1;
                    cellNumbers[2] -= 1;

                    for (int i = 0; i < 3; i++) {
                        if (!isCellNumberValid(cellNumbers[i])) {
                            CardEffectsView.respond(CardEffectsResponses.INVALID_CELL_NUMBER);
                            continue main;
                        }
                    }
                    if (cellNumbers[0] == cellNumbers[1] || cellNumbers[1] == cellNumbers[2] || cellNumbers[0] == cellNumbers[2]) {
                        CardEffectsView.respond(CardEffectsResponses.INVALID_CELL_NUMBER);
                        continue;
                    }
                    for (int i = 0; i < 3; i++) {
                        if (!cell[cellNumbers[i]].isOccupied()) {
                            CardEffectsView.respond(CardEffectsResponses.INVALID_CELL_NUMBER);
                            continue main;
                        }
                    }
                    GameMenuController.tribute(game, cellNumbers);
                    setMonster(game, card, State.FACE_UP_ATTACK);
                    return;
                }
            } else if (howToSummon == HowToSummon.BACK) {
                return;
            }
        }

    }

    public static void Texchanger(Game game, Card card) throws GameException {
        if (CardEffectsView.doYouWantTo("do you want to summon a normal cyberse card?")) {
            Board board = getBoard(game,card);
            Deck deck = getDeck(game, card);
            ArrayList<Card> cards = getCardsInHand(game, card);

            if (board.isMonsterZoneFull()) CardEffectsView.respond(CardEffectsResponses.MONSTER_ZONE_IS_FULL);
            else if (!doesHaveCardWithType(MonsterType.CYBERSE, deck,board.getGraveyard(),cards))
                CardEffectsView.respond(CardEffectsResponses.NO_MONSTERS);
            else {
                while (true) {
                    Card card1 = CardEffectsView.getCardFrom(board.getGraveyard(),deck, cards);
                    if (card1 == null) return;
                    if (card1.isMonster()) {
                        if (((Monster) card1).getMonsterType().equals(MonsterType.CYBERSE)) {
                            Monster monster = (Monster) card1;
                            if (monster.hasEffect() || monster.isMonsterRitual())
                                CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_MONSTER);
                            else {
                                setMonster(game, monster, State.FACE_UP_ATTACK);
                                return;
                            }
                        } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_TYPE);
                    } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_MONSTER);
                }
            }
        }
    }

    public static void TheCalculator(Game game, Card card) {
        Board board = getBoard(game, card);
        int sumLevel = 0;
        for (int i = 0; i < 5; i++) {
            if (board.getMonsterZone(i).isOccupied() && board.getMonsterZone(i).isFaceUp()) {
                Monster monster = (Monster) board.getMonsterZone(i).getCard();
                sumLevel += monster.getLevel();
            }
        }
        Monster monster = (Monster) card;
        monster.setAttack(sumLevel * 300);
    }

    public static void MirageDragon(Game game, Card card) {
        Limits limits = getRivalsLimits(game,card);
        Board board = getBoard(game,card);
        if (board.getMonsterZoneCellByCard(card).isFaceUp()) {
            limits.addLimit(EffectLimitations.CANT_ACTIVATE_TRAP);
        }
    }

    public static void HeraldofCreation(Game game, Card card) {
        Graveyard graveyard = getBoard(game, card).getGraveyard();
        ArrayList<Card> cards = getCardsInHand(game, card);
        if (cards.size() == 0) {
            CardEffectsView.respond(CardEffectsResponses.HAVE_NO_CARDS);
            return;
        }
        mainLoop:
        while (true) {
            int numberOfCardInHand = CardEffectsView.getNumberOfCardInHand(cards) - 1;
            if (cards.size() < numberOfCardInHand)
                CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_NUMBER);
            else {
                Card removingCard = cards.get(numberOfCardInHand);
                while (true) {
                    Card givenCardFromGraveYard = CardEffectsView.getCardFromGraveyard(graveyard);
                    if (givenCardFromGraveYard.isMonster()) {
                        Monster monster = (Monster) givenCardFromGraveYard;
                        if (monster.getLevel() >= 7) {
                            cards.remove(removingCard);
                            graveyard.addCard(removingCard);
                            cards.add(monster);
                            graveyard.getCards().remove(monster);
                            break mainLoop;
                        } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_LEVEL_7_OR_MORE);
                    } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_MONSTER);
                }
            }
        }
    }

    public static void TerratigertheEmpoweredWarrior(Game game, Card card) throws GameException {
        Board board = getBoard(game, card);
        ArrayList<Card> cards = getCardsInHand(game, card);
        if (CardEffectsView.doYouWantTo("do you want to summon a normal monster with level 4 or less?")) {
            if (board.isMonsterZoneFull()) CardEffectsView.respond(CardEffectsResponses.MONSTER_ZONE_IS_FULL);
            else {
                while (true) {
                    int numberOfCardInHand = CardEffectsView.getNumberOfCardInHand(cards) - 1;
                    if (numberOfCardInHand < cards.size()) {
                        Card chosenCard = cards.get(numberOfCardInHand);
                        if (chosenCard != null) {
                            if (chosenCard.isMonster()) {
                                Monster monster = (Monster) chosenCard;
                                if (monster.getLevel() <= 4 && !monster.hasEffect()) {
                                    setMonster(game, monster, State.FACE_DOWN_DEFENCE);
                                    break;
                                } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_MONSTER);
                            } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_MONSTER);
                        } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_NUMBER);
                    } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_NUMBER);
                }
            }
        }
    }

    public static void TheTricky(Game game, Card card) throws GameException {
        Board board = getBoard(game, card);
        ArrayList<Card> cards = getCardsInHand(game, card);
        if (board.isMonsterZoneFull()) CardEffectsView.respond(CardEffectsResponses.MONSTER_ZONE_IS_FULL);
        else if (cards.size() == 1) CardEffectsView.respond(CardEffectsResponses.HAVE_NO_CARDS);
        else {
            while (true) {
                int numberOfCardInHand = CardEffectsView.getNumberOfCardInHand(cards) - 1;
                if (cards.size() <= numberOfCardInHand)
                    CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_NUMBER);
                else {
                    if (cards.get(numberOfCardInHand).equals(card))
                        CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_NUMBER);
                    else {
                        Card toBeRemovedCard = cards.get(numberOfCardInHand);
                        cards.remove(toBeRemovedCard);
                        board.sendToGraveYard(toBeRemovedCard);
                        setMonster(game, card, State.FACE_UP_ATTACK);
                        break;
                    }
                }
            }
        }
    }


    //helping functions

    static protected void setMonster(Game game, Card card, State state) throws GameException {
        Deck deck = getDeck(game,card);
        Deck rivalDeck = getRivalDeck(game,card);
        Board board = getBoard(game, card);
        Board rivalsBoard = getRivalBoard(game,card);
        ArrayList<Card> cards = getCardsInHand(game, card);
        ArrayList<Card> rivalCards = getRivalsCardsInHand(game,card);
        if (cards.contains(card)) cards.remove(card);
        else if (board.getGraveyard().getCards().contains(card)) board.getGraveyard().getCards().remove(card);
        else if (deck.getMainDeck().getCards().contains(card)) deck.getMainDeck().getCards().remove(card);
        else if (rivalDeck.getMainDeck().getCards().contains(card)) rivalDeck.getMainDeck().getCards().remove(card);
        else if (rivalsBoard.getGraveyard().getCards().contains(card)) rivalsBoard.getGraveyard().getCards().remove(card);
        else if (rivalCards.contains(card)) rivalCards.remove(card);

        board.addCardToMonsterZone(card);
        int cellNumber = getCellNumberOfMonster(game, card);
        board.getMonsterZone(cellNumber).setState(state);

        if (state.equals(State.FACE_UP_ATTACK)) {
            if (card.getFeatures().contains(CardFeatures.SUMMON_EFFECT) ||
                    card.getFeatures().contains(CardFeatures.SCANNER) ||
                    card.getFeatures().contains(CardFeatures.VARIABLE_ATK_DEF_NUMBERS))
                try {
                    card.activeEffect(game);
                } catch (GameException ignored) {
                    //ToDo
                }
        }
    }

    private static Deck getRivalDeck(Game game, Card card) {
        if (doesCardBelongsToPlayer(game,card)) return game.getRivalDeck();
        else return game.getPlayerDeck();
    }

    static private void duplicateMonster(Monster monster, Monster originalMonster) {
        monster.setMonsterEffectType(originalMonster.getMonsterEffectType());
        monster.setMonsterType(originalMonster.getMonsterType());
        monster.setAttack(originalMonster.getAttack());
        monster.setDefense(originalMonster.getDefense());
        monster.setLevel(originalMonster.getLevel());
        monster.setMonsterCardType(originalMonster.getMonsterCardType());
        monster.setCardName(originalMonster.getCardName());
        monster.setDescription(originalMonster.getDescription());
    }

    static private boolean doesHaveCardWithType(MonsterType type, Deck deck,Graveyard graveyard,ArrayList<Card> cards) {
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.addAll(cards);
        allCards.addAll(graveyard.getCards());
        allCards.addAll(deck.getMainDeck().getCards());
        for (Card card : allCards) {
            if (card.isMonster() && ((Monster) card).getMonsterType().equals(type)) return true;
        }
        return false;
    }

}
