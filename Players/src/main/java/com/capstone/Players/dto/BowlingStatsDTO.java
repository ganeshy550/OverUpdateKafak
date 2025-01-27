package com.capstone.Players.dto;

public class BowlingStatsDTO {

    private String playerId;
    private String playerName;
    private int totalMatches;
    private int totalWickets;
    private int highestWickets;

    // Constructor, Getters, and Setters
    public BowlingStatsDTO(String playerId, String playerName, int totalMatches, int totalWickets, int highestWickets) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.totalMatches = totalMatches;
        this.totalWickets = totalWickets;
        this.highestWickets = highestWickets;
    }

    // Getters and Setters
    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public int getTotalWickets() {
        return totalWickets;
    }

    public int getHighestWickets() {
        return highestWickets;
    }
}
