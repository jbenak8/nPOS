package cz.jbenak.bo.repositories.data;

import cz.jbenak.bo.models.data.VATModel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface VATRepository extends ReactiveCrudRepository<VATModel, Integer> {

    @Query("SELECT * FROM vat WHERE valid_to IS NULL OR valid_to >= CURRENT_DATE ORDER BY vat_type ASC;")
    Flux<VATModel> getValidVAT();

    @Query("SELECT * FROM vat ORDER BY vat_type ASC;")
    Flux<VATModel> getAllVAT();
}
