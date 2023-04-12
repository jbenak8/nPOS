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
public class LoginStatus {

    public enum Status {
        OK, FAILED, USER_LOCKED, ID_UNKNOWN
    }

    private Status status;
    private User user;
}
