package cz.jbenak.npos.boClient.gui.panels.warehouse;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;;
import java.util.ResourceBundle;

public class WarehousePanelController implements Initializable {

    @FXML
    private BorderPane  mainPane;
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
    private Button btnPreviouslySelected;

    @FXML
    private void setItemsPane() {
        try {
            FXMLLoader loader = new FXMLLoader(WarehousePanelController.class.getResource("items-menu.fxml"));
            FlowPane panel = loader.load();
            menuPane.setCenter(panel);
            btnPreviouslySelected.getStyleClass().remove("button-toggled");
            btnMenuItems.getStyleClass().add("button-toggled");
            btnPreviouslySelected = btnMenuItems;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setItemGroupsPane() {
        try {
            FXMLLoader loader = new FXMLLoader(WarehousePanelController.class.getResource("item-groups-menu.fxml"));
            FlowPane panel = loader.load();
            menuPane.setCenter(panel);
            btnPreviouslySelected.getStyleClass().remove("button-toggled");;
            btnMenuItemGroups.getStyleClass().add("button-toggled");
            btnPreviouslySelected = btnMenuItemGroups;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void setStockReceiptsPane() {
        try {
            FXMLLoader loader = new FXMLLoader(WarehousePanelController.class.getResource("stock-receipt-menu.fxml"));
            FlowPane panel = loader.load();
            menuPane.setCenter(panel);
            btnPreviouslySelected.getStyleClass().remove("button-toggled");;
            btnMenuStockReceipts.getStyleClass().add("button-toggled");
            btnPreviouslySelected = btnMenuStockReceipts;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setStockDispensesPane() {
        try {
            FXMLLoader loader = new FXMLLoader(WarehousePanelController.class.getResource("stock-dispenses-menu.fxml"));
            FlowPane panel = loader.load();
            menuPane.setCenter(panel);
            btnPreviouslySelected.getStyleClass().remove("button-toggled");;
            btnMenuStockDispenses.getStyleClass().add("button-toggled");
            btnPreviouslySelected = btnMenuStockDispenses;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setStockTakingPane() {
        try {
            FXMLLoader loader = new FXMLLoader(WarehousePanelController.class.getResource("stock-TAKING-menu.fxml"));
            FlowPane panel = loader.load();
            menuPane.setCenter(panel);
            btnPreviouslySelected.getStyleClass().remove("button-toggled");;
            btnMenuStockTaking.getStyleClass().add("button-toggled");
            btnPreviouslySelected = btnMenuStockTaking;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setViewPane() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnMenuItems.getStyleClass().add("button-toggled");
        btnPreviouslySelected = btnMenuItems;
    }
}
