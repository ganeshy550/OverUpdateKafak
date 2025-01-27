package com.capstone.Players.dto;

public class BattingStatsDTO {

    private String playerId;
    private String playerName;
    private int totalRuns;
    private int totalMatches;
    private int highestRuns;

    // Constructor, Getters, and Setters
    public BattingStatsDTO(String playerId, String playerName, int totalRuns, int totalMatches, int highestRuns) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.totalRuns = totalRuns;
        this.totalMatches = totalMatches;
        this.highestRuns = highestRuns;
    }

    // Getters and Setters
    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public int getHighestRuns() {
        return highestRuns;
    }
}
