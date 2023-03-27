package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.DocumentNumbering;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialog;
import cz.jbenak.npos.boClient.gui.dialogs.generic.InfoDialog;
import cz.jbenak.npos.boClient.gui.dialogs.generic.YesNoDialog;
import cz.jbenak.npos.boClient.gui.helpers.Utils;
import cz.jbenak.npos.boClient.gui.panels.AbstractPanelContentController;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
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

public class NumberingSeriesController extends AbstractPanelContentController {

    private static final Logger LOGGER = LogManager.getLogger(NumberingSeriesController.class);
    private ObservableList<DocumentNumbering> numberingList = FXCollections.emptyObservableList();
    private List<DocumentNumbering> loadedNumberings = new ArrayList<>();
    private final MFXTableView<DocumentNumbering> table = new MFXTableView<>();
    private boolean selectionStatusValidOnlyNumberings = true;

    @FXML
    private MFXToggleButton switchValidOnly;

    @FXML
    private void switchValidOnlyChanged() {
        this.selectionStatusValidOnlyNumberings = switchValidOnly.isSelected();
        loadData();
    }

    @FXML
    private void btnNewPressed() {
        EditDialog<DocumentNumbering, NumberingSeriesEditingControler> newItem = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/document-numbering-series-edit-dialog.fxml", "Nová číselná řada", this);
        newItem.preloadDialog();
        if (newItem.openNewDialog()) {
            loadData();
        }
    }

    @FXML
    private void btnDeletePressed() {
        if (table.getSelectionModel().getSelectedValues().size() == 1) {
            DocumentNumbering selected = table.getSelectionModel().getSelectedValues().get(0);
            YesNoDialog question = new YesNoDialog(BoClient.getInstance().getMainStage());
            YesNoDialog.Choice answer = question.showDialog("Smazat definici řady?", "Přejete si opravdu smazat vybranou definici\nčíselné řady číslo " + selected.getNumber() +
                    " - " + selected.getLabel() + " " + selected.getDefinition() + " platnost od " + Utils.formatDate(selected.getValidFrom()) + "?");
            if (answer == YesNoDialog.Choice.YES) {
                deleteNumbering(selected.getNumber());
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadData() {
        prepareTable();
        LOGGER.debug("Process of loading document series numbering list from server will be prepared."
                + (selectionStatusValidOnlyNumberings ? "Only valid document series numbering list will be loaded." : "Will be loaded all document numbering series."));
        Task<List<DocumentNumbering>> loadTask = new Task<>() {
            @Override
            protected List<DocumentNumbering> call() {
                DataOperations dataOperations = new DataOperations();
                return dataOperations.getDocumentNumberingSeriesList(selectionStatusValidOnlyNumberings).join();
            }
        };
        loadTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Načítám číselné řady...");
        });
        loadTask.setOnSucceeded(evt -> {
            LOGGER.info("Document numbering series list has been successfully loaded.");
            if (loadedNumberings != null && !loadedNumberings.isEmpty()) {
                loadedNumberings.clear();
                numberingList.clear();
            }
            loadedNumberings = (List<DocumentNumbering>) evt.getSource().getValue();
            numberingList = FXCollections.observableArrayList(loadedNumberings);
            prepareTable();
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Načteno " + loadedNumberings.size() + " záznamů.");
        });
        loadTask.setOnFailed(evt -> {
            Throwable e = evt.getSource().getException();
            LOGGER.error("Document numbering series list loading failed.", e);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při načítání číselných řad");
            Utils.showGenricErrorDialog(e, "Nemohu načíst číselné řady",
                    "Nastala chyba při načítání seznamu číselných řad",
                    "Seznam číselných řad nebyl načten z důvodu jiné chyby.");
            BoClient.getInstance().getMainController().showProgressIndicator(false);
        });
        BoClient.getInstance().getTaskExecutor().submit(loadTask);
    }

    private void deleteNumbering(int number) {
        LOGGER.info("Selected document numbering serial definition with with number '{}' will be deleted.", number);
        Task<CRUDResult> deleteTask = new Task<>() {
            @Override
            protected CRUDResult call() {
                DataOperations operations = new DataOperations();
                return operations.deleteDocumentNumbering(number).join();
            }
        };
        deleteTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Mažu vybranou definici číselné řady...");
        });
        deleteTask.setOnSucceeded(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            CRUDResult result = (CRUDResult) evt.getSource().getValue();
            if (result.getResultType() == CRUDResult.ResultType.OK) {
                LOGGER.info("Document number series definition with number '{}' has been deleted successfully.", number);
                BoClient.getInstance().getMainController().setSystemStatus("Definice číselné řady byla smazána");
            }
            if (result.getResultType() == CRUDResult.ResultType.HAS_BOUND_RECORDS) {
                LOGGER.error("Document number series definition with number {} could not be deleted because it has bound records.", number);
                BoClient.getInstance().getMainController().setSystemStatus("Definice číselné řady smazána");
                InfoDialog warnDialog = new InfoDialog(InfoDialog.InfoDialogType.WARNING, BoClient.getInstance().getMainStage(), false);
                warnDialog.setDialogTitle("Nemůžu smazat definici číselné řady");
                warnDialog.setDialogMessage("Na vybranou definici číselné řady s interním číslem \"" + number + "\" odkazují jiné záznamy v databázi, tedy DPH je používána.");
                warnDialog.showDialog();
            }
            if (result.getResultType() == CRUDResult.ResultType.GENERAL_ERROR) {
                LOGGER.error("There was a general error during deletion of selected Document number series definition with number {} : {}", number, evt.getSource().getMessage());
                BoClient.getInstance().getMainController().setSystemStatus("DPH nebyla smazána");
                InfoDialog errorDialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
                errorDialog.setDialogTitle("Chyba při mazání definice číselné řady. " + number);
                errorDialog.setDialogMessage(result.getMessage());
                errorDialog.showDialog();
            }
            loadData();
        });
        deleteTask.setOnFailed(evt -> {
            LOGGER.error("There was an error during deletion of selected Document number series definition with number {} : {}", number, evt.getSource().getException());
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při mazání definice číselné řady.");
            Utils.showGenricErrorDialog(evt.getSource().getException(),
                    "Chyba při mazání", "Vybranou definici číselné řady jsem nemohl smazat.",
                    "Vybranou definici číselné řady nebylo možné smazat z důvodu jiné chyby:");
        });
        BoClient.getInstance().getTaskExecutor().submit(deleteTask);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switchValidOnly.setSelected(selectionStatusValidOnlyNumberings);
        quickSearchField.textProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null && !newVal.isBlank()) {
                List<DocumentNumbering> filtered = loadedNumberings.stream().filter(itm -> {
                    final String value = newVal.trim().toLowerCase();
                    String toFilter = itm.getNumber() + ";"
                            + itm.getLabel() + ";"
                            + itm.getDefinition() + ";"
                            + itm.getSequenceNumberLength() + ";"
                            + itm.getStartFrom() + ";"
                            + Utils.formatDate(itm.getValidFrom()) + ";";
                    return toFilter.toLowerCase().contains(value);
                }).toList();
                numberingList = FXCollections.observableArrayList(filtered);
            } else {
                numberingList = FXCollections.observableArrayList(loadedNumberings);
            }
            table.setItems(numberingList);
        });
    }

    @SuppressWarnings("unchecked")
    private void prepareTable() {
        ObservableList<MFXTableColumn<DocumentNumbering>> columns = table.getTableColumns();

        if (columns.isEmpty()) {
            MFXTableColumn<DocumentNumbering> numberColumn = new MFXTableColumn<>("Číslo položky", true, Comparator.comparing(DocumentNumbering::getNumber));
            MFXTableColumn<DocumentNumbering> documentTypeColumn = new MFXTableColumn<>("Typ dokladu", true, Comparator.comparing(DocumentNumbering::getLabel));
            MFXTableColumn<DocumentNumbering> definitionColumn = new MFXTableColumn<>("Definice řady", true, Comparator.comparing(DocumentNumbering::getDefinition));
            MFXTableColumn<DocumentNumbering> sequenceNumberCountColumn = new MFXTableColumn<>("Délka pořadového čísla", true, Comparator.comparing(DocumentNumbering::getSequenceNumberLength));
            MFXTableColumn<DocumentNumbering> startFromColumn = new MFXTableColumn<>("Začít od", true, Comparator.comparing(DocumentNumbering::getStartFrom));
            MFXTableColumn<DocumentNumbering> validFromColumn = new MFXTableColumn<>("Platost od", true, Comparator.comparing(DocumentNumbering::getValidFrom));

            numberColumn.setRowCellFactory(numbering -> new MFXTableRowCell<>(DocumentNumbering::getNumber));
            documentTypeColumn.setRowCellFactory(numbering -> new MFXTableRowCell<>(DocumentNumbering::getLabel));
            definitionColumn.setRowCellFactory(numbering -> new MFXTableRowCell<>(DocumentNumbering::getDefinition));
            sequenceNumberCountColumn.setRowCellFactory(numbering -> new MFXTableRowCell<>(DocumentNumbering::getSequenceNumberLength) {{
                setAlignment(Pos.CENTER);
            }});
            startFromColumn.setRowCellFactory(numbering -> new MFXTableRowCell<>(DocumentNumbering::getStartFrom) {{
                setAlignment(Pos.CENTER);
            }});
            validFromColumn.setRowCellFactory(numbering -> new MFXTableRowCell<>(val -> Utils.formatDate(val.getValidFrom())) {{
                setAlignment(Pos.CENTER);
            }});

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
            definitionColumn.setMinWidth(200);

            table.autosizeColumnsOnInitialization();
            columns.add(numberColumn);
            columns.add(documentTypeColumn);
            columns.add(definitionColumn);
            columns.add(sequenceNumberCountColumn);
            columns.add(startFromColumn);
            columns.add(validFromColumn);

            table.getFilters().addAll(
                    new IntegerFilter<>("Číslo položky", DocumentNumbering::getNumber),
                    new StringFilter<>("Typ dokumentu", DocumentNumbering::getLabel),
                    new StringFilter<>("Definice řady", DocumentNumbering::getDefinition),
                    new IntegerFilter<>("Délka pořadového čísla", DocumentNumbering::getSequenceNumberLength),
                    new IntegerFilter<>("Začít od", DocumentNumbering::getStartFrom)
                    //TODO vymyslet něco na date filter
                    /*new DateFil<>("Platost od", Country::getCurrencyIsoCode)*/
            );

            table.setFooterVisible(true);
            table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            table.getStyleClass().add("content-panel");
            table.getSelectionModel().setAllowsMultipleSelection(false);
            mainPane.setCenter(table);
        }
        table.setItems(numberingList);
    }
}
