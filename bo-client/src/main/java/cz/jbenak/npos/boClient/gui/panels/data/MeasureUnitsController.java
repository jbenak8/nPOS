package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.boClient.api.DataOperations;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MeasureUnitsController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DataOperations dataOperations = new DataOperations();
            dataOperations.getAllMeasureUnits().join().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
