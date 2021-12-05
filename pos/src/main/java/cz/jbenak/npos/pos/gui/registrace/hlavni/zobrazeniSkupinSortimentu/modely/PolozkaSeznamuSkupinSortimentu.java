package cz.jbenak.npos.pos.gui.registrace.hlavni.zobrazeniSkupinSortimentu.modely;

import cz.jbenak.npos.pos.objekty.sortiment.SkupinaSortimentu;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Reprezentace řádku se skupinou sortimentu v tabulce seznamu.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-09-16
 */
public class PolozkaSeznamuSkupinSortimentu {

    private final SkupinaSortimentu skupina;

    /**
     * Konstruktor objektu
     *
     * @param skupina zobrazovaná skupina sortimentu
     */
    public PolozkaSeznamuSkupinSortimentu(SkupinaSortimentu skupina) {
        this.skupina = skupina;
    }

    /**
     * Vrátí daný objekt zobrazované skupiny.
     *
     * @return předaný objekt, co se zobrazuje v řádku.
     */
    public SkupinaSortimentu getSkupina() {
        return skupina;
    }

    /**
     * Zobrazí ID skupiny
     *
     * @return hodnota buňky s ID skupiny.
     */
    public StringProperty getID() {
        return new SimpleStringProperty(skupina.getId());
    }

    /**
     * Zobrazí ID nadřazené skupiny.
     *
     * @return hodnota buňky s ID nadřazené skupiny. Může být i prázdné, pokud není nastaveno.
     */
    public StringProperty getIdNadrazeneSkupiny() {
        return new SimpleStringProperty(skupina.getIdNadrazeneSkupiny() == null ? "" : skupina.getIdNadrazeneSkupiny());
    }

    /**
     * Vrátí název skupiny.
     *
     * @return Hodnota buňky s názvem skupiny.
     */
    public StringProperty getNazev() {
        return new SimpleStringProperty(skupina.getNazev());
    }
}
