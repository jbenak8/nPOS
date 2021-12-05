package cz.jbenak.npos.pos.objekty.meny;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Definuje jednotlivá pravidla zaokrouhlování zejména pro platby vybranou měnou v hotovosti nebo vklady a výběry.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-01-05
 */
public class PravidloZaokrouhlovani implements Serializable {

    @Serial
    private static final long serialVersionUID = 2879153441327792432L;

    public enum SmerZaokrouhleni {
        NAHORU, DOLU
    }

    private int cisloPravidla;
    private String isoKodMeny;
    private String idPlatebnihoProstredku;
    private BigDecimal hodnotaOd;
    private BigDecimal hodnotaDo;
    private BigDecimal hodnotaZaokrouhleni;
    private SmerZaokrouhleni smer;

    public int getCisloPravidla() {
        return cisloPravidla;
    }

    public void setCisloPravidla(int cisloPravidla) {
        this.cisloPravidla = cisloPravidla;
    }

    public String getIsoKodMeny() {
        return isoKodMeny;
    }

    public void setIsoKodMeny(String isoKodMeny) {
        this.isoKodMeny = isoKodMeny;
    }

    public String getIdPlatebnihoProstredku() {
        return idPlatebnihoProstredku;
    }

    public void setIdPlatebnihoProstredku(String idPlatebnihoProstredku) {
        this.idPlatebnihoProstredku = idPlatebnihoProstredku;
    }

    public BigDecimal getHodnotaOd() {
        return hodnotaOd;
    }

    public void setHodnotaOd(BigDecimal hodnotaOd) {
        this.hodnotaOd = hodnotaOd;
    }

    public BigDecimal getHodnotaDo() {
        return hodnotaDo;
    }

    public void setHodnotaDo(BigDecimal hodnotaDo) {
        this.hodnotaDo = hodnotaDo;
    }

    public BigDecimal getHodnotaZaokrouhleni() {
        return hodnotaZaokrouhleni;
    }

    public void setHodnotaZaokrouhleni(BigDecimal hodnotaZaokrouhleni) {
        this.hodnotaZaokrouhleni = hodnotaZaokrouhleni;
    }

    public SmerZaokrouhleni getSmer() {
        return smer;
    }

    public void setSmer(SmerZaokrouhleni smer) {
        this.smer = smer;
    }
}
