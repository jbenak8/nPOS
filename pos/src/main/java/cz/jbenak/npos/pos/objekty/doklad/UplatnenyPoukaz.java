package cz.jbenak.npos.pos.objekty.doklad;

import cz.jbenak.npos.pos.objekty.platba.PolozkaPlatby;
import cz.jbenak.npos.pos.objekty.slevy.SkupinaSlev;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Definuje uplatněné platební nebo slevové poukazy na doklad;
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-02-02
 */
public class UplatnenyPoukaz implements Serializable {

    public enum TypHodnoty {
        PROCENTNI, HODNOTOVY
    }

    @Serial
    private static final long serialVersionUID = 3576258856833789776L;

    private String cisloPoukazu;
    private BigDecimal hodnotaPoukazu;
    private BigDecimal uplatnenaHodnota;
    private boolean castecneUplatneni;
    private SkupinaSlev vazanaSkupinaSlev;
    private PolozkaPlatby vazanaPolozkaPlatby;
    private TypHodnoty typHodnoty;

    public String getCisloPoukazu() {
        return cisloPoukazu;
    }

    public void setCisloPoukazu(String cisloPoukazu) {
        this.cisloPoukazu = cisloPoukazu;
    }

    public BigDecimal getHodnotaPoukazu() {
        return hodnotaPoukazu;
    }

    public void setHodnotaPoukazu(BigDecimal hodnotaPoukazu) {
        this.hodnotaPoukazu = hodnotaPoukazu;
    }

    public BigDecimal getUplatnenaHodnota() {
        return uplatnenaHodnota;
    }

    public void setUplatnenaHodnota(BigDecimal uplatnenaHodnota) {
        this.uplatnenaHodnota = uplatnenaHodnota;
    }

    public boolean isCastecneUplatneni() {
        return castecneUplatneni;
    }

    public void setCastecneUplatneni(boolean castecneUplatneni) {
        this.castecneUplatneni = castecneUplatneni;
    }

    public SkupinaSlev getVazanaSkupinaSlev() {
        return vazanaSkupinaSlev;
    }

    public void setVazanaSkupinaSlev(SkupinaSlev vazanaSkupinaSlev) {
        this.vazanaSkupinaSlev = vazanaSkupinaSlev;
    }

    public PolozkaPlatby getVazanaPolozkaPlatby() {
        return vazanaPolozkaPlatby;
    }

    public void setVazanaPolozkaPlatby(PolozkaPlatby vazanaPolozkaPlatby) {
        this.vazanaPolozkaPlatby = vazanaPolozkaPlatby;
    }

    public TypHodnoty getTypHodnoty() {
        return typHodnoty;
    }

    public void setTypHodnoty(TypHodnoty typHodnoty) {
        this.typHodnoty = typHodnoty;
    }
}
