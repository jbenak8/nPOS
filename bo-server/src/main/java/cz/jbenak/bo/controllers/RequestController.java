package cz.jbenak.bo.controllers;

import cz.jbenak.bo.repositories.Test;
import cz.jbenak.bo.repositories.TestRepo;
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

    @Value("${boclient.connection.usename}")
    private String clientName;

    @Autowired
    TestRepo tr;

    @GetMapping("/test")
    Mono<String> getHttpStatus() {
        return Mono.just("{\"status\":200}"+clientName);
    }

    @GetMapping("/testdb")
    Mono<Test> getTestDb() {
        return tr.getCityById(2);
    }
}
