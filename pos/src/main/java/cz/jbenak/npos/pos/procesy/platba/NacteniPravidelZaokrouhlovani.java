package cz.jbenak.npos.pos.procesy.platba;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.meny.Mena;
import cz.jbenak.npos.pos.objekty.meny.PravidloZaokrouhlovani;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Načte pravidla zaokrouhlování pro vybrané měny.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-05-25
 */
public class NacteniPravidelZaokrouhlovani extends Task<Void> {

    private final static Logger LOGER = LogManager.getLogger(PravidloZaokrouhlovani.class);
    private final Connection spojeni = Pos.getInstance().getSpojeni();
    private final List<PravidloZaokrouhlovani> pravidla = new ArrayList<>();
    private final List<Mena> vybraneMeny;

    public NacteniPravidelZaokrouhlovani(List<Mena> vybraneMeny) {
        this.vybraneMeny = vybraneMeny;
    }

    @Override
    protected Void call() throws Exception {
        Object[] menyKVyhledani = getMenyKVyhledani();
        LOGER.info("Budou načtena pravidla zaokrouhlování pro vybrané měny {}.", Arrays.toString(menyKVyhledani));
        String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".pravidla_zaokrouhlovani WHERE iso_kod_meny = ANY (?) ORDER BY cislo_pravidla ASC;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setArray(1, spojeni.createArrayOf("varchar", menyKVyhledani));
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    int radek = rs.getRow();
                    rs.first();
                    while (rs.getRow() <= radek) {
                        PravidloZaokrouhlovani pravidlo = new PravidloZaokrouhlovani();
                        pravidlo.setCisloPravidla(rs.getInt("cislo_pravidla"));
                        pravidlo.setIdPlatebnihoProstredku(rs.getString("id_platebniho_prostredku"));
                        pravidlo.setIsoKodMeny(rs.getString("iso_kod_meny"));
                        pravidlo.setHodnotaOd(rs.getBigDecimal("hodnota_od"));
                        pravidlo.setHodnotaDo(rs.getBigDecimal("hodnota_do"));
                        pravidlo.setHodnotaZaokrouhleni(rs.getBigDecimal("hodnota_zaokrouhleni"));
                        pravidlo.setSmer(PravidloZaokrouhlovani.SmerZaokrouhleni.valueOf(rs.getString("smer")));
                        pravidla.add(pravidlo);
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
                }
            }
            vlozPravidlaZaokrouhlovaniDoMeny();
        } catch (Exception e) {
            LOGER.error("Nastala vnitřní chyba v procesu načítání pravidel zaokrouhlování pro vybrané měny.", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Nemohu připravit platbu",
                    "Nastala chyba v procesu načítání pravidel zaokrouhlování pro vybrané měny:\n\n" + e.getLocalizedMessage(),
                    Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            failed();
        }
        return null;
    }

    private Object[] getMenyKVyhledani() {
        Object[] kriteria = new Object[vybraneMeny.size()];
        int index = 0;
        for (Mena m : vybraneMeny) {
            kriteria[index] = m.getIsoKod();
            index++;
        }
        return kriteria;
    }

    private void vlozPravidlaZaokrouhlovaniDoMeny() {
        vybraneMeny.forEach(mena -> {
            mena.getPravidlaZaokrouhlovani().clear();
            pravidla.forEach(pravidlo -> {
                if (pravidlo.getIsoKodMeny().equalsIgnoreCase(mena.getIsoKod())) {
                    mena.getPravidlaZaokrouhlovani().add(pravidlo);
                }
            });
        });
    }
}
