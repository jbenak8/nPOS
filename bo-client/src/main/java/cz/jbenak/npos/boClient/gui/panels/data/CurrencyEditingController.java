package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.Currency;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialogController;
import cz.jbenak.npos.boClient.gui.helpers.Helpers;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class CurrencyEditingController extends EditDialogController<Currency> {

    private final static Logger LOGGER = LogManager.getLogger(CurrencyEditingController.class);
    @FXML
    private MFXTextField fieldIsoCode;
    @FXML
    private MFXTextField fieldName;
    @FXML
    private MFXTextField fieldSymbol;
    @FXML
    private MFXToggleButton btnAcceptable;
    @FXML
    private MFXToggleButton btnMain;
    @FXML
    private Label validationLabel;

    @Override
    public void setDataEdited(Currency dataEdited) {
        this.dataEdited = dataEdited;
        title.setText("Úprava měny \"" + dataEdited.getIsoCode() + "\"");
        fieldIsoCode.setText(dataEdited.getIsoCode());
        fieldIsoCode.setDisable(true);
        fieldName.setText(dataEdited.getName());
        fieldSymbol.setText(dataEdited.getSymbol());
        btnAcceptable.setSelected(dataEdited.isAcceptable());
        btnMain.setSelected(dataEdited.isMain());
    }

    @Override
    protected void savePressed() {
        if (validateFields()) {
            dataEdited = new Currency();
            dataEdited.setIsoCode(fieldIsoCode.getText());
            dataEdited.setName(fieldName.getText());
            dataEdited.setSymbol(fieldSymbol.getText());
            dataEdited.setAcceptable(btnAcceptable.isSelected());
            dataEdited.setMain(btnMain.isSelected());
            save();
        }
    }

    private boolean validateFields() {
        if (!fieldIsoCode.getValidator().isValid()) {
            return false;
        }
        if (!fieldName.getValidator().isValid()) {
            return false;
        }
        return fieldSymbol.getValidator().isValid();
    }

    @Override
    protected void save() {
        LOGGER.info("Currency {} will be saved.", dataEdited);
        Task<CRUDResult> saveUnitTask = new Task<>() {
            @Override
            protected CRUDResult call() {
                DataOperations operations = new DataOperations();
                return operations.storeCurrency(dataEdited).join();
            }
        };
        saveUnitTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Ukládám měnu...");
        });
        saveUnitTask.setOnSucceeded(evt -> {
            CRUDResult result = (CRUDResult) evt.getSource().getValue();
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            if (result.getResultType() == CRUDResult.ResultType.OK) {
                LOGGER.info("Currency data has been saved.");
                BoClient.getInstance().getMainController().setSystemStatus("Měna uložena");
                dialog.setSaved(true);
                dialog.setEdited(false);
                dialog.setCancelled(false);
                dialog.closeDialog();
            }
            else {
                BoClient.getInstance().getMainController().setSystemStatus("Chyba při ukládání měny.");
                showCRUDresultErrorDialog(result.getMessage());
            }
        });
        saveUnitTask.setOnFailed(evt -> {
            LOGGER.error("There was an error during saving of currency data.", evt.getSource().getException());
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při ukládání měny.");
            showSaveErrorDialog(evt.getSource().getException());
        });
        BoClient.getInstance().getTaskExecutor().submit(saveUnitTask);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title.setText("Nová měna");
        fieldIsoCode.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (dataEdited.getIsoCode().compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        fieldIsoCode.setTextLimit(3);
        fieldName.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (dataEdited.getName().compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        fieldName.setTextLimit(45);
        fieldSymbol.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (dataEdited.getSymbol().compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        fieldSymbol.setTextLimit(3);
        Helpers.getEmptyTextConstraint(fieldIsoCode, false, "Je nutno vyplnit ISO kód.", validationLabel);
        Helpers.getEmptyTextConstraint(fieldName, false, "Je nutno vyplnit název měny.", validationLabel);
        Helpers.getEmptyTextConstraint(fieldSymbol, false, "Je nutno vyplnit národní symbol.", validationLabel);
    }
}
