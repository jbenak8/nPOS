/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesy.prihlaseni.lokalniParkovani;

import cz.jbenak.npos.pos.system.Pos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Proces pro zkontrolování, jestli existuje lokálně zaparkovaný doklad.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-05
 */
public class ZkontrolujZaparkovanyDoklad {

    private static final Logger LOGER = LogManager.getLogger(ZkontrolujZaparkovanyDoklad.class);
    private final Connection SPOJENI = Pos.getInstance().getSpojeni();

    /**
     * Zjistí, zdali v databázi existuje zaparkovaný doklad (právě jeden!).
     *
     * @return existence zaprakovaného dokladu.
     * @throws Exception Buď chyba databázového dotazování (SQLException), nebo
     * výjima, že existuje více dokladů.
     */
    public boolean existujeZaparkovanyDoklad() throws Exception {
        LOGER.debug("Bude zjištěno, zdali existuje zaparkovaný doklad.");
        boolean existuje = false;
        boolean vice = false;
        final String dotaz = "SELECT COUNT(*) FROM " + SPOJENI.getCatalog() + ".parkoviste;";
        try (PreparedStatement psmt = SPOJENI.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.first()) {
                    if (rs.getInt(1) == 1) {
                        existuje = true;
                    } else if (rs.getInt(1) > 1) {
                        vice = true;
                    }
                }
            }
        }
        if (vice) {
            throw new Exception("Existuje více než jeden zaprakovaný doklad!");
        }
        LOGER.info("Zapradkovaný doklad existuje: " + existuje);
        return existuje;
    }

}
