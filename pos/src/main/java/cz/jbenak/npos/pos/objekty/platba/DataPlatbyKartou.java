package cz.jbenak.npos.pos.objekty.platba;

import cz.jbenak.npos.pos.objekty.meny.Mena;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data získaná z terminálu při provádění transakce.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-01-30
 */
public class DataPlatbyKartou implements Serializable {

    public enum StavTransakce {
        SCHVALENO, ZAMITNUTO, CHYBA
    }

    @Serial
    private static final long serialVersionUID = 5856675627667898859L;
    private String cisloKarty;
    private String druhKarty;
    private String cisloTransakce;
    private String cisloTerminalu;
    private String autorizacniId;
    private boolean vratka;
    private LocalDateTime datumACasTransakce;
    private BigDecimal castka;
    private String dataZTerminalu;
    private StavTransakce stav;
    private Mena vybranaMena;

    public String getCisloKarty() {
        return cisloKarty;
    }

    public void setCisloKarty(String cisloKarty) {
        this.cisloKarty = cisloKarty;
    }

    public String getDruhKarty() {
        return druhKarty;
    }

    public void setDruhKarty(String druhKarty) {
        this.druhKarty = druhKarty;
    }

    public String getCisloTransakce() {
        return cisloTransakce;
    }

    public void setCisloTransakce(String cisloTransakce) {
        this.cisloTransakce = cisloTransakce;
    }

    public String getCisloTerminalu() {
        return cisloTerminalu;
    }

    public void setCisloTerminalu(String cisloTerminalu) {
        this.cisloTerminalu = cisloTerminalu;
    }

    public String getAutorizacniId() {
        return autorizacniId;
    }

    public void setAutorizacniId(String autorizacniId) {
        this.autorizacniId = autorizacniId;
    }

    public boolean isVratka() {
        return vratka;
    }

    public void setVratka(boolean vratka) {
        this.vratka = vratka;
    }

    public LocalDateTime getDatumACasTransakce() {
        return datumACasTransakce;
    }

    public void setDatumACasTransakce(LocalDateTime datumACasTransakce) {
        this.datumACasTransakce = datumACasTransakce;
    }

    public BigDecimal getCastka() {
        return castka;
    }

    public void setCastka(BigDecimal castka) {
        this.castka = castka;
    }

    public String getDataZTerminalu() {
        return dataZTerminalu;
    }

    public void setDataZTerminalu(String dataZTerminalu) {
        this.dataZTerminalu = dataZTerminalu;
    }

    public StavTransakce getStav() {
        return stav;
    }

    public void setStav(StavTransakce stav) {
        this.stav = stav;
    }

    public Mena getVybranaMena() {
        return vybranaMena;
    }

    public void setVybranaMena(Mena vybranaMena) {
        this.vybranaMena = vybranaMena;
    }
}
