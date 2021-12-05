/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesory;

import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.otazkaAnoNe.OtazkaAnoNe;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.slevy.SkupinaSlev;
import cz.jbenak.npos.pos.objekty.slevy.Sleva;
import cz.jbenak.npos.pos.objekty.slevy.Sleva.TypPuvodu;
import cz.jbenak.npos.pos.objekty.slevy.Sleva.TypSlevy;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;
import cz.jbenak.npos.pos.system.Pos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static cz.jbenak.npos.pos.objekty.slevy.Sleva.TypSlevy.*;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-11
 */
public class ManazerSlev {

    private static final Logger LOGER = LogManager.getLogger(ManazerSlev.class);

    public static void zaregistrujSlevuNaPolozku(TypSlevy typSlevy, TypPuvodu puvod, BigDecimal hodnota, Sortiment polozka) {
        LOGER.info("Bude zaregistrována {} {} sleva na položku s registračním číslem {} v hodnotě {}", puvod, typSlevy, polozka.getRegistr(), hodnota);
        /*if (overZakladniKriteriaRegistraceSlevy(typSlevy, puvod, hodnota, polozka)) {
            LOGER.info("Ověření proběhlo úspěšně. Sleva {} {} na položku s registračním číslem {} v hodnotě {} bude zaregistrována", puvod, typSlevy, polozka.getRegistr(), hodnota);
            Sleva sleva = new Sleva(typSlevy, TypUplatneni.POLOZKA, puvod, hodnota);
            polozka.getSlevy().add(sleva);
            HlavniOkno.getInstance().getKontroler().setPolozky(DokladProcesor.getInstance().getDoklad().getPolozky());
            DokladProcesor.getInstance().aktualizujBinarniKopii();
        }*/
    }

    public static void zaregistrujSlevuNaDoklad() {

    }

    public static void zaregistrujSkupinuSlev(SkupinaSlev skupina) {

    }

    public static boolean jeZadaniSlevyMozne() {
        return true;
    }

    private static boolean overZakladniKriteriaRegistraceSlevy(TypSlevy typ, TypPuvodu puvod, BigDecimal hodnota, Sortiment polozka) {
        if ((typ == PROCENTNI && hodnota.compareTo(new BigDecimal(100)) > 0) || (typ == HODNOTOVA && polozka.getJednotkovaCena().compareTo(hodnota.divide(polozka.getMnozstvi(), RoundingMode.HALF_UP)) < 0)) {
            LOGER.info("U vybraného sortimentu s registrem {} překročila výše slevy stanovenou jednotkovou cenu.", polozka.getRegistr());
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nemůžu dát slevu", "Zadaná sleva překročila cenu položky, čili by pak bylo nutno zákazníkovi naopak zaplatit za nákup.",
                    new Object[]{polozka.getRegistr(), polozka.getNazev()}, Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
            return false;
        } //TODO: stanovit maximální možnou slevu?
        LOGER.info("Provede se základní ověřování možnosti registrace slevy pro položku s registrem {}.", polozka.getRegistr());
        if (!polozka.isSlevaPovolena()) {
            LOGER.info("U vybraného sortimentu s registrem {} není povolena sleva.", polozka.getRegistr());
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nemůžu dát slevu", "U této položky {} - {} je zakázána sleva.",
                    new Object[]{polozka.getRegistr(), polozka.getNazev()}, Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
            return false;
        }
        LOGER.debug("Položka s registračním číslem {} bude zkontrolována, zdali již nějaká sleva nebyla aplikována.", polozka.getRegistr());
        if (!polozka.getSlevy().isEmpty()) {
            LOGER.debug("Ověří se možnost registrace při akci.");
            Optional<Sleva> sl = polozka.getSlevy().stream().filter(sleva -> sleva.getPuvod() == TypPuvodu.AKCNI).findFirst();
            if (sl.isPresent() && !polozka.isSlevaVAkciPovolena()) {
                LOGER.info("U vybraného sortimentu s registrem {} není povolena sleva.", polozka.getRegistr());
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nemůžu dát slevu v akci", "U této položky {} - {} nemůžu dát slevu, pokud je v akci. Ledaže by to bylo extra povoleno.",
                        new Object[]{polozka.getRegistr(), polozka.getNazev()}, Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                return false;
            }
            LOGER.debug("Ověří se možnost registrace slevy se zákazníkem.");
            Optional<Sleva> slZak = polozka.getSlevy().stream().filter(sleva -> sleva.getPuvod() == TypPuvodu.ZAKAZNICKA).findFirst();
            if (slZak.isPresent() && puvod == TypPuvodu.RUCNI && !DokladProcesor.getInstance().getDoklad().getZakaznik().isManualniSlevaPovolena()) {
                LOGER.info("U vybraného sortimentu s registrem {} není povolena sleva.", polozka.getRegistr());
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nemůžu dát slevu", "Tento zákazník má další slevy zakázané.",
                        Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                return false;
            }
            LOGER.debug("Zkontroluje se, zdali již není přiřazena nějaká další ruční sleva.");
            if (getRucniSleva(polozka) != null) {
                LOGER.info("U vybraného sortimentu s registrem {} již existuje ručně přiřazená sleva.", polozka.getRegistr());
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nemůžu dát slevu", "Tato položka už slevu má.",
                        Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                return false;
            }
        }
        return true;
    }

    public static Sleva getRucniSleva(Sortiment polozka) {
        Sleva rucni = null;
        Optional<Sleva> slRucni = polozka.getSlevy().stream().filter(sleva -> sleva.getPuvod() == TypPuvodu.RUCNI).findFirst();
        if (slRucni.isPresent()) {
            rucni = slRucni.get();
        }
        return rucni;
    }

    public static void odeberRucniSlevu(Sortiment polozka, Sleva slRucni) {
        LOGER.info("U vybraného sortimentu s registrem {} již existuje ručně přiřazená sleva. Bude nabídnuto odebrání.", polozka.getRegistr());
        OtazkaAnoNe odebrat = new OtazkaAnoNe("Odebrat slevu?", "Tato položka už slevu má. Mám jí odebrat, aby se mohlo zadat novou?", Pos.getInstance().getAplikacniOkno());
        odebrat.zobrazDialog();
        if (odebrat.getVolba() == OtazkaAnoNe.Volby.ANO) {
            polozka.getSlevy().remove(slRucni);
            HlavniOkno.getInstance().getKontroler().setPolozky(DokladProcesor.getInstance().getDoklad().getPolozky());
            DokladProcesor.getInstance().aktualizujBinarniKopii();
            LOGER.info("Aktuální ruční {} sleva {} byla s položky s registrem {} odebrána.", slRucni.getTyp(), slRucni.getHodnota(), polozka.getRegistr());
        }
        if (odebrat.getVolba() == OtazkaAnoNe.Volby.NE) {
            LOGER.info("Sleva nebude odebrána - volba byla zamítnuta.");
        }
    }

    public static void zaregistrujSlevuNaDoklad(TypSlevy typSlevy, TypPuvodu puvod, BigDecimal hodnota) {
        LOGER.info("Bude zaregistrována {} {} sleva na aktuální doklad v hodnotě {}", puvod, typSlevy, hodnota);
    }

    /**
     * Sečte celkovou hodnotu všech tří slev. Procesy registrace slev zajistí, že bude aplikována pouze správná sleva - pokud se rozoduje ze slev, aplikuje se nejvyšší a další se aplikují dle povolení.
     *
     * @param polozka položka, jejíž slevy mají být spočteny.
     * @return celková hodnota slev aplikovaná na položku.
     */
    public static BigDecimal getHodnotuSlev(Sortiment polozka) {
        BigDecimal hodnota = BigDecimal.ZERO;
        for (Sleva s : polozka.getSlevy()) {
            hodnota = hodnota.add(spoctiHodnotuSlevy(polozka, s));
        }
        return hodnota;
    }

    public static BigDecimal getHodnotuSlev() {
        BigDecimal hodnota = BigDecimal.ZERO;
        if (DokladProcesor.getInstance().jeOtevrenDoklad()) {
            for (Sortiment s : DokladProcesor.getInstance().getDoklad().getPolozky()) {
                hodnota = hodnota.add(getHodnotuSlev(s));
            }
        }
        return hodnota;
    }

    public static BigDecimal spoctiHodnotuSlevy(Sortiment polozka, Sleva sleva) {
        BigDecimal hodnota = BigDecimal.ZERO;
        if (sleva.getTyp() == HODNOTOVA || sleva.getTyp() == AKCE) {
            hodnota = sleva.getHodnota().multiply(polozka.getMnozstvi());
        }
        if (sleva.getTyp() == PROCENTNI) {
            BigDecimal koeficient = sleva.getHodnota().divide(new BigDecimal(100));
            hodnota = polozka.getJednotkovaCena().multiply(polozka.getMnozstvi()).multiply(koeficient);
        }
        return hodnota;
    }

    public static void zkontrolujSlevyNaPolozku() {
        LOGER.info("Proběhne kontrola dokladu na slevy položkách. Nyní se ověří přítomnost zákazníka a jeho slev.");
        /*
        TODO: 1. vyzvednout seznam aktuálně platných skupin slev (zákazník, akce, doklad,...)
        TODO: 2. zkontrolovat aplikovatelnost skupin v dokladu
        TODO: 3. vyloučit případné ostatní slevy a aplikovat slevy dle skupiny
         */
        zpracujZakaznickePolozkoveSlevy();
    }

    public static void odeberZakaznickeSlevy() {
        LOGER.debug("Zákazník není přítomen v dokladu, nebo byl odebrán. Případné zákaznické slevy se odeberou.");
        DokladProcesor.getInstance().getDoklad().getPolozky().forEach(pol -> {
            Sleva nalezena = null;
            for (Sleva sl : pol.getSlevy()) {
                if (sl.getPuvod() == TypPuvodu.ZAKAZNICKA) {
                    nalezena = sl;
                    break;
                }
            }
            if (nalezena != null) {
                pol.getSlevy().remove(nalezena);
            }
        });
        Sleva dokladova = null;
        for (Sleva s : DokladProcesor.getInstance().getDoklad().getSlevy()) {
            if (s.getPuvod() == TypPuvodu.ZAKAZNICKA) {
                dokladova = s;
                break;
            }
        }
        if (dokladova != null) {
            DokladProcesor.getInstance().getDoklad().getSlevy().remove(dokladova);
        }
        HlavniOkno.getInstance().getKontroler().setPolozky(DokladProcesor.getInstance().getDoklad().getPolozky());
        DokladProcesor.getInstance().aktualizujBinarniKopii();
    }

    private static void zpracujZakaznickePolozkoveSlevy() {
        if (DokladProcesor.getInstance().getDoklad().getZakaznik() != null) {
            LOGER.debug("Zákazník je přítomen v dokladu. Budou aplikovány jeho slevy.");
            SkupinaSlev skupina = DokladProcesor.getInstance().getDoklad().getZakaznik().getSkupinaSlev();
            if (skupina == null || (skupina.getOkamzikUplatneni() != SkupinaSlev.OkamzikUplatneni.REGISTRACE)) {
                LOGER.info("Zákazník nemá slevy, co byl šly uplatnit v registraci.");
            } else {
                Sleva sleva = new Sleva(skupina.getCislo(), skupina.getTypSlevy(), Sleva.TypUplatneni.POLOZKA, TypPuvodu.ZAKAZNICKA, skupina.getHodnotaSlevy());
                sleva.setPopis(skupina.getNazev());
                if (skupina.getTyp() == SkupinaSlev.TypSkupinySlev.NA_REGISTR) {
                    DokladProcesor.getInstance().getDoklad().getPolozky().stream().filter(pol -> pol.getRegistr().equalsIgnoreCase(skupina.getIdCileSlevy())).forEach(pol -> {
                        if (pol.isSlevaPovolena() && !slevaNaPolozceJizExistuje(pol, sleva)) {
                            pol.getSlevy().add(sleva);
                        }
                    });
                }
                if (skupina.getTyp() == SkupinaSlev.TypSkupinySlev.NA_SKUPINU) {
                    DokladProcesor.getInstance().getDoklad().getPolozky().stream().filter(pol -> pol.getSkupina().getId().equalsIgnoreCase(skupina.getIdCileSlevy())).forEach(pol -> {
                        if (pol.isSlevaPovolena() && pol.getSkupina().isSlevaPovolena() && !slevaNaPolozceJizExistuje(pol, sleva)) {

                        }
                    });
                }
            }
            HlavniOkno.getInstance().getKontroler().setPolozky(DokladProcesor.getInstance().getDoklad().getPolozky());
            DokladProcesor.getInstance().aktualizujBinarniKopii();
            LOGER.info("Dostupné zákaznické slevy byly aplikovány.");
        }
    }

    private static boolean slevaNaPolozceJizExistuje(Sortiment polozka, Sleva sleva) {
        boolean existuje = false;
        for (Sleva s : polozka.getSlevy()) {
            existuje = s.getCisloSkupinySlev() == sleva.getCisloSkupinySlev();
            break;
        }
        return existuje;
    }

    private static void registrujAutomatickySlevuNaPolozku(Sortiment polozka, Sleva sleva) {

    }

    private static Sleva sestavSlevuDleSkupiny(SkupinaSlev skupina) {
        return null;
    }
}
