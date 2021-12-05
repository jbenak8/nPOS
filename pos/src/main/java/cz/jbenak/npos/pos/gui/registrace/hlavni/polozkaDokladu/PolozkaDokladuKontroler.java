/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni.polozkaDokladu;

import cz.jbenak.npos.pos.gui.Pomocnici;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-07
 */
public class PolozkaDokladuKontroler implements Initializable {

    @FXML
    private Label registr;
    @FXML
    private Label nazev;
    @FXML
    private Label mnozstvi;
    @FXML
    private Label celkemCena;
    @FXML
    private Label jednotkovaCena;
    @FXML
    private Label akce;
    @FXML
    private Label akceCena;
    @FXML
    private Label sleva;
    @FXML
    private Label slevaHodnota;
    @FXML
    private Label zakaznicaSleva;
    @FXML
    private Label zakaznickaSlevaHodnota;
    @FXML
    private Label slevaVelikost;
    @FXML
    private GridPane akceKontejner;
    @FXML
    private GridPane slevaKontejner;
    @FXML
    private GridPane zakaznickaSlevaKontejner;
    @FXML
    private VBox panel;
    private boolean prazdnaPolozka;

    public boolean isPrazdnaPolozka() {
        return prazdnaPolozka;
    }

    public void setPrazdnaPolozka(boolean prazdnaPolozka) {
        this.prazdnaPolozka = prazdnaPolozka;
    }

    public void setRegistr(String regCislo) {
        if (regCislo == null || regCislo.isEmpty()) {
            registr.setText("");
        } else {
            registr.setText(regCislo);
        }
    }

    public void setNazev(String nazevZbozi) {
        if (nazevZbozi == null || nazevZbozi.isEmpty()) {
            nazev.setText("");
        } else {
            nazev.setText(nazevZbozi);
        }
    }

    public void setMnozstvi(BigDecimal zadaneMnozstvi) {
        mnozstvi.setText(zadaneMnozstvi.toPlainString() + " ×");
    }

    public void setMnozstviAJednotka(BigDecimal mnozstvi, String mj) {
        this.mnozstvi.setText(mnozstvi.toPlainString() + " " + mj + " ×");
    }

    public void setCelkem(BigDecimal cenaCelkem, boolean vraceno) {
        if (cenaCelkem == null) {
            celkemCena.setText("");
        } else {
            String cena = Pomocnici.formatujNaDveMista(cenaCelkem);
            celkemCena.setText(vraceno ? "-" + cena : cena);
        }
    }

    public void setJednotkovaCena(BigDecimal jCena) {
        if (jCena == null) {
            jednotkovaCena.setText("");
        } else {
            jednotkovaCena.setText(Pomocnici.formatujNaDveMista(jCena));
        }
    }

    public void setAkce(String nazevAkce) {
        akce.setText(nazevAkce);
    }

    public void setAkcniSleva(BigDecimal hodnota) {
        akceCena.setText(Pomocnici.formatujNaDveMista(hodnota));
    }

    public void setHodnotuSlevy(BigDecimal hodnota) {
        slevaHodnota.setText(Pomocnici.formatujNaDveMista(hodnota));
    }

    public void setHodnotuZakaznickeSlevy(BigDecimal hodnota) {
        zakaznickaSlevaHodnota.setText(Pomocnici.formatujNaDveMista(hodnota));
    }

    /**
     * Nastaví text pro pole s velikostí slevy.
     *
     * @param velikost Velikost slevy, např. 20 % nebo 15,- Kč
     */
    public void setVelikostSlevy(String velikost) {
        slevaVelikost.setText(velikost);
    }

    public void zobrazAkci(boolean zobraz) {
        akceKontejner.setMaxHeight(zobraz ? 25 : 0);
        this.akceKontejner.setVisible(zobraz);
    }

    public void zobrazSlevu(boolean zobraz) {
        this.slevaKontejner.setVisible(zobraz);
        slevaKontejner.setMaxHeight(zobraz ? 25 : 0);
    }

    public void zobrazZakaznickouSlevu(boolean zobraz) {
        zakaznickaSlevaKontejner.setVisible(zobraz);
        zakaznickaSlevaKontejner.setMaxHeight(zobraz ? 25 : 0);
    }

    public void zobrazJakoVratku() {
        nazev.getStyleClass().add("vratka");
        registr.getStyleClass().add("vratka");
        mnozstvi.getStyleClass().add("vratka");
        jednotkovaCena.getStyleClass().add("vratka");
        celkemCena.getStyleClass().add("vratka");
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        registr.setText("");
        nazev.setText("");
        mnozstvi.setText("");
        celkemCena.setText("0,00");
        jednotkovaCena.setText("");
        this.akceKontejner.setVisible(false);
        this.slevaKontejner.setVisible(false);
        this.zakaznickaSlevaKontejner.setVisible(false);
        akceKontejner.setMaxHeight(0);
        slevaKontejner.setMaxHeight(0);
        zakaznickaSlevaKontejner.setMaxHeight(0);
    }

}
