package cz.jbenak.npos.pos.gui.registrace.skupinySlev.modely;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.objekty.slevy.SkupinaSlev;
import cz.jbenak.npos.pos.objekty.slevy.Sleva;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;

/**
 * Definice zobrazení hodnot v tabulce se seznamem slev.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-01-22
 */

public class PolozkaSeznamuSlev {

    private final SkupinaSlev skupina;

    public PolozkaSeznamuSlev(SkupinaSlev skupina) {
        this.skupina = skupina;
    }

    public SkupinaSlev getSkupina() {
        return skupina;
    }

    public IntegerProperty getCislo() {
        return new SimpleIntegerProperty(skupina.getCislo());
    }

    public StringProperty getNazev() {
        return new SimpleStringProperty(skupina.getNazev());
    }

    public StringProperty getHodnota() {
        if (skupina.getHodnotaSlevy().compareTo(new BigDecimal(-1)) == 0) {
            return new SimpleStringProperty("volná" + (skupina.getTypSlevy() == Sleva.TypSlevy.PROCENTNI ? " %" : ""));
        }
        if (skupina.getTypSlevy() == Sleva.TypSlevy.PROCENTNI) {
            return new SimpleStringProperty(Pomocnici.formatujNaDveMista(skupina.getHodnotaSlevy()) + " %");
        }
        if (skupina.getTypSlevy() == Sleva.TypSlevy.HODNOTOVA) {
            return new SimpleStringProperty(Pomocnici.formatujNaDveMista(skupina.getHodnotaSlevy()));
        }
        return null;
    }

    public StringProperty getTypCile() {
        return switch (skupina.getTyp()) {
            case NA_SKUPINU -> new SimpleStringProperty("S");
            case NA_REGISTR -> new SimpleStringProperty("R");
            case NA_DOKLAD -> new SimpleStringProperty("D");
            default -> new SimpleStringProperty("");
        };
    }

    public StringProperty getRozsah() {
        return switch (skupina.getRozsahPouziti()) {
            case VZDY -> new SimpleStringProperty("A");
            case JEN_SE_ZAKAZNIKEM -> new SimpleStringProperty("Z");
            default -> new SimpleStringProperty("");
        };
    }
}
