/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.sortiment;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Třída reprezentující skupinu sortimentu.
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-11
 */
public class SkupinaSortimentu implements Serializable, Comparable<SkupinaSortimentu> {

    private static final long serialVersionUID = -164576205665927356L;
    
    private String id;
    private String idNadrazeneSkupiny;
    private String nazev;
    private boolean slevaPovolena;
    private BigDecimal maxSleva;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdNadrazeneSkupiny() {
        return idNadrazeneSkupiny;
    }

    public void setIdNadrazeneSkupiny(String idNadrazeneSkupiny) {
        this.idNadrazeneSkupiny = idNadrazeneSkupiny;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public boolean isSlevaPovolena() {
        return slevaPovolena;
    }

    public void setSlevaPovolena(boolean slevaPovolena) {
        this.slevaPovolena = slevaPovolena;
    }

    public BigDecimal getMaxSleva() {
        return maxSleva;
    }

    public void setMaxSleva(BigDecimal maxSleva) {
        this.maxSleva = maxSleva;
    }

    @Override
    public int compareTo(SkupinaSortimentu o) {
        return o.getId().compareToIgnoreCase(id);
    }

    @Override
    public String toString() {
        return id + " - " + nazev;
    }
}
