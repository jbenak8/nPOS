package cz.jbenak.npos.api;

import cz.jbenak.npos.api.data.RequestState;

/**
 * A record holding message from client to server and vice versa.
 *
 * @author Jan Benák
 * @since 2021-12-06
 * @version 1.0.0.0
 *
 * @param origin Origin of the message - POS, Backoffice server or backoffice client.
 * @param endpoint target API endpoint - see Endpoints.
 * @param requestState state of procession of the request
 * @param jsonData the very content of the message.
 *
 * @see Endpoints
 * @see RequestState
 */
public record Message(Origins origin, String endpoint, RequestState requestState, String jsonData) {

    /**
     * Definition of target endpoints.
     */
    public enum Origins {
        BO_SERVER, BO_CLIENT, POS
    }

}
