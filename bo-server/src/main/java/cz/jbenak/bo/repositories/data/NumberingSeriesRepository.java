package cz.jbenak.bo.repositories.data;

import cz.jbenak.bo.models.data.DocumentNumberingModel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NumberingSeriesRepository extends ReactiveCrudRepository<DocumentNumberingModel, Integer> {

    @Query("SELECT DISTINCT ON (document_type) * FROM numbering_series ORDER BY type, valid_from DESC;")
    Flux<DocumentNumberingModel> getValidNumberings();

    @Query("SELECT * FROM numbering_series ORDER BY document_type, valid_from ASC;")
    Flux<DocumentNumberingModel> getAllNumberings();

    @Query("SELECT CASE WHEN count(*) > 0 THEN true ELSE false END AS numbering_exists FROM numbering_series WHERE definition = :definition;")
    Mono<Boolean> numberingDefinitionExists(String definition);
}
