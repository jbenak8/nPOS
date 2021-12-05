/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesory;

import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Třída pro zajištění autorizace funkcí pokladních dle přiřazených práv.
 * @author Jan Benák
 * @since 2021-01-18
 */
public class AutorizacniManazer {
    
    public enum Funkce {
        ODHLASENI, KALKULACKA, SKOLICI_REZIM, ZAMEK, MNOZSTVI, ZAHOZENI_DOKLADU, OPAKOVANI_POLOZKY,
        STORNO_POLOZKY, REZIM_VRATKY, PARKOVANI, SLEVA, SLEVA_UPRAVA, SLEVA_SMAZANI, ZMENA_CENY, REGISTRACE_ZAKAZNIKA,
        ULOZENI_NA_SERVER, INFO_O_SORTIMENTU, INFO_O_ZAKAZNIKOVI, ZALOZENI_ZAKAZNIKA, ZRUSENI_REGISTRACE_ZAKAZNIKA
    }
    
    private final static Logger LOGER = LogManager.getLogger(AutorizacniManazer.class);
    /**
     * Provede autorizaci funkce. Hlášení n a případné zamítnutí se provede zde.
     * @param funkce Identifikátor autorizované funkce.
     * @return výsledek autorizace
     */
    public boolean autorizujFunkci(Funkce funkce) {
        LOGER.info("Funkce {} bude nyní autorizována pro pokladní {}.", funkce.name(), HlavniOkno.getInstance().getPrihlasenaPokladni().getId());
        return true;
    }
}
