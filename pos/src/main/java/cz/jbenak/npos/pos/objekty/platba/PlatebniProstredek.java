package cz.jbenak.npos.pos.objekty.platba;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class PlatebniProstredek implements Serializable {

    public enum TypPlatebnihoProstredku {
        HOTOVOST, KARTA, POUKAZ
    }

    @Serial
    private static final long serialVersionUID = -52609539641385011L;
    private String id;
    private TypPlatebnihoProstredku typ;
    private String oznaceni;
    private boolean akceptovatelny;
    private boolean ciziMenaPovolena;
    private boolean zaokrouhlitDleDenominace;
    private boolean stornoOk;
    private boolean vratkaOk;
    private boolean evidenceVZasuvce;
    private boolean evidenceVTrezoru;
    private boolean otevritZasuvku;
    private boolean pocitaniNutne;
    private BigDecimal minCastkaPrijem;
    private BigDecimal maxCastkaPrijem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TypPlatebnihoProstredku getTyp() {
        return typ;
    }

    public void setTyp(TypPlatebnihoProstredku typ) {
        this.typ = typ;
    }

    public String getOznaceni() {
        return oznaceni;
    }

    public void setOznaceni(String oznaceni) {
        this.oznaceni = oznaceni;
    }

    public boolean isAkceptovatelny() {
        return akceptovatelny;
    }

    public void setAkceptovatelny(boolean akceptovatelny) {
        this.akceptovatelny = akceptovatelny;
    }

    public boolean isCiziMenaPovolena() {
        return ciziMenaPovolena;
    }

    public void setCiziMenaPovolena(boolean ciziMenaPovolena) {
        this.ciziMenaPovolena = ciziMenaPovolena;
    }

    public boolean isZaokrouhlitDleDenominace() {
        return zaokrouhlitDleDenominace;
    }

    public void setZaokrouhlitDleDenominace(boolean zaokrouhlitDleDenominace) {
        this.zaokrouhlitDleDenominace = zaokrouhlitDleDenominace;
    }

    public boolean isStornoOk() {
        return stornoOk;
    }

    public void setStornoOk(boolean stornoOk) {
        this.stornoOk = stornoOk;
    }

    public boolean isVratkaOk() {
        return vratkaOk;
    }

    public void setVratkaOk(boolean vratkaOk) {
        this.vratkaOk = vratkaOk;
    }

    public boolean isEvidenceVZasuvce() {
        return evidenceVZasuvce;
    }

    public void setEvidenceVZasuvce(boolean evidenceVZasuvce) {
        this.evidenceVZasuvce = evidenceVZasuvce;
    }

    public boolean isEvidenceVTrezoru() {
        return evidenceVTrezoru;
    }

    public void setEvidenceVTrezoru(boolean evidenceVTrezoru) {
        this.evidenceVTrezoru = evidenceVTrezoru;
    }

    public boolean isOtevritZasuvku() {
        return otevritZasuvku;
    }

    public void setOtevritZasuvku(boolean otevritZasuvku) {
        this.otevritZasuvku = otevritZasuvku;
    }

    public boolean isPocitaniNutne() {
        return pocitaniNutne;
    }

    public void setPocitaniNutne(boolean pocitaniNutne) {
        this.pocitaniNutne = pocitaniNutne;
    }

    public BigDecimal getMinCastkaPrijem() {
        return minCastkaPrijem;
    }

    public void setMinCastkaPrijem(BigDecimal minCastkaPrijem) {
        this.minCastkaPrijem = minCastkaPrijem;
    }

    public BigDecimal getMaxCastkaPrijem() {
        return maxCastkaPrijem;
    }

    public void setMaxCastkaPrijem(BigDecimal maxCastkaPrijem) {
        this.maxCastkaPrijem = maxCastkaPrijem;
    }

    @Override
    public String toString() {
        return "Platební prostředek " + id + " " + typ;
    }
}
