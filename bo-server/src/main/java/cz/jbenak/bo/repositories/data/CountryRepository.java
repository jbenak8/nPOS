package cz.jbenak.bo.repositories.data;

import cz.jbenak.bo.models.data.CountryModel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CountryRepository extends ReactiveCrudRepository<CountryModel, String> {

    @Query("SELECT * FROM countries WHERE main = true;")
    Flux<CountryModel> mainCountryExists();
}
