package cz.jbenak.bo.repositories.functions;

import cz.jbenak.bo.models.Function;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface FunctionsRepository extends ReactiveCrudRepository<Function, String> {

    @Query("SELECT * FROM functions ORDER BY id ASC;")
    Flux<Function> getAllFunctions();
}
