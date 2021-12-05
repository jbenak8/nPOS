/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.finance;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Objekt reprezentující DPH.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-09
 */
public class DPH implements Serializable, Comparable<DPH> {

    public enum TypDPH {
        ZAKLADNI, SNIZENA_1, SNIZENA_2, NULOVA;
    }

    @Serial
    private static final long serialVersionUID = 5166299043935150944L;
    private int id;
    private TypDPH typ;
    private BigDecimal sazba;
    private String oznaceni;
    private Date platnostOd;
    private Date platnostDo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypDPH getTyp() {
        return typ;
    }

    public void setTyp(TypDPH typ) {
        this.typ = typ;
    }

    public BigDecimal getSazba() {
        return sazba;
    }

    public void setSazba(BigDecimal sazba) {
        this.sazba = sazba;
    }

    public String getOznaceni() {
        return oznaceni;
    }

    public void setOznaceni(String oznaceni) {
        this.oznaceni = oznaceni;
    }

    public Date getPlatnostOd() {
        return platnostOd;
    }

    public void setPlatnostOd(Date platnostOd) {
        this.platnostOd = platnostOd;
    }

    public Date getPlatnostDo() {
        return platnostDo;
    }

    public void setPlatnostDo(Date platnostDo) {
        this.platnostDo = platnostDo;
    }

    @Override
    public int compareTo(DPH o) {
        return o.getSazba().compareTo(sazba);
    }
}
