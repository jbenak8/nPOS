package cz.jbenak.npos.pos.objekty.slevy;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-12-30
 * <p>
 * Objekt pro definici de-facto permanentních registrovatelných slev.
 */

public class SkupinaSlev implements Serializable {

    public enum TypSkupinySlev {
        NA_REGISTR, NA_SKUPINU, NA_ZAKAZNIKA, NA_DOKLAD
    }

    public enum RozsahPouziti {
        JEN_SE_ZAKAZNIKEM, JEN_V_AKCI, VZDY
    }

    public enum OkamzikUplatneni {
        REGISTRACE, PLATBA
    }

    @Serial
    private static final long serialVersionUID = -6120377857850764438L;
    private int cislo;
    private String nazev;
    private TypSkupinySlev typ;
    private List<Pravidlo> pravidla = new ArrayList<>();
    private BigDecimal hodnotaSlevy;
    private Sleva.TypSlevy typSlevy;
    private RozsahPouziti rozsahPouziti;
    private OkamzikUplatneni okamzikUplatneni;
    private String idCileSlevy;

    public int getCislo() {
        return cislo;
    }

    public void setCislo(int cislo) {
        this.cislo = cislo;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public TypSkupinySlev getTyp() {
        return typ;
    }

    public void setTyp(TypSkupinySlev typ) {
        this.typ = typ;
    }

    public List<Pravidlo> getPravidla() {
        return pravidla;
    }

    public BigDecimal getHodnotaSlevy() {
        return hodnotaSlevy;
    }

    public void setHodnotaSlevy(BigDecimal hodnotaSlevy) {
        this.hodnotaSlevy = hodnotaSlevy;
    }

    public Sleva.TypSlevy getTypSlevy() {
        return typSlevy;
    }

    public void setTypSlevy(Sleva.TypSlevy typSlevy) {
        this.typSlevy = typSlevy;
    }

    public RozsahPouziti getRozsahPouziti() {
        return rozsahPouziti;
    }

    public void setRozsahPouziti(RozsahPouziti rozsahPouziti) {
        this.rozsahPouziti = rozsahPouziti;
    }

    public OkamzikUplatneni getOkamzikUplatneni() {
        return okamzikUplatneni;
    }

    public void setOkamzikUplatneni(OkamzikUplatneni okamzikUplatneni) {
        this.okamzikUplatneni = okamzikUplatneni;
    }

    public String getIdCileSlevy() {
        return idCileSlevy;
    }

    public void setIdCileSlevy(String idCileSlevy) {
        this.idCileSlevy = idCileSlevy;
    }
}
