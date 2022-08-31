package cz.jbenak.npos.api.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginStatus(cz.jbenak.npos.api.client.LoginStatus.Status status, User user, int leftAttempts) {

    public enum Status {
        OK, FAILED, USER_BLOCKED, ID_UNKNOWN
    }

    @JsonCreator
    public LoginStatus(@JsonProperty(value = "loginStatus", required = true) Status status,
                       @JsonProperty(value = "user", required = true) User user,
                       @JsonProperty(value = "leftLoginAttempts", required = true) int leftAttempts) {
        this.status = status;
        this.user = user;
        this.leftAttempts = leftAttempts;
    }

    @Override
    @JsonProperty("loginStatus")
    public Status status() {
        return status;
    }

    @Override
    @JsonProperty("user")
    public User user() {
        return user;
    }

    @Override
    @JsonProperty("leftLoginAttempts")
    public int leftAttempts() {
        return leftAttempts;
    }
}
