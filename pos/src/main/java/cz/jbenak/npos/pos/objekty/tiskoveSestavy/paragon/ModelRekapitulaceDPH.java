package cz.jbenak.npos.pos.objekty.tiskoveSestavy.paragon;

import cz.jbenak.npos.pos.gui.Pomocnici;

import java.math.BigDecimal;

public class ModelRekapitulaceDPH {

    private String sazba;
    private String dan;
    private String zaklad;
    private String celkem;

    public String getSazba() {
        return sazba;
    }

    public void setSazba(BigDecimal sazba) {
        this.sazba = Pomocnici.formatujNaCele(sazba) + " %";
    }

    public String getDan() {
        return dan;
    }

    public void setDan(BigDecimal dan) {
        this.dan = Pomocnici.formatujNaDveMista(dan);
    }

    public String getZaklad() {
        return zaklad;
    }

    public void setZaklad(BigDecimal zaklad) {
        this.zaklad = Pomocnici.formatujNaDveMista(zaklad);
    }

    public String getCelkem() {
        return celkem;
    }

    public void setCelkem(BigDecimal celkem) {
        this.celkem = Pomocnici.formatujNaDveMista(celkem);
    }
}
