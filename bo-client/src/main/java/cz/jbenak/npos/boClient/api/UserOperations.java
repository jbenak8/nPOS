package cz.jbenak.npos.boClient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.jbenak.npos.api.client.LoginAttempt;
import cz.jbenak.npos.api.client.LoginStatus;
import cz.jbenak.npos.api.client.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * API class for maintaining user affected calls with server (e.g. login, store new user, etc.)
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2022-08-31
 */
public class UserOperations extends AbstractClientOperations {

    private final static Logger LOGGER = LogManager.getLogger(UserOperations.class);

    public UserOperations() {
        super("/user");
    }

    public CompletableFuture<LoginStatus> loginUser(int user, String password) {
        LOGGER.info("Performing login to BO server for user {}.", user);
        return httpClientOperations.postDataWithResponse(URI.create(baseURI + "/login"), new LoginAttempt(user, password), new TypeReference<>() {
        });
    }

    public CompletableFuture<List<User>> getAllUsers() {
        LOGGER.info("Getting all users from the BO server");
        return httpClientOperations.getData(URI.create(baseURI + "/getAllUsers"), new TypeReference<List<User>>() {
        });
    }

    public CompletableFuture<User> getUserByUserID(String userId) {
        LOGGER.info("Getting data of user {} from BO server.", userId);
        return httpClientOperations.getData(URI.create(baseURI + "/getUser/" + userId), new TypeReference<User>() {
        });
    }
}
