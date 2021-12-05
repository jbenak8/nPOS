/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesory;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.platba.platebniOkno.Platba;
import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.otazkaAnoNe.OtazkaAnoNe;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.progres.ProgresDialog;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.doklad.Doklad;
import cz.jbenak.npos.pos.objekty.doklad.RekapitulaceDPH;
import cz.jbenak.npos.pos.objekty.doklad.TypDokladu;
import cz.jbenak.npos.pos.objekty.meny.Mena;
import cz.jbenak.npos.pos.objekty.meny.ObrazekNaTlacitku;
import cz.jbenak.npos.pos.objekty.meny.PravidloZaokrouhlovani;
import cz.jbenak.npos.pos.objekty.platba.PlatebniProstredek;
import cz.jbenak.npos.pos.objekty.platba.PolozkaPlatby;
import cz.jbenak.npos.pos.procesy.doklad.NastaveniCislaDokladu;
import cz.jbenak.npos.pos.procesy.platba.*;
import cz.jbenak.npos.pos.system.Pos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * Třída pro přípravu a vykonání placení.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-09-24
 */
public class PlatebniProcesor {

    private static final Logger LOGER = LogManager.getLogger(PlatebniProcesor.class);
    private static volatile PlatebniProcesor instance;
    private final ProgresDialog progres;
    private Platba platebniOkno;
    private List<PlatebniProstredek> platebniProstredky;
    private List<Mena> meny;
    private List<ObrazekNaTlacitku> obrazkyNaTlacitkach;
    private PlatebniProstredek vybranyPlatebniProstredek;
    private Mena vybranaMena;

    private PlatebniProcesor() {
        progres = new ProgresDialog(Pos.getInstance().getAplikacniOkno());
    }

    /**
     * Metoda poskytující přístup k funkcím platebního procesoru.
     *
     * @return jedinečná instance platebního procesoru.
     */
    public static PlatebniProcesor getInstance() {
        PlatebniProcesor procesor = PlatebniProcesor.instance;
        if (procesor == null) {
            synchronized (PlatebniProcesor.class) {
                procesor = PlatebniProcesor.instance;
                if (procesor == null) {
                    PlatebniProcesor.instance = procesor = new PlatebniProcesor();
                }
            }
        }
        return procesor;
    }

    public List<Mena> getMeny() {
        return meny;
    }

    public List<ObrazekNaTlacitku> getObrazkyNaTlacitkach() {
        return obrazkyNaTlacitkach;
    }

    public List<PlatebniProstredek> getPlatebniProstredky() {
        return platebniProstredky;
    }

    public PlatebniProstredek getVybranyPlatebniProstredek() {
        return vybranyPlatebniProstredek;
    }

    public void setVybranyPlatebniProstredek(PlatebniProstredek vybranyPlatebniProstredek) {
        this.vybranyPlatebniProstredek = vybranyPlatebniProstredek;
    }

    public Mena getVybranaMena() {
        return vybranaMena;
    }

    public void setVybranaMena(Mena vybranaMena) {
        this.vybranaMena = vybranaMena;
    }

    public BigDecimal getCelkemKZaplaceni() {
        BigDecimal celkem = DokladProcesor.getInstance().getDoklad().getCenaDokladuCelkem();
        return celkem.compareTo(BigDecimal.ZERO) < 0 ? celkem.add(DokladProcesor.getInstance().getDoklad().getCelkemZaplaceno())
                : celkem.subtract(DokladProcesor.getInstance().getDoklad().getCelkemZaplaceno());
    }

    public BigDecimal getCelkemVKurzuVybraneMeny(BigDecimal kDoplaceni) {
        if (!vybranaMena.getKurzy().isEmpty()) {
            kDoplaceni = kDoplaceni.divide(vybranaMena.getKurzy().get(0).getNakup(), new MathContext(5));
        }
        return kDoplaceni;
    }

    public BigDecimal getKZaplaceniVeVybraneMene() {
        BigDecimal celkem = getCelkemKZaplaceni();
        if (!vybranaMena.isKmenova()) {
            celkem = getCelkemVKurzuVybraneMeny(celkem);
        }
        if (jeVratka()) {
            celkem = celkem.multiply(new BigDecimal("-1"), new MathContext(5));
        }
        celkem = zaokrouhliVeVybraneMene(vybranaMena, celkem);
        return celkem;
    }

    public void zahajPlatbuDokladu() {
        LOGER.info("Byla zahájena příprava k platbě dokladu.");
        if (zkontrolujMaximumProParagon()) {
            if (DokladProcesor.getInstance().getDoklad().getCenaDokladuCelkem().compareTo(BigDecimal.ZERO) == 0) {
                zaplatDoklad();
            } else {
                pripravPlatbuDokladu();
            }
        } else {
            LOGER.info("Nastaly podmínky pro vydání úplného daňového dokladu, ale nebyly splněny");
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nemůžu zaplatit", "Doklad přesáhl částku {}, do které lze vystavit běžný paragon," +
                    " tedy je nutné zadat zákazníka. Jenže ten nebyl zadán, proto nemůžu pokračovat k placení.", new Object[]{Pomocnici.formatujNaDveMista(new BigDecimal(Pos.getInstance().getNastaveni().getProperty("platba.limitUplnyDanovyDoklad")))},
                    Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        }
    }

    /**
     * Zjistí rozdíl mezi zaplacenou částkou a hodnotou dokladu v kmenové měně.
     *
     * @return Částka k vrácení. Zaokrouhlená dle denominace měny pouze pokud platy obsahují hotovost.
     */
    public BigDecimal getKVraceni() {
        BigDecimal celkemDoklad = DokladProcesor.getInstance().getDoklad().getCenaDokladuCelkem();
        BigDecimal celkemZaplaceno = DokladProcesor.getInstance().getDoklad().getCelkemZaplaceno();
        for (PolozkaPlatby pol : DokladProcesor.getInstance().getDoklad().getPlatby()) {
            if (pol.getPlatebniProstredek().getTyp() == PlatebniProstredek.TypPlatebnihoProstredku.KARTA) {
                return celkemDoklad.subtract(celkemZaplaceno).abs();
            }
        }
        BigDecimal celkemZaokrouhleno = zaokrouhliVeVybraneMene(Pos.getInstance().getHlavniMena(), celkemDoklad);
        return celkemZaokrouhleno.subtract(celkemZaplaceno).abs();
    }

    /**
     * Zaokrouhlí částku dle denominace vybrané měny. Důležité pro platby v hotovosti.
     *
     * @param mena   vybraná měna.
     * @param celkem částka k zaokrouhlení.
     * @return zaokrouhlená částka.
     */
    public BigDecimal zaokrouhliVeVybraneMene(Mena mena, BigDecimal celkem) {
        for (PravidloZaokrouhlovani pz : mena.getPravidlaZaokrouhlovani()) {
            if (celkem.remainder(BigDecimal.ONE).compareTo(pz.getHodnotaOd()) > -1 && celkem.remainder(BigDecimal.ONE).compareTo(pz.getHodnotaDo()) < 1) {
                if (pz.getHodnotaZaokrouhleni().compareTo(BigDecimal.ONE) == 0) {
                    celkem = celkem.setScale(0, pz.getSmer() == PravidloZaokrouhlovani.SmerZaokrouhleni.NAHORU ? RoundingMode.UP : RoundingMode.DOWN);
                }
                if (pz.getHodnotaZaokrouhleni().compareTo(new BigDecimal("0.1")) == 0) {
                    celkem = celkem.setScale(1, pz.getSmer() == PravidloZaokrouhlovani.SmerZaokrouhleni.NAHORU ? RoundingMode.UP : RoundingMode.DOWN);
                }
                if (pz.getHodnotaZaokrouhleni().compareTo(new BigDecimal("0.01")) == 0) {
                    celkem = celkem.setScale(2, pz.getSmer() == PravidloZaokrouhlovani.SmerZaokrouhleni.NAHORU ? RoundingMode.UP : RoundingMode.DOWN);
                }
                if (pz.getHodnotaZaokrouhleni().compareTo(new BigDecimal("0.05")) == 0) { // CHF - speciální případ
                    BigDecimal rozdil = celkem.remainder(new BigDecimal("0.1"));
                    if (rozdil.compareTo(new BigDecimal("0.01")) == 0 || rozdil.compareTo(new BigDecimal("0.02")) == 0) {
                        celkem = celkem.subtract(rozdil);
                    }
                    if (rozdil.compareTo(new BigDecimal("0.03")) == 0 || rozdil.compareTo(new BigDecimal("0.04")) == 0) {
                        celkem = celkem.add((new BigDecimal("0.05")).subtract(rozdil));
                    }
                    if (rozdil.compareTo(new BigDecimal("0.06")) == 0 || rozdil.compareTo(new BigDecimal("0.07")) == 0) {
                        celkem = celkem.subtract(rozdil.subtract((new BigDecimal("0.05"))));
                    }
                    if (rozdil.compareTo(new BigDecimal("0.07")) == 0 || rozdil.compareTo(new BigDecimal("0.09")) == 0) {
                        celkem = celkem.add((new BigDecimal("0.1")).subtract(rozdil));
                    }
                }
                break;
            }
        }
        return celkem;
    }

    private boolean zkontrolujMaximumProParagon() {
        LOGER.info("Doklad se zkontroluje na maximální možnou částku pro paragon z hlediska Zákona o DPH");
        BigDecimal maxParagon = new BigDecimal(Pos.getInstance().getNastaveni().getProperty("platba.limitUplnyDanovyDoklad", "-1"));
        if (maxParagon.compareTo(new BigDecimal("-1")) == 0) {
            LOGER.info("Maximální výše pro paragon není nastavena.");
            return true;
        }
        Doklad doklad = DokladProcesor.getInstance().getDoklad();
        if (doklad.getCenaDokladuCelkem().compareTo(maxParagon) < 0) {
            LOGER.info("Maximální hodnota dokladu {} nepřesáhla nastavený limit pro fakturu " +
                            "{} z hlediska Zákona o DPH a doklad může být vystaven jako paragon.",
                    doklad.getCenaDokladuCelkem().toPlainString(), maxParagon.toPlainString());
            return true;
        } else if (doklad.getZakaznik() != null) {
            LOGER.info("Maximální hodnota dokladu {} již přesáhla nastavený limit pro fakturu " +
                            "{} z hlediska Zákona o DPH. Bude vydán úplný daňový doklad. Zákazník je přítomen na dokladu.",
                    doklad.getCenaDokladuCelkem().toPlainString(), maxParagon.toPlainString());
            return true;
        } else {
            LOGER.info("Maximální hodnota dokladu {} již přesáhla nastavený limit pro fakturu " +
                            "{} z hlediska Zákona o DPH. Bude vydán úplný daňový doklad. Zákazník není přítomen na dokladu a bude požadováno zadání.",
                    doklad.getCenaDokladuCelkem().toPlainString(), maxParagon.toPlainString());
            //TODO: dotaz 3 volby -> registrace, vyhledání, odmítnutí
        }
        return false;
    }

    private void sestavRekapitulaciDPH(Doklad doklad) {
        LOGER.info("Provede se sestavení rekapitulační doložky DPH pro doklad s ID {}.", doklad);
        doklad.getTabulkaDPH().clear();
        doklad.getPolozky().forEach(pol -> {
            RekapitulaceDPH rekapitulace = new RekapitulaceDPH();
            rekapitulace.setDph(pol.getDph());
            rekapitulace.setCelkem(pol.getCenaCelkem());
            rekapitulace.setZaklad(pol.getCelkovyZakladDPH());
            rekapitulace.setDan(pol.getCenaCelkem().subtract(pol.getCelkovyZakladDPH()));
            if (doklad.getTabulkaDPH().isEmpty() || !obsahujeDPH(doklad, rekapitulace)) {
                doklad.getTabulkaDPH().add(rekapitulace);
            } else {
                doklad.getTabulkaDPH().forEach(dph -> {
                    if (dph.compareTo(rekapitulace) == 0) {
                        dph.setZaklad(pol.isVracena() ? dph.getZaklad().subtract(rekapitulace.getZaklad()) : dph.getZaklad().add(rekapitulace.getZaklad()));
                        dph.setCelkem(pol.isVracena() ? dph.getCelkem().subtract(rekapitulace.getCelkem()) : dph.getCelkem().add(rekapitulace.getCelkem()));
                        dph.setDan(pol.isVracena() ? dph.getDan().subtract(rekapitulace.getDan()) : dph.getDan().add(rekapitulace.getDan()));
                    }
                });
            }
        });
    }

    private boolean obsahujeDPH(Doklad doklad, RekapitulaceDPH dph) {
        for (RekapitulaceDPH pol : doklad.getTabulkaDPH()) {
            if (pol.getDph().compareTo(dph.getDph()) == 0) {
                return true;
            }
        }
        return false;
    }

    private void pripravPlatbuDokladu() {
        Doklad doklad = DokladProcesor.getInstance().getDoklad();
        LOGER.info("Bude zahájena příprava platby dokladu s ID {}.", doklad.getId());
        sestavRekapitulaciDPH(doklad);
        doklad.setSmena(HlavniOkno.getInstance().getAktualniSmena());
        progres.zobrazProgres("Načítám dostupné možné prostředky.");
        NacteniPlatebnichProstredku npp = new NacteniPlatebnichProstredku(true);
        NacteniMen nm = new NacteniMen(true);
        try {
            npp.run();
            platebniProstredky = npp.get();
            if (platebniProstredky.isEmpty()) {
                LOGER.warn("Nebyly načteny žádné platné platební prostředky pro úhradu dokladu ID {}!", doklad.getId());
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nemám čím zaplatit",
                        "Nemohl jsem načíst z databáze žádné platné platební prostředky pro tento doklad.",
                        Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            } else {
                progres.setText("Načítám akceptovatelné měny.");
                nm.run();
                meny = nm.get();
                progres.setText("Načítám výčtovou tabulku k vybraným měnám.");
                NacteniDenominaci nd = new NacteniDenominaci(meny);
                nd.run();
                NacteniPravidelZaokrouhlovani npz = new NacteniPravidelZaokrouhlovani(meny);
                progres.setText("Načítám pravidla zaokrouhlování k vybraným měnám.");
                npz.run();
                if (meny.size() > 1) {
                    NacteniKurzuMen nkm = new NacteniKurzuMen(meny, true);
                    progres.setText("Načítám převodní kurzy k vybraným měnám.");
                    nkm.run();
                }
                progres.setText("Načítám obrázky na tlačítka hotovosti.");
                NacteniObrazkuNaTlacitka no = new NacteniObrazkuNaTlacitka(meny);
                no.run();
                obrazkyNaTlacitkach = no.get();
                LOGER.info("Data pro platbu dokladu byla úspěšně načtena.");
                vybranyPlatebniProstredek = PlatebniProcesor.getInstance().getPlatebniProstredky().get(0);
                for (Mena m : PlatebniProcesor.getInstance().getMeny()) {
                    if (m.isAkceptovatelna() && m.isKmenova()) {
                        vybranaMena = m;
                        break;
                    }
                }
                platebniOkno = new Platba();
                platebniOkno.zobrazDialog();
                platebniOkno.getKontroler().setVratka(jeVratka());
                DokladProcesor.getInstance().getDoklad().setVratka(jeVratka());
                if (!DokladProcesor.getInstance().getDoklad().getPlatby().isEmpty()) {
                    platebniOkno.getKontroler().setPolozky();
                }
            }
        } catch (Exception e) {
            LOGER.error("Došlo k problému při přípravě úhrady dokladu ID {}!", doklad.getId(), e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Nemohu připravit platbu",
                    "Nastala chyba v procesu přípravy platby tohoto dokladu:\n\n" + e.getLocalizedMessage(),
                    Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        } finally {
            if (progres.isShowing()) {
                progres.zavriDialog();
            }
        }
    }

    public boolean lzePlatebniProstredekZaregistrovat() {

        return true;
    }

    public boolean jeVratka() {
        return (DokladProcesor.getInstance().getDoklad().isVratka() || getCelkemKZaplaceni().compareTo(BigDecimal.ZERO) < 0);
    }

    public void zaregistrujPolozkuPlatby(BigDecimal hodnota) { // TODO: píčuje při doplacení kartou - nechce spustit uzavření
        LOGER.info("Bude zaregistrována položka platby typu {}, zadaná hodnota {}, měna {}.", vybranyPlatebniProstredek, hodnota.toPlainString(), vybranaMena.getIsoKod());
        PolozkaPlatby polozka = new PolozkaPlatby();
        polozka.setCastka(hodnota);
        polozka.setCislo(DokladProcesor.getInstance().getDoklad().getPlatby().size() + 1);
        polozka.setMena(vybranaMena);
        polozka.setPlatebniProstredek(vybranyPlatebniProstredek);
        if (!vybranaMena.isKmenova() && !vybranaMena.getKurzy().isEmpty()) {
            polozka.setKurzMeny(vybranaMena.getKurzy().get(0));
            polozka.setCastkaVKmenoveMene(hodnota.multiply(vybranaMena.getKurzy().get(0).getNakup(), new MathContext(5)));
        } else {
            polozka.setCastkaVKmenoveMene(hodnota);
        }
        DokladProcesor.getInstance().getDoklad().getPlatby().add(polozka);
        DokladProcesor.getInstance().aktualizujBinarniKopii();
        platebniOkno.getKontroler().setPolozky();
        platebniOkno.getKontroler().setVratka(jeVratka());
        BigDecimal celkem = jePosledniPlatbaHotovost() ? zaokrouhliVeVybraneMene(vybranaMena, DokladProcesor.getInstance().getDoklad().getCenaDokladuCelkem().abs())
                : DokladProcesor.getInstance().getDoklad().getCenaDokladuCelkem().abs();
        BigDecimal zaplaceno = DokladProcesor.getInstance().getDoklad().getCelkemZaplaceno().abs();
        if ((celkem.subtract(zaplaceno)).compareTo(BigDecimal.ZERO) <= 0) {
            zaplatDoklad();
        }
    }

    /**
     * Zjistí, jestli poslední položkou platby je hotovost kvůli zaokrouhlení celkové částky.
     *
     * @return je hotovost poslední platbou?
     */
    public boolean jePosledniPlatbaHotovost() {
        for (int i = DokladProcesor.getInstance().getDoklad().getPlatby().size() - 1; i >= 0; i--) {
            if (DokladProcesor.getInstance().getDoklad().getPlatby().get(i).getPlatebniProstredek().getTyp() == PlatebniProstredek.TypPlatebnihoProstredku.HOTOVOST) {
                return true;
            }
        }
        return false;
    }

    public void odeberPolozkuPlatby(int cislo) {
        if (!DokladProcesor.getInstance().getDoklad().getPlatby().isEmpty()) {
            PolozkaPlatby pol = DokladProcesor.getInstance().getDoklad().getPlatby().get(cislo);
            LOGER.info("Bude odebrána platební položka {} s číslem {}.", pol, cislo);
            if (pol != null) {
                DokladProcesor.getInstance().getDoklad().getPlatby().remove(pol);
                DokladProcesor.getInstance().aktualizujBinarniKopii();
                platebniOkno.getKontroler().setPolozky();
                platebniOkno.getKontroler().setVratka(jeVratka());
            }
        }
    }

    public void zaplatKartou() {
        LOGER.info("Bude inicializována platba dokladu interní ID {} platební kartou.", DokladProcesor.getInstance().getDoklad().getId());
        PlatebniProstredek pp = null;
        for (PlatebniProstredek pol : platebniProstredky) {
            if (pol.getTyp() == PlatebniProstredek.TypPlatebnihoProstredku.KARTA) {
                pp = pol;
                break;
            }
        }
        if (pp != null) {
            vybranyPlatebniProstredek = pp;
            if (HlavniOkno.getInstance().isSkoliciRezimZapnut() || ManazerZarizeni.getInstance().getTerminal().isVirtualni()) {
                OtazkaAnoNe kartaOK = new OtazkaAnoNe("Zaplaťte kartou", """
                        Nyní prosím proveďte platbu na terminálu.
                                            
                        Pokud je platba úspěšná, stiskněte tlačítko OK. Pokud ne, stiskněte tlačítko Storno. 
                        V každém případě založte potvrzení pro obchodníka z terminálu.
                        """, platebniOkno);
                kartaOK.zobrazDialog();
                if (kartaOK.getVolba() == OtazkaAnoNe.Volby.ANO) {
                    zaregistrujPolozkuPlatby(getCelkemKZaplaceni());
                }
            } else {
                if (ManazerZarizeni.getInstance().getTerminal().isInicializovan()) {
                    boolean platbaProsla = ManazerZarizeni.getInstance().getTerminal().realizujPlatbu(getCelkemKZaplaceni(), jeVratka());
                    DokladProcesor.getInstance().getDoklad().getDataPlatbyKartou().add(ManazerZarizeni.getInstance().getTerminal().getDataPlatbyKartou());
                    DokladProcesor.getInstance().aktualizujBinarniKopii();
                    if (platbaProsla) {
                        zaregistrujPolozkuPlatby(getCelkemKZaplaceni());
                    }
                } else {
                    LOGER.warn("Terminál není inicializován.");
                    (new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Platební terminál není k dispozici",
                            "Platební terminál není k dispozici. Platba kartou nebude provedena. Proveďte inicializaci terminálu.", platebniOkno)).zobrazDialog(true);
                }
            }
        } else {
            LOGER.warn("Platební karta jako platební prostředek není k dispozici.");
            (new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Platební prostředek není k dispozici",
                    "Platební karta jako platební prostředek není k dispozici. Platba kartou nebude provedena.", platebniOkno)).zobrazDialog(true);
        }
    }

    private void zaplatDoklad() {
        LOGER.info("Doklad interní ID {} bude zaplacen a uzavřen.", DokladProcesor.getInstance().getDoklad().getId());
        platebniOkno.zavriDialog(true);
        progres.zobrazProgres("Uzavírám doklad a provádím ukládání do databáze.");
        try {
            NastaveniCislaDokladu nastaveniCisla = new NastaveniCislaDokladu();
            nastaveniCisla.run();
            if (nastaveniCisla.get()) {
                LOGER.info("Dokladu s id {} a pořadovým číslem {} bylo přiřazeno číslo {}.", DokladProcesor.getInstance().getDoklad().getId(), DokladProcesor.getInstance().getDoklad().getPoradoveCislo(), DokladProcesor.getInstance().getDoklad().getCislo());
                java.util.Date vytvoreni = new java.util.Date();
                DokladProcesor.getInstance().getDoklad().setDatumVytvoreni(vytvoreni);
                if (DokladProcesor.getInstance().getDoklad().getTyp() != TypDokladu.DODACI_LIST && Boolean.parseBoolean(Pos.getInstance().getNastaveni().getProperty("fiskalizace.eet.aktivni", "false"))) {
                    LOGER.info("Doklad interní ID {} bude odeslán do systému EET.", DokladProcesor.getInstance().getDoklad().getId());
                    try {
                        progres.setText("Odesílám data dokldadu do EET.");
                        EvidenceEET eet = new EvidenceEET(true);
                        eet.run();
                        if (eet.get()) {
                            LOGER.info("Evidence EET (prvotní) proběhla. Bude pokračovat uzavření dokladu.");
                            try {
                                progres.zobrazProgres("Uzavírám doklad a provádím ukládání do databáze.");
                                DokladProcesor.getInstance().getDoklad().setCastkaKVraceni(getKVraceni());
                                DokladProcesor.getInstance().uzavriDoklad();
                            } catch (Exception e) {
                                progres.zavriDialog();
                                LOGER.error("Došlo k selhání při uzavírání a ukládání dokladu.", e);
                                new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Došlo k selhání při uzavírání a ukládání dokladu.\n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                            }
                        } else {
                            progres.zavriDialog();
                            LOGER.error("Evidence EET neproběhla v pořádku.");
                            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba EET", "Nebylo možno evidovat EET. Nemohu pokračovat.\nZkontrolujte protokoly aplikace.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                            DokladProcesor.getInstance().getDoklad().getDataPlatbyKartou().clear();
                            DokladProcesor.getInstance().getDoklad().getPlatby().clear();
                            DokladProcesor.getInstance().aktualizujBinarniKopii();
                        }
                    } catch (Exception e) {
                        progres.zavriDialog();
                        LOGER.error("Došlo k selhání při odesílání dokladu do evidence tržeb.", e);
                        new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Došlo k selhání při odesílání EET \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                    }
                } else {
                    try {
                        DokladProcesor.getInstance().getDoklad().setCastkaKVraceni(getKVraceni());
                        DokladProcesor.getInstance().uzavriDoklad();
                    } catch (Exception e) {
                        progres.zavriDialog();
                        LOGER.error("Došlo k selhání při uzavírání a ukládání dokladu.", e);
                        new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Došlo k selhání při uzavírání a ukládání dokladu.\n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                    }
                }
            }
        } catch (Exception e) {
            progres.zavriDialog();
            LOGER.error("Došlo k selhání při přiřazování čísla dokladu.", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Došlo k selhání při přiřazování čísla dokladu.\n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        }
    }

    public ProgresDialog getProgres() {
        return progres;
    }
}
