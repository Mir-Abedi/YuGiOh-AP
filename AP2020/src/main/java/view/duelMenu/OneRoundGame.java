package view.duelMenu;

import controller.AI;
import controller.GameMenuController;
import model.User;
import model.card.Card;
import model.exceptions.*;
import model.game.Cell;
import model.game.Game;
import view.MainMenu;
import view.regexes.CheatRegex;
import view.regexes.OneRoundGameRegexes;
import view.regexes.RegexFunctions;
import view.responses.GameMenuResponse;
import view.responses.GameMenuResponsesEnum;
import view.responses.OneRoundGameResponses;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

public class OneRoundGame {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    protected Game game;
    protected final Scanner scanner;
    private AI ai = null;
    private Phase currentPhase = Phase.DRAW_PHASE;

    public OneRoundGame(User firstPlayer, User secondPlayer, Scanner scanner) {
        try {
            game = new Game(firstPlayer, secondPlayer);
        } catch (CloneNotSupportedException ignored) { }
        this.scanner = scanner;
    }

    public OneRoundGame(User currentPlayer, AI ai, Scanner scanner){
        try {
            game = new Game(currentPlayer, ai.getAI());
        }
        catch (CloneNotSupportedException ignored){ }
        this.scanner = scanner;
        this.ai = ai;
    }

    public OneRoundGame(Game game, Scanner scanner) {
        this.game = game;
        this.scanner = scanner;
    }

    public void run() throws WinnerException {
        String command;
        GameMenuController.firstDraw(game);
        while (true) {
            command = scanner.nextLine().trim();
            if (OneRoundGameRegexes.doesItSelectMyMonsterCellCommand(command))
                selectMyMonsterCell(command);
            else if (OneRoundGameRegexes.doesItSelectMySpellCellCommand(command))
                selectMySpellCell(command);
            else if (OneRoundGameRegexes.doesItSelectCardFromMyHandCommand(command))
                selectCardFromMyHand(command);
            else if (OneRoundGameRegexes.doesItSelectMyFieldZoneCommand(command))
                selectMyFieldZone();
            else if (OneRoundGameRegexes.doesItSelectOpponentFieldZoneCommand(command))
                selectOpponentFieldZone();
            else if (OneRoundGameRegexes.doesItSelectOpponentMonsterCellCommand(command))
                selectOpponentMonsterCell(command);
            else if (OneRoundGameRegexes.doesItSelectOpponentSpellCellCommand(command))
                selectOpponentSpellCell(command);
            else if (OneRoundGameRegexes.doesItSetAttackCommand(command))
                setAttack();
            else if (OneRoundGameRegexes.doesItSetDefenseCommand(command))
                setDefense();
            else if (command.matches(OneRoundGameRegexes.activeEffect))
                activeEffect();
            else if (command.matches(OneRoundGameRegexes.set))
                set();
            else if (command.matches(OneRoundGameRegexes.attackDirect))
                attackDirect();
            else if (command.matches(OneRoundGameRegexes.deselectCard))
                deselectCard(true);
            else if (command.matches(OneRoundGameRegexes.flipSummon))
                flipSummon();
            else if (command.matches(OneRoundGameRegexes.attackToOpponentMonster))
                attackToOpponentMonster(command);
            else if (OneRoundGameRegexes.showSelectedCard(command))
                showSelectedCard();
            else if (command.matches(OneRoundGameRegexes.showGraveyard))
                showGraveyard(true);
            else if (command.matches(OneRoundGameRegexes.SHOW_GRAVEYARD_RIVAL))
                showGraveyard(false);
            else if (command.matches(OneRoundGameRegexes.summon))
                summon();
            else if (command.matches(OneRoundGameRegexes.surrender))
                surrender();
            else if (command.matches(OneRoundGameRegexes.nextPhase))
                goToNextPhase();
            else if (command.equals("help"))
                showHelp();
            else if (command.matches(OneRoundGameRegexes.SHOW_TABLE))
                showTable();
            else if (command.matches(OneRoundGameRegexes.SHOW_PHASE))
                showPhase();
            else if (command.matches(OneRoundGameRegexes.showHandCards))
                showHandCards();
            else if (command.matches(CheatRegex.WIN_GAME)) winGame();
            else if (command.matches(CheatRegex.INCREASE_HEALTH))
                increaseHealth(MainMenu.getInt(command));
            else if (command.matches(CheatRegex.MIREBOZORG_CHEAT))
                mireBozorgCheat();
            else
                respond(OneRoundGameResponses.INVALID_COMMAND);
        }
    }

    public void showHandCards() {
        for (int i = 0; i < game.getPlayerHandCards().size(); i++)
            System.out.println((i + 1) + " : " + game.getPlayerHandCards().get(i).toString());
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public void goToNextPhase() throws WinnerException {
        if (currentPhase.equals(Phase.STANDBY_PHASE))
            goToMainPhase1();
        else if (currentPhase.equals(Phase.END_PHASE)) {
            goToDrawPhase();
            GameMenuResponse gameMenuResponse;
            if ((gameMenuResponse = GameMenuController.draw(game)).getGameMenuResponseEnum() == GameMenuResponsesEnum.SUCCESSFUL) {
                System.out.println("New Card added to hand : \n" + gameMenuResponse.getObj());
            }
        } else if (currentPhase.equals(Phase.DRAW_PHASE))
            goToStandByPhase();
        else if (currentPhase.equals(Phase.MAIN_PHASE2))
            goToEndPhase();
        else if (currentPhase.equals(Phase.BATTLE_PHASE))
            goToMainPhase2();
        else if (currentPhase.equals(Phase.MAIN_PHASE1))
            goToBattlePhase();
    }

    public void goToStandByPhase() throws WinnerException{
        GameMenuController.goToStandByPhase(game);
        setCurrentPhase(Phase.STANDBY_PHASE);
        respond(OneRoundGameResponses.STANDBY_PHASE);
    }

    public void goToMainPhase1() {
        setCurrentPhase(Phase.MAIN_PHASE1);
        respond(OneRoundGameResponses.MAIN_PHASE1);
        System.out.println(ANSI_BLACK + ANSI_BLUE_BACKGROUND +
                game.showTable() +
                ANSI_RESET);
    }

    public void goToDrawPhase() throws WinnerException {
        deselectCard(false);
        if (game.getPlayerHandCards().size() > 6) new EndPhaseMenu(game).run();
        System.out.println("It's " + game.getRival().getNickname() + "'s turn ..");
        game.changeTurn();
        if (ai != null){
            ai.run(game);
            System.out.println("It's " + game.getRival().getNickname() + "'s turn ..");
            game.changeTurn();
        }
        deselectCard(false);
        setCurrentPhase(Phase.DRAW_PHASE);
        respond(OneRoundGameResponses.DRAW_PHASE);
    }

    public void goToMainPhase2() {
        setCurrentPhase(Phase.MAIN_PHASE2);
        respond(OneRoundGameResponses.MAIN_PHASE2);
    }

    public void goToBattlePhase() {
        setCurrentPhase(Phase.BATTLE_PHASE);
        respond(OneRoundGameResponses.BATTLE_PHASE);
    }

    public void goToEndPhase() {
        setCurrentPhase(Phase.END_PHASE);
        respond(OneRoundGameResponses.END_PHASE);
    }

    public void surrender() throws WinnerException {
        throw new WinnerException(game.getRival(), game.getPlayer(), game.getRivalLP(), game.getPlayerLP());
    }

    public void winGame() throws WinnerException {
        throw new WinnerException(game.getPlayer(), game.getRival(), game.getPlayerLP(), game.getRivalLP());
    }

    public void increaseHealth(int LP) {
        game.increaseHealth(LP);
    }

    public void mireBozorgCheat() {
        Cell[] monsters = game.getRivalBoard().getMonsterZone();
        for (Cell cell : monsters) cell.removeCard();
    }

    public void deselectCard(boolean printNeeded) {
        if (GameMenuController.getSelectState() == null) {
            if (printNeeded) respond(OneRoundGameResponses.NO_CARD_IS_SELECTED_YET);
        } else {
            GameMenuController.setSelectState(null);
            GameMenuController.setCellNumber(-1);
            if (printNeeded) respond(OneRoundGameResponses.CARD_DESELECTED);
        }
    }

    public void summon() throws WinnerException {
        if (!canSummonInThisPhase()) respond(OneRoundGameResponses.ACTION_NOT_ALLOWED_IN_THIS_PHASE);
        else {
            SelectState selectState = GameMenuController.getSelectState();
            if (selectState == null) respond(OneRoundGameResponses.NO_CARD_IS_SELECTED_YET);
            else if (selectState != SelectState.HAND) respond(OneRoundGameResponses.YOU_CANT_SUMMON_THIS_CARD);
            else {
                GameMenuResponse gameMenuResponse;
                if (ai == null) gameMenuResponse = GameMenuController.summon(game, GameMenuController.getCellNumber(), false);
                else gameMenuResponse = GameMenuController.summon(game, GameMenuController.getCellNumber(), true);
                GameMenuResponsesEnum answer = gameMenuResponse.getGameMenuResponseEnum();
                if (answer == GameMenuResponsesEnum.ALREADY_SUMMONED)
                    respond(OneRoundGameResponses.YOU_ALREADY_SUMMONED_OR_SET_ON_THIS_TURN);
                else if (answer == GameMenuResponsesEnum.CANT_NORMAL_SUMMON)
                    respond(OneRoundGameResponses.CANT_NORMAL_SUMMON);
                else if (answer == GameMenuResponsesEnum.MONSTER_ZONE_IS_FULL)
                    respond(OneRoundGameResponses.MONSTER_CARD_ZONE_IS_FULL);
                else if (answer == GameMenuResponsesEnum.SPELL_AND_TRAP_ZONE_IS_FULL)
                    respond(OneRoundGameResponses.SPELL_CARD_ZONE_IS_FULL);
                else if (answer == GameMenuResponsesEnum.NOT_ENOUGH_MONSTERS)
                    respond(OneRoundGameResponses.THERE_ARE_NOT_ENOUGH_CARDS_FOR_TRIBUTE);
                else if (answer == GameMenuResponsesEnum.ABORTED) respond(OneRoundGameResponses.ABORTED);
                deselectCard(false);
            }
        }
    }

    private boolean canSummonInThisPhase() {
        return canChangePosition();
    }

    public void set() {
        if (!canSetInThisPhase()) respond(OneRoundGameResponses.ACTION_NOT_ALLOWED_IN_THIS_PHASE);
        else {
            SelectState selectState = GameMenuController.getSelectState();
            if (selectState == null) {
                respond(OneRoundGameResponses.NO_CARD_IS_SELECTED_YET);
            } else if (selectState != SelectState.HAND) {
                respond(OneRoundGameResponses.YOU_CANT_SET_THIS_CARD);
            } else {
                int cellNumber = GameMenuController.getCellNumber();
                Card card = game.getPlayerHandCards().get(cellNumber - 1);
                if (card.isMonster()) setMonster(cellNumber);
                else setTrapAndSpell(cellNumber);
            }
        }
    }

    private void setMonster(int cellNumber) {
        GameMenuResponse gameMenuResponse = GameMenuController.setMonsterCard(game, cellNumber);
        GameMenuResponsesEnum answer = gameMenuResponse.getGameMenuResponseEnum();
        if (answer == GameMenuResponsesEnum.INVALID_SELECTION) respond(OneRoundGameResponses.INVALID_SELECTION);
        else if (answer == GameMenuResponsesEnum.MONSTER_ZONE_IS_FULL)
            respond(OneRoundGameResponses.MONSTER_CARD_ZONE_IS_FULL);
        else if (answer == GameMenuResponsesEnum.ALREADY_SUMMONED)
            respond(OneRoundGameResponses.YOU_ALREADY_SUMMONED_OR_SET_ON_THIS_TURN);
        else if (answer == GameMenuResponsesEnum.SUCCESSFUL) {
            respond(OneRoundGameResponses.SET_SUCCESSFULLY);
            deselectCard(false);
        }
    }

    private void setTrapAndSpell(int cellNumber) {
        GameMenuResponse gameMenuResponse = GameMenuController.setSpellAndTrap(game, cellNumber);
        GameMenuResponsesEnum answer = gameMenuResponse.getGameMenuResponseEnum();
        if (answer == GameMenuResponsesEnum.INVALID_SELECTION) respond(OneRoundGameResponses.INVALID_SELECTION);
        else if (answer == GameMenuResponsesEnum.SPELL_AND_TRAP_ZONE_IS_FULL)
            respond(OneRoundGameResponses.SPELL_CARD_ZONE_IS_FULL);
        else if (answer == GameMenuResponsesEnum.SUCCESSFUL) {
            respond(OneRoundGameResponses.SET_SUCCESSFULLY);
            deselectCard(false);
        }
    }

    private boolean canSetInThisPhase() {
        return canChangePosition();
    }

    public void flipSummon() throws WinnerException {
        if (!canFlipIntThisPhase()) respond(OneRoundGameResponses.ACTION_NOT_ALLOWED_IN_THIS_PHASE);
        else {
            SelectState selectState = GameMenuController.getSelectState();
            if (selectState == null) respond(OneRoundGameResponses.NO_CARD_IS_SELECTED_YET);
            else if (selectState != SelectState.PLAYER_MONSTER) respond(OneRoundGameResponses.PLEASE_SELECT_MONSTER);
            else {
                GameMenuResponse gameMenuResponse;
                try {
                    if (ai == null) gameMenuResponse = GameMenuController.flipSummon(game, GameMenuController.getCellNumber(), false);
                    else gameMenuResponse = GameMenuController.flipSummon(game, GameMenuController.getCellNumber(), true);
                } catch (GameException e) {
                    if (e instanceof WinnerException) throw (WinnerException) e;
                    return;
                }
                GameMenuResponsesEnum answer = gameMenuResponse.getGameMenuResponseEnum();
                if (answer == GameMenuResponsesEnum.CANT_FLIP_SUMMON)
                    respond(OneRoundGameResponses.YOU_CANT_FLIP_SUMMON_THIS_CARD);
                else if (answer == GameMenuResponsesEnum.SUCCESSFUL)
                    respond(OneRoundGameResponses.FLIP_SUMMONED_SUCCESSFULLY);
            }
        }
    }

    private boolean canFlipIntThisPhase() {
        return canChangePosition();
    }

    public void attackDirect() throws WinnerException {
        if (!(currentPhase == Phase.BATTLE_PHASE)) respond(OneRoundGameResponses.ACTION_NOT_ALLOWED_IN_THIS_PHASE);
        else {
            SelectState selectState = GameMenuController.getSelectState();
            if (selectState == null) respond(OneRoundGameResponses.NO_CARD_IS_SELECTED_YET);
            else if (selectState != SelectState.PLAYER_MONSTER)
                respond(OneRoundGameResponses.YOU_CANT_ATTACK_WITH_THIS_CARD);
            else {
                GameMenuResponse gameMenuResponse = GameMenuController.directAttack(game, GameMenuController.getCellNumber());
                GameMenuResponsesEnum answer = gameMenuResponse.getGameMenuResponseEnum();
                if (answer == GameMenuResponsesEnum.CANT_ATTACK) respond(OneRoundGameResponses.CANT_ATTACK_DIRECTLY);
                else if (answer == GameMenuResponsesEnum.ALREADY_ATTACKED)
                    respond(OneRoundGameResponses.THIS_CARD_ALREADY_ATTACKED);
                else if (answer == GameMenuResponsesEnum.SUCCESSFUL) {
                    System.out.println("Opponent received " + gameMenuResponse.getObj() + " damage.");
                }
            }
        }
    }

    public void activeEffect() throws WinnerException {
        if (!canActiveEffect()) respond(OneRoundGameResponses.ACTION_NOT_ALLOWED_IN_THIS_PHASE);
        else { // action allowed
            SelectState selectState = GameMenuController.getSelectState();
            if (selectState == null) respond(OneRoundGameResponses.NO_CARD_IS_SELECTED_YET);
//            else if (selectState == SelectState.HAND) {
//                Card card = game.getPlayerHandCards().get(GameMenuController.getCellNumber() - 1);
//                if (!card.isSpell()) respond(OneRoundGameResponses.ACTIVE_EFFECT_IS_ONLY_FOR_SPELL_CARDS);
//                else { // card is in hand and is spell or trap
//                    if (card.isSpell()) {
//                        Spell tempSpell = (Spell) card;
//                        if (tempSpell.getSpellType() == SpellType.FIELD) {
//                            GameMenuController.setCardInPlayerFieldZone(game, GameMenuController.getCellNumber());
//                            respond(OneRoundGameResponses.SPELL_ACTIVATED);
//                            return;
//                        }
//                    }
//                    if (game.isSpellZoneFull()) {
//                        respond(OneRoundGameResponses.SPELL_CARD_ZONE_IS_FULL);
//                        return;
//                    }
//                    GameMenuResponse gameMenuResponse = GameMenuController.summon(game, GameMenuController.getCellNumber());
//                }
//            }
            else if (selectState != SelectState.PLAYER_SPELL)
                respond(OneRoundGameResponses.ACTIVE_EFFECT_IS_ONLY_FOR_SPELL_CARDS);
            else { // card is in spell zone
                Cell tempCell = game.getPlayerBoard().getSpellZone(GameMenuController.getCellNumber() - 1);
                Card card = tempCell.getCard();
                if (GameMenuController.hasNotUsedEffect(card.getFeatures())) {
                    try {
                        if (ai == null)
                            GameMenuController.activeEffect(game, card, game.getRival(), GameMenuController.getSpeed(card.getFeatures()));
                        else
                            GameMenuController.activeEffect(game, card, game.getRival(), 0);
                    } catch (Exception e) {
                        if (e instanceof WinnerException) throw (WinnerException) e;
                        if (e instanceof StopSpell) {
                            if (((StopSpell) e).getState() == StopEffectState.DESTROY_SPELL)
                                GameMenuController.sendToGraveYard(game,
                                        card);
                        }
                        return;
                    }
                } else respond(OneRoundGameResponses.YOU_HAVE_ALREADY_ACTIVATED_THIS_CARD);
            }
            deselectCard(false);
        }
    }

    protected boolean canActiveEffect() {
        return canChangePosition();
    }

    public void showGraveyard(boolean myGraveYard) {
        String graveYard;
        if (myGraveYard) graveYard = (String) GameMenuController.showPlayerGraveYard(game).getObj();
        else graveYard = (String) GameMenuController.showRivalGraveYard(game).getObj();
        System.out.println(graveYard);
    }

    public void showSelectedCard() {
        SelectState selectState = GameMenuController.getSelectState();
        if (selectState == null) respond(OneRoundGameResponses.NO_CARD_IS_SELECTED_YET);
        else if (selectState == SelectState.HAND)
            System.out.println(game.getPlayerHandCards().get(GameMenuController.getCellNumber() - 1).toString());
        else if (selectState == SelectState.PLAYER_FIELD)
            System.out.println(game.getPlayerBoard().getFieldZone().getCard().toString());
        else if (selectState == SelectState.PLAYER_SPELL)
            System.out.println(game.getPlayerBoard().getSpellZone(GameMenuController.getCellNumber() - 1).getCard().toString());
        else if (selectState == SelectState.PLAYER_MONSTER)
            System.out.println(game.getPlayerBoard().getMonsterZone(GameMenuController.getCellNumber() - 1).getCard().toString());
        else if (selectState == SelectState.RIVAL_FIELD)
            System.out.println(game.getRivalBoard().getFieldZone().getCard().toString());
        else if (selectState == SelectState.RIVAL_MONSTER) {
            Cell tempCell = game.getRivalBoard().getMonsterZone(GameMenuController.getCellNumber() - 1);
            if (tempCell.isFaceDown()) respond(OneRoundGameResponses.CARD_IS_NOT_VISIBLE);
            else System.out.println(tempCell.getCard().toString());
        } else if (selectState == SelectState.RIVAL_SPELL) {
            Cell tempCell = game.getRivalBoard().getSpellZone(GameMenuController.getCellNumber() - 1);
            if (tempCell.isFaceDown()) respond(OneRoundGameResponses.CARD_IS_NOT_VISIBLE);
            else System.out.println(tempCell.getCard().toString());
        }
    }

    public void selectMyMonsterCell(String command) {
        int cellNumber = getNumberFromString(command);
        GameMenuResponse gameMenuResponse = GameMenuController.selectMonsterFromPlayer(game, cellNumber);
        selectCard(gameMenuResponse, SelectState.PLAYER_MONSTER);
    }


    public void selectMySpellCell(String command) {
        int cellNumber = getNumberFromString(command);
        GameMenuResponse gameMenuResponse = GameMenuController.selectSpellAndTrapFromPlayer(game, cellNumber);
        selectCard(gameMenuResponse, SelectState.PLAYER_SPELL);
    }

    // for easier application of selectMySpell and selectMyMonster and selectCardFromHand and selectMyFieldZone
    private void selectCard(GameMenuResponse gameMenuResponse, SelectState selectState) {
        GameMenuResponsesEnum answer = gameMenuResponse.getGameMenuResponseEnum();
        if (answer == GameMenuResponsesEnum.NO_CARD_FOUND) {
            respond(OneRoundGameResponses.NO_CARD_FOUND_IN_GIVEN_POSITION);
        } else if (answer == GameMenuResponsesEnum.INVALID_SELECTION) {
            respond(OneRoundGameResponses.INVALID_SELECTION);
        } else if (answer == GameMenuResponsesEnum.SUCCESSFUL) {
            Object obj = gameMenuResponse.getObj();
            if (!(obj instanceof Cell)) {
                if (!(obj instanceof Card)) {
                    unknownError();
                    return;
                } else {
                    GameMenuController.setCellNumber(getCellNumberFromCard((Card) obj));
                }
            } else {
                GameMenuController.setCellNumber(getCellNumberFromCard(((Cell) obj).getCard()));
            }
            GameMenuController.setSelectState(selectState);
            respond(OneRoundGameResponses.CARD_SELECTED);
        } else unknownError();
    }

    private int getCellNumberFromCard(Card card) {
        Cell[] temp = game.getPlayerBoard().getMonsterZone();
        for (int i = 0; i < 5; i++) if (temp[i].getCard() == card) return i + 1;
        temp = game.getPlayerBoard().getSpellZone();
        for (int i = 0; i < 5; i++) if (temp[i].getCard() == card) return i + 1;
        temp = game.getRivalBoard().getMonsterZone();
        for (int i = 0; i < 5; i++) if (temp[i].getCard() == card) return i + 1;
        temp = game.getRivalBoard().getSpellZone();
        for (int i = 0; i < 5; i++) if (temp[i].getCard() == card) return i + 1;
        ArrayList<Card> tempHands = game.getPlayerHandCards();
        for (int i = 0; i < tempHands.size(); i++) if (tempHands.get(i) == card) return i + 1;
        return -1;
    }

    public void selectMyFieldZone() {
        GameMenuResponse gameMenuResponse = GameMenuController.selectPlayerFieldZone(game);
        selectCard(gameMenuResponse, SelectState.PLAYER_FIELD);
    }

    public void selectCardFromMyHand(String command) {
        int cardNumber = getNumberFromString(command);
        GameMenuResponse gameMenuResponse = GameMenuController.selectCardFromHand(game, cardNumber);
        selectCard(gameMenuResponse, SelectState.HAND);
    }

    public void selectOpponentMonsterCell(String command) {
        int cellNumber = getNumberFromString(command);
        GameMenuResponse gameMenuResponse = GameMenuController.selectMonsterFromRival(game, cellNumber);
        selectCard(gameMenuResponse, SelectState.RIVAL_MONSTER);
    }

    public void selectOpponentSpellCell(String command) {
        int cellNumber = getNumberFromString(command);
        GameMenuResponse gameMenuResponse = GameMenuController.selectSpellAndTrapFromRival(game, cellNumber);
        selectCard(gameMenuResponse, SelectState.RIVAL_SPELL);
    }

    public void selectOpponentFieldZone() {
        GameMenuResponse gameMenuResponse = GameMenuController.selectRivalFieldZone(game);
        selectCard(gameMenuResponse, SelectState.RIVAL_FIELD);
    }

    public void setAttack() {
        setPosition("attack");
    }

    public void setDefense() {
        setPosition("defense");
    }

    private void setPosition(String position) {
        if (!canChangePosition()) {
            respond(OneRoundGameResponses.ACTION_NOT_ALLOWED_IN_THIS_PHASE);
            return;
        }
        SelectState selectState = GameMenuController.getSelectState();
        if (selectState == null) {
            respond(OneRoundGameResponses.NO_CARD_IS_SELECTED_YET);
        } else if (selectState != SelectState.PLAYER_MONSTER) {
            respond(OneRoundGameResponses.YOU_CANT_CHANGE_THIS_CARDS_POSITION);
        } else {
            GameMenuResponse gameMenuResponse = GameMenuController.setMonsterPosition(game, GameMenuController.getCellNumber(), position);
            GameMenuResponsesEnum answer = gameMenuResponse.getGameMenuResponseEnum();
            if (answer == GameMenuResponsesEnum.INVALID_SELECTION) respond(OneRoundGameResponses.INVALID_SELECTION);
            else if (answer == GameMenuResponsesEnum.NO_CARD_FOUND)
                respond(OneRoundGameResponses.NO_CARD_FOUND_IN_GIVEN_POSITION);
            else if (answer == GameMenuResponsesEnum.YOU_HAVENT_SUMMONED_YET)
                respond(OneRoundGameResponses.YOU_CANT_CHANGE_THIS_CARDS_POSITION);
            else if (answer == GameMenuResponsesEnum.ALREADY_IN_THIS_POSITION)
                respond(OneRoundGameResponses.THIS_CARD_IS_ALREADY_IN_THE_WANTED_POSITION);
            else if (answer == GameMenuResponsesEnum.ALREADY_CHANGED)
                respond(OneRoundGameResponses.YOU_ALREADY_CHANGED_THIS_CARD_POSITION_THIS_TURN);
            else if (answer == GameMenuResponsesEnum.SUCCESSFUL) {
                respond(OneRoundGameResponses.MONSTER_CARD_POSITION_CHANGED_SUCCESSFULLY);
                deselectCard(false);
            } else if (answer == GameMenuResponsesEnum.CANT_CHANGE)
                respond(OneRoundGameResponses.YOU_CANT_CHANGE_THIS_CARDS_POSITION);
            else unknownError();
            deselectCard(false);
        }
    }

    private boolean canChangePosition() {
        ArrayList<Phase> phases = getAllowedPhaseForSummonSetChangePositionActiveEffect();
        for (Phase p : phases) if (p == currentPhase) return true;
        return false;
    }

    public void attackToOpponentMonster(String command) throws WinnerException {
        int cellNumber = getNumberFromString(command);
        if (!(currentPhase == Phase.BATTLE_PHASE)) respond(OneRoundGameResponses.ACTION_NOT_ALLOWED_IN_THIS_PHASE);
        else {
            SelectState selectState = GameMenuController.getSelectState();
            if (selectState == null) respond(OneRoundGameResponses.NO_CARD_IS_SELECTED_YET);
            else if (selectState != SelectState.PLAYER_MONSTER) respond(OneRoundGameResponses.SELECT_FROM_TABLE);
            else {
                GameMenuResponse gameMenuResponse;
                try {
                    if (ai == null) gameMenuResponse = GameMenuController.attack(game, GameMenuController.getCellNumber(), cellNumber, false);
                    else gameMenuResponse = GameMenuController.attack(game, GameMenuController.getCellNumber(), cellNumber, true);
                } catch (GameException e) {
                    if (e instanceof WinnerException) throw (WinnerException) e;
                    else if (e instanceof StopAttackException) {
                        if (((StopAttackException) e).getState() == StopEffectState.END_BATTLE_PHASE) {
                            goToNextPhase();
                        }
                    }
                    return;
                }
                GameMenuResponsesEnum answer = gameMenuResponse.getGameMenuResponseEnum();
                if (answer == GameMenuResponsesEnum.INVALID_SELECTION) respond(OneRoundGameResponses.INVALID_SELECTION);
                else if (answer == GameMenuResponsesEnum.NO_CARD_FOUND)
                    respond(OneRoundGameResponses.NO_CARD_FOUND_IN_GIVEN_POSITION);
                else if (answer == GameMenuResponsesEnum.YOU_HAVENT_SUMMONED_YET)
                    respond(OneRoundGameResponses.NOT_SUMMONED_YET);
                else if (answer == GameMenuResponsesEnum.ALREADY_ATTACKED)
                    respond(OneRoundGameResponses.THIS_CARD_ALREADY_ATTACKED);
                else if (answer == GameMenuResponsesEnum.ABORTED) {
                    respond(OneRoundGameResponses.ABORTED);
                    deselectCard(false);
                } else if (answer == GameMenuResponsesEnum.SUCCESSFUL) {
                    System.out.println((String) gameMenuResponse.getObj());
                    deselectCard(false);
                } else if (answer == GameMenuResponsesEnum.CANT_ATTACK)
                    System.out.println("This card cant Attack !");
            }
        }
    }

    public ArrayList<Phase> getAllowedPhaseForSummonSetChangePositionActiveEffect() {
        ArrayList<Phase> allowedPhaseForSummon = new ArrayList<>();
        allowedPhaseForSummon.add(Phase.MAIN_PHASE1);
        allowedPhaseForSummon.add(Phase.MAIN_PHASE2);
        return allowedPhaseForSummon;
    }

    public void showTable() {
        System.out.println(ANSI_BLACK + ANSI_YELLOW_BACKGROUND + GameMenuController.showTable(game) + ANSI_RESET);
    }

    public void respond(OneRoundGameResponses responses) {
        if (responses.equals(OneRoundGameResponses.INVALID_COMMAND))
            System.out.println("invalid command!");
        else if (responses.equals(OneRoundGameResponses.INVALID_SELECTION))
            System.out.println("invalid selection");
        else if (responses.equals(OneRoundGameResponses.ACTION_NOT_ALLOWED_IN_THIS_PHASE))
            System.out.println("action is not allowed in this phase");
        else if (responses.equals(OneRoundGameResponses.ACTIVE_EFFECT_IS_ONLY_FOR_SPELL_CARDS))
            System.out.println("active effect is only for spell card");
        else if (responses.equals(OneRoundGameResponses.BOTH_YOU_AND_YOUR_OPPONENT_MONSTER_CARDS_ARE_DESTROYED_AND_NO_ONE_RECEIVES_DAMAGE))
            System.out.println("both you and your opponent monster cards are destroyed and no one receives damage");
        else if (responses.equals(OneRoundGameResponses.CARD_DESELECTED))
            System.out.println("card deselected");
        else if (responses.equals(OneRoundGameResponses.CARD_IS_NOT_VISIBLE))
            System.out.println("card is not visible");
        else if (responses.equals(OneRoundGameResponses.CARD_SELECTED))
            System.out.println("card selected");
        else if (responses.equals(OneRoundGameResponses.DO_YOU_WANT_TO_ACTIVATE_YOUR_TRAP_AND_SPELL))
            System.out.println("do you want to activate your trap and spell?");
        else if (responses.equals(OneRoundGameResponses.FLIP_SUMMONED_SUCCESSFULLY))
            System.out.println("flip-summoned successfully");
        else if (responses.equals(OneRoundGameResponses.GRAVE_YARD_EMPTY))
            System.out.println("graveyard empty");
        else if (responses.equals(OneRoundGameResponses.ITS_NOT_YOUR_TURN_TO_PLAY_THIS_KIND_OF_MOVES))
            System.out.println("it's not your turn to play this kind of moves");
        else if (responses.equals(OneRoundGameResponses.MONSTER_CARD_POSITION_CHANGED_SUCCESSFULLY))
            System.out.println("monster card position changed successfully");
        else if (responses.equals(OneRoundGameResponses.MONSTER_CARD_ZONE_IS_FULL))
            System.out.println("monster card zone is full");
        else if (responses.equals(OneRoundGameResponses.NO_CARD_FOUND_IN_GIVEN_POSITION))
            System.out.println("no card found in given position");
        else if (responses.equals(OneRoundGameResponses.NO_CARD_IS_DESTROYED))
            System.out.println("no card is destroyed");
        else if (responses.equals(OneRoundGameResponses.NO_CARD_IS_SELECTED_YET))
            System.out.println("no card is selected yet");
        else if (responses.equals(OneRoundGameResponses.PREPARATIONS_OF_THIS_SPELL_ARE_NOT_DONE_YET))
            System.out.println("preparations of this spell are not done yet");
        else if (responses.equals(OneRoundGameResponses.THE_DEFENSE_POSITION_MONSTER_IS_DESTROYED))
            System.out.println("the defense position monster is destroyed");
        else if (responses.equals(OneRoundGameResponses.SELECTED_MONSTERS_LEVEL_DONT_MATCH_WITH_RITUAL_MONSTER))
            System.out.println("selected monsters level don't match with ritual monster");
        else if (responses.equals(OneRoundGameResponses.SPELL_ACTIVATED))
            System.out.println("spell activated");
        else if (responses.equals(OneRoundGameResponses.SPELL_CARD_ZONE_IS_FULL))
            System.out.println("spell card zone is full");
        else if (responses.equals(OneRoundGameResponses.SPELL_OR_TRAP_ACTIVATED))
            System.out.println("spell or trap activated");
        else if (responses.equals(OneRoundGameResponses.SUMMONED_SUCCESSFULLY))
            System.out.println("summoned successfully");
        else if (responses.equals(OneRoundGameResponses.THERE_ARE_NOT_ENOUGH_CARDS_FOR_TRIBUTE))
            System.out.println("there are not enough cards for tribute");
        else if (responses.equals(OneRoundGameResponses.THERE_IS_NO_CARD_TO_ATTACK_HERE))
            System.out.println("there is no card to attack here");
        else if (responses.equals(OneRoundGameResponses.THERE_IS_NO_WAY_YOU_COULD_RITUAL_SUMMON_A_MONSTER))
            System.out.println("there is no way you could ritual summon a monster");
        else if (responses.equals(OneRoundGameResponses.THERE_IS_NO_WAY_YOU_COULD_SPECIAL_SUMMON_A_MONSTER))
            System.out.println("there is no way you could special summon a monster");
        else if (responses.equals(OneRoundGameResponses.THERE_NO_MONSTERS_ON_ONE_OF_THIS_ADDRESSES))
            System.out.println("there no monsters on one this addresses");
        else if (responses.equals(OneRoundGameResponses.THERE_NO_MONSTERS_ON_THIS_ADDRESS))
            System.out.println("there no monster on this address");
        else if (responses.equals(OneRoundGameResponses.THIS_CARD_ALREADY_ATTACKED))
            System.out.println("this card already attacked");
        else if (responses.equals(OneRoundGameResponses.THIS_CARD_IS_ALREADY_IN_THE_WANTED_POSITION))
            System.out.println("this card is already in the wanted position");
        else if (responses.equals(OneRoundGameResponses.YOU_ALREADY_CHANGED_THIS_CARD_POSITION_THIS_TURN))
            System.out.println("you already changed this card position this turn");
        else if (responses.equals(OneRoundGameResponses.YOU_ALREADY_SUMMONED_OR_SET_ON_THIS_TURN))
            System.out.println("you already summoned/set on this turn");
        else if (responses.equals(OneRoundGameResponses.YOU_CANT_ACTIVE_AN_EFFECT_ON_THIS_TURN))
            System.out.println("you can't active an effect on this turn");
        else if (responses.equals(OneRoundGameResponses.YOU_CANT_ATTACK_WITH_THIS_CARD))
            System.out.println("you can't attack with this card");
        else if (responses.equals(OneRoundGameResponses.YOU_CANT_CHANGE_THIS_CARDS_POSITION))
            System.out.println("you can't change this cards position");
        else if (responses.equals(OneRoundGameResponses.YOU_CANT_FLIP_SUMMON_THIS_CARD))
            System.out.println("you can't flip summon this card");
        else if (responses.equals(OneRoundGameResponses.YOU_CANT_SET_THIS_CARD))
            System.out.println("you can't set this card");
        else if (responses.equals(OneRoundGameResponses.YOU_CANT_SUMMON_THIS_CARD))
            System.out.println("you can't summon this card");
        else if (responses.equals(OneRoundGameResponses.YOU_HAVE_ALREADY_ACTIVATED_THIS_CARD))
            System.out.println("you have already activated this card");
        else if (responses.equals(OneRoundGameResponses.YOU_SHOULD_RITUAL_SUMMON_RIGHT_NOW))
            System.out.println("you should ritual summon right now");
        else if (responses.equals(OneRoundGameResponses.YOU_SHOULD_SPECIAL_SUMMON_RIGHT_NOW))
            System.out.println("you should special summon right now");
        else if (responses.equals(OneRoundGameResponses.STANDBY_PHASE))
            System.out.println("phase: standby phase");
        else if (responses.equals(OneRoundGameResponses.MAIN_PHASE1))
            System.out.println("phase: main phase 1");
        else if (responses.equals(OneRoundGameResponses.MAIN_PHASE2))
            System.out.println("phase: main phase 2");
        else if (responses.equals(OneRoundGameResponses.DRAW_PHASE))
            System.out.println("phase: draw phase");
        else if (responses.equals(OneRoundGameResponses.BATTLE_PHASE))
            System.out.println("phase: battle phase");
        else if (responses.equals(OneRoundGameResponses.END_PHASE))
            System.out.println("phase: end phase");
        else if (responses.equals(OneRoundGameResponses.SET_SUCCESSFULLY))
            System.out.println("Card was set successfully");
        else if (responses.equals(OneRoundGameResponses.PLEASE_SELECT_MONSTER))
            System.out.println("Selected Card is not monster .. please select monster .");
        else if (responses.equals(OneRoundGameResponses.CANT_ATTACK_DIRECTLY))
            System.out.println("You can’t attack the opponent directly");
        else if (responses.equals(OneRoundGameResponses.NOT_SUMMONED_YET))
            System.out.println("You haven't summoned this card yet .");
        else if (responses.equals(OneRoundGameResponses.ABORTED))
            System.out.println("Action was aborted .");
        else if (responses.equals(OneRoundGameResponses.CANT_NORMAL_SUMMON))
            System.out.println("This card can't normal summon !");
        else if (responses.equals(OneRoundGameResponses.SELECT_FROM_TABLE))
            System.out.println("Please select from table .");
        else unknownError();
    }

    private static void unknownError() {
        System.out.println("unknown error occurred !");
    }

    private int getNumberFromString(String command) {
        Matcher matcher = RegexFunctions.getCommandMatcher(command, "^[\\D]*(?<cellNumber>[\\d]{1,9})[\\D]*$");
        if (matcher.find()) return Integer.parseInt(matcher.group("cellNumber"));
        return 0;
    }

    public void showHelp() {
        System.out.println(ANSI_BLACK_BACKGROUND + ANSI_YELLOW + "summon\n" +
                "show table\n" +
                "set\n" +
                "flip-summon\n" +
                "attack <cell number>\n" +
                "attack direct\n" +
                "active effect\n" +
                "show graveyard\n" +
                "show graveyard -r\n" +
                "send to graveyard\n" +
                "show all hand cards\n" +
                "card show --selected\n" +
                "select --monster <cell number> --opponent(optional)\n" +
                "select --spell <cell number> --opponent(optional)\n" +
                "select --field --opponent(optional)\n" +
                "select --hand <number>\n" +
                "set --position attack\n" +
                "set --position defense\n" +
                "next phase\n" +
                "surrender\n" +
                "select -d\n" +
                "show all hand cards\n" +
                "show phase\n" + ANSI_RESET);
    }

    public void showPhase() {
        switch (currentPhase) {
            case BATTLE_PHASE:
                respond(OneRoundGameResponses.BATTLE_PHASE);
                break;
            case DRAW_PHASE:
                respond(OneRoundGameResponses.DRAW_PHASE);
                break;
            case END_PHASE:
                respond(OneRoundGameResponses.END_PHASE);
                break;
            case MAIN_PHASE1:
                respond(OneRoundGameResponses.MAIN_PHASE1);
                break;
            case MAIN_PHASE2:
                respond(OneRoundGameResponses.MAIN_PHASE2);
                break;
            case STANDBY_PHASE:
                respond(OneRoundGameResponses.STANDBY_PHASE);
                break;
        }
    }
}
