package cz.jbenak.npos.pos.objekty.partneri;

/**
 * Třída pro reprezentaci kritérií pro vyhledávání zákazníka.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-09-14
 */
public class KriteriaVyhledavaniZakaznika {

    private String cislo;
    private String jmeno;
    private String prijmeni;
    private String nazev;
    private String ic;
    private String dic;
    private String ulice;
    private String cp;
    private String cor;
    private String mesto;
    private String psc;
    private boolean blokovany = false;

    public String getCislo() {
        return cislo;
    }

    public void setCislo(String cislo) {
        this.cislo = cislo;
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

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
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

    public String getUlice() {
        return ulice;
    }

    public void setUlice(String ulice) {
        this.ulice = ulice;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public boolean isBlokovany() {
        return blokovany;
    }

    public void setBlokovany(boolean blokovany) {
        this.blokovany = blokovany;
    }
}
