package model.game;

import model.User;

public class MiniGame {
    private final User firstUser;
    private final User secondUser;
    private User winner;
    public MiniGame (User firstUser, User secondUser){
        this.firstUser = firstUser;
        this.secondUser = secondUser;
    }
    public User getFirstUser (){
        return firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public void setWinner(User winner){
        this.winner = winner;
    }
    public User getWinner(){
        return winner;
    }
}
