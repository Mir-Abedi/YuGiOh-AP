package controller.EffectController;

import controller.GameMenuController;
import controller.database.CSVInfoGetter;
import model.card.CardFeatures;
import model.card.monster.Monster;
import model.card.monster.MonsterType;
import model.card.spell_traps.Spell;
import model.card.spell_traps.SpellType;
import model.deck.*;
import model.exceptions.GameException;
import model.exceptions.StopAttackException;
import model.exceptions.StopEffectState;
import model.exceptions.StopSpell;
import model.game.*;
import model.card.Card;
import view.CardEffectsView;
import view.responses.CardEffectsResponses;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static controller.EffectController.MonsterEffectController.setMonster;


public class SpellEffectController extends EffectController {

    public static void MonsterReborn(Game game, Card card) throws GameException {
        Board board = getBoard(game, card);
        if (board.isMonsterZoneFull()) {
            CardEffectsView.respond(CardEffectsResponses.MONSTER_ZONE_IS_FULL);
            return;
        }
        if (getNumberOfMonstersOfGraveyard(game) == 0) {
            CardEffectsView.respond(CardEffectsResponses.NO_MONSTERS);
            return;
        }
        while (true) {
            Card chosenCard = CardEffectsView.getCardFromBothGraveyards(game.getPlayerBoard().getGraveyard(), game.getRivalBoard().getGraveyard());
            if (chosenCard == null) return;
            if (chosenCard.isMonster()) {
                setMonster(game, chosenCard, State.FACE_UP_ATTACK);
                return;
            } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_MONSTER);
        }

    }

    public static void Terraforming(Game game, Card card) throws GameException {
        ArrayList<Card> cards = getCardsInHand(game, card);
        Deck deck = getDeck(game, card);
        boolean hasFieldZoneSpell = false;
        for (Card tempCard : game.getPlayerDeck().getAllCards()) {
            if (tempCard.isSpell()) {
                Spell spell = (Spell) tempCard;
                if (spell.getSpellType().equals(SpellType.FIELD)) hasFieldZoneSpell = true;
            }
        }
        if (!hasFieldZoneSpell) CardEffectsView.respond(CardEffectsResponses.YOU_DONT_HAVE_ANY_FIELD_SPELL);
        while (hasFieldZoneSpell) {
            Card chosenCard = CardEffectsView.getCardFromDeck(game.getPlayerDeck());
            if (chosenCard.isSpell()) {
                Spell spell = (Spell) chosenCard;
                if (spell.getSpellType().equals(SpellType.FIELD)) {
                    cards.add(spell);
                    deck.getMainDeck().getCards().remove(spell);
                    break;
                } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_FIELD_SPELL);
            } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_FIELD_SPELL);
        }
    }

    public static void PotofGreed(Game game, Card card) throws GameException {
        ArrayList<Card> cardsInHands = getCardsInHand(game, card);
        Deck deck = getDeck(game, card);
        if (deck.getAllCards().size() >= 2) {
            cardsInHands.add(deck.getMainDeck().getCards().get(0));
            deck.getMainDeck().getCards().remove(0);
            cardsInHands.add(deck.getMainDeck().getCards().get(0));
            deck.getMainDeck().getCards().remove(0);
        } else game.setWinner(game.getRival());
    }

    public static void Raigeki(Game game, Card card) throws GameException {
        Board board = getRivalBoard(game, card);
        Limits limits = getRivalsLimits(game, card);
        for (Cell tempCell : board.getMonsterZone()) {
            if (tempCell.isOccupied()) {
                if (limits.hasControlOnMonster(tempCell.getCard())) board.removeCardFromMonsterZone(tempCell.getCard());
            }
        }
    }

    public static void ChangeofHeart(Game game, Card card) throws GameException {
        Limits limits = getRivalsLimits(game, card);
        Board rivalBoard = getRivalBoard(game, card);
        if (getNumberOfMonstersInMonsterZone(rivalBoard) == 0) CardEffectsView.respond(CardEffectsResponses.NO_MONSTERS);
        else {
            int monsterNumber = CardEffectsView.getCellNumber() - 1;
            if (isCellNumberValid(monsterNumber)) {
                if (rivalBoard.getMonsterZone(monsterNumber).isOccupied()) {
                    Card card1 = rivalBoard.getMonsterZone(monsterNumber).getCard();
                    limits.loseControlOfMonster(card1);
                } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_NUMBER);
            } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_NUMBER);
        }
    }

    public static void HarpiesFeatherDuster(Game game, Card card) throws GameException {
        Board board = getRivalBoard(game, card);
        for (Cell tempCell : board.getSpellZone()) {
            if (tempCell.isOccupied()) board.removeCardFromSpellZone(tempCell.getCard());
        }
    }

    public static void SwordsofRevealingLight(Game game, Card card) throws GameException {
        Limits limits = getRivalsLimits(game, card);
        Board board = getRivalBoard(game, card);
        Cell[] cells = board.getMonsterZone();
        for (int i = 0; i < cells.length; i++) {
            if (cells[i].isOccupied() && cells[i].isFaceDown()) {
                GameMenuController.rivalFlipSummon(game, i + 1);
            }
        }
        limits.addLimit(EffectLimitations.CANT_ATTACK);
    }

    public static void DarkHole(Game game, Card card) throws GameException {
        destroyAllMonsters(game);
    }

    public static void SupplySquad(Game game, Card card) throws GameException {
        Deck deck = getDeck(game, card);
        if (deck.getMainDeck().getCards().size() == 0) {
            game.setWinner(getWinner(game, card));
        } else {
            if (doesCardBelongsToPlayer(game, card)) {
                GameMenuController.draw(game);
            } else {
                GameMenuController.drawRival(game);
            }
        }
    }

    public static void SpellAbsorption(Game game, Card card) throws GameException {
        if (doesCardBelongsToPlayer(game, card)) {
            game.increaseHealth(500);
        } else {
            game.increaseRivalHealth(500);
        }
    }

    public static void Messengerofpeace(Game game, Card card) throws GameException {
        game.getPlayerLimits().addCardLimitOnATKBound(card, 1500);
        game.getRivalLimits().addCardLimitOnATKBound(card, 1500);
        if (CardEffectsView.doYouWantTo("do you want to pay 100 LP to keep this card?")) {
            if (doesCardBelongsToPlayer(game, card)) game.decreaseHealth(100);
            else game.decreaseRivalHealth(100);
        } else {
            GameMenuController.sendToGraveYard(game, card);
        }
    }

    public static void TwinTwisters(Game game, Card card) throws GameException {
        ArrayList<Card> cardsInHand = getCardsInHand(game, card);
        Board board = getBoard(game, card);
        if (cardsInHand.size() == 0) CardEffectsView.respond(CardEffectsResponses.HAVE_NO_CARDS);
        else {
            while (true) {
                int cardNumberInHand = CardEffectsView.getNumberOfCardInHand(cardsInHand) - 1;
                if (cardNumberInHand < cardsInHand.size()) {
                    Card cardToBeRemoved = cardsInHand.get(cardNumberInHand);
                    cardsInHand.remove(cardToBeRemoved);
                    board.sendToGraveYard(card);
                    if (CardEffectsView.doYouWantTo("do you want to destroy a spell card?")) {
                        for (int i = 0; i < 2; ++i) {
                            if (i == 1 && CardEffectsView.doYouWantTo("do you want to destroy another spell card?")) {
                                while (true) {
                                    Card card1 = CardEffectsView.getCardFromBothBoards(game.getPlayerBoard().getSpellZone(), game.getRivalBoard().getSpellZone());
                                    if (card1 == null) {
                                        CardEffectsView.respond(CardEffectsResponses.HAVE_NO_CARDS);
                                        return;
                                    } else {
                                        GameMenuController.sendToGraveYard(game, card1);
                                    }
                                }
                            }
                        }
                    }
                } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_NUMBER);
            }
        }
    }

    public static void Mysticalspacetyphoon(Game game, Card card) throws GameException {
        Card chosenCard;
        while (true) {
            chosenCard = CardEffectsView.getCardFromBothBoards(game.getPlayerBoard().getSpellZone(), game.getRivalBoard().getSpellZone());
            if (chosenCard == null) return;
            if (chosenCard.isSpell()) break;
            else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_AN_SPELL);
        }
        Board board = getBoard(game, card);
        board.removeCardFromSpellZone(chosenCard);
    }

    public static void Ringofdefense(Game game, Card card) throws GameException {
        //ToDo
    }

    public static void Yami(Game game, Card card) throws GameException {
        Limits playerLimits = game.getPlayerLimits();
        Limits rivalLimits = game.getRivalLimits();
        addPowerNumbersToType(playerLimits, rivalLimits, MonsterType.FAIRY, -200);
        addPowerNumbersToType(playerLimits, rivalLimits, MonsterType.FIEND, +200);
        addPowerNumbersToType(playerLimits, rivalLimits, MonsterType.SPELLCASTER, +200);
    }

    public static void Forest(Game game, Card card) throws GameException {
        Limits playerLimits = game.getPlayerLimits();
        Limits rivalLimits = game.getRivalLimits();
        addPowerNumbersToType(playerLimits, rivalLimits, MonsterType.BEAST, 200);
        addPowerNumbersToType(playerLimits, rivalLimits, MonsterType.BEAST_WARRIOR, 200);
        addPowerNumbersToType(playerLimits, rivalLimits, MonsterType.INSECT, 200);
    }

    public static void ClosedForest(Game game, Card card) throws GameException {
        Limits limits = getLimits(game, card);
        limits.addFieldZoneATK(MonsterType.BEAST, 100);
    }

    public static void Umiiruka(Game game, Card card) throws GameException {
        Limits playerLimits = game.getPlayerLimits();
        Limits rivalLimits = game.getRivalLimits();
        playerLimits.addFieldZoneATK(MonsterType.AQUA, 500);
        rivalLimits.addFieldZoneATK(MonsterType.AQUA, 500);
        playerLimits.addFieldZoneDEF(MonsterType.AQUA, -400);
        rivalLimits.addFieldZoneDEF(MonsterType.AQUA, -400);
    }

    public static void Swordofdarkdestruction(Game game, Card card) throws GameException {
        Board board = getBoard(game, card);
        Limits limits = getLimits(game, card);
        if (!isThereAnyFaceUpMonsters(board)) {
            board.removeCardFromSpellZone(card);
            CardEffectsView.respond(CardEffectsResponses.NO_MONSTERS);
            return;
        }
        while (true) {
            int cellNumber = CardEffectsView.getCellNumber() - 1;
            if (isCellNumberValid(cellNumber)) {
                Cell cell = board.getMonsterZone(cellNumber);
                if (cell.isOccupied() && cell.isFaceUp()) {
                    Monster monster = (Monster) board.getMonsterZone(cellNumber).getCard();
                    MonsterType monsterType = monster.getMonsterType();
                    if (monsterType.equals(MonsterType.FIEND) || monsterType.equals(MonsterType.SPELLCASTER)) {
                        limits.equipGadgetATKAddition(card, 400);
                        limits.equipGadgetDEFAddition(card, -200);
                    } else {
                        limits.equipGadgetATKAddition(card, 0);
                        limits.equipGadgetDEFAddition(card, 0);
                    }
                    limits.equipMonsterToCard(card, monster);
                    break;
                } else CardEffectsView.respond(CardEffectsResponses.INVALID_CELL_NUMBER);
            } else CardEffectsView.respond(CardEffectsResponses.INVALID_CELL_NUMBER);
        }
    }

    public static void BlackPendant(Game game, Card card) throws GameException {
        Board board = getBoard(game, card);
        Limits limits = getLimits(game, card);
        if (!isThereAnyFaceUpMonsters(board)) {
            board.removeCardFromSpellZone(card);
            CardEffectsView.respond(CardEffectsResponses.NO_MONSTERS);
            return;
        }
        addLimitationForEquipments(card, board, limits, 500, 0);
    }

    public static void UnitedWeStand(Game game, Card card) throws GameException {
        Board board = getBoard(game, card);
        Limits limits = getLimits(game, card);
        if (!isThereAnyFaceUpMonsters(board)) {
            board.removeCardFromSpellZone(card);
            CardEffectsView.respond(CardEffectsResponses.NO_MONSTERS);
            return;
        }

        Cell[] cells = board.getMonsterZone();
        int countFaceUpMonsters = 0;
        for (Cell cell : cells) {
            if (cell.isOccupied() && cell.isFaceUp()) countFaceUpMonsters++;
        }
        addLimitationForEquipments(card, board, limits, 800 * countFaceUpMonsters, 800 * countFaceUpMonsters);
    }

    public static void MagnumShield(Game game, Card card) throws GameException {
        Board board = getBoard(game, card);
        Limits limits = getLimits(game, card);
        if (!isThereAnyFaceUpMonsters(board) && !isThereAnyFaceUpCardWithType(board, MonsterType.WARRIOR)) {
            board.removeCardFromSpellZone(card);
            CardEffectsView.respond(CardEffectsResponses.NO_MONSTERS);
            return;
        }
        while (true) {
            int cellNumber = CardEffectsView.getCellNumber() - 1;
            if (isCellNumberValid(cellNumber)) {
                Cell cell = board.getMonsterZone(cellNumber);
                if (cell.isOccupied() && cell.isFaceUp()) {
                    Monster monster = (Monster) board.getMonsterZone(cellNumber).getCard();
                    MonsterType monsterType = monster.getMonsterType();
                    if (monsterType.equals(MonsterType.WARRIOR)) {
                        int atk = cell.isAttack() ? monster.getAttack() : 0;
                        int def = cell.isDefence() ? monster.getDefense() : 0;
                        setEquipmentInLimits(card, board, limits, atk, def, cellNumber);
                        break;
                    } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_TYPE);
                } else CardEffectsView.respond(CardEffectsResponses.INVALID_CELL_NUMBER);
            } else CardEffectsView.respond(CardEffectsResponses.INVALID_CELL_NUMBER);
        }
    }

    public static void AdvancedRitualArt(Game game, Card card) throws GameException {
        Board board = getBoard(game, card);
        ArrayList<Card> cardsInHand = getCardsInHand(game, card);
        if (canRitualSummon(game, card)) {
            CardEffectsView.respond(CardEffectsResponses.SPECIAL_SUMMON_NOW);
            main:
            while (true) {
                int cardNumber = CardEffectsView.getNumberOfCardInHand(cardsInHand) - 1;
                if (cardNumber < cardsInHand.size()) {
                    Card card1 = cardsInHand.get(cardNumber);
                    if (card1.isMonster()) {
                        Monster monster = (Monster) card1;
                        if (card1.getFeatures().contains(CardFeatures.RITUAL_SUMMON)) {
                            if (isThereAnyCombinationOfCardsThatTheyLevelEqualsTo(board, monster.getLevel())) {
                                int[] cellNumbers = CardEffectsView.getCellNumbers();
                                for (int cellNumber : cellNumbers) {
                                    if (!isCellNumberValid(cellNumber)) {
                                        CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_NUMBER);
                                        continue main;
                                    }
                                }
                                int sumOfLevel = board.getSumLevel(cellNumbers);
                                if (sumOfLevel == monster.getLevel()) {
                                    GameMenuController.tribute(game, cellNumbers);
                                    setMonster(game, monster, State.FACE_UP_ATTACK);
                                    return;
                                } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_NUMBER);
                            } else CardEffectsView.respond(CardEffectsResponses.CANT_RITUAL_SUMMON);
                        } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_MONSTER);
                    } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_MONSTER);
                } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_NUMBER);
            }
        } else CardEffectsView.respond(CardEffectsResponses.CANT_RITUAL_SUMMON);
        cantRitualSummon(game, card, board);
    }


    //traps!
    public static void MagicCylinder(Game game, Card card) throws GameException {
        throw new StopAttackException(StopEffectState.REDUCE_FROM_ATTACKERS_LP);
    }

    public void MirrorForce(Game game, Card card) throws GameException {
        Board board = getRivalBoard(game, card);
        Cell[] monsterZone = board.getMonsterZone();
        for (Cell cell : monsterZone) {
            if (cell.isOccupied() && cell.isFaceUp()) {
                Card cardToBeDestroyed = cell.getCard();
                GameMenuController.sendToGraveYard(game, cardToBeDestroyed);
            }
        }
        throw new StopAttackException(StopEffectState.JUST_STOP_ATTACK);
    }

    public static void MindCrush(Game game, Card card) throws GameException {
        Board board = getBoard(game, card);
        Board opponentsBoard = getRivalBoard(game, card);
        ArrayList<Card> cards = getCardsInHand(game, card);
        ArrayList<Card> opponentsCards = getRivalsCardsInHand(game, card);

        String cardName = CardEffectsView.getCardName();
        if (CSVInfoGetter.cardNameExists(cardName) && doWeHaveACardWithNameInHand(cardName, opponentsCards)) {
            for (Card card1 : opponentsCards) {
                if (card1.getCardName().equals(cardName)) {
                    opponentsBoard.sendToGraveYard(card1);
                    opponentsCards.remove(card1);
                }
            }
        } else removeARandomCardFromHand(board, cards);
    }

    public static void TrapHole(Game game, Card card) throws GameException {
        throw new StopSpell(StopEffectState.STOP_SUMMON);
    }

    public static void TorrentialTribute(Game game, Card card) throws GameException {
        destroyAllMonsters(game);
        throw new StopAttackException(StopEffectState.STOP_SUMMON);
    }

    public static void TimeSeal(Game game, Card card) throws GameException {
        Limits limits = getRivalsLimits(game, card);
        limits.addLimit(EffectLimitations.HAS_NO_DRAW_PHASE);
    }

    public static void NegateAttack(Game game, Card card) throws GameException {
        throw new StopAttackException(StopEffectState.END_BATTLE_PHASE);
    }

    public static void SolemnWarning(Game game, Card card) throws GameException {
        if (doesCardBelongsToPlayer(game, card)) game.decreaseHealth(2000);
        else game.decreaseRivalHealth(2000);
        throw new StopSpell(StopEffectState.DESTROY_SPELL);
    }

    public static void MagicJammer(Game game, Card card) throws GameException {
        ArrayList<Card> cards = getCardsInHand(game, card);
        if (cards.size() == 0) CardEffectsView.respond(CardEffectsResponses.HAVE_NO_CARDS);
        else {
            while (true) {
                int cardNumber = CardEffectsView.getNumberOfCardInHand(cards) - 1;
                if (cardNumber < cards.size()) {
                    Board board = getBoard(game, card);
                    removeCardFromHand(board, cards, cardNumber);
                    throw new StopSpell(StopEffectState.DESTROY_SPELL);
                    //ToDo
                } else CardEffectsView.respond(CardEffectsResponses.PLEASE_SELECT_A_VALID_NUMBER);
            }
        }
    }

    public static void CallofTheHaunted(Game game, Card card) throws GameException {
        Board board = getBoard(game, card);
        Graveyard graveyard = board.getGraveyard();
        Limits limits = getLimits(game, card);
        if (board.isMonsterZoneFull()) CardEffectsView.respond(CardEffectsResponses.MONSTER_ZONE_IS_FULL);
        else if (isThereAnyMonsters(graveyard)) CardEffectsView.respond(CardEffectsResponses.NO_MONSTERS);
        else {
            while (true) {
                Card card1 = CardEffectsView.getCardFromGraveyard(graveyard);
                if (card1.isMonster()) {
                    setMonster(game, card1, State.FACE_UP_ATTACK);
                    int cellNumber = EffectController.getCellNumberOfMonster(game, card1);
                    setEquipmentInLimits(card, board, limits, 0, 0, cellNumber);
                    break;
                } else CardEffectsView.respond(CardEffectsResponses.NO_MONSTERS);
            }
        }
    }


    //helping functions!

    private static boolean isThereAnyMonsters(PrimaryDeck deck) {
        for (Card card : deck.getCards()) {
            if (card.isMonster()) return true;
        }
        return false;
    }

    private static void addPowerNumbersToType(Limits playerLimits, Limits rivalLimits, MonsterType type, int amount) {
        playerLimits.addFieldZoneATK(type, amount);
        playerLimits.addFieldZoneDEF(type, amount);
        rivalLimits.addFieldZoneATK(type, amount);
        rivalLimits.addFieldZoneDEF(type, amount);
    }

    public static int getCellNumberOfSpell(Game game, Card card) {
        Board board;
        board = getBoard(game, card);
        Cell[] cells = board.getSpellZone();
        for (int i = 0; i < cells.length; i++) {
            if (cells[i].getCard().equals(card)) return i;
        }
        return 0;
    }

    private static void addLimitationForEquipments(Card card, Board board, Limits limits, int atk, int def) {
        while (true) {
            int cellNumber = CardEffectsView.getCellNumber() - 1;
            if (isCellNumberValid(cellNumber)) {
                Cell cell = board.getMonsterZone(cellNumber);
                if (cell.isOccupied() && cell.isFaceUp()) {
                    setEquipmentInLimits(card, board, limits, atk, def, cellNumber);
                    break;
                } else CardEffectsView.respond(CardEffectsResponses.INVALID_CELL_NUMBER);
            } else CardEffectsView.respond(CardEffectsResponses.INVALID_CELL_NUMBER);
        }
    }

    private static void setEquipmentInLimits(Card card, Board board, Limits limits, int atk, int def, int cellNumber) {
        Monster monster = (Monster) board.getMonsterZone(cellNumber).getCard();
        limits.equipGadgetATKAddition(card, atk);
        limits.equipGadgetDEFAddition(card, def);
        limits.equipMonsterToCard(card, monster);
    }

    private static boolean isThereAnyFaceUpMonsters(Board board) {
        Cell[] cells = board.getMonsterZone();
        for (Cell cell : cells) {
            if (cell.isOccupied() && cell.isOccupied()) return true;
        }
        return false;
    }

    private static boolean isThereAnyFaceUpCardWithType(Board board, MonsterType monsterType) {
        Cell[] cells = board.getMonsterZone();
        for (Cell cell : cells) {
            if (cell.isOccupied() && cell.isFaceUp() && ((Monster) cell.getCard()).getMonsterType().equals(monsterType))
                return true;
        }
        return false;
    }

    private static void cantRitualSummon(Game game, Card card, Board board) {
        CardEffectsView.respond(CardEffectsResponses.CANT_RITUAL_SUMMON);
        int cellNumberSpell = getCellNumberOfSpell(game, card);
        board.getSpellZone(cellNumberSpell).setState(State.FACE_DOWN_SPELL);
    }

    private static boolean isThereAnyCombinationOfCardsThatTheyLevelEqualsTo(Board board, int level) {
        if (level < 0) return false;
        if (level == 0) return true;
        ArrayList<Integer> levels = new ArrayList<>();
        for (Cell cell : board.getMonsterZone()) {
            if (cell.isOccupied() && cell.isFaceUp()) levels.add(((Monster) cell.getCard()).getLevel());
        }
        return haveAnySubSequenceWithSum(levels, level);
    }

    private static boolean haveAnySubSequenceWithSum(ArrayList<Integer> integers, int sum) {
        if (sum < 0) return false;
        if (sum == 0) return true;
        if (integers.size() == 0) return false;
        int temp /* :) */ = integers.get(0);
        integers.remove(0);
        return haveAnySubSequenceWithSum(integers, sum - temp) || haveAnySubSequenceWithSum(integers, sum);
    }

    private static ArrayList<Integer> getLevesOfRitualMonstersInHand(ArrayList<Card> cardsInHand) {
        ArrayList<Integer> levelsOfRitualMonsters = new ArrayList<>();
        for (Card card1 : cardsInHand) {
            if (card1.getFeatures().contains(CardFeatures.RITUAL_SUMMON))
                levelsOfRitualMonsters.add(((Monster) card1).getLevel());
        }
        return levelsOfRitualMonsters;
    }

    private static boolean canRitualSummon(Game game, Card card) {
        Board board = getBoard(game, card);
        ArrayList<Card> cardsInHand = getCardsInHand(game, card);
        ArrayList<Integer> levelsOfRitualMonsters = getLevesOfRitualMonstersInHand(cardsInHand);
        if (board.isMonsterZoneFull()) {
            return false;
        }
        if (levelsOfRitualMonsters.size() == 0) {
            return false;
        }
        boolean canSummon = false;
        for (Integer level : levelsOfRitualMonsters) {
            if (isThereAnyCombinationOfCardsThatTheyLevelEqualsTo(board, level)) {
                canSummon = true;
            }
        }
        return canSummon;
    }

    private static boolean doWeHaveACardWithNameInHand(String cardName, ArrayList<Card> cards) {
        for (Card card : cards) {
            if (card.getCardName().equals(cardName)) return true;
        }
        return false;
    }

    private static void removeARandomCardFromHand(Board board, ArrayList<Card> cards) {
        int cardToBeDeleted = LocalDateTime.now().getSecond() % cards.size();
        removeCardFromHand(board, cards, cardToBeDeleted);
    }

    private static void removeCardFromHand(Board board, ArrayList<Card> cards, int cardToBeDeleted) {
        board.sendToGraveYard(cards.get(cardToBeDeleted));
        cards.remove(cardToBeDeleted);
    }

    private static void destroyAllMonsters(Game game) {
        for (Cell cell : game.getPlayerBoard().getMonsterZone()) {
            if (cell.isOccupied()) GameMenuController.sendToGraveYard(game, cell.getCard());
        }
        for (Cell cell : game.getRivalBoard().getMonsterZone()) {
            if (cell.isOccupied()) GameMenuController.sendToGraveYard(game, cell.getCard());
        }
    }

    private static int getNumberOfMonstersOfGraveyard(Game game) {
        int count = 0;
        ArrayList<Card> allCards = new ArrayList<>(game.getPlayerBoard().getGraveyard().getCards());
        allCards.addAll(game.getRivalBoard().getGraveyard().getCards());
        for (Card card : allCards) if (card.isMonster()) ++count;
        return count;
    }

    private static int getNumberOfMonstersInMonsterZone(Board board) {
        int numberOfMonsters = 0;
        for (Cell cell : board.getMonsterZone()) if (cell.isOccupied()) ++numberOfMonsters;
        return numberOfMonsters;
    }

}



