package cz.jbenak.bo.repositories.user;

import cz.jbenak.bo.models.user.UserGroup;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GroupRepository extends ReactiveCrudRepository<UserGroup, Integer> {

    @Query("SELECT * FROM user_groups WHERE id = :userId")
    Flux<UserGroup> getUserGroupsByUserId(int userId);
}
