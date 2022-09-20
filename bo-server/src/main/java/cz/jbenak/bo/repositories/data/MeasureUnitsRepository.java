package cz.jbenak.bo.repositories.data;

import cz.jbenak.bo.models.MeasureUnitModel;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MeasureUnitsRepository extends ReactiveCrudRepository<MeasureUnitModel, String> {

    @Query("SELECT * FROM measure_units WHERE base_unit = :baseUnit ORDER BY unit ASC;")
    public Flux<MeasureUnitModel> getUnitsByBaseUnit(String baseUnit);
}
