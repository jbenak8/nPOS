/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.sezeni;

import java.io.Serializable;
import java.util.Date;

/**
 * Datový objekt pro pokladního uživatele.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-02
 */
public class Pokladni implements Serializable {

    private static final long serialVersionUID = -7836861651908347917L;

    private int id;
    private SkupinaUzivatele skupina;
    private String jmeno;
    private String prijmeni;
    private boolean blokovany;
    private boolean hesloPlatne;
    private Date datumHesla;
    private int pocetPrihlasovacichPokusu;
    private boolean skoliciRezim;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SkupinaUzivatele getSkupina() {
        return skupina;
    }

    public void setSkupina(SkupinaUzivatele skupina) {
        this.skupina = skupina;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public boolean isBlokovany() {
        return blokovany;
    }

    public void setBlokovany(boolean blokovany) {
        this.blokovany = blokovany;
    }

    public boolean isHesloPlatne() {
        return hesloPlatne;
    }

    public void setHesloPlatne(boolean hesloPlatne) {
        this.hesloPlatne = hesloPlatne;
    }

    public Date getDatumHesla() {
        return datumHesla;
    }

    public void setDatumHesla(Date datumHesla) {
        this.datumHesla = datumHesla;
    }

    public int getPocetPrihlasovacichPokusu() {
        return pocetPrihlasovacichPokusu;
    }

    public void setPocetPrihlasovacichPokusu(int pocetPrihlasovacichPokusu) {
        this.pocetPrihlasovacichPokusu = pocetPrihlasovacichPokusu;
    }

    public boolean isSkoliciRezim() {
        return skoliciRezim;
    }

    public void setSkoliciRezim(boolean skoliciRezim) {
        this.skoliciRezim = skoliciRezim;
    }
}
