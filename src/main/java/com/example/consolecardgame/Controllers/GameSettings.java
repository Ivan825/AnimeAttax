package com.example.consolecardgame.Controllers;

public class GameSettings {
    private String gameMode;
    private int numberOfRounds;
    private String player1Name;
    private String player2Name;

    // Getters and Setters
    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int NumberOfRounds) {
        numberOfRounds = NumberOfRounds;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String Player1Name) {
        player1Name = Player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String Player2Name) {
        player2Name = Player2Name;
    }
}
