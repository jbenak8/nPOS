package cz.jbenak.bo.services.data;

import cz.jbenak.bo.models.data.MeasureUnitModel;
import cz.jbenak.bo.repositories.data.MeasureUnitsRepository;
import cz.jbenak.npos.api.client.CRUDResult;
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

    public Mono<CRUDResult> storeMeasureUnit(MeasureUnit unit) {
        LOGGER.info("Measure unit with following data will be saved: {}", unit);
        CRUDResult resultOK = new CRUDResult();
        CRUDResult resultError = new CRUDResult();
        return measureUnitsRepository.findById(unit.getUnit())
                .flatMap(model -> {
                    LOGGER.info("This measure unit was found, so there will be done update.");
                    model.setName(unit.getName());
                    model.setBase_unit(unit.getBaseUnit());
                    model.setRatio(unit.getRatio());
                    return measureUnitsRepository.save(model);
                })
                .switchIfEmpty(measureUnitsRepository.save(mapNewModel(unit)))
                .doOnSuccess(saved -> {
                    LOGGER.info("Measure unit with number '{}' has been deleted.", saved.getUnit());
                    resultOK.setResultType(CRUDResult.ResultType.OK);
                })
                .thenReturn(resultOK)
                .doOnError(err -> {
                    LOGGER.error("Measure unit with ID '{}' has not been deleted because of an error.", unit.getUnit(), err);
                    resultError.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                    resultError.setMessage(err.getLocalizedMessage());
                })
                .onErrorReturn(resultError);
    }

    //TODO: kontrola návaznosti záznamů
    public Mono<CRUDResult> deleteMeasureUnit(String id) {
        LOGGER.info("Measure unit with ID {} will be deleted.", id);
        if(measureUnitsRepository.getUnitsByBaseUnit(id).collectList().toFuture().join().isEmpty()) {
            CRUDResult resultOK = new CRUDResult();
            CRUDResult resultError = new CRUDResult();
            return measureUnitsRepository.deleteById(id)
                    .doOnSuccess(result -> {
                        LOGGER.info("Measure unit with number '{}' has been deleted.", id);
                        resultOK.setResultType(CRUDResult.ResultType.OK);
                    })
                    .thenReturn(resultOK)
                    .doOnError(err -> {
                        LOGGER.error("Measure unit with ID '{}' has not been deleted because of an error.", id, err);
                        resultError.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                        resultError.setMessage(err.getLocalizedMessage());
                    })
                    .onErrorReturn(resultError);
        } else {
            CRUDResult itemsExists = new CRUDResult();
            itemsExists.setResultType(CRUDResult.ResultType.HAS_BOUND_RECORDS);
            return Mono.just(itemsExists);
        }
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
