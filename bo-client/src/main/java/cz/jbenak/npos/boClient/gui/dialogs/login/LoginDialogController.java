package cz.jbenak.npos.boClient.gui.dialogs.login;

import cz.jbenak.npos.api.client.LoginStatus;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.UserOperations;
import cz.jbenak.npos.boClient.exceptions.ClientException;
import cz.jbenak.npos.boClient.gui.dialogs.generic.InfoDialog;
import cz.jbenak.npos.boClient.gui.helpers.Helpers;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.ConnectException;
import java.net.URL;
import java.time.Year;
import java.util.ResourceBundle;

import static cz.jbenak.npos.boClient.gui.dialogs.generic.InfoDialog.InfoDialogType;

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
    private MFXProgressSpinner progressSpinner;
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

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
        //if (fieldUserName.getValidator().isValid() && fieldPassword.getValidator().isValid()) {
        if (areFieldsFilled()) {
            progressSpinner.setVisible(true);
            fieldUserName.setDisable(true);
            fieldPassword.setDisable(true);
            LOGGER.info("Login of user with user name {} will be performed.", fieldUserName.getText());
            try {
                UserOperations operations = new UserOperations();
                LoginStatus status = operations.loginUser(Integer.parseInt(fieldUserName.getText().trim()), fieldPassword.getText().trim()).join();
                System.out.println(status);
            } catch (Exception e) {
                //TODO VYtvořit CommonDialogOperation - tohle aby šlo jednou metodou všude. I třeba i OK.
                LOGGER.error("Error during login operation occurred:", e);
                InfoDialog errorDialog;
                if (e.getCause() instanceof ConnectException) {
                    errorDialog = new InfoDialog(InfoDialogType.OFFLINE, dialogStage, false);
                } else {
                    errorDialog = new InfoDialog(InfoDialogType.ERROR, dialogStage, false);
                    errorDialog.setDialogTitle("Chyba při přihlašování");
                    errorDialog.setDialogMessage(e.getMessage());
                    if (e.getCause() instanceof ClientException) {
                        errorDialog.setDialogSubtitle("HTTP status " + ((ClientException) e.getCause()).getStatus());
                    }
                }
                errorDialog.showDialog();
            } finally {
                progressSpinner.setVisible(false);
                fieldUserName.setDisable(false);
                fieldPassword.setDisable(false);
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
        progressSpinner.setVisible(false);
    }

    private boolean areFieldsFilled() {
        if (fieldUserName.getText().trim().isEmpty()) {
            InfoDialog dialog = new InfoDialog(InfoDialogType.WARNING, dialogStage, true);
            dialog.setDialogTitle("Zadejte uživatele");
            dialog.setDialogMessage("""
                    Uživatelské jméno nesmí být prázdné.
                                        
                    Prosím zadejte uživatelské jméno (ID), abych Vás mohl přihlásit.
                    """);
            dialog.showDialog();
            fieldUserName.requestFocus();
            return false;
        }
        if (fieldPassword.getText().trim().isEmpty()) {
            InfoDialog dialog = new InfoDialog(InfoDialogType.WARNING, dialogStage, true);
            dialog.setDialogTitle("Zadejte heslo");
            dialog.setDialogMessage("""
                    Heslo musí být vyplněno.
                                        
                    Prosím zadejte heslo vybraného uživatele, abych Vás mohl přihlásit.
                    """);
            dialog.showDialog();
            fieldPassword.requestFocus();
            return false;
        }
        return true;
    }
}
