package cz.jbenak.npos.pos.objekty.meny;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class Denominace implements Serializable {

    public enum TypPlatidla {
        BANKOVKA, MINCE
    }

    @Serial
    private static final long serialVersionUID = -248139483607509388L;
    private int cislo;
    private TypPlatidla typPlatidla;
    private String isoKodMeny;
    private int poradi;
    private int jednotka;
    private BigDecimal hodnota;
    private String nazevJednotky;
    private int ksBalicek;
    private boolean akceptovatelne;
    private boolean zobrazitNaTlacitku;

    public int getCislo() {
        return cislo;
    }

    public void setCislo(int cislo) {
        this.cislo = cislo;
    }

    public TypPlatidla getTypPlatidla() {
        return typPlatidla;
    }

    public void setTypPlatidla(TypPlatidla typPlatidla) {
        this.typPlatidla = typPlatidla;
    }

    public String getIsoKodMeny() {
        return isoKodMeny;
    }

    public void setIsoKodMeny(String isoKodMeny) {
        this.isoKodMeny = isoKodMeny;
    }

    public int getPoradi() {
        return poradi;
    }

    public void setPoradi(int poradi) {
        this.poradi = poradi;
    }

    public int getJednotka() {
        return jednotka;
    }

    public void setJednotka(int jednotka) {
        this.jednotka = jednotka;
    }

    public BigDecimal getHodnota() {
        return hodnota;
    }

    public void setHodnota(BigDecimal hodnota) {
        this.hodnota = hodnota;
    }

    public String getNazevJednotky() {
        return nazevJednotky;
    }

    public void setNazevJednotky(String nazevJednotky) {
        this.nazevJednotky = nazevJednotky;
    }

    public int getKsBalicek() {
        return ksBalicek;
    }

    public void setKsBalicek(int ksBalicek) {
        this.ksBalicek = ksBalicek;
    }

    public boolean isAkceptovatelne() {
        return akceptovatelne;
    }

    public void setAkceptovatelne(boolean akceptovatelne) {
        this.akceptovatelne = akceptovatelne;
    }

    public boolean isZobrazitNaTlacitku() {
        return zobrazitNaTlacitku;
    }

    public void setZobrazitNaTlacitku(boolean zobrazitNaTlacitku) {
        this.zobrazitNaTlacitku = zobrazitNaTlacitku;
    }

    @Override
    public String toString() {
        return "Denominace:\n" +
                """
                        Měna | Typ platidla | Pořadí | Jednotka | Hodnota |
                        """ +
                isoKodMeny + " | " +
                typPlatidla.name() + " | " +
                poradi + " | " +
                jednotka + " | " +
                hodnota.toPlainString() + " |\n" +
                "-----------------------------------------------";
    }
}
