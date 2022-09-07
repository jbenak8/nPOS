package cz.jbenak.bo.repositories.user;

import cz.jbenak.bo.models.BoUser;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<BoUser, Integer> {

    @Query("SELECT * FROM users WHERE id = :userId")
    Mono<BoUser> getUserByUserId(int userId);

    @Query("SELECT * FROM users ORDER BY id ASC ")
    Flux<BoUser> getAllUsers();

    @Query("UPDATE users SET rest_login_attempts = (rest_login_attempts - 1) WHERE id = :userId")
    Mono<BoUser> decreasePasswordAttemptAndReturn(int userId);

    @Query("UPDATE users SET locked = true WHERE id = :userId")
    Mono<BoUser> lockUserAndReturn(int userId);

    @Query("UPDATE users SET locked = false, rest_login_attempts = 3 WHERE id = :userId")
    Mono<BoUser> unlockUserAndReturn(int userId);

    @Query("UPDATE users SET last_login_timestamp = LOCALTIMESTAMP WHERE id = :userId")
    Mono<BoUser> setLoginTimestampAndReturn(int userId);

    @Query("SELECT id FROM users WHERE id = :userId AND password = crypt(:password, users.password);")
    Mono<Integer> checkPassword(int userId, String password);
}
