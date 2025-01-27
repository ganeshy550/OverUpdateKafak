package com.capstone.teams.dto;

import java.util.List;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data
// @AllArgsConstructor
// @NoArgsConstructor
public class TeamCreationRequest {
    private String matchId;
    private List<String> teamNames; // Two team names
    private int teamSize;

    

    public TeamCreationRequest() {
    }
    public TeamCreationRequest(String matchId, List<String> teamNames, int teamSize) {
        this.matchId = matchId;
        this.teamNames = teamNames;
        this.teamSize = teamSize;
    }
    public String getMatchId() {
        return matchId;
    }
    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
    public List<String> getTeamNames() {
        return teamNames;
    }
    public void setTeamNames(List<String> teamNames) {
        this.teamNames = teamNames;
    }
    public int getTeamSize() {
        return teamSize;
    }
    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    
}
