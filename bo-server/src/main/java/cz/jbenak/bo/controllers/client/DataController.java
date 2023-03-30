package cz.jbenak.bo.controllers.client;

import cz.jbenak.bo.services.data.*;
import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.*;
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
    private CountryService countryService;
    private VATService vatService;
    private DocumentNumberingsService documentNumberingsService;
    private FinanceOperationService financeOperationService;

    @Autowired
    public void setMeasureUnitService(MeasureUnitService measureUnitService) {
        this.measureUnitService = measureUnitService;
    }

    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Autowired
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Autowired
    public void setVatService(VATService vatService) {
        this.vatService = vatService;
    }

    @Autowired
    public void setDocumentNumberingsService(DocumentNumberingsService documentNumberingsService) {
        this.documentNumberingsService = documentNumberingsService;
    }

    @Autowired
    public void setFinanceOperationService(FinanceOperationService financeOperationService) {
        this.financeOperationService = financeOperationService;
    }

    // Measure units
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

    // Currencies
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

    // Countries
    @GetMapping("/countries/getAll")
    public Flux<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    @GetMapping("/countries/get/{isoCode}")
    public Mono<Country> getCountry(@PathVariable String isoCode) {
        return countryService.getCountry(isoCode);
    }

    @PostMapping("/countries/store")
    public Mono<CRUDResult> storeCountry(@RequestBody Country country) {
        return countryService.storeCountry(country);
    }

    @DeleteMapping("/countries/delete/{isoCode}")
    public Mono<CRUDResult> deleteCountry(@PathVariable String isoCode) {
        return countryService.deleteCountry(isoCode);
    }

    // VAT
    @GetMapping("/vat/getAll")
    public Flux<VAT> getAllVAT() {
        return vatService.getAllVAT();
    }

    @GetMapping("/vat/getAllValid")
    public Flux<VAT> getAllValidVAT() {
        return vatService.getAllValidVAT();
    }

    @GetMapping("/vat/get/{id}")
    public Mono<VAT> getVATbyID(@PathVariable int id) {
        return vatService.getVATbyId(id);
    }

    @PostMapping("/vat/store")
    public Mono<CRUDResult> storeVAT(@RequestBody VAT vat) {
        return vatService.saveVAT(vat);
    }

    @DeleteMapping("/vat/delete/{id}")
    public Mono<CRUDResult> deleteVAT(@PathVariable int id) {
        return vatService.deleteVAT(id);
    }

    // Document numbering series
    @GetMapping("/documentNumberings/getAll")
    public Flux<DocumentNumbering> getAllNumberings() {
        return documentNumberingsService.getAllNumberings();
    }

    @GetMapping("/documentNumberings/getAllValid")
    public Flux<DocumentNumbering> getAllValidNumberings() {
        return documentNumberingsService.getAllValidNumberings();
    }

    @GetMapping("/documentNumbering/get/{number}")
    public Mono<DocumentNumbering> getNumberingByNumber(@PathVariable int number) {
        return documentNumberingsService.getNumberingByNumber(number);
    }

    @PostMapping("/documentNumberings/store")
    public Mono<CRUDResult> storeNumbering(@RequestBody DocumentNumbering numbering) {
        return documentNumberingsService.saveNumbering(numbering);
    }

    @DeleteMapping("/documentNumberings/delete/{number}")
    public Mono<CRUDResult> deleteNumbering(@PathVariable int number) {
        return documentNumberingsService.deleteNumbering(number);
    }

    @GetMapping("/financeOperations/getAll")
    public Flux<FinanceOperation> getAllFinanceOperations() {
        return financeOperationService.getAllFinanceOperations();
    }

    @GetMapping("/financeOperations/get/{number}")
    public Mono<FinanceOperation> getFinanceOperationByNumber(@PathVariable int number) {
        return financeOperationService.getFinanceOperationByNumber(number);
    }

    @PostMapping("/financeOperations/store")
    public Mono<CRUDResult> storeFinanceOperation(@RequestBody FinanceOperation operation) {
        return financeOperationService.saveFinanceOperation(operation);
    }

    @DeleteMapping("/financeOperations/delete/{number}")
    public Mono<CRUDResult> deleteFinanceOperation(@PathVariable int number) {
        return financeOperationService.deleteFinanceOperation(number);
    }

}
