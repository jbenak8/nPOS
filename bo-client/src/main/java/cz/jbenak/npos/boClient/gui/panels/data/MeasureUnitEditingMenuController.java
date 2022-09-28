package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.data.MeasureUnit;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MeasureUnitEditingMenuController implements Initializable {


    private MeasureUnitEditingController editingController;

    public void setEditingController(MeasureUnitEditingController editingController) {
        this.editingController = editingController;
    }

    @FXML
    private void savePressed() {
        editingController.savePressed();
    }

    @FXML
    private void cancelPressed() {
        editingController.cancelPressed();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
