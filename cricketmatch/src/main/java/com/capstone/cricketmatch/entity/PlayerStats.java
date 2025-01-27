package com.capstone.cricketmatch.entity;


public class PlayerStats {
    private String playerId;      // Changed from userId
    private String teamId;    // Changed from userName
    private int runsScored;      // Changed from currentScore
    private int wicketsTaken;    // Changed from currentWickets
    private int deliveries;

    public PlayerStats(String playerId, String teamId, int runsScored, int wicketsTaken, int deliveries) {
        this.playerId = playerId;
        this.teamId = teamId;
        this.runsScored = runsScored;
        this.wicketsTaken = wicketsTaken;
        this.deliveries = deliveries;
    }

    public PlayerStats() {
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public int getRunsScored() {
        return runsScored;
    }

    public void setRunsScored(int runsScored) {
        this.runsScored = runsScored;
    }

    public int getWicketsTaken() {
        return wicketsTaken;
    }

    public void setWicketsTaken(int wicketsTaken) {
        this.wicketsTaken = wicketsTaken;
    }

    public int getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(int deliveries) {
        this.deliveries = deliveries;
    }
}