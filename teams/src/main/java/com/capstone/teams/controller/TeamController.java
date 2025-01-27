package com.capstone.teams.controller;

import com.capstone.teams.dto.TeamScoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.capstone.teams.dto.TeamCreationRequest;
import com.capstone.teams.entity.Team;
import com.capstone.teams.service.TeamService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    
    @Autowired
    private TeamService teamService;
    
    @PostMapping("/create-for-match")
    public Mono<Void> createTeamsForMatch(@RequestBody TeamCreationRequest request) {
        return teamService.createTeamsForMatch(request.getMatchId(), request.getTeamNames(), request.getTeamSize());
    }


    @PostMapping("/register")
    public Mono<Team> registerUser(@RequestParam String matchId, @RequestParam String userId, @RequestParam String choice) {
        return teamService.registerUser(matchId, userId, choice);
    }

    @GetMapping("/{matchId}")
    public Flux<Team> getTeamDetails(@PathVariable String matchId) {
        return teamService.getTeamDetails(matchId);
    }

    @GetMapping("/{matchId}/{teamName}/score")
    public Mono<TeamScoreDTO> getTeamScore(
            @PathVariable String matchId,
            @PathVariable String teamName) {
        return teamService.getTeamScore(matchId, teamName);
    }

    // Optional: Add endpoint to view player stats in a team
    @GetMapping("/{matchId}/{teamName}/player-stats")
    public Mono<Map<String, List<Integer>>> getTeamPlayerStats(
            @PathVariable String matchId,
            @PathVariable String teamName) {
        return teamService.getTeamDetails(matchId)
                .filter(team -> team.getTeamName().equals(teamName))
                .next()
                .map(team -> team.getTeam());
    }

}
