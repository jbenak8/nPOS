/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.sdilene.ciselnaKlavesnice;

/**
 * Rozhraní pro předání dat ze společné číselné klávesnice.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-01
 */
public interface ICiselnaKlavesnice {

    /**
     * Předá z kontroleru číslené klávesnice znak reprezentující číslo 0-9,
     * popř. 00 a desetinnou čárku.
     *
     * @param znak znak ze stisklé klávesy.
     */
    public void predejZnak(String znak);

    /**
     * Funkce klávesy Backspace - umaže jeden zadaný znak z prava.
     */
    public void umazZnak();

    /**
     * Smaže všechna zadaná data.
     */
    public void smazVstup();

    /**
     * Potvrdí zadaná data. Funkce klávesy Enter.
     */
    public void potvrdVstup();
}
