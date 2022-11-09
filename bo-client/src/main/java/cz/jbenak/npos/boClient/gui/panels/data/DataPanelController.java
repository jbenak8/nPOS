package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.boClient.gui.panels.PanelController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class DataPanelController extends PanelController implements Initializable {

    @FXML
    private Button btnMenuPOSes;
    @FXML
    private Button btnMenuAccountingPeriods;
    @FXML
    private Button btnMenuPOSQuickMenu;
    @FXML
    private Button btnMenuMeasureUnits;
    @FXML
    private Button btnMenuNumberings;
    @FXML
    private Button btnMenuPaymentMeans;
    @FXML
    private Button btnMenuDepositsDisbursements;
    @FXML
    private Button btnMenuCurrencies;
    @FXML
    private Button btnMenuCountries;
    @FXML
    private Button btnMenuVAT;
    @FXML
    private Button btnMenuExchangeRates;
    @FXML
    private Button btnMenuAdvertisementMedia;

    @FXML
    private void btnMenuPOSesPressed() {

    }

    @FXML
    private void btnMenuAccountingPeriodsPressed() {
        changeContentMenuPane("accounting-periods-menu.fxml", btnMenuAccountingPeriods);
    }

    @FXML
    private void btnMenuMeasureUnitsPressed() {
        changeContentMenuPane("measure-units-menu.fxml", btnMenuMeasureUnits);
    }

    @FXML
    private void btnMenuCurrenciesPressed() {
        changeContentMenuPane("currency-menu.fxml", btnMenuCurrencies);
    }

    @FXML
    private void btnMenuCountriesPressed() {
        changeContentMenuPane("countries-menu.fxml", btnMenuCountries);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnPreviouslySelected = btnMenuAccountingPeriods;
        btnMenuAccountingPeriodsPressed();
    }
}
