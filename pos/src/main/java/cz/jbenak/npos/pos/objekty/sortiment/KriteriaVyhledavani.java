package cz.jbenak.npos.pos.objekty.sortiment;

import java.math.BigDecimal;

/**
 * Třída reprezentující zadání kritérií pro vyhledávání sortimentu z vyhledávacího dialogu.
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-03-04
 */

public class KriteriaVyhledavani {

    private String registr;
    private String nazev;
    private String carovyKod;
    private SkupinaSortimentu skupina;
    private BigDecimal cenaOd;
    private BigDecimal cenaDo;
    private boolean jenNeblokovane = false;
    private int maxZazanmu = -1;

    public String getRegistr() {
        return registr;
    }

    public void setRegistr(String registr) {
        this.registr = registr;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getCarovyKod() {
        return carovyKod;
    }

    public void setCarovyKod(String carovyKod) {
        this.carovyKod = carovyKod;
    }

    public SkupinaSortimentu getSkupina() {
        return skupina;
    }

    public void setSkupina(SkupinaSortimentu skupina) {
        this.skupina = skupina;
    }

    public BigDecimal getCenaOd() {
        return cenaOd;
    }

    public void setCenaOd(BigDecimal cenaOd) {
        this.cenaOd = cenaOd;
    }

    public BigDecimal getCenaDo() {
        return cenaDo;
    }

    public void setCenaDo(BigDecimal cenaDo) {
        this.cenaDo = cenaDo;
    }

    public boolean isJenNeblokovane() {
        return jenNeblokovane;
    }

    public void setJenNeblokovane(boolean jenNeblokovane) {
        this.jenNeblokovane = jenNeblokovane;
    }

    public int getMaxZazanmu() {
        return maxZazanmu;
    }

    public void setMaxZazanmu(int maxZazanmu) {
        this.maxZazanmu = maxZazanmu;
    }
}
