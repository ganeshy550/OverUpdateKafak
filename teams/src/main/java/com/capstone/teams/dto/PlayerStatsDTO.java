package com.capstone.teams.dto;

public class PlayerStatsDTO {
    private int currentScore;
    private int currentWickets;

    // Default constructor
    public PlayerStatsDTO() {}

    // Getters and setters
    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getCurrentWickets() {
        return currentWickets;
    }

    public void setCurrentWickets(int currentWickets) {
        this.currentWickets = currentWickets;
    }
}
