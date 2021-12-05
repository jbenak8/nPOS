package cz.jbenak.npos.pos.objekty.tiskoveSestavy.paragon;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.objekty.meny.Kurz;
import cz.jbenak.npos.pos.objekty.meny.Mena;
import cz.jbenak.npos.pos.objekty.platba.PlatebniProstredek;
import cz.jbenak.npos.pos.system.Pos;

import java.math.BigDecimal;

public class ModelPolozkyPlatby {

    private String mena;
    private String typ;
    private String kurz;
    private String zaplaceno;
    private String hodnota_platby;

    public String getMena() {
        return mena;
    }

    public void setMena(Mena mena) {
        this.mena = mena.getNarodniSymbol();
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(PlatebniProstredek.TypPlatebnihoProstredku typ) {
        this.typ = typ.name();
    }

    public String getKurz() {
        return kurz;
    }

    public void setKurz(Kurz kurz) {
        this.kurz = kurz == null ? "" : Pomocnici.formatujNaTriMista(kurz.getNakup());
    }

    public String getZaplaceno() {
        return zaplaceno;
    }

    public void setZaplaceno(BigDecimal zaplaceno) {
        this.zaplaceno = Pomocnici.formatujNaDveMista(zaplaceno);
    }

    public String getHodnota_platby() {
        return hodnota_platby;
    }

    public void setHodnota_platby(BigDecimal hodnota) {
        this.hodnota_platby = Pomocnici.formatujNaDveMista(hodnota) + Pos.getInstance().getHlavniMena().getNarodniSymbol();
    }
}
