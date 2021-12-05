/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni.menu.registracePripravena;

import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOknoKontroler;
import cz.jbenak.npos.pos.gui.registrace.hlavni.vyhledaniSortimentu.VyhledaniSortimentu;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.otazkaAnoNe.OtazkaAnoNe;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.otazkaAnoNe.OtazkaAnoNe.Volby;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.gui.utility.kalkulacka.Kalkulacka;

import static cz.jbenak.npos.pos.procesory.AutorizacniManazer.Funkce.*;

import cz.jbenak.npos.pos.gui.zakaznik.vyhledani.VyhledaniZakazniku;
import cz.jbenak.npos.pos.gui.zamek.Zamek;
import cz.jbenak.npos.pos.procesory.RegistracniProcesor;
import cz.jbenak.npos.pos.procesy.doklad.parkovani.ObsluhaParkovani;
import cz.jbenak.npos.pos.system.Pos;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-04
 */
public class MenuRegistracePripravenaKontroler implements Initializable {

    private final Logger LOGER = LogManager.getLogger(MenuRegistracePripravenaKontroler.class);
    private HlavniOknoKontroler hlKontroler;
    @FXML
    private GridPane panelMenuRegPripravena;

    public void setHlKontroler(HlavniOknoKontroler hlKontroler) {
        this.hlKontroler = hlKontroler;
    }

    @FXML
    public void tlOdhlasitStisknuto() {
        LOGER.debug("Tlačítkto pro odhlášení bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(ODHLASENI)) {
            OtazkaAnoNe odhlasit = new OtazkaAnoNe("Odhlásit se?", "Opravdu si přejete se odhlásit?", Pos.getInstance().getAplikacniOkno());
            odhlasit.zobrazDialog();
            if (odhlasit.getVolba() == Volby.ANO) {
                LOGER.info("Bude provedeno odhlášení.");
                HlavniOkno.getInstance().uzavriHlavniOkno(true);
            }
        }
    }

    @FXML
    public void tlKalkulackaStisknuto() {
        LOGER.debug("Tlačítkto pro zobrazení obchodnického kalkulátoru bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(KALKULACKA)) {
            Kalkulacka kalk = new Kalkulacka(Pos.getInstance().getAplikacniOkno());
            kalk.zobrazDialog();
        }
    }

    @FXML
    public void tlZamknoutStisknuto() {
        LOGER.debug("Tlačítkto pro uzamčení pokladny bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(ZAMEK)) {
            new Zamek().zobrazDialog();
        }
    }

    @FXML
    public void tlSkoliciRezim() {
        LOGER.debug("Tlačítkto pro aktivaci nebo deaktivaci školícího řežimu bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(SKOLICI_REZIM)) {
            HlavniOkno.getInstance().getPrihlasenaPokladni().setSkoliciRezim(!HlavniOkno.getInstance().getPrihlasenaPokladni().isSkoliciRezim());
            HlavniOkno.getInstance().getKontroler().setSkoliciRezim(HlavniOkno.getInstance().getPrihlasenaPokladni().isSkoliciRezim());
        }
    }

    @FXML
    public void tlMnozstviStisknuto() {
        LOGER.debug("Tlačítkto pro vložení množství bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(MNOZSTVI)) {
            String vstup = HlavniOkno.getInstance().getKontroler().getPoleVstup().getText();
            HlavniOkno.getInstance().getKontroler().getPoleVstup().clear();
            if (vstup.isEmpty() || !vstup.matches("(\\d+)|(\\d+([.|,])?)|(\\d+([.|,]\\d{1,5})?)")) {
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Množství - neplatný vstup", "Nebyla zadána platná hodnota pro zadání množství. Prosím zadejte celé nebo desetinné číslo (max. 5 míst).", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                HlavniOkno.getInstance().getKontroler().getPoleVstup().requestFocus();
            }  else {
                RegistracniProcesor.getInstance().otevriPolozkuMnozstvim(new BigDecimal(vstup.replaceAll(",", ".")));
            }
        }
    }

    @FXML
    public void tlRezimVratkyStisknuto() {
        LOGER.debug("Tlačítkto pro aktivaci/deaktivaci režimu vratky na dokladu bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(REZIM_VRATKY)) {
            RegistracniProcesor.getInstance().setRezimVratky(!RegistracniProcesor.getInstance().isRezimVratky());
            hlKontroler.setRezimVratky(RegistracniProcesor.getInstance().isRezimVratky());
        }
    }

    @FXML
    private void tlParkovaniStisknuto() {
        LOGER.debug("Tlačítko pro parkování bylo stisknuto.");
        ObsluhaParkovani.odparkujZaparkujDoklad();
    }

    @FXML
    public void tlInfoOPolozceStisknuto() {
        LOGER.debug("Tlačítkto pro informace o položce sortimentu bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(INFO_O_SORTIMENTU)) {
            new VyhledaniSortimentu(true, Pos.getInstance().getAplikacniOkno()).zobrazDialog();
        }
    }

    @FXML
    public void tlHledatSortimnentStisknuto() {
        LOGER.debug("Tlačítkto pro vyhledávání položky sortimentu bylo stisknuto.");
        new VyhledaniSortimentu(false, Pos.getInstance().getAplikacniOkno()).zobrazDialog();
    }

    @FXML
    public void tlRegistrovatZakaznikaStisknuto() {
        LOGER.debug("Tlačítkto pro registraci zákazníka bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(REGISTRACE_ZAKAZNIKA)) {
            new VyhledaniZakazniku(Pos.getInstance().getAplikacniOkno()).zobrazDialog();
        }
    }

    public void zobrazMenu(boolean zobrazit) {
        panelMenuRegPripravena.setVisible(zobrazit);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
