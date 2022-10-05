package cz.jbenak.npos.boClient.gui.panels;

import javafx.scene.layout.BorderPane;

public interface PanelMainContentChangeable {

    void setMainContentPane(BorderPane mainContentPane);

    void setPanelController(PanelController controller);

    void loadData();
}
