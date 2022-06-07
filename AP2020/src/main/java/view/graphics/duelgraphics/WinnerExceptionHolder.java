package view.graphics.duelgraphics;

import model.exceptions.WinnerException;

public abstract class WinnerExceptionHolder {

    public enum GameMode {ONE_ROUND, THREE_ROUND}
    public static GameMode gameMode;
    public static WinnerException winnerException1;
    public static WinnerException winnerException2;
    public static WinnerException winnerException3;

    public static void setGameMode(GameMode gameMode) {
        WinnerExceptionHolder.gameMode = gameMode;
    }

    public static void resetExceptions() {
        winnerException1 = null;
        winnerException2 = null;
        winnerException3 = null;
    }
}
