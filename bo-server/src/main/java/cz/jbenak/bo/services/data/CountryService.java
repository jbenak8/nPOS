package cz.jbenak.bo.services.data;

import cz.jbenak.bo.models.data.CountryModel;
import cz.jbenak.bo.models.data.mappers.CountryMapper;
import cz.jbenak.bo.repositories.data.CountryRepository;
import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.Country;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CountryService {

    private final static Logger LOGGER = LogManager.getLogger(CountryService.class);
    private CountryRepository repository;
    private final CountryMapper mapper = Mappers.getMapper(CountryMapper.class);

    @Autowired
    public void setRepository(CountryRepository repository) {
        this.repository = repository;
    }

    public Flux<Country> getAllCountries() {
        LOGGER.info("List of all countries will be loaded.");
        return repository.getAllOrdered().map(mapper::fromModel);
    }

    public Mono<Country> getCountry(String isCode) {
        LOGGER.info("Country with ISO code {} will be loaded", isCode);
        return repository.findById(isCode).map(mapper::fromModel);
    }

    public Mono<CRUDResult> storeCountry(Country country) {
        LOGGER.info("Country with following data will be stored: {}.", country);
        CRUDResult result = new CRUDResult();
        if (country.isMain() && repository.mainCountryExists().collectList().toFuture().join().isEmpty()) {
            return performStoreOperation(country, result);
        } else if (country.isMain() && !repository.mainCountryExists().collectList().toFuture().join().isEmpty()) {
            result.setResultType(CRUDResult.ResultType.DATA_CONSTRAINT_VIOLATION);
            result.setMessage("Hlavní země již byla uložena. Uložení zadané měny nebylo provedeno. Prosím, uložte tuto zemi ne jako hlavní.");
            return Mono.just(result);
        } else {
            return performStoreOperation(country, result);
        }
    }

    public Mono<CRUDResult> deleteCountry(String isoCode) {
        LOGGER.info("Country with ISO code {} will be deleted.", isoCode);
        //TODO ověření v jiných tabulkách a záznamech.
        CRUDResult result = new CRUDResult();
        return repository.deleteById(isoCode)
                .doOnSuccess(successResult -> {
                    LOGGER.info("Country with ISO code {} has been deleted successfully.", isoCode);
                    result.setResultType(CRUDResult.ResultType.OK);
                })
                .thenReturn(result)
                .doOnError(err -> {
                    LOGGER.error("Country with ISO code {} could not be deleted because of an error: ", isoCode, err);
                    result.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                    result.setMessage(err.getLocalizedMessage());
                })
                .onErrorReturn(result);
    }

    private Mono<CRUDResult> performStoreOperation(Country country, CRUDResult result) {
        return repository.findById(country.getIsoCode())
                .flatMap(model -> {
                    LOGGER.info("This country has been found, so there will be done update in the database.");
                    model.setIso_code(country.getIsoCode());
                    model.setCommon_name(country.getCommonName());
                    model.setFull_name(country.getFullName());
                    model.setCurrency_code(country.getCurrencyIsoCode());
                    model.setMain(country.isMain());
                    return repository.save(model);
                })
                .switchIfEmpty(repository.save(mapper.toModel(country).saveAsNew()))
                .doOnSuccess(saved -> {
                    LOGGER.info("This country has been saved successfully.");
                    result.setResultType(CRUDResult.ResultType.OK);
                })
                .thenReturn(result)
                .doOnError(err -> {
                    LOGGER.error("Country {} has not been saved because of an error: ", country, err);
                    result.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                    result.setMessage(err.getLocalizedMessage());
                })
                .onErrorReturn(result);
    }
}
