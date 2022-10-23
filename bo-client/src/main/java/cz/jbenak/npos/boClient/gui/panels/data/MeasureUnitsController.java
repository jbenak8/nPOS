package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.data.MeasureUnit;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialog;
import cz.jbenak.npos.boClient.gui.dialogs.generic.InfoDialog;
import cz.jbenak.npos.boClient.gui.helpers.Utils;
import cz.jbenak.npos.boClient.gui.panels.AbstractPanelContentController;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MeasureUnitsController extends AbstractPanelContentController {
    private ObservableList<MeasureUnit> measureUnitsList;
    private List<MeasureUnit> allMeasureUnits;
    private MeasureUnitEditingController editingController;
    private final MFXTableView<MeasureUnit> table = new MFXTableView<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void loadData() {
        BoClient.getInstance().getMainController().showProgressIndicator(true);
        BoClient.getInstance().getMainController().setSystemStatus("Načítám seznam měrných jednotek...");
        try {
            DataOperations dataOperations = new DataOperations();
            allMeasureUnits = dataOperations.getAllMeasureUnits().join();
            measureUnitsList = FXCollections.observableArrayList(allMeasureUnits);
            prepareTable();
            BoClient.getInstance().getMainController().setSystemStatus("Načteno " + measureUnitsList.size() + " záznamů.");
        } catch (Exception e) {
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při načítání seznamu MJ");
            e.printStackTrace();
        }
        finally {
            BoClient.getInstance().getMainController().showProgressIndicator(false);
        }
    }

    @FXML
    private void btnNewPressed() {
        EditDialog<MeasureUnit, MeasureUnitEditingController> dialog = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/measure-unit-edit-dialog.fxml", "Nová měrná jednotka");
        MeasureUnitEditingController controller = dialog.preloadDialog();
        controller.setBaseUnitsList(allMeasureUnits.stream().map(MeasureUnit::getUnit).collect(Collectors.toList()));
        if (dialog.openNewDialog()) {
            loadData();
        }
    }

    @FXML
    private void bntEditPressed() {
        if (table.getSelectionModel().getSelectedValues().size() == 1) {
            EditDialog<MeasureUnit, MeasureUnitEditingController> dialog = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/measure-unit-edit-dialog.fxml", "Úprava měrné jednotky");
            MeasureUnitEditingController controller = dialog.preloadDialog();
            controller.setBaseUnitsList(allMeasureUnits.stream().map(MeasureUnit::getUnit).collect(Collectors.toList()));
            if (dialog.openEditDialog(table.getSelectionModel().getSelectedValues().get(0))) {
                loadData();
            }
        }
    }

    private void prepareTable() {
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
        ObservableList<MFXTableColumn<MeasureUnit>> columns = table.getTableColumns();
        columns.add(unitColumn);
        columns.add(nameColumn);
        columns.add(baseUnitColumn);
        columns.add(ratioColumn);
        table.setItems(measureUnitsList);
        table.setFooterVisible(false);
        table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        table.getStyleClass().add("content-panel");
        mainPane.setCenter(table);
    }
}
