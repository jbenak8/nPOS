package cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice;

import cz.jbenak.npos.pos.gui.Pomocnici;
import javafx.scene.control.TextInputControl;

/**
 * Třída pro definici polí,které jsou předávány v listu textové klávesnici a definuje jeho typ, maximální délku a případné omezení vstupu pomocí regulárního výrazu
 *
 * @see Pomocnici
 */
public class PoleKVyplneni {

    private final TextInputControl pole;
    private final Pomocnici.TypyVstupnichHodnot typ;
    private final int maxDelka;
    private String regexOmezeni;
    private final String popisOmezeni;

    /**
     * Výchozí konstruktor. Předá se pouze pole a typ jeho vstupní hodnoty. Maximální délka vstupu je neomezená a vstup není ničím omezen.
     *
     * @param pole pole, co se má vyplnit.
     * @param typ  Typ vstupní hodnoty.
     * @see Pomocnici.TypyVstupnichHodnot
     */
    public PoleKVyplneni(TextInputControl pole, Pomocnici.TypyVstupnichHodnot typ) {
        this.pole = pole;
        this.typ = typ;
        this.maxDelka = -1;
        this.popisOmezeni = "";
    }

    /**
     * Konstruktor pro stanovení maximální délky vstupu.
     *
     * @param pole     pole, co se má vyplnit.
     * @param typ      Typ vstupní hodnoty.
     * @param maxDelka maximální délka vstupu, co se má vyplnit.
     * @see Pomocnici.TypyVstupnichHodnot
     */
    public PoleKVyplneni(TextInputControl pole, Pomocnici.TypyVstupnichHodnot typ, int maxDelka) {
        this.pole = pole;
        this.typ = typ;
        this.maxDelka = maxDelka;
        this.popisOmezeni = "";
    }

    /**
     * Konstuktor, který stanoví jak objekt pole, max. délku vstupu, ale i případné omezení regulárním výrazem.
     *
     * @param pole         pole, co se má vyplnit.
     * @param typ          Typ vstupní hodnoty.
     * @param maxDelka     maximální dékla vstupu, co se má vyplnit.
     * @param regexOmezeni omezení vstupních hodnot pomocí regulárního výrazu.
     * @param popisOmezeni popis omezení vstupních hodnot pomocí regulárního výrazu.
     * @see Pomocnici.TypyVstupnichHodnot
     */
    public PoleKVyplneni(TextInputControl pole, Pomocnici.TypyVstupnichHodnot typ, int maxDelka, String regexOmezeni, String popisOmezeni) {
        this.pole = pole;
        this.maxDelka = maxDelka;
        this.typ = typ;
        this.regexOmezeni = regexOmezeni;
        this.popisOmezeni = popisOmezeni;
    }

    /**
     * Získá objekt pole, co se má vyplnit.
     *
     * @return objekt pole k vyplnění.
     */
    public TextInputControl getPole() {
        return pole;
    }

    /**
     * Získá maximální délku pole.
     *
     * @return hodnota maximální délky vstupní hodnoty, nebo -1 pro neomezeno.
     */
    public int getMaxDelka() {
        return maxDelka;
    }

    /**
     * Získá regulární výraz omezující vstupní hodnotu.
     *
     * @return regulární výraz omezení, nebo null, pokud není nutné omezení na nějakou hodnotu.
     */
    public String getRegexOmezeni() {
        return regexOmezeni;
    }

    /**
     * Získá typ hodnoty vstupního pole
     *
     * @return typ zadávané hondoty
     * @see Pomocnici.TypyVstupnichHodnot
     */
    public Pomocnici.TypyVstupnichHodnot getTyp() {
        return typ;
    }

    /**
     * Získá text pro upozornění při omezení dat ve vstupním poli.
     *
     * @return text popisující omezení dat vstupního pole.
     */
    public String getPopisOmezeni() {
        return popisOmezeni;
    }
}
