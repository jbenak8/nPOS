package cz.jbenak.npos.boClient.engine;

import cz.jbenak.npos.api.shared.Utils;
import cz.jbenak.npos.boClient.BoClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.net.http.HttpClient;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Properties;

/**
 * Connection class providing all HTTP clients.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2022-08-29
 */
public class Connection {

    private static final Logger LOGGER = LogManager.getLogger(Connection.class);
    private final Utils apiUtils = BoClient.getInstance().getApiUtils();
    private final Properties appProps = BoClient.getInstance().getAppProperties();
    private String basicAuthBoServer;
    private HttpClient boHttpClient;

    public Connection() {
        createBasicAuthStrings();
    }

    public HttpClient getBoHttpClient() {
        if (boHttpClient == null) {
            createBoHttpClient();
        }
        return boHttpClient;
    }

    public String getBasicAuthBoServer() {
        return basicAuthBoServer;
    }

    private void createBasicAuthStrings() {
        LOGGER.debug("Create authentication for BO Server.");
        String name = apiUtils.getStringEncryptor().decrypt(appProps.getProperty("connection.boserver.user"));
        String password = apiUtils.getStringEncryptor().decrypt(appProps.getProperty("connection.boserver.password"));
        String encoded = Base64.getEncoder().encodeToString((name + ":" + password).getBytes());
        basicAuthBoServer = "Basic " + encoded;
    }

    private SSLContext createSSLContext() throws Exception {
        String keyPassword = apiUtils.getStringEncryptor().decrypt(appProps.getProperty("connection.ssl.truststore.password"));
                /*
                KeyStore store = KeyStore.getInstance("PKCS12");
                keyStore.load(new FileInputStream("c:/IDEA_PROJEKTY/nPOS/keystore_selfSigned.p12"), keyPassphrase.toCharArray());
                KeyManagerFactory keyMan = KeyManagerFactory.getInstance("SunX509");
                keyMan.init(keyStore, keyPassphrase.toCharArray());
                */
        KeyStore trustStore = KeyStore.getInstance(appProps.getProperty("connection.ssl.truststore.type"));
        trustStore.load(new FileInputStream(appProps.getProperty("connection.ssl.truststore")), keyPassword.toCharArray());
        TrustManagerFactory trustMan = TrustManagerFactory.getInstance("SunX509");
        trustMan.init(trustStore);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, trustMan.getTrustManagers(), new SecureRandom());
        return context;
    }

    private void createBoHttpClient() {
        if (Boolean.parseBoolean(appProps.getProperty("connection.ssl.enabled"))) {
            try {
                boHttpClient = HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_1_1)
                        .followRedirects(HttpClient.Redirect.NEVER)
                        .connectTimeout(Duration.ofSeconds(5))
                        .sslContext(createSSLContext())
                        .build();
            } catch (Exception e) {
                LOGGER.error("There was an error during creation of SSL Context.", e);
                boHttpClient = null;
            }
        } else {
            boHttpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NEVER)
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();
        }
    }
}
