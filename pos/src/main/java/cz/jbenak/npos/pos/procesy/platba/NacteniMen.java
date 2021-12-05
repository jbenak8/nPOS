package cz.jbenak.npos.pos.procesy.platba;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.meny.Mena;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Načte dostupné měny z databáze.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-01-25
 */
public class NacteniMen extends Task<List<Mena>> {

    private final static Logger LOGER = LogManager.getLogger(NacteniMen.class);
    private final boolean jenAkceptovatelne;
    List<Mena> meny = new ArrayList<>();
    private final Connection spojeni = Pos.getInstance().getSpojeni();

    /**
     * Připraví proces
     *
     * @param jenAkceptovatelne mačíst pouze akceptovatelné měny nebo všechny
     */
    public NacteniMen(boolean jenAkceptovatelne) {
        this.jenAkceptovatelne = jenAkceptovatelne;
    }

    /**
     * Provede načtení seznamu měn.
     *
     * @return vybrané měny.
     * @throws Exception chyba při načítání.
     */
    @Override
    protected List<Mena> call() throws Exception {
        LOGER.info("Budou načteny {} měny z databáze.", jenAkceptovatelne ? "pouze akceptovatelné" : "všechny");
        String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".mena " + (jenAkceptovatelne ? "WHERE akceptovatelna = true" : "") + " ORDER BY iso_kod ASC;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    int radek = rs.getRow();
                    rs.first();
                    while (rs.getRow() <= radek) {
                        Mena m = new Mena();
                        m.setIsoKod(rs.getString("iso_kod"));
                        m.setOznaceni(rs.getString("oznaceni"));
                        m.setNarodniSymbol(rs.getString("narodni_symbol"));
                        m.setAkceptovatelna(rs.getBoolean("akceptovatelna"));
                        m.setKmenova(rs.getBoolean("kmenova"));
                        meny.add(m);
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
                }
            }
        } catch (Exception e) {
            LOGER.error("Nastala vnitřní chyba v procesu načítání měn.", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Nemohu načíst měny",
                    "Nastala chyba v procesu načítání měn:\n\n" + e.getLocalizedMessage(),
                    Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            failed();
        }
        return meny;
    }
}
