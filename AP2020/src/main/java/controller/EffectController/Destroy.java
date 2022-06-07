package controller.EffectController;

import controller.GameMenuController;
import model.card.Card;
import model.card.monster.MonsterType;
import model.game.*;
import org.jetbrains.annotations.Nullable;

public class Destroy extends EffectController {

    public static void CommandKnight(Game game, Card card) {
        Limits limits = getLimits(game, card);
        limits.decreaseATKAddition(400);
        limits = getRivalsLimits(game, card);
        limits.unbanAttackingToCell(getCellNumberOfMonster(game, card));
    }

    public static void MirageDragon(Game game, Card card) {
        getLimits(game, card).removeLimit(EffectLimitations.CANT_ACTIVATE_TRAP);
    }

    public static void Messengerofpeace(Game game, Card card) {
        game.getPlayerLimits().removeCardLimitOnATKBound(card);
        game.getRivalLimits().removeCardLimitOnATKBound(card);
    }

    public static void Yami(Game game, Card card) {
        game.getPlayerLimits().removeFieldZoneATK(MonsterType.FIEND);
        game.getRivalLimits().removeFieldZoneATK(MonsterType.FIEND);
        game.getPlayerLimits().removeFieldZoneATK(MonsterType.FAIRY);
        game.getRivalLimits().removeFieldZoneATK(MonsterType.FAIRY);
        game.getPlayerLimits().removeFieldZoneATK(MonsterType.SPELLCASTER);
        game.getRivalLimits().removeFieldZoneATK(MonsterType.SPELLCASTER);
    }

    public static void Forest(Game game, Card card) {
        game.getPlayerLimits().removeFieldZoneATK(MonsterType.BEAST);
        game.getPlayerLimits().removeFieldZoneATK(MonsterType.BEAST_WARRIOR);
        game.getPlayerLimits().removeFieldZoneATK(MonsterType.INSECT);
        game.getRivalLimits().removeFieldZoneATK(MonsterType.BEAST);
        game.getRivalLimits().removeFieldZoneATK(MonsterType.BEAST_WARRIOR);
        game.getRivalLimits().removeFieldZoneATK(MonsterType.INSECT);

    }

    public static void ClosedForest(Game game, Card card) {
        getLimits(game, card).removeFieldZoneATK(MonsterType.BEAST);
    }

    public static void UMIIRUKA(Game game, Card card) {
        game.getPlayerLimits().removeFieldZoneATK(MonsterType.AQUA);
        game.getPlayerLimits().removeFieldZoneDEF(MonsterType.AQUA);
        game.getRivalLimits().removeFieldZoneATK(MonsterType.AQUA);
        game.getRivalLimits().removeFieldZoneDEF(MonsterType.AQUA);
    }

    public static void SwordofDarkDestruction(Game game, Card card) {
        unEquip(game, card);
    }

    public static void BlackPendant(Game game, Card card) {
        unEquip(game,card);
    }

    public static void UnitedWeStand(Game game, Card card) {
        unEquip(game,card);
    }

    public static void MagnumShield(Game game, Card card) {
        unEquip(game,card);
    }

    public static void CalloftheHaunted(Game game, Card card){
        Limits limits = getLimits(game,card);
        Card card1 = limits.getEquipMonster().get(card);
        if (isCardExists(getBoard(game,card),card1)) GameMenuController.sendToGraveYard(game,card1);
        unEquip(game,card);
    }


    private static boolean isCardExists(Board board, Card card) {
        for (Cell cell : board.getMonsterZone()) {
            Card card2 = cell.getCard();
            if (card2 != null && card2.equals(card)) return true;
        }
        return false;
    }

    private static void unEquip(Game game, Card card) {
        Limits limits = getLimits(game, card);
        limits.unEquipMonster(card);
    }
}
