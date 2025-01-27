package com.capstone.cricketmatch.repository;

import java.util.Date;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.capstone.cricketmatch.entity.Match;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MatchRepository extends ReactiveMongoRepository<Match,Long>{

    Mono<Match> findByCode(String code);

    Flux<Match> findByDate(Date date);

    Flux<Match> findByLocation(String location);

    Flux<Match> findByStatus(String status);

}
