/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesy.doklad.parkovani;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.system.Pos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Proces pro kontrolu lokálně zaparkovaného dokladu.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-11
 */
public class KontrolaParkovani extends Task<Boolean> {

    private static final Logger LOGER = LogManager.getLogger(KontrolaParkovani.class);
    private final Connection spojeni;

    public KontrolaParkovani() {
        this.spojeni = Pos.getInstance().getSpojeni();
    }

    @Override
    protected Boolean call() throws Exception {
        LOGER.info("Zkontroluje se přítomnost zaparkovaného dokladu.");
        boolean zaparkovano = false;
        try {
            final String dotaz = "SELECT COUNT(*) FROM " + spojeni.getCatalog() + ".parkoviste;";
            try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                ResultSet rs = psmt.executeQuery();
                if (rs != null) {
                    if (rs.first()) {
                        zaparkovano = (rs.getInt(1) == 1);
                    }
                }
            }
            LOGER.info("Doklad {} zaparkován.", zaparkovano ? "je" : "není");
        } catch (SQLException e) {
            LOGER.error("Nebylo možno zkontrolovat, zdali se v databázi nachází zaparkovaný doklad: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nebylo možno zkontrolovat, zdali se v databázi nachází zaparkovaný doklad: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
            failed();
        }
        return zaparkovano;
    }

}
