package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.data.MeasureUnit;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialogController;
import cz.jbenak.npos.boClient.gui.helpers.Helpers;
import cz.jbenak.npos.boClient.gui.helpers.Utils;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private Label validationLabel;
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
                dialog.setEdited(false);
                dialog.setCancelled(false);
                dialog.closeDialog();
            } else {
                LOGGER.warn("Saved measure unit {} and present measure unit {} are not the same! Please check procedure.", savedUnit, dataEdited);
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

    @Override
    @FXML
    protected void keyPressed(KeyEvent evt) {
        if (evt.isControlDown() && evt.getCode() == KeyCode.ENTER) {
            savePressed();
        }
        if (evt.getCode() == KeyCode.ESCAPE) {
            dialog.closeDialog();
        }
    }

    @FXML
    void savePressed() {
        if (validateFields()) {
            dataEdited = new MeasureUnit();
            dataEdited.setName(fieldName.getText().trim());
            dataEdited.setUnit(fieldUnit.getText().trim());
            if (!fieldRatio.getText().trim().isEmpty()) {
                dataEdited.setRatio(new BigDecimal(fieldRatio.getText().trim().replace(',', '.')));
            }
            if (selectorBaseUnit.getSelectedIndex() >= 0) {
                dataEdited.setBaseUnit(selectorBaseUnit.getSelectedItem());
            }
            save();
        }
    }

    private boolean validateFields() {
        if (!fieldRatio.getText().isBlank() && !fieldRatio.getValidator().isValid()) {
            return false;
        }
        if (!fieldRatio.getText().isBlank() && !selectorBaseUnit.isValid()) {
            validationLabel.setText("Není vybrána hlavní měrná jednotka.");
            validationLabel.setVisible(true);
            return false;
        }
        if (fieldRatio.getText().isBlank() && selectorBaseUnit.getSelectedIndex() >= 0) {
            validationLabel.setText("Není zadán poměr vůči vybrané hlavní MJ.");
            validationLabel.setVisible(true);
            return false;
        }
        if (!fieldName.getValidator().isValid()) {
            return false;
        }
        if (!fieldUnit.getValidator().isValid()) {
            return false;
        }
        return true;
    }

    @FXML
    void cancelPressed() {
        dialog.setCancelled(true);
        dialog.closeDialog();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title.setText("Nová měrná jednotka");
        selectorBaseUnit.setItems(baseUnits);
        fieldUnit.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (dataEdited.getUnit().compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        fieldUnit.setTextLimit(5);
        fieldName.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (dataEdited.getName().compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        fieldName.setTextLimit(45);
        fieldRatio.textProperty().addListener((observable, oldVal, newVal) -> {
            if(validationLabel.isVisible()) {
                validationLabel.setVisible(false);
            }
            String ratio = "";
            if (dataEdited != null) {
                ratio = dataEdited.getRatio() == null ? "" : Utils.formatDecimalCZPlain(dataEdited.getRatio());
            }
            if (dataEdited == null || (ratio.compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        fieldRatio.setTextLimit(9);
        selectorBaseUnit.selectedItemProperty().addListener((observable, oldVal, newVal) -> {
            String baseUnit = "";
            if (dataEdited != null) {
                baseUnit = dataEdited.getBaseUnit() == null ? "" : dataEdited.getBaseUnit();
            }
            if (dataEdited == null || (baseUnit.compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        //TODO: přepsat až bude vyřešeno https://github.com/palexdev/MaterialFX/issues/252
        Helpers.getDecimalLengthConstraint(fieldRatio, true, 5,3,
                "Lze napsat pouze číslo s max. 5 celými a max. 3 desetinnými místy", validationLabel);
        Helpers.getEmptyTextConstraint(fieldUnit, false, "Je nutno vyplnit jednotku", validationLabel);
        Helpers.getEmptyTextConstraint(fieldName, false, "Je nutno vyplnit název jednotky", validationLabel);
        Helpers.getNoItemSelectedConstraint(selectorBaseUnit, "Je třeba vybrat hlavní MJ.", validationLabel);
    }
}
