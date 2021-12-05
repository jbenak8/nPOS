/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesory;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.registrace.hlavni.seznamSortimentu.SeznamSortimentu;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.progres.ProgresDialog;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.gui.sdilene.zadaniPopisu.ZadaniPopisu;
import cz.jbenak.npos.pos.gui.sdilene.zadavaciDialog.ZadavaciDialog;
import cz.jbenak.npos.pos.objekty.doklad.Doklad;
import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
import cz.jbenak.npos.pos.objekty.sortiment.CarovyKod;
import cz.jbenak.npos.pos.objekty.sortiment.SeznamRegistrace;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;
import cz.jbenak.npos.pos.procesy.polozka.vyhledavani.KontrolaRegistru;
import cz.jbenak.npos.pos.procesy.polozka.vyhledavani.VyhledaniDleCarovehoKodu;
import cz.jbenak.npos.pos.procesy.polozka.vyhledavani.VyhledavaniDleNazvu;
import cz.jbenak.npos.pos.procesy.polozka.vyhledavani.VyhledavaniDlePlu;
import cz.jbenak.npos.pos.procesy.registrace.NacteniPolozky;
import cz.jbenak.npos.pos.system.Pos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Třída zastřešující registraci položek
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-10
 */
public class RegistracniProcesor {

    private static final Logger LOGER = LogManager.getLogger(RegistracniProcesor.class);
    private final ProgresDialog progres;
    private static volatile RegistracniProcesor instance;
    private Sortiment polozka;
    private boolean rezimVratky;

    private RegistracniProcesor() {
        progres = new ProgresDialog(Pos.getInstance().getAplikacniOkno());
    }

    /**
     * Metoda poskytne jedinečnou strukturu možnosti registrace
     *
     * @return instanci registračního procesoru
     */
    public static RegistracniProcesor getInstance() {
        RegistracniProcesor procesor = RegistracniProcesor.instance;
        if (procesor == null) {
            synchronized (RegistracniProcesor.class) {
                procesor = RegistracniProcesor.instance;
                if (procesor == null) {
                    RegistracniProcesor.instance = procesor = new RegistracniProcesor();
                }
            }
        }
        return procesor;
    }

    /**
     * Zapne nebo vypne režim vratky.
     *
     * @param rezimVratky je třeba zaregistrovat sortiment jako vratku, nebo ne.
     */
    public void setRezimVratky(boolean rezimVratky) {
        this.rezimVratky = rezimVratky;
        LOGER.info("Režim vratky byl " + (!this.rezimVratky ? "de" : "") + "aktivován.");
    }

    /**
     * Zjistí, zdali je zapnutý režim vratky.
     *
     * @return vratka aktivní.
     */
    public boolean isRezimVratky() {

        return rezimVratky;
    }

    /**
     * Proveder registraci sortimentu dle zadaného vstupu. Sortiment se zde
     * vyhledá.
     *
     * @param vstup EAN, registrační číslo, PLU, nebo název.
     */
    public void zaregistrujPolozkuVolne(String vstup) {
        progres.zobrazProgres("Vyhledávám registrační číslo sortimentu na základě čárového kódu: " + vstup);
        String registr = null;
        polozka = null;
        try {
            VyhledaniDleCarovehoKodu hledaniCK = new VyhledaniDleCarovehoKodu(vstup);
            hledaniCK.run();
            polozka = hledaniCK.get();
            // PLU
            if (polozka == null) {
                LOGER.info("Vyhledávání na základě čárového kódu nebylo úspěšné. Vyhledávám vstup {} dle PLU.", vstup);
                progres.setText("Vyhledávám registrační číslo sortimentu na základě kódu PLU: " + vstup);
                VyhledavaniDlePlu hledaniPLU = new VyhledavaniDlePlu(vstup);
                hledaniPLU.run();
                registr = hledaniPLU.get();
            }
            //registr - jedinečné
            if (polozka == null && registr == null) {
                LOGER.info("Vyhledávání na základě kódu PLU nebylo úspěšné. Vyhledávám vstup {} na základě reistračního čísla.", vstup);
                progres.setText("Vyhledávám registrační číslo sortimentu přímo na základě registračního čísla: " + vstup);
                KontrolaRegistru kontrola = new KontrolaRegistru(vstup);
                kontrola.run();
                if (kontrola.get()) {
                    registr = vstup;
                }
            }
            //název
            if (polozka == null && registr == null) {
                LOGER.info("Vyhledávání na základě registračního čísla nebylo úspěšné. Vyhledávám vstup {} na základě názvu.", vstup);
                progres.setText("Vyhledávám registrační číslo sortimentu na základě návzu: " + vstup);
                VyhledavaniDleNazvu hledaniNazvu = new VyhledavaniDleNazvu(vstup);
                hledaniNazvu.run();
                List<SeznamRegistrace> vysledky = hledaniNazvu.get();
                if (vysledky == null) {
                    LOGER.info("Ani dle názvu '{}' nebylo možné zadaný sortiment vyhledat.", vstup);
                } else {
                    LOGER.info("Dle názvu '{}' bylo nalezeno {} výsledků. Bude zobrazen seznam.", vstup, vysledky.size());
                    SeznamSortimentu seznam = new SeznamSortimentu(vysledky, Pos.getInstance().getAplikacniOkno(), false);
                    seznam.zobrazSeznam();
                    polozka = seznam.getPolozka();
                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            LOGER.error("Nebylo možno vyhledat registrační číslo sortimentu v databázi.", ex);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nebylo možno vyhledat registrační číslo sortimentu v databázi: \n\n" + ex.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        }
        if (polozka == null && registr == null) {
            progres.zavriDialog();
            LOGER.warn("Nebylo možno vyhledat registrační číslo sortimentu dle zadaného vstupu: {}. Nebo pokud byl zobrazen seznam a uživatel jej zavřel bez výběru.", vstup);
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Sortiment nenalezen nebo nevybrán", "Nebylo možno vyhledat registrační číslo sortimentu v databázi dle zadaného vstupu: " + vstup + " Nebo nebyl vybrán žádný nalezený sortiment z případného seznamu.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
            Doklad doklad = DokladProcesor.getInstance().getDoklad();
            if (doklad.getPolozky().size() > 0) {
                LOGER.info("Odebere se prázdná položka.");
                for (Sortiment s : doklad.getPolozky()) {
                    if (s.jePrazdny()) {
                        doklad.getPolozky().remove(s);
                        doklad.setPolozkaOtevrena(false);
                        break;
                    }
                }
                HlavniOkno.getInstance().getKontroler().setPolozky(doklad.getPolozky());
                DokladProcesor.getInstance().aktualizujBinarniKopii();
            }
        } else if (polozka != null) {
            LOGER.info("Byl nalezen sortiment s registračním číslem {} dle čárového kódu. Bude se pokračovat v registraci.", registr);
            zaregistrujPolozku(polozka);
        } else {
            LOGER.info("Byl nalezen nebo vybrán sortiment s registračním číslem {}. Bude se pokračovat v registraci.", registr);
            polozka = new Sortiment();
            polozka.setRegistr(registr);
            zaregistrujPolozku(polozka);
        }
    }

    public void zaregistrujPolozkuKodem(CarovyKod kod) {
        progres.zobrazProgres("Vyhledávám registrační číslo sortimentu na základě čárového kódu: " + kod.getKod());
        try {
            VyhledaniDleCarovehoKodu hledaniCK = new VyhledaniDleCarovehoKodu(kod.getKod());
            hledaniCK.run();
            polozka = hledaniCK.get();
        } catch (InterruptedException | ExecutionException ex) {
            LOGER.error("Nebylo možno vyhledat registrační číslo sortimentu v databázi.", ex);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nebylo možno vyhledat registrační číslo sortimentu v databázi: \n\n" + ex.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        }
        if (polozka == null) {
            progres.zavriDialog();
            LOGER.warn("Nebylo možno vyhledat registrační číslo sortimentu dle zadaného vstupu: {}.", kod.getKod());
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Sortiment nenalezen", "Nebylo možno vyhledat registrační číslo sortimentu v databázi dle zadaného vstupu: " + kod.getKod(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        } else {
            LOGER.info("Byl nalezen nebo vybrán sortiment s registračním číslem {}. Bude se pokračovat v registraci.", polozka.getRegistr());
            zaregistrujPolozku(polozka);
        }
    }

    public void otevriPolozkuMnozstvim(BigDecimal mnozstvi) {
        if (mnozstvi.equals(BigDecimal.ZERO)) {
            LOGER.warn("Zadané množství je nulové.");
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Množství je 0", "Nulové množství opravdu nelze zadat.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        } else {
            polozka = new Sortiment();
            polozka.setMnozstvi(mnozstvi);
            Doklad doklad = DokladProcesor.getInstance().getDoklad();
            Sortiment prazdny = null;
            if (doklad.getPolozky().size() > 0) {
                for (Sortiment s : doklad.getPolozky()) {
                    if (s.jePrazdny()) {
                        prazdny = s;
                        break;
                    }
                }
            }
            if (prazdny == null) {
                LOGER.info("Bylo zadáno množství {}. Prázdná položka bude otevřena", mnozstvi.toPlainString());
                polozka.setJednotkovaCena(BigDecimal.ZERO);
                doklad.getPolozky().add(polozka);
            } else {
                LOGER.info("Množství {} prázdné položky bude upraveno.", mnozstvi.toPlainString());
                for (Sortiment s : doklad.getPolozky()) {
                    if (s.jePrazdny()) {
                        if (s.equals(prazdny)) {
                            s.setMnozstvi(mnozstvi);
                        }
                        break;
                    }
                }
            }
            doklad.setPolozkaOtevrena(true);
            HlavniOkno.getInstance().getKontroler().setPolozky(doklad.getPolozky());
            DokladProcesor.getInstance().aktualizujBinarniKopii();
        }
    }

    /**
     * Zaregistruje vybraného zákazníka do aktuálního dokladu. Pokud doklad není ještě otevřen, založí se nový.
     *
     * @param zakaznik vybraný zákazník. Pokud je zákazník blokován, registrace se odmítne.
     */
    public void zaregistrujZakaznika(Zakaznik zakaznik) {
        LOGER.info("Provede se registrace zákazníka s číslem {} do dokladu.", zakaznik.getCislo());
        if (DokladProcesor.getInstance().jeOtevrenDoklad() && DokladProcesor.getInstance().getDoklad().getZakaznik() != null) {
            LOGER.info("V aktuálně otevřeném dokladu je již zákazník zaregistrován.");
            new DialogZpravy(DialogZpravy.TypZpravy.INFO, "Zákazníka už mám",
                    "Už mám zaregistrovaného zákazníka. Pokud chcete zaregistrovat nového, odeberte prosím současného.",
                    Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        } else if (zakaznik.isBlokovan()) {
            LOGER.info("Zákazník s číslem {} je blokován a nebude zaregistrován.", zakaznik.getCislo());
            String nazev = (zakaznik.getJmeno() == null ? "" : zakaznik.getJmeno()) + (zakaznik.getPrijmeni() == null ? "" : " " + zakaznik.getPrijmeni());
            nazev += zakaznik.getNazev() == null ? "" : zakaznik.getNazev();
            DialogZpravy dialog = new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Blokovaného zákazníka neberu",
                    "Nemohu zaregistrovat blokovaného zákazníka. Buď odblokujte tohoto zákazníka {} s číslem {} nebo vyberte jiného ",
                    new Object[]{nazev, zakaznik.getCislo()}, Pos.getInstance().getAplikacniOkno());
            dialog.zobrazDialogAPockej();
        } else {
            LOGER.info("Zákazník s číslem {} bude zaregistrován.", zakaznik.getCislo());
            DokladProcesor.getInstance().getDoklad().setZakaznik(zakaznik);
            ManazerSlev.zkontrolujSlevyNaPolozku();
            DokladProcesor.getInstance().aktualizujBinarniKopii();
            HlavniOkno.getInstance().getKontroler().zobrazZakaznika(zakaznik);
        }
    }

    /**
     * Odebere aktuálně registrovaného zákazníka z aktuálně otevřeného dokladu.
     */
    public void odregistrujZakaznika() {
        LOGER.info("Registrovaný zákazník s číslem {} bude odebrán.", DokladProcesor.getInstance().getDoklad().getZakaznik().getCislo());
        DokladProcesor.getInstance().getDoklad().setZakaznik(null);
        ManazerSlev.odeberZakaznickeSlevy();
        HlavniOkno.getInstance().getKontroler().smazZakaznika();
        new DialogZpravy(DialogZpravy.TypZpravy.INFO, "Zákazník odebrán", "Odebral jsem zaregistrovaného zákazníka. Můžete zaregistrovat nového, nebo pokračovat bez něj.",
                Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
    }

    /**
     * Provede vlastní načtení a registraci položky. Položka už v objektu
     * Sortiment se předává v případě uložení čárového kódu, který obsahuje data
     * o množství, nebo ceně. Položka musí obsahovat alespoň registrační číslo.
     *
     * @param polozkaKRegistraci Položka sortimentu alespoň s registračním číslem.
     */
    public void zaregistrujPolozku(Sortiment polozkaKRegistraci) {
        LOGER.info("Zaregistruje se sortiment s registračním číslem {}.", polozkaKRegistraci.getRegistr());
        if (progres.isShowing()) {
            progres.setText("Registruji sortiment s registračním číslem " + polozkaKRegistraci.getRegistr());
        } else {
            progres.zobrazProgres("Registruji sortiment s registračním číslem " + polozkaKRegistraci.getRegistr());
        }
        NacteniPolozky np = new NacteniPolozky(polozkaKRegistraci);
        np.run();
        try {
            polozka = np.get();
            if (!polozka.isProdejne() && !rezimVratky) {
                LOGER.warn("Položka s registračním číslem {} není určena pro prodej.", polozka.getRegistr());
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Položku nelze prodat", "Položka s registračním číslem " + polozka.getRegistr() + " není určena pro prodej.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
            } else if (rezimVratky && !polozka.isVratkaPovolena()) {
                LOGER.warn("Položka s registračním číslem {} není určena pro vrácení.", polozka.getRegistr());
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Položku nelze vrátit", "Položku s registračním číslem " + polozka.getRegistr() + " nelze vrátit. Režim vratky bude ukončen.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                rezimVratky = false;
                HlavniOkno.getInstance().getKontroler().setRezimVratky(rezimVratky);
            } else {
                if (rezimVratky) {
                    LOGER.info("Sortiment s registračním číslem {} bude zaregistrován jako vratka.", polozka.getRegistr());
                    polozka.setVracena(true);
                }
                boolean doplneniZadano = false;
                boolean doplneniNutne = false;
                if (polozka.isNutnoZadatCenu() || polozka.isNutnoZadatPopis()) {
                    doplneniNutne = true;
                    doplneniZadano = doplnData();
                }
                if (doplneniNutne && !doplneniZadano) {
                    LOGER.warn("Data položky (cena/množství/popis) s registračním číslem {} nebyla zadána.", polozka.getRegistr());
                } else {
                    if (polozka.getMnozstvi() == null || polozka.getMnozstvi().compareTo(BigDecimal.ZERO) == 0) {
                        LOGER.info("U položky s registračním číslem {} bude nastaveno výchozí množství 1.", polozka.getRegistr());
                        polozka.setMnozstvi(BigDecimal.ONE);
                    }
                    if (polozka.getMnozstvi().compareTo(polozka.getMinProdavaneMnozstvi()) < 0) {
                        LOGER.warn("U položky s registračním číslem {} bylo zadáno menší množství ({}), než je minimální prodejné ({}).", polozka.getRegistr(), polozka.getMnozstvi(), polozka.getMinProdavaneMnozstvi());
                        new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Příliš nízké množství",
                                "Pro tuto položku s registračním číslem " + polozka.getRegistr() + " jste zadali příliš nízké množství. Minimum je " + Pomocnici.formatujNaDveMista(polozka.getMinProdavaneMnozstvi()), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                    } else if (polozka.getMaxProdejneMnozstvi() != null && polozka.getMnozstvi().compareTo(polozka.getMaxProdejneMnozstvi()) > 0) {
                        LOGER.warn("U položky s registračním číslem {} bylo zadáno větší množství ({}), než je maximální prodejné ({}).", polozka.getRegistr(), polozka.getMnozstvi(), polozka.getMaxProdejneMnozstvi());
                        new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Příliš vysoké množství",
                                "Pro tuto položku s registračním číslem " + polozka.getRegistr() + " jste zadali příliš vysoké množství. Maximum je " + Pomocnici.formatujNaDveMista(polozka.getMaxProdejneMnozstvi()), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                    } else if (polozka.isNedelitelne() && polozka.getMnozstvi().scale() != 0) {
                        LOGER.warn("Položka s registračním číslem {} je nedělitelná.", polozka.getRegistr());
                        new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Položku je nedělitelná", "Položku s registračním číslem " + polozka.getRegistr() + " nelze rozdělit.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                    } else {
                        List<Sortiment> polozky = DokladProcesor.getInstance().getDoklad().getPolozky();
                        if (!polozky.isEmpty() && polozky.get(polozky.size() - 1).jePrazdny()) {
                            Sortiment pp = polozky.get(polozky.size() - 1);
                            if (pp.getJednotkovaCena() != null && pp.getJednotkovaCena().compareTo(BigDecimal.ZERO) > 0) {
                                polozka.setJednotkovaCena(pp.getJednotkovaCena());
                            }
                            if (pp.getMnozstvi() != null && pp.getMnozstvi().compareTo(BigDecimal.ZERO) > 0) {
                                polozka.setMnozstvi(pp.getMnozstvi());
                            }
                            polozky.set((polozky.size() - 1), polozka);
                        } else {
                            polozky.add(polozka);
                        }
                        ManazerSlev.zkontrolujSlevyNaPolozku();
                        HlavniOkno.getInstance().getKontroler().setPolozky(polozky);
                        DokladProcesor.getInstance().aktualizujBinarniKopii();
                        LOGER.info("Položka sortimentu s registračním číslem {} byla zaregistrována.", polozka.getRegistr());
                    }
                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            LOGER.error("Nebylo možno zaregistrovat sortiment s registrem {}.", polozka.getRegistr(), ex);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nebylo možno zaregistrovat položku sortimentu s reg. číslem " + polozka.getRegistr() + ": \n\n" + ex.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        }
        progres.zavriDialog();
    }

    private boolean doplnData() {
        boolean doplneno = false;
        if (polozka.isNutnoZadatCenu()) {
            LOGER.info("Pro položku sortimentu s registračním číslem {} je vyžadováno zadání ceny.", polozka.getRegistr());
            ZadavaciDialog cena = new ZadavaciDialog(ZadavaciDialog.Typ.CENA, polozka);
            cena.setPredRegistraci();
            cena.zobrazDialog();
            doplneno = !cena.isZadaniZamitnuto();
        }
        if (polozka.isNutnoZadatMnozstvi()) {
            LOGER.info("Pro položku sortimentu s registračním číslem {} je vyžadováno zadání množství.", polozka.getRegistr());
            ZadavaciDialog mno = new ZadavaciDialog(ZadavaciDialog.Typ.MNOZSTVI, polozka);
            mno.setPredRegistraci();
            mno.zobrazDialog();
            doplneno = !mno.isZadaniZamitnuto();
        }
        if (polozka.isNutnoZadatPopis()) {
            LOGER.info("Pro položku sortimentu s registračním číslem {} je vyžadováno zadání popisu.", polozka.getRegistr());
            ZadaniPopisu zadaniPopisu = new ZadaniPopisu(Pos.getInstance().getAplikacniOkno());
            zadaniPopisu.zobrazDialog();
            if (zadaniPopisu.isZadaniOdmitnuto()) {
                doplneno = false;
            } else {
                polozka.setPopis(zadaniPopisu.getPopis());
                doplneno = true;
            }
        }
        return doplneno;
    }
}
