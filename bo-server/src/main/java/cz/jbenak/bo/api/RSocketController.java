package cz.jbenak.bo.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
public class RSocketController {

    private final List<RSocketRequester> CLIENTS = new ArrayList<>();

    @PreDestroy
    void shutdown() {
        log.info("Disconnecting connected clients. Count: {}.", CLIENTS.size());
        CLIENTS.forEach(client -> Objects.requireNonNull(client.rsocket()).dispose());
        log.info("Clients connections terminated.");
    }
}
