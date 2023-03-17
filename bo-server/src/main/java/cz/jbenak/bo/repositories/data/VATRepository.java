package cz.jbenak.bo.repositories.data;

import cz.jbenak.bo.models.data.VATModel;
import cz.jbenak.npos.api.data.VAT;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface VATRepository extends ReactiveCrudRepository<VATModel, Integer> {

    @Query("SELECT DISTINCT ON (vat_type) * FROM vat ORDER BY vat_type, valid_from DESC;")
    Flux<VATModel> getValidVAT();

    @Query("SELECT * FROM vat ORDER BY vat_type, valid_from ASC;")
    Flux<VATModel> getAllVAT();

    @Query("SELECT count(*) FROM vat WHERE vat_type = :type AND valid_from = :validFrom;")
    Mono<Integer> getVatCountByVATType(VAT.Type type, LocalDate validFrom);

}
