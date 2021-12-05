package cz.jbenak.npos.pos.objekty.doklad;

import java.io.Serial;
import java.io.Serializable;

/**
 * Třída pro serializaci chyb vrácených při registraci EET.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-02-13
 */
public class ChybyEET implements Serializable {

    @Serial
    private static final long serialVersionUID = 5894170658882041539L;
    private int cislo;
    private String popis;

    public int getCislo() {
        return cislo;
    }

    public void setCislo(int cislo) {
        this.cislo = cislo;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }
}
