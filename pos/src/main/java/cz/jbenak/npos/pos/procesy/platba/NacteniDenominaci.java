package cz.jbenak.npos.pos.procesy.platba;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.meny.Denominace;
import cz.jbenak.npos.pos.objekty.meny.Mena;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Načte denominační tabulku, de facto výčetku, pro vybrané měny. Jednotlivé výčetky jsou uloženy do odpovídajících měn.
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-01-25
 */
public class NacteniDenominaci extends Task<Void> {

    private static final Logger LOGER = LogManager.getLogger(NacteniDenominaci.class);
    private final List<Mena> vybraneMeny;
    private final List<Denominace> denominace = new ArrayList<>();
    private final Connection spojeni = Pos.getInstance().getSpojeni();

    /**
     * Vytvoří proces načtení denominací.
     * @param vybraneMeny měny, pro něž se má denominační tabulka načíst.
     */
    public NacteniDenominaci(List<Mena> vybraneMeny) {
        this.vybraneMeny = vybraneMeny;
    }

    @Override
    protected Void call() throws Exception {
        Object[] menyKVyhledani = getMenyKVyhledani();
        LOGER.info("Budou načteny výčtové tabulky pro vybrané měny {}.", Arrays.toString(menyKVyhledani));
        String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".denominace WHERE iso_kod_meny = ANY (?) ORDER BY cislo ASC;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setArray(1, spojeni.createArrayOf("varchar", menyKVyhledani));
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    int radek = rs.getRow();
                    rs.first();
                    while (rs.getRow() <= radek) {
                        Denominace d = new Denominace();
                        d.setCislo(rs.getInt("cislo"));
                        d.setTypPlatidla(Denominace.TypPlatidla.valueOf(rs.getString("typ_platidla")));
                        d.setIsoKodMeny(rs.getString("iso_kod_meny"));
                        d.setPoradi(rs.getInt("poradi"));
                        d.setJednotka(rs.getInt("jednotka"));
                        d.setHodnota(rs.getBigDecimal("hodnota"));
                        d.setNazevJednotky(rs.getString("nazev_jednotky"));
                        d.setKsBalicek(rs.getInt("ks_balicek"));
                        d.setAkceptovatelne(rs.getBoolean("akceptovatelne"));
                        d.setZobrazitNaTlacitku(rs.getBoolean("zobrazit_na_tlacitku"));
                        denominace.add(d);
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
                }
            }
            vlozDenominaceDoMeny();
        } catch (Exception e) {
            LOGER.error("Nastala vnitřní chyba v procesu načítání denominací pro vybrané měny.", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Nemohu připravit platbu",
                    "Nastala chyba v procesu načítání denominací pro vybrané měny:\n\n" + e.getLocalizedMessage(),
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

    private void vlozDenominaceDoMeny() {
        vybraneMeny.forEach(mena -> {
            mena.getDenominace().clear();
            denominace.forEach(d -> {
                if (d.getIsoKodMeny().equalsIgnoreCase(mena.getIsoKod())) {
                    mena.getDenominace().add(d);
                }
            });
            mena.getDenominace().sort(Comparator.comparingInt(Denominace::getPoradi));
        });
    }
}
