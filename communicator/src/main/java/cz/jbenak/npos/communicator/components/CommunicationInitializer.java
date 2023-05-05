package cz.jbenak.npos.communicator.components;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class CommunicationInitializer {

    private static final Logger LOGGER = LogManager.getLogger(CommunicationInitializer.class);


    @PostConstruct
    public void init() {
        LOGGER.info("Communication will be started.");
        System.out.println("test");
    }
}
