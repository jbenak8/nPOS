package cz.jbenak.npos.boClient.gui.main;

import cz.jbenak.npos.api.client.User;
import cz.jbenak.npos.boClient.BoClient;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class BoClientController implements Initializable {

    @FXML
    private Label labelLoggedUser;
    @FXML
    private Label labelSystemStatus;
    @FXML
    MFXProgressSpinner mainProgressIndicator;

    public void setUser() {
        User user = BoClient.getInstance().getLoggedUser();
        labelLoggedUser.setText(user.getUserName() + " " + user.getUserSurname());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainProgressIndicator.setVisible(false);
        labelSystemStatus.setText("Systém připraven");
    }
}
