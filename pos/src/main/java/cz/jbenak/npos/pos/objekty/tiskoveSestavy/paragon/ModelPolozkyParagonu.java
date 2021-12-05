package cz.jbenak.npos.pos.objekty.tiskoveSestavy.paragon;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.objekty.slevy.Sleva;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;
import cz.jbenak.npos.pos.procesory.ManazerSlev;
import cz.jbenak.npos.pos.system.Pos;

import java.math.BigDecimal;
import java.util.List;

/**
 * Model položek paragonu pro tisk.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-02-07
 */
public class ModelPolozkyParagonu {

    private String registr;
    private String nazev;
    private String cena_celkem;
    private String sazba_dph;
    private String j_cena_mj;
    private Boolean vratka;
    private String orig_doklad;
    private String slevy_popis;
    private String slevy_hodnoty;

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

    public String getCena_celkem() {
        return cena_celkem;
    }

    public void setCena_celkem(BigDecimal polozkaCelkem) {
        this.cena_celkem = Pomocnici.formatujNaDveMista(polozkaCelkem);
    }

    public String getSazba_dph() {
        return sazba_dph;
    }

    public void setSazba_dph(BigDecimal sazba) {
        this.sazba_dph = Pomocnici.formatujNaCele(sazba) + " %";
    }

    public String getJ_cena_mj() {
        return j_cena_mj;
    }

    public void setJ_cena_mj(BigDecimal jednotkovaCena, BigDecimal mnozstvi, String mj) {
        this.j_cena_mj = Pomocnici.formatujNaDveMista(mnozstvi) + " " + mj + " × " + Pomocnici.formatujNaDveMista(jednotkovaCena);
    }

    public Boolean getVratka() {
        return vratka;
    }

    public void setVratka(Boolean vratka) {
        this.vratka = vratka;
    }

    public String getOrig_doklad() {
        return orig_doklad;
    }

    public void setOrig_doklad(String orig_doklad) {
        this.orig_doklad = orig_doklad;
    }

    public String getSlevy_popis() {
        return slevy_popis;
    }

    public void setSlevy(Sortiment polozka) {
        final StringBuilder popisy = new StringBuilder();
        final StringBuilder hodnoty = new StringBuilder();
        for (int i = 0; i < polozka.getSlevy().size(); i++) {
            popisy.append(polozka.getSlevy().get(i).getPopis());
            hodnoty.append(Pomocnici.formatujNaDveMista(ManazerSlev.spoctiHodnotuSlevy(polozka, polozka.getSlevy().get(i))));
            if (i < polozka.getSlevy().size() - 1) {
                popisy.append("\n");
                hodnoty.append("\n");
            }
        }
        this.slevy_popis = popisy.toString();
        this.slevy_hodnoty = hodnoty.toString();
    }

    public String getSlevy_hodnoty() {
        return slevy_hodnoty;
    }
}
