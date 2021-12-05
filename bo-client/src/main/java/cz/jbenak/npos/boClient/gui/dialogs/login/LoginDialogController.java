package cz.jbenak.npos.boClient.gui.dialogs.login;

import cz.jbenak.npos.boClient.BoClient;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.utils.BindingUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.Year;
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
    private MFXTextField fieldUserName;
    @FXML
    private MFXPasswordField fieldPassword;
    @FXML
    private MFXButton btnOK;

    @FXML
    private void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB && fieldPassword.isFocused()) {
            btnOK.requestFocus();
        }
        if (event.isShiftDown() && event.getCode() == KeyCode.TAB && fieldPassword.isFocused()) {
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
        if (!fieldUserName.getText().trim().isEmpty() && !fieldPassword.getPassword().trim().isBlank()) {
            LOGGER.info("Login of user with user name {} will be performed.", fieldUserName.getText());
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
        fieldUserName.getValidator().add(BindingUtils.toProperty(fieldUserName.textProperty().length().greaterThan(0)), "Uživatelské jméno nesmí být prázdné");
        fieldUserName.setIcon(new MFXFontIcon("mfx-variant7-mark", 16, Color.web("#8FF7A7")));
        fieldUserName.getIcon().visibleProperty().bind(fieldUserName.getValidator().validProperty());
        fieldPassword.getValidator().add(BindingUtils.toProperty(fieldPassword.passwordProperty().length().greaterThan(0)), "Uživatelské jméno nesmí být prázdné");
        fieldPassword.setIcon(new MFXFontIcon("mfx-variant7-mark", 16, Color.web("#8FF7A7")));
        fieldPassword.getIcon().visibleProperty().bind(fieldPassword.getValidator().validProperty());
    }
}
