package cz.jbenak.npos.pos.procesy.zakaznik;

import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
import javafx.concurrent.Task;

/**
 * Třída procesu pro načtení zákazníka dle jeho čísla (ID). Tuto třídu lze zejména použít pro načtení zákaznických dat po naskenování zákaznické karty, kde je kódováno jeho číslo.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @see Zakaznik
 * @since 2019-09-06
 */
public class NacteniZakaznikaDleCisla extends Task<Zakaznik> {

    private final String cisloZakaznika;

    /**
     * Konstruktor třídy procesu pro vyhledání a načtení zákazníka dle jeno čísla.
     *
     * @param cisloZakaznika číslo zákazníka z karty, nebo jiného vstupu.
     */
    public NacteniZakaznikaDleCisla(String cisloZakaznika) {
        this.cisloZakaznika = cisloZakaznika;
    }

    /**
     * Spustí proces vyhledávání a načtení zákazníka (s kompletními daty)
     *
     * @return objekt zákazníka, nebo null, pokud zákazník nebyl nalezen.
     * @see Zakaznik
     */
    @Override
    protected Zakaznik call() {

        return null;
    }
}
