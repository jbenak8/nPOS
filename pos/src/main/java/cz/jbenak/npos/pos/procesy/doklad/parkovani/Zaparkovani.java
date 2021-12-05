package cz.jbenak.npos.pos.procesy.doklad.parkovani;

import cz.jbenak.npos.pos.objekty.doklad.Doklad;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Třída pro vlastní zaparkování předaného dokladu (resp. uložení jako rozpracovaného do lokální databáze).
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-25
 */
public class Zaparkovani extends Task<Boolean> {

    private static final Logger LOGER = LogManager.getLogger(Zaparkovani.class);
    private final Connection spojeni;
    private final Doklad doklad;

    /**
     * Konstruktor třídy procesu zaparkování dokladu.
     * @param doklad doklad, co se má zaparkovat.
     */
    public Zaparkovani(Doklad doklad) {
        this.doklad = doklad;
        this.spojeni = Pos.getInstance().getSpojeni();
    }

    @Override
    protected Boolean call() throws Exception {
        boolean ok = false;
        LOGER.info("Nyní bude zaparkován lokálně doklad s ID {}.", doklad.getId());
        try {
            ok = provedZaparkovani();
        } catch (SQLException e) {
            LOGER.error("Nastala chyba při lokálním parkování dokladu s ID {}: ", doklad.getId(), e);
            failed();
        }
        return ok;
    }

    private boolean provedZaparkovani() throws SQLException {
        boolean zaparkovano = false;
        final String dotaz = "INSERT INTO " + spojeni.getCatalog() + ".parkoviste (uid, vytvoreno, doklad) VALUES (?, ?, ?);";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, Statement.RETURN_GENERATED_KEYS)) {
            psmt.setObject(1, doklad.getId());
            psmt.setDate(2, new Date(System.currentTimeMillis()));
            psmt.setBytes(3, SerializationUtils.serialize(doklad));
            int radekVlozeno = psmt.executeUpdate();
            if (radekVlozeno != 1) {
                LOGER.error("Nastal problém při vkládání dokladu k zaparkování do databáze. Pocet vložených řádek je {}.", radekVlozeno);
                failed();
            } else {
                LOGER.info("Zaparkovaný doklad byl uložen do databáze.");
                zaparkovano = true;
            }
        }
        return zaparkovano;
    }
}
