package cz.jbenak.npos.pos.procesy.platba;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.platba.PlatebniProstredek;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída procesu pro načtení dostupných platebních prostředků.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-10-11
 */
public class NacteniPlatebnichProstredku extends Task<List<PlatebniProstredek>> {

    private static final Logger LOGER = LogManager.getLogger(NacteniPlatebnichProstredku.class);
    private final List<PlatebniProstredek> data = new ArrayList<>();
    private final Connection spojeni = Pos.getInstance().getSpojeni();
    private final boolean jenPlatne;

    /**
     * Konstruktor procesu pro načtení platebních prostředků z databáze.
     *
     * @param jenPlatne určí, zda načíst všechny platební prostředky (false), nebo jen akceptovatelné (true).
     */
    public NacteniPlatebnichProstredku(boolean jenPlatne) {
        this.jenPlatne = jenPlatne;
    }

    @Override
    protected List<PlatebniProstredek> call() throws Exception {
        LOGER.info("Budou načteny {} platební prostředky z databáze.", jenPlatne ? "pouze platné" : "všechny");
        String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".platebni_prostredky WHERE akceptovatelny = ? ORDER BY id ASC;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setBoolean(1, jenPlatne);
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    int radek = rs.getRow();
                    rs.first();
                    while (rs.getRow() <= radek) {
                        PlatebniProstredek pp = new PlatebniProstredek();
                        pp.setId(rs.getString("id"));
                        pp.setTyp(PlatebniProstredek.TypPlatebnihoProstredku.valueOf(rs.getString("typ")));
                        pp.setAkceptovatelny(rs.getBoolean("akceptovatelny"));
                        pp.setOznaceni(rs.getString("oznaceni"));
                        pp.setCiziMenaPovolena(rs.getBoolean("cizi_mena_povolena"));
                        pp.setZaokrouhlitDleDenominace(rs.getBoolean("zaokrouhlit_dle_denominace"));
                        pp.setVratkaOk(rs.getBoolean("povoleno_pro_vratku"));
                        pp.setStornoOk(rs.getBoolean("povoleno_pro_storno"));
                        pp.setEvidenceVZasuvce(rs.getBoolean("evidence_v_zasuvce"));
                        pp.setEvidenceVTrezoru(rs.getBoolean("evidence_v_trezoru"));
                        pp.setOtevritZasuvku(rs.getBoolean("otevrit_zasuvku"));
                        pp.setPocitaniNutne(rs.getBoolean("pocitani_nutne"));
                        pp.setMinCastkaPrijem(rs.getBigDecimal("miminalni_castka") == null ? new BigDecimal(-1): rs.getBigDecimal("miminalni_castka"));
                        pp.setMaxCastkaPrijem(rs.getBigDecimal("maximalni_castka") == null ? new BigDecimal(-1): rs.getBigDecimal("maximalni_castka"));
                        data.add(pp);
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
                }
            }
        } catch (Exception e) {
            LOGER.error("Nastala vnitřní chyba v procesu načítání platebních prostředků.", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Nemohu načíst platební prostředky",
                    "Nastala chyba v procesu načítání platebních prostředků:\n\n" + e.getLocalizedMessage(),
                    Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            failed();
        }
        return data;
    }
}
