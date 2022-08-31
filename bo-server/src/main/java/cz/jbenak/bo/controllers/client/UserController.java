package cz.jbenak.bo.controllers.client;

import cz.jbenak.npos.api.client.LoginAttempt;
import cz.jbenak.npos.api.client.LoginStatus;
import cz.jbenak.npos.api.client.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/client/user/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @PostMapping(value = "/login")
    public Mono<LoginStatus> loginClient(@RequestBody LoginAttempt attempt) {
        User u = new User("999");
        return Mono.just(new LoginStatus(LoginStatus.Status.OK, u, 2));//možná flux lepší
    }
}
