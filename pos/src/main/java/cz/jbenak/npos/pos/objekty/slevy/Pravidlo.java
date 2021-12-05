package cz.jbenak.npos.pos.objekty.slevy;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-12-31
 */
public class Pravidlo implements Serializable {

    public enum Operator {
        A_ZAROVEN, NEBO, OBSAHUJE, NEOBSAHUJE, VICE_NEZ, MENE_NEZ, OD, DO
    }

    public enum TypPodminky {
        SORTIMENT, DOKLAD, SKUPINA, POUKAZ, ZAKAZNIK, MNOZSTVI, CENA_KS, CENA_CELKEM, CAS, SLEVA_HODNOTA, SLEVA_PROCENTA, AKCE
    }

    private static final long serialVersionUID = -3430814208046064670L;
    private final int cisloPravidla;
    private final int poradoveCislo;
    private final Operator operator;
    private final TypPodminky typPodminky;
    private String idObjektuPodminky;
    private BigDecimal hodnotaPodminky;

    public Pravidlo(int cisloPravidla, int poradoveCislo, Operator operator, TypPodminky typPodminky) {
        this.cisloPravidla = cisloPravidla;
        this.poradoveCislo = poradoveCislo;
        this.operator = operator;
        this.typPodminky = typPodminky;
    }

    public String getIdObjektuPodminky() {
        return idObjektuPodminky;
    }

    public void setIdObjektuPodminky(String idObjektuPodminky) {
        this.idObjektuPodminky = idObjektuPodminky;
    }

    public BigDecimal getHodnotaPodminky() {
        return hodnotaPodminky;
    }

    public void setHodnotaPodminky(BigDecimal hodnotaPodminky) {
        this.hodnotaPodminky = hodnotaPodminky;
    }

    public int getCisloPravidla() {
        return cisloPravidla;
    }

    public int getPoradoveCislo() {
        return poradoveCislo;
    }

    public Operator getOperator() {
        return operator;
    }

    public TypPodminky getTypPodminky() {
        return typPodminky;
    }
}
