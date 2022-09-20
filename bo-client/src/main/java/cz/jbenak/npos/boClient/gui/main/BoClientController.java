package cz.jbenak.npos.boClient.gui.main;

import cz.jbenak.npos.api.client.User;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.gui.dialogs.generic.InfoDialog;
import cz.jbenak.npos.boClient.gui.panels.data.DataPanelController;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class BoClientController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(BoClientController.class);
    @FXML
    private BorderPane mainPane;
    @FXML
    private Label labelLoggedUser;
    @FXML
    private Label labelSystemStatus;
    @FXML
    private Label labelStore;
    @FXML
    private Label labelAccountingPeriod;
    @FXML
    MFXProgressSpinner mainProgressIndicator;
    @FXML
    private Button menuButtonHome;
    @FXML
    private Button menuButtonData;
    @FXML
    private Button menuButtonWarehouse;
    @FXML
    private Button menuButtonFinance;
    @FXML
    private Button menuButtonInvoicing;
    @FXML
    private Button menuButtonCustomers;
    @FXML
    private Button menuButtonSales;
    @FXML
    private Button menuButtonDiscounts;
    @FXML
    private Button menuButtonShifts;
    @FXML
    private Button menuButtonViews;
    @FXML
    private Button menuButtonImportExport;
    @FXML
    private Button menuButtonSettings;
    @FXML
    private Button menuButtonHelp;
    private Button currentlySelectedButton;

    public void setUser() {
        User user = BoClient.getInstance().getLoggedUser();
        labelLoggedUser.setText(user.getUserName() + " " + user.getUserSurname());
    }

    public void setActualAccountingPeriod(String actualAccountingPeriod) {
        this.labelAccountingPeriod.setText(actualAccountingPeriod);
    }

    public void setCurrentStoreData(String storeData) {
        labelStore.setText(storeData);
    }

    @FXML
    private void buttonHomePressed() {
        currentlySelectedButton = menuButtonHome;
    }

    @FXML
    private void buttonDataPressed() {
        changeContentPane("/cz/jbenak/npos/boClient/gui/panels/data/data-panel.fxml", menuButtonData);
    }

    @FXML
    private void buttonWarehousePressed() {
        changeContentPane("/cz/jbenak/npos/boClient/gui/panels/warehouse/warehouse-panel.fxml", menuButtonWarehouse);
    }

    @FXML
    private void buttonInvoicingPressed() {
        changeContentPane("/cz/jbenak/npos/boClient/gui/panels/invoicing/invoicing-panel.fxml", menuButtonInvoicing);
    }

    @FXML
    private void buttonFinancePressed() {
        changeContentPane("/cz/jbenak/npos/boClient/gui/panels/finance/finance-panel.fxml", menuButtonFinance);
    }

    private void changeContentPane(String fxml, Button currentlySelectedButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            BorderPane panel = loader.load();
            mainPane.setCenter(panel);
            this.currentlySelectedButton.getStyleClass().remove("button-toggled");
            currentlySelectedButton.getStyleClass().add("button-toggled");
            this.currentlySelectedButton = currentlySelectedButton;
        } catch (Exception e) {
            LOGGER.error("During opening of main menu items a serious error occurred:", e);
            InfoDialog dialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
            dialog.setDialogTitle("Nelze přepnout menu");
            dialog.setDialogMessage("Nelze zobrazit požadovoanou položku menu z důvodu závažné chyby.\n" +
                    "Následující text zašlete aministrátorovi: \n\n" + e);
            dialog.showDialog();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentlySelectedButton = menuButtonHome;
        currentlySelectedButton.getStyleClass().add("button-toggled");
        mainProgressIndicator.setVisible(false);
        labelSystemStatus.setText("Systém připraven");
    }
}
