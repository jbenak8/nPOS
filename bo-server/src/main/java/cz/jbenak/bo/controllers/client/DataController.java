package cz.jbenak.bo.controllers.client;

import cz.jbenak.bo.services.data.MeasureUnitService;
import cz.jbenak.npos.api.data.MeasureUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/measure_units/get/{id}")
    public Mono<MeasureUnit> getMeasureUnit(@PathVariable String id) {
        return measureUnitService.getMeasureUnit(id);
    }

    @PostMapping(value = "/measure_units/store")
    public Mono<MeasureUnit> storeMeasureUnit(@RequestBody MeasureUnit unit) {
        return measureUnitService.storeMeasureUnit(unit);
    }

    @DeleteMapping(value = "/measure_units/delete/{id}")
    public Mono<Void> deleteMeasureUnit(@PathVariable String id) {
        return measureUnitService.deleteMeasureUnit(id);
    }
}
