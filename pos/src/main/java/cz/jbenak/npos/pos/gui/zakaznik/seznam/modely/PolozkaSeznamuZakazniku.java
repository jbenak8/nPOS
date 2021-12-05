package cz.jbenak.npos.pos.gui.zakaznik.seznam.modely;

import cz.jbenak.npos.pos.objekty.adresy.Adresa;
import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Optional;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-12-30
 */

public class PolozkaSeznamuZakazniku {

    private final Zakaznik zakaznik;

    public PolozkaSeznamuZakazniku(Zakaznik zakaznik) {
        this.zakaznik = zakaznik;
    }

    public Zakaznik getZakaznik() {
        return zakaznik;
    }

    public StringProperty getCisloZakaznika() {
        return new SimpleStringProperty(zakaznik.getCislo());
    }

    public StringProperty getJmeno() {
        String jmeno = "";
        if (zakaznik.getJmeno() != null) {
            jmeno += zakaznik.getJmeno() + " ";
        }
        if (zakaznik.getPrijmeni() != null) {
            jmeno += zakaznik.getPrijmeni();
        }
        return new SimpleStringProperty(jmeno);
    }

    public StringProperty getNazev() {
        return new SimpleStringProperty(zakaznik.getNazev());
    }

    public StringProperty getTextBlokovany() {
        return new SimpleStringProperty(zakaznik.isBlokovan() ? "\u2BBF" : "");
    }

    public StringProperty getAdresa() {
        if (!zakaznik.getAdresy().isEmpty()) {
            Optional<Adresa> hlAdresa = zakaznik.getAdresy().stream().filter(Adresa::isHlavni).findFirst();
            return hlAdresa.map(adresa -> new SimpleStringProperty(sestavAdresu(adresa))).orElseGet(() -> new SimpleStringProperty(""));
        } else {
            return new SimpleStringProperty("");
        }
    }

    private String sestavAdresu(Adresa a) {
        String adresa = "";
        if (a.getUlice() != null) {
            adresa += a.getUlice() + " ";
        }
        if (a.getCp() != null) {
            adresa += a.getCp();
        }
        if (a.getCor() != null) {
            adresa += ("/" + a.getCor());
        }
        if (a.getCp() != null || a.getCor() != null) {
            adresa += ", ";
        }
        if (a.getPsc() != null) {
            adresa += a.getPsc() + " ";
        }
        if (a.getMesto() != null) {
            adresa += a.getMesto();
        }
        return adresa;
    }
}
