package view.duelMenu;

import controller.GameMenuController;
import model.exceptions.WinnerException;
import model.game.Game;
import view.LoginMenu;
import view.regexes.OneRoundGameRegexes;
import view.responses.OneRoundGameResponses;

public class EndPhaseMenu extends OneRoundGame{
    public EndPhaseMenu(Game game) {
        super(game, LoginMenu.getInstance().getScanner());
    }

    public void run() throws WinnerException {
        String command;
        System.out.println("You need to send some of your hand cards to graveyard before you can continue ..\n" +
                "Your current number of cards : " + game.getPlayerHandCards().size());
        while(true) {
            command = scanner.nextLine().trim();
            if (OneRoundGameRegexes.doesItSelectMyMonsterCellCommand(command))
                super.selectMyMonsterCell(command);
            else if (OneRoundGameRegexes.doesItSelectMySpellCellCommand(command))
                super.selectMySpellCell(command);
            else if (OneRoundGameRegexes.doesItSelectCardFromMyHandCommand(command))
                super.selectCardFromMyHand(command);
            else if (OneRoundGameRegexes.doesItSelectMyFieldZoneCommand(command))
                super.selectMyFieldZone();
            else if (OneRoundGameRegexes.doesItSelectOpponentFieldZoneCommand(command))
                super.selectOpponentFieldZone();
            else if (OneRoundGameRegexes.doesItSelectOpponentMonsterCellCommand(command))
                super.selectOpponentMonsterCell(command);
            else if (OneRoundGameRegexes.doesItSelectOpponentSpellCellCommand(command))
                super.selectOpponentSpellCell(command);
            else if (command.matches("^send to graveyard$")) {
                sendToGraveYard();
                int size = game.getPlayerHandCards().size();
                if (size > 6) {
                    System.out.println("Current number : " + size);
                } else {
                    System.out.println("Now you can change your turn ..");
                    return;
                }
            }else if (command.matches(OneRoundGameRegexes.SHOW_TABLE)) {
                showTable();
            } else if (OneRoundGameRegexes.showSelectedCard(command))
                showSelectedCard();
            else if (command.matches(OneRoundGameRegexes.showGraveyard))
                showGraveyard(true);
            else if (command.matches(OneRoundGameRegexes.SHOW_GRAVEYARD_RIVAL))
                showGraveyard(false);
            else if (command.matches(OneRoundGameRegexes.summon))
                summon();
            else if (command.matches(OneRoundGameRegexes.surrender))
                surrender();
            else if (command.matches("^help$"))
                showHelp();
            else if (command.matches(OneRoundGameRegexes.deselectCard))
                deselectCard(true);
            else if (command.matches(OneRoundGameRegexes.showHandCards))
                super.showHandCards();
            else respond(OneRoundGameResponses.INVALID_COMMAND);
        }
    }

    @Override
    public void showHelp() {
        System.out.println(ANSI_BLACK_BACKGROUND + ANSI_YELLOW + "show table\n" +
                "show graveyard\n" +
                "show graveyard -r\n" +
                "card show --selected\n" +
                "select --monster <cell number> --opponent(optional)\n" +
                "select --spell <cell number> --opponent(optional)\n" +
                "select --field --opponent(optional)\n" +
                "select --hand <number>\n" +
                "surrender\n" +
                "show all hand cards\n" +
                "select -d\n" + ANSI_RESET);
    }

    public void sendToGraveYard() {
        SelectState selectState = GameMenuController.getSelectState();
        if (selectState == null) respond(OneRoundGameResponses.NO_CARD_IS_SELECTED_YET);
        else if (selectState != SelectState.HAND) System.out.println("Please select from hand ..");
        else {
            GameMenuController.sendToGraveYardFromHand(game);
            deselectCard(false);
        }
    }
}
