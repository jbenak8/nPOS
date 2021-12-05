/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Třída pro zobrazování data a času v hlavním okně na paneloú.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-05
 */
public class DatumCasTask extends Task<Void> {
    
    private final static Logger LOGER = LogManager.getLogger(DatumCasTask.class);
    private final Label poleCas;
    private final Label poleDatum;
    
    public DatumCasTask(Label poleCas, Label poleDatum) {
        this.poleCas = poleCas;
        this.poleDatum = poleDatum;
    }
    
    @Override
    protected Void call() throws Exception {
        LOGER.debug("Startuji proce pro aktualizaci data a času.");
        synchronized (this) {
            SimpleDateFormat sdfDatum = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat sdfCas = new SimpleDateFormat("HH:mm:ss");
            while (true) {
                try {
                    Date okamzik = new Date(System.currentTimeMillis());
                    Platform.runLater(() -> poleCas.setText(sdfCas.format(okamzik)));
                    Platform.runLater(() -> poleDatum.setText(sdfDatum.format(okamzik)));
                    this.wait(1000);
                } catch (InterruptedException ex) {
                    LOGER.error("Proces pro zobrazeni data a času nemohl být spuštěn", ex);
                }
            }
        }
    }
    
}
