package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.VAT;
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
import io.github.palexdev.materialfx.filter.BigDecimalFilter;
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

public class VATController extends AbstractPanelContentController {

    private static final Logger LOGGER = LogManager.getLogger(VATController.class);
    private ObservableList<VAT> VATList = FXCollections.emptyObservableList();
    private List<VAT> loadedVATs = new ArrayList<>();
    private final MFXTableView<VAT> table = new MFXTableView<>();
    private boolean selectionStatusValidOnlyVATs = true;

    @FXML
    private MFXToggleButton switchValidOnly;

    @Override
    @SuppressWarnings("unchecked")
    public void loadData() {
        prepareTable();
        LOGGER.debug("Process of loading VAT list from server will be prepared."
                + (selectionStatusValidOnlyVATs ? "Only valid VAT list will be loaded." : "Will be loaded all VAT."));
        Task<List<VAT>> loadTask = new Task<>() {
            @Override
            protected List<VAT> call() {
                DataOperations dataOperations = new DataOperations();
                return dataOperations.getVATList(selectionStatusValidOnlyVATs).join();
            }
        };
        loadTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Načítám seznam DPH...");
        });
        loadTask.setOnSucceeded(evt -> {
            LOGGER.info("VAT list has been successfully loaded.");
            if (loadedVATs != null && !loadedVATs.isEmpty()) {
                loadedVATs.clear();
                VATList.clear();
            }
            loadedVATs = (List<VAT>) evt.getSource().getValue();
            VATList = FXCollections.observableArrayList(loadedVATs);
            prepareTable();
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Načteno " + loadedVATs.size() + " záznamů.");
        });
        loadTask.setOnFailed(evt -> {
            Throwable e = evt.getSource().getException();
            LOGGER.error("VAT list loading failed.", e);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při načítání seznamu DPH");
            Utils.showGenricErrorDialog(e, "Nemohu načíst DPH",
                    "Nastala chyba při načítání seznamu DPH",
                    "Seznam DPH nebyl načten z důvodu jiné chyby.");
            BoClient.getInstance().getMainController().showProgressIndicator(false);
        });
        BoClient.getInstance().getTaskExecutor().submit(loadTask);
    }

    @FXML
    private void switchValidOnlyChanged() {
        this.selectionStatusValidOnlyVATs = switchValidOnly.isSelected();
        loadData();
    }

    @FXML
    private void btnNewPressed() {
        EditDialog<VAT, VATEditingController> dialog = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/vat-edit-dialog.fxml", "Nová DPH", this);
        dialog.preloadDialog();
        if (dialog.openNewDialog()) {
            loadData();
        }
    }

    @FXML
    private void btnEditPressed() {
        if (table.getSelectionModel().getSelectedValues().size() == 1) {
            VAT selectedVAT = table.getSelectionModel().getSelectedValues().get(0);
            if (isVatValid(selectedVAT)) {
                EditDialog<VAT, VATEditingController> dialog = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/vat-edit-dialog.fxml", "Nová DPH", this);
                dialog.preloadDialog();
                if (dialog.openEditDialog(table.getSelectionModel().getSelectedValues().get(0))) {
                    loadData();
                }
            } else {
                InfoDialog inf = new InfoDialog(InfoDialog.InfoDialogType.INFO, BoClient.getInstance().getMainStage(), true);
                inf.setDialogMessage("Zadaná DPH je již neplatná a není možné ji upravovat.");
                inf.setDialogTitle("Nelze upravit");
                inf.setDialogSubtitle("Zadanou DPH nelze upravit");
                inf.showDialog();
            }
        }
    }

    private boolean isVatValid(VAT selectedVAT) {
        loadedVATs.sort(Comparator.comparing(VAT::getValidFrom));
        return loadedVATs.indexOf(selectedVAT) == loadedVATs.size() - 1;
    }

    @FXML
    private void btnDeletePressed() {
        if (table.getSelectionModel().getSelectedValues().size() == 1) {
            VAT selected = table.getSelectionModel().getSelectedValues().get(0);
            YesNoDialog question = new YesNoDialog(BoClient.getInstance().getMainStage());
            YesNoDialog.Choice answer = question.showDialog("Smazat DPH?", "Přejete si opravdu smazat vybranou DPH " + selected.getId() + "" +
                    " - " + selected.getLabel() + " " + selected.getPercentage() + "% ?");
            if (answer == YesNoDialog.Choice.YES) {
                deleteVAT(selected.getId());
            }
        }
    }

    private void deleteVAT(int VATid) {
        LOGGER.info("Selected VAT with ID '{}' will be deleted.", VATid);
        Task<CRUDResult> deleteTask = new Task<>() {
            @Override
            protected CRUDResult call() {
                DataOperations operations = new DataOperations();
                return operations.deleteVAT(VATid).join();
            }
        };
        deleteTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Mažu vybranou DPH...");
        });
        deleteTask.setOnSucceeded(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            CRUDResult result = (CRUDResult) evt.getSource().getValue();
            if (result.getResultType() == CRUDResult.ResultType.OK) {
                LOGGER.info("VAT with ID '{}' has been deleted successfully.", VATid);
                BoClient.getInstance().getMainController().setSystemStatus("Měrná jednotka smazána");
            }
            if (result.getResultType() == CRUDResult.ResultType.HAS_BOUND_RECORDS) {
                LOGGER.error("VAT with ID {} could not be deleted because it has bound records.", VATid);
                BoClient.getInstance().getMainController().setSystemStatus("DPH nebyla smazána");
                InfoDialog warnDialog = new InfoDialog(InfoDialog.InfoDialogType.WARNING, BoClient.getInstance().getMainStage(), false);
                warnDialog.setDialogTitle("Nemůžu smazat vybranou DPH");
                warnDialog.setDialogMessage("Na vybranou DPH s interním číslem \"" + VATid + "\" odkazují jiné záznamy v databázi, tedy DPH je používána.");
                warnDialog.showDialog();
            }
            if (result.getResultType() == CRUDResult.ResultType.GENERAL_ERROR) {
                LOGGER.error("There was a general error during deletion of selected VAT with ID {} : {}", VATid, evt.getSource().getMessage());
                BoClient.getInstance().getMainController().setSystemStatus("DPH nebyla smazána");
                InfoDialog errorDialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
                errorDialog.setDialogTitle("Chyba při mazání DPH " + VATid);
                errorDialog.setDialogMessage(result.getMessage());
                errorDialog.showDialog();
            }
            loadData();
        });
        deleteTask.setOnFailed(evt -> {
            LOGGER.error("There was an error during deletion of selected VAT with ID {} : {}", VATid, evt.getSource().getException());
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při mazání DPH.");
            Utils.showGenricErrorDialog(evt.getSource().getException(),
                    "Chyba při mazání", "Vybranou DPH jsem nemohl smazat.",
                    "Vybranou DPH nebylo možné smazat z důvodu jiné chyby:");
        });
        BoClient.getInstance().getTaskExecutor().submit(deleteTask);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switchValidOnly.setSelected(selectionStatusValidOnlyVATs);
        quickSearchField.textProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null && !newVal.isBlank()) {
                List<VAT> filtered = loadedVATs.stream().filter(itm -> {
                    final String value = newVal.trim().toLowerCase();
                    String sb = itm.getId() + ";"
                            + itm.getLabel() + ";"
                            + Utils.formatDecimalCZPlain(itm.getPercentage()) + ";"
                            + Utils.formatDate(itm.getValidFrom()) + ";";
                    return sb.toLowerCase().contains(value);
                }).toList();
                VATList = FXCollections.observableArrayList(filtered);
            } else {
                VATList = FXCollections.observableArrayList(loadedVATs);
            }
            table.setItems(VATList);
        });
    }

    @SuppressWarnings("unchecked")
    private void prepareTable() {
        ObservableList<MFXTableColumn<VAT>> columns = table.getTableColumns();

        if (columns.isEmpty()) {
            MFXTableColumn<VAT> idColumn = new MFXTableColumn<>("Číslo položky", true, Comparator.comparing(VAT::getId));
            MFXTableColumn<VAT> labelColumn = new MFXTableColumn<>("Typ daně", true, Comparator.comparing(VAT::getLabel));
            MFXTableColumn<VAT> percentageColumn = new MFXTableColumn<>("Sazba v procentech", true, Comparator.comparing(VAT::getPercentage));
            MFXTableColumn<VAT> validFromColumn = new MFXTableColumn<>("Platost od", true, Comparator.comparing(VAT::getValidFrom));

            idColumn.setRowCellFactory(vat -> new MFXTableRowCell<>(VAT::getId));
            labelColumn.setRowCellFactory(vat -> new MFXTableRowCell<>(VAT::getLabel));
            percentageColumn.setRowCellFactory(vat -> new MFXTableRowCell<>(val -> Utils.formatDecimalCZPlain(val.getPercentage())) {{
                setAlignment(Pos.CENTER_RIGHT);
            }});
            validFromColumn.setRowCellFactory(vat -> new MFXTableRowCell<>(val -> Utils.formatDate(val.getValidFrom())) {{
                setAlignment(Pos.CENTER);
            }});

            idColumn.setMinWidth(100);
            labelColumn.setMinWidth(250);
            percentageColumn.setMinWidth(150);
            validFromColumn.setMinWidth(150);

            table.autosizeColumnsOnInitialization();
            columns.add(idColumn);
            columns.add(labelColumn);
            columns.add(percentageColumn);
            columns.add(validFromColumn);

            table.getFilters().addAll(
                    new IntegerFilter<>("Číslo položky", VAT::getId),
                    new StringFilter<>("Typ daně", VAT::getLabel),
                    new BigDecimalFilter<>("Sazba v procentech", VAT::getPercentage)
                    //TODO vymyslet něco na date filter
                    /*new DateFil<>("Platost od", Country::getCurrencyIsoCode)*/
            );

            table.setFooterVisible(true);
            table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            table.getStyleClass().add("content-panel");
            table.getSelectionModel().setAllowsMultipleSelection(false);
            mainPane.setCenter(table);
        }
        table.setItems(VATList);
    }
}
