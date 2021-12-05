/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesy.doklad;

import com.google.gson.Gson;
import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.adresy.Adresa;
import cz.jbenak.npos.pos.objekty.doklad.Doklad;
import cz.jbenak.npos.pos.objekty.doklad.RekapitulaceDPH;
import cz.jbenak.npos.pos.objekty.doklad.TypDokladu;
import cz.jbenak.npos.pos.objekty.doklad.UplatnenyPoukaz;
import cz.jbenak.npos.pos.objekty.platba.DataPlatbyKartou;
import cz.jbenak.npos.pos.objekty.platba.PolozkaPlatby;
import cz.jbenak.npos.pos.objekty.slevy.Sleva;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;
import cz.jbenak.npos.pos.procesory.DokladProcesor;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Objects;

/**
 * Třída pro vlastní uložení předaného dokladu do databáze.
 *
 * @author Jan Benák
 */
public class UlozeniDoDatabaze extends Task<Boolean> {

    private final static Logger LOGER = LogManager.getLogger(UlozeniDoDatabaze.class);
    private final Connection spojeni = Pos.getInstance().getSpojeni();
    private final Doklad doklad = DokladProcesor.getInstance().getDoklad();

    @Override
    protected Boolean call() throws Exception {
        LOGER.debug("Proces ukládání dokladu zahájen.");
        boolean procesOK = true;
        try {
            spojeni.setAutoCommit(false);
            spojeni.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            Savepoint spHlavicka = spojeni.setSavepoint("hlavickaDokladu");
            if (!ulozHlavicku()) {
                LOGER.warn("Uložení hlavičky dokladu s ID {} se nepovedlo. Akce bude vrácena zpět.", doklad.getId());
                spojeni.rollback(spHlavicka);
                procesOK = false;
            }
            if (!ulozRekapitulaciDPH()) {
                LOGER.warn("Uložení rekapitulace DPH dokladu s ID {} se nepovedlo. Akce bude vrácena zpět.", doklad.getId());
                spojeni.rollback(spHlavicka);
                procesOK = false;
            }
            if (!ulozPolozkyDokladu()) {
                LOGER.warn("Uložení položek na dokladu s ID {} se nepovedlo. Akce bude vrácena zpět.", doklad.getId());
                spojeni.rollback(spHlavicka);
                procesOK = false;
            }
            if (!doklad.getPlatby().isEmpty() && !ulozPlatby()) {
                LOGER.warn("Uložení plateb na dokladu s ID {} se nepovedlo. Akce bude vrácena zpět.", doklad.getId());
                spojeni.rollback(spHlavicka);
                procesOK = false;
            }
            if (!doklad.getSlevy().isEmpty() && !ulozSlevyNaDoklad()) {
                LOGER.warn("Uložení slev na dokladu s ID {} se nepovedlo. Akce bude vrácena zpět.", doklad.getId());
                spojeni.rollback(spHlavicka);
                procesOK = false;
            }
            if (!ulozSlevyNaPolozku()) {
                LOGER.warn("Uložení slev na položky dokladu s ID {} se nepovedlo. Akce bude vrácena zpět.", doklad.getId());
                spojeni.rollback(spHlavicka);
                procesOK = false;
            }
            if (!doklad.getUplatnenePoukazy().isEmpty() && !ulozUplatnenePoukazy()) {
                LOGER.warn("Uložení uplatněných poukazů na dokladu s ID {} se nepovedlo. Akce bude vrácena zpět.", doklad.getId());
                spojeni.rollback(spHlavicka);
                procesOK = false;
            }
            if (doklad.getDataEET() != null && !ulozDataEET()) {
                LOGER.warn("Uložení dat EET dokladu s ID {} se nepovedlo. Akce bude vrácena zpět.", doklad.getId());
                spojeni.rollback(spHlavicka);
                procesOK = false;
            }
            if (!doklad.getDataPlatbyKartou().isEmpty() && !ulozDataPlatbyKartou()) {
                LOGER.warn("Uložení dat plateb kartou dokladu s ID {} se nepovedlo. Akce bude vrácena zpět.", doklad.getId());
                spojeni.rollback(spHlavicka);
                procesOK = false;
            }
            if (!ulozHlavickuFaktury()) {
                LOGER.warn("Uložení hlavičky faktury pro doklad s ID {} se nepovedlo. Akce bude vrácena zpět.", doklad.getId());
                spojeni.rollback(spHlavicka);
                procesOK = false;
            }
            spojeni.commit();
            spojeni.setAutoCommit(true);
        } catch (Exception e) {
            LOGER.error("Nastala vnitřní chyba v procesu ukládání dokladu do databáze.", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Nemohu uložit doklad",
                    "Nastala chyba v procesu ukládání dokladu do databáze:\n\n" + e.getLocalizedMessage(),
                    Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            procesOK = false;
        }
        return procesOK;
    }

    private boolean ulozHlavicku() throws SQLException {
        LOGER.debug("Ukládám hlavičku dokladu s ID {}.", doklad.getId());
        final String dotaz = "INSERT INTO " + spojeni.getCatalog() + ".doklady (id_dokladu, cislo, poradove_cislo, typ, vratka, storno, " +
                "datum_vystaveni, cislo_pokladni, cislo_pokladny, cislo_smeny, id_sparovaneho_dokladu, castka_bez_dph, castka_vcetne_dph, filialka, zakaznik, zaplaceno, vraceno) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setObject(1, doklad.getId());
            psmt.setString(2, doklad.getCislo());
            psmt.setInt(3, doklad.getPoradoveCislo());
            psmt.setString(4, doklad.getTyp().name());
            psmt.setBoolean(5, doklad.isVratka());
            psmt.setBoolean(6, doklad.isStorno());
            psmt.setTimestamp(7, new Timestamp(doklad.getDatumVytvoreni().getTime()));
            psmt.setInt(8, HlavniOkno.getInstance().getPrihlasenaPokladni().getId());
            psmt.setInt(9, Pos.getInstance().getCisloPokladny());
            psmt.setString(10, doklad.getSmena().getCisloSmeny());
            if (doklad.getSparovanyDoklad() == null) {
                psmt.setNull(11, Types.OTHER);
            } else {
                psmt.setObject(11, doklad.getSparovanyDoklad());
            }
            psmt.setBigDecimal(12, doklad.getCelkemZaklad());
            psmt.setBigDecimal(13, doklad.getCenaDokladuCelkem());
            psmt.setString(14, Pos.getInstance().getFilialka().getOznaceni());
            if (doklad.getZakaznik() == null) {
                psmt.setNull(15, Types.VARCHAR);
            } else {
                psmt.setString(15, doklad.getZakaznik().getCislo());
            }
            if (doklad.isVratka()) {
                psmt.setBigDecimal(17, doklad.getCelkemZaplaceno().abs());
                psmt.setBigDecimal(16, BigDecimal.ZERO);
            } else {
                psmt.setBigDecimal(16, doklad.getCelkemZaplaceno());
                psmt.setBigDecimal(17, doklad.getCastkaKVraceni());
            }
            int radekUlozeno = psmt.executeUpdate();
            LOGER.debug("Uložení hlavičky dokladu s ID {} provedeno. Bylo vloženo {} řádek.", doklad.getId(), radekUlozeno);
            return radekUlozeno > 0;
        }
    }

    private boolean ulozRekapitulaciDPH() throws SQLException {
        LOGER.debug("Ukládám rekapitulaci DPH dokladu s ID {}.", doklad.getId());
        final String dotaz = "INSERT INTO " + spojeni.getCatalog() + ".rekapitulace_dph (doklad, cislo_dph, zaklad, dan, celkem) VALUES (?,?,?,?,?);";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            for (RekapitulaceDPH pol : doklad.getTabulkaDPH()) {
                psmt.setObject(1, doklad.getId());
                psmt.setInt(2, pol.getDph().getId());
                psmt.setBigDecimal(3, pol.getZaklad().abs());
                psmt.setBigDecimal(4, pol.getDan().abs());
                psmt.setBigDecimal(5, pol.getCelkem().abs());
                psmt.addBatch();
            }
            int[] radekUlozeno = psmt.executeBatch();
            LOGER.debug("Uložení rekapitulace DPH dokladu s ID {} provedeno. Bylo vloženo {} řádek.", doklad.getId(), radekUlozeno.length);
            return radekUlozeno.length > 0;
        }
    }

    private boolean ulozPolozkyDokladu() throws SQLException {
        LOGER.debug("Ukládám položky dokladu s ID {}.", doklad.getId());
        final String dotaz = "INSERT INTO " + spojeni.getCatalog() + ".polozky_prodejniho_dokladu (doklad, cislo_radku, registr, vraceno," +
                " mnozstvi, jednotkova_cena, cena_polozky_celkem, originalni_doklad, cena_polozky_celkem_s_dph, hodnota_dph)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?);";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            int cisloRadku = 1;
            for (Sortiment pol : doklad.getPolozky()) {
                pol.setCisloPolozkyVDokladu(cisloRadku);
                psmt.setObject(1, doklad.getId());
                psmt.setInt(2, pol.getCisloPolozkyVDokladu());
                psmt.setString(3, pol.getRegistr());
                psmt.setBoolean(4, pol.isVracena());
                psmt.setBigDecimal(5, pol.getMnozstvi());
                psmt.setBigDecimal(6, pol.getJednotkovaCena());
                psmt.setBigDecimal(7, pol.getCenaCelkem());
                if (pol.getOriginalniDoklad() == null) {
                    psmt.setNull(8, Types.OTHER);
                } else {
                    psmt.setObject(8, pol.getOriginalniDoklad());
                }
                psmt.setBigDecimal(9, pol.getCenaCelkem());
                psmt.setBigDecimal(10, pol.getDan());
                psmt.addBatch();
                cisloRadku++;
            }
            int[] radekUlozeno = psmt.executeBatch();
            LOGER.debug("Uložení položek dokladu s ID {} provedeno. Bylo vloženo {} řádek.", doklad.getId(), radekUlozeno.length);
            return radekUlozeno.length > 0;
        }
    }

    private boolean ulozPlatby() throws SQLException {
        LOGER.debug("Ukládám položky plateb dokladu s ID {}.", doklad.getId());
        final String dotaz = "INSERT INTO " + spojeni.getCatalog() + ".platby (doklad, cislo_polozky, vraceno, mena, " +
                "castka, castka_v_kmenove_mene, platebni_prostredek, kurz)" +
                " VALUES (?,?,?,?,?,?,?,?);";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            for (PolozkaPlatby pol : doklad.getPlatby()) {
                psmt.setObject(1, doklad.getId());
                psmt.setInt(2, pol.getCislo());
                psmt.setBoolean(3, pol.isVratka());
                psmt.setString(4, pol.getMena().getIsoKod());
                psmt.setBigDecimal(5, pol.getCastka());
                psmt.setBigDecimal(6, pol.getCastkaVKmenoveMene());
                psmt.setString(7, pol.getPlatebniProstredek().getId());
                if (pol.getKurzMeny() == null) {
                    psmt.setNull(8, Types.INTEGER);
                } else {
                    psmt.setInt(8, pol.getKurzMeny().getCisloKurzu());
                }
                psmt.addBatch();
            }
            int[] radekUlozeno = psmt.executeBatch();
            LOGER.debug("Uložení položek plateb dokladu s ID {} provedeno. Bylo vloženo {} řádek.", doklad.getId(), radekUlozeno.length);
            return radekUlozeno.length > 0;
        }
    }

    private boolean ulozHlavickuFaktury() throws SQLException {
        if (doklad.getTyp() == TypDokladu.FAKTURA) {
            LOGER.info("Doklad s ID {} je vystaven jako faktura. Bude uložena hlavička faktury.", doklad.getId());
            final String dotaz = "INSERT INTO " + spojeni.getCatalog() + ".hlavicka_faktury (doklad, id_zakaznika, nazev, nazev_druhy_radek, jmeno, prijmeni, " +
                    "ulice, cp, cor, psc, mesto, stat, ic, dic) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                psmt.setObject(1, doklad.getId());
                if (doklad.getZakaznik() != null) {
                    psmt.setString(2, doklad.getZakaznik().getCislo());
                    if (doklad.getZakaznik().getNazev() == null) {
                        psmt.setNull(3, Types.VARCHAR);
                    } else {
                        psmt.setString(3, doklad.getZakaznik().getNazev());
                    }
                    Adresa hlavniAdresa = getFakturacniAdresaZakaznika(doklad.getZakaznik().getAdresy());
                    if (Objects.requireNonNull(hlavniAdresa).getAdresaDruhyRadek() == null) {
                        psmt.setNull(4, Types.VARCHAR);
                    } else {
                        psmt.setString(4, hlavniAdresa.getAdresaDruhyRadek());
                    }
                    if (doklad.getZakaznik().getJmeno() == null) {
                        psmt.setNull(5, Types.VARCHAR);
                    } else {
                        psmt.setString(5, doklad.getZakaznik().getJmeno());
                    }
                    if (doklad.getZakaznik().getPrijmeni() == null) {
                        psmt.setNull(6, Types.VARCHAR);
                    } else {
                        psmt.setString(6, doklad.getZakaznik().getPrijmeni());
                    }
                    if (Objects.requireNonNull(hlavniAdresa).getUlice() == null) {
                        psmt.setNull(7, Types.VARCHAR);
                    } else {
                        psmt.setString(7, hlavniAdresa.getUlice());
                    }
                    if (Objects.requireNonNull(hlavniAdresa).getCp() == null) {
                        psmt.setNull(8, Types.VARCHAR);
                    } else {
                        psmt.setString(8, hlavniAdresa.getCp());
                    }
                    if (Objects.requireNonNull(hlavniAdresa).getCor() == null) {
                        psmt.setNull(9, Types.VARCHAR);
                    } else {
                        psmt.setString(9, hlavniAdresa.getCor());
                    }
                    if (Objects.requireNonNull(hlavniAdresa).getPsc() == null) {
                        psmt.setNull(10, Types.VARCHAR);
                    } else {
                        psmt.setString(10, hlavniAdresa.getPsc());
                    }
                    if (Objects.requireNonNull(hlavniAdresa).getMesto() == null) {
                        psmt.setNull(11, Types.VARCHAR);
                    } else {
                        psmt.setString(11, hlavniAdresa.getMesto());
                    }
                    psmt.setString(12, Objects.requireNonNull(hlavniAdresa).getStat().getIsoKod());
                    if (doklad.getZakaznik().getIc() == null) {
                        psmt.setNull(13, Types.VARCHAR);
                    } else {
                        psmt.setString(13, doklad.getZakaznik().getIc());
                    }
                    if (doklad.getZakaznik().getDic() == null) {
                        psmt.setNull(14, Types.VARCHAR);
                    } else {
                        psmt.setString(14, doklad.getZakaznik().getDic());
                    }
                } else {
                    psmt.setNull(2, Types.VARCHAR);
                    if (doklad.getHlavickaFaktury().getNazev() == null) {
                        psmt.setNull(3, Types.VARCHAR);
                    } else {
                        psmt.setString(3, doklad.getHlavickaFaktury().getNazev());
                    }
                    psmt.setNull(4, Types.VARCHAR);
                    if (doklad.getHlavickaFaktury().getJmeno() == null) {
                        psmt.setNull(5, Types.VARCHAR);
                    } else {
                        psmt.setString(5, doklad.getHlavickaFaktury().getJmeno());
                    }
                    if (doklad.getHlavickaFaktury().getPrijmeni() == null) {
                        psmt.setNull(6, Types.VARCHAR);
                    } else {
                        psmt.setString(6, doklad.getHlavickaFaktury().getPrijmeni());
                    }
                    if (doklad.getHlavickaFaktury().getAdresa().getUlice() == null) {
                        psmt.setNull(7, Types.VARCHAR);
                    } else {
                        psmt.setString(7, doklad.getHlavickaFaktury().getAdresa().getUlice());
                    }
                    if (doklad.getHlavickaFaktury().getAdresa().getCp() == null) {
                        psmt.setNull(8, Types.VARCHAR);
                    } else {
                        psmt.setString(8, doklad.getHlavickaFaktury().getAdresa().getCp());
                    }
                    if (doklad.getHlavickaFaktury().getAdresa().getCor() == null) {
                        psmt.setNull(9, Types.VARCHAR);
                    } else {
                        psmt.setString(9, doklad.getHlavickaFaktury().getAdresa().getCor());
                    }
                    if (doklad.getHlavickaFaktury().getAdresa().getPsc() == null) {
                        psmt.setNull(10, Types.VARCHAR);
                    } else {
                        psmt.setString(10, doklad.getHlavickaFaktury().getAdresa().getPsc());
                    }
                    if (doklad.getHlavickaFaktury().getAdresa().getMesto() == null) {
                        psmt.setNull(11, Types.VARCHAR);
                    } else {
                        psmt.setString(11, doklad.getHlavickaFaktury().getAdresa().getMesto());
                    }
                    psmt.setString(12, doklad.getHlavickaFaktury().getAdresa().getStat().getIsoKod());
                    if (doklad.getHlavickaFaktury().getIc() == null) {
                        psmt.setNull(13, Types.VARCHAR);
                    } else {
                        psmt.setString(13, doklad.getHlavickaFaktury().getIc());
                    }
                    if (doklad.getHlavickaFaktury().getDic() == null) {
                        psmt.setNull(14, Types.VARCHAR);
                    } else {
                        psmt.setString(14, doklad.getHlavickaFaktury().getDic());
                    }
                }
                int radekUlozeno = psmt.executeUpdate();
                LOGER.debug("Uložení hlavičky faktury s ID {} provedeno. Bylo vloženo {} řádek.", doklad.getId(), radekUlozeno);
                return radekUlozeno > 0;
            }
        } else {
            LOGER.info("Doklad s ID {} není vystaven jako faktura.", doklad.getId());
            return true;
        }
    }

    private Adresa getFakturacniAdresaZakaznika(List<Adresa> adresy) {
        for (Adresa a : adresy) {
            if (a.isHlavni()) {
                return a;
            }
        }
        return null;
    }

    private boolean ulozDataPlatbyKartou() throws SQLException {
        if (doklad.getDataPlatbyKartou().isEmpty()) {
            LOGER.info("Doklad s ID {} neobsahuje informace o transakcích z platebního terminálu.", doklad.getId());
            return true;
        } else {
            LOGER.debug("Ukládám informace o transakcích z platebního terminálu dokladu s ID {}.", doklad.getId());
            final String dotaz = "INSERT INTO " + spojeni.getCatalog() + ".data_platby_kartou (doklad, cislo_platby, cislo_karty, cislo_transakce, vratka, " +
                    "cislo_terminalu, autorizacni_id, stav_transakce, datum_a_cas, castka, vybrana_mena, data_terminalu, druh_karty)" +
                    " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
            try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                int polozka = 1;
                for (DataPlatbyKartou pol : doklad.getDataPlatbyKartou()) {
                    psmt.setObject(1, doklad.getId());
                    psmt.setInt(2, polozka);
                    psmt.setString(3, pol.getCisloKarty());
                    psmt.setString(4, pol.getCisloTransakce());
                    psmt.setBoolean(5, pol.isVratka());
                    psmt.setString(6, pol.getCisloTerminalu());
                    psmt.setString(7, pol.getAutorizacniId());
                    psmt.setString(8, pol.getStav().name());
                    psmt.setTimestamp(9, Timestamp.valueOf(pol.getDatumACasTransakce()));
                    psmt.setBigDecimal(10, pol.getCastka());
                    psmt.setString(11, pol.getVybranaMena().getIsoKod());
                    psmt.setString(12, pol.getDataZTerminalu());
                    psmt.setString(13, pol.getDruhKarty());
                    psmt.addBatch();
                    polozka++;
                }
                int[] radekUlozeno = psmt.executeBatch();
                LOGER.debug("Uložení informací o transakcích z platebního terminál dokladu s ID {} provedeno. Bylo vloženo {} řádek.", doklad.getId(), radekUlozeno.length);
                return radekUlozeno.length > 0;
            }
        }
    }

    private boolean ulozUplatnenePoukazy() throws SQLException {
        if (doklad.getUplatnenePoukazy().isEmpty()) {
            LOGER.info("Doklad s ID {} neobsahuje žádné uplatněné poukazy.", doklad.getId());
            return true;
        } else {
            LOGER.debug("Ukládám uplatněné poukazy na dokladu s ID {}.", doklad.getId());
            final String dotaz = "INSERT INTO " + spojeni.getCatalog() + ".uplatnene_poukazy (doklad, cislo_poukazu, hodnota_poukazu, uplatnena_hodnota, " +
                    "castecne_uplatneni, vazana_polozka_platby, vazana_skupina_slev, typ_hodnoty) VALUES (?,?,?,?,?,?,?,?);";
            try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                for (UplatnenyPoukaz poukaz : doklad.getUplatnenePoukazy()) {
                    psmt.setObject(1, doklad.getId());
                    psmt.setString(2, poukaz.getCisloPoukazu());
                    psmt.setBigDecimal(3, poukaz.getHodnotaPoukazu());
                    psmt.setBigDecimal(4, poukaz.getUplatnenaHodnota());
                    psmt.setBoolean(5, poukaz.isCastecneUplatneni());
                    if (poukaz.getVazanaPolozkaPlatby() == null) {
                        psmt.setNull(6, Types.INTEGER);
                    } else {
                        psmt.setInt(6, poukaz.getVazanaPolozkaPlatby().getCislo());
                    }
                    if (poukaz.getVazanaSkupinaSlev() == null) {
                        psmt.setNull(7, Types.INTEGER);
                    } else {
                        psmt.setInt(7, poukaz.getVazanaSkupinaSlev().getCislo());
                    }
                    psmt.setString(8, poukaz.getTypHodnoty().name());
                    psmt.addBatch();
                }
                int[] radekUlozeno = psmt.executeBatch();
                LOGER.debug("Uložení uplatněných poukazů na dokladu s ID {} provedeno. Bylo vloženo {} řádek.", doklad.getId(), radekUlozeno.length);
                return radekUlozeno.length > 0;
            }
        }
    }

    private boolean ulozSlevyNaPolozku() throws SQLException {
        boolean obsahujeSlevy = false;
        for (Sortiment polozka : doklad.getPolozky()) {
            if (!polozka.getSlevy().isEmpty()) {
                obsahujeSlevy = true;
                break;
            }
        }
        if (obsahujeSlevy) {
            final String dotaz = "INSERT INTO " + spojeni.getCatalog() + ".slevy_na_polozku (cislo_polozky, doklad, cislo_skupiny_slev, hodnota, registr) VALUES (?,?,?,?,?);";
            try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                for (Sortiment polozka : doklad.getPolozky()) {
                    if (!polozka.getSlevy().isEmpty()) {
                        for (Sleva sleva : polozka.getSlevy()) {
                            psmt.setInt(1, polozka.getCisloPolozkyVDokladu());
                            psmt.setObject(2, doklad.getId());
                            psmt.setInt(3, sleva.getCisloSkupinySlev());
                            psmt.setBigDecimal(4, sleva.getHodnota());
                            psmt.setString(5, polozka.getRegistr());
                            psmt.addBatch();
                        }
                    }
                }
                int[] radekUlozeno = psmt.executeBatch();
                LOGER.debug("Uložení slev na položku na dokladu s ID {} provedeno. Bylo vloženo {} řádek.", doklad.getId(), radekUlozeno.length);
                return radekUlozeno.length > 0;
            }
        } else {
            LOGER.info("Doklad s ID {} neobsahuje žádné slevy na položku.", doklad.getId());
            return true;
        }
    }

    private boolean ulozSlevyNaDoklad() throws SQLException {
        if (doklad.getSlevy().isEmpty()) {
            LOGER.info("Doklad s ID {} neobsahuje žádné slevy.", doklad.getId());
            return true;
        } else {
            final String dotaz = "INSERT INTO " + spojeni.getCatalog() + ".slevy_na_doklad (doklad, cislo_skupiny_slev, hodnota) VALUES (?,?,?);";
            try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                for (Sleva sleva : doklad.getSlevy()) {
                    psmt.setObject(1, doklad.getId());
                    psmt.setInt(2, sleva.getCisloSkupinySlev());
                    psmt.setBigDecimal(3, sleva.getHodnota());
                    psmt.addBatch();
                }
                int[] radekUlozeno = psmt.executeBatch();
                LOGER.debug("Uložení slev na dokladu s ID {} provedeno. Bylo vloženo {} řádek.", doklad.getId(), radekUlozeno.length);
                return radekUlozeno.length > 0;
            }
        }
    }

    private boolean ulozDataEET() throws SQLException {
        LOGER.info("Ukládám data EET (prvotní odeslání) dokaldu {} do databáze.", doklad.getId());
        final String dotaz = "INSERT INTO " + spojeni.getCatalog() + ".eet (doklad, cislo_dokladu, prvni_odeslani, odeslani_ok, datum_odeslani, bkp, pkp, fik, " +
                "celkova_trzba, cislo_pokladny, id_provozovny, dic, poverujici_dic, rezim, chyby) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setObject(1, doklad.getId());
            psmt.setString(2, doklad.getCislo());
            psmt.setBoolean(3, doklad.getDataEET().isPrvniOdeslani());
            psmt.setBoolean(4, doklad.getDataEET().isOdeslaniOK());
            psmt.setTimestamp(5, new Timestamp(doklad.getDataEET().getDatum().getTime()));
            psmt.setString(6, doklad.getDataEET().getBkp());
            if (doklad.getDataEET().getPkp() == null || doklad.getDataEET().getPkp().isEmpty()) {
                psmt.setNull(7, Types.VARCHAR);
            } else {
                psmt.setString(7, doklad.getDataEET().getPkp());
            }
            if (doklad.getDataEET().getFik() == null || doklad.getDataEET().getFik().isEmpty()) {
                psmt.setNull(8, Types.VARCHAR);
            } else {
                psmt.setString(8, doklad.getDataEET().getFik());
            }
            psmt.setBigDecimal(9, doklad.getDataEET().getCelkovaCastka());
            psmt.setInt(10, Pos.getInstance().getCisloPokladny());
            psmt.setInt(11, doklad.getDataEET().getIdProvozovny());
            psmt.setString(12, doklad.getDataEET().getDic());
            if (doklad.getDataEET().getPoverujiciDic() == null || doklad.getDataEET().getPoverujiciDic().isEmpty()) {
                psmt.setNull(13, Types.VARCHAR);
            } else {
                psmt.setString(13, doklad.getDataEET().getPoverujiciDic());
            }
            psmt.setInt(14, doklad.getDataEET().getRezim());
            if (doklad.getDataEET().getChybyEET().isEmpty()) {
                psmt.setNull(15, Types.VARCHAR);
            } else {
                psmt.setString(15, (new Gson()).toJson(doklad.getDataEET().getChybyEET()));
            }
            int radekUlozeno = psmt.executeUpdate();
            LOGER.debug("Uložení dat EET s ID {} provedeno. Bylo vloženo {} řádek.", doklad.getId(), radekUlozeno);
            return radekUlozeno > 0;
        }
    }
}
