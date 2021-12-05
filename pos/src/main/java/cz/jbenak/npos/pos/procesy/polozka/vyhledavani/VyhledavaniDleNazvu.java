/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesy.polozka.vyhledavani;

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
 * Proces pro vyhledání registračního čísla sortimentu dle PLU
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-11
 */
public class VyhledavaniDleNazvu extends Task<List<SeznamRegistrace>> {

    private static final Logger LOGER = LogManager.getLogger(VyhledavaniDleNazvu.class);
    private final String nazev;
    private final Connection spojeni;

    public VyhledavaniDleNazvu(String nazev) {
        this.nazev = nazev;
        spojeni = Pos.getInstance().getSpojeni();
    }

    @Override
    protected List<SeznamRegistrace> call() throws Exception {
        LOGER.info("Proběhne vyhledání registračních čísel položky dle zadaného názvu: {}.", nazev);
        List<SeznamRegistrace> seznam = null;
        try {
            seznam = vyhledejRegistrDleNazvu();
        } catch (SQLException e) {
            LOGER.error("Nebylo možné vyhledat registrační čísla v databázi dle názvu: ", e);
            failed();
        }
        return seznam;
    }

    private List<SeznamRegistrace> vyhledejRegistrDleNazvu() throws SQLException {
        List<SeznamRegistrace> seznam = null;
        final String dotaz = "SELECT sortiment.registr AS reg, skupiny_sortimentu.id AS sk_id, skupiny_sortimentu.nazev AS sk_naz, "
                + "sortiment.nazev AS naz, sortiment.jednotkova_cena AS cena FROM " + spojeni.getCatalog() + ".sortiment "
                + "INNER JOIN " + spojeni.getCatalog() + ".skupiny_sortimentu ON sortiment.skupina = skupiny_sortimentu.id"
                + " WHERE LOWER(sortiment.nazev) LIKE LOWER('%" + nazev + "%');";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    int radek = rs.getRow();
                    seznam = new ArrayList();
                    rs.first();
                    while (rs.getRow() <= radek) {
                        SeznamRegistrace polozka = new SeznamRegistrace();
                        polozka.setRegistr(rs.getString("reg"));
                        polozka.setIdSkupiny(rs.getString("sk_id"));
                        polozka.setNazevSkupiny(rs.getString("sk_naz"));
                        polozka.setNazev(rs.getString("naz"));
                        polozka.setCena(rs.getBigDecimal("cena"));
                        seznam.add(polozka);
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
                }
            }
        }
        return seznam;
    }

}
