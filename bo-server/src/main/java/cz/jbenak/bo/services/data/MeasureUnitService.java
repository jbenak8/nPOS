package cz.jbenak.bo.services.data;

import cz.jbenak.bo.repositories.data.MeasureUnitsRepository;
import cz.jbenak.npos.api.data.MeasureUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MeasureUnitService {

    private static final Logger LOGGER = LogManager.getLogger(MeasureUnitService.class);
    private final MeasureUnitsRepository measureUnitsRepository;

    public MeasureUnitService(MeasureUnitsRepository measureUnitsRepository) {
        this.measureUnitsRepository = measureUnitsRepository;
    }

    public Flux<MeasureUnit> getMeasureUnits() {
        LOGGER.info("All measure units will be loaded.");
        return measureUnitsRepository.findAll().map(model -> {
            MeasureUnit unit = new MeasureUnit();
            unit.setUnit(model.unit());
            unit.setName(model.name());
            unit.setBaseUnit(model.base_unit());
            unit.setRatio(model.ratio());
            return unit;
        });
    }
}
