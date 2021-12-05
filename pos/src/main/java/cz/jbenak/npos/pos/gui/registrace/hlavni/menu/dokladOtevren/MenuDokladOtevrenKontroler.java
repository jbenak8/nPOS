/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni.menu.dokladOtevren;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOknoKontroler;
import cz.jbenak.npos.pos.gui.registrace.hlavni.vyhledaniSortimentu.VyhledaniSortimentu;
import cz.jbenak.npos.pos.gui.registrace.skupinySlev.SeznamSkupinSlev;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.otazkaAnoNe.OtazkaAnoNe;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.gui.sdilene.zadavaciDialog.ZadavaciDialog;
import cz.jbenak.npos.pos.gui.utility.kalkulacka.Kalkulacka;
import cz.jbenak.npos.pos.gui.zakaznik.registracniFunkce.FunkceRegistrovanehoZakaznika;
import cz.jbenak.npos.pos.gui.zakaznik.vyhledani.VyhledaniZakazniku;
import cz.jbenak.npos.pos.gui.zamek.Zamek;
import cz.jbenak.npos.pos.objekty.slevy.Sleva;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;
import cz.jbenak.npos.pos.procesory.DokladProcesor;
import cz.jbenak.npos.pos.procesory.ManazerSlev;
import cz.jbenak.npos.pos.procesory.PlatebniProcesor;
import cz.jbenak.npos.pos.procesory.RegistracniProcesor;
import cz.jbenak.npos.pos.procesy.doklad.parkovani.ObsluhaParkovani;
import cz.jbenak.npos.pos.system.Pos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static cz.jbenak.npos.pos.procesory.AutorizacniManazer.Funkce.*;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-24
 */
public class MenuDokladOtevrenKontroler implements Initializable {

    private final Logger LOGER = LogManager.getLogger(MenuDokladOtevrenKontroler.class);
    private HlavniOknoKontroler hlKontroler;
    @FXML
    private GridPane panelMenuDoklad;

    public void setHlKontroler(HlavniOknoKontroler hlKontroler) {
        this.hlKontroler = hlKontroler;
    }

    public void zobrazPanel(boolean zobrazit) {
        panelMenuDoklad.setVisible(zobrazit);
    }

    @FXML
    private void tlZahodDokladStisknuto() {
        LOGER.debug("Tlačítko pro zrušení právě rozpracovaného dokladu bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(ZAHOZENI_DOKLADU)) {
            OtazkaAnoNe zahodit = new OtazkaAnoNe("Zahodit doklad?", "Opravdu si přejete zahodit právě rozpracovaný doklad?", Pos.getInstance().getAplikacniOkno());
            zahodit.zobrazDialog();
            if (zahodit.getVolba() == OtazkaAnoNe.Volby.ANO) {
                LOGER.info("Bude provedeno zahození právě rozpracovaného dokladu.");
                hlKontroler.smazPolozky();
                DokladProcesor.getInstance().zrusDoklad();
                if (RegistracniProcesor.getInstance().isRezimVratky()) {
                    RegistracniProcesor.getInstance().setRezimVratky(false);
                    hlKontroler.setRezimVratky(false);
                }
            }
        }
    }

    @FXML
    public void tlKalkulackaStisknuto() {
        LOGER.debug("Tlačítko pro zobrazení obchodnického kalkulátoru bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(KALKULACKA)) {
            Kalkulacka kalk = new Kalkulacka(Pos.getInstance().getAplikacniOkno());
            kalk.zobrazDialog();
        }
    }

    @FXML
    public void tlZamknoutStisknuto() {
        LOGER.debug("Tlačítto pro uzamčení pokladny bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(ZAMEK)) {
            new Zamek().zobrazDialog();
        }
    }

    @FXML
    public void tlOpakovatPolozkuStisknuto() {
        LOGER.debug("Tlačítko pro opakování položky na dokladu bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(OPAKOVANI_POLOZKY)) {
            if (hlKontroler.getVybranaPolozka() != null) {
                BigDecimal celkoveMnozstvi = BigDecimal.ZERO;
                DokladProcesor.getInstance().getDoklad().getPolozky().stream().filter((s) -> (s.getRegistr().equalsIgnoreCase(hlKontroler.getVybranaPolozka().getRegistr()))).forEachOrdered((s) -> celkoveMnozstvi.add(s.getMnozstvi()));
                if (!hlKontroler.getVybranaPolozka().isVracena() && hlKontroler.getVybranaPolozka().getMaxProdejneMnozstvi() != null
                        && hlKontroler.getVybranaPolozka().getMaxProdejneMnozstvi().compareTo(celkoveMnozstvi) > 0) {
                    new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nemohu opakovat položku",
                            "Nemohu opakovat položku s registračním číslem {} jelikož bylo překročeno její maximální prodejné množství {} na celý doklad.",
                            new Object[]{hlKontroler.getVybranaPolozka().getRegistr(), Pomocnici.formatujNaDveMista(hlKontroler.getVybranaPolozka().getMaxProdejneMnozstvi())},
                            Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                } else {
                    DokladProcesor.getInstance().getDoklad().getPolozky().add((Sortiment) SerializationUtils.clone(hlKontroler.getVybranaPolozka()));
                    ManazerSlev.zkontrolujSlevyNaPolozku();
                    DokladProcesor.getInstance().aktualizujBinarniKopii();
                    hlKontroler.setPolozky(DokladProcesor.getInstance().getDoklad().getPolozky());
                    LOGER.info("Položka sortimentu s registračním číslem {} byla opakována.", hlKontroler.getVybranaPolozka().getRegistr());
                }
            }
        }
    }

    @FXML
    public void tlStornoPolozkyStisknuto() {
        LOGER.debug("Tlačítko pro storno položky na dokladu bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(STORNO_POLOZKY)) {
            if (hlKontroler.getVybranaPolozka() != null) {
                String smazanaPolozka = hlKontroler.getVybranaPolozka().getRegistr();
                DokladProcesor.getInstance().getDoklad().getPolozky().remove(hlKontroler.getVybranaPolozka());
                ManazerSlev.zkontrolujSlevyNaPolozku();
                DokladProcesor.getInstance().aktualizujBinarniKopii();
                hlKontroler.setPolozky(DokladProcesor.getInstance().getDoklad().getPolozky());
                LOGER.info("Položka sortimentu s registračním číslem {} byla smazána.", smazanaPolozka);
                if (DokladProcesor.getInstance().getDoklad().getPolozky().isEmpty()) {
                    LOGER.info("Všechny položky byly z dokladu byly vymazány. Doklad bude zrušen.");
                    if (DokladProcesor.getInstance().getDoklad().getZakaznik() != null) {
                        HlavniOkno.getInstance().getKontroler().smazZakaznika();
                    }
                    DokladProcesor.getInstance().zrusDoklad();
                    new DialogZpravy(DialogZpravy.TypZpravy.INFO, "Doklad zrušen", "Vymazali jste všechny položky z dokladu, proto jsem doklad zrušil.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                }
            }
        }
    }

    @FXML
    public void tlRezimVratkyStisknuto() {
        LOGER.debug("Tlačítko pro aktivaci/deaktivaci režimu vratky na dokladu bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(REZIM_VRATKY)) {
            RegistracniProcesor.getInstance().setRezimVratky(!RegistracniProcesor.getInstance().isRezimVratky());
            hlKontroler.setRezimVratky(RegistracniProcesor.getInstance().isRezimVratky());
        }
    }

    @FXML
    public void tlParkovaniStisknuto() {
        LOGER.debug("Tlačítko pro parkování bylo stisknuto.");
        ObsluhaParkovani.odparkujZaparkujDoklad();
    }

    @FXML
    public void tlMnozstviStisknuto() {
        LOGER.debug("Tlačítko pro vložení nebo úpravu množství bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(MNOZSTVI)) {
            String vstup = HlavniOkno.getInstance().getKontroler().getPoleVstup().getText();
            HlavniOkno.getInstance().getKontroler().getPoleVstup().clear();
            if (!vstup.isEmpty() && !vstup.matches(Pomocnici.REGEX_PETIMISTNE_DESETINNE_CISLO)) {
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Množství - neplatný vstup", "Nebyla zadána platná hodnota pro zadání množství. Prosím zadejte celé nebo desetinné číslo (max. 5 míst).", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                HlavniOkno.getInstance().getKontroler().getPoleVstup().requestFocus();
            } else if (vstup.isEmpty()) {
                new ZadavaciDialog(ZadavaciDialog.Typ.MNOZSTVI, hlKontroler.getVybranaPolozka()).zobrazDialog();
            } else {
                RegistracniProcesor.getInstance().otevriPolozkuMnozstvim(new BigDecimal(vstup.replaceAll(",", ".")));
            }
        }
    }

    @FXML
    public void tlSlevaVybratStisknuto() {
        LOGER.debug("Tlačítko pro výběr nebo úpravu slevy ze skupiny slev bylo stisknuto.");
        //TODO přepis na vložení nebo úpravu slevy
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(SLEVA)) {
            new SeznamSkupinSlev(Pos.getInstance().getAplikacniOkno(), false).zobrazDialog();
        } else {
            LOGER.warn("Zadání slevy pro položku s registrem {} není povoleno", hlKontroler.getVybranaPolozka().getRegistr());
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Sleva není povolena", "Pro tuto položku není povoleno zadat slevu.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        }
    }

    @FXML
    public void tlSlevaOdebratStisknuto() {
        LOGER.debug("Tlačítko pro zadání nebo úpravu hodnotové slevy bylo stisknuto.");
        // TODO přepis na smazání slevy
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(SLEVA_SMAZANI)) {
            if (hlKontroler.getVybranaPolozka().isSlevaPovolena()) {
                Sleva rucni = ManazerSlev.getRucniSleva(hlKontroler.getVybranaPolozka());
                if (rucni == null) {
                    new ZadavaciDialog(ZadavaciDialog.Typ.SLEVA_HODNOTA, hlKontroler.getVybranaPolozka()).zobrazDialog();
                } else {
                    ManazerSlev.odeberRucniSlevu(hlKontroler.getVybranaPolozka(), rucni);
                }
            } else {
                LOGER.warn("Zadání slevy pro položku s registrem {} není povoleno", hlKontroler.getVybranaPolozka().getRegistr());
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Sleva není povolena", "Pro tuto položku není povoleno zadat slevu.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
            }
        }
    }

    @FXML
    public void tlZmenaCenyStisknuta() {
        LOGER.debug("Tlačítko pro změnu ceny bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(ZMENA_CENY)) {
            if (hlKontroler.getVybranaPolozka().isZmenaCenyPovolena()) {
                new ZadavaciDialog(ZadavaciDialog.Typ.CENA, hlKontroler.getVybranaPolozka()).zobrazDialog();
            } else {
                LOGER.warn("Změna ceny pro položku s registrem {} není povolena.", hlKontroler.getVybranaPolozka().getRegistr());
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Změna ceny není povolena", "Pro tuto položku není povoleno změnit její cenu.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
            }
        }
    }

    @FXML
    public void tlInfoOPolozceStisknuto() {
        LOGER.debug("Tlačítko pro informace o položce sortimentu bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(INFO_O_SORTIMENTU)) {
            new VyhledaniSortimentu(true, Pos.getInstance().getAplikacniOkno()).zobrazDialog();
        }
    }

    @FXML
    public void tlHledatSortimnentStisknuto() {
        LOGER.debug("Tlačítko pro vyhledávání položky sortimentu bylo stisknuto.");
        new VyhledaniSortimentu(false, Pos.getInstance().getAplikacniOkno()).zobrazDialog();
    }

    @FXML
    public void tlRegistrovatZakaznikaStisknuto() {
        LOGER.debug("Tlačítko pro registraci zákazníka bylo stisknuto.");
        if (DokladProcesor.getInstance().jeOtevrenDoklad() && DokladProcesor.getInstance().getDoklad().getZakaznik() != null) {
            new FunkceRegistrovanehoZakaznika(DokladProcesor.getInstance().getDoklad().getZakaznik()).zobrazDialog();
        } else {
            if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(REGISTRACE_ZAKAZNIKA)) {
                new VyhledaniZakazniku(Pos.getInstance().getAplikacniOkno()).zobrazDialog();
            }
        }
    }

    @FXML
    public void tlPlatbaStisknuto() {
        LOGER.debug("Tlačítko pro spuštění platebního režimu bylo stisknuto.");
        PlatebniProcesor.getInstance().zahajPlatbuDokladu();
    }

    @FXML
    public void tlServerParkingStisknuto() {
        LOGER.debug("Tlačítko pro odložení dokladu na server bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(ULOZENI_NA_SERVER)) {

        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb  rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        panelMenuDoklad.setVisible(false);
    }

}
