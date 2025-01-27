package com.capstone.Players.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.capstone.Players.dto.BattingStatsDTO;
import com.capstone.Players.dto.BowlingStatsDTO;
import com.capstone.Players.dto.OrganizerDTO;
import com.capstone.Players.dto.PlayerStatsDTO;
import com.capstone.Players.model.User;
import com.capstone.Players.repository.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, PlayerStatsDTO> latestStats = new ConcurrentHashMap<>();

    // Create a new user
    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    // Update existing user
    public Mono<User> updateUser(String userId, User user) {
        return userRepository.findById(userId)
                .flatMap(existingUser -> {
                    existingUser.setUserName(user.getUserName());
                    existingUser.setUserEmail(user.getUserEmail());
                    existingUser.setUserTeamId(user.getUserTeamId());
                    existingUser.setTotalScore(user.getTotalScore());
                    existingUser.setTotalWickets(user.getTotalWickets());
                    existingUser.setTotalMatches(user.getTotalMatches());
                    existingUser.setHighestScore(user.getHighestScore());
                    existingUser.setHighestWickets(user.getHighestWickets());
                    existingUser.setNumberOfMatchesOrganized(user.getNumberOfMatchesOrganized());
                    existingUser.setNumberOfSupportStaff(user.getNumberOfSupportStaff());
                    existingUser.setNumberOfSponsors(user.getNumberOfSponsors());
                    return userRepository.save(existingUser);
                });
    }

    public Mono<User> updateTeamId(String userId, String teamId) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    user.setUserTeamId(teamId);
                    return userRepository.save(user);
                });
    }

    // Find user by email
    public Mono<User> findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

    // Retrieve player stats
    public Mono<PlayerStatsDTO> getPlayerStats(String userId) {
        return userRepository.findById(userId)
            .map(user -> {
                // Get the latest Kafka update for this user
                PlayerStatsDTO kafkaUpdate = latestStats.get(userId);

                // If we have a Kafka update, use its current values
                int currentScore = (kafkaUpdate != null) ?
                    kafkaUpdate.getCurrentScore() : user.getCurrentScore();
                int currentWickets = (kafkaUpdate != null) ?
                    kafkaUpdate.getCurrentWickets() : user.getCurrentWickets();

                return new PlayerStatsDTO(
                    user.getUserId(),
                    user.getUserName(),
                    user.getTotalScore(),
                    user.getTotalWickets(),
                    user.getTotalMatches(),
                    user.getHighestScore(),
                    user.getHighestWickets(),
                    currentScore,
                    currentWickets
                );
            });
    }

    public Mono<BattingStatsDTO> getBattingStats(String userId) {
        return userRepository.findById(userId)
                .map(user -> new BattingStatsDTO(
                        user.getUserId(),
                        user.getUserName(),
                        user.getTotalScore(), // total runs
                        user.getTotalMatches(),
                        user.getHighestScore()
                ));
    }

    // Method to retrieve Bowling Stats
    public Mono<BowlingStatsDTO> getBowlingStats(String userId) {
        return userRepository.findById(userId)
                .map(user -> new BowlingStatsDTO(
                        user.getUserId(),
                        user.getUserName(),
                        user.getTotalMatches(),
                        user.getTotalWickets(),
                        user.getHighestWickets()
                ));
    }

    // Method to retrieve Organizer Stats
    public Mono<OrganizerDTO> getOrganizerStats(String userId) {
        return userRepository.findById(userId)
                .map(user -> new OrganizerDTO(
                        user.getUserId(),
                        user.getUserName(),
                        user.getNumberOfMatchesOrganized(),
                        user.getNumberOfSponsors(),
                        user.getNumberOfSupportStaff()
                ));
    }

    public Mono<User> updatePlayerStats(String userId, PlayerStatsDTO playerStatsDTO) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    // Update player stats
                    user.setTotalScore(playerStatsDTO.getTotalScore());
                    user.setTotalMatches(playerStatsDTO.getTotalMatches());
                    user.setHighestScore(playerStatsDTO.getHighestScore());
                    user.setTotalWickets(playerStatsDTO.getTotalWickets());
                    user.setHighestWickets(playerStatsDTO.getHighestWickets());

                    return userRepository.save(user);
                });
    }

    public Mono<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public Mono<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }




        @KafkaListener(topics = "match-score", groupId = "player-scores", containerFactory = "kafkaListenerContainerFactory")
        public void listen(String message) {
            try {
                System.out.println("Received Kafka message: " + message);

                // Convert JSON string to Map
                Map<String, Object> messageValue = objectMapper.readValue(message, Map.class);

                String userId = messageValue.get("playerId") != null ? messageValue.get("playerId").toString() : null;
                String userTeamId = messageValue.get("teamId") != null ? messageValue.get("teamId").toString() : null;
                Integer runsScored = messageValue.get("runsScored") != null ?
                        Integer.parseInt(messageValue.get("runsScored").toString()) : 0;
                Integer wicketsTaken = messageValue.get("wicketsTaken") != null ?
                        Integer.parseInt(messageValue.get("wicketsTaken").toString()) : 0;
                Integer totaldeliveries = messageValue.get("deliveries") != null ?
                        Integer.parseInt(messageValue.get("deliveries").toString()) : 0;

                // Skip processing if userId is null
                if (userId == null) {
                    System.out.println("Skipping message with null playerId");
                    return;
                }

                userRepository.findById(userId)
                        .flatMap(user -> {
                            // Update current stats
//                            user.setCurrentScore(runsScored);
//                            user.setCurrentWickets(wicketsTaken);
                            user.setCurrentScore(user.getCurrentScore() + runsScored);
                            user.setCurrentWickets(user.getCurrentWickets() + wicketsTaken);

                            // Update total stats
                            if (runsScored > 0) {
                                user.setTotalScore(user.getTotalScore() + runsScored);
                            }
                            if (wicketsTaken > 0) {
                                user.setTotalWickets(user.getTotalWickets() + wicketsTaken);
                            }

                            // Update highest stats if current is higher
                            if (runsScored > user.getHighestScore()) {
                                user.setHighestScore(runsScored);
                            }
                            if (wicketsTaken > user.getHighestWickets()) {
                                user.setHighestWickets(wicketsTaken);
                            }

                            return userRepository.save(user);
                        })
                        .subscribe(
                                savedUser -> System.out.println("Updated user in database: " + savedUser),
                                error -> System.err.println("Error updating user: " + error.getMessage())
                        );
            } catch (Exception e) {
                System.err.println("Error processing Kafka message: " + e.getMessage());
                e.printStackTrace();
            }
        }


    public Mono<List<PlayerStatsDTO>> getAllPlayerStats() {
        return userRepository.findAll()
            .map(user -> new PlayerStatsDTO(
                user.getUserId(),
                user.getUserName(),
                user.getTotalScore(),
                user.getTotalWickets(),
                user.getTotalMatches(),
                user.getHighestScore(),
                user.getHighestWickets(),
                user.getCurrentScore(),  // Now including current score
                user.getCurrentWickets() // Now including current wickets
            ))
            .collectList()
            .map(currentStats -> {
                List<PlayerStatsDTO> allStats = new ArrayList<>(currentStats);
                synchronized (latestStats) {
                    // Add only the most recent stats for each user
                    Map<String, PlayerStatsDTO> latestStats = new HashMap<>();
                    for (PlayerStatsDTO stat : latestStats.values()) {
                        latestStats.put(stat.getUserId(), stat);
                    }
                    allStats.addAll(latestStats.values());
                }
                return allStats;
            });
    }

    public Mono<List<PlayerStatsDTO>> getPlayerStatsByUserId(String userId) {
        return userRepository.findById(userId)
            .map(user -> {
                PlayerStatsDTO currentStats = new PlayerStatsDTO(
                    user.getUserId(),
                    user.getUserName(),
                    user.getTotalScore(),
                    user.getTotalWickets(),
                    user.getTotalMatches(),
                    user.getHighestScore(),
                    user.getHighestWickets(),
                    user.getCurrentScore(),  // Now including current score
                    user.getCurrentWickets() // Now including current wickets
                );

                List<PlayerStatsDTO> allStats = new ArrayList<>();
                allStats.add(currentStats);

                synchronized (latestStats) {
                    List<PlayerStatsDTO> userStats = latestStats.values().stream()
                        .filter(stats -> stats.getUserId().equals(userId))
                        .collect(Collectors.toList());
                    allStats.addAll(userStats);
                }

                return allStats;
            });
    }

    public Mono<PlayerStatsDTO> savePlayerStats(PlayerStatsDTO playerStatsDTO) {
        return userRepository.findById(playerStatsDTO.getUserId())
            .flatMap(user -> {
                user.setCurrentScore(playerStatsDTO.getCurrentScore());
                user.setCurrentWickets(playerStatsDTO.getCurrentWickets());
                user.setTotalScore(user.getTotalScore() + playerStatsDTO.getCurrentScore());
                user.setTotalWickets(user.getTotalWickets() + playerStatsDTO.getCurrentWickets());

                if (playerStatsDTO.getCurrentScore() > user.getHighestScore()) {
                    user.setHighestScore(playerStatsDTO.getCurrentScore());
                }
                if (playerStatsDTO.getCurrentWickets() > user.getHighestWickets()) {
                    user.setHighestWickets(playerStatsDTO.getCurrentWickets());
                }

                return userRepository.save(user);
            })
            .map(user -> playerStatsDTO);
    }

}
