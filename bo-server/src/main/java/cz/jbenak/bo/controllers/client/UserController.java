package cz.jbenak.bo.controllers.client;

import cz.jbenak.bo.models.BoUser;
import cz.jbenak.bo.repositories.user.UserRepository;
import cz.jbenak.npos.api.client.LoginAttempt;
import cz.jbenak.npos.api.client.LoginStatus;
import cz.jbenak.npos.api.client.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/client/user/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/login")
    public Mono<LoginStatus> loginClient(@RequestBody LoginAttempt attempt) {
        int id = attempt.userId();
        LOGGER.info("Login for user with id {} requested.", id);
        String password = attempt.password();
        BoUser user = userRepository.getUserByUserId(id).toFuture().join();
        if (user == null) {
            LOGGER.info("User with ID {} has not been found in the database.", id);
            return Mono.just(new LoginStatus(LoginStatus.Status.ID_UNKNOWN, null));
        }
        LOGGER.debug("User {} - {} {} has been found in the database. Will be checked password for user him.",
                user.id(), user.user_name(), user.user_surname());
        int passwordCheckedUser = userRepository.checkPassword(id, password).toFuture().join();
        if (passwordCheckedUser == 0) {
            LOGGER.warn("Password for user {} is not correct. Login attempts will be decreased.", id);
            user = userRepository.decreasePasswordAttemptAndReturn(id).toFuture().join();
            if (user.rest_login_attempts() == 0) {
                LOGGER.warn("User {} will be locked.", id);
                user = userRepository.lockUserAndReturn(id).toFuture().join();
            }
            return Mono.just(new LoginStatus(LoginStatus.Status.FAILED, mapBoUserToApiUser(user)));
        }
        if (user.locked()) {
            LOGGER.warn("User {} was locked.", id);
            return Mono.just(new LoginStatus(LoginStatus.Status.USER_LOCKED, mapBoUserToApiUser(user)));
        }
        user = userRepository.setLoginTimestampAndReturn(id).toFuture().join();
        return Mono.just(new LoginStatus(LoginStatus.Status.OK, mapBoUserToApiUser(user)));
    }

    private User mapBoUserToApiUser(BoUser boUser) {
        LOGGER.debug("BO User {} will be mapped for API.", boUser);
        User user = new User();
        user.setUserId(boUser.id());
        user.setUserName(boUser.user_name());
        user.setUserSurname(boUser.user_surname());
        user.setMail(boUser.mail());
        user.setPhone(boUser.phone());
        user.setNote(boUser.note());
        user.setUserLocked(boUser.locked());
        user.setUserLocked(boUser.locked());
        user.setLastLoginTimestamp(boUser.last_login_timestamp());
        user.setRestLoginAttempts(boUser.rest_login_attempts());
        return user;
    }
}
