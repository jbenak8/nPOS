package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.VAT;
import cz.jbenak.npos.api.shared.enums.VATType;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialogController;
import cz.jbenak.npos.boClient.gui.dialogs.generic.InfoDialog;
import cz.jbenak.npos.boClient.gui.helpers.Helpers;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class VATEditingController extends EditDialogController<VAT> {

    @FXML
    private Label validationLabel;
    @FXML
    private MFXTextField fieldVATPercentage;
    @FXML
    private MFXComboBox<String> selectorVATType;
    @FXML
    private MFXDatePicker datePickerValidFrom;

    private final ObservableList<String> vatTypeObservableList = FXCollections.observableArrayList();
    private final static Logger LOGGER = LogManager.getLogger(VATEditingController.class);

    @Override
    public void setDataEdited(VAT dataEdited) {
    }

    @Override
    protected void save() {
        LOGGER.info("VAT {} will be saved.", dataEdited);
        Task<CRUDResult> saveVATTask = new Task<>() {
            @Override
            protected CRUDResult call() {
                DataOperations operations = new DataOperations();
                return operations.storeVAT(dataEdited).join();
            }
        };
        saveVATTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Ukládám DPH...");
        });
        saveVATTask.setOnSucceeded(evt -> {
            CRUDResult result = (CRUDResult) evt.getSource().getValue();
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            if (result.getResultType() == CRUDResult.ResultType.OK) {
                LOGGER.info("VAT data has been saved.");
                BoClient.getInstance().getMainController().setSystemStatus("DPH uložena");
                dialog.setSaved(true);
                dialog.setEdited(false);
                dialog.setCancelled(false);
                dialog.closeDialog();
            } else {
                BoClient.getInstance().getMainController().setSystemStatus("Chyba při ukládání DPH.");
                showCRUDresultErrorDialog(result.getMessage());
            }
        });
        saveVATTask.setOnFailed(evt -> {
            LOGGER.error("There was an error during saving of VAT data.", evt.getSource().getException());
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při ukládání DPH.");
            showSaveErrorDialog(evt.getSource().getException());
        });
        BoClient.getInstance().getTaskExecutor().submit(saveVATTask);
    }

    @Override
    protected void savePressed() {
        if (validateFields()) {
            int vatId = dataEdited == null ? 0 : dataEdited.getId();
            dataEdited = new VAT();
            dataEdited.setId(vatId);
            dataEdited.setPercentage(new BigDecimal(fieldVATPercentage.getText().trim().replace(',', '.')));
            dataEdited.setType(VATType.get(selectorVATType.getValue()));
            dataEdited.setValidFrom(datePickerValidFrom.getValue());
            if (datePickerValidFrom.getValue().isBefore(LocalDate.now())) {
                InfoDialog validityInPastInfo = new InfoDialog(InfoDialog.InfoDialogType.WARNING, dialog, true);
                validityInPastInfo.setDialogTitle("Nesprávné datum");
                validityInPastInfo.setDialogSubtitle("Datum platnosti od DPH je v minulosti.");
                validityInPastInfo.setDialogMessage("""
                        Datum platnosti OD ukládané DPH nemůže být v minulosti.
                        
                        Zadejte prosím dnešní datum nebo nějaké budoucí.
                        """);
                validityInPastInfo.showDialog();
                datePickerValidFrom.requestFocus();
                datePickerValidFrom.show();
            } else {
                save();
            }
        }
    }

    private boolean validateFields() {
        if (!fieldVATPercentage.getValidator().isValid()) {
            return false;
        }
        if (!selectorVATType.isValid()) {
            return false;
        }
        return datePickerValidFrom.getValidator().isValid();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title.setText("Nová DPH");
        datePickerValidFrom.setValue(LocalDate.now());
        initSelectorValues();
        Helpers.getEmptyTextConstraint(fieldVATPercentage, false, "Vyplňte sazbu DPH", validationLabel);
        Helpers.getEmptyTextConstraint(datePickerValidFrom, false, "Zadejte datum začátku platnosti sazby DPH.", validationLabel);
        Helpers.getNoItemSelectedConstraint(selectorVATType, "Vyberte typ sazby DPH.", validationLabel);
        Helpers.getDecimalLengthConstraint(fieldVATPercentage, false, 3, 2, "Zadejte sazbu jako celé procento.", validationLabel);
        fieldVATPercentage.setTextLimit(3);
        fieldVATPercentage.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (dataEdited.getPercentage().compareTo(new BigDecimal(newVal.trim().replace(',', '.'))) != 0)) {
                dialog.setEdited(true);
            }
        });
        datePickerValidFrom.valueProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (!dataEdited.getValidFrom().isEqual(newVal))) {
                dialog.setEdited(true);
            }
        });
        selectorVATType.selectedItemProperty().addListener((observable, oldVal, newVal) -> {
            VATType type = VATType.get(newVal);
            if (type == VATType.ZERO) {
                fieldVATPercentage.setText("0");
            }
            if (dataEdited != null) {
                type = dataEdited.getType() == null ? null : dataEdited.getType();
            }
            if (dataEdited == null || (Objects.requireNonNull(type).getLabel().compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
    }

    private void initSelectorValues() {
        Arrays.stream(VATType.values()).forEach(type -> vatTypeObservableList.add(type.getLabel()));
        selectorVATType.setItems(vatTypeObservableList);
    }
}
