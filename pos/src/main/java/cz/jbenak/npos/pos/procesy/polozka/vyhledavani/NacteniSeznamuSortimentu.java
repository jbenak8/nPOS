package cz.jbenak.npos.pos.procesy.polozka.vyhledavani;

import cz.jbenak.npos.pos.objekty.sortiment.KriteriaVyhledavani;
import cz.jbenak.npos.pos.objekty.sortiment.SeznamRegistrace;
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
 * Proces načtení seznamu sortimentu z databáze na základě zadaných kritérií.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-09-18
 */
public class NacteniSeznamuSortimentu extends Task<List<SeznamRegistrace>> {

    private static final Logger LOGER = LogManager.getLogger(NacteniSeznamuSkupinSortimentu.class);
    private final List<SeznamRegistrace> data = new ArrayList<>();
    private final Connection spojeni = Pos.getInstance().getSpojeni();
    private final KriteriaVyhledavani kriteria;

    /**
     * Konstruktor procesu
     *
     * @param kriteria vyhledávací kritéria.
     */
    public NacteniSeznamuSortimentu(KriteriaVyhledavani kriteria) {
        this.kriteria = kriteria;
    }

    /**
     * Spustí proces vyhledávání.
     *
     * @return načtené položky seznamu sortimentu.
     */
    @Override
    protected List<SeznamRegistrace> call() {
        LOGER.info("Vyhledávání položek sortimentu dle zahájených kritérií bylo zahájeno.");
        try {
            nactiData();
        } catch (SQLException e) {
            LOGER.error("Nebylo možné vyhledat položky sortimentu v databázi: ", e);
            failed();
        }
        return data;
    }

    private void nactiData() throws SQLException {
        try (PreparedStatement psmt = spojeni.prepareStatement(sestavDotaz(), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    int radek = rs.getRow();
                    rs.first();
                    while (rs.getRow() <= radek) {
                        SeznamRegistrace polozka = new SeznamRegistrace();
                        polozka.setRegistr(rs.getString("registr"));
                        polozka.setNazev(rs.getString("nazev"));
                        polozka.setCena(rs.getBigDecimal("jednotkova_cena"));
                        polozka.setIdSkupiny(rs.getString("skupina"));
                        polozka.setNazevSkupiny(rs.getString("nazev_skupiny"));
                        data.add(polozka);
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
        StringBuilder dotaz = new StringBuilder("SELECT * FROM " + spojeni.getCatalog() + ".seznam_sortimentu");
        List<String> polozky = new ArrayList<>();
        if (kriteria.getRegistr() != null) {
            polozky.add("LOWER(registr) like LOWER('%" + kriteria.getRegistr() + "%')");
        }
        if (kriteria.getSkupina() != null && kriteria.getSkupina().getId() != null) {
            polozky.add("LOWER(skupina) like LOWER('%" + kriteria.getSkupina().getId() + "%')");
        }
        if (kriteria.getSkupina() != null && kriteria.getSkupina().getNazev() != null) {
            polozky.add("LOWER(nazev_skupiny) like LOWER('%" + kriteria.getSkupina().getNazev() + "%')");
        }
        if (kriteria.getCarovyKod() != null) {
            polozky.add("LOWER(kod) like LOWER('%" + kriteria.getCarovyKod() + "%')");
        }
        if (kriteria.getCenaOd() != null) {
            polozky.add("jednotkova_cena >= " + kriteria.getCenaOd());
        }
        if (kriteria.getCenaDo() != null) {
            polozky.add("jednotkova_cena <= " + kriteria.getCenaDo());
        }
        if (kriteria.isJenNeblokovane()) {
            polozky.add("prodej_povolen = true");
        }
        if (!polozky.isEmpty()) {
            dotaz.append(" WHERE ");
            for (int i = 0; i < polozky.size(); i++) {
                if (i > 0) {
                    dotaz.append(" AND ");
                }
                dotaz.append(polozky.get(i));
            }
        }
        if (kriteria.getMaxZazanmu() > 0) {
            dotaz.append(" LIMIT ").append(kriteria.getMaxZazanmu());
        }
        dotaz.append(";");
        return dotaz.toString();
    }
}
