/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesory;

import cz.jbenak.npos.pos.gui.platba.dialogKVraceni.DialogKVraceni;
import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.doklad.Doklad;
import cz.jbenak.npos.pos.objekty.platba.PolozkaPlatby;
import cz.jbenak.npos.pos.procesy.doklad.UlozeniDoDatabaze;
import cz.jbenak.npos.pos.system.Pos;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import static cz.jbenak.npos.pos.objekty.doklad.TypDokladu.DODACI_LIST;
import static cz.jbenak.npos.pos.objekty.doklad.TypDokladu.PARAGON;

/**
 * Třída, vykonávající elementární práci s dokladem a držící jeho instanci.
 * Určena pro použití v procesech
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-10
 */
public class DokladProcesor {

    public static final String FS_ULOZISTE = "doklady";
    private static final Logger LOGER = LogManager.getLogger(DokladProcesor.class);
    private static volatile DokladProcesor instance;
    private final SimpleDateFormat sdf;
    private Doklad doklad;
    private String nazevSouboruKopie;

    private DokladProcesor() {
        sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-sss");
    }

    /**
     * Vrátí instanci doklad procesoru, nebo vytvoří novou a vrátí jí.
     *
     * @return jedinečná instance doklad procesoru.
     */
    public static DokladProcesor getInstance() {
        DokladProcesor procesor = DokladProcesor.instance;
        if (procesor == null) {
            synchronized (DokladProcesor.class) {
                procesor = DokladProcesor.instance;
                if (procesor == null) {
                    DokladProcesor.instance = procesor = new DokladProcesor();
                }
            }
        }
        return procesor;
    }

    /**
     * Vrátí existující oteřený doklad. Pokud doklad neexistuje, založí jej.
     *
     * @return rozpracovaný doklad.
     */
    public Doklad getDoklad() {
        if (doklad == null) {
            zalozParagon();
        }
        return doklad;
    }

    /**
     * Zjistí, zdali je již otevřen nějaký doklad.
     *
     * @return Ano, pokud doklad není null, Ne pokud je doklad null.
     */
    public boolean jeOtevrenDoklad() {
        return doklad != null;
    }

    /**
     * Založí nový paragon, pokud již neexistuje otevřený.
     */
    public void zalozParagon() {
        if (doklad == null) {
            LOGER.info("Bude založen nový paragon.");
            doklad = new Doklad(PARAGON);
            ulozBinarniKopii();
        }
    }

    /**
     * Nastaví již existující doklad např. ze serverového načtení, odparkování,
     * nebo vratky
     *
     * @param nactenyDoklad existující načtený doklad.
     * @throws Exception pokud již existuje otevřený doklad.
     */
    public void setDoklad(Doklad nactenyDoklad) throws Exception {
        LOGER.debug("Bude nastaven doklad s id {}.", nactenyDoklad.getId());
        if (doklad == null) {
            doklad = nactenyDoklad;
            ulozBinarniKopii();
        } else {
            LOGER.warn("Nemohu nastavit nový doklad s id {} - již existuje otevřený s id {}.", nactenyDoklad.getId(), doklad.getId());
            throw new Exception("Již existuje otevřený doklad!");
        }
    }

    /**
     * Aktualizuje binární bezpečnostní kopii na disku.
     */
    public void aktualizujBinarniKopii() {
        LOGER.debug("Bude aktualizována bezpečnostní binární kopie dokladu s id {}.", doklad.getId());
        try {
            SerializationUtils.serialize(doklad, new FileOutputStream(nazevSouboruKopie));
        } catch (Exception e) {
            LOGER.error("Nepodařilo se aktualizovat binární kopii souboru.", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se aktualizovat bezpečnostní kopii dokladu na disk: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        }
    }

    /**
     * Zkontroluje, jestli na disku existuje binární bezpečnostní kopie a pak jí
     * načte jako aktivní doklad.
     *
     * @return aktivní doklad, který byl načten z disku.
     */
    public boolean zkontrolujANactiBinarniKopii() {
        boolean nacteno = false;
        File[] soubory = new File(FS_ULOZISTE).listFiles();
        if (soubory == null || soubory.length == 0) {
            LOGER.debug("Na disku neexistuje žádá bezpečnostní binární kopie dokladu.");
        } else if (soubory.length > 1) {
            LOGER.warn("Existuje více než jedna bezpečnostní binární kopie! V obnově se nebude pokračovat!");
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Chyba systému", "Existuje více než jedna bezpečnostní kopie dokladu. Prosím proveďte údržbu a kontrolu!", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        } else {
            try {
                doklad = SerializationUtils.deserialize(new FileInputStream(soubory[0]));
                nazevSouboruKopie = soubory[0].getPath();
                nacteno = true;
                LOGER.info("Byla načtena binární kopie dokladu s id {}.", doklad.getId());
            } catch (IOException e) {
                LOGER.error("Nepodařilo se načíst binární kopii dokladu.", e);
                new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se načíst bezpečnostní kopii dokladu na disk: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
            }
        }
        return nacteno;
    }

    private boolean otevritZasuvku() {
        for (PolozkaPlatby pp : doklad.getPlatby()) {
            if (pp.getPlatebniProstredek().isOtevritZasuvku()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Provede všechny potřebné úkoly pro uzavření dokladu a zapíše jej do
     * databáze. Tato metoda je volána po uhrazení dokladu jako konečná pro
     * práci s dokladem.
     */
    public void uzavriDoklad() throws Exception {
        LOGER.info("Doklad s id {} bude uzavřen a zapsán do databáze.", doklad.getId());
        UlozeniDoDatabaze ulozeni = new UlozeniDoDatabaze();
        ulozeni.run();
        if (ulozeni.get()) {
            //replikace na BO - pokud spojení NOK - pak příznak v DB s doklady
            //Pos.getInstance().getManazerReplikace().odesliDoklad();
            if (doklad.getTyp() != DODACI_LIST && otevritZasuvku()) {
                LOGER.info("Pokladní zásuvka bude otevřena.");
            }
            if (doklad.getTyp() != DODACI_LIST && otevritZasuvku() && doklad.getCastkaKVraceni().compareTo(BigDecimal.ZERO) > 0) {
                DialogKVraceni dialog = new DialogKVraceni(true);
                dialog.zobrazDialog();
            }
            //tisk přes !!manažer tisku!!  a zobrazení vrácení přes okno -> vrátí se do platebního procesoru
            //TEST TISKU !!!
            PlatebniProcesor.getInstance().getProgres().zavriDialog();
            TiskovyProcesor tp = new TiskovyProcesor();
            tp.vytiskniDoklad();
            //KONEC TESTU !!!
            HlavniOkno.getInstance().getKontroler().smazPolozky();
            HlavniOkno.getInstance().getKontroler().setRezimVratky(false);
            zrusDoklad();
        } else {
            throw new Exception("Proces ukládání dokladu selhal na dílčí chybě - viz log.");
        }
    }

    /**
     * Metoda provede odebrání bezpečnostní bitové kopie a zrušení reference
     * dokladu.
     */
    public void zrusDoklad() {
        LOGER.info("Bezpečnostní binární kopie dokladu s id {} bude smazána a doklad zde v procesoru bude taktéž smazán.", doklad.getId());
        if (!new File(nazevSouboruKopie).delete()) {
            LOGER.error("Nepodařilo se smazat bezpečnostní binární kopii dokladu. Prosím proveďte údržbu.");
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se smazat bezpečnostní binární kopii dokladu. Prosím proveďte údržbu a kontrolu!", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        }
        doklad = null;
    }

    /**
     * Provede vlastní serializaci dokladu na disk.
     */
    private void ulozBinarniKopii() {
        LOGER.debug("Bude uložena bezpečnostní binární kopie dokladu s id {}.", doklad.getId());
        File slozka = new File(FS_ULOZISTE);
        if (!slozka.exists()) {
            boolean vytvoreno = slozka.mkdir();
            LOGER.debug("Složka dokladů {} byla vytvořena: {}", slozka.getPath(), vytvoreno);
        }
        if (slozka.exists()) {
            if (!slozka.canWrite()) {
                LOGER.debug("Do složky {} nelze zapisovat.", slozka.getPath());
                boolean writable = slozka.setWritable(true);
                LOGER.debug("Právo zápisu na složku dokladů {}: {}", slozka.getPath(), writable);
            }
            nazevSouboruKopie = FS_ULOZISTE + "/" + sdf.format(new Date(System.currentTimeMillis())) + ".doklad";
            try {
                SerializationUtils.serialize(doklad, new FileOutputStream(nazevSouboruKopie));
            } catch (IOException e) {
                LOGER.error("Nepodařilo se uložit binární kopii souboru.", e);
                new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se uložit bezpečnostní kopii dokladu na disk: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
            }
        } else {
            LOGER.error("Nepodařilo se vytvořit složku pro binární kopii souboru.");
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se vytvořit složku pro ukládání bezpečnostních kopií dokladu. Doporučujeme kontrolu protokolů aplikace.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        }
    }
}
