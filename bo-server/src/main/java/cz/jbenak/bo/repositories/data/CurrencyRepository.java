package cz.jbenak.bo.repositories.data;

import cz.jbenak.bo.models.data.CurrencyModel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CurrencyRepository extends ReactiveCrudRepository<CurrencyModel, String> {

    @Query("SELECT * FROM currencies ORDER BY iso_code;")
    Flux<CurrencyModel> getAllOrdered();

    @Query("SELECT * FROM currencies WHERE main = true;")
    Flux<CurrencyModel> mainCurrencyExists();
}
