package cz.jbenak.bo.repositories.user;

import cz.jbenak.bo.models.BoUser;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<BoUser, Integer> {

    @Query("SELECT id FROM users WHERE id = :userId AND password = crypt(:password, users.password);")
    Mono<Integer> checkPassword(int userId, String password);
}
