package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.data.VAT;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialog;
import cz.jbenak.npos.boClient.gui.helpers.Utils;
import cz.jbenak.npos.boClient.gui.panels.AbstractPanelContentController;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
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
            EditDialog<VAT, VATEditingController> dialog = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/vat-edit-dialog.fxml", "Nová DPH", this);
            dialog.preloadDialog();
            if (dialog.openEditDialog(table.getSelectionModel().getSelectedValues().get(0))) {
                loadData();
            }
        }
    }

    @FXML
    private void btnDeletePressed() {
        /*if (table.getSelectionModel().getSelectedValues().size() == 1) {
            Country selected = table.getSelectionModel().getSelectedValues().get(0);
            YesNoDialog question = new YesNoDialog(BoClient.getInstance().getMainStage());
            YesNoDialog.Choice answer = question.showDialog("Smazat stát?", "Přejete si opravdu smazat vybraný stát \"" + selected.getCommonName() + "\"?");
            if (answer == YesNoDialog.Choice.YES) {
                deleteCountry(selected.getIsoCode());
            }
        }*/
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switchValidOnly.setSelected(selectionStatusValidOnlyVATs);
    }

    private void prepareTable() {
        ObservableList<MFXTableColumn<VAT>> columns = table.getTableColumns();

        if (columns.isEmpty()) {
            MFXTableColumn<VAT> labelColumn = new MFXTableColumn<>("Typ daně", true, Comparator.comparing(VAT::getLabel));
            MFXTableColumn<VAT> percentageColumn = new MFXTableColumn<>("Sazba v procentech", true, Comparator.comparing(VAT::getPercentage));
            MFXTableColumn<VAT> validFromColumn = new MFXTableColumn<>("Platost od", true, Comparator.comparing(VAT::getValidFrom));
            MFXTableColumn<VAT> validToColumn = new MFXTableColumn<>("Platnostd do", true, Comparator.comparing(VAT::getValidTo));

            labelColumn.setRowCellFactory(vat -> new MFXTableRowCell<>(VAT::getLabel));
            percentageColumn.setRowCellFactory(vat -> new MFXTableRowCell<>(val -> Utils.formatDecimalCZPlain(val.getPercentage())) {{
                setAlignment(Pos.CENTER_RIGHT);
            }});
            validFromColumn.setRowCellFactory(vat -> new MFXTableRowCell<>(val -> Utils.formatDate(val.getValidFrom())) {{
                setAlignment(Pos.CENTER);
            }});
            validToColumn.setRowCellFactory(vat -> new MFXTableRowCell<>(val -> (val.getValidTo() == null ? "" : Utils.formatDate(val.getValidTo()))) {{
                setAlignment(Pos.CENTER);
            }});

            labelColumn.setMinWidth(250);
            percentageColumn.setMinWidth(150);
            validFromColumn.setMinWidth(150);
            validToColumn.setMinWidth(150);

            table.autosizeColumnsOnInitialization();
            columns.add(labelColumn);
            columns.add(percentageColumn);
            columns.add(validFromColumn);
            columns.add(validToColumn);

            table.setFooterVisible(false);
            table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            table.getStyleClass().add("content-panel");
            table.getSelectionModel().setAllowsMultipleSelection(false);
            mainPane.setCenter(table);
        }
        table.setItems(VATList);
    }
}
