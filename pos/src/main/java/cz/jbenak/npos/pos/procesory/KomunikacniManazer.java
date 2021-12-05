/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesory;

import cz.jbenak.npos.pos.objekty.doklad.TypDokladu;
import cz.jbenak.npos.pos.objekty.filialka.Smena;
import cz.jbenak.npos.pos.objekty.sezeni.Pokladni;

/**
 * Třída zabezpečující komunikaci s BO a zpracovávající příkazy.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-04
 */
public class KomunikacniManazer {

    private boolean pripojeno;
    private Pokladni pokladniTmp; //Pokud bude výsledek přihlasování/odhlašování false, pak bude nastavena tato proměnná. Pokud nebude null, proces se bude opakovat. Při úspěšném přihlášení/odhlášení se nastaví na null.

    /**
     * Přihlásí pokladní na BO. Resp. notifikuje BO, že tato pokladní je zde
     * přihlášena - z důvodu uzávěrek, atd.
     *
     * @param pokladni Pokladní k přihlášení.
     * @return výsledek oznámení.
     */
    public boolean prihlasPokladni(Pokladni pokladni) {
        pokladniTmp = pokladni;
        return true;
    }

    /**
     * Odhlásí pokladní na BO. Resp. notifikuje BO, že tato pokladní je zde
     * odhlášena - z důvodu uzávěrek, atd.
     *
     * @param pokladni Pokladní odhlášena
     * @return výsledek přihlášení
     */
    public boolean odhlasPokladni(Pokladni pokladni) {
        pokladniTmp = pokladni;
        return true;
    }

    /**
     * Start komunikace s BO.
     */
    public void nastartujKomunikaci() {

    }

    /**
     * Oznamuje stav připojení k BO.
     *
     * @return stav připojení.
     */
    public boolean isPripojeno() {
        return pripojeno;
    }

    /**
     * Vrátí poslední registrované pořadové číslo dokladu pro přiřazení nového čísla.
     *
     * @param typDokladu typ dokladu, pro který se má zjistit poslední pořadové číslo.
     * @return poslední registrované pořadové číslo.
     */
    public int getPosledniPoradoveCisloDokladu(TypDokladu typDokladu) {
        return -1;
    }

    /**
     * Otevře novou pracovní směnu na pokladně. Tato směna se následně uloží do lokální databáze.
     *
     * @return nová směna.
     */
    public Smena otevriSmenu() {
        return null;
    }
}
