package cz.jbenak.npos.boClient.engine;

import java.net.http.HttpClient;

public class HttpClientOperations {

    private final String authentication;
    private final HttpClient client;

    public HttpClientOperations(String authentication, HttpClient client) {
        this.authentication = authentication;
        this.client = client;
    }
}
