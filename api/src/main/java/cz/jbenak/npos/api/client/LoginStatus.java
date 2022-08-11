package cz.jbenak.npos.api.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginStatus {

    public enum Status {
        OK, FAILED, USER_BLOCKED, ID_UNKNOWN
    }

    private final Status status;
    private final User user;
    private final int leftAttempts;

    @JsonCreator
    public LoginStatus(@JsonProperty(value = "loginStatus", required = true) Status status,
                       @JsonProperty(value = "user", required = true) User user,
                       @JsonProperty(value = "leftLoginAttempts", required = true) int leftAttempts) {
        this.status = status;
        this.user = user;
        this.leftAttempts = leftAttempts;
    }

    @JsonProperty("loginStatus")
    public Status getStatus() {
        return status;
    }

    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    @JsonProperty("leftAttempts")
    public int getLeftAttempts() {
        return leftAttempts;
    }
}
