package cz.jbenak.bo.controllers;

import cz.jbenak.bo.repositories.Test;
import cz.jbenak.bo.repositories.TestRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class RequestController {

    Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Value("${boclient.connection.usename}")
    private String clientName;

    @Autowired
    TestRepo tr;

    @GetMapping("/test")
    Mono<String> getHttpStatus() {
        logger.info("Status request requested.");
        return Mono.just("{\"status\":200}"+clientName);
    }

    @GetMapping("/testdb")
    Mono<Test> getTestDb() {
        logger.info("Database test requested.");
        return tr.getCityById(2);
    }
}
