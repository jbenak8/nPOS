package cz.jbenak.npos.boClient.gui.panels;

import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.gui.dialogs.generic.InfoDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class used for change of subgroup menu and main content.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2022-09-28
 */
public class PanelController {

    private static final Logger LOGGER = LogManager.getLogger(PanelController.class);
    @FXML
    public BorderPane mainPane;
    @FXML
    public BorderPane menuPane;
    public Button btnPreviouslySelected;

    /**
     * Changes actual menu control panel for data operation on subgroup item - e.g. Data -> Measure units -> New/Update/Delete.
     * Calls menu change without change of selection of subgroup menu button.
     * It will also change associated content - e.g. shows pane for edition/add of new data.
     *
     * @param fxml menu definition to load.
     */
    public <T extends Initializable> T changeContentMenuPane(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Node panel = loader.load();
            menuPane.setCenter(panel);
            return loader.getController();
        } catch (Exception e) {
            LOGGER.error("During opening of main menu items a serious error occurred:", e);
            InfoDialog dialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
            dialog.setDialogTitle("Nelze přepnout menu");
            dialog.setDialogMessage("Nelze zobrazit požadovanou položku menu z důvodu závažné chyby.\n" +
                    "Následující text zašlete aministrátorovi: \n\n" + e);
            dialog.showDialog();
            return null;
        }
    }

    /**
     * Change current menu control panel (the menu for operating on subgroup - e.g. Data -> Accounting period)
     * and associated content (e.g. list of accounting periods)
     *
     * @param fxml                    menu definition to load.
     * @param currentlySelectedButton subgroup menu to be selected.
     */
    public void changeContentMenuPane(String fxml, Button currentlySelectedButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Node panel = loader.load();
            menuPane.setCenter(panel);
            PanelMainContentChangeable controller = loader.getController();
            controller.setMainContentPane(mainPane);
            controller.setPanelController(this);
            this.btnPreviouslySelected.getStyleClass().remove("button-toggled");
            currentlySelectedButton.getStyleClass().add("button-toggled");
            this.btnPreviouslySelected = currentlySelectedButton;
            controller.loadData();
        } catch (Exception e) {
            LOGGER.error("During opening of main menu items a serious error occurred:", e);
            InfoDialog dialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
            dialog.setDialogTitle("Nelze přepnout menu");
            dialog.setDialogMessage("Nelze zobrazit požadovanou položku menu z důvodu závažné chyby.\n" +
                    "Následující text zašlete aministrátorovi: \n\n" + e);
            dialog.showDialog();
        }
    }
}
