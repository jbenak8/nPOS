/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.doklad;

import cz.jbenak.npos.pos.objekty.finance.DPH;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Třída reprezentující rekapitulační doložku DPH.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-09
 */
public class RekapitulaceDPH implements Serializable, Comparable<RekapitulaceDPH> {

    @Serial
    private static final long serialVersionUID = 4038791042972374983L;
    private DPH dph;
    private BigDecimal zaklad;
    private BigDecimal dan;
    private BigDecimal celkem;

    /**
     * Metoda pro získání objektu DPH
     *
     * @return sazba DPH
     */
    public DPH getDph() {
        return dph;
    }

    public void setDph(DPH dph) {
        this.dph = dph;
    }

    public BigDecimal getZaklad() {
        return zaklad;
    }

    public void setZaklad(BigDecimal zaklad) {
        this.zaklad = zaklad;
    }

    public BigDecimal getDan() {
        return dan;
    }

    public void setDan(BigDecimal dan) {
        this.dan = dan;
    }

    public BigDecimal getCelkem() {
        return celkem;
    }

    public void setCelkem(BigDecimal celkem) {
        this.celkem = celkem;
    }

    @Override
    public int compareTo(RekapitulaceDPH o) {
        return o.getDph().getSazba().compareTo(dph.getSazba());
    }

}
