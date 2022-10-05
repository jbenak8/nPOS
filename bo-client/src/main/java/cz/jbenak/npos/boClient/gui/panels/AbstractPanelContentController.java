package cz.jbenak.npos.boClient.gui.panels;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractPanelContentController implements Initializable, PanelMainContentChangeable {

    public BorderPane mainPane;
    public PanelController panelController;
    @Override
    public void setMainContentPane(BorderPane mainContentPane) {
        this.mainPane = mainContentPane;
    }

    @Override
    public void setPanelController(PanelController controller) {
        this.panelController = controller;
    }

    @Override
    public abstract void loadData();

    @Override
    public abstract void initialize(URL url, ResourceBundle resourceBundle);
}
