package cz.jbenak.npos.pos.procesy.platba;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.meny.Kurz;
import cz.jbenak.npos.pos.objekty.meny.Mena;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NacteniKurzuMen extends Task<Void> {

    private final static Logger LOGER = LogManager.getLogger(NacteniKurzuMen.class);
    private final List<Kurz> kurzy = new ArrayList<>();
    private final Connection spojeni = Pos.getInstance().getSpojeni();
    private final List<Mena> vybraneMeny;
    private final boolean jenPlatne;

    public NacteniKurzuMen(List<Mena> vybraneMeny, boolean jenPlatne) {
        this.vybraneMeny = vybraneMeny;
        this.jenPlatne = jenPlatne;
    }

    @Override
    protected Void call() throws Exception {
        Object[] menyKVyhledani = getMenyKVyhledani();
        LOGER.info("Budou načteny výčtové {} kurzy pro vybrané měny {}.", jenPlatne ? "pouze platné" : "", Arrays.toString(menyKVyhledani));
        String dotaz = jenPlatne ? "SELECT * FROM " + spojeni.getCatalog()
                + ".kurzy_men WHERE iso_kod_meny = ANY (?) AND platnost_od <= ? " +
                "AND (platnost_do >= ? OR platnost_do IS NULL) ORDER BY cislo_kurzu ASC;"
                : "SELECT * FROM " + spojeni.getCatalog() + ".kurzy_men WHERE iso_kod_meny = ANY (?) ORDER BY cislo_kurzu ASC;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setArray(1, spojeni.createArrayOf("varchar", menyKVyhledani));
            if (jenPlatne) {
                LocalDate dnes = LocalDate.now();
                psmt.setDate(2, Date.valueOf(dnes));
                psmt.setDate(3, Date.valueOf(dnes));
            }
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    int radek = rs.getRow();
                    rs.first();
                    while (rs.getRow() <= radek) {
                        Kurz kurzMeny = new Kurz();
                        kurzMeny.setCisloKurzu(rs.getInt("cislo_kurzu"));
                        kurzMeny.setIsoKodMeny(rs.getString("iso_kod_meny"));
                        if (rs.getDate("platnost_od") != null) {
                            kurzMeny.setPlatnostOd(rs.getDate("platnost_od").toLocalDate());
                        }
                        if (rs.getDate("platnost_do") != null) {
                            kurzMeny.setPlatnostDo(rs.getDate("platnost_do").toLocalDate());
                        }
                        kurzMeny.setNakup(rs.getBigDecimal("nakup"));
                        kurzMeny.setProdej(rs.getBigDecimal("prodej"));
                        kurzy.add(kurzMeny);
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
                }
            }
            vlozKurzyDoMeny();
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
        Object[] kriteria = new Object[vybraneMeny.size() - 1];
        int index = 0;
        for (Mena m : vybraneMeny) {
            if (!m.isKmenova()) {
                kriteria[index] = m.getIsoKod();
                index++;
            }
        }
        return kriteria;
    }

    private void vlozKurzyDoMeny() {
        vybraneMeny.forEach(mena -> {
            mena.getKurzy().clear();
            kurzy.forEach(kurz -> {
                if (kurz.getIsoKodMeny().equalsIgnoreCase(mena.getIsoKod())) {
                    mena.getKurzy().add(kurz);
                }
            });
        });
    }
}
