package cz.jbenak.npos.boClient.gui.panels.finance;

import cz.jbenak.npos.boClient.gui.panels.PanelController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class FinancePanelController extends PanelController implements Initializable {

    @FXML
    private Button btnMenuDeposits;
    @FXML
    private Button btnMenuDisbursements;
    @FXML
    private Button btnMenuViews;

    @FXML
    private void btnMenuDepositsPressed() {
        changeContentMenuPane("deposits-menu.fxml", btnMenuDeposits);
    }

    @FXML
    private void btnMenuDisbursementsPressed() {
        changeContentMenuPane("disbursements-menu.fxml", btnMenuDisbursements);
    }

    @FXML
    private void btnMenuViewsPressed() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnPreviouslySelected = btnMenuDeposits;
        btnMenuDepositsPressed();
    }
}
