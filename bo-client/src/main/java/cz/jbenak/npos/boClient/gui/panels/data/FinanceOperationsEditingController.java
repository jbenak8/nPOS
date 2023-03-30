package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.FinanceOperation;
import cz.jbenak.npos.api.shared.enums.DocumentType;
import cz.jbenak.npos.api.shared.enums.FinanceOperationType;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialogController;
import cz.jbenak.npos.boClient.gui.helpers.Helpers;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class FinanceOperationsEditingController extends EditDialogController<FinanceOperation> {

    @FXML
    private Label validationLabel;
    @FXML
    private MFXFilterComboBox<String> selectorOperationType;
    @FXML
    private MFXFilterComboBox<String> selectorDocumentType;
    @FXML
    private MFXTextField fieldName;
    @FXML
    private MFXTextField fieldAccount;

    private final ObservableList<String> operationTypeObservableList = FXCollections.observableArrayList();
    private final ObservableList<String> documentTypeObservableList = FXCollections.observableArrayList();
    private final static Logger LOGGER = LogManager.getLogger(FinanceOperationsEditingController.class);

    @Override
    public void setDataEdited(FinanceOperation dataEdited) {
        this.dataEdited = dataEdited;
        title.setText("Úprava pohybu " + dataEdited.getName());
        fieldName.setText(dataEdited.getName());
        fieldAccount.setText(dataEdited.getAccount());
        selectorOperationType.selectItem(dataEdited.getOperationType().getLabel());
        selectorDocumentType.selectItem(dataEdited.getDocumentType().getLabel());
    }

    @Override
    protected void save() {
        LOGGER.info("Finance operation definition {} will be saved.", dataEdited);
        Task<CRUDResult> saveFinanceOperationDefinition = new Task<>() {
            @Override
            protected CRUDResult call() {
                DataOperations operations = new DataOperations();
                return operations.storeFinanceOperation(dataEdited).join();
            }
        };
        saveFinanceOperationDefinition.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Ukládám definici pohybu...");
        });
        saveFinanceOperationDefinition.setOnSucceeded(evt -> {
            CRUDResult result = (CRUDResult) evt.getSource().getValue();
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            if (result.getResultType() == CRUDResult.ResultType.OK) {
                LOGGER.info("Finance operation definition data has been saved.");
                BoClient.getInstance().getMainController().setSystemStatus("Definice pohybu uložena");
                dialog.setSaved(true);
                dialog.setEdited(false);
                dialog.setCancelled(false);
                dialog.closeDialog();
            } else {
                BoClient.getInstance().getMainController().setSystemStatus("Chyba při ukládání pohybu.");
                showCRUDresultErrorDialog(result.getMessage());
            }
        });
        saveFinanceOperationDefinition.setOnFailed(evt -> {
            LOGGER.error("There was an error during saving of finance operation definition data.", evt.getSource().getException());
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při ukládání pohybu.");
            showSaveErrorDialog(evt.getSource().getException());
        });
        BoClient.getInstance().getTaskExecutor().submit(saveFinanceOperationDefinition);
    }

    @Override
    protected void savePressed() {
        if (validateFields()) {
            int id = dataEdited == null ? 0 : dataEdited.getId();
            dataEdited = new FinanceOperation();
            dataEdited.setId(id);
            dataEdited.setName(fieldName.getText().trim());
            if (!fieldAccount.getText().isBlank()) {
                dataEdited.setAccount(fieldAccount.getText().trim());
            }
            dataEdited.setOperationType(FinanceOperationType.get(selectorOperationType.getSelectedItem()));
            dataEdited.setDocumentType(DocumentType.get(selectorDocumentType.getSelectedItem()));
            save();
        }
    }

    private boolean validateFields() {
        if (!selectorDocumentType.isValid()) {
            return false;
        }
        if (!selectorOperationType.isValid()) {
            return false;
        }
        return fieldName.isValid();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title.setText("Nový pohyb");
        initSelectorValues();
        Function<String, Predicate<String>> filterFunction = searchFor -> value -> StringUtils.containsIgnoreCase(value, searchFor);
        selectorDocumentType.setFilterFunction(filterFunction);
        selectorOperationType.setFilterFunction(filterFunction);
        Helpers.getEmptyTextConstraint(fieldName, false, "Vyplňte definici číselné řady", validationLabel);
        Helpers.getNoItemSelectedConstraint(selectorDocumentType, "Vyberte typ dokladu.", validationLabel);
        Helpers.getNoItemSelectedConstraint(selectorOperationType, "Vyberte typ pohybu.", validationLabel);
        selectorDocumentType.selectedItemProperty().addListener((observable, oldVal, newVal) -> {
            DocumentType type = null;
            if (dataEdited != null) {
                type = dataEdited.getDocumentType() == null ? null : dataEdited.getDocumentType();
            }
            if (dataEdited == null || (Objects.requireNonNull(type).getLabel().compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        selectorOperationType.selectedItemProperty().addListener((observable, oldVal, newVal) -> {
            FinanceOperationType type = null;
            if (dataEdited != null) {
                type = dataEdited.getOperationType() == null ? null : dataEdited.getOperationType();
            }
            if (dataEdited == null || (Objects.requireNonNull(type).getLabel().compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        fieldName.setTextLimit(128);
        fieldName.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || dataEdited.getName().compareToIgnoreCase(newVal) != 0) {
                dialog.setEdited(true);
            }
        });
        fieldAccount.setTextLimit(10);
        fieldAccount.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || dataEdited.getAccount().compareToIgnoreCase(newVal) != 0) {
                dialog.setEdited(true);
            }
        });
    }

    private void initSelectorValues() {
        Arrays.stream(DocumentType.values()).forEach(type -> documentTypeObservableList.add(type.getLabel()));
        FXCollections.sort(documentTypeObservableList);
        selectorDocumentType.setItems(documentTypeObservableList);
        Arrays.stream(FinanceOperationType.values()).forEach(type -> operationTypeObservableList.add(type.getLabel()));
        FXCollections.sort(operationTypeObservableList);
        selectorOperationType.setItems(operationTypeObservableList);
    }
}
