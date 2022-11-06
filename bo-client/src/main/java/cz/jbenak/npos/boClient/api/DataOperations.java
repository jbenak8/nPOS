package cz.jbenak.npos.boClient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.MeasureUnit;
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
}
