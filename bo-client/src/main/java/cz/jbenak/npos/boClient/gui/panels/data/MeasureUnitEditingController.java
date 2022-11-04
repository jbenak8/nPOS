package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.data.MeasureUnit;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialogController;
import cz.jbenak.npos.boClient.gui.helpers.Utils;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MeasureUnitEditingController extends EditDialogController<MeasureUnit> {

    private static final Logger LOGGER = LogManager.getLogger(MeasureUnitEditingController.class);
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

    public void setBaseUnitsList(List<String> baseUnitsList) {
        baseUnits = FXCollections.observableArrayList(baseUnitsList);
        selectorBaseUnit.setItems(baseUnits);
    }

    @Override
    public void setDataEdited(MeasureUnit dataEdited) {
        this.dataEdited = dataEdited;
        title.setText("Úprava měrné jednotky \"" + dataEdited.getUnit() + "\"");
        fieldUnit.setText(dataEdited.getUnit());
        fieldUnit.setDisable(true);
        fieldName.setText(dataEdited.getName());
        fieldRatio.setText(dataEdited.getRatio() == null ? "" : Utils.formatDecimalCZPlain(dataEdited.getRatio()));
        if (dataEdited.getBaseUnit() != null) {
            selectorBaseUnit.selectItem(dataEdited.getBaseUnit());
        }
    }

    @Override
    protected void save() {
        LOGGER.info("Measure unit {} will be saved.", dataEdited);
        Task<MeasureUnit> saveUnitTask = new Task<>() {
            @Override
            protected MeasureUnit call() {
                DataOperations operations = new DataOperations();
                return operations.storeMeasureUnit(dataEdited).join();
            }
        };
        saveUnitTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Ukládám měrnou jednotku...");
        });
        saveUnitTask.setOnSucceeded(evt -> {
            LOGGER.info("Measure unit data has been saved.");
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("MJ uložena");
            MeasureUnit savedUnit = (MeasureUnit) evt.getSource().getValue();
            if (savedUnit.equals(dataEdited)) {
                dialog.setSaved(true);
                dialog.closeDialog();
            } else {
                LOGGER.warn("Saved measure unit {} and present measure unit {} are not the same! Please check procedure.",savedUnit, dataEdited);
                showDataNotSameDialog(savedUnit, dataEdited);
            }
        });
        saveUnitTask.setOnFailed(evt -> {
            LOGGER.error("There was an error during saving of measure unit data.", evt.getSource().getException());
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při ukládání měrné jednotky.");
            showSaveErrorDialog(evt.getSource().getException());
        });
        BoClient.getInstance().getTaskExecutor().submit(saveUnitTask);
    }

    @FXML
    void savePressed() {
        dataEdited = new MeasureUnit();
        dataEdited.setName(fieldName.getText().trim());
        dataEdited.setUnit(fieldUnit.getText().trim());
        if (!fieldRatio.getText().trim().isEmpty()) {
            dataEdited.setRatio(new BigDecimal(fieldRatio.getText().trim().replace(',', '.')));
        }
        if (selectorBaseUnit.getSelectedIndex() > 0) {
            dataEdited.setBaseUnit(selectorBaseUnit.getSelectedItem());
        }
        save();
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
