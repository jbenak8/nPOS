package cz.jbenak.npos.pos.objekty.filialka;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class Smena implements Serializable {

    @Serial
    private static final long serialVersionUID = 2484340942139087938L;
    private String cisloSmeny;
    private boolean otevrena;
    private Date datumOtevreni;
    private Date datumUzavreni;
    private int pokladna;
    private Filialka filialka;

    public String getCisloSmeny() {
        return cisloSmeny;
    }

    public void setCisloSmeny(String cisloSmeny) {
        this.cisloSmeny = cisloSmeny;
    }

    public boolean isOtevrena() {
        return otevrena;
    }

    public void setOtevrena(boolean otevrena) {
        this.otevrena = otevrena;
    }

    public Date getDatumOtevreni() {
        return datumOtevreni;
    }

    public void setDatumOtevreni(Date datumOtevreni) {
        this.datumOtevreni = datumOtevreni;
    }

    public Date getDatumUzavreni() {
        return datumUzavreni;
    }

    public void setDatumUzavreni(Date datumUzavreni) {
        this.datumUzavreni = datumUzavreni;
    }

    public int getPokladna() {
        return pokladna;
    }

    public void setPokladna(int pokladna) {
        this.pokladna = pokladna;
    }

    public Filialka getFilialka() {
        return filialka;
    }

    public void setFilialka(Filialka filialka) {
        this.filialka = filialka;
    }
}
