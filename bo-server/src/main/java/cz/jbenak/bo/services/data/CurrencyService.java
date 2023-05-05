package cz.jbenak.bo.services.data;

import cz.jbenak.bo.models.data.mappers.CurrencyMapper;
import cz.jbenak.bo.repositories.data.CurrencyRepository;
import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.Currency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CurrencyService {

    private final static Logger LOGGER = LogManager.getLogger(CurrencyService.class);
    private CurrencyRepository repository;
    private final CurrencyMapper mapper = Mappers.getMapper(CurrencyMapper.class);

    @Autowired
    public void setRepository(CurrencyRepository repository) {
        this.repository = repository;
    }

    public Flux<Currency> getAllCurrencies() {
        LOGGER.info("All currencies will be loaded.");
        return repository.getAllOrdered().map(mapper::fromModel);
    }

    public Mono<Currency> getCurrency(String isoCode) {
        LOGGER.info("Currency with given ISO code '{}' will be loaded.", isoCode);
        return repository.findById(isoCode).map(mapper::fromModel);
    }

    public Mono<CRUDResult> storeCurrency(Currency currency) {
        LOGGER.info("Currency with following data will be stored: {}.", currency);
        CRUDResult result = new CRUDResult();
        if (currency.isMain() && repository.mainCurrencyExists().collectList().toFuture().join().isEmpty()) {
            return performStoreOperation(currency, result);
        } else if (currency.isMain() && !repository.mainCurrencyExists().collectList().toFuture().join().isEmpty()) {
            result.setResultType(CRUDResult.ResultType.DATA_CONSTRAINT_VIOLATION);
            result.setMessage("Hlavní měna již byla uložena. Uložení zadané měny nebylo provedeno. Prosím, uložte tuto měnu ne jako hlavní.");
            return Mono.just(result);
        } else {
            return performStoreOperation(currency, result);
        }
    }

    private Mono<CRUDResult> performStoreOperation(Currency currency, CRUDResult result) {
        return repository.findById(currency.getIsoCode())
                .flatMap(model -> {
                    LOGGER.info("This currency has been found, so there will be done update in the database.");
                    return repository.save(mapper.toModel(currency));
                })
                .switchIfEmpty(repository.save(mapper.toModel(currency).saveAsNew()))
                .doOnSuccess(saved -> {
                    LOGGER.info("This currency has been saved successfully.");
                    result.setResultType(CRUDResult.ResultType.OK);
                })
                .thenReturn(result)
                .doOnError(err -> {
                    LOGGER.error("Currency {} has not been saved because of an error: ", currency, err);
                    result.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                    result.setMessage(err.getLocalizedMessage());
                })
                .onErrorReturn(result);
    }

    public Mono<CRUDResult> deleteCurrency(String isoCode) {
        LOGGER.info("Currency with ISO code {} will be deleted.", isoCode);
        //TODO ověření v jiných tabulkách a záznamech.
        CRUDResult result = new CRUDResult();
        return repository.deleteById(isoCode)
                .doOnSuccess(successResult -> {
                    LOGGER.info("Currency with ISO code {} has been deleted successfully.", isoCode);
                    result.setResultType(CRUDResult.ResultType.OK);
                })
                .thenReturn(result)
                .doOnError(err -> {
                    LOGGER.error("Currency with ISO code {} could not be deleted because of an error: ", isoCode, err);
                    result.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                    result.setMessage(err.getLocalizedMessage());
                })
                .onErrorReturn(result);
    }
}
