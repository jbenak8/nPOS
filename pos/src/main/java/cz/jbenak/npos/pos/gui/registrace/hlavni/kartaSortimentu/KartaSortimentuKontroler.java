/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni.kartaSortimentu;

import cz.jbenak.npos.komunikace.StavSkladu;
import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.objekty.adresy.Adresa;
import cz.jbenak.npos.pos.objekty.partneri.Dodavatel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-03-03
 */
public class KartaSortimentuKontroler implements Initializable {

    private final Image ANO = new Image(getClass().getResourceAsStream("../../../sdilene/img/tlOk.png"));
    private final Image NE = new Image(getClass().getResourceAsStream("../../../sdilene/img/storno.png"));
    private KartaSortimentu dialog;
    @FXML
    private Label nadpis;
    @FXML
    private Label registr;
    @FXML
    private Label nazev;
    @FXML
    private Label skupinaID;
    @FXML
    private Label skupinaText;
    @FXML
    private Label plu;
    @FXML
    private Label mj;
    @FXML
    private Label dph;
    @FXML
    private ImageView prodejPovolen;
    @FXML
    private ImageView sluzba;
    @FXML
    private ImageView vratkaPovolena;
    @FXML
    private ImageView platebniPoukaz;
    @FXML
    private ImageView slevaPovolena;
    @FXML
    private ImageView nedelitelne;
    @FXML
    private ImageView zmenaCenyPovolena;
    @FXML
    private ImageView nutnoZadatCenu;
    @FXML
    private ImageView nutnoZadatPopis;
    @FXML
    private ImageView nutnoZadatMnozstvi;
    @FXML
    private Label popis;
    @FXML
    private Label prodejniCena;
    @FXML
    private Label maxSleva;
    @FXML
    private Label minProdejneMnozstvi;
    @FXML
    private Label maxProdejneMnozstvi;
    @FXML
    private Label hlavniCK;
    @FXML
    private Label dodavatel;
    @FXML
    private ImageView neskladova;
    @FXML
    private ProgressIndicator indikaceStavSkladu;
    @FXML
    private ProgressIndicator indikaceDatumPrijmu;
    @FXML
    private ProgressIndicator indikacePosledniNC;
    @FXML
    private Label stavSkladu;
    @FXML
    private Label datumPoslPrijmu;
    @FXML
    private Label posledniNC;
    @FXML
    private ImageView obrazek;
    @FXML
    private ImageView obrTlOk;

    protected void setDialog(KartaSortimentu dialog) {
        this.dialog = dialog;
    }

    protected void zobrazData() {
        nadpis.setText("Karta položky sortimentu".toUpperCase());
        obrTlOk.setImage(dialog.isZaregistrovat() ? new Image(getClass().getResourceAsStream("img/zbozi_registrace.png")) : new Image(getClass().getResourceAsStream("img/tlOk2.png")));
        registr.setText(dialog.getPolozka().getRegistr());
        nazev.setText(dialog.getPolozka().getNazev());
        skupinaID.setText(dialog.getPolozka().getSkupina().getId());
        skupinaText.setText(dialog.getPolozka().getSkupina().getNazev());
        plu.setText(dialog.getPolozka().getPLU() == null ? "" : dialog.getPolozka().getPLU());
        mj.setText(dialog.getPolozka().getMj().getJednotka() + " - " + dialog.getPolozka().getMj().getNazev());
        dph.setText(dialog.getPolozka().getDph().getOznaceni() + " - " + Pomocnici.formatujNaDveMista(dialog.getPolozka().getDph().getSazba()) + " %");
        prodejPovolen.setImage(dialog.getPolozka().isProdejne() ? ANO : NE);
        sluzba.setImage(dialog.getPolozka().isSluzba() ? ANO : NE);
        vratkaPovolena.setImage(dialog.getPolozka().isVratkaPovolena() ? ANO : NE);
        platebniPoukaz.setImage(dialog.getPolozka().isPlatebniPoukaz() ? ANO : NE);
        prodejPovolen.setImage(dialog.getPolozka().isProdejne() ? ANO : NE);
        slevaPovolena.setImage(dialog.getPolozka().isSlevaPovolena() ? ANO : NE);
        nedelitelne.setImage(dialog.getPolozka().isNedelitelne() ? ANO : NE);
        zmenaCenyPovolena.setImage(dialog.getPolozka().isZmenaCenyPovolena() ? ANO : NE);
        nutnoZadatCenu.setImage(dialog.getPolozka().isNutnoZadatCenu() ? ANO : NE);
        neskladova.setImage(dialog.getPolozka().isNeskladova() ? ANO : NE);
        nutnoZadatPopis.setImage(dialog.getPolozka().isNutnoZadatPopis() ? ANO : NE);
        nutnoZadatMnozstvi.setImage(dialog.getPolozka().isNutnoZadatMnozstvi() ? ANO : NE);
        popis.setText(dialog.getPolozka().getPopis() == null ? "" : dialog.getPolozka().getPopis());
        prodejniCena.setText(Pomocnici.formatujNaDveMista(dialog.getPolozka().getJednotkovaCena()));
        maxSleva.setText((dialog.getPolozka().getSkupina().getMaxSleva() == null || dialog.getPolozka().getSkupina().getMaxSleva().compareTo(BigDecimal.ZERO) == 0)
                ? (dialog.getPolozka().isSlevaPovolena() ? "neomezeno" : "sleva zakázána")
                : Pomocnici.formatujNaDveMista(dialog.getPolozka().getSkupina().getMaxSleva()));

        minProdejneMnozstvi.setText(Pomocnici.formatujNaDveMista(dialog.getPolozka().getMinProdavaneMnozstvi()));
        maxProdejneMnozstvi.setText(dialog.getPolozka().getMaxProdejneMnozstvi() == null ? "neomezeno" : Pomocnici.formatujNaDveMista(dialog.getPolozka().getMaxProdejneMnozstvi()));
        hlavniCK.setText(dialog.getPolozka().getHlavniCarovyKod() == null ? "" : dialog.getPolozka().getHlavniCarovyKod().getKod());
        setDodavatele(dialog.getPolozka().getDodavatel());
        if (dialog.getPolozka().getObrazek() != null) {
            obrazek.setImage(new Image(new ByteArrayInputStream(dialog.getPolozka().getObrazek())));
        }
        if (dialog.getPolozka().isNeskladova()) {
            indikaceDatumPrijmu.setVisible(false);
            indikacePosledniNC.setVisible(false);
            indikaceStavSkladu.setVisible(false);
            stavSkladu.setText("Neeviduje se");
            datumPoslPrijmu.setText("Neeviduje se");
            posledniNC.setText("Neeviduje se");
        } else {
            dialog.zjistiSkladovaData();
        }
    }

    public void zobrazStavSkladu(StavSkladu stav) {
        indikaceDatumPrijmu.setVisible(false);
        indikacePosledniNC.setVisible(false);
        indikaceStavSkladu.setVisible(false);
        if (stav == null) {
            stavSkladu.setText("Nepodařilo se zjistit");
            datumPoslPrijmu.setText("Nepodařilo se zjistit");
            posledniNC.setText("Nepodařilo se zjistit");
        } else {
            stavSkladu.setText(Pomocnici.formatujNaDveMista(stav.getStavSkladu()) + dialog.getPolozka().getMj().getJednotka());
            if (stav.getStavSkladu().compareTo(BigDecimal.ZERO) <= 0) {
                stavSkladu.getStyleClass().add("zaporny-sklad");
            }
            datumPoslPrijmu.setText(Pomocnici.formatujDatum(stav.getPosledniPrijem()));
            posledniNC.setText(Pomocnici.formatujNaDveMista(stav.getPosledniNakupniCena()));
        }
    }

    @FXML
    private void tlZavritStisknuto() {
        dialog.setZavrenoPotvrzenim(false);
        dialog.zavriDialog();
    }

    @FXML
    private void tlOkStisknuto() {
        dialog.setZavrenoPotvrzenim(true);
        if (dialog.isZaregistrovat()) {
            dialog.zaregistrujPolozku();
        } else {
            dialog.zavriDialog();
        }
    }

    @FXML
    private void klavesaStisknuta(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ENTER) {
            tlOkStisknuto();
        }
        if (evt.getCode() == KeyCode.ESCAPE) {
            tlZavritStisknuto();
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        indikaceDatumPrijmu.setVisible(true);
        indikacePosledniNC.setVisible(true);
        indikaceStavSkladu.setVisible(true);
        stavSkladu.setText("Zjišťuje se...");
        datumPoslPrijmu.setText("Zjišťuje se...");
        posledniNC.setText("Zjišťuje se...");
    }

    private void setDodavatele(Dodavatel d) {
        if (d == null) {
            dodavatel.setText("");
        } else {
            if (d.getAdresy().stream().filter(Adresa::isHlavni).findFirst().isPresent()) {
                Adresa adresa = d.getAdresy().stream().filter(Adresa::isHlavni).findFirst().get();
                String text = d.getNazev();
                text += ", IČ: " + d.getIc();
                text += adresa.getUlice() == null ? "" : (", " + adresa.getUlice());
                text += adresa.getCp() == null ? "" : (" " + adresa.getCp());
                text += adresa.getCor() == null ? "" : ("/" + adresa.getCor());
                text += adresa.getPsc() == null ? "" : (", " + adresa.getPsc());
                text += (", " + adresa.getMesto());
                text += (", " + adresa.getStat().getUplnyNazev());
                dodavatel.setText(text);
            }
        }
    }
}
