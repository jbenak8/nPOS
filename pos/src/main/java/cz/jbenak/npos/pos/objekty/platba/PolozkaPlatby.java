/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.platba;

import cz.jbenak.npos.pos.objekty.meny.Kurz;
import cz.jbenak.npos.pos.objekty.meny.Mena;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-10
 */
public class PolozkaPlatby implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1541754879808112531L;

    private int cislo;
    private Mena mena;
    private PlatebniProstredek platebniProstredek;
    private Kurz kurzMeny;
    private BigDecimal castka;
    private BigDecimal castkaVKmenoveMene;
    private boolean vratka;

    public int getCislo() {
        return cislo;
    }

    public void setCislo(int cislo) {
        this.cislo = cislo;
    }

    public Mena getMena() {
        return mena;
    }

    public void setMena(Mena mena) {
        this.mena = mena;
    }

    public PlatebniProstredek getPlatebniProstredek() {
        return platebniProstredek;
    }

    public void setPlatebniProstredek(PlatebniProstredek platebniProstredek) {
        this.platebniProstredek = platebniProstredek;
    }

    public Kurz getKurzMeny() {
        return kurzMeny;
    }

    public void setKurzMeny(Kurz kurzMeny) {
        this.kurzMeny = kurzMeny;
    }

    public BigDecimal getCastka() {
        return castka;
    }

    public void setCastka(BigDecimal castka) {
        this.castka = castka;
    }

    public BigDecimal getCastkaVKmenoveMene() {
        return castkaVKmenoveMene;
    }

    public void setCastkaVKmenoveMene(BigDecimal castkaVKmenoveMene) {
        this.castkaVKmenoveMene = castkaVKmenoveMene;
    }

    public boolean isVratka() {
        return vratka;
    }

    public void setVratka(boolean vratka) {
        this.vratka = vratka;
    }
}
