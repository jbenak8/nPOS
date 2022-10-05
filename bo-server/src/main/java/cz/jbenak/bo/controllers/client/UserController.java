package cz.jbenak.bo.controllers.client;

import cz.jbenak.bo.services.UserService;
import cz.jbenak.npos.api.client.LoginAttempt;
import cz.jbenak.npos.api.client.LoginStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/client/user/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(value = "/login")
    public Mono<LoginStatus> loginClient(@RequestBody LoginAttempt attempt) {
        return service.doLogin(attempt);
    }

}
