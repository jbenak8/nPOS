package cz.jbenak.npos.pos.procesy.prihlaseni;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.progres.ProgresDialog;
import cz.jbenak.npos.pos.objekty.sezeni.Pokladni;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class OdemceniPokladny extends Task<OdemceniPokladny.StavOdemceni> {

    public enum StavOdemceni {
        MUZE_SE_ODEMKNOUT, SPATNE_HESLO, UZIVATEL_ZABLOKOVAN
    }

    private final static Logger LOGER = LogManager.getLogger(OdemceniPokladny.class);
    private final int heslo;
    private final Pokladni uzivatel;
    private final Connection spojeni;
    private final ProgresDialog progres;

    public OdemceniPokladny(Pokladni uzivatel, int heslo) {
        this.heslo = heslo;
        this.uzivatel = uzivatel;
        this.spojeni = Pos.getInstance().getSpojeni();
        this.progres = new ProgresDialog(Pos.getInstance().getAplikacniOkno());
    }

    @Override
    protected StavOdemceni call() throws Exception {
        StavOdemceni stav;
        LOGER.info("Bude zkontrolováno heslo pro uživatele s ID {}.", uzivatel.getId());
        progres.zobrazProgres("Ověřuje se zadané heslo pro doemčení právě příhlášeného uživatele s uživatelským ID {} v databázi.", new Object[]{uzivatel.getId()});
        LOGER.debug("Pokladní se zadaným uživatelským ID {} byl nalezen v databázi. Ověří se platnost.", uzivatel.getId());
        zkontrolujHeslo();
        if (!uzivatel.isHesloPlatne()) {
            snizPocetPokusu();
            if (uzivatel.getPocetPrihlasovacichPokusu() == 0) {
                zablokujUzivatele();
                stav = StavOdemceni.UZIVATEL_ZABLOKOVAN;
            } else {
                LOGER.warn("Uživatel {} zadal špatné heslo pro odemčení pokladny.", uzivatel.getId());
                stav = StavOdemceni.SPATNE_HESLO;
            }
        } else {
            if (uzivatel.getPocetPrihlasovacichPokusu() < PrihlaseniPokladni.MAX_POCET_POKUSU) {
                resetPocetPokusu();
            }
            LOGER.info("Uživatel {} zadal správné heslo pro odemčení pokladny.", uzivatel.getId());
            stav = StavOdemceni.MUZE_SE_ODEMKNOUT;
        }
        return stav;
    }


    @Override
    protected void done() {
        LOGER.info("Ověřovací proces pokladního byl ukončen.");
        progres.zavriDialog();
    }

    private void zkontrolujHeslo() throws SQLException {
        final String dotaz = "SELECT COUNT(*) FROM " + spojeni.getCatalog() + ".uzivatele WHERE id_uzivatele = ? AND heslo = crypt(?, '$1$p.t2nIOB');";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setInt(1, uzivatel.getId());
            psmt.setString(2, Integer.toString(heslo));
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.first()) {
                    uzivatel.setHesloPlatne(rs.getInt(1) == 1);
                }
            }
        }
    }

    private void snizPocetPokusu() throws Exception {
        LOGER.warn("Zadané heslo pokladního s uživatelským ID {} není správné. Počet pokusů bude snížen o 1", uzivatel.getId());
        uzivatel.setPocetPrihlasovacichPokusu(uzivatel.getPocetPrihlasovacichPokusu() - 1);
        final String dotaz = "UPDATE " + spojeni.getCatalog() + ".uzivatele SET pocet_prihlasovacich_pokusu = ? WHERE id_uzivatele = ?;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, Statement.RETURN_GENERATED_KEYS)) {
            psmt.setInt(1, uzivatel.getPocetPrihlasovacichPokusu());
            psmt.setInt(2, uzivatel.getId());
            if (psmt.executeUpdate() != 1) {
                throw new Exception("Aktualizační dotaz vrátil nesprávné hodnoty.");
            }
        }
    }

    private void zablokujUzivatele() throws Exception {
        LOGER.warn("Počet přihlašovacích pokusů pokladního s uživatelským ID {} byl vyčerpán. Uživatel bude zablokován.", uzivatel.getId());
        uzivatel.setBlokovany(true);
        final String dotaz = "UPDATE " + spojeni.getCatalog() + ".uzivatele SET blokovany = true WHERE id_uzivatele = ?;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, Statement.RETURN_GENERATED_KEYS)) {
            psmt.setInt(1, uzivatel.getId());
            if (psmt.executeUpdate() != 1) {
                throw new Exception("Aktualizační dotaz vrátil nesprávné hodnoty.");
            }
        }
    }

    private void resetPocetPokusu() throws Exception {
        LOGER.info("Pokladní s uživatelským ID {} již zadal správné heslo. Počet pokusů bude resetován na {}.", uzivatel.getId(), PrihlaseniPokladni.MAX_POCET_POKUSU);
        uzivatel.setPocetPrihlasovacichPokusu(PrihlaseniPokladni.MAX_POCET_POKUSU);
        final String dotaz = "UPDATE " + spojeni.getCatalog() + ".uzivatele SET pocet_prihlasovacich_pokusu = ? WHERE id_uzivatele = ?;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, Statement.RETURN_GENERATED_KEYS)) {
            psmt.setInt(1, PrihlaseniPokladni.MAX_POCET_POKUSU);
            psmt.setInt(2, uzivatel.getId());
            if (psmt.executeUpdate() != 1) {
                throw new Exception("Aktualizační dotaz vrátil nesprávné hodnoty.");
            }
        }
    }
}
