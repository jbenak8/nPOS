package cz.jbenak.bo.services.data;

import cz.jbenak.bo.models.data.mappers.FinanceOperationMapper;
import cz.jbenak.bo.repositories.data.FinanceOperationRepository;
import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.FinanceOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FinanceOperationService {

    private final static Logger LOGGER = LogManager.getLogger(FinanceOperationService.class);
    private final FinanceOperationMapper mapper = Mappers.getMapper(FinanceOperationMapper.class);
    private FinanceOperationRepository repository;

    @Autowired
    public void setRepository(FinanceOperationRepository repository) {
        this.repository = repository;
    }

    public Flux<FinanceOperation> getAllFinanceOperations() {
        LOGGER.info("All finance operation definitions will be loaded.");
        return repository.findAll().map(mapper::fromModel);
    }

    public Mono<FinanceOperation> getFinanceOperationByNumber(int number) {
        LOGGER.info("Will be loaded finance operation definition with number {}.", number);
        return repository.findById(number).map(mapper::fromModel);
    }

    public Mono<CRUDResult> saveFinanceOperation(FinanceOperation operation) {
        LOGGER.info("Finance operation definition {} will be stored.", operation);
        CRUDResult result = new CRUDResult();
        return repository.save(mapper.toModel(operation))
                .doOnSuccess(saved -> {
                    LOGGER.info("Finance operation definition {} was successfully stored under number {}.", operation, saved.id());
                    result.setResultType(CRUDResult.ResultType.OK);
                })
                .thenReturn(result)
                .doOnError(err -> {
                    LOGGER.error("Finance operation definition {} could not be stored because of error: ", operation, err);
                    result.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                    result.setMessage(err.getLocalizedMessage());
                })
                .onErrorReturn(result);
    }

    public Mono<CRUDResult> deleteFinanceOperation(int number) {
        LOGGER.info("Finance operation definition with number '{}' will be deleted.", number);
        CRUDResult result = new CRUDResult();
        return repository.deleteById(number)
                .doOnSuccess(deleted -> {
                    LOGGER.info("Finance operation definition with number'{}' has been deleted.", number);
                    result.setResultType(CRUDResult.ResultType.OK);
                })
                .thenReturn(result)
                .doOnError(err -> {
                    LOGGER.error("Finance operation definition with number '{}' has not been deleted because of an error.", number, err);
                    result.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                    result.setMessage(err.getLocalizedMessage());
                })
                .onErrorReturn(result);
    }
}
