package com.example.consolecardgame.game;

import com.example.consolecardgame.players.Player;

public class Round {
    private int roundNumber;
    private Player roundWinner;

    public Round(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Player getRoundWinner() {
        return roundWinner;
    }

    public void setRoundWinner(Player roundWinner) {
        this.roundWinner = roundWinner;
    }

    // Additional round methods...
}
