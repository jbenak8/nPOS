/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.doklad;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data pro evidenci EET (mimo evidovaných částek DPH - ty jdou z rekapitulační doložky).
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-09
 */
public class DataEET implements Serializable {

    @Serial
    private static final long serialVersionUID = -945427611307400669L;
    public static final int REZIM_BEZNY = 0;
    public static final int REZIM_ZJEDNODUSENY = 1;

    private String bkp;
    private String pkp;
    private String fik;
    private int rezim;
    private boolean prvniOdeslani;
    private boolean odeslaniOK;
    private Date datum;
    private BigDecimal celkovaCastka;
    private int idProvozovny;
    private String dic;
    private String poverujiciDic;
    private final List<ChybyEET> chybyEET;

    public DataEET() {
        this.chybyEET = new ArrayList<>();
    }

    public String getBkp() {
        return bkp;
    }

    public void setBkp(String bkp) {
        this.bkp = bkp;
    }

    public String getPkp() {
        return pkp;
    }

    public void setPkp(String pkp) {
        this.pkp = pkp;
    }

    public String getFik() {
        return fik;
    }

    public void setFik(String fik) {
        this.fik = fik;
    }

    public int getRezim() {
        return rezim;
    }

    public void setRezim(int rezim) {
        this.rezim = rezim;
    }

    public boolean isPrvniOdeslani() {
        return prvniOdeslani;
    }

    public void setPrvniOdeslani(boolean prvniOdeslani) {
        this.prvniOdeslani = prvniOdeslani;
    }

    public boolean isOdeslaniOK() {
        return odeslaniOK;
    }

    public void setOdeslaniOK(boolean odeslaniOK) {
        this.odeslaniOK = odeslaniOK;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public BigDecimal getCelkovaCastka() {
        return celkovaCastka;
    }

    public void setCelkovaCastka(BigDecimal celkovaCastka) {
        this.celkovaCastka = celkovaCastka;
    }

    public int getIdProvozovny() {
        return idProvozovny;
    }

    public void setIdProvozovny(int idProvozovny) {
        this.idProvozovny = idProvozovny;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public String getPoverujiciDic() {
        return poverujiciDic;
    }

    public void setPoverujiciDic(String poverujiciDic) {
        this.poverujiciDic = poverujiciDic;
    }

    public List<ChybyEET> getChybyEET() {
        return chybyEET;
    }
}
