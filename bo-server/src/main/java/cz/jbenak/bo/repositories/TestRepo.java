package cz.jbenak.bo.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TestRepo extends ReactiveCrudRepository<Test, Integer> {

    @Query("SELECT mesto as city, id FROM adresy WHERE id = :id")
    Mono<Test> getCityById(Integer id);
}
