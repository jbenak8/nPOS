/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.sortiment;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Objekt pro reprezentaci měrné jednotky.
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-11
 */
public class MernaJednotka implements Serializable {
    
    private static final long serialVersionUID = -573921754765356558L;
    private String jednotka;
    private String nazev;
    private String zakladniJednotka;
    private BigDecimal pomer;

    public String getJednotka() {
        return jednotka;
    }

    public void setJednotka(String jednotka) {
        this.jednotka = jednotka;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getZakladniJednotka() {
        return zakladniJednotka;
    }

    public void setZakladniJednotka(String zakladniJednotka) {
        this.zakladniJednotka = zakladniJednotka;
    }

    public BigDecimal getPomer() {
        return pomer;
    }

    public void setPomer(BigDecimal pomer) {
        this.pomer = pomer;
    }
    
}
