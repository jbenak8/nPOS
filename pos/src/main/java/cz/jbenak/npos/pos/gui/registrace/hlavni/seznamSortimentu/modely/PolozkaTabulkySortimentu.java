/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni.seznamSortimentu.modely;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.objekty.sortiment.SeznamRegistrace;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Třída reprezentující model pro zobrazení položky seznamu sortimentu v
 * tabulce.
 *
 * @author JanBenák
 * @version 1.0.0.0
 * @since 2018-02-14
 */
public class PolozkaTabulkySortimentu {

    private final SeznamRegistrace polozka;

    public PolozkaTabulkySortimentu(SeznamRegistrace polozka) {
        this.polozka = polozka;
    }

    public SeznamRegistrace getPolozka() {
        return polozka;
    }

    public StringProperty getRegistr() {
        return new SimpleStringProperty(polozka.getRegistr());
    }

    public StringProperty getNazev() {
        return new SimpleStringProperty(polozka.getNazev());
    }

    public StringProperty getSkupina() {
        return new SimpleStringProperty(polozka.getIdSkupiny() + " " + polozka.getNazevSkupiny());
    }

    public StringProperty getCena() {
        return new SimpleStringProperty(Pomocnici.formatujNaDveMista(polozka.getCena()));
    }
}
