package cz.jbenak.npos.boClient.gui.dialogs.login;

import cz.jbenak.npos.api.client.LoginStatus;
import cz.jbenak.npos.api.shared.Utils;
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
import javafx.concurrent.Task;
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
            performLogin();
        }
    }

    @FXML
    private void btnCancelPressed() {
        LOGGER.info("Cancel button pressed. Application will be terminated.");
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        info.setText("nBO Backoffice klient verze " + BoClient.getInstance().getAppProperties().getProperty("app.version", "<N/A>") + " © Jan Benák " + Year.now());
        Helpers.getEmptyTextConstraint(fieldUserName, false, "Uživatelské jméno nemůže být prázdné.", validationText);
        Helpers.getEmptyTextConstraint(fieldPassword, false, "Heslo nemůže být prázdné", validationText);
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

    private void performLogin() {
        LOGGER.info("Login of user with user name {} will be performed.", fieldUserName.getText());
        Task<LoginStatus> loginTask = new Task<>() {
            @Override
            protected LoginStatus call() {
                final UserOperations operations = new UserOperations();
                final Utils apiUtils = new Utils();
                return operations.loginUser(Integer.parseInt(fieldUserName.getText().trim()),
                        apiUtils.getStringEncryptor().encrypt(fieldPassword.getText().trim())).join();
            }
        };
        loginTask.setOnRunning(evt -> {
            progressSpinner.setVisible(true);
            fieldUserName.setDisable(true);
            fieldPassword.setDisable(true);
        });
        loginTask.setOnSucceeded(evt -> {
            progressSpinner.setVisible(false);
            fieldUserName.setDisable(false);
            fieldPassword.setDisable(false);
            handleLoginStatus((LoginStatus) evt.getSource().getValue());
        });
        loginTask.setOnFailed(evt -> {
            Throwable e = evt.getSource().getException();
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
            progressSpinner.setVisible(false);
            fieldUserName.setDisable(false);
            fieldPassword.setDisable(false);
        });
        BoClient.getInstance().getTaskExecutor().submit(loginTask);
    }

    private void handleLoginStatus(LoginStatus status) {
        switch (status.getStatus()) {
            case OK -> {
                LOGGER.info("User with ID {} has been successfully logged in.", status.getUser().getUserId());
                BoClient.getInstance().showMainWindow(status.getUser());
            }
            case FAILED -> {
                LOGGER.warn("Login of user with ID {} has failed.", status.getUser().getUserId());
                InfoDialog dialog = new InfoDialog(InfoDialogType.WARNING, dialogStage, false);
                dialog.setDialogTitle("Přihlášení selhalo");
                dialog.setDialogSubtitle("Bylo zadáno nesprávné heslo.");
                String message = status.getUser().getRestLoginAttempts() > 0
                        ?
                        "Nebylo možné přihlásit uživatele " + status.getUser().getUserId()
                                + " - " + status.getUser().getUserName() + " " + status.getUser().getUserSurname()
                                + " jelikož bylo zadáno nesprávné heslo. \n\n Máte ještě " + status.getUser().getRestLoginAttempts()
                                + (status.getUser().getRestLoginAttempts() > 1 ? " pokusy" : " pokus")
                                + " než bude tento uživatel zablokován."
                        :
                        "Pro uživatele " + status.getUser().getUserId()
                                + " byly vyčerpány všechny dostupné pokusy o přihlášení, takže uživatel byl zablokován.\n\n" +
                                "Obraťte se prosím na svého systémového administrátora o odblokování.";
                dialog.setDialogMessage(message);
                dialog.showDialog();
            }
            case ID_UNKNOWN -> {
                LOGGER.warn("Entered user with ID {} has not been found on the server.", fieldUserName.getText());
                InfoDialog dialog = new InfoDialog(InfoDialogType.WARNING, dialogStage, false);
                dialog.setDialogTitle("Přihlášení selhalo");
                dialog.setDialogSubtitle("Uživatel neznámý.");
                dialog.setDialogMessage("Zadaný uživatel nebyl na serveru nalezen. Prosím zkontrolujte jeho ID a zkuste se přihlásit znovu.");
                dialog.showDialog();
            }
            case USER_LOCKED -> {
                LOGGER.warn("User with ID {} has been locked.", status.getUser().getUserId());
                InfoDialog dialog = new InfoDialog(InfoDialogType.ERROR, dialogStage, false);
                dialog.setDialogTitle("Přihlášení selhalo");
                dialog.setDialogSubtitle("Uživatel byl zablokován.");
                dialog.setDialogMessage("Zadaný uživatel "
                        + status.getUser().getUserId()
                        + " - " + status.getUser().getUserName() + " " + status.getUser().getUserSurname()
                        + " byl zablokován z důvodu vyčerpání možných pokusů o přihlášení.\n\n" +
                        "Obraťte se prosím na svého systémového administrátora o odblokování.");
                dialog.showDialog();
            }
        }
    }
}
