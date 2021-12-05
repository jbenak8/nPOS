/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni;

import cz.jbenak.npos.pos.gui.prihlaseni.LoginDialog;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.progres.ProgresDialog;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.doklad.TypDokladu;
import cz.jbenak.npos.pos.objekty.filialka.Smena;
import cz.jbenak.npos.pos.objekty.sezeni.Pokladni;
import cz.jbenak.npos.pos.procesory.AutorizacniManazer;
import cz.jbenak.npos.pos.procesory.DokladProcesor;
import cz.jbenak.npos.pos.procesy.doklad.parkovani.KontrolaParkovani;
import cz.jbenak.npos.pos.procesy.prihlaseni.NacteniSmeny;
import cz.jbenak.npos.pos.system.Pos;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Třída pro zobrazení hlavního okna.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-04
 */
public final class HlavniOkno {

    private static final Logger LOGER = LogManager.getLogger(HlavniOkno.class);
    private static volatile HlavniOkno instance;
    private final AutorizacniManazer autorizacniManazer;
    private HlavniOknoKontroler kontroler;
    private Pokladni prihlasenaPokladni;
    private boolean skoliciRezimZapnut;
    private Smena aktualniSmena = null;

    /**
     * Privátní konstruktor pro zajištění jedinečnosti.
     */
    private HlavniOkno() {
        autorizacniManazer = new AutorizacniManazer();
    }

    /**
     * Metoda pro vrácení instance okna. Pokud je null, vytvoří se.
     *
     * @return vláknově bezpečná instance hlavního okna.
     */
    public static HlavniOkno getInstance() {
        HlavniOkno okno = HlavniOkno.instance;
        if (okno == null) {
            synchronized (HlavniOkno.class) {
                okno = HlavniOkno.instance;
                if (okno == null) {
                    HlavniOkno.instance = okno = new HlavniOkno();
                }
            }
        }
        return okno;
    }

    /**
     * Metoda pro získání instance kontroléru hlavního okna.
     *
     * @return kontrolér hlavního okna.
     */
    public HlavniOknoKontroler getKontroler() {
        return kontroler;
    }

    /**
     * Vrátí aktuálně přihlášenou pokladní.
     *
     * @return Objekt obsahující aktuálně přihlášenou pokladní pro další funkce
     * a kontrolu přihlášení z BO.
     */
    public Pokladni getPrihlasenaPokladni() {
        return prihlasenaPokladni;
    }

    /**
     * Metoda pro získání manažera autorizací pro jednotlivé funkce dle
     * přihlášené pokladní.
     *
     * @return Autorizační manažer.
     */
    public AutorizacniManazer getAutorizacniManazer() {
        return autorizacniManazer;
    }

    /**
     * Metoda pro zjištění otevřené změny pro právě přihlášené pokladní.
     *
     * @return Aktuální právě otevřená směna.
     */
    public Smena getAktualniSmena() {
        return aktualniSmena;
    }

    /**
     * Metoda pro zobrazení hlavního okna. Při zobrazení se předá aktuálně
     * přihlášený pokladní, protože přihlášení je výchozím bodem pro zobrazení
     * hlavního okna.
     *
     * @param prihlasenaPokladni aktuálně přihlášená pokladní.
     */
    public void zobrazHlavniOkno(Pokladni prihlasenaPokladni) {
        LOGER.info("Hlavní okno programu bude zobrazeno.");
        this.prihlasenaPokladni = prihlasenaPokladni;
        if (zjistiOtevrenouSmenu() && zjistiInicialniCislaDokladu()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("HlavniOkno.fxml"));
                Scene hlOknoScena = new Scene(loader.load(), Color.TRANSPARENT);
                kontroler = loader.getController();
                kontroler.setOkno(this);
                //Toolkit.getDefaultToolkit().beep();
                Pos.getInstance().getAplikacniOkno().setScene(hlOknoScena);
                Pos.getInstance().getAplikacniOkno().centerOnScreen();
                kontroler.setPokladniData();
                zkontrolujZaparkovanyDoklad();
                zkontrolujBinarniKopiiDokladu();
            } catch (IOException e) {
                LOGER.error("Nepodařilo se zobrazit hlavní okno: ", e);
                new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit hlavní okno: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
                Platform.exit();
            }
        } else {
            Pos.getInstance().getManazerKomunikace().odhlasPokladni(prihlasenaPokladni);
            LOGER.error("Nepodařilo se zjistit nebo otevřít aktivní směnu nebo prvotní pořadová čísla dokladů.");
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Nemohu spustit pokladní funkce", "Nepodařilo se zjistit nebo otevřít aktivní směnu nebo prvotní pořadová čísla dokladů.", Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }

    /**
     * Metoda pro uzavření hlavního okna. Kontrola rozpracovaného dokladu není
     * nutná, protože v rozpracovaném dokladu nebude tato funkce v menu dostupná.
     * Pokud přijat příkaz na pouhé odhlášení a následně se zobrazí přihlašovací
     * okno. Pokud nebude přijat, program se ukončí.
     *
     * @param odhlasit Příkaz pro odhlášení, pokud bude true.
     */
    public void uzavriHlavniOkno(boolean odhlasit) {
        LOGER.info("Pokladní bude odhlášen a okno bude uzavřeno. Bude zobrazen přihlašovací dialog.");
        if (!Pos.getInstance().getManazerKomunikace().odhlasPokladni(prihlasenaPokladni)) {
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Odhlášní pokladní z BO neúspěšné", """
                    Nepodařilo se odhlásit uživatele ze serveru kanceláře.

                    Pokud budete provádět v Kanceláři finanční fukce, jako např. denní uzávěrku, bude nutné pokladní v kanceláři nuceně odhlásit a pokračovat se zvýšenou opatrností.""", Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
        this.prihlasenaPokladni = null;
        if (odhlasit) {
            LOGER.info("Okno bude uzavřeno. Bude zobrazen přihlašovací dialog.");
            new LoginDialog().zobrazDialog();
            instance = null;
        } else {
            LOGER.info("Program bude ukončen.");
            Platform.exit();
        }

    }

    /**
     * Zjistí aktivaci školícího režimu.
     *
     * @return školící režim aktivní.
     */
    public boolean isSkoliciRezimZapnut() {
        return skoliciRezimZapnut;
    }

    /**
     * Zapne nebo vypne školící režim na základě aktivace tlačítka - řízeno kotnrolerem.
     *
     * @param skoliciRezimZapnut vypnout nebo zapnout školící režim.
     */
    protected void setSkoliciRezimZapnut(boolean skoliciRezimZapnut) {
        this.skoliciRezimZapnut = skoliciRezimZapnut;
    }

    private boolean zjistiInicialniCislaDokladu() {
        //TODO zatím prvontí inicializace
        Pos.getInstance().getInicialniPoradovaCisla().put(TypDokladu.PARAGON, 0);
        Pos.getInstance().getInicialniPoradovaCisla().put(TypDokladu.DODACI_LIST, 0);
        Pos.getInstance().getInicialniPoradovaCisla().put(TypDokladu.FAKTURA, 0);
        Pos.getInstance().getInicialniPoradovaCisla().put(TypDokladu.DOBROPIS, 0);
        Pos.getInstance().getInicialniPoradovaCisla().put(TypDokladu.VKLAD, 0);
        Pos.getInstance().getInicialniPoradovaCisla().put(TypDokladu.VYBER, 0);
        return true;
    }

    private boolean zjistiOtevrenouSmenu() {
        NacteniSmeny nacteniSmeny = new NacteniSmeny();
        nacteniSmeny.run();
        try {
            aktualniSmena = nacteniSmeny.get();
        } catch (Exception e) {
            LOGER.error("Nepodařilo se načíst aktivní směnu z lokální databáze:", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se načíst aktivní směnu: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            return false;
        }
        if (aktualniSmena == null) {
            aktualniSmena = Pos.getInstance().getManazerKomunikace().otevriSmenu();
            if (aktualniSmena == null) {
                LOGER.warn("Nepodařilo se otevřít směnu pro přihlášenou pokladní:");
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Chyba systému", "Nepodařilo se otevřít směnu pro přihlášenou pokladní. Zkontrolujte připojení k Backoffice.", Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
                return false;
            }
        }
        return true;
    }

    private void zkontrolujZaparkovanyDoklad() {
        LOGER.info("Zkontroluje se přítomnost zaparkovaného dokladu.");
        KontrolaParkovani kontrola = new KontrolaParkovani();
        ProgresDialog progres = new ProgresDialog(Pos.getInstance().getAplikacniOkno());
        progres.zobrazProgres("Kontroluji přítomnost zaparkovaného dokladu...");
        kontrola.run();
        try {
            boolean zaparkovano = kontrola.get();
            LOGER.info("Zaparkovaný doklad byl nalezen: " + zaparkovano);
            kontroler.setZaparkovanyDoklad(zaparkovano);
        } catch (InterruptedException | ExecutionException ex) {
            LOGER.error("Nebylo možno ověřit zaparkování dokladu", ex);
        }
        progres.zavriDialog();
    }

    private void zkontrolujBinarniKopiiDokladu() {
        LOGER.info("Zkontroluje se přítomnost bezpečnostní binární kopie dokladu dokladu.");
        if (DokladProcesor.getInstance().zkontrolujANactiBinarniKopii()) {
            LOGER.info("Doklad existuje. Jeho ID je {} a jeho položky se načtou.", DokladProcesor.getInstance().getDoklad().getId());
            kontroler.setPolozky(DokladProcesor.getInstance().getDoklad().getPolozky());
        }
    }
}
