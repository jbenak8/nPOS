package cz.jbenak.npos.pos.procesy.doklad.parkovani;

import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.progres.ProgresDialog;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.doklad.Doklad;
import cz.jbenak.npos.pos.procesory.DokladProcesor;
import cz.jbenak.npos.pos.system.Pos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutionException;

import static cz.jbenak.npos.pos.procesory.AutorizacniManazer.Funkce.PARKOVANI;

/**
 * Třída pro obsluhu stisku tlačítka parkování. Tlačítko parkování je na více místech (2 menu), proto zvláštní obslužná třída.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-25
 */
public class ObsluhaParkovani {

    private static final Logger LOGER = LogManager.getLogger(ObsluhaParkovani.class);
    private static final ProgresDialog PROGRES = new ProgresDialog(Pos.getInstance().getAplikacniOkno());

    public static void odparkujZaparkujDoklad() {
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(PARKOVANI)) {
            KontrolaParkovani kontrolaParkovani = new KontrolaParkovani();
            PROGRES.setText("Probíhá kontrola, zdali v databázi neexistuje odložený doklad.");
            try {
                kontrolaParkovani.run();
                boolean zaparkovanyDoklad = kontrolaParkovani.get();
                boolean otevrenyDoklad = DokladProcesor.getInstance().jeOtevrenDoklad();
                if (otevrenyDoklad && zaparkovanyDoklad) {
                    LOGER.warn("Již existuje zaparkovaný doklad.");
                    new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Již zaparkováno.",
                            "Nějaký doklad je již zaparkován. Nemohu zaparkovat tedy tento.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                }
                else if (!otevrenyDoklad && zaparkovanyDoklad) {
                    odparkujDoklad();
                }
                else if (otevrenyDoklad) {
                    zaparkujDoklad();
                } else {
                    LOGER.warn("Není co zaparkovat.");
                    new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Není co odparkovat",
                            "V databázi není lokálně uložený žádný rozpracovaný doklad, který by bylo možno odparkovat.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                }
            } catch (Exception e) {
                LOGER.error("Nastal problém při parkování nebo odparkování dokladu.", e);
                new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému.",
                        "Nastala chyba při provádění parkování nebo načtení: " + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
            } finally {
                if (PROGRES.isShowing()) {
                    PROGRES.zavriDialog();
                }
            }
        }
    }

    private static void zaparkujDoklad() throws InterruptedException, ExecutionException {
        PROGRES.setText("Doklad bude lokálně odložen (zaparkován).");
        Doklad doklad = DokladProcesor.getInstance().getDoklad();
        LOGER.info("Doklad s ID {} bude zaparkován", doklad.getId());
        Zaparkovani parkovani = new Zaparkovani(doklad);
        parkovani.run();
        if (parkovani.get()) {
            DokladProcesor.getInstance().zrusDoklad();
            HlavniOkno.getInstance().getKontroler().smazPolozky();
            HlavniOkno.getInstance().getKontroler().setParkovano(true);
            new DialogZpravy(DialogZpravy.TypZpravy.INFO, "Zaparkováno",
                    "Právě rozpracovaný doklad byl zaparkován.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        } else {
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Nepodařilo se zaparkovat",
                    "Právě rozpracovaný doklad se nepodařilo zaparkovat. Zkontrolujte prosím systémové protokoly.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        }
    }

    private static void odparkujDoklad() throws Exception {
        LOGER.info("Doklad pro odparkování bude načten.");
        PROGRES.zobrazProgres("Lokálně zaparkovaný doklad bude načten.");
        Odparkovani odparkovani = new Odparkovani();
        PROGRES.setText("Lokálně odložený doklad bude načten.");
        odparkovani.run();
        Doklad doklad = odparkovani.get();
        if (doklad != null) {
            DokladProcesor.getInstance().setDoklad(doklad);
            HlavniOkno.getInstance().getKontroler().setParkovano(false);
            HlavniOkno.getInstance().getKontroler().setPolozky(doklad.getPolozky());
            if (doklad.getZakaznik() != null) {
                HlavniOkno.getInstance().getKontroler().zobrazZakaznika(doklad.getZakaznik());
            }
        } else {
            throw new Exception("Načtený doklad je prázdný.");
        }
    }
}
