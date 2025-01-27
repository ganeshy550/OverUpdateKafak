// PlayerRepository.java
package com.capstone.Players.repository;


//import com.capstone.playermicroservice.model.Player;
import com.capstone.Players.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByUserEmail(String userEmail);

    Mono<User> findByUserName(String userName);

    Mono<User> findByUserId(String userId);
}