package cz.jbenak.npos.api.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * DTO for sending login data to Backoffice server.
 *
 * @author Jan Benák
 * @version 1.1.0.0
 * @since 2023-04-12
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class LoginAttempt {

    private int userId;
    private String password;

}
