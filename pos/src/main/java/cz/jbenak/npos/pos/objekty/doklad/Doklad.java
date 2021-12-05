/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.objekty.doklad;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.objekty.filialka.Smena;
import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
import cz.jbenak.npos.pos.objekty.platba.DataPlatbyKartou;
import cz.jbenak.npos.pos.objekty.platba.PolozkaPlatby;
import cz.jbenak.npos.pos.objekty.sezeni.Pokladni;
import cz.jbenak.npos.pos.objekty.slevy.Sleva;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-09
 */
public class Doklad implements Serializable {

    @Serial
    private static final long serialVersionUID = 8485428616211853270L;

    private final UUID id;
    private final List<Sortiment> polozky;
    private final List<RekapitulaceDPH> tabulkaDPH;
    private final List<PolozkaPlatby> platby;
    private final List<Sleva> slevy;
    private final List<UplatnenyPoukaz> uplatnenePoukazy;
    private final List<DataPlatbyKartou> dataPlatbyKartou;
    private TypDokladu typ;
    private String cislo; //číslo dokladu dle řady
    private int poradoveCislo;
    private int cisloPokladny;
    private boolean vratka;
    private boolean storno;
    private Date datumVytvoreni;
    private Pokladni pokladni;
    private DataEET dataEET;
    private Zakaznik zakaznik;
    private boolean polozkaOtevrena;
    private boolean maualniSlevyZakazany = false;
    private Smena smena;
    private UUID sparovanyDoklad;
    private String sparovanyDokladCislo;
    private BigDecimal castkaKVraceni;
    private HlavickaFaktury hlavickaFaktury;

    public Doklad(TypDokladu typ) {
        this.id = UUID.randomUUID();
        this.polozky = new ArrayList<>();
        this.platby = new ArrayList<>();
        this.slevy = new ArrayList<>();
        this.tabulkaDPH = new ArrayList<>();
        this.typ = typ;
        this.dataPlatbyKartou = new ArrayList<>();
        this.uplatnenePoukazy = new ArrayList<>();
    }

    public List<Sortiment> getPolozky() {
        return this.polozky;
    }

    public TypDokladu getTyp() {
        return typ;
    }

    public void zmenTyp(TypDokladu typ) {
        this.typ = typ;
    }

    public String getCislo() {
        return cislo;
    }

    public void setCislo(String cislo) {
        this.cislo = cislo;
    }

    public int getPoradoveCislo() {
        return poradoveCislo;
    }

    public void setPoradoveCislo(int poradoveCislo) {
        this.poradoveCislo = poradoveCislo;
    }

    public int getCisloPokladny() {
        return cisloPokladny;
    }

    public void setCisloPokladny(int cisloPokladny) {
        this.cisloPokladny = cisloPokladny;
    }

    public boolean isVratka() {
        return vratka;
    }

    public boolean isStorno() {
        return storno;
    }

    public void setStorno(boolean storno) {
        this.storno = storno;
    }

    public void setVratka(boolean vratka) {
        this.vratka = vratka;
    }

    public Date getDatumVytvoreni() {
        return datumVytvoreni;
    }

    public void setDatumVytvoreni(Date datumVytvoreni) {
        this.datumVytvoreni = datumVytvoreni;
    }

    public Pokladni getPokladni() {
        return pokladni;
    }

    public void setPokladni(Pokladni pokladni) {
        this.pokladni = pokladni;
    }

    public DataEET getDataEET() {
        return dataEET;
    }

    public void setDataEET(DataEET dataEET) {
        this.dataEET = dataEET;
    }

    public Zakaznik getZakaznik() {
        return zakaznik;
    }

    public void setZakaznik(Zakaznik zakaznik) {
        this.zakaznik = zakaznik;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public List<RekapitulaceDPH> getTabulkaDPH() {
        return tabulkaDPH;
    }

    public List<PolozkaPlatby> getPlatby() {
        return platby;
    }

    public List<Sleva> getSlevy() {
        return slevy;
    }

    public List<DataPlatbyKartou> getDataPlatbyKartou() {
        return dataPlatbyKartou;
    }

    public List<UplatnenyPoukaz> getUplatnenePoukazy() {
        return uplatnenePoukazy;
    }

    public boolean isPolozkaOtevrena() {
        return polozkaOtevrena;
    }

    public void setPolozkaOtevrena(boolean polozkaOtevrena) {
        this.polozkaOtevrena = polozkaOtevrena;
    }

    public boolean isMaualniSlevyZakazany() {
        return maualniSlevyZakazany;
    }

    public void setMaualniSlevyZakazany(boolean maualniSlevyZakazany) {
        this.maualniSlevyZakazany = maualniSlevyZakazany;
    }

    public Smena getSmena() {
        return smena;
    }

    public void setSmena(Smena smena) {
        this.smena = smena;
    }

    public UUID getSparovanyDoklad() {
        return sparovanyDoklad;
    }

    public void setSparovanyDoklad(UUID sparovanyDoklad) {
        this.sparovanyDoklad = sparovanyDoklad;
    }

    public String getSparovanyDokladCislo() {
        return sparovanyDokladCislo;
    }

    public void setSparovanyDokladCislo(String sparovanyDokladCislo) {
        this.sparovanyDokladCislo = sparovanyDokladCislo;
    }

    public BigDecimal getCastkaKVraceni() {
        return castkaKVraceni;
    }

    public void setCastkaKVraceni(BigDecimal castkaKVraceni) {
        this.castkaKVraceni = castkaKVraceni;
    }

    public HlavickaFaktury getHlavickaFaktury() {
        return hlavickaFaktury;
    }

    public void setHlavickaFaktury(HlavickaFaktury hlavickaFaktury) {
        this.hlavickaFaktury = hlavickaFaktury;
    }

    public BigDecimal getCenaDokladuCelkem() {
        BigDecimal celkem = BigDecimal.ZERO;
        if (polozky.isEmpty()) {
            return celkem;
        } else {
            for (Sortiment s : polozky) {
                if (s.isVracena()) {
                    celkem = celkem.subtract(s.getCenaCelkem());
                } else {
                    celkem = celkem.add(s.getCenaCelkem());
                }
            }
            return celkem;
        }
    }

    public BigDecimal getCelkemZaklad() {
        BigDecimal celkem = BigDecimal.ZERO;
        for (RekapitulaceDPH pol : tabulkaDPH) {
            celkem = celkem.add(pol.getZaklad());
        }
        return celkem;
    }

    /**
     * Zjistí celkovou hodnotu plateb v kmenové měně.
     *
     * @return Zaplacená částka v kmenové měně.
     */
    public BigDecimal getCelkemZaplaceno() {
        BigDecimal celkemZaplaceno = BigDecimal.ZERO;
        if (!platby.isEmpty()) {
            for (PolozkaPlatby pl : platby) {
                celkemZaplaceno = celkemZaplaceno.add(pl.getCastkaVKmenoveMene());
            }
        }
        return celkemZaplaceno;
    }

    @Override
    public String toString() {
        String text = "Doklad ID: " + this.id;
        text += " Číslo: " + (this.cislo == null ? "číslo neurčeno" : this.cislo);
        text += " Pokladna: " + this.cisloPokladny;
        text += " Datum: " + Pomocnici.formatujDatumACas(this.datumVytvoreni);
        text += " Typ: " + this.typ;
        text += " Položek: " + getPolozky().size();
        text += " Celkem: " + getCenaDokladuCelkem();
        return text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Doklad) {
            Doklad porovnavany = (Doklad) obj;
            boolean cisloOK = porovnavany.cislo.equalsIgnoreCase(this.cislo);
            boolean pokladnaOK = porovnavany.getCisloPokladny() == this.cisloPokladny;
            boolean datumOk = porovnavany.getDatumVytvoreni().equals(this.datumVytvoreni);
            boolean typOk = porovnavany.getTyp().equals(this.typ);
            boolean celkemOk = porovnavany.getCenaDokladuCelkem().equals(this.getCenaDokladuCelkem());
            return cisloOK && pokladnaOK && datumOk && typOk && celkemOk;
        } else {
            return false;
        }
    }
}
