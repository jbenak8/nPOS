package cz.jbenak.npos.pos.procesy.komunikace;

import cz.jbenak.npos.komunikace.StavSkladu;
import cz.jbenak.npos.pos.gui.registrace.hlavni.kartaSortimentu.KartaSortimentu;
import cz.jbenak.npos.pos.gui.registrace.hlavni.kartaSortimentu.KartaSortimentuKontroler;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Proces pro zjištění stavu skladu položky sortimentu na BO. Využívá se v zobrazení karty sortimentu.
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-03-03
 */
public class ZjisteniStavuSkladu extends Task<Void> {

    private final static Logger LOGER = LogManager.getLogger(ZjisteniStavuSkladu.class);
    //private boolean volaniOk;
    //private final int timeout = 10000;
    private final StavSkladu stav;
    private final KartaSortimentuKontroler kontroler;

    public ZjisteniStavuSkladu(StavSkladu stav, KartaSortimentuKontroler kontroler) {
        this.stav = stav;
        this.kontroler = kontroler;
    }

    @Override
    protected Void call() throws Exception {
        LOGER.info("Proces zjištění stavu skladu sortimentu s registrem {} byl spuštěn.", stav.getRegistr());
        synchronized (this) {
            int pokusu = 5;
            while (!zeptejSeBo() && pokusu > 0) {
                wait(1000);
                pokusu--;
            }
            if (pokusu > 0) {
                LOGER.debug("Stav skladu pro položku s registrem {} byl zjišěn a zobrazí se.", stav.getRegistr());
                Platform.runLater(() -> kontroler.zobrazStavSkladu(stav));
            } else {
                LOGER.warn("Stav skladu pro položku s registrem {} se nepodařilo zjistit.", stav.getRegistr());
                Platform.runLater(() -> kontroler.zobrazStavSkladu(null));
            }
            return null;
        }
    }

    private boolean zeptejSeBo() {
        LOGER.debug("Dotaz na stav skladu bude proveden.");
        return false;
    }
}
