/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesy.registrace;

import cz.jbenak.npos.pos.objekty.adresy.Adresa;
import cz.jbenak.npos.pos.objekty.adresy.Stat;
import cz.jbenak.npos.pos.objekty.finance.DPH;
import cz.jbenak.npos.pos.objekty.partneri.Dodavatel;
import cz.jbenak.npos.pos.objekty.sortiment.CarovyKod;
import cz.jbenak.npos.pos.objekty.sortiment.MernaJednotka;
import cz.jbenak.npos.pos.objekty.sortiment.SkupinaSortimentu;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída procesu pro načtení dat položky z databáze.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-16
 */
public class NacteniPolozky extends Task<Sortiment> {
    
    private static final Logger LOGER = LogManager.getLogger(NacteniPolozky.class);
    private final Connection spojeni = Pos.getInstance().getSpojeni();
    private final Sortiment polozka;
    
    public NacteniPolozky(Sortiment polozka) {
        this.polozka = polozka;
    }
    
    @Override
    protected Sortiment call() throws Exception {
        LOGER.info("Data položky s registračním číslem {} budou načtena.", polozka.getRegistr());
        try {
            nactiHlavicku();
            polozka.setMj(nactiMj(nactiMj(polozka.getMj())));
            nactiSkupinu(polozka.getSkupina());
            nactiDPH(polozka.getDph());
            nactiHlavniCarovyKod();
            if (polozka.getDodavatel() != null) {
                nactiDodavatele(polozka.getDodavatel());
                nactiAdresyDodavatele(polozka.getDodavatel());
            }
            if (polozka.getObsahovaMJ() != null) {
                polozka.setObsahovaMJ(nactiMj(polozka.getObsahovaMJ()));
            }
            if (polozka.getZakladniObsahovaMJ() != null) {
                polozka.setZakladniObsahovaMJ(nactiMj(polozka.getZakladniObsahovaMJ()));
            }
        } catch (SQLException e) {
            LOGER.error("Nebylo možné načíst data položky s registračním číslem {} z databáze: ", polozka.getRegistr(), e);
            failed();
//            throw new Exception(e);
        }
        return polozka;
    }
    
    
    
    private void nactiHlavicku() throws SQLException {
        LOGER.debug("Data položky s registračním číslem {} budou načtena.", polozka.getRegistr());
        final String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".sortiment WHERE registr = ?;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setString(1, polozka.getRegistr());
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.first()) {
                    SkupinaSortimentu skupina = new SkupinaSortimentu();
                    skupina.setId(rs.getString("skupina"));
                    polozka.setSkupina(skupina);
                    polozka.setNazev(rs.getString("nazev"));
                    polozka.setPLU(rs.getString("plu"));
                    MernaJednotka mj = new MernaJednotka();
                    mj.setJednotka(rs.getString("mj"));
                    polozka.setMj(mj);
                    DPH dph = new DPH();
                    dph.setId(rs.getInt("dph"));
                    polozka.setDph(dph);
                    polozka.setMinProdavaneMnozstvi(rs.getBigDecimal("minimalni_prodejne_mnozstvi"));
                    polozka.setMaxProdejneMnozstvi(rs.getBigDecimal("maximalni_prodejne_mnozstvi"));
                    polozka.setProdejne(rs.getBoolean("prodej_povolen"));
                    polozka.setJednotkovaCena(rs.getBigDecimal("jednotkova_cena"));
                    if (rs.getInt("dodavatel") > 0) {
                        Dodavatel dodavatel = new Dodavatel();
                        dodavatel.setId(rs.getInt("dodavatel"));
                        polozka.setDodavatel(dodavatel);
                    }
                    polozka.setSlevaPovolena(rs.getBoolean("sleva_povolena"));
                    polozka.setSlevaVAkciPovolena(rs.getBoolean("sleva_v_akci_povolena"));
                    polozka.setZmenaCenyPovolena(rs.getBoolean("zmena_ceny_povolena"));
                    polozka.setNeskladova(rs.getBoolean("neskladova"));
                    polozka.setSluzba(rs.getBoolean("sluzba"));
                    polozka.setNutnoZadatCenu(rs.getBoolean("nutno_zadat_cenu"));
                    polozka.setNutnoZadatMnozstvi(rs.getBoolean("nutno_zadat_mnozstvi"));
                    polozka.setPlatebniPoukaz(rs.getBoolean("platebni_poukaz"));
                    polozka.setVratkaPovolena(rs.getBoolean("vratka_povolena"));
                    polozka.setNedelitelne(rs.getBoolean("nedelitelne"));
                    polozka.setPopis(rs.getString("popis"));
                    polozka.setObrazek(rs.getBytes("obrazek"));
                    polozka.setNutnoZadatPopis(rs.getBoolean("nutno_zadat_popis"));
                    polozka.setObsahBaleni(rs.getBigDecimal("obsah_baleni"));
                    if (rs.getString("obsahova_mj") != null && !rs.getString("obsahova_mj").isEmpty()) {
                        MernaJednotka obsahovaMj = new MernaJednotka();
                        obsahovaMj.setJednotka(rs.getString("obsahova_mj"));
                        polozka.setObsahovaMJ(obsahovaMj);
                    }
                    polozka.setPomerneMnozstvi(rs.getBigDecimal("pomerne_mnozstvi"));
                    if (rs.getString("zakladni_obsahova_mj") != null && !rs.getString("zakladni_obsahova_mj").isEmpty()) {
                        MernaJednotka zakladniOMJ = new MernaJednotka();
                        zakladniOMJ.setJednotka(rs.getString("zakladni_obsahova_mj"));
                        polozka.setZakladniObsahovaMJ(zakladniOMJ);
                        polozka.setZakladniObsahoveMnozstvi(rs.getBigDecimal("zakladni_obsahove_mnozstvi"));
                    }
                }
            }
        }
    }
    
    private void nactiDPH(DPH dph) throws SQLException {
        LOGER.debug("DPH s id skupiny {} bude načtena pro položku sortimentu {}.", dph.getId(), polozka.getRegistr());
        final String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".dph WHERE id = ?;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setInt(1, dph.getId());
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.first()) {
                    dph.setTyp(DPH.TypDPH.valueOf(rs.getString("typ")));
                    dph.setSazba(rs.getBigDecimal("sazba"));
                    dph.setOznaceni(rs.getString("oznaceni"));
                    dph.setPlatnostOd(rs.getDate("platnost_od"));
                    dph.setPlatnostDo(rs.getDate("platnost_do"));
                }
            }
        }
    }
    
    private void nactiSkupinu(SkupinaSortimentu skupina) throws SQLException {
        LOGER.debug("Skupina sortimentu s id {} bude načtena pro položku sortimentu {}.", skupina.getId(), polozka.getRegistr());
        final String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".skupiny_sortimentu WHERE id = ?;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setString(1, skupina.getId());
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.first()) {
                    skupina.setNazev(rs.getString("nazev"));
                    skupina.setIdNadrazeneSkupiny(rs.getString("id_nadrazene_skupiny"));
                    skupina.setSlevaPovolena(rs.getBoolean("sleva_povolena"));
                    skupina.setMaxSleva(rs.getBigDecimal("max_sleva"));
                }
            }
        }
    }
    
    private MernaJednotka nactiMj(MernaJednotka mj) throws SQLException {
        LOGER.debug("Měrná jednotka {} bude načtena pro položku sortimentu {}.", mj.getJednotka(), polozka.getRegistr());
        final String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".merne_jednotky WHERE mj = ?;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setString(1, mj.getJednotka());
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.first()) {
                    mj.setNazev(rs.getString("oznaceni"));
                    mj.setZakladniJednotka(rs.getString("zakladni_mj"));
                    mj.setPomer(rs.getBigDecimal("pomer"));
                }
            }
        }
        return mj;
    }
    
    private void nactiDodavatele(Dodavatel dodavatel) throws SQLException {
        LOGER.debug("Dodavatel s interním ID {} bude načtena pro položku sortimentu {}.", dodavatel.getId(), polozka.getRegistr());
        final String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".dodavatele WHERE id = ?;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setInt(1, dodavatel.getId());
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.first()) {
                    dodavatel.setNazev(rs.getString("nazev"));
                    dodavatel.setDoplnekNazvu(rs.getString("doplnek_nazvu"));
                    dodavatel.setIc(rs.getString("ic"));
                    dodavatel.setDic(rs.getString("dic"));
                }
            }
        }
    }
    
    private void nactiAdresyDodavatele(Dodavatel dodavatel) throws SQLException {
        LOGER.debug("Pro dodavatele s interním ID {} budou načteny adresy.", dodavatel.getId());
        final String dotaz = "SELECT adresy_dodavatelu.*, adresy.*, staty.* FROM " + spojeni.getCatalog() + ".adresy_dodavatelu "
                + "INNER JOIN " + spojeni.getCatalog() + ".adresy ON adresy_dodavatelu.adresa = adresy.id "
                + "INNER JOIN " + spojeni.getCatalog() + ".staty ON adresy.stat = staty.iso_kod "
                + "WHERE adresy_dodavatelu.dodavatel = ?;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setInt(1, dodavatel.getId());
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    List<Adresa> adresy = new ArrayList();
                    int radek = rs.getRow();
                    rs.first();
                    while (rs.getRow() <= radek) {
                        Adresa a = new Adresa();
                        a.setId(rs.getInt("id"));
                        a.setHlavni(rs.getBoolean("hlavni"));
                        a.setAdresaDruhyRadek(rs.getString("druhy_radek"));
                        a.setCp(rs.getString("cp"));
                        a.setCor(rs.getString("cor"));
                        a.setUlice(rs.getString("ulice"));
                        a.setMesto(rs.getString("mesto"));
                        a.setPsc(rs.getString("psc"));
                        Stat s = new Stat();
                        s.setIsoKod(rs.getString("iso_kod"));
                        s.setBeznyNazev(rs.getString("bezny_nazev"));
                        s.setUplnyNazev(rs.getString("uplny_nazev"));
                        s.setHlavni(rs.getBoolean("hlavni"));
                        s.setKodMeny(rs.getString("mena"));
                        a.setStat(s);
                        a.setTelefon(rs.getString("telefon"));
                        a.setMobil(rs.getString("mobil"));
                        a.setFax(rs.getString("fax"));
                        a.setEmail(rs.getString("email"));
                        a.setWeb(rs.getString("web"));
                        adresy.add(a);
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
                    dodavatel.setAdresy(adresy);
                }
            }
        }
    }

    private void nactiHlavniCarovyKod() throws SQLException {
        LOGER.debug("Hlavní čárový kód bude načten pro položku sortimentu {}.", polozka.getRegistr());
        final String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".carove_kody WHERE registr = ? AND hlavni = true;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setString(1, polozka.getRegistr());
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.first()) {
                    CarovyKod hlavniCK = new CarovyKod();
                    hlavniCK.setHlavni(true);
                    hlavniCK.setKod(rs.getString("kod"));
                    hlavniCK.setTyp(CarovyKod.TypCarovehoKodu.valueOf(rs.getString("typ")));
                    hlavniCK.setMnozstvi(rs.getBigDecimal("mnozstvi"));
                    polozka.setHlavniCarovyKod(hlavniCK);
                }
            }
        }
    }
    
}
