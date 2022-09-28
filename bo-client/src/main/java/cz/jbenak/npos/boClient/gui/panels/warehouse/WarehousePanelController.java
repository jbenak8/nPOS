package cz.jbenak.npos.boClient.gui.panels.warehouse;

import cz.jbenak.npos.boClient.gui.panels.PanelController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

;

public class WarehousePanelController extends PanelController implements Initializable {

    @FXML
    private BorderPane menuPane;
    @FXML
    private Button btnMenuItems;
    @FXML
    private Button btnMenuItemGroups;
    @FXML
    private Button btnMenuStockReceipts;
    @FXML
    private Button btnMenuStockDispenses;
    @FXML
    private Button btnMenuStockTaking;
    @FXML
    private Button btnMenuViews;

    @FXML
    private void setItemsPane() {
        changeContentMenuPane("items-menu.fxml", btnMenuItems);
    }

    @FXML
    private void setItemGroupsPane() {
        changeContentMenuPane("item-groups-menu.fxml", btnMenuItemGroups);
    }
    @FXML
    private void setStockReceiptsPane() {
        changeContentMenuPane("stock-receipt-menu.fxml", btnMenuStockReceipts);
    }

    @FXML
    private void setStockDispensesPane() {
        changeContentMenuPane("stock-dispenses-menu.fxml", btnMenuStockDispenses);
    }

    @FXML
    private void setStockTakingPane() {
        changeContentMenuPane("stock-taking-menu.fxml", btnMenuStockTaking);
    }

    @FXML
    private void setViewPane() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnPreviouslySelected = btnMenuItems;
        setItemsPane();
    }
}
