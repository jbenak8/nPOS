package cz.jbenak.npos.api.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CRUDResult {

    public enum ResultType {
        OK, ALREADY_EXISTS, HAS_BOUND_RECORDS, GENERAL_ERROR, DATA_CONSTRAINT_VIOLATION
    }

    private ResultType resultType;
    private String message;

    @JsonProperty(value = "resultType", required = true)
    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
