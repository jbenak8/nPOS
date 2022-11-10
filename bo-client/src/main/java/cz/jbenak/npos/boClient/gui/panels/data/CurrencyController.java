package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.Currency;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class CurrencyController extends AbstractPanelContentController {

    private static final Logger LOGGER = LogManager.getLogger(CurrencyController.class);
    private ObservableList<Currency> currencyList = FXCollections.emptyObservableList();
    private List<Currency> allCurrencies = new ArrayList<>();
    private final MFXTableView<Currency> table = new MFXTableView<>();

    @Override
    @SuppressWarnings("unchecked")
    public void loadData() {
        prepareTable();
        LOGGER.debug("Process of loading currencies from server will be prepared.");
        Task<List<Currency>> loadTask = new Task<>() {
            @Override
            protected List<Currency> call() {
                DataOperations dataOperations = new DataOperations();
                return dataOperations.getAllCurrencies().join();
            }
        };
        loadTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Načítám seznam měn...");
        });
        loadTask.setOnSucceeded(evt -> {
            LOGGER.info("Currencies has been successfully loaded.");
            if (allCurrencies != null && !allCurrencies.isEmpty()) {
                allCurrencies.clear();
                currencyList.clear();
            }
            allCurrencies = (List<Currency>) evt.getSource().getValue();
            currencyList = FXCollections.observableArrayList(allCurrencies);
            prepareTable();
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Načteno " + currencyList.size() + " záznamů.");
        });
        loadTask.setOnFailed(evt -> {
            Throwable e = evt.getSource().getException();
            LOGGER.error("Currencies list loading failed.", e);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při načítání seznamu měn");
            Utils.showGenricErrorDialog(e, "Měny nelze načíst",
                    "Nastala chyba při načítání seznamu měn.",
                    "Měny načteny z důvodu jiné chyby.");
            BoClient.getInstance().getMainController().showProgressIndicator(false);
        });
        BoClient.getInstance().getTaskExecutor().submit(loadTask);
    }

    @FXML
    private void btnNewPressed() {
        EditDialog<Currency, CurrencyEditingController> dialog = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/currency-edit-dialog.fxml", "Nová měna", this);
        dialog.preloadDialog();
        if (dialog.openNewDialog()) {
            loadData();
        }
    }

    @FXML
    private void btnEditPressed() {
        if (table.getSelectionModel().getSelectedValues().size() == 1) {
            EditDialog<Currency, CurrencyEditingController> dialog = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/currency-edit-dialog.fxml", "Úprava měny", this);
            dialog.preloadDialog();
            if (dialog.openEditDialog(table.getSelectionModel().getSelectedValues().get(0))) {
                loadData();
            }
        }
    }

    @FXML
    private void btnDeletePressed() {
        if (table.getSelectionModel().getSelectedValues().size() == 1) {
            Currency selected = table.getSelectionModel().getSelectedValues().get(0);
            YesNoDialog question = new YesNoDialog(BoClient.getInstance().getMainStage());
            YesNoDialog.Choice answer = question.showDialog("Smazat měnu?", "Přejete si opravdu smazat vybranou měnu \"" + selected.getName() + "\"?");
            if (answer == YesNoDialog.Choice.YES) {
                deleteCurrency(selected.getIsoCode());
            }
        }
    }

    @FXML
    void btnDenominationPressed() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        quickSearchField.textProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null && !newVal.isBlank()) {
                List<Currency> filtered = allCurrencies.stream().filter(itm -> {
                    final String value = newVal.trim().toLowerCase();
                    String sb = itm.getIsoCode() + ";" +
                            itm.getName().trim() + ";" +
                            itm.getSymbol().trim() + ";" +
                            (itm.isAcceptable() ? "ano" : "ne") + ";" +
                            (itm.isMain() ? "ano" : "ne") + ";";
                    return sb.toLowerCase().contains(value);
                }).toList();
                currencyList = FXCollections.observableArrayList(filtered);
            } else {
                currencyList = FXCollections.observableArrayList(allCurrencies);
            }
            table.setItems(currencyList);
        });
    }

    private void deleteCurrency(String isoCode) {
        LOGGER.info("Selected currency '{}' will be deleted.", isoCode);
        Task<CRUDResult> deleteTask = new Task<>() {
            @Override
            protected CRUDResult call() {
                DataOperations operations = new DataOperations();
                return operations.deleteCurrency(isoCode).join();
            }
        };
        deleteTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Mažu vybranou měnu...");
        });
        deleteTask.setOnSucceeded(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            CRUDResult result = (CRUDResult) evt.getSource().getValue();
            if (result.getResultType() == CRUDResult.ResultType.OK) {
                LOGGER.info("Currency'{}' has been deleted successfully.", isoCode);
                BoClient.getInstance().getMainController().setSystemStatus("Měna smazána");
            }
            if (result.getResultType() == CRUDResult.ResultType.HAS_BOUND_RECORDS) {
                LOGGER.error("Currency {} could not be deleted because it has bound records.", isoCode);
                BoClient.getInstance().getMainController().setSystemStatus("Vybraná měna nebyla smazána");
                InfoDialog warnDialog = new InfoDialog(InfoDialog.InfoDialogType.WARNING, BoClient.getInstance().getMainStage(), false);
                warnDialog.setDialogTitle("Měnu nelze smazat");
                warnDialog.setDialogMessage("Na vybranou měnu \"" + isoCode + "\" odkazují jiné záznamy v databázi, tedy tato měna je používána.");
                warnDialog.showDialog();
            }
            if (result.getResultType() == CRUDResult.ResultType.GENERAL_ERROR) {
                LOGGER.error("There was a general error during deletion of selected currency data: {}", evt.getSource().getMessage());
                BoClient.getInstance().getMainController().setSystemStatus("Měna nebyla smazána");
                InfoDialog errorDialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
                errorDialog.setDialogTitle("Chyba při mazání měny " + isoCode);
                errorDialog.setDialogMessage(result.getMessage());
                errorDialog.showDialog();
            }
            loadData();
        });
        deleteTask.setOnFailed(evt -> {
            LOGGER.error("There was an error during deletion of selected currency data.", evt.getSource().getException());
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při mazání měny.");
            Utils.showGenricErrorDialog(evt.getSource().getException(),
                    "Chyba mazání", "Vybranou měnu nebylo možno smazat.",
                    "Vybranou měnu nebylo možné smazat z důvodu jiné chyby:");
        });
        BoClient.getInstance().getTaskExecutor().submit(deleteTask);
    }

    private void prepareTable() {
        ObservableList<MFXTableColumn<Currency>> columns = table.getTableColumns();

        if (columns.isEmpty()) {
            MFXTableColumn<Currency> isoCodeColumn = new MFXTableColumn<>("ISO kód", true, Comparator.comparing(Currency::getIsoCode));
            MFXTableColumn<Currency> nameColumn = new MFXTableColumn<>("Název", true, Comparator.comparing(Currency::getName));
            MFXTableColumn<Currency> symbolColumn = new MFXTableColumn<>("Národní symbol", true, Comparator.comparing(Currency::getSymbol));
            MFXTableColumn<Currency> isAcceptableColumn = new MFXTableColumn<>("Je akceptovaná", true, Comparator.comparing(Currency::isAcceptable));
            MFXTableColumn<Currency> isMainColumn = new MFXTableColumn<>("Je hlavní", true, Comparator.comparing(Currency::isMain));

            isoCodeColumn.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getIsoCode));
            nameColumn.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getName));
            symbolColumn.setRowCellFactory(currency -> new MFXTableRowCell<>(Currency::getSymbol) {{
                setAlignment(Pos.CENTER);
            }});
            isAcceptableColumn.setRowCellFactory(unit -> new MFXTableRowCell<>(currency -> currency.isAcceptable() ? "Ano" : "Ne") {{
                setAlignment(Pos.CENTER);
            }});
            isMainColumn.setRowCellFactory(unit -> new MFXTableRowCell<>(currency -> currency.isMain() ? "Ano" : "Ne") {{
                setAlignment(Pos.CENTER);
            }});

            isoCodeColumn.setMinWidth(160);
            nameColumn.setMinWidth(200);
            symbolColumn.setMinWidth(50);
            isAcceptableColumn.setMinWidth(30);
            isMainColumn.setMinWidth(30);

            table.autosizeColumnsOnInitialization();
            columns.add(isoCodeColumn);
            columns.add(nameColumn);
            columns.add(symbolColumn);
            columns.add(isAcceptableColumn);
            columns.add(isMainColumn);

            table.setFooterVisible(false);
            table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            table.getStyleClass().add("content-panel");
            table.getSelectionModel().setAllowsMultipleSelection(false);
            mainPane.setCenter(table);
        }
        table.setItems(currencyList);
    }
}
