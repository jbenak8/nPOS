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
        //TODO: kontrola platnosti poslední DPH ve skupině a kontrola data platnosti od.
        VATModel savedVAT = repository.save(model).doOnSuccess(saved -> {
                    LOGGER.info("VAT {} was successfully stored under ID {}", vat, saved.id());
                    result.setResultType(CRUDResult.ResultType.OK);
                })
                .doOnError(err -> {
                    LOGGER.error("VAT {} could not be stored because of error: ", vat, err);
                    result.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                    result.setMessage(err.getLocalizedMessage());
                })
                .toFuture().join();
        if (result.getResultType() == CRUDResult.ResultType.OK && savedVAT.id() != vat.getId()) {
            checkAndInvalidateVAT(savedVAT, result);
        }
        return Mono.just(result);
    }

    private void checkAndInvalidateVAT(VATModel newVAT, CRUDResult result) {
        VATModel VATtoInvalidate = repository.getPreviousValidVATbyType(newVAT.vat_type(), newVAT.id()).toFuture().join();
        if (VATtoInvalidate != null) {
            LOGGER.info("VAT with id {} will be invalidated.", VATtoInvalidate.id());
            VATModel invalidated = repository.save(new VATModel(VATtoInvalidate.id(),
                            VATtoInvalidate.vat_type(),
                            VATtoInvalidate.percentage(),
                            VATtoInvalidate.label(),
                            VATtoInvalidate.valid_from(),
                            newVAT.valid_from().minusDays(1)))
                    .toFuture().join();
            if (invalidated != null) {
                result.setResultType(CRUDResult.ResultType.OK);
            } else {
                LOGGER.error("Invalidated VAT {} could not be stored.", VATtoInvalidate);
                result.setResultType(CRUDResult.ResultType.GENERAL_ERROR);
                result.setMessage("DPH, u které by měl být nastaven konec platnosti, jsem nemohl uložit.");
            }
        } else {
            LOGGER.info("VAT to invalidate was not found.");
            result.setResultType(CRUDResult.ResultType.OK);
        }
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
        vat.setLabel(model.label());
        vat.setValidFrom(model.valid_from());
        vat.setValidTo(model.valid_to());
        return vat;
    }

    private VATModel VATtoModel(VAT vat) {
        return new VATModel(vat.getId(), vat.getType(), vat.getPercentage(), vat.getLabel(), vat.getValidFrom(), vat.getValidTo());
    }
}
