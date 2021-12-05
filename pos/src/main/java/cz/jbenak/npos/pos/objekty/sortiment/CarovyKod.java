/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.sortiment;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Třída reprezentující čárový kód na kartě sortimentu.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-11
 */
public class CarovyKod implements Serializable {

    public enum TypCarovehoKodu {
        EAN13, EAN8, PDF417, DVA_Z_PETI, TRI_Z_DEVITI;
    }

    private static final long serialVersionUID = -5801578643485674788L;
    private String kod;
    private String registr;
    private boolean hlavni;
    private BigDecimal mnozstvi;
    private BigDecimal cena;
    private TypCarovehoKodu typ;
    private int cenaPozice;
    private int cenaDelka;
    private int cenaDesetinne;
    private int mnozstviDelka;
    private int mnozstviPozice;
    private int mnozstviDesetinne;

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public String getRegistr() {
        return registr;
    }

    public void setRegistr(String registr) {
        this.registr = registr;
    }

    public boolean isHlavni() {
        return hlavni;
    }

    public void setHlavni(boolean hlavni) {
        this.hlavni = hlavni;
    }

    public BigDecimal getMnozstvi() {
        if (this.mnozstvi != null || this.mnozstviPozice > 0) {
            return mnozstvi;
        } else {
            return parsujHodnotu(mnozstviDelka, mnozstviPozice, mnozstviDesetinne);
        }
    }

    public void setMnozstvi(BigDecimal mnozstvi) {
        this.mnozstvi = mnozstvi;
    }

    public BigDecimal getCena() {
        if (this.cena != null || this.cenaPozice > 0) {
            return cena;
        } else {
            return parsujHodnotu(cenaDelka, cenaPozice, cenaDesetinne);
        }
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public TypCarovehoKodu getTyp() {
        return typ;
    }

    public void setTyp(TypCarovehoKodu typ) {
        this.typ = typ;
    }

    public int getCenaPozice() {
        return cenaPozice;
    }

    public void setCenaPozice(int cenaPozice) {
        this.cenaPozice = cenaPozice;
    }

    public int getCenaDelka() {
        return cenaDelka;
    }

    public void setCenaDelka(int cenaDelka) {
        this.cenaDelka = cenaDelka;
    }

    public int getCenaDesetinne() {
        return cenaDesetinne;
    }

    public void setCenaDesetinne(int cenaDesetinne) {
        this.cenaDesetinne = cenaDesetinne;
    }

    public int getMnozstviDelka() {
        return mnozstviDelka;
    }

    public void setMnozstviDelka(int mnozstviDelka) {
        this.mnozstviDelka = mnozstviDelka;
    }

    public int getMnozstviPozice() {
        return mnozstviPozice;
    }

    public void setMnozstviPozice(int mnozstviPozice) {
        this.mnozstviPozice = mnozstviPozice;
    }

    public int getMnozstviDesetinne() {
        return mnozstviDesetinne;
    }

    public void setMnozstviDesetinne(int mnozstviDesetinne) {
        this.mnozstviDesetinne = mnozstviDesetinne;
    }

    private BigDecimal parsujHodnotu(int delka, int pozice, int desetinne) {
        BigDecimal hodnota;
        String hodnotovaCast = kod.substring(pozice, pozice + delka);
        if (desetinne > 0) {
            String celaCast = hodnotovaCast.substring(0, hodnotovaCast.length() - desetinne);
            String desetinnaCast = hodnotovaCast.substring(hodnotovaCast.length() - desetinne);
            hodnota = new BigDecimal(celaCast + "." + desetinnaCast);
        } else {
            hodnota = new BigDecimal(hodnotovaCast);
        }
        return hodnota;
    }

}
