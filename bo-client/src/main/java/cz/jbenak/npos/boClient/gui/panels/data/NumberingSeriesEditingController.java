package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.DocumentNumbering;
import cz.jbenak.npos.api.shared.enums.DocumentType;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialogController;
import cz.jbenak.npos.boClient.gui.dialogs.generic.InfoDialog;
import cz.jbenak.npos.boClient.gui.helpers.Helpers;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class NumberingSeriesEditingController extends EditDialogController<DocumentNumbering> {

    @FXML
    private Label validationLabel;
    @FXML
    private MFXDatePicker datePickerValidFrom;
    @FXML
    private MFXTextField fieldDefinition;
    @FXML
    private MFXTextField fieldSeqNumberLength;
    @FXML
    private MFXTextField fieldStartFrom;
    @FXML
    private MFXFilterComboBox<String> selectorDocumentType;
    private final ObservableList<String> numberingTypeObservableList = FXCollections.observableArrayList();
    private final static Logger LOGGER = LogManager.getLogger(VATEditingController.class);

    @Override
    public void setDataEdited(DocumentNumbering dataEdited) {
    }

    @Override
    protected void save() {
        LOGGER.info("Document numbering series definition {} will be saved.", dataEdited);
        Task<CRUDResult> saveDefinitionTask = new Task<>() {
            @Override
            protected CRUDResult call() {
                DataOperations operations = new DataOperations();
                return operations.storeDocumentNumbering(dataEdited).join();
            }
        };
        saveDefinitionTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Ukládám definici číselné řady...");
        });
        saveDefinitionTask.setOnSucceeded(evt -> {
            CRUDResult result = (CRUDResult) evt.getSource().getValue();
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            if (result.getResultType() == CRUDResult.ResultType.OK) {
                LOGGER.info("Document numbering series definition data has been saved.");
                BoClient.getInstance().getMainController().setSystemStatus("Definice číselné řady uložena");
                dialog.setSaved(true);
                dialog.setEdited(false);
                dialog.setCancelled(false);
                dialog.closeDialog();
            } else {
                BoClient.getInstance().getMainController().setSystemStatus("Chyba při ukládání DPH.");
                showCRUDresultErrorDialog(result.getMessage());
            }
        });
        saveDefinitionTask.setOnFailed(evt -> {
            LOGGER.error("There was an error during saving of Document numbering series definition data.", evt.getSource().getException());
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při ukládání definice číselné řady.");
            showSaveErrorDialog(evt.getSource().getException());
        });
        BoClient.getInstance().getTaskExecutor().submit(saveDefinitionTask);
    }

    @Override
    protected void savePressed() {
        if (validateFields()) {
            int id = dataEdited == null ? 0 : dataEdited.getId();
            dataEdited = new DocumentNumbering();
            dataEdited.setId(id);
            dataEdited.setDocumentType(DocumentType.get(selectorDocumentType.getSelectedItem()));
            dataEdited.setDefinition(fieldDefinition.getText().trim());
            dataEdited.setSequenceNumberLength(Integer.parseInt(fieldSeqNumberLength.getText().trim()));
            dataEdited.setStartFrom(Integer.parseInt(fieldStartFrom.getText().trim()));
            dataEdited.setValidFrom(datePickerValidFrom.getValue());
            if (datePickerValidFrom.getValue().isBefore(LocalDate.now())) {
                InfoDialog validityInPastInfo = new InfoDialog(InfoDialog.InfoDialogType.WARNING, dialog, true);
                validityInPastInfo.setDialogTitle("Nesprávné datum");
                validityInPastInfo.setDialogSubtitle("Datum platnosti od definice číselné řady je v minulosti.");
                validityInPastInfo.setDialogMessage("""
                        Datum platnosti OD ukládané definice číselné řady nemůže být v minulosti.
                                                
                        Zadejte prosím dnešní datum nebo nějaké budoucí.
                        """);
                validityInPastInfo.showDialog();
                datePickerValidFrom.requestFocus();
                datePickerValidFrom.show();
            } else if (dataEdited.getDefinition().length() + dataEdited.getSequenceNumberLength() > 25) {
                InfoDialog definitionTooLong = new InfoDialog(InfoDialog.InfoDialogType.WARNING, dialog, true);
                definitionTooLong.setDialogTitle("Příliš velká definice řady");
                definitionTooLong.setDialogSubtitle("Výsledné číslo bude příliš velké");
                definitionTooLong.setDialogMessage("Zadaná kombinace definice řady " + fieldDefinition.getText() + " a počet míst pořadového čísla " + fieldSeqNumberLength.getText() + "\n"
                        + "vytvoří řadu, jejíž délka je více, než maximálně povolených 25 znaků.");
                definitionTooLong.showDialog();
                fieldDefinition.requestFocus();
            } else {
                save();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title.setText("Nová řada");
        datePickerValidFrom.setValue(LocalDate.now());
        fieldSeqNumberLength.setText("0");
        fieldStartFrom.setText("1");
        initSelectorValues();
        Function<String, Predicate<String>> filterFunction = searchFor -> value -> StringUtils.containsIgnoreCase(value, searchFor);
        selectorDocumentType.setFilterFunction(filterFunction);
        Helpers.getEmptyTextConstraint(fieldDefinition, false, "Vyplňte definici číselné řady", validationLabel);
        Helpers.getEmptyTextConstraint(datePickerValidFrom, false, "Zadejte datum začátku platnosti sazby definice.", validationLabel);
        Helpers.getNoItemSelectedConstraint(selectorDocumentType, "Vyberte typ dokladu.", validationLabel);
        Helpers.getRegexConstraint(fieldSeqNumberLength, false, "\\d+", "Vyplňte počet míst pořadového čísla. Pouze čísly.", validationLabel);
        Helpers.getRegexConstraint(fieldStartFrom, false, "\\d+", "Vyplňte odkud mám číslovat. Číslem.", validationLabel);
        datePickerValidFrom.valueProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (!dataEdited.getValidFrom().isEqual(newVal))) {
                dialog.setEdited(true);
            }
        });
        fieldDefinition.setTextLimit(64);
        fieldDefinition.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (dataEdited.getDefinition().compareToIgnoreCase(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        fieldSeqNumberLength.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (Integer.toString(dataEdited.getSequenceNumberLength()).compareToIgnoreCase(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        fieldStartFrom.setTextLimit(9);
        fieldStartFrom.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (Integer.toString(dataEdited.getStartFrom()).compareToIgnoreCase(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        selectorDocumentType.selectedItemProperty().addListener((observable, oldVal, newVal) -> {
            DocumentType type = null;
            if (dataEdited != null) {
                type = dataEdited.getDocumentType() == null ? null : dataEdited.getDocumentType();
            }
            if (dataEdited == null || (Objects.requireNonNull(type).getLabel().compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
    }

    private boolean validateFields() {
        if (!selectorDocumentType.isValid()) {
            return false;
        }
        if (!fieldDefinition.isValid()) {
            return false;
        }
        if (!fieldSeqNumberLength.isValid()) {
            return false;
        }
        if (!fieldStartFrom.isValid()) {
            return false;
        }
        return datePickerValidFrom.getValidator().isValid();
    }

    private void initSelectorValues() {
        for (DocumentType type: DocumentType.values()) {
            numberingTypeObservableList.add(type.getLabel());
        }
        FXCollections.sort(numberingTypeObservableList);
        selectorDocumentType.setItems(numberingTypeObservableList);
    }
}

