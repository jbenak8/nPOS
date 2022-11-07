package cz.jbenak.bo.controllers.client;

import cz.jbenak.bo.services.data.CurrencyService;
import cz.jbenak.bo.services.data.MeasureUnitService;
import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.Currency;
import cz.jbenak.npos.api.data.MeasureUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/client/data/", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataController {

    private MeasureUnitService measureUnitService;
    private CurrencyService currencyService;

    @Autowired
    public void setMeasureUnitService(MeasureUnitService measureUnitService) {
        this.measureUnitService = measureUnitService;
    }

    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/measure_units/getAll")
    public Flux<MeasureUnit> getAllMeasureUnits() {
        return measureUnitService.getMeasureUnits();
    }

    @GetMapping("/measure_units/get/{id}")
    public Mono<MeasureUnit> getMeasureUnit(@PathVariable String id) {
        return measureUnitService.getMeasureUnit(id);
    }

    @PostMapping(value = "/measure_units/store")
    public Mono<CRUDResult> storeMeasureUnit(@RequestBody MeasureUnit unit) {
        return measureUnitService.storeMeasureUnit(unit);
    }

    @DeleteMapping("/measure_units/delete/{id}")
    public Mono<CRUDResult> deleteMeasureUnit(@PathVariable String id) {
        return measureUnitService.deleteMeasureUnit(id);
    }

    @GetMapping("/currencies/getAll")
    public Flux<Currency> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @GetMapping("/currencies/get/{isoCode}")
    public Mono<Currency> getCurrency(@PathVariable String isoCode) {
        return currencyService.getCurrency(isoCode);
    }

    @PostMapping("/currencies/store")
    public Mono<CRUDResult> storeCurrency(@RequestBody Currency currency) {
        return currencyService.storeCurrency(currency);
    }

    @DeleteMapping("/currencies/delete/{isoCode}")
    public Mono<CRUDResult> deleteCurrency(@PathVariable String isoCode) {
        return currencyService.deleteCurrency(isoCode);
    }

}
