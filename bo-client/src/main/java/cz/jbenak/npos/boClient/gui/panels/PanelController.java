package cz.jbenak.npos.boClient.gui.panels;

import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.gui.dialogs.generic.InfoDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PanelController {

    private static final Logger LOGGER = LogManager.getLogger(PanelController.class);
    @FXML
    public BorderPane mainPane;
    @FXML
    public  BorderPane menuPane;
    public Button btnPreviouslySelected;

    public void changeContentMenuPane(String fxml, Button currentlySelectedButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Node panel = loader.load();
            menuPane.setCenter(panel);
            this.btnPreviouslySelected.getStyleClass().remove("button-toggled");
            currentlySelectedButton.getStyleClass().add("button-toggled");
            this.btnPreviouslySelected = currentlySelectedButton;
        } catch (Exception e) {
            LOGGER.error("During opening of main menu items a serious error occurred:", e);
            InfoDialog dialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
            dialog.setDialogTitle("Nelze přepnout menu");
            dialog.setDialogMessage("Nelze zobrazit požadovoanou položku menu z důvodu závažné chyby.\n" +
                    "Následující text zašlete aministrátorovi: \n\n" + e);
            dialog.showDialog();
        }
    }
}
