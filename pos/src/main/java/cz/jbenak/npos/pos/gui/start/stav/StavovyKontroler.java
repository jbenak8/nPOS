package cz.jbenak.npos.pos.gui.start.stav;

import cz.jbenak.npos.pos.system.Pos;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Kontroler pro stavové okno načítání aplikace
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-01-28
 */
public class StavovyKontroler implements Initializable {

    private final static Logger LOGER = LogManager.getLogger(StavovyKontroler.class);
    @FXML
    private Label verze;
    @FXML
    private Label popisStav;
    @FXML
    private ProgressBar stav;

    /**
     * Nastaví text poisující stav nad ukazatelem stavu
     * @param popis Popis právě prováděné činnosti.
     */
    public void setPopisStavu(String popis) {
        this.popisStav.setText(popis);
    }

    public void setStav(double stav) {
        this.stav.setProgress(stav);
    }

    @FXML
    private void tlZavritStisknuto() {
        LOGER.debug("Vyvoláno ukončení aplikace stisknutím tlačítka.");
        Platform.exit();
    }

    @FXML
    private void klavesaStisknuta(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ESCAPE || (evt.isAltDown() && evt.getCode() == KeyCode.F4)) {
            LOGER.debug("Vyvoláno ukončení aplikace klávesovou zkratkou. Alt: {}, klávesa: {}.", evt.isAltDown(), evt.getCode());
            Platform.exit();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGER.info("Okno zobrazující proces startu bude zobrazeno s výchozími hodnotami.");
        verze.setText("Verze: " + Pos.VERZE);
        stav.setProgress(0);
        popisStav.setText("");
    }
}
