package cz.jbenak.npos.pos.procesy.prihlaseni;

import cz.jbenak.npos.pos.objekty.filialka.Smena;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Provede načtení otevřené pracovní směny na pokladně z lokální databáze.
 * Tato směna je pak předána pro uložení do instance hlavního okna po přihlášení, nebo dalšímu zpracování.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-02-05
 */
public class NacteniSmeny extends Task<Smena> {

    private static final Logger LOGER = LogManager.getLogger(NacteniSmeny.class);
    private final Connection spojeni = Pos.getInstance().getSpojeni();

    @Override
    protected Smena call() throws Exception {
        LOGER.info("Bude načtena aktivní směna z lokální databáze.");
        final String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".smeny WHERE otevrena = true AND pokladna = ? AND filialka = ? AND datum_uzavreni IS NULL;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setInt(1, Pos.getInstance().getCisloPokladny());
            psmt.setString(2, Pos.getInstance().getFilialka().getOznaceni());
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    int radek = rs.getRow();
                    if (radek > 1) {
                        LOGER.error("Bylo načteno pro aktuální pokladnu ({}) vice než jedna otevřená směna! Počet načtených: {}. Zkontrolujte systém!", Pos.getInstance().getCisloPokladny(), radek);
                        return null;
                    } else {
                        rs.first();
                        Smena smena = new Smena();
                        smena.setCisloSmeny(rs.getString("cislo_smeny"));
                        smena.setOtevrena(rs.getBoolean("otevrena"));
                        smena.setFilialka(Pos.getInstance().getFilialka());
                        smena.setPokladna(rs.getInt("pokladna"));
                        smena.setDatumOtevreni(rs.getTimestamp("datum_otevreni"));
                        return smena;
                    }
                }
            }
        }
        return null;
    }
}
