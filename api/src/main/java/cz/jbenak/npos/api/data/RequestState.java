package cz.jbenak.npos.api.data;

/**
 * Data record containing status of processing the request.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-12-06
 */
public record RequestState(RequestStates state) {

    /**
     * Request status types.
     */
    public enum RequestStates {
        OK, ERROR, UNAUTHORIZED, UNSUPPORTED
    }
}
