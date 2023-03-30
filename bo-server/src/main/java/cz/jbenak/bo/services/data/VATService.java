package cz.jbenak.bo.services.data;

import cz.jbenak.bo.models.data.VATModel;
import cz.jbenak.bo.repositories.data.VATRepository;
import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.VAT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class VATService {

    private final static Logger LOGGER = LogManager.getLogger(VATService.class);
    private VATRepository repository;

    @Autowired
    public void setRepository(VATRepository repository) {
        this.repository = repository;
    }

    public Flux<VAT> getAllVAT() {
        LOGGER.info("All VAT (including invalid) will be loaded.");
        return repository.getAllVAT().map(this::modelToVAT);
    }

    public Flux<VAT> getAllValidVAT() {
        LOGGER.info("All valid VAT will be loaded.");
        return repository.getValidVAT().map(this::modelToVAT);
    }

    public Mono<VAT> getVATbyId(int id) {
        LOGGER.info("VAT with ID {} will be loaded.", id);
        return repository.findById(id).map(this::modelToVAT);
    }

    public Mono<CRUDResult> saveVAT(VAT vat) {
        LOGGER.info("VAT {} will be stored.", vat);
        CRUDResult result = new CRUDResult();
        VATModel model = VATtoModel(vat);
        if (vat.getValidFrom().isBefore(LocalDate.now())) {
            LOGGER.error("VAT {} could not be stored because valid from is in past.", vat);
            result.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
            result.setMessage("Server odmítl uložit DPH - datum platnosti od je v minulosti.");
            return Mono.just(result);
        }
        if (repository.getVatCountByVATType(vat.getType(), vat.getValidFrom()).toFuture().join() > 0) {
            LOGGER.error("VAT {} could not be stored because selected VAT type with selected validity already exists.", vat);
            result.setResultType(CRUDResult.ResultType.ALREADY_EXISTS);
            result.setMessage("Server odmítl uložit DPH - již existuje DPH ve vybraném typu sazby se stejným datem platnosti Od.");
            return Mono.just(result);
        }
        return repository.save(model).doOnSuccess(saved -> {
                    LOGGER.info("VAT {} was successfully stored under ID {}", vat, saved.id());
                    result.setResultType(CRUDResult.ResultType.OK);
                })
                .thenReturn(result)
                .doOnError(err -> {
                    LOGGER.error("VAT {} could not be stored because of error: ", vat, err);
                    result.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                    result.setMessage(err.getLocalizedMessage());
                })
                .onErrorReturn(result);
    }

    public Mono<CRUDResult> deleteVAT(int id) {
        LOGGER.info("VAT with ID {} will be deleted.", id);
        CRUDResult result = new CRUDResult();
        return repository.deleteById(id).doOnSuccess(deleted -> {
                    LOGGER.info("VAT with ID '{}' has been deleted.", id);
                    result.setResultType(CRUDResult.ResultType.OK);
                }).thenReturn(result)
                .doOnError(err -> {
                    LOGGER.error("VAT with ID '{}' has not been deleted because of an error.", id, err);
                    result.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                    result.setMessage(err.getLocalizedMessage());
                })
                .onErrorReturn(result);
    }

    private VAT modelToVAT(VATModel model) {
        VAT vat = new VAT();
        vat.setId(model.id());
        vat.setType(model.vat_type());
        vat.setPercentage(model.percentage());
        vat.setValidFrom(model.valid_from());
        return vat;
    }

    private VATModel VATtoModel(VAT vat) {
        return new VATModel(
                vat.getId(),
                vat.getType(),
                vat.getPercentage(),
                vat.getValidFrom() == null ? LocalDate.now() : vat.getValidFrom());
    }
}
