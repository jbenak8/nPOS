/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.meny;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Objekt pro reprezentaci měny.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-03
 */
public class Mena implements Serializable {

    @Serial
    private static final long serialVersionUID = 7791955729255344863L;

    private String isoKod;
    private String oznaceni;
    private String narodniSymbol;
    private boolean akceptovatelna;
    private boolean kmenova;
    private final List<Denominace> denominace = new ArrayList<>();
    private final List<Kurz> kurzy = new ArrayList<>();
    private final List<PravidloZaokrouhlovani> pravidlaZaokrouhlovani = new ArrayList<>();

    public String getIsoKod() {
        return isoKod;
    }

    public void setIsoKod(String isoKod) {
        this.isoKod = isoKod;
    }

    public String getOznaceni() {
        return oznaceni;
    }

    public void setOznaceni(String oznaceni) {
        this.oznaceni = oznaceni;
    }

    public String getNarodniSymbol() {
        return narodniSymbol;
    }

    public void setNarodniSymbol(String narodniSymbol) {
        this.narodniSymbol = narodniSymbol;
    }

    public boolean isAkceptovatelna() {
        return akceptovatelna;
    }

    public void setAkceptovatelna(boolean akceptovatelna) {
        this.akceptovatelna = akceptovatelna;
    }

    public boolean isKmenova() {
        return kmenova;
    }

    public void setKmenova(boolean kmenova) {
        this.kmenova = kmenova;
    }

    public List<Denominace> getDenominace() {
        return denominace;
    }

    public List<Kurz> getKurzy() {
        return kurzy;
    }

    public List<PravidloZaokrouhlovani> getPravidlaZaokrouhlovani() {
        return pravidlaZaokrouhlovani;
    }

    @Override
    public String toString() {
        return "Měna " + isoKod + " - " + oznaceni + ", kmenová: " + kmenova;
    }
}
