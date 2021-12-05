package cz.jbenak.npos.pos.objekty.meny;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data reprezentující kurz dané měny.
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-01-25
 */
public class Kurz implements Serializable {

    @Serial
    private static final long serialVersionUID = -7176298715947047226L;
    private int cisloKurzu;
    private String isoKodMeny;
    private LocalDate platnostOd;
    private LocalDate platnostDo;
    private BigDecimal nakup;
    private BigDecimal prodej;

    public int getCisloKurzu() {
        return cisloKurzu;
    }

    public void setCisloKurzu(int cisloKurzu) {
        this.cisloKurzu = cisloKurzu;
    }

    public String getIsoKodMeny() {
        return isoKodMeny;
    }

    public void setIsoKodMeny(String isoKodMeny) {
        this.isoKodMeny = isoKodMeny;
    }

    public LocalDate getPlatnostOd() {
        return platnostOd;
    }

    public void setPlatnostOd(LocalDate platnostOd) {
        this.platnostOd = platnostOd;
    }

    public LocalDate getPlatnostDo() {
        return platnostDo;
    }

    public void setPlatnostDo(LocalDate platnostDo) {
        this.platnostDo = platnostDo;
    }

    public BigDecimal getNakup() {
        return nakup;
    }

    public void setNakup(BigDecimal nakup) {
        this.nakup = nakup;
    }

    public BigDecimal getProdej() {
        return prodej;
    }

    public void setProdej(BigDecimal prodej) {
        this.prodej = prodej;
    }
}
