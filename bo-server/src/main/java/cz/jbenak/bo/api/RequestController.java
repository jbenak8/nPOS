package cz.jbenak.bo.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RequestController {
    
    @GetMapping("/")
    Mono<String> getHttpStatus() {
        return Mono.just("{\"status\":200}");
    }
}
