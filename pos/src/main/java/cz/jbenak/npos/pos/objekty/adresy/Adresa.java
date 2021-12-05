/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.adresy;

import java.io.Serializable;

/**
 * Objekt reprezentující adresu.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-11
 */
public class Adresa implements Serializable, Comparable<Adresa> {

    private static final long serialVersionUID = 8075607254591530406L;
    private int id;
    private boolean hlavni;
    private boolean dodaci;
    private String adresaDruhyRadek;
    private String cp;
    private String cor;
    private String ulice;
    private String mesto;
    private String psc;
    private Stat stat;
    private String telefon;
    private String mobil;
    private String fax;
    private String email;
    private String web;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHlavni() {
        return hlavni;
    }

    public String getAdresaDruhyRadek() {
        return adresaDruhyRadek;
    }

    public void setAdresaDruhyRadek(String adresaDruhyRadek) {
        this.adresaDruhyRadek = adresaDruhyRadek;
    }

    public void setHlavni(boolean hlavni) {
        this.hlavni = hlavni;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
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

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public boolean isDodaci() {
        return dodaci;
    }

    public void setDodaci(boolean dodaci) {
        this.dodaci = dodaci;
    }

    @Override
    public int compareTo(Adresa o) {
        return (Integer.compare(o.getId(), id));
    }
}
