package cz.jbenak.npos.pos.objekty.tiskoveSestavy.paragon;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.objekty.doklad.UplatnenyPoukaz;

import java.math.BigDecimal;

public class ModelUplatnenePoukazy {

    private String cislo;
    private String castka;

    public String getCislo() {
        return cislo;
    }

    public void setCislo(String cislo) {
        this.cislo = cislo;
    }

    public String getCastka() {
        return castka;
    }

    public void setCastka(UplatnenyPoukaz.TypHodnoty typHodnoty, BigDecimal hodnota) {
        this.castka = typHodnoty == UplatnenyPoukaz.TypHodnoty.PROCENTNI ? Pomocnici.formatujNaCele(hodnota) + " %" : Pomocnici.formatujNaDveMista(hodnota);
    }
}
