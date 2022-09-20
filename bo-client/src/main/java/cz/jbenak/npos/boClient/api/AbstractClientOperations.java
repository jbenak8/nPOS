package cz.jbenak.npos.boClient.api;

import cz.jbenak.npos.boClient.engine.Connection;
import cz.jbenak.npos.boClient.engine.HttpClientOperations;

public class AbstractClientOperations {

    protected final HttpClientOperations httpClientOperations;
    protected final String baseURI;

    public AbstractClientOperations(String baseURI) {
        Connection connection = Connection.getInstance();
        this.httpClientOperations = new HttpClientOperations(connection.getBasicAuthBoServer(), connection.getBoHttpClient());
        this.baseURI = connection.getBaseURIBoServer() + baseURI;
    }
}
