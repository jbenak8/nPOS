package cz.jbenak.npos.pos.procesy.doklad.parkovani;

import cz.jbenak.npos.pos.objekty.doklad.Doklad;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;

/**
 * Třída pro provedení procesu odparkování dokladu.
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-25
 */
public class Odparkovani extends Task<Doklad> {

    private final static Logger LOGER = LogManager.getLogger(Odparkovani.class);
    private final Connection spojeni;

    Odparkovani() {
        spojeni = Pos.getInstance().getSpojeni();
    }

    @Override
    protected Doklad call() throws Exception {
        Doklad doklad = nactiZaparkovanyDoklad();
        if(doklad == null) {
            LOGER.error("Nepodařilo se načíst zaparkovaný doklad.", getException());
            failed();
            return null;
        } else if(!smazParkoviste()) {
            LOGER.error("Nepodařilo se vymazat úložiště zaparkovaných dokladů.", getException());
            failed();
            return null;
        } else {
            LOGER.info("Doklad s interním ID {} byl úspěšně načten.", doklad.getId());
            return doklad;
        }
    }

    private Doklad nactiZaparkovanyDoklad() throws SQLException {
        LOGER.debug("Bude načten zaparkovaný doklad.");
        Doklad nactenyDoklad = null;
        final String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".parkoviste;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, TYPE_SCROLL_SENSITIVE, CONCUR_READ_ONLY)) {
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    if (rs.getRow() > 1) {
                        failed();
                    } else {
                        rs.first();
                        nactenyDoklad = (Doklad) SerializationUtils.deserialize(rs.getBytes("doklad"));
                    }
                }
            }
        }
        return nactenyDoklad;
    }

    private boolean smazParkoviste() throws SQLException {
        boolean smazano = false;
        LOGER.debug("Lokální úložiště zaparkovaných dokladů bude vymazáno.");
        final String dotaz = "DELETE FROM " + spojeni.getCatalog() + ".parkoviste;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, Statement.RETURN_GENERATED_KEYS)) {
            int radek = psmt.executeUpdate();
            if (radek == 1) {
                smazano = true;
            }
        }
        return smazano;
    }
}
