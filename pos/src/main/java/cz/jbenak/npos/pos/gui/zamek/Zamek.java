package cz.jbenak.npos.pos.gui.zamek;

import cz.jbenak.npos.pos.gui.prihlaseni.LoginDialog;
import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.procesory.DokladProcesor;
import cz.jbenak.npos.pos.procesy.prihlaseni.OdemceniPokladny;
import cz.jbenak.npos.pos.system.Pos;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Třída pro uzamčení obrazovky a následné ověření pro odemnknutí.
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-03-01
 *
 */
public class Zamek extends Stage {

    private final static Logger LOGER = LogManager.getLogger(Zamek.class);
    private ZamekKontroler kontroler;

    /**
     * Konstruktor GUI
     */
    public Zamek() {
        this.initOwner(Pos.getInstance().getAplikacniOkno());
    }

    /**
     * Zobrazí uzamykací dialog a de facto uzamkne GUI programu.
     */
    public void zobrazDialog() {
        LOGER.info("Uzamykací dialog bude zobrazen.");
        try {
            if (this.getStyle() != StageStyle.TRANSPARENT) {
                this.initStyle(StageStyle.TRANSPARENT);
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Zamek.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            kontroler = loader.getController();
            kontroler.setDialog(this);
            this.centerOnScreen();
            if (this.getModality() != Modality.WINDOW_MODAL) {
                this.initModality(Modality.WINDOW_MODAL);
            }
            this.show();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit uzamykací dialog: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit uzamykací dialog: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }

    /**
     * Provede odemčení programu.
     * @param pin zadaný PIN.
     */
    protected void odemkniProgram(int pin) {
        LOGER.debug("Heslo pro odemčení bylo zadáno.");
        OdemceniPokladny proces = new OdemceniPokladny(HlavniOkno.getInstance().getPrihlasenaPokladni(), pin);
        proces.run();
        try {
            OdemceniPokladny.StavOdemceni stav = proces.get();
            switch (stav) {
                case MUZE_SE_ODEMKNOUT:
                    LOGER.info("Odemčení bylo úspěšné. Dialog bude uzavřen.");
                    close();
                    break;
                case SPATNE_HESLO:
                    LOGER.warn("Uživatel zadal špatné heslo. Bude vyzván k opakování zadání.");
                    new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Špatné heslo", "Zadali jste nesprávné heslo. Prosím opakujte. Upozorňuji, že máte "
                            + LoginDialog.getTextPocetPokusu(HlavniOkno.getInstance().getPrihlasenaPokladni().getPocetPrihlasovacichPokusu()), this).zobrazDialogAPockej();
                    kontroler.fokus();
                    break;
                case UZIVATEL_ZABLOKOVAN:
                    LOGER.warn("Uživatel byl zablokován. Bude provedeno nucené odhlášení. Případný rozpracovaný doklad bude zahozen.");
                    new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Pokladní zablokován", "Vyčerpali jste maximální počet pokusů o přihlášení a došlo k zablokování. " +
                            "Nyní bude provedeno nucené odhlášení. Případný rozpracovaný doklad bude zahozen.", this).zobrazDialogAPockej();
                    if (DokladProcesor.getInstance().jeOtevrenDoklad()) {
                        DokladProcesor.getInstance().zrusDoklad();
                    }
                    close();
                    HlavniOkno.getInstance().uzavriHlavniOkno(true);
                    break;
                    default:
                        break;
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGER.error("Nastal problém při zpracování výsledků procesu ověření uživatelského hesla: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nastal problém při zpracování výsledků procesu ověření uživatelského hesla: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            close();
        }
    }
}
