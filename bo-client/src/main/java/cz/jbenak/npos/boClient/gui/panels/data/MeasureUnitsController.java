package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.data.MeasureUnit;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialog;
import cz.jbenak.npos.boClient.gui.helpers.Utils;
import cz.jbenak.npos.boClient.gui.panels.AbstractPanelContentController;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MeasureUnitsController extends AbstractPanelContentController {

    private static final Logger LOGGER = LogManager.getLogger(MeasureUnitEditingController.class);
    private ObservableList<MeasureUnit> measureUnitsList;
    private List<MeasureUnit> allMeasureUnits;
    private MeasureUnitEditingController editingController;
    private final MFXTableView<MeasureUnit> table = new MFXTableView<>();
    @FXML
    MFXTextField quickSearchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        quickSearchField.textProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != null && !newVal.isBlank()) {
                List<MeasureUnit> filtered = allMeasureUnits.stream().filter(itm -> {
                    final String value = newVal.trim().toLowerCase();
                    final StringBuilder sb = new StringBuilder();
                    sb.append(itm.getUnit()).append(";");
                    sb.append(itm.getName().trim()).append(";");
                    if (itm.getBaseUnit() != null) {
                        sb.append(itm.getUnit()).append(";");
                    }
                    if (itm.getRatio() != null) {
                        sb.append(Utils.formatDecimalCZPlain(itm.getRatio()));
                    }
                    return sb.toString().toLowerCase().contains(value);
                }).toList();
                measureUnitsList = FXCollections.observableArrayList(filtered);
            } else {
                measureUnitsList = FXCollections.observableArrayList(allMeasureUnits);
            }
            table.setItems(measureUnitsList);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadData() {
        LOGGER.debug("Process of loading measure units from server will be prepared.");
        Task<List<MeasureUnit>> loadTask = new Task<>() {
            @Override
            protected List<MeasureUnit> call() {
                DataOperations dataOperations = new DataOperations();
                return dataOperations.getAllMeasureUnits().join();
            }
        };
        loadTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Načítám seznam měrných jednotek...");
        });
        loadTask.setOnSucceeded(evt -> {
            LOGGER.info("Measure units list has been successfully loaded.");
            if (allMeasureUnits != null && !allMeasureUnits.isEmpty()) {
                allMeasureUnits.clear();
            }
            allMeasureUnits = (List<MeasureUnit>) evt.getSource().getValue();
            measureUnitsList = FXCollections.observableArrayList(allMeasureUnits);
            prepareTable();
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Načteno " + measureUnitsList.size() + " záznamů.");
        });
        loadTask.setOnFailed(evt -> {
            Throwable e = evt.getSource().getException();
            LOGGER.error("Measure units list loading failed.", e);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při načítání seznamu MJ");
            Utils.showGenricErrorDialog(e, "Měrné jednotky nelze načíst",
                    "Nastala chyba při načítání seznamu měrných jednotek.",
                    "Měrné jednotky nebyly načteny z důvodu jiné chyby.");
            BoClient.getInstance().getMainController().showProgressIndicator(false);
        });
        BoClient.getInstance().getTaskExecutor().submit(loadTask);
    }

    @FXML
    private void btnNewPressed() {
        EditDialog<MeasureUnit, MeasureUnitEditingController> dialog = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/measure-unit-edit-dialog.fxml", "Nová měrná jednotka", this);
        MeasureUnitEditingController controller = dialog.preloadDialog();
        controller.setBaseUnitsList(allMeasureUnits.stream().map(MeasureUnit::getUnit).collect(Collectors.toList()));
        if (dialog.openNewDialog()) {
            loadData();
        }
    }

    @FXML
    private void btnEditPressed() {
        if (table.getSelectionModel().getSelectedValues().size() == 1) {
            EditDialog<MeasureUnit, MeasureUnitEditingController> dialog = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/measure-unit-edit-dialog.fxml", "Úprava měrné jednotky", this);
            MeasureUnitEditingController controller = dialog.preloadDialog();
            controller.setBaseUnitsList(allMeasureUnits.stream().map(MeasureUnit::getUnit).collect(Collectors.toList()));
            if (dialog.openEditDialog(table.getSelectionModel().getSelectedValues().get(0))) {
                loadData();
            }
        }
    }

    @FXML
    private void btnDeletePressed() {

    }

    @FXML
    void btnClearFilterFieldPressed() {
        quickSearchField.clear();
    }

    @FXML
    void btnReloadPressed() {
        quickSearchField.clear();
        loadData();
    }

    private void prepareTable() {
        ObservableList<MFXTableColumn<MeasureUnit>> columns = table.getTableColumns();

        if (columns.isEmpty()) {
            MFXTableColumn<MeasureUnit> unitColumn = new MFXTableColumn<>("Měrná jednotka", true, Comparator.comparing(MeasureUnit::getUnit));
            MFXTableColumn<MeasureUnit> nameColumn = new MFXTableColumn<>("Název", true, Comparator.comparing(MeasureUnit::getName));
            MFXTableColumn<MeasureUnit> baseUnitColumn = new MFXTableColumn<>("Základní MJ", true, Comparator.comparing((measureUnit -> measureUnit.getBaseUnit() == null ? "" : measureUnit.getBaseUnit())));
            MFXTableColumn<MeasureUnit> ratioColumn = new MFXTableColumn<>("Poměr", true, Comparator.comparing(measureUnit -> measureUnit.getRatio() == null ? BigDecimal.ZERO : measureUnit.getRatio()));

            unitColumn.setRowCellFactory(unit -> new MFXTableRowCell<>(MeasureUnit::getUnit));
            nameColumn.setRowCellFactory(unit -> new MFXTableRowCell<>(MeasureUnit::getName));
            baseUnitColumn.setRowCellFactory(unit -> new MFXTableRowCell<>(measureUnit -> measureUnit.getBaseUnit() == null ? "" : measureUnit.getBaseUnit()));
            ratioColumn.setRowCellFactory(unit -> new MFXTableRowCell<>(measureUnit -> measureUnit.getRatio() == null ? "" : Utils.formatDecimalCZPlain(measureUnit.getRatio())) {{
                setAlignment(Pos.CENTER_RIGHT);
            }});

            unitColumn.setMinWidth(160);
            nameColumn.setMinWidth(200);
            baseUnitColumn.setMinWidth(150);
            ratioColumn.setMinWidth(90);

            table.autosizeColumnsOnInitialization();
            columns.add(unitColumn);
            columns.add(nameColumn);
            columns.add(baseUnitColumn);
            columns.add(ratioColumn);

            table.setFooterVisible(false);
            table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            table.getStyleClass().add("content-panel");
            mainPane.setCenter(table);
        }
        table.setItems(measureUnitsList);
    }
}
