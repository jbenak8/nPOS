package cz.jbenak.npos.boClient.gui.panels.invoicing;

import cz.jbenak.npos.boClient.gui.panels.PanelController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

//TODO udělat class PanelController, která má previouslySelectedButton a initialize a metodu pro přepnutí panelu
public class InvoicingPanelController extends PanelController implements Initializable {
    @FXML
    private Button btnMenuIssuedInvoices;
    @FXML
    private Button btnMenuIssuedDeposits;
    @FXML
    private Button btnMenuIssuedCreditNotes;
    @FXML
    private Button btnMenuIssuedDeliveryNotes;

    @FXML
    private void btnMenuIssuedInvoicesPressed() {

    }

    @FXML
    private void btnMenuIssuedDepositsPressed() {

    }

    @FXML
    private void btnMenuIssuedCreditNotesPressed() {

    }

    @FXML
    private void btnMenuIssuedDeliveryNotesPressed() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnMenuIssuedInvoices.getStyleClass().add("button-toggled");
        btnPreviouslySelected = btnMenuIssuedInvoices;
    }
}
