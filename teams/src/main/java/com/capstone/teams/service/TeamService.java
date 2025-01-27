package com.capstone.teams.service;

import com.capstone.teams.dto.PlayerStatsDTO;
import com.capstone.teams.dto.TeamScoreDTO;
import com.capstone.teams.entity.Team;
import com.capstone.teams.repository.TeamRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeamService {

    public static Long id = 0l;

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserServiceClient userServiceClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    //    private PlayerStatsDTO playerStatsDTO;

    public Mono<Void> createTeamsForMatch(String matchId, List<String> teamNames, int teamSize) {
        if (teamNames.size() != 2) {
            return Mono.error(new IllegalArgumentException("Exactly two team names must be provided."));
        }

        // Create the first team
        Team team1 = new Team();
        team1.setId(id); // Ensure this matches your Team entity's field and method names
        id++;
        team1.setMatchId(matchId);
        team1.setTeam(new HashMap<String,List<Integer>>());
        team1.setTeamSize(teamSize);
        team1.setTeamName(teamNames.get(0));

        // Create the second team
        Team team2 = new Team();
        team2.setId(id); // Ensure this matches your Team entity's field and method names
        id++;
        team2.setMatchId(matchId);
        team2.setTeam(new HashMap<String,List<Integer>>());
        team2.setTeamSize(teamSize);
        team2.setTeamName(teamNames.get(1));

        // Save both teams to the repository
        return teamRepository.saveAll(List.of(team1, team2)).then();
    }

    // public Mono<Team> registerUser(String matchId, String userId, String choice) {
    //     return teamRepository.findAllByMatchId(matchId)
    //             .filter(team -> {
    //                 // Check if the chosen team has space based on the teamName and teamSize
    //                 if (choice.equals("Team A") && team.getTeamName().equals("Team A")) {
    //                     return team.getTeam().size() < team.getTeamSize(); // Check if Team A has space
    //                 } else if (choice.equals("Team B") && team.getTeamName().equals("Team B")) {
    //                     return team.getTeam().size() < team.getTeamSize(); // Check if Team B has space
    //                 }
    //                 return false; // If neither team has space, return false
    //             })
    //             .next() // Get the first team with space
    //             .flatMap(team -> {
    //                 // Add user to the chosen team
    //                 if (choice.equals("Team A") && team.getTeamName().equals("Team A")) {
    //                     team.getTeam().put(userId, new ArrayList<Integer>()); // Add the user to Team A
    //                     userServiceClient.updateTeam(userId, team.getId().toString()); // Update the user's team ID in the database
    //                 } else if (choice.equals("Team B") && team.getTeamName().equals("Team B")) {
    //                     team.getTeam().put(userId, new ArrayList<Integer>()); // Add the user to Team B
    //                     userServiceClient.updateTeam(userId, team.getId().toString()); // Update the user's team ID in the database
    //                 }
    //                 return teamRepository.save(team); // Save the updated team

    //             }).switchIfEmpty(Mono.error(new RuntimeException("No available teams for match: " + matchId)));
    // }

    // ... existing code ...

    public Mono<Team> registerUser(String matchId, String userId, String choice) {
        return teamRepository.findAllByMatchId(matchId)
                .filter(team -> {
                    if (choice.equals("Team A") && team.getTeamName().equals("Team A")) {
                        return team.getTeam().size() < team.getTeamSize();
                    } else if (choice.equals("Team B") && team.getTeamName().equals("Team B")) {
                        return team.getTeam().size() < team.getTeamSize();
                    }
                    return false;
                })
                .next()
                .flatMap(team -> {
                    // Add user to the chosen team
                    if ((choice.equals("Team A") && team.getTeamName().equals("Team A")) ||
                            (choice.equals("Team B") && team.getTeamName().equals("Team B"))) {
                        List<Integer> initialStats = new ArrayList<>();
                        initialStats.add(0);  // currentScore = 0
                        initialStats.add(0);  // currentWickets = 0
                        team.getTeam().put(userId, initialStats);
                        return teamRepository.save(team)
                                // Then update the user's team ID
                                .flatMap(savedTeam ->
                                        userServiceClient.updateTeam(userId, savedTeam.getId().toString())
                                                .thenReturn(savedTeam)
                                );
                    }
                    return Mono.just(team);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No available teams for match: " + matchId)));
    }

    // ... existing code ...
    public Flux<Team> getTeamDetails(String matchId) {
        return teamRepository.findAllByMatchId(matchId);
    }

//    public Mono<Team> updateTeamScore(String matchId, String userId) {
//        return teamRepository.findAllByMatchId(matchId)
//                .filter(team -> team.getTeam().containsKey(userId))
//                .next()
//                .flatMap(team -> {
//                    return userServiceClient.getCurrentStats(userId)
//                            .flatMap(userStats -> {
//                                // Update this player's stats in the team
//                                List<Integer> stats = new ArrayList<>();
//                                stats.add(userStats.getCurrentScore());
//                                stats.add(userStats.getCurrentWickets());
//                                team.getTeam().put(userId, stats);
//
//                                // Calculate total team score and wickets from all players
//                                int totalScore = 0;
//                                int totalWickets = 0;
//                                for (List<Integer> playerStats : team.getTeam().values()) {
//                                    if (!playerStats.isEmpty()) {
//                                        totalScore += playerStats.get(0);
//                                        totalWickets += playerStats.get(1);
//                                    }
//                                }
//
//                                team.setTeamScore(totalScore);
//                                team.setTeamWickets(totalWickets);
//
//                                return teamRepository.save(team);
//                            });
//                });
//    }

    @KafkaListener(topics = "match-score", groupId = "team-scores", containerFactory = "kafkaListenerContainerFactory")
    public void consumeScoreUpdate(String message) {
        try {
            System.out.println("Received Kafka message in Team Service: " + message);

            // Convert JSON string to Map
            Map<String, Object> messageValue = objectMapper.readValue(message, Map.class);

            String userId = messageValue.get("playerId") != null ? messageValue.get("playerId").toString() : null;
            String teamId = messageValue.get("teamId") != null ? messageValue.get("teamId").toString() : null;
            Integer runsScored = messageValue.get("runsScored") != null ?
                    Integer.parseInt(messageValue.get("runsScored").toString()) : 0;
            Integer wicketsTaken = messageValue.get("wicketsTaken") != null ?
                    Integer.parseInt(messageValue.get("wicketsTaken").toString()) : 0;
            Integer deliviries = messageValue.get("deliveries") != null ?
                    Integer.parseInt(messageValue.get("deliveries").toString()) : 0;


            // If userId is null or "0", directly update team score
            if (userId == null || userId.equals("0")) {
                System.out.println("Updating team score directly for teamId: " + teamId);
                updateTeamScore(teamId, runsScored, wicketsTaken,deliviries);
            }
            // If userId exists, still update team score (no player stats update)
            else if (teamId != null) {
                System.out.println("Updating team score for teamId: " + teamId);
                updateTeamScore(teamId, runsScored, wicketsTaken,deliviries);
            }

        } catch (Exception e) {
            System.err.println("Error processing Kafka message in Team Service: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateTeamScore(String teamId, int runsScored, int wicketsTaken,int deliveries) {
        teamRepository.findById(Long.parseLong(teamId))
                .flatMap(team -> {
                    // Simply update team's total score and wickets
                    team.setTeamScore(team.getTeamScore() + runsScored);
                    team.setTeamWickets(team.getTeamWickets() + wicketsTaken);

                    if (deliveries == 1) {
                        updateTeamOvers(team);
                    }

                    return teamRepository.save(team);
                })
                .subscribe(
                        savedTeam -> System.out.println("Updated team score - Team: " + savedTeam.getTeamName()
                                + ", Score: " + savedTeam.getTeamScore()
                                + ", Wickets: " + savedTeam.getTeamWickets()),
                        error -> System.err.println("Error updating team score: " + error.getMessage())
                );
    }
    public Mono<TeamScoreDTO> getTeamScore(String matchId, String teamName) {
        return teamRepository.findAllByMatchId(matchId)
                .filter(team -> team.getTeamName().equals(teamName))
                .next()
                .flatMap(team -> {
                    List<Mono<PlayerStatsDTO>> playerStatsList = new ArrayList<>();

                    // Get current stats for all players in the team
                    for (String userId : team.getTeam().keySet()) {
                        playerStatsList.add(userServiceClient.getCurrentStats(userId));
                    }

                    return Flux.fromIterable(playerStatsList)
                            .flatMap(mono -> mono)
                            .collectList()
                            .flatMap(stats -> {
                                // Just store player stats in team
                                return storePlayerStats(team, stats)
                                        .then(Mono.just(new TeamScoreDTO(
                                                team.getTeamName(),
                                                team.getTeamScore(),
                                                team.getTeamWickets(),
                                                team.getTeamOvers()

                                        )));
                            });
                });
    }
//    private Mono<Void> updateAllPlayerStats(Team team) {
//        List<Mono<Void>> updates = new ArrayList<>();
//
//        for (String userId : team.getTeam().keySet()) {
//            updates.add(userServiceClient.getCurrentStats(userId)
//                    .flatMap(stats -> {
//                        List<Integer> playerStats = new ArrayList<>();
//                        playerStats.add(stats.getCurrentScore());
//                        playerStats.add(stats.getCurrentWickets());
//                        team.getTeam().put(userId, playerStats);
//                        return teamRepository.save(team).then();
//                    }));
//        }
//        return Flux.fromIterable(updates)
//                .flatMap(mono -> mono)
//                .then();
//    }

    private Mono<Team> storePlayerStats(Team team, List<PlayerStatsDTO> playerStats) {
        int index = 0;
        for (String userId : team.getTeam().keySet()) {
            if (index < playerStats.size()) {
                List<Integer> stats = new ArrayList<>();
                stats.add(playerStats.get(index).getCurrentScore());
                stats.add(playerStats.get(index).getCurrentWickets());
                team.getTeam().put(userId, stats);
                index++;
            }
        }
        return teamRepository.save(team);
    }

    private void updateTeamOvers(Team team) {
        double currentOvers = team.getTeamOvers();
        int wholePart = (int) currentOvers;
        double decimalPart = currentOvers - wholePart;

        // Add 0.1 to the current overs
        decimalPart = Math.round((decimalPart + 0.1) * 10.0) / 10.0;

        // If decimal part reaches or exceeds 0.6, increment the whole number
        if (decimalPart >= 0.6) {
            wholePart++;
            decimalPart = 0.0;
        }

        team.setTeamOvers(wholePart + decimalPart);
    }

}
