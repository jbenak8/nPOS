package cz.jbenak.npos.boClient.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.boClient.exceptions.ClientException;
import cz.jbenak.npos.boClient.exceptions.ServerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class HttpClientOperations {

    private final static Logger LOGGER = LogManager.getLogger(HttpClientOperations.class);
    private final static Duration TIMEOUT = Duration.ofSeconds(5);
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();
    private final String authorization;
    private final HttpClient client;

    public HttpClientOperations(String authorization, HttpClient client) {
        this.authorization = authorization;
        this.client = client;
    }

    public <T> CompletableFuture<T> getData(URI uri, TypeReference<T> responseTypeReference) {
        LOGGER.debug("Performing GET request to URI {}", uri);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(TIMEOUT)
                .header("Content-Type", "application/json")
                .header("Authorization", authorization)
                .GET()
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        throw parseRemoteException(uri, response.statusCode(), response.body());
                    }
                    return response.body();
                })
                .thenApply(response -> deserialize(response, responseTypeReference));
    }

    public <T> CompletableFuture<T> postDataWithResponse(URI uri, Object data, TypeReference<T> responseTypeReference) {
        LOGGER.debug("Performing POST request to URI {} awaiting another data in response.", uri);
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            LOGGER.error("Given data {} cannot be converted to JSON for POST: {}", data, e);
            return CompletableFuture.failedFuture(e);
        }
        HttpRequest request = preparePOSTRequest(uri, json);
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        throw parseRemoteException(uri, response.statusCode(), response.body());
                    }
                    return response.body();
                })
                .thenApply(response -> deserialize(response, responseTypeReference));
    }


    public CompletableFuture<CRUDResult> postData(URI uri, Object data) {
        LOGGER.debug("Performing POST request to URI {} with standard HTTP response.", uri);
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            LOGGER.error("Given data {} cannot be converted to JSON for POST: {}", data, e);
            return CompletableFuture.failedFuture(e);
        }
        HttpRequest request = preparePOSTRequest(uri, json);
        return getCrudResultCompletableFuture(uri, request);
    }

    private CompletableFuture<CRUDResult> getCrudResultCompletableFuture(URI uri, HttpRequest request) {
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        throw parseRemoteException(uri, response.statusCode(), response.body());
                    }
                    if (response.statusCode() == 404) {
                        throw parseRemoteException(uri, response.statusCode(), "Požadovaná data nebyla na serveru nalezena.");
                    }
                    return response.body();
                })
                .thenApply(response -> deserialize(response, new TypeReference<>() {
                }));
    }

    public CompletableFuture<CRUDResult> deleteData(URI uri) {
        LOGGER.debug("Performing DELETE request to URI {} with standard HTTP response.", uri);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(TIMEOUT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", authorization)
                .DELETE()
                .build();
        return getCrudResultCompletableFuture(uri, request);
    }

    private HttpRequest preparePOSTRequest(URI uri, String json) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .timeout(TIMEOUT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", authorization)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    private <T> T deserialize(String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new ClientException("Cannot parse response", e);
        }
    }

    private RuntimeException parseRemoteException(URI uri, int statusCode, String body) {
        if (body == null) {
            return new ClientException(statusCode, uri, "HTTP Error");
        }
        try {
            return OBJECT_MAPPER.readValue(body, ServerException.class);
        } catch (JsonProcessingException e) {
            return new ClientException(statusCode, uri,  "HTTP Error");
        }
    }
}
