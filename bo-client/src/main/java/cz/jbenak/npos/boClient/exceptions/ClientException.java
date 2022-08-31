package cz.jbenak.npos.boClient.exceptions;

public class ClientException extends RuntimeException{

    public ClientException(int status, String message) {
        super("HTTP Status: " + status + ": " + message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
