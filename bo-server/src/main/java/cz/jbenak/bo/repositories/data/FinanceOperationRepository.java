package cz.jbenak.bo.repositories.data;

import cz.jbenak.bo.models.data.FinanceOperationModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceOperationRepository extends ReactiveCrudRepository<FinanceOperationModel, Integer> {
}
