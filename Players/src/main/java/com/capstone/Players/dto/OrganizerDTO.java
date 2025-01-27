package com.capstone.Players.dto;

public class OrganizerDTO {

    private String userId;
    private String userName;
    private int numberOfMatchesOrganized;
    private int sponsors;
    private int supportStaff;

    // Constructor, Getters, and Setters
    public OrganizerDTO(String userId, String userName, int numberOfMatchesOrganized, int sponsors, int supportStaff) {
        this.userId = userId;
        this.userName = userName;
        this.numberOfMatchesOrganized = numberOfMatchesOrganized;
        this.sponsors = sponsors;
        this.supportStaff = supportStaff;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getNumberOfMatchesOrganized() {
        return numberOfMatchesOrganized;
    }

    public int getSponsors() {
        return sponsors;
    }

    public int getSupportStaff() {
        return supportStaff;
    }
}

