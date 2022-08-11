package cz.jbenak.bo.controllers.client;

import cz.jbenak.npos.api.client.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/client/user/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @PostMapping(value = "/login")
    public Mono<User> loginClient(@RequestParam(value = "userId") String userId,
                                  @RequestParam(value = "password") String password) {
        return Mono.just(new User(""));//možná flux lepší
    }
}
