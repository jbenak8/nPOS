package cz.jbenak.npos.commTester;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.util.Properties;

public class CommTester extends Application {

    private static final Logger LOGGER = LogManager.getLogger(CommTester.class);
    private Properties appProperties;
    private Stage mainStage;
    private static volatile CommTester instance;

    public CommTester() {
        super();
        synchronized (CommTester.class) {
            if (instance != null) {
                throw new UnsupportedOperationException("Multiple initialization of " + CommTester.class.getCanonicalName());
            }
            instance = this;
        }
    }

    public Properties getAppProperties() {
        return appProperties;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public static CommTester getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) {
        try {
            if (loadSettings()) {
                mainStage = stage;
                LOGGER.info("Application version {} is starting", appProperties.getProperty("app.version"));
                FXMLLoader loader = new FXMLLoader(CommTester.class.getResource("gui/main/main-app-window.fxml"));
                stage.setScene(new Scene(loader.load()));
                stage.setTitle("nPOS Comm tester");
                stage.setResizable(false);
                stage.show();
            }
        } catch (Exception e) {
            LOGGER.fatal("Application cannot start", e);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Tester technologie nelze spustit");
            error.setHeaderText("Nepodařilo se spustit tester technologie");
            error.setContentText(e.getLocalizedMessage());
            error.showAndWait();
            Platform.exit();
        }
    }

    private boolean loadSettings() throws Exception {
        boolean loaded = true;
        LOGGER.info("Application nBOS BO client is starting. Loading application settings.");
        appProperties = new Properties();
        try (FileInputStream fis = new FileInputStream("conf/comm-tester.properties")) {
            appProperties.load(fis);
            if (appProperties.isEmpty()) {
                throw new Exception("Application properties are empty!");
            }
            return loaded;
        }
    }
}
