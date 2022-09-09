package cz.jbenak.bo.services;

import cz.jbenak.bo.models.BoUser;
import cz.jbenak.bo.models.Function;
import cz.jbenak.bo.models.UserGroup;
import cz.jbenak.bo.repositories.functions.FunctionsRepository;
import cz.jbenak.bo.repositories.user.GroupRepository;
import cz.jbenak.bo.repositories.user.UserRepository;
import cz.jbenak.npos.api.client.LoginAttempt;
import cz.jbenak.npos.api.client.LoginStatus;
import cz.jbenak.npos.api.client.User;
import cz.jbenak.npos.api.shared.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static cz.jbenak.npos.api.client.LoginStatus.Status.*;

@Service
public class UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private final Utils apiUtils = new Utils();
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final FunctionsRepository functionsRepository;

    public UserService(UserRepository userRepository, GroupRepository groupRepository, FunctionsRepository functionsRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.functionsRepository = functionsRepository;
    }

    public Mono<LoginStatus> doLogin(LoginAttempt attempt) {
        final StringEncryptor encryptor = apiUtils.getStringEncryptor();
        int id = attempt.userId();
        LOGGER.info("Login for user with id {} requested.", id);
        String password = encryptor.decrypt(attempt.password());
        BoUser user = userRepository.findById(id).toFuture().join();
        if (user == null) {
            LOGGER.info("User with ID {} has not been found in the database.", id);
            return Mono.just(new LoginStatus(ID_UNKNOWN, null));
        }
        LOGGER.debug("User {} - {} {} has been found in the database. Will be checked locking status and password for user him.",
                user.getId(), user.getUser_name(), user.getUser_surname());
        if (user.isLocked()) {
            LOGGER.warn("User {} is locked.", id);
            return Mono.just(new LoginStatus(USER_LOCKED, mapBoUserToApiUser(user)));
        }
        if (userRepository.checkPassword(id, password).toFuture().join() == null) {
            LOGGER.warn("Password for user {} is not correct. Login attempts will be decreased.", id);
            user.setRest_login_attempts(user.getRest_login_attempts()-1);
            user = userRepository.save(user).toFuture().join();
            if (user.getRest_login_attempts() <= 0) {
                LOGGER.warn("User {} will be locked.", id);
                user.setLocked(true);
                user = userRepository.save(user).toFuture().join();
            }
            return Mono.just(new LoginStatus(FAILED, mapBoUserToApiUser(user)));
        }
        user.setLast_login_timestamp(LocalDateTime.now());
        user = userRepository.save(user).toFuture().join();
        User apiUser = mapBoUserToApiUser(user);
        apiUser.setUserGroupIds(groupRepository.getUserGroupsByUserId(id).map(UserGroup::id).collectList().toFuture().join());
        apiUser.setAccessRights(functionsRepository.getFunctionsByGroupsAndComponent(apiUser.getUserGroupIds(),
                Function.KeyComponent.BOC).map(Function::component).collect(Collectors.toSet()).toFuture().join());
        return Mono.just(new LoginStatus(OK, apiUser));
    }

    private User mapBoUserToApiUser(BoUser boUser) {
        LOGGER.debug("BO User {} will be mapped for API.", boUser);
        User user = new User();
        user.setUserId(boUser.getId());
        user.setUserName(boUser.getUser_name());
        user.setUserSurname(boUser.getUser_surname());
        user.setMail(boUser.getMail());
        user.setPhone(boUser.getPhone());
        user.setNote(boUser.getNote());
        user.setUserLocked(boUser.isLocked());
        user.setLastLoginTimestamp(boUser.getLast_login_timestamp());
        user.setRestLoginAttempts(boUser.getRest_login_attempts());
        return user;
    }
}
