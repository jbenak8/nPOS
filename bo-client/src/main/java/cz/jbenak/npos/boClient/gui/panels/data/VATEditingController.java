package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.VAT;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialogController;
import cz.jbenak.npos.boClient.gui.dialogs.generic.InfoDialog;
import cz.jbenak.npos.boClient.gui.helpers.Helpers;
import cz.jbenak.npos.boClient.gui.helpers.Utils;
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class VATEditingController extends EditDialogController<VAT> {

    @FXML
    private Label validationLabel;
    @FXML
    private MFXTextField fieldVATPercentage;
    @FXML
    private MFXComboBox<VATTypeSelectionItem> selectorVATType;
    @FXML
    private MFXDatePicker datePickerValidFrom;

    private final ObservableList<VATTypeSelectionItem> vatTypeObservableList = FXCollections.observableArrayList();
    private final static Logger LOGGER = LogManager.getLogger(VATEditingController.class);

    @Override
    public void setDataEdited(VAT dataEdited) {
        title.setText("Úprava DPH");
        this.dataEdited = dataEdited;
        Optional<VATTypeSelectionItem> setValue = vatTypeObservableList.stream().filter(itm -> itm.type.equals(dataEdited.getType())).findFirst();
        setValue.ifPresent(itm -> selectorVATType.selectItem(itm));
        fieldVATPercentage.setText(Utils.formatDecimalCZPlain(dataEdited.getPercentage()));
        datePickerValidFrom.setValue(dataEdited.getValidFrom());
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
            dataEdited.setType(selectorVATType.getValue().getType());
            dataEdited.setLabel(selectorVATType.getValue().toString());
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
            if (newVal.getType() == VAT.Type.ZERO) {
                fieldVATPercentage.setText("0");
            }
            VAT.Type VATType = null;
            if (dataEdited != null) {
                VATType = dataEdited.getType() == null ? null : dataEdited.getType();
            }
            if (dataEdited == null || (Objects.requireNonNull(VATType).compareTo(newVal.getType()) != 0)) {
                dialog.setEdited(true);
            }
        });
    }

    private void initSelectorValues() {
        vatTypeObservableList.add(new VATTypeSelectionItem(VAT.Type.BASE, "Základní"));
        vatTypeObservableList.add(new VATTypeSelectionItem(VAT.Type.LOWERED_1, "Snížená 1"));
        vatTypeObservableList.add(new VATTypeSelectionItem(VAT.Type.LOWERED_2, "Snížená 2"));
        vatTypeObservableList.add(new VATTypeSelectionItem(VAT.Type.ZERO, "Nulová"));
        selectorVATType.setItems(vatTypeObservableList);

    }

    private static class VATTypeSelectionItem {
        VAT.Type type;
        String translation;

        public VATTypeSelectionItem(VAT.Type type, String translation) {
            this.type = type;
            this.translation = translation;
        }

        VAT.Type getType() {
            return type;
        }

        @Override
        public String toString() {
            return translation;
        }
    }
}
