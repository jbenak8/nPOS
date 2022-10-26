package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.MeasureUnit;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialogController;
import cz.jbenak.npos.boClient.gui.helpers.Utils;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MeasureUnitEditingController extends EditDialogController<MeasureUnit> {

    @FXML
    private Label title;
    @FXML
    private MFXTextField fieldUnit;
    @FXML
    private MFXTextField fieldName;
    @FXML
    private MFXComboBox<String> selectorBaseUnit;
    @FXML
    private MFXTextField fieldRatio;
    private ObservableList<String> baseUnits = FXCollections.emptyObservableList();
    private MeasureUnit unit;

    public void setBaseUnitsList(List<String> baseUnitsList) {
        baseUnits = FXCollections.observableArrayList(baseUnitsList);
        selectorBaseUnit.setItems(baseUnits);
    }

    @Override
    public void setDataEdited(MeasureUnit dataEdited) {
        this.unit = dataEdited;
        title.setText("Úprava měrné jednotky \"" + unit.getUnit() + "\"");
        fieldUnit.setText(unit.getUnit());
        fieldUnit.setDisable(true);
        fieldName.setText(unit.getName());
        fieldRatio.setText(unit.getRatio() == null ? "" : Utils.formatDecimalCZPlain(unit.getRatio()));
        if (unit.getBaseUnit() != null) {
            selectorBaseUnit.selectItem(unit.getBaseUnit());
        }
    }

    @Override
    public void save(MeasureUnit data) {

    }

    @FXML
    void savePressed() {
        unit = new MeasureUnit();
        unit.setName(fieldName.getText().trim());
        unit.setUnit(fieldUnit.getText().trim());
        if (!fieldRatio.getText().trim().isEmpty()) {
            unit.setRatio(new BigDecimal(fieldRatio.getText().trim().replace(',', '.')));
        }
        if (selectorBaseUnit.getSelectedIndex() > 0) {
            unit.setBaseUnit(selectorBaseUnit.getSelectedItem());
        }
        System.out.println(unit);
        DataOperations operations = new DataOperations();
        MeasureUnit saveResult = operations.storeMeasureUnit(unit).join();
        System.out.println(saveResult);
    }

    @FXML
    void cancelPressed() {
        dialog.closeDialog();
    }

    @FXML
    void contentChanged() {
        dialog.setEdited(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title.setText("Nová měrná jednotka");
        selectorBaseUnit.setItems(baseUnits);
    }
}
