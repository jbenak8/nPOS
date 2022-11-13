package cz.jbenak.bo.services.data;

import cz.jbenak.bo.models.data.VATModel;
import cz.jbenak.bo.repositories.data.VATRepository;
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

    //invalidateVAT - auto update valid_to ke dnešku
    //save
    //delete

    public VAT modelToVAT(VATModel model) {
        VAT vat = new VAT();
        vat.setId(model.id());
        vat.setType(model.vat_type());
        vat.setName(model.name());
        vat.setValidFrom(model.valid_from());
        vat.setValidTo(model.valid_to());
        return vat;
    }
}
