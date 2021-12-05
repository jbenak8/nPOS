package cz.jbenak.npos.pos.gui.registrace.hlavni.kartaSortimentu;

import cz.jbenak.npos.komunikace.StavSkladu;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;
import cz.jbenak.npos.pos.procesory.RegistracniProcesor;
import cz.jbenak.npos.pos.procesy.komunikace.ZjisteniStavuSkladu;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Třída pro zobrazení karty sortimentu u existující položky sortimentu s možností registrace.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-03-03
 */
public class KartaSortimentu extends Stage {

    private final static Logger LOGER = LogManager.getLogger(KartaSortimentu.class);
    private final Sortiment polozka;
    private final boolean zaregistrovat;
    private KartaSortimentuKontroler kontroler;
    private Service zjisteniStavu;
    private boolean zavrenoPotvrzenim;

    /**
     * Konstruktor dialogu.
     *
     * @param polozka       Položka, jejíž data se mají zobrazit.
     * @param zaregistrovat Zdali se má položka zaregistrovat (řízení potvrzovacího tlačítka dialogu).
     */
    public KartaSortimentu(Sortiment polozka, boolean zaregistrovat) {
        this.polozka = polozka;
        this.zaregistrovat = zaregistrovat;
        initOwner(Pos.getInstance().getAplikacniOkno());
    }

    /**
     * Zobrazí dialog.
     */
    public void zobrazDialog() {
        LOGER.info("Zobrazí se dialog s informacemi o položce s registračním číslem {}.", polozka.getRegistr());
        try {
            if (this.getStyle() != StageStyle.TRANSPARENT) {
                this.initStyle(StageStyle.TRANSPARENT);
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("KartaSortimentu.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            kontroler = loader.getController();
            kontroler.setDialog(this);
            this.centerOnScreen();
            if (this.getModality() != Modality.WINDOW_MODAL) {
                this.initModality(Modality.WINDOW_MODAL);
            }
            kontroler.zobrazData();
            this.showAndWait();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit uzamykací dialog: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit dialog zobrazující kartu sortimentu dialog: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }

    /**
     * zjistí, zdali došlo k uzavření karty sortimentu potvrzením nebo registrací.
     *
     * @return původ zavření
     */
    public boolean isZavrenoPotvrzenim() {
        return zavrenoPotvrzenim;
    }

    /**
     * nastaví stav uzavření
     *
     * @param zavrenoBezPotvrzeni uzavřeno registrací nebo potvrzovacím tlačítkem?
     */
    void setZavrenoPotvrzenim(boolean zavrenoBezPotvrzeni) {
        this.zavrenoPotvrzenim = zavrenoBezPotvrzeni;
    }

    /**
     * Uzavře dialog a zastaví službu pro zjišťování stavu skladu, pokud běží.
     */
    protected void zavriDialog() {
        LOGER.info("Dialog pro zobrazení karty sortimentu bude uzavřen.");
        if (zjisteniStavu.isRunning()) {
            zjisteniStavu.cancel();
        }
        close();
    }

    /**
     * Vrátí určenou položku sortimentu.
     *
     * @return Položka sortimentu, jejíž data se mají zobrazit.
     */
    protected Sortiment getPolozka() {
        return polozka;
    }

    /**
     * Určí, zdali se má použít pro potvrzovací tlačítko volba zaregistrování.
     *
     * @return zaregistrovat, nebo jen zavřít.
     */
    protected boolean isZaregistrovat() {
        return zaregistrovat;
    }

    /**
     * Zaregistruje tuto zobrazenou položku.
     */
    protected void zaregistrujPolozku() {
        LOGER.info("tato položka s registrem {} bude zaregistrována.", polozka.getRegistr());
        RegistracniProcesor.getInstance().zaregistrujPolozku(polozka);
        zavriDialog();
    }

    /**
     * Zjistí data o zásobě a příjmu z BO.
     */
    protected void zjistiSkladovaData() {
        LOGER.info("SPustí se služba pro zjištění skladových dat položky s registrem {} na BO.", polozka.getRegistr());
        zjisteniStavu = new Service() {
            @Override
            protected Task createTask() {
                return new ZjisteniStavuSkladu(new StavSkladu(polozka.getRegistr()), kontroler);
            }
        };
        zjisteniStavu.start();
    }
}
