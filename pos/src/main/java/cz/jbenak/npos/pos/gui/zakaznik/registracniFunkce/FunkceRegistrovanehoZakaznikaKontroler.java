package cz.jbenak.npos.pos.gui.zakaznik.registracniFunkce;

import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.objekty.adresy.Adresa;
import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

import static cz.jbenak.npos.pos.procesory.AutorizacniManazer.Funkce.INFO_O_ZAKAZNIKOVI;
import static cz.jbenak.npos.pos.procesory.AutorizacniManazer.Funkce.ZRUSENI_REGISTRACE_ZAKAZNIKA;

/**
 * Kontrolér dialogu pro funkce registrovaného zákazníka
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-09-12
 */
public class FunkceRegistrovanehoZakaznikaKontroler implements Initializable {

    private static final Logger LOGER = LogManager.getLogger(FunkceRegistrovanehoZakaznikaKontroler.class);
    private FunkceRegistrovanehoZakaznika dialog;
    private Zakaznik vybranyZakaznik;
    @FXML
    private Label popisCislo;
    @FXML
    private Label popisJmeno;
    @FXML
    private Label popisPrijmeni;
    @FXML
    private Label popisFirma;
    @FXML
    private Label popisIc;
    @FXML
    private Label popisDic;
    @FXML
    private Label popisUlice;
    @FXML
    private Label popisCpCor;
    @FXML
    private Label popisPsc;
    @FXML
    private Label popisMesto;
    @FXML
    private Label popisStat;
    @FXML
    private Button tlInfo;
    @FXML
    private Button tlOdregistrovat;

    void setDialog(FunkceRegistrovanehoZakaznika dialog) {
        this.dialog = dialog;
    }

    void setZakaznik(Zakaznik zakaznik) {
        this.vybranyZakaznik = zakaznik;
    }

    void zobrazZakaznika() {
        popisCislo.setText(vybranyZakaznik.getCislo());
        popisJmeno.setText(vybranyZakaznik.getJmeno() == null ? "" : vybranyZakaznik.getJmeno());
        popisPrijmeni.setText(vybranyZakaznik.getPrijmeni() == null ? "" : vybranyZakaznik.getPrijmeni());
        popisFirma.setText(vybranyZakaznik.getNazev() == null ? "" : vybranyZakaznik.getNazev());
        popisIc.setText(vybranyZakaznik.getIc() == null ? "" : vybranyZakaznik.getIc());
        popisDic.setText(vybranyZakaznik.getDic() == null ? "" : vybranyZakaznik.getDic());
        Adresa a = null;
        for (Adresa adr : vybranyZakaznik.getAdresy()) {
            if (adr.isHlavni()) {
                a = adr;
                break;
            }
        }
        if (a != null) {
            popisUlice.setText(a.getUlice() != null ? a.getUlice() : "");
            String cpCor = a.getCp() != null ? a.getCp() : "";
            if (a.getCor() != null) {
                cpCor += (" / " + a.getCor());
            }
            popisCpCor.setText(cpCor);
            popisPsc.setText(a.getPsc() != null ? a.getPsc() : "");
            popisMesto.setText(a.getMesto() != null ? a.getMesto() : "");
            popisStat.setText(a.getStat().getUplnyNazev() != null ? a.getStat().getUplnyNazev() : "");
        } else {
            popisUlice.setText("");
            popisCpCor.setText("");
            popisPsc.setText("");
            popisMesto.setText("");
            popisStat.setText("");
        }
    }

    @FXML
    private void klavesaStisknuta(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ESCAPE) {
            zavriDialog();
        }
        if (evt.getCode() == KeyCode.DELETE || evt.getCode() == KeyCode.F8) {
            tlOdregistrovatStisknuto();
        }
        if (evt.getCode() == KeyCode.I || evt.getCode() == KeyCode.F3) {
            tlInfoStisknuto();
        }
    }

    @FXML
    private void zavriDialog() {
        LOGER.info("Dialog pro funkce vybraného registrovaného zákazníka s číslem {} bude uzvařen.", vybranyZakaznik.getCislo());
        dialog.close();
    }

    @FXML
    private void tlInfoStisknuto() {
        LOGER.debug("Tlačítko pro detailní informace o registrovaném zákazníkovi s číslem {} bylo stisknuto.", vybranyZakaznik.getCislo());
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(INFO_O_ZAKAZNIKOVI)) {
            dialog.zobrazInfoOZakaznikovi();
        }
    }

    @FXML
    private void tlOdregistrovatStisknuto() {
        LOGER.debug("Tlačítko pro zrušení registrace zákazníka s číslem {} bylo stisknuto.", vybranyZakaznik.getCislo());
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(ZRUSENI_REGISTRACE_ZAKAZNIKA)) {
            dialog.odregistrujZakaznika();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tlInfo.setTooltip(new Tooltip("Detail zákazníka"));
        tlOdregistrovat.setTooltip(new Tooltip("Odregistrovat tohoto zákazníka"));
    }
}
