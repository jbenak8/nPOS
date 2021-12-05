package cz.jbenak.npos.pos.procesy.zakaznik;

import cz.jbenak.npos.pos.objekty.adresy.Adresa;
import cz.jbenak.npos.pos.objekty.adresy.Stat;
import cz.jbenak.npos.pos.objekty.partneri.KriteriaVyhledavaniZakaznika;
import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
import cz.jbenak.npos.pos.objekty.slevy.SkupinaSlev;
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
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-07-14
 * <p>
 * Třída procesu pro načtení seznamu zákazníků dle zadaných kritérií
 */

public class NacteniSeznamuZakazniku extends Task<List<Zakaznik>> {

    private final static Logger LOGGER = LogManager.getLogger(NacteniSeznamuZakazniku.class);
    private final Connection spojeni = Pos.getInstance().getSpojeni();
    private final List<Zakaznik> zakaznici = new ArrayList<>();
    private final KriteriaVyhledavaniZakaznika filtracniKriteria;
    private final int maxZaznamu;

    /**
     * Konstruktor procesu pro získání základního seznamu zákazníků
     *
     * @param filtracniKriteria objekt s hodnotami pro příslušný filtr.
     * @param maxZaznamu        stanoví maximum načtených záznamů, pro vše zadejte -1
     */
    public NacteniSeznamuZakazniku(KriteriaVyhledavaniZakaznika filtracniKriteria, int maxZaznamu) {
        this.filtracniKriteria = filtracniKriteria;
        this.maxZaznamu = maxZaznamu;
    }

    /**
     * Metoda pro načtení základního seznamu zákazníků z databáze
     *
     * @return Základní seznam zákazníků (bez speciálních detailních dat)
     */
    @Override
    protected List<Zakaznik> call() {
        LOGGER.info("Načtení seznamu zákazníků bude zahájeno.");
        try {
            nactiSeznamZakazniku();
        } catch (SQLException e) {
            LOGGER.error("Nastala chyba při načítání základního seznamu zákazníků.", e);
            failed();
        }
        return zakaznici;
    }

    private void nactiSeznamZakazniku() throws SQLException {
        try (PreparedStatement psmt = spojeni.prepareStatement(sestavDotaz(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    int radek = rs.getRow();
                    rs.first();
                    while (rs.getRow() <= radek) {
                        Zakaznik z = new Zakaznik();
                        z.setCislo(rs.getString("id"));
                        z.setJmeno(rs.getString("jmeno"));
                        z.setPrijmeni(rs.getString("prijmeni"));
                        z.setNazev(rs.getString("nazev"));
                        z.setIc(rs.getString("ic"));
                        z.setDic(rs.getString("dic"));
                        z.setBlokovan(rs.getBoolean("blokovan"));
                        z.setDuvodBlokace(rs.getString("duvod_blokace"));
                        z.setOdebiraNaDL(rs.getBoolean("odebira_na_dl"));
                        z.setManualniSlevaPovolena(rs.getBoolean("manualni_sleva_povolena"));
                        List<Adresa> adresy = new ArrayList<>();
                        Adresa a = new Adresa();
                        a.setHlavni(true);
                        a.setId(rs.getInt("id_adresy"));
                        a.setDodaci(rs.getBoolean("dodaci"));
                        a.setAdresaDruhyRadek(rs.getString("druhy_radek"));
                        a.setCp(rs.getString("cp"));
                        a.setCor(rs.getString("cor"));
                        a.setUlice(rs.getString("ulice"));
                        a.setMesto(rs.getString("mesto"));
                        a.setPsc(rs.getString("psc"));
                        Stat stat = new Stat();
                        stat.setHlavni(true);
                        stat.setIsoKod(rs.getString("stat"));
                        a.setStat(stat);
                        adresy.add(a);
                        z.setAdresy(adresy);
                        SkupinaSlev skupina = new SkupinaSlev();
                        skupina.setCislo(rs.getInt("skupina_slev"));
                        z.setSkupinaSlev(skupina);
                        zakaznici.add(z);
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
                }
            }
        }
    }

    private String sestavDotaz() throws SQLException {
        StringBuilder dotaz = new StringBuilder("SELECT * FROM " + spojeni.getCatalog() + ".vyhledani_zakaznika");
        List<String> polozky = new ArrayList<>();
        if (filtracniKriteria.getCislo() != null) {
            polozky.add("LOWER(id) like LOWER('%" + filtracniKriteria.getCislo() + "%')");
        }
        if (filtracniKriteria.getJmeno() != null) {
            polozky.add("LOWER(jmeno) like LOWER('%" + filtracniKriteria.getJmeno() + "%')");
        }
        if (filtracniKriteria.getPrijmeni() != null) {
            polozky.add("LOWER(prijmeni) like LOWER('%" + filtracniKriteria.getPrijmeni() + "%')");
        }
        if (filtracniKriteria.getIc() != null) {
            polozky.add("LOWER(ic) like LOWER(%'" + filtracniKriteria.getIc() + "%')");
        }
        if (filtracniKriteria.getDic() != null) {
            polozky.add("LOWER(dic) like LOWER('%" + filtracniKriteria.getDic() + "%')");
        }
        if (filtracniKriteria.getNazev() != null) {
            polozky.add("LOWER(nazev) like LOWER('%" + filtracniKriteria.getNazev() + "%')");
        }
        if (filtracniKriteria.getUlice() != null) {
            polozky.add("LOWER(ulice) like LOWER('%" + filtracniKriteria.getUlice() + "%')");
        }
        if (filtracniKriteria.getCp() != null) {
            polozky.add("LOWER(cp) like LOWER('%" + filtracniKriteria.getCp() + "%')");
        }
        if (filtracniKriteria.getCor() != null) {
            polozky.add("LOWER(cor) like LOWER('%" + filtracniKriteria.getCor() + "%')");
        }
        if (filtracniKriteria.getPsc() != null) {
            polozky.add("LOWER(psc) like LOWER('%" + filtracniKriteria.getPsc() + "%')");
        }
        if (filtracniKriteria.getMesto() != null) {
            polozky.add("LOWER(mesto) like LOWER('%" + filtracniKriteria.getMesto() + "%')");
        }
        polozky.add("blokovan = " + filtracniKriteria.isBlokovany());
        if (!polozky.isEmpty()) {
            dotaz.append(" WHERE ");
            for (int i = 0; i < polozky.size(); i++) {
                if (i > 0) {
                    dotaz.append(" AND ");
                }
                dotaz.append(polozky.get(i));
            }
        }
        if (maxZaznamu > 0) {
            dotaz.append(" LIMIT ").append(maxZaznamu);
        }
        dotaz.append(";");
        return dotaz.toString();
    }
}
