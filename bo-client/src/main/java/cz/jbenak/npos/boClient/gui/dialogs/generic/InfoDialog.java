package cz.jbenak.npos.boClient.gui.dialogs.generic;

import cz.jbenak.npos.boClient.BoClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class InfoDialog extends Stage {

    public enum InfoDialogType {
        OK, INFO, WARNING, ERROR, OFFLINE
    }

    private static final Logger LOGGER = LogManager.getLogger(InfoDialog.class);
    private final InfoDialogType type;
    private final boolean wait;
    private String title = "";
    private String subtitle = "";
    private String message = "";

    public InfoDialog(InfoDialogType type, Window owner, boolean wait) {
        this.type = type;
        this.initModality(Modality.APPLICATION_MODAL);
        this.initStyle(StageStyle.UTILITY);
        this.initOwner(owner);
        this.wait = wait;
    }

    public void setDialogTitle(String title) {
        this.title = title;
    }

    public void setDialogSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setDialogMessage(String message) {
        this.message = message;
    }

    public void showDialog() {
        try {
            LOGGER.info("Informational dialog will be shown.");
            Toolkit.getDefaultToolkit().beep();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InfoDialog.fxml"));
            this.setScene(new Scene(loader.load()));
            InfoDialogController controller = loader.getController();
            controller.setDialog(this);
            if (type != InfoDialogType.OFFLINE) {
                controller.setTitle(title);
                controller.setSubtitle(subtitle);
                controller.setMessage(message);
            } else {
                controller.setTitle("Server offline");
                controller.setMessage("""
                        Spojení s backoffice serverem nebylo navázáno.
                                                
                        Zkontrolujte prosím, jestli je backoffice server online a zkontrolujte platnost nastavení adresy a přihlašovacích údajů v klientovi.
                        
                        Backoffice server vyžaduje šifrované spojení SSL.
                        """);
            }
            switch (type) {
                case OK -> controller.setOKIcon();
                case INFO -> controller.setInfoIcon();
                case WARNING -> controller.setWarnIcon();
                case ERROR -> controller.setErrorIcon();
                case OFFLINE -> controller.setOfflineIcon();
            }
            this.centerOnScreen();
            if (wait) {
                this.showAndWait();
            } else {
                this.show();
            }
        } catch (Exception e) {
            LOGGER.error("Displaying dialog failed.", e);
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Dialog nelze zobrazit");
            error.setContentText("Nastala závažná chybpa při zobrazování dialogu aplikace. Podívejte se prosím do protokolů aplikace.");
            error.showAndWait();
        }
    }

    protected void closeDialog() {
        LOGGER.info("Dialog will be closed.");
        this.close();
    }
}
