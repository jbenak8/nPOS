package cz.jbenak.bo.services.data;

import cz.jbenak.bo.models.data.CurrencyModel;
import cz.jbenak.bo.repositories.data.CurrencyRepository;
import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.Currency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CurrencyService {

    private final static Logger LOGGER = LogManager.getLogger(CurrencyService.class);
    private CurrencyRepository repository;

    @Autowired
    public void setRepository(CurrencyRepository repository) {
        this.repository = repository;
    }

    public Flux<Currency> getAllCurrencies() {
        LOGGER.info("All currencies will be loaded.");
        return repository.findAll().map(this::modelToCurrency);
    }

    public Mono<Currency> getCurrency(String isoCode) {
        LOGGER.info("Currency with given ISO code '{}' will be loaded.", isoCode);
        return repository.findById(isoCode).map(this::modelToCurrency);
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
                    model.setIso_code(currency.getIsoCode());
                    model.setName(model.getName());
                    model.setSymbol(currency.getSymbol());
                    model.setAcceptable(currency.isAcceptable());
                    model.setMain(currency.isMain());
                    return repository.save(model);
                })
                .switchIfEmpty(repository.save(mapNewModel(currency)))
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

    private Currency modelToCurrency(CurrencyModel model) {
        Currency currency = new Currency();
        currency.setIsoCode(model.getIso_code());
        currency.setName(model.getName());
        currency.setSymbol(model.getSymbol());
        currency.setAcceptable(model.isAcceptable());
        currency.setMain(model.isMain());
        return currency;
    }

    private CurrencyModel mapNewModel(Currency currency) {
        CurrencyModel model = new CurrencyModel();
        model.setIso_code(currency.getIsoCode());
        model.setName(currency.getName());
        model.setSymbol(currency.getSymbol());
        model.setAcceptable(currency.isAcceptable());
        model.setMain(currency.isMain());
        return model.saveAsNew();
    }
}
