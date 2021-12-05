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
 * Proces pro vyhledání registračního čísla sortimentu dle PLU
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-11
 */
public class VyhledavaniDlePlu extends Task<String> {
    
    private static final Logger LOGER = LogManager.getLogger(VyhledavaniDlePlu.class);
    private final String plu;
    private final Connection spojeni;

    public VyhledavaniDlePlu(String plu) {
        this.plu = plu;
        spojeni = Pos.getInstance().getSpojeni();
    }

    @Override
    protected String call() throws Exception {
        LOGER.info("Proběhne vyhledání registračního čísla položky dle zadaného PLU kódu: {}.", plu);
        String registr = null;
        try {
            registr = vyhledejRegistrDlePLU();
        } catch (SQLException e) {
            LOGER.error("Nebylo možné vyhledat registrační číslo v databázi dle PLU kódu: ", e);
            failed();
        }
        return registr;
    }
    
    private String vyhledejRegistrDlePLU() throws SQLException {
        String registr = null;
        final String dotaz = "SELECT registr FROM " + spojeni.getCatalog() + ".sortiment WHERE LOWER(plu) = LOWER(?);";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setString(1, plu);
            ResultSet rs = psmt.executeQuery();
            if(rs != null) {
                if(rs.first()) {
                    registr = rs.getString(1);
                }
            }
        }
        return registr;
    }
    
}
