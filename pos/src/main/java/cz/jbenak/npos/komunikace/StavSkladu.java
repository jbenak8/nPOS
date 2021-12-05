/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.komunikace;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Třída sloužící pro dotazování se BO na stav skladu u jednotlivého sortimentu.
 * Zároveň v sobě tato třída nese i odpověď.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-03-03
 */
public class StavSkladu implements Serializable {

    private static final long serialVersionUID = -3972008480515029922L;
    private final String registr;
    private BigDecimal stavSkladu;
    private Date posledniPrijem;
    private BigDecimal posledniNakupniCena;

    /**
     * Konstruktor transportního objektu. Nutno zadat registr - obsahuje
     * skladová data pro určitou položku.
     *
     * @param registr registr položky sortimentu.
     */
    public StavSkladu(String registr) {
        this.registr = registr;
    }

    /**
     * Získá registr položky sortimentu.
     *
     * @return Registr položky.
     */
    public String getRegistr() {
        return registr;
    }

    /**
     * Obsahuje stav skladu.
     *
     * @return aktuální stav skladu pro položku na BO.
     */
    public BigDecimal getStavSkladu() {
        return stavSkladu;
    }

    /**
     * Na BO nastaví akutální stav skladu.
     *
     * @param stavSkladu aktuální stav skladu pro danou položku.
     */
    public void setStavSkladu(BigDecimal stavSkladu) {
        this.stavSkladu = stavSkladu;
    }

    /**
     * Vrátí datum posledního příjmu na sklad. Dodávky, NE vratka.
     *
     * @return datum posledního příjmu (dodávky) položky na sklad.
     */
    public Date getPosledniPrijem() {
        return posledniPrijem;
    }

    /**
     * Nastaví datum posledního příjmu na sklad formou dodávky od dodavatele.
     *
     * @param posledniPrijem Datum poslendího příjmu dané položky sortimentu na
     * sklad.
     */
    public void setPosledniPrijem(Date posledniPrijem) {
        this.posledniPrijem = posledniPrijem;
    }

    /**
     * Získá poslední nákupní cenu
     *
     * @return poslední nákupní cena dané položky.
     */
    public BigDecimal getPosledniNakupniCena() {
        return posledniNakupniCena;
    }

    /**
     * Nastaví poslední nákupní cenu.
     *
     * @param posledniNakupniCena Poslední nákupní cena dané položky.
     */
    public void setPosledniNakupniCena(BigDecimal posledniNakupniCena) {
        this.posledniNakupniCena = posledniNakupniCena;
    }
}
