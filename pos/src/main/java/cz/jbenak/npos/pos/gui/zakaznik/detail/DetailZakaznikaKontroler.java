package cz.jbenak.npos.pos.gui.zakaznik.detail;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.objekty.adresy.Adresa;
import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
import cz.jbenak.npos.pos.objekty.slevy.SkupinaSlev;
import cz.jbenak.npos.pos.system.Pos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Kontrolér dialogu pro zobrazení detailu vybraného zákazníka
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-09-10
 */
public class DetailZakaznikaKontroler implements Initializable {

    private final static Logger LOGER = LogManager.getLogger(DetailZakaznikaKontroler.class);
    private final Image ANO = new Image(getClass().getResourceAsStream("../../sdilene/img/tlOk.png"));
    private final Image NE = new Image(getClass().getResourceAsStream("../../sdilene/img/storno.png"));
    private DetailZakaznika dialog;
    private Zakaznik zakaznik;

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
    private Label popisUrl;
    @FXML
    private Label popisEmail;
    @FXML
    private Label popisTelefon;
    @FXML
    private Label popisMobil;
    @FXML
    private Label popisFax;
    @FXML
    private Label popisSkupinaSlevNazev;
    @FXML
    private Label popisSkupinaSlevTyp;
    @FXML
    private Label popisSkupinaSlevHodnota;
    @FXML
    private Label popisSkupinaSlevOkamzikUplatneni;
    @FXML
    private Label popisSkupinaSlevCilUplatneniTyp;
    @FXML
    private Label popisSkupinaSlevCilUplatneniObjekt;
    @FXML
    private Label popisDuvodBlokace;
    @FXML
    private Label nadpisDuvodBlokace;
    @FXML
    private ImageView ikonaManSlevaPovolena;
    @FXML
    private ImageView ikonaOdebiraDL;
    @FXML
    private ImageView ikonaBlokovan;

    /**
     * Nastaví objekt dialogového okna.
     *
     * @param dialog dialog okna detailu zákazníka
     */
    void setDialog(DetailZakaznika dialog) {
        this.dialog = dialog;
    }

    /**
     * Předá objekt vybraného zákazníka.
     *
     * @param zakaznik vybraný zákazník k zobrazení.
     */
    void setZakaznik(Zakaznik zakaznik) {
        this.zakaznik = zakaznik;
        setHodnoty();
    }

    /**
     * Nastaví atributy vybraného zákazníka do zobrazovacích políček.
     */
    private void setHodnoty() {
        popisCislo.setText(zakaznik.getCislo());
        popisJmeno.setText(zakaznik.getJmeno() == null ? "" : zakaznik.getJmeno());
        popisPrijmeni.setText(zakaznik.getPrijmeni() == null ? "" : zakaznik.getPrijmeni());
        popisFirma.setText(zakaznik.getNazev() == null ? "" : zakaznik.getNazev());
        popisIc.setText(zakaznik.getIc() == null ? "" : zakaznik.getIc());
        popisDic.setText(zakaznik.getDic() == null ? "" : zakaznik.getDic());
        popisDuvodBlokace.setText(zakaznik.getDuvodBlokace() == null ? "" : zakaznik.getDuvodBlokace());
        nadpisDuvodBlokace.setVisible(zakaznik.getDuvodBlokace() != null);
        ikonaManSlevaPovolena.setImage(zakaznik.isManualniSlevaPovolena() ? ANO : NE);
        ikonaOdebiraDL.setImage(zakaznik.isOdebiraNaDL() ? ANO : NE);
        ikonaBlokovan.setImage(zakaznik.isBlokovan() ? ANO : NE);
        setHlavniAdresu();
        setSkupinuSlev();
    }

    private void setHlavniAdresu() {
        Adresa a = null;
        for (Adresa adr : zakaznik.getAdresy()) {
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
            popisUrl.setText(a.getWeb() != null ? a.getWeb() : "");
            popisEmail.setText(a.getEmail() != null ? a.getEmail() : "");
            popisTelefon.setText(a.getTelefon() != null ? a.getTelefon() : "");
            popisMobil.setText(a.getMobil() != null ? a.getMobil() : "");
            popisFax.setText(a.getFax() != null ? a.getFax() : "");
        } else {
            popisUlice.setText("");
            popisCpCor.setText("");
            popisPsc.setText("");
            popisMesto.setText("");
            popisStat.setText("");
            popisUrl.setText("");
            popisEmail.setText("");
            popisTelefon.setText("");
            popisMobil.setText("");
            popisFax.setText("");
        }
    }

    private void setSkupinuSlev() {
        SkupinaSlev s = zakaznik.getSkupinaSlev();
        if (s != null) {
            popisSkupinaSlevNazev.setText(s.getNazev());
            popisSkupinaSlevCilUplatneniObjekt.setText(s.getIdCileSlevy());
            String cilTyp, okamzik, typ, hodnota;
            switch (s.getTyp()) {
                case NA_DOKLAD:
                    cilTyp = "Celý doklad";
                    break;
                case NA_REGISTR:
                    cilTyp = "Sortiment";
                    break;
                case NA_SKUPINU:
                    cilTyp = "Skupina sortimentu";
                    break;
                default:
                    cilTyp = "";
                    break;
            }
            popisSkupinaSlevCilUplatneniTyp.setText(cilTyp);
            switch (s.getOkamzikUplatneni()) {
                case PLATBA:
                    okamzik = "Při platbě";
                    break;
                case REGISTRACE:
                    okamzik = "Při registraci";
                    break;
                default:
                    okamzik = "";
                    break;
            }
            popisSkupinaSlevOkamzikUplatneni.setText(okamzik);
            switch (s.getTypSlevy()) {
                case HODNOTOVA:
                    typ = "Sleva absolutní";
                    hodnota = Pomocnici.formatujNaDveMista(s.getHodnotaSlevy()) + " " + Pos.getInstance().getHlavniMena().getNarodniSymbol();
                    break;
                case PROCENTNI:
                    typ = "Sleva procentní";
                    hodnota = Pomocnici.formatujNaDveMista(s.getHodnotaSlevy()) + " %";
                    break;
                default:
                    typ = "";
                    hodnota = "0";
                    break;
            }
            popisSkupinaSlevTyp.setText(typ);
            popisSkupinaSlevHodnota.setText(hodnota);
        } else {
            popisSkupinaSlevNazev.setText("Skupina slev není pro tohoto zákazníka nastavena.");
            popisSkupinaSlevNazev.setStyle("-fx-font-style: italic");
            popisSkupinaSlevCilUplatneniObjekt.setText("");
            popisSkupinaSlevCilUplatneniTyp.setText("");
            popisSkupinaSlevHodnota.setText("");
            popisSkupinaSlevOkamzikUplatneni.setText("");
            popisSkupinaSlevTyp.setText("");
        }
    }

    @FXML
    private void klavesaStisknuta(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ESCAPE) {
            tlZavritStisknuto();
        }
    }

    @FXML
    private void tlZavritStisknuto() {
        LOGER.info("Dialog zobrazující detaily vybraného zákazníka s číslem {} bude uzavřen.", zakaznik.getCislo());
        dialog.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
