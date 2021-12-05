package cz.jbenak.npos.pos.objekty.doklad;

import cz.jbenak.npos.pos.objekty.adresy.Adresa;

import java.io.Serial;
import java.io.Serializable;

/**
 * Objekt pro data hlavičky faktury, pokud zákazník (neregistrovaný) musí dostat fakturu místo paragonu.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-02-07
 */
public class HlavickaFaktury implements Serializable {

    @Serial
    private static final long serialVersionUID = -124490840222172020L;
    private Adresa adresa;
    private String jmeno;
    private String prijmeni;
    private String nazev;
    private String ic;
    private String dic;

    public Adresa getAdresa() {
        return adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }
}
