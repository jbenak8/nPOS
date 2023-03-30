package cz.jbenak.npos.boClient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataOperations extends AbstractClientOperations {

    private final static Logger LOGGER = LogManager.getLogger(DataOperations.class);

    public DataOperations() {
        super("/data");
    }

    public CompletableFuture<List<MeasureUnit>> getAllMeasureUnits() {
        LOGGER.info("List of all measure units will be loaded from server.");
        return httpClientOperations.getData(URI.create(baseURI + "/measure_units/getAll"), new TypeReference<>() {
        });
    }

    public CompletableFuture<CRUDResult> storeMeasureUnit(MeasureUnit unit) {
        LOGGER.info("Given measure unit with ID {} will be saved.", unit.getUnit());
        return httpClientOperations.postData(URI.create(baseURI + "/measure_units/store"), unit);
    }

    public CompletableFuture<CRUDResult> deleteMeasureUnit(String unitId) {
        LOGGER.info("Given measure unit with ID {} will be deleted.", unitId);
        return httpClientOperations.deleteData(URI.create(baseURI + "/measure_units/delete/" + unitId));
    }

    public CompletableFuture<List<Currency>> getAllCurrencies() {
        LOGGER.info("List of all currencies will be loaded from server.");
        return httpClientOperations.getData(URI.create(baseURI + "/currencies/getAll"), new TypeReference<>() {
        });
    }

    public CompletableFuture<CRUDResult> storeCurrency(Currency currency) {
        LOGGER.info("Given currency with ISO code {} will be saved.", currency.getIsoCode());
        return httpClientOperations.postData(URI.create(baseURI + "/currencies/store"), currency);
    }

    public CompletableFuture<CRUDResult> deleteCurrency(String isoCode) {
        LOGGER.info("Given currency with ISO code {} will be deleted.", isoCode);
        return httpClientOperations.deleteData(URI.create(baseURI + "/currencies/delete/" + isoCode));
    }

    public CompletableFuture<List<Country>> getAllCountries() {
        LOGGER.info("List of all countries will be loaded from server.");
        return httpClientOperations.getData(URI.create(baseURI + "/countries/getAll"), new TypeReference<>() {
        });
    }

    public CompletableFuture<CRUDResult> storeCountry(Country country) {
        LOGGER.info("Given country with ISO code {} will be saved.", country.getIsoCode());
        return httpClientOperations.postData(URI.create(baseURI + "/countries/store"), country);
    }

    public CompletableFuture<CRUDResult> deleteCountry(String isoCode) {
        LOGGER.info("Given country with ISO code {} will be deleted.", isoCode);
        return httpClientOperations.deleteData(URI.create(baseURI + "/countries/delete/" + isoCode));
    }

    public CompletableFuture<List<VAT>> getVATList(boolean validOnly) {
        LOGGER.info("Will be loaded a list of VAT: " + (validOnly ? "only valid VAT list." : "list wit all VATs."));
        return httpClientOperations.getData(URI.create(baseURI + "/vat/" + (validOnly ? "getAllValid" : "getAll")), new TypeReference<List<VAT>>() {
        });
    }

    public CompletableFuture<CRUDResult> storeVAT(VAT vat) {
        LOGGER.info("Given VAT {} will be saved.", vat);
        return httpClientOperations.postData(URI.create(baseURI + "/vat/store"), vat);
    }

    public CompletableFuture<CRUDResult> deleteVAT(int VATid) {
        LOGGER.info("Given VAT with VAT ID {} will be deleted.", VATid);
        return httpClientOperations.deleteData(URI.create(baseURI + "/vat/delete/" + VATid));
    }

    public CompletableFuture<List<DocumentNumbering>> getDocumentNumberingSeriesList(boolean validOnly) {
        LOGGER.info("Will be loaded a list of document numbering series: " + (validOnly ? "only valid list." : "list wit all numberings."));
        return httpClientOperations.getData(URI.create(baseURI + "/documentNumberings/" + (validOnly ? "getAllValid" : "getAll")), new TypeReference<List<DocumentNumbering>>() {
        });
    }

    public CompletableFuture<CRUDResult> storeDocumentNumbering(DocumentNumbering numbering) {
        LOGGER.info("Given document numbering {} will be saved.", numbering);
        return httpClientOperations.postData(URI.create(baseURI + "/documentNumberings/store"), numbering);
    }

    public CompletableFuture<CRUDResult> deleteDocumentNumbering(int number) {
        LOGGER.info("Given document numbering with number {} will be deleted.", number);
        return httpClientOperations.deleteData(URI.create(baseURI + "/documentNumberings/delete/" + number));
    }

    public CompletableFuture<List<FinanceOperation>> getAllFinanceOperations() {
        LOGGER.info("List of all finance operations definitions will be loaded from server.");
        return httpClientOperations.getData(URI.create(baseURI + "/financeOperations/getAll"), new TypeReference<>() {
        });
    }

    public CompletableFuture<CRUDResult> storeFinanceOperation(FinanceOperation operation) {
        LOGGER.info("Given finance operations definition {} will be saved.", operation);
        return httpClientOperations.postData(URI.create(baseURI + "/financeOperations/store"), operation);
    }

    public CompletableFuture<CRUDResult> deleteFinanceOperation(int number) {
        LOGGER.info("Given finance operations definition with number {} will be deleted.", number);
        return httpClientOperations.deleteData(URI.create(baseURI + "/financeOperations/delete/" + number));
    }
}
