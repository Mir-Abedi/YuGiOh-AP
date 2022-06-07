package view.duelMenu;

import model.User;

public class WinnerStatus {
    private User winner;
    private User loser;
    private int winnerLP;
    private int loserLP;

    public WinnerStatus(User winner, User loser, int winnerLP, int loserLP){
        setWinner(winner);
        setWinnerLP(winnerLP);
        setLoser(loser);
        setLoserLP(loserLP);
    }

    public User getWinner() {
        return winner;
    }

    public User getLoser() {
        return loser;
    }

    public int getWinnerLP() {
        return winnerLP;
    }

    public int getLoserLP() {
        return loserLP;
    }

    public void setWinnerLP(int winnerLP) {
        this.winnerLP = winnerLP;
    }

    public void setLoserLP(int loserLP) {
        this.loserLP = loserLP;
    }

    public void setLoser(User loser) {
        this.loser = loser;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

}
