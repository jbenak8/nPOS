package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.FinanceOperation;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialog;
import cz.jbenak.npos.boClient.gui.dialogs.generic.InfoDialog;
import cz.jbenak.npos.boClient.gui.dialogs.generic.YesNoDialog;
import cz.jbenak.npos.boClient.gui.helpers.Utils;
import cz.jbenak.npos.boClient.gui.panels.AbstractPanelContentController;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class FinanceOperationsController extends AbstractPanelContentController {

    private static final Logger LOGGER = LogManager.getLogger(FinanceOperationsController.class);
    private ObservableList<FinanceOperation> operationsList = FXCollections.emptyObservableList();
    private List<FinanceOperation> loadedOperations = new ArrayList<>();
    private final MFXTableView<FinanceOperation> table = new MFXTableView<>();

    @FXML
    private void btnNewPressed() {
        EditDialog<FinanceOperation, FinanceOperationsEditingController> newItem = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/finance-operations-edit-dialog.fxml", "Nový pohyb", this);
        newItem.preloadDialog();
        if (newItem.openNewDialog()) {
            loadData();
        }
    }

    @FXML
    private void btnEditPressed() {
        if (table.getSelectionModel().getSelectedValues().size() == 1) {
            EditDialog<FinanceOperation, FinanceOperationsEditingController> newItem = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/finance-operations-edit-dialog.fxml", "Úprava pohybu", this);
            newItem.preloadDialog();
            if (newItem.openNewDialog()) {
                loadData();
            }
        }
    }

    @FXML
    private void btnDeletePressed() {
        if (table.getSelectionModel().getSelectedValues().size() == 1) {
            FinanceOperation selected = table.getSelectionModel().getSelectedValues().get(0);
            YesNoDialog question = new YesNoDialog(BoClient.getInstance().getMainStage());
            YesNoDialog.Choice answer = question.showDialog("Smazat definici pohybu?", "Přejete si opravdu smazat vybranou definici pohybu řady číslo " + selected.getId() +
                    " - " + selected.getName() + "?");
            if (answer == YesNoDialog.Choice.YES) {
                deleteFinanceOperationDefinition(selected.getId());
            }
        }
    }

    private void deleteFinanceOperationDefinition(int id) {
        LOGGER.info("Selected finance operation definition with with number '{}' will be deleted.", id);
        Task<CRUDResult> deleteTask = new Task<>() {
            @Override
            protected CRUDResult call() {
                DataOperations operations = new DataOperations();
                return operations.deleteFinanceOperation(id).join();
            }
        };
        deleteTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Mažu vybranou definici pohybu...");
        });
        deleteTask.setOnSucceeded(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            CRUDResult result = (CRUDResult) evt.getSource().getValue();
            if (result.getResultType() == CRUDResult.ResultType.OK) {
                LOGGER.info("Finance operation definition with number '{}' has been deleted successfully.", id);
                BoClient.getInstance().getMainController().setSystemStatus("Definice pohybu byla smazána");
            }
            if (result.getResultType() == CRUDResult.ResultType.HAS_BOUND_RECORDS) {
                LOGGER.error("Finance operation definition with number {} could not be deleted because it has bound records.", id);
                BoClient.getInstance().getMainController().setSystemStatus("Definice pohybu smazána");
                InfoDialog warnDialog = new InfoDialog(InfoDialog.InfoDialogType.WARNING, BoClient.getInstance().getMainStage(), false);
                warnDialog.setDialogTitle("Nemůžu smazat definici číselné řady");
                warnDialog.setDialogMessage("Na vybranou definici pohybu s interním číslem \"" + id + "\" odkazují jiné záznamy v databázi, tedy DPH je používána.");
                warnDialog.showDialog();
            }
            if (result.getResultType() == CRUDResult.ResultType.GENERAL_ERROR) {
                LOGGER.error("There was a general error during deletion of selected finance operation definition with number {} : {}", id, evt.getSource().getMessage());
                BoClient.getInstance().getMainController().setSystemStatus("Definice pohybu nebyla smazána");
                InfoDialog errorDialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
                errorDialog.setDialogTitle("Chyba při mazání definice pohybu č. " + id);
                errorDialog.setDialogMessage(result.getMessage());
                errorDialog.showDialog();
            }
            loadData();
        });
        deleteTask.setOnFailed(evt -> {
            LOGGER.error("There was an error during deletion of selected finance operation definition with number {} : {}", id, evt.getSource().getException());
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při mazání definice pohybu.");
            Utils.showGenricErrorDialog(evt.getSource().getException(),
                    "Chyba při mazání", "Vybranou definici pohybu jsem nemohl smazat.",
                    "Vybranou definici pohybu nebylo možné smazat z důvodu jiné chyby:");
        });
        BoClient.getInstance().getTaskExecutor().submit(deleteTask);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadData() {
        prepareTable();
        LOGGER.debug("Will be loaded list of all finance operations definitions.");
        Task<List<FinanceOperation>> loadTask = new Task<>() {
            @Override
            protected List<FinanceOperation> call() {
                DataOperations operations = new DataOperations();
                return operations.getAllFinanceOperations().join();
            }
        };
        loadTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Načítám definice pohybů...");
        });
        loadTask.setOnSucceeded(evt -> {
            LOGGER.info("Finance operations definitions list has been successfully loaded.");
            if (loadedOperations != null && !loadedOperations.isEmpty()) {
                loadedOperations.clear();
                operationsList.clear();
            }
            loadedOperations = (List<FinanceOperation>) evt.getSource().getValue();
            operationsList = FXCollections.observableArrayList(loadedOperations);
            prepareTable();
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Načteno " + loadedOperations.size() + " záznamů.");
        });
        loadTask.setOnFailed(evt -> {
            Throwable e = evt.getSource().getException();
            LOGGER.error("Finance operations definitions list loading failed.", e);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při načítání definicí pohybů");
            Utils.showGenricErrorDialog(e, "Nemohu načíst definice pohybů",
                    "Nastala chyba při načítání seznamu definic pohybů",
                    "Seznam definic finančních pohybů nebyl načten z důvodu jiné chyby.");
            BoClient.getInstance().getMainController().showProgressIndicator(false);
        });
        BoClient.getInstance().getTaskExecutor().submit(loadTask);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        quickSearchField.textProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null && !newVal.isBlank()) {
                List<FinanceOperation> filtered = loadedOperations.stream().filter(itm -> {
                    final String value = newVal.trim().toLowerCase();
                    String toFilter = itm.getId() + ";"
                            + itm.getName() + ";"
                            + itm.getAccount() + ";"
                            + itm.getDocumentType().getLabel() + ";"
                            + itm.getOperationType().getLabel() + ";";
                    return toFilter.toLowerCase().contains(value);
                }).toList();
                operationsList = FXCollections.observableArrayList(filtered);
            } else {
                operationsList = FXCollections.observableArrayList(loadedOperations);
            }
            table.setItems(operationsList);
        });
    }

    @SuppressWarnings("unchecked")
    private void prepareTable() {
        ObservableList<MFXTableColumn<FinanceOperation>> columns = table.getTableColumns();

        if (columns.isEmpty()) {
            MFXTableColumn<FinanceOperation> numberColumn = new MFXTableColumn<>("Číslo položky", true, Comparator.comparing(FinanceOperation::getId));
            MFXTableColumn<FinanceOperation> operationTypeColumn = new MFXTableColumn<>("Typ operace", true, Comparator.comparing(content -> content.getOperationType().getLabel()));
            MFXTableColumn<FinanceOperation> documentTypeColumn = new MFXTableColumn<>("Typ dokladu", true, Comparator.comparing(content -> content.getDocumentType().getLabel()));
            MFXTableColumn<FinanceOperation> nameColumn = new MFXTableColumn<>("Název", true, Comparator.comparing(FinanceOperation::getName));
            MFXTableColumn<FinanceOperation> accountColumn = new MFXTableColumn<>("Předkontace", true, Comparator.comparing(FinanceOperation::getAccount));

            numberColumn.setRowCellFactory(numbering -> new MFXTableRowCell<>(FinanceOperation::getId));
            operationTypeColumn.setRowCellFactory(numbering -> new MFXTableRowCell<>(itm -> itm.getOperationType().getLabel()));
            documentTypeColumn.setRowCellFactory(numbering -> new MFXTableRowCell<>(itm -> itm.getDocumentType().getLabel()));
            nameColumn.setRowCellFactory(numbering -> new MFXTableRowCell<>(FinanceOperation::getName));
            accountColumn.setRowCellFactory(numbering -> new MFXTableRowCell<>(FinanceOperation::getAccount));

            /*
             * Possible column width changes:
             *
             numberColumn.setMinWidth(100);
             documentTypeColumn.setMinWidth(250);
             definitionColumn.setMinWidth(150);
             startFromColumn.setMinWidth(100);
             validFromColumn.setMinWidth(150);
            */
            documentTypeColumn.setMinWidth(200);
            operationTypeColumn.setMinWidth(150);
            nameColumn.setMinWidth(200);

            table.autosizeColumnsOnInitialization();
            columns.add(numberColumn);
            columns.add(operationTypeColumn);
            columns.add(documentTypeColumn);
            columns.add(nameColumn);
            columns.add(accountColumn);

            table.getFilters().addAll(
                    new IntegerFilter<>("Číslo položky", FinanceOperation::getId),
                    new StringFilter<>("Typ operace", itm -> itm.getOperationType().getLabel()),
                    new StringFilter<>("Typ dokladu", itm -> itm.getDocumentType().getLabel()),
                    new StringFilter<>("Název", FinanceOperation::getName),
                    new StringFilter<>("Předkontace", FinanceOperation::getAccount)
                    //TODO vymyslet něco na date filter
                    /*new DateFil<>("Platnost od", Country::getCurrencyIsoCode)*/
            );

            table.setFooterVisible(true);
            table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            table.getStyleClass().add("content-panel");
            table.getSelectionModel().setAllowsMultipleSelection(false);
            mainPane.setCenter(table);
        }
        table.setItems(operationsList);
    }
}
