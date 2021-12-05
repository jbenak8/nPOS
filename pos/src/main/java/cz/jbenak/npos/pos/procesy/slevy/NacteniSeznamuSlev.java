package cz.jbenak.npos.pos.procesy.slevy;

import cz.jbenak.npos.pos.objekty.slevy.Pravidlo;
import cz.jbenak.npos.pos.objekty.slevy.SkupinaSlev;
import cz.jbenak.npos.pos.objekty.slevy.Sleva;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static cz.jbenak.npos.pos.objekty.slevy.SkupinaSlev.OkamzikUplatneni;
import static cz.jbenak.npos.pos.objekty.slevy.SkupinaSlev.TypSkupinySlev;

/**
 * Provede načtení seznamu platných volných skupin slev pro zobrazení v GUI a možnosti následného výběru.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-01-23
 */
public class NacteniSeznamuSlev extends Task<List<SkupinaSlev>> {

    private static final Logger LOGER = LogManager.getLogger(NacteniSeznamuSlev.class);
    private final Connection spojeni = Pos.getInstance().getSpojeni();
    private final List<SkupinaSlev> skupiny = new ArrayList<>();
    private final boolean platba;

    public NacteniSeznamuSlev(boolean platba) {
        this.platba = platba;
    }

    @Override
    protected List<SkupinaSlev> call() throws Exception {
        LOGER.info("Načtení seznamu platných volných skupin slev bylo zahájeno");
        try {
            nactiSkupiny();
            if (!skupiny.isEmpty()) {
                skupiny.forEach(sk -> {
                    try {
                        LOGER.debug("Načítám pravidla skupiny slev pro skupinu č. {}.", sk.getCislo());
                        nactiPravida(sk);
                    } catch (SQLException ex) {
                        LOGER.error("Nepodařilo se načíst pravidlo pro skupinu slev č. {}: ", sk.getCislo(), ex);
                        failed();
                    }
                });
            }
        } catch (Exception e) {
            LOGER.error("Nastala chyba při načítání seznamu platných skupin slev.");
            failed();
        }
        return skupiny;
    }

    private void nactiSkupiny() throws SQLException {
        String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".skupiny_slev WHERE rozsah_pouziti = ? AND okamzik_uplatneni = ? AND platne = ? AND typ_skupiny_slev = ANY (?) ORDER BY cislo ASC;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setString(1, SkupinaSlev.RozsahPouziti.VZDY.name());
            psmt.setString(2, platba ? OkamzikUplatneni.PLATBA.name() : OkamzikUplatneni.REGISTRACE.name());
            psmt.setBoolean(3, true);
            psmt.setArray(4, spojeni.createArrayOf("varchar", platba
                    ? new Object[]{TypSkupinySlev.NA_DOKLAD.name()} : new Object[]{TypSkupinySlev.NA_DOKLAD.name(), TypSkupinySlev.NA_REGISTR.name()}));
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    int radek = rs.getRow();
                    rs.first();
                    while (rs.getRow() <= radek) {
                        SkupinaSlev skupina = new SkupinaSlev();
                        skupina.setCislo(rs.getInt("cislo"));
                        skupina.setNazev(rs.getString("nazev"));
                        skupina.setHodnotaSlevy(rs.getBigDecimal("hodnota_slevy"));
                        if (rs.getString("id_cile_slevy") != null) {
                            skupina.setIdCileSlevy(rs.getString("id_cile_slevy"));
                        }
                        skupina.setOkamzikUplatneni(SkupinaSlev.OkamzikUplatneni.valueOf(rs.getString("okamzik_uplatneni")));
                        skupina.setTyp(SkupinaSlev.TypSkupinySlev.valueOf(rs.getString("typ_skupiny_slev")));
                        skupina.setRozsahPouziti(SkupinaSlev.RozsahPouziti.VZDY);
                        skupina.setTypSlevy(Sleva.TypSlevy.valueOf(rs.getString("typ_slevy")));
                        skupiny.add(skupina);
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
                }
            }
        }
    }

    private void nactiPravida(SkupinaSlev skupinaSlev) throws SQLException {
        LOGER.debug("Načítám pravidla pro skupinu slev č. {}.", skupinaSlev.getCislo());
        String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".pravidla_skupin_slev WHERE cislo_skupiny_slev = ? ORDER BY poradove_cislo ASC;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setInt(1, skupinaSlev.getCislo());
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    int radek = rs.getRow();
                    rs.first();
                    while (rs.getRow() <= radek) {
                        Pravidlo p = new Pravidlo(rs.getInt("cislo_pravidla"), rs.getInt("poradova_cislo"),
                                Pravidlo.Operator.valueOf(rs.getString("operator")), Pravidlo.TypPodminky.valueOf(rs.getString("typ_podminky")));
                        p.setHodnotaPodminky(rs.getBigDecimal("hodnota_podminky"));
                        if (rs.getString("id_polozky_podminky") != null) {
                            p.setIdObjektuPodminky(rs.getString("id_polozky_podminky"));
                        }
                        skupinaSlev.getPravidla().add(p);
                        if (rs.isLast()) {
                            break;
                        }
                    }
                }
            }
        }
    }
}
