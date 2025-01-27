package com.capstone.Players.model;

import java.util.Random;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String userId="UID-"+generateCode();
    private String userName;
    private String userEmail;
    private String password;
    private String role;

    private String userTeamId="-";
    private int currentScore= 0;
    private int currentWickets=0;
    
    private int totalWickets = 0;
    private int totalMatches = 0;
    private int highestScore = 0;
    private int highestWickets = 0;
    private int numberOfMatchesOrganized = 0;
    private int numberOfSupportStaff = 0;
    private int numberOfSponsors = 0;
    private int totalScore = 0;
    private int totaldeliveries=0;
    
    // Default constructor
    public User() {}
    
    // Parameterized constructor
    public User(String userName, String userEmail, String role) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.role = role;
    }
    
    // Getters and Setters
    // ... (same as previously defined)
    
    
    public int getCurrentWickets() {
        return currentWickets;
    }

    public void setCurrentWickets(int currentWickets) {
        this.currentWickets = currentWickets;
    }
    
    public String getUserId() {
        return userId;
    }
    
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    
    public String getUserTeamId() {
        return userTeamId;
    }
    
    public void setUserTeamId(String userTeamId) {
        this.userTeamId = userTeamId;
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
    
    public int getHighestWickets() {
        return highestWickets;
    }

    public void setHighestWickets(int highestWickets) {
        this.highestWickets = highestWickets;
    }

    public int getNumberOfMatchesOrganized() {
        return numberOfMatchesOrganized;
    }
    
    public void setNumberOfMatchesOrganized(int numberOfMatchesOrganized) {
        this.numberOfMatchesOrganized = numberOfMatchesOrganized;
    }
    
    public int getNumberOfSupportStaff() {
        return numberOfSupportStaff;
    }
    
    public void setNumberOfSupportStaff(int numberOfSupportStaff) {
        this.numberOfSupportStaff = numberOfSupportStaff;
    }

    public int getNumberOfSponsors() {
        return numberOfSponsors;
    }

    public void setNumberOfSponsors(int numberOfSponsors) {
        this.numberOfSponsors = numberOfSponsors;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public static String generateCode(){
        StringBuilder code = new StringBuilder();
        String possible = "0123456789";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            code.append(possible.charAt(random.nextInt(possible.length())));
        }
        return code.toString();
    }

    public int getTotaldeliveries() {
        return totaldeliveries;
    }

    public void setTotaldeliveries(int totaldeliveries) {
        this.totaldeliveries = totaldeliveries;
    }
}
