package cz.jbenak.npos.pos.procesy.polozka.vyhledavani;

import cz.jbenak.npos.pos.objekty.sortiment.SkupinaSortimentu;
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
 * Proces pro načtení seznamu skupin sortimentu. Zejména lze použít ve vyhledávacích dialozích zaměřených na sortiment.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-09-16
 */
public class NacteniSeznamuSkupinSortimentu extends Task<List<SkupinaSortimentu>> {

    private final static Logger LOGER = LogManager.getLogger(NacteniSeznamuSkupinSortimentu.class);
    private final List<SkupinaSortimentu> seznam = new ArrayList<>();
    private final Connection spojeni = Pos.getInstance().getSpojeni();
    private String id;
    private String nazev;

    /**
     * Nastaví ID vyhledávané skupiny.
     *
     * @param id ID vyhledáváci skupiny. Lze částečné, není povinné.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Určí název vyhledávané skupiny.
     *
     * @param nazev název vyhledávané skupiny. Opět lze pouze část a není povinný.
     */
    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    /**
     * Spustí vlastní proces vyhledávání.
     *
     * @return seznam nalezených skupin sortimentu.
     * @see SkupinaSortimentu
     */
    @Override
    protected List<SkupinaSortimentu> call() {
        LOGER.info("Proces načtení skupin sortimentu byl spuštěn. Hodnoty pro filtr jsou ID '{}', NAZEV '{}'.", id, nazev);
        try {
            nactiData();
        } catch (SQLException e) {
            LOGER.error("Nastala chyba při načítání seznamu skupin sortimentu.", e);
            failed();
        }
        return seznam;
    }

    private void nactiData() throws SQLException {
        int radek = 0;
        String dotaz = "SELECT id, nazev, id_nadrazene_skupiny, sleva_povolena, max_sleva FROM " + spojeni.getCatalog() + ".skupiny_sortimentu";
        if(id != null || nazev != null) {
            dotaz += " WHERE ";
            if (id != null) {
                dotaz += "LOWER(id) like LOWER('%" + id + "%')";
            }
            if(id != null && nazev != null) {
                dotaz += " AND ";
            }
            if (nazev != null) {
                dotaz += "LOWER(nazev) like LOWER('%" + nazev + "%')";
            }
        }
        dotaz +=";";
        try(PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    radek = rs.getRow();
                    rs.first();
                    while (rs.getRow() <= radek) {
                        SkupinaSortimentu skupina  = new SkupinaSortimentu();
                        skupina.setId(rs.getString("id"));
                        skupina.setNazev(rs.getString("nazev"));
                        skupina.setIdNadrazeneSkupiny(rs.getString("id_nadrazene_skupiny"));
                        skupina.setSlevaPovolena(rs.getBoolean("sleva_povolena"));
                        skupina.setMaxSleva(rs.getBigDecimal("max_sleva"));
                        seznam.add(skupina);
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
                }
            }
        }
        LOGER.info("Proces načítání seznamu skupin sortimentu byl dokončen. Bylo načteno {} položek.", radek);
    }
}
