/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.partneri;

import cz.jbenak.npos.pos.objekty.adresy.Adresa;
import cz.jbenak.npos.pos.objekty.slevy.SkupinaSlev;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-10
 */
public class Zakaznik implements Serializable {

    private static final long serialVersionUID = -5173735643828806893L;
    private String cislo;
    private String nazev;
    private String jmeno;
    private String prijmeni;
    private SkupinaSlev skupinaSlev;
    private List<Adresa> adresy;
    private String ic;
    private String dic;
    private boolean blokovan;
    private String duvodBlokace;
    private boolean odebiraNaDL;
    private boolean manualniSlevaPovolena;
    private boolean detailNacten = false;
    private boolean pouzeProDanovyDoklad = false;

    public String getCislo() {
        return cislo;
    }

    public void setCislo(String cislo) {
        this.cislo = cislo;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public SkupinaSlev getSkupinaSlev() {
        return skupinaSlev;
    }

    public void setSkupinaSlev(SkupinaSlev skupinaSlev) {
        this.skupinaSlev = skupinaSlev;
    }

    public List<Adresa> getAdresy() {
        return adresy;
    }

    public void setAdresy(List<Adresa> adresy) {
        this.adresy = adresy;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public boolean isBlokovan() {
        return blokovan;
    }

    public void setBlokovan(boolean blokovan) {
        this.blokovan = blokovan;
    }

    public String getDuvodBlokace() {
        return duvodBlokace;
    }

    public void setDuvodBlokace(String duvodBlokace) {
        this.duvodBlokace = duvodBlokace;
    }

    public boolean isOdebiraNaDL() {
        return odebiraNaDL;
    }

    public void setOdebiraNaDL(boolean odebiraNaDL) {
        this.odebiraNaDL = odebiraNaDL;
    }

    public boolean isManualniSlevaPovolena() {
        return manualniSlevaPovolena;
    }

    public void setManualniSlevaPovolena(boolean manualniSlevaPovolena) {
        this.manualniSlevaPovolena = manualniSlevaPovolena;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public boolean isDetailNacten() {
        return detailNacten;
    }

    public void setDetailNacten(boolean detailNacten) {
        this.detailNacten = detailNacten;
    }

    public boolean isPouzeProDanovyDoklad() {
        return pouzeProDanovyDoklad;
    }

    public void setPouzeProDanovyDoklad(boolean pouzeProDanovyDoklad) {
        this.pouzeProDanovyDoklad = pouzeProDanovyDoklad;
    }
}
