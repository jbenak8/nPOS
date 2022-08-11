package cz.jbenak.npos.boClient.gui.dialogs.login;

import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.gui.helpers.Helpers;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.time.Year;
import java.util.Base64;
import java.util.ResourceBundle;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-12-03
 * <p>
 * Login dialog GUI components controller.
 */

public class LoginDialogController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(LoginDialogController.class);
    @FXML
    private Label info;
    @FXML
    private Label validationText;
    @FXML
    private MFXTextField fieldUserName;
    @FXML
    private MFXPasswordField fieldPassword;
    @FXML
    private MFXButton btnOK;

    @FXML
    private void keyPressed(KeyEvent event) {
        if ((event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) && fieldPassword.isFocused()) {
            btnOK.requestFocus();
        }
        if ((event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) && fieldPassword.isFocused()) {
            fieldUserName.requestFocus();
        }
        if (event.getCode() == KeyCode.ENTER && fieldPassword.isFocused()) {
            btnOkPressed();
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            btnCancelPressed();
        }
    }

    @FXML
    private void btnOkPressed() {
        if (fieldUserName.getValidator().isValid() && fieldPassword.getValidator().isValid()) {
            LOGGER.info("Login of user with user name {} will be performed.", fieldUserName.getText());
            try {
                SecureRandom random = new SecureRandom();
                byte[] salt = new byte[8];
                random.nextBytes(salt);
                KeySpec spec = new PBEKeySpec("Client123".toCharArray(), salt, 185000, 256);
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                byte[] hash = factory.generateSecret(spec).getEncoded();

                HttpClient client = HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_1_1)
                        .followRedirects(HttpClient.Redirect.NEVER)
                        .connectTimeout(Duration.ofSeconds(5))
                        .build();

                String password = new String(hash);
                String basicAuth = "Basic " + Base64.getEncoder().encodeToString(("BoClient" + ":" + "Client123").getBytes());

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://localhost:7422/test/test"))
                        .timeout(Duration.ofSeconds(5))
                        .header("Content-Type", "application/json")
                        .header("Authorization", basicAuth)
                        .GET()
                        .build();

                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(response -> {
                            if (response.statusCode() != 200) {
                                System.out.println(response.statusCode());
                                System.out.println(response.body());
                            }
                            return response.body();
                        })
                        .thenApply(body -> {
                            System.out.println(body);
                            return null;
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void btnCancelPressed() {
        LOGGER.info("Cancel button pressed. Application will be terminated.");
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        info.setText("nBO Backoffice klient verze " + BoClient.getInstance().getAppProperties().getProperty("app.version", "<N/A>") + " \u00a9 Jan Benák " + Year.now());
        Helpers.getEmptyTextConstraint(fieldUserName, "Uživatelské jméno nemůže být prázdné.", validationText);
        Helpers.getEmptyTextConstraint(fieldPassword, "Heslo nemůže být prázdné", validationText);
    }
}
