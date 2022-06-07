package model.exceptions;
import model.User;

public class WinnerException extends GameException {

    private final User winner;
    private final User loser;
    private final int winnerLP, loserLP;

    public WinnerException(User winner, User loser, int winnerLP, int loserLP) {
        this.winner = winner;
        this.loser = loser;
        this.winnerLP = winnerLP;
        this.loserLP = loserLP;
    }

    public int getLoserLP() {
        return loserLP;
    }

    public int getWinnerLP() {
        return winnerLP;
    }

    public User getWinner() {
        return winner;
    }

    public User getLoser() {
        return loser;
    }
}
