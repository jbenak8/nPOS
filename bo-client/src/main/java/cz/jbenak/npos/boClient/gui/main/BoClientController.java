package cz.jbenak.npos.boClient.gui.main;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class BoClientController implements Initializable {

    @FXML
    MFXTextField field;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        field.setText("text");
    }
}
