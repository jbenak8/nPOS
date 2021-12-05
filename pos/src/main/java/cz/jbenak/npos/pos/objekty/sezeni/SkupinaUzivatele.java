/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.sezeni;

import java.io.Serializable;

/**
 * Datový objekt pro data uživatelské skupiny.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-02
 */
public class SkupinaUzivatele implements Serializable {

    private static final long serialVersionUID = 4228656426212413202L;

    private int id;
    private String oznaceni;
    private String poznamka;
    // skupina s právy

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOznaceni() {
        return oznaceni;
    }

    public void setOznaceni(String oznaceni) {
        this.oznaceni = oznaceni;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

}
