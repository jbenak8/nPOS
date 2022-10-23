package cz.jbenak.bo.controllers.client;

import cz.jbenak.bo.services.data.MeasureUnitService;
import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.MeasureUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/client/data/", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataController {

    private final MeasureUnitService measureUnitService;

    @Autowired
    public DataController(MeasureUnitService measureUnitService) {
        this.measureUnitService = measureUnitService;
    }

    @GetMapping("/measure_units/getAll")
    public Flux<MeasureUnit> getAllMeasureUnits() {
        return measureUnitService.getMeasureUnits();
    }

    @PostMapping(value = "/measure_units/store")
    public Mono<CRUDResult> storeMeasureUnit(MeasureUnit unit) {
        return measureUnitService.storeMeasureUnit(unit);
    }
}
