package cz.jbenak.npos.api.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginStatus(@JsonProperty("login_status") Status status,
                          @JsonProperty("user") User user) {

    public enum Status {
        OK, FAILED, USER_LOCKED, ID_UNKNOWN
    }

    @Override
    public String toString() {
        return "LoginStatus{" +
                "status=" + status +
                ", user=" + user +
                '}';
    }
}
