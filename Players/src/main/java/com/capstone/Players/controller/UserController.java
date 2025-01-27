package com.capstone.Players.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.Players.dto.BattingStatsDTO;
import com.capstone.Players.dto.BowlingStatsDTO;
import com.capstone.Players.dto.PlayerStatsDTO;
import com.capstone.Players.model.User;
import com.capstone.Players.service.UserService;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;



    // Create a new user
    @PostMapping("/createUser")
    public Mono<User> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Update an existing user
    @PutMapping("/updateUser/{userId}")
    public Mono<User> updateUser(@PathVariable String userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    @GetMapping("user/userId/{userId}")
    public Mono<User> findByUserId(@PathVariable String userId) {
        return userService.findByUserId(userId);
    }

    @GetMapping("/user/userName/{userName}")
    public Mono<User> findByUserName(@PathVariable String userName) {
        return userService.findByUserName(userName);
    }

    // Get user by email
    @GetMapping("/user/email/{userEmail}")
    public Mono<User> findByUserEmail(@PathVariable String userEmail) {
        return userService.findByUserEmail(userEmail);
    }


    // Get player stats
    @GetMapping("user/stats/{userId}")
    public Mono<ResponseEntity<PlayerStatsDTO>> getPlayerStats(@PathVariable String userId) {
        return userService.getPlayerStats(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Fetch Batting Stats
    @GetMapping("/user/batting-stats/{userId}")
    public Mono<BattingStatsDTO> getBattingStats(@PathVariable String userId) {
        return userService.getBattingStats(userId);
    }

    // Fetch Bowling Stats
    @GetMapping("/user/bowling-stats/{userId}")
    public Mono<BowlingStatsDTO> getBowlingStats(@PathVariable String userId) {
        return userService.getBowlingStats(userId);
    }

    // Update player stats
    @PutMapping("/user/update-stats/{userId}")
    public Mono<ResponseEntity<User>> updatePlayerStats(
            @PathVariable String userId,
            @RequestBody PlayerStatsDTO playerStatsDTO) {

        return userService.updatePlayerStats(userId, playerStatsDTO)
                .map(user -> ResponseEntity.ok(user))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/stats")
    public Mono<ResponseEntity<List<PlayerStatsDTO>>> getAllPlayerStats() {
        return userService.getAllPlayerStats()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/user/updateTeam/{userId}/{teamId}")
    public Mono<User> updateTeamId(@PathVariable String userId, @PathVariable String teamId) {
        return userService.updateTeamId(userId, teamId);
    }
}

