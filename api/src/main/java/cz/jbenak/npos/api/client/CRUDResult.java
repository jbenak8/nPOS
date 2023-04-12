package cz.jbenak.npos.api.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class CRUDResult {

    public enum ResultType {
        OK, ALREADY_EXISTS, HAS_BOUND_RECORDS, GENERAL_ERROR, DATA_CONSTRAINT_VIOLATION
    }

    private ResultType resultType;
    private String message;
}
