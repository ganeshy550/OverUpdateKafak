package com.capstone.Players.dto;

public class PlayerStatsDTO {

    private String userId;
    private String userName;
    private int totalScore;
    private int totalWickets;
    private int totalMatches;
    private int highestScore;
    private int highestWickets;
    private int currentScore;
    private int currentWickets;

    // Constructor
    public PlayerStatsDTO(String userId, String userName, int totalScore, int totalWickets,
                          int totalMatches, int highestScore, int highestWickets, int currentScore, int currentWickets) {
        this.userId = userId;
        this.userName = userName;
        this.totalScore = totalScore;
        this.totalWickets = totalWickets;
        this.totalMatches = totalMatches;
        this.highestScore = highestScore;
        this.highestWickets = highestWickets;
        this.currentScore= currentScore;
        this.currentWickets= currentWickets;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalWickets() {
        return totalWickets;
    }

    public void setTotalWickets(int totalWickets) {
        this.totalWickets = totalWickets;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

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

    public int getHighestWickets() {
        return highestWickets;
    }

    public void setHighestWickets(int highestWickets) {
        this.highestWickets = highestWickets;
    }

    // Add this default constructor just after your class declaration
    public PlayerStatsDTO() {
        // Default constructor required for JSON deserialization
    }
}
