package cz.jbenak.bo.services.data;

import cz.jbenak.bo.models.MeasureUnitModel;
import cz.jbenak.bo.repositories.data.MeasureUnitsRepository;
import cz.jbenak.npos.api.data.MeasureUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MeasureUnitService {

    private static final Logger LOGGER = LogManager.getLogger(MeasureUnitService.class);
    private final MeasureUnitsRepository measureUnitsRepository;

    @Autowired
    public MeasureUnitService(MeasureUnitsRepository measureUnitsRepository) {
        this.measureUnitsRepository = measureUnitsRepository;
    }

    public Flux<MeasureUnit> getMeasureUnits() {
        LOGGER.info("All measure units will be loaded.");
        return measureUnitsRepository.findAll().map(this::modelToUnit);
    }

    public Mono<MeasureUnit> getMeasureUnit(String id) {
        LOGGER.info("Getting specific measure unit with ID {}.", id);
        return measureUnitsRepository.findById(id).map(this::modelToUnit);
    }

    public Mono<MeasureUnit> storeMeasureUnit(MeasureUnit unit) {
        LOGGER.info("Measure unit with following data will be saved: {}", unit);
        return measureUnitsRepository.findById(unit.getUnit())
                .flatMap(model -> {
                    LOGGER.info("This measure unit was found, so there will be done update.");
                    model.setName(unit.getName());
                    model.setBase_unit(unit.getBaseUnit());
                    model.setRatio(unit.getRatio());
                    return measureUnitsRepository.save(model);
                })
                .switchIfEmpty(measureUnitsRepository.save(mapNewModel(unit)))
                .map(this::modelToUnit);
    }

    //TODO: kontrola návaznosti záznamů
    public Mono<Void> deleteMeasureUnit(String id) {
        LOGGER.info("Measure unit with ID {} will be deleted.", id);
        return measureUnitsRepository.deleteById(id);
    }

    private MeasureUnit modelToUnit(MeasureUnitModel model) {
        MeasureUnit unit = new MeasureUnit();
        unit.setUnit(model.getUnit());
        unit.setName(model.getName());
        unit.setBaseUnit(model.getBase_unit());
        unit.setRatio(model.getRatio());
        return unit;
    }

    private MeasureUnitModel mapNewModel(MeasureUnit unit) {
        MeasureUnitModel model = new MeasureUnitModel();
        model.setUnit(unit.getUnit());
        model.setName(unit.getName());
        model.setBase_unit(unit.getBaseUnit());
        model.setRatio(unit.getRatio());
        return model.asNew();
    }
}
