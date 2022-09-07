package cz.jbenak.npos.boClient;

import cz.jbenak.npos.api.shared.Utils;
import cz.jbenak.npos.boClient.gui.dialogs.login.LoginDialogController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Primary class of an application Bo Client - it will initialize necessary settings and show login dialog.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-12-03
 */

public class BoClient extends Application {

    private static final Logger LOGGER = LogManager.getLogger(BoClient.class);
    private static volatile BoClient instance;
    private Properties appProperties;
    private Utils apiUtils;

    /**
     * Static instance initialization constructor.
     */
    public BoClient() {
        super();
        synchronized (BoClient.class) {
            if (instance != null) {
                throw new UnsupportedOperationException("Unsupported operation - multiple initialization of app main class " + BoClient.class.getCanonicalName());
            }
            instance = this;
        }
    }

    /**
     * Get instance of the main application class.
     *
     * @return global instance of the BoClient app.
     */
    public static BoClient getInstance() {
        return instance;
    }

    public Properties getAppProperties() {
        return appProperties;
    }

    public Utils getApiUtils() {
        return apiUtils;
    }

    private boolean loadSettings() {
        boolean loaded = true;
        LOGGER.info("Application nBOS BO client is starting. Loading application settings.");
        appProperties = new Properties();
        try (FileInputStream fis = new FileInputStream("conf/bo-client.properties")) {
            appProperties.load(fis);
            if (appProperties.isEmpty()) {
                LOGGER.error("Application properties are empty!");
                loaded = false;
            }
        } catch (IOException ex) {
            LOGGER.error("Could not load system properties.", ex);
            loaded = false;
        }
        return loaded;
    }

    private void initLogging() {
        if (Boolean.parseBoolean(appProperties.getProperty("connection.log"))) {
            System.setProperty("jdk.httpclient.HttpClient.log", "all");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        if (loadSettings()) {
            LOGGER.info("Application settings has been loaded. Application version is {}. Additional logging and API utilities will be initialized.", appProperties.getProperty("app.version", "<N/A>"));
            initLogging();
            apiUtils = new Utils();
            LOGGER.info("Login dialog will be shown.");
            FXMLLoader loader = new FXMLLoader(BoClient.class.getResource("gui/dialogs/login/login-dialog.fxml"));
            stage.setScene(new Scene(loader.load()));
            LoginDialogController controller = loader.getController();
            controller.setDialogStage(stage);
            stage.setTitle("Přihlašte se do nBO klienta");
            stage.setResizable(false);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("gui/img/BOikona.png"))));
            stage.show();
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("BO klienta nelze spustit");
            error.setHeaderText("Nepodařilo se spustit BO klienta");
            error.setContentText("Nemůžu spustit BO klienta, protože se nepodařilo načíst jeho nastavení. Podívejte se prosím do protokolů aplikace.");
            error.showAndWait();
            Platform.exit();
        }
    }

    protected void showError(Exception e) {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
