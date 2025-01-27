package com.capstone.teams.repository;

import com.capstone.teams.entity.Team;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;


public interface TeamRepository extends ReactiveMongoRepository<Team, Long> {
    Flux<Team> findAllByMatchId(String matchId);
}
