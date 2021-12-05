package cz.jbenak.npos.pos.procesy.zakaznik;

import cz.jbenak.npos.pos.objekty.adresy.Adresa;
import cz.jbenak.npos.pos.objekty.adresy.Stat;
import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
import cz.jbenak.npos.pos.objekty.slevy.Pravidlo;
import cz.jbenak.npos.pos.objekty.slevy.SkupinaSlev;
import cz.jbenak.npos.pos.objekty.slevy.Sleva;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-09-04
 * Třída procesu pro načtení detailu vybraného zákazníka.
 */
public class NacteniDetailuVybranehoZakaznika extends Task<Boolean> {

    private final static Logger LOGER = LogManager.getLogger(NacteniDetailuVybranehoZakaznika.class);
    private final Connection spojeni = Pos.getInstance().getSpojeni();
    private final Zakaznik zakaznik;

    /**
     * Konstruktor procesu načtení vybraného zákazníka.
     *
     * @param zakaznik vybraný zákazník, jehož data budou doplněna.
     * @see Zakaznik
     */
    public NacteniDetailuVybranehoZakaznika(Zakaznik zakaznik) {
        this.zakaznik = zakaznik;
    }

    /**
     * Metoda pro spuštění procesu načítání detailů vybraného zákazníka.
     *
     * @return stav načtení dat vybraného zákazníka.
     * @see Zakaznik
     */
    @Override
    protected Boolean call() {
        LOGER.info("Načtení detailu vybraného zákazníka s číslem {} bude provedeno.", zakaznik.getCislo());
        boolean vysledek = false;
        try {
            nactiVsechnyAdresy();
            nactiSkupinuSlev();
            zakaznik.setDetailNacten(true);
            vysledek = true;
        } catch (SQLException e) {
            LOGER.error("Nastala chyba při načítání detailu vybraného zákazníka s číslem {}: {}", zakaznik.getCislo(), e);
            failed();
        }
        return vysledek;
    }

    private void nactiVsechnyAdresy() throws SQLException {
        LOGER.debug("Budou načteny všechny adresy zákazníka s číslem {}", zakaznik.getCislo());
        final String dotaz = "SELECT adresy.*, adresy_zakazniku.dodaci, adresy_zakazniku.hlavni AS hlavni_adresa, staty.* FROM " + spojeni.getCatalog() + ".adresy "
                + "INNER JOIN " + spojeni.getCatalog() + ".adresy_zakazniku ON adresy.id = adresy_zakazniku.adresa "
                + "INNER JOIN " + spojeni.getCatalog() + ".staty ON adresy.stat = staty.iso_kod "
                + "WHERE adresy_zakazniku.zakaznik = ?";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setString(1, zakaznik.getCislo());
            ResultSet rs = psmt.executeQuery();
            int radek = 0;
            if (rs != null) {
                if (rs.last()) {
                    radek = rs.getRow();
                    if (radek > 0) {
                        zakaznik.getAdresy().clear();
                    }
                    rs.first();
                    while (rs.getRow() <= radek) {
                        Adresa a = new Adresa();
                        Stat s = new Stat();
                        a.setHlavni(rs.getBoolean("hlavni_adresa"));
                        a.setId(rs.getInt("id"));
                        a.setAdresaDruhyRadek(rs.getString("druhy_radek"));
                        a.setCp(rs.getString("cp"));
                        a.setCor(rs.getString("cor"));
                        a.setUlice(rs.getString("ulice"));
                        a.setMesto(rs.getString("mesto"));
                        a.setPsc(rs.getString("psc"));
                        a.setTelefon(rs.getString("telefon"));
                        a.setMobil(rs.getString("mobil"));
                        a.setFax(rs.getString("fax"));
                        a.setEmail(rs.getString("email"));
                        a.setWeb(rs.getString("web"));
                        a.setDodaci(rs.getBoolean("dodaci"));
                        s.setIsoKod(rs.getString("iso_kod"));
                        s.setBeznyNazev(rs.getString("bezny_nazev"));
                        s.setUplnyNazev(rs.getString("uplny_nazev"));
                        s.setHlavni(rs.getBoolean("hlavni"));
                        a.setStat(s);
                        zakaznik.getAdresy().add(a);
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
                }
            }
            LOGER.debug("Bylo načteno {} řádek adres.", radek);
        }
    }

    private void nactiSkupinuSlev() throws SQLException {
        LOGER.debug("Budou načtena skupina slev přiřazená zákazníkovi s číslem {}", zakaznik.getCislo());
        final String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".skupiny_slev WHERE cislo = ?;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setInt(1, zakaznik.getSkupinaSlev().getCislo());
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    rs.first();
                    zakaznik.getSkupinaSlev().setNazev(rs.getString("nazev"));
                    zakaznik.getSkupinaSlev().setTyp(SkupinaSlev.TypSkupinySlev.valueOf(rs.getString("typ_skupiny_slev")));
                    zakaznik.getSkupinaSlev().setTypSlevy(Sleva.TypSlevy.valueOf(rs.getString("typ_slevy")));
                    zakaznik.getSkupinaSlev().setHodnotaSlevy(rs.getBigDecimal("hodnota_slevy"));
                    zakaznik.getSkupinaSlev().setIdCileSlevy(rs.getString("id_cile_slevy"));
                    zakaznik.getSkupinaSlev().setRozsahPouziti(SkupinaSlev.RozsahPouziti.valueOf(rs.getString("rozsah_pouziti")));
                    zakaznik.getSkupinaSlev().setOkamzikUplatneni(SkupinaSlev.OkamzikUplatneni.valueOf(rs.getString("okamzik_uplatneni")));
                    nactiPravidlaSkupinySlev();
                }
            }
        }
    }

    private void nactiPravidlaSkupinySlev() throws SQLException {
        LOGER.debug("Budou načtena pravidla skupiny slev č. {} přiřazená zákazníkovi s číslem {}", zakaznik.getSkupinaSlev().getCislo(), zakaznik.getCislo());
        final String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".pravidla_skupin_slev WHERE cislo_skupiny_slev = ? ORDER BY poradove_cislo ASC;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            int radek = 0;
            psmt.setInt(1, zakaznik.getSkupinaSlev().getCislo());
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    radek = rs.getRow();
                    rs.first();
                    zakaznik.getSkupinaSlev().getPravidla().clear();
                    while (rs.getRow() <= radek) {
                        Pravidlo p = new Pravidlo(rs.getInt("cislo_pravidla"),
                                rs.getInt("poradove_cislo"),
                                Pravidlo.Operator.valueOf(rs.getString("operator")),
                                Pravidlo.TypPodminky.valueOf(rs.getString("typ_podminky")));
                        p.setHodnotaPodminky(rs.getBigDecimal("hodnota_podminky"));
                        p.setIdObjektuPodminky(rs.getString("id_polozky_podminky"));
                        zakaznik.getSkupinaSlev().getPravidla().add(p);
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
                }
            }
            LOGER.debug("Bylo načteno {} řádek adres.", radek);
        }
    }
}
