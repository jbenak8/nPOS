package cz.jbenak.npos.boClient.exceptions;

import java.net.URI;

public class ClientException extends RuntimeException {

    private int status;

    public ClientException(int status, URI uri, String message) {
        super("HTTP Status: " + status + " on server URI " + uri + " error message: " + message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
