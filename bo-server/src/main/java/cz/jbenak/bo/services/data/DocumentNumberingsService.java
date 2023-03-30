package cz.jbenak.bo.services.data;

import cz.jbenak.bo.models.data.DocumentNumberingModel;
import cz.jbenak.bo.repositories.data.NumberingSeriesRepository;
import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.DocumentNumbering;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DocumentNumberingsService {

    private final Logger LOGGER = LogManager.getLogger(DocumentNumberingsService.class);
    private NumberingSeriesRepository repository;

    @Autowired
    public void setRepository(NumberingSeriesRepository repository) {
        this.repository = repository;
    }

    public Flux<DocumentNumbering> getAllNumberings() {
        LOGGER.info("All definitions for document numberings will be loaded.");
        return repository.getAllNumberings().map(this::modelToNumbering);
    }

    public Flux<DocumentNumbering> getAllValidNumberings() {
        LOGGER.info("Will be loaded all valid definitions for document numberings.");
        return repository.getValidNumberings().map(this::modelToNumbering);
    }

    public Mono<DocumentNumbering> getNumberingByNumber(int number) {
        LOGGER.info("Document numbering with number {} will be loaded.", number);
        return repository.findById(number).map(this::modelToNumbering);
    }

    public Mono<CRUDResult> saveNumbering(DocumentNumbering numbering) {
        LOGGER.info("Document numbering {} will be saved.", numbering);
        CRUDResult result = new CRUDResult();
        DocumentNumberingModel model = numberingToModel(numbering);
        if (repository.numberingDefinitionExists(numbering.getDefinition()).toFuture().join()) {
            LOGGER.error("Document numbering definition {} could not be stored because definition already exists.", numbering);
            result.setResultType(CRUDResult.ResultType.ALREADY_EXISTS);
            result.setMessage("Server odmítl uložit definici číselné řady - tato definice již v databázi existuje.");
            return Mono.just(result);
        }
        return repository.save(model).doOnSuccess(saved -> {
                    LOGGER.info("Document numbering {} was successfully stored under sequence number {}", numbering, saved.id());
                    result.setResultType(CRUDResult.ResultType.OK);
                })
                .thenReturn(result)
                .doOnError(err -> {
                    LOGGER.error("Document numbering '{}' could not be stored because of an error.", numbering, err);
                    result.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                    result.setMessage(err.getLocalizedMessage());
                })
                .onErrorReturn(result);
    }

    //TODO kontrola navazujících dokladů - tohle všude.
    public Mono<CRUDResult> deleteNumbering(int number) {
        LOGGER.info("Document numbering with number {} will be deleted.", number);
        CRUDResult result = new CRUDResult();
        return repository.deleteById(number).doOnSuccess(deleted -> {
                    LOGGER.info("Document numbering with number '{}' has been deleted.", number);
                    result.setResultType(CRUDResult.ResultType.OK);
                }).thenReturn(result)
                .doOnError(err -> {
                    LOGGER.error("Document numbering with number '{}' has not been deleted because of an error.", number, err);
                    result.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                    result.setMessage(err.getLocalizedMessage());
                })
                .onErrorReturn(result);
    }

    private DocumentNumbering modelToNumbering(DocumentNumberingModel model) {
        DocumentNumbering numbering = new DocumentNumbering();
        numbering.setId(model.id());
        numbering.setDocumentType(model.document_type());
        numbering.setDefinition(model.definition());
        numbering.setSequenceNumberLength(model.sequence_number_length());
        numbering.setValidFrom(model.valid_from());
        numbering.setStartFrom(model.start_serial_from());
        return numbering;
    }

    private DocumentNumberingModel numberingToModel(DocumentNumbering numbering) {
        return new DocumentNumberingModel(numbering.getId(),
                numbering.getDefinition(),
                numbering.getDocumentType(),
                numbering.getSequenceNumberLength(),
                numbering.getValidFrom(),
                numbering.getStartFrom());
    }
}
