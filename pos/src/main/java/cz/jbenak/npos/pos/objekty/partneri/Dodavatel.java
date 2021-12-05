/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.partneri;

import cz.jbenak.npos.pos.objekty.adresy.Adresa;
import java.io.Serializable;
import java.util.List;

/**
 * Objekt reprezentující dodavatele
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-11
 */
public class Dodavatel implements Serializable {
    
    private static final long serialVersionUID = 8400401762792673686L;
    private int id;
    private String nazev;
    private String doplnekNazvu;
    private String ic;
    private String dic;
    private List<Adresa> adresy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getDoplnekNazvu() {
        return doplnekNazvu;
    }

    public void setDoplnekNazvu(String doplnekNazvu) {
        this.doplnekNazvu = doplnekNazvu;
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

    public List<Adresa> getAdresy() {
        return adresy;
    }

    public void setAdresy(List<Adresa> adresy) {
        this.adresy = adresy;
    }
    
}
