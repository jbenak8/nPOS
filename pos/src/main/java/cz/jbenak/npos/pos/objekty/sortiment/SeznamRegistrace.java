/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.sortiment;

import java.math.BigDecimal;

/**
 * Objekt reprezentující seznam výsledků vyhledávání sortimentu na základě
 * vyhledávání v registraci.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-11
 */
public class SeznamRegistrace {

    private String registr;
    private String nazev;
    private String idSkupiny;
    private String nazevSkupiny;
    private BigDecimal cena;

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

    public String getIdSkupiny() {
        return idSkupiny;
    }

    public void setIdSkupiny(String idSkupiny) {
        this.idSkupiny = idSkupiny;
    }

    public String getNazevSkupiny() {
        return nazevSkupiny;
    }

    public void setNazevSkupiny(String nazevSkupiny) {
        this.nazevSkupiny = nazevSkupiny;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }
}
