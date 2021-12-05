/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesy.polozka.vyhledavani;

import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Proces pro kontrolu lokálně zaparkovaného dokladu.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-11
 */
public class KontrolaRegistru extends Task<Boolean> {

    private static final Logger LOGER = LogManager.getLogger(KontrolaRegistru.class);
    private final Connection spojeni;
    private final String registr;

    public KontrolaRegistru(String registr) {
        this.registr = registr;
        this.spojeni = Pos.getInstance().getSpojeni();
    }

    @Override
    protected Boolean call() throws Exception {
        boolean existuje = false;
        try {
            final String dotaz = "SELECT COUNT(*) FROM " + spojeni.getCatalog() + ".sortiment WHERE LOWER(registr) = LOWER(?);";
            try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                psmt.setString(1, registr);
                ResultSet rs = psmt.executeQuery();
                if (rs != null) {
                    if (rs.first()) {
                        existuje = (rs.getInt(1) == 1);
                    }
                }
            }
        } catch (SQLException e) {
            LOGER.error("Nebylo možno zkontrolovat, zdali se v databázi nachází sortiment se zadaným registračním číslem {}: ", registr, e);
            failed();
        }
        return existuje;
    }

}
