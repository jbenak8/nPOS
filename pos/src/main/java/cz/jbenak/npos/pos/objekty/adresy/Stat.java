/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.adresy;

import java.io.Serializable;

/**
 * Objekt pro reprezentaci států.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-03
 */
public class Stat implements Serializable {

    private static final long serialVersionUID = -1923345893746821706L;

    private String isoKod;
    private String beznyNazev;
    private String uplnyNazev;
    private boolean hlavni;
    private String kodMeny;

    public String getIsoKod() {
        return isoKod;
    }

    public void setIsoKod(String isoKod) {
        this.isoKod = isoKod;
    }

    public String getBeznyNazev() {
        return beznyNazev;
    }

    public void setBeznyNazev(String beznyNazev) {
        this.beznyNazev = beznyNazev;
    }

    public String getUplnyNazev() {
        return uplnyNazev;
    }

    public void setUplnyNazev(String uplnyNazev) {
        this.uplnyNazev = uplnyNazev;
    }

    public boolean isHlavni() {
        return hlavni;
    }

    public void setHlavni(boolean hlavni) {
        this.hlavni = hlavni;
    }

    public String getKodMeny() {
        return kodMeny;
    }

    public void setKodMeny(String kodMeny) {
        this.kodMeny = kodMeny;
    }
}
