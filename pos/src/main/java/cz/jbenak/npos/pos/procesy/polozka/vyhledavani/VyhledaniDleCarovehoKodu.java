/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesy.polozka.vyhledavani;

import cz.jbenak.npos.pos.objekty.sortiment.CarovyKod;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Proces pro vyhledání registru položky dle zadaného čárového kódu v databázi
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-11
 */
public class VyhledaniDleCarovehoKodu extends Task<Sortiment> {
    
    private static final Logger LOGER = LogManager.getLogger(VyhledaniDleCarovehoKodu.class);
    private final String carovyKod;
    private final Connection spojeni;

    public VyhledaniDleCarovehoKodu(String carovyKod) {
        this.carovyKod = carovyKod;
        spojeni = Pos.getInstance().getSpojeni();
    }

    @Override
    protected Sortiment call() throws Exception {
        LOGER.info("Proběhne vyhledání registračního čísla položky dle zadaného čárového kódu: {}.", carovyKod);
        Sortiment registr = null;
        try {
            registr = vyhledejRegistrDleCK();
        } catch (SQLException e) {
            LOGER.error("Nebylo možné vyhledat registrační číslo v databázi dle čárového kódu: ", e);
            failed();
        }
        return registr;
    }
    
    private Sortiment vyhledejRegistrDleCK() throws SQLException {
        Sortiment registr = null;
        final String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".carove_kody WHERE LOWER(kod) = LOWER(?);";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setString(1, carovyKod);
            ResultSet rs = psmt.executeQuery();
            if(rs != null) {
                if(rs.first()) {
                    registr = new Sortiment();
                    registr.setRegistr(rs.getString("registr"));
                    CarovyKod kod = new CarovyKod();
                    kod.setRegistr(registr.getRegistr());
                    kod.setTyp(CarovyKod.TypCarovehoKodu.valueOf(rs.getString("typ")));
                    kod.setHlavni(rs.getBoolean("hlavni"));
                    kod.setCena(rs.getBigDecimal("cena"));
                    kod.setMnozstvi(rs.getBigDecimal("mnozstvi"));
                    kod.setCenaPozice(rs.getInt("cena_pozice"));
                    kod.setCenaDelka(rs.getInt("cena_delka"));
                    kod.setCenaDesetinne(rs.getInt("cena_desetinnych_mist"));
                    kod.setMnozstviDelka(rs.getInt("mnozstvi_delka"));
                    kod.setMnozstviPozice(rs.getInt("mnozstvi_pozice"));
                    kod.setMnozstviDesetinne(rs.getInt("mnozstvi_desetinnych_mist"));
                    registr.setCarovyKod(kod);
                }
            }
        }
        return registr;
    }
}
