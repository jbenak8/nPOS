/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.filialka;

import cz.jbenak.npos.pos.objekty.adresy.Stat;
import java.io.Serializable;

/**
 * Objekt pro reprezentaci dat obchodu pokladny.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-03
 */
public class Filialka implements Serializable {

    private static final long serialVersionUID = 6559045558607166522L;
    private String oznaceni;
    private String nazev;
    private String popis;
    private String cp_cor;
    private String ulice;
    private String mesto;
    private String psc;
    private Stat stat;
    private String telefon;
    private String mobil;
    private String email;
    private String odpovednyVedouci;
    private String ic;
    private String dic;
    private String danovyKodDph;
    private String nazevSpolecnosti;
    private String adresaSpolecnosti;
    private String url;
    private String zapisOr;

    public String getOznaceni() {
        return oznaceni;
    }

    public void setOznaceni(String oznaceni) {
        this.oznaceni = oznaceni;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public String getCp_cor() {
        return cp_cor;
    }

    public void setCp_cor(String cp_cor) {
        this.cp_cor = cp_cor;
    }

    public String getUlice() {
        return ulice;
    }

    public void setUlice(String ulice) {
        this.ulice = ulice;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMobil() {
        return mobil;
    }

    public void setMobil(String mobil) {
        this.mobil = mobil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOdpovednyVedouci() {
        return odpovednyVedouci;
    }

    public void setOdpovednyVedouci(String odpovednyVedouci) {
        this.odpovednyVedouci = odpovednyVedouci;
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

    public String getDanovyKodDph() {
        return danovyKodDph;
    }

    public void setDanovyKodDph(String danovyKodDph) {
        this.danovyKodDph = danovyKodDph;
    }

    public String getNazevSpolecnosti() {
        return nazevSpolecnosti;
    }

    public void setNazevSpolecnosti(String nazevSpolecnosti) {
        this.nazevSpolecnosti = nazevSpolecnosti;
    }

    public String getAdresaSpolecnosti() {
        return adresaSpolecnosti;
    }

    public void setAdresaSpolecnosti(String adresaSpolecnosti) {
        this.adresaSpolecnosti = adresaSpolecnosti;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getZapisOr() {
        return zapisOr;
    }

    public void setZapisOr(String zapisOr) {
        this.zapisOr = zapisOr;
    }
}
