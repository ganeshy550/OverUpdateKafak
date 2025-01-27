package com.capstone.cricketmatch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.capstone.cricketmatch.dto.TeamCreationRequest;

import reactor.core.publisher.Mono;

@Service
public class TeamServiceClient {

    private final WebClient webClient;

    public TeamServiceClient(@Value("${team.service.url}") String teamServiceUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(teamServiceUrl).build();
    }

    public Mono<Void> createTeamsForMatch(String matchId, List<String> teamNames, int teamSize) {
        TeamCreationRequest request = new TeamCreationRequest();
        request.setMatchId(matchId);
        request.setTeamNames(teamNames);
        request.setTeamSize(teamSize);

        return webClient.post()
                .uri("/api/teams/create-for-match")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

