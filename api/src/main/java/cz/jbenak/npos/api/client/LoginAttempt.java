package cz.jbenak.npos.api.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for sending login data to Backoffice server.
 *
 * @param userId   ID of the user (number - as used on POS)
 * @param password password of the user - can be numeric (like on POS) or string.
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2022-08-31
 */
public record LoginAttempt(String userId, String password) {

    @JsonCreator
    public LoginAttempt {
    }

    @Override
    @JsonProperty
    public String userId() {
        return userId;
    }

    @Override
    @JsonProperty
    public String password() {
        return password;
    }
}
