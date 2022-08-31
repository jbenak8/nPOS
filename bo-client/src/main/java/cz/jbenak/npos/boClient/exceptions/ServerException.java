package cz.jbenak.npos.boClient.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerException extends RuntimeException {

    private final Instant timestamp;
    private final String path;
    private final int status;
    private final String error;
    private final String message;

    @JsonCreator
    public ServerException(@JsonProperty("timestamp") Instant timestamp,
                           @JsonProperty("path") String path,
                           @JsonProperty("status") int status,
                           @JsonProperty("error") String error,
                           @JsonProperty("message") String message) {
        this.timestamp = timestamp;
        this.path = path;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    @JsonProperty
    public Instant getTimestamp() {
        return timestamp;
    }

    @JsonProperty
    public String getPath() {
        return path;
    }

    @JsonProperty
    public int getStatus() {
        return status;
    }

    @JsonProperty
    public String getError() {
        return error;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
