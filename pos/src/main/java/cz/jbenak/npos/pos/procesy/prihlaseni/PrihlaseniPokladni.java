/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.procesy.prihlaseni;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.progres.ProgresDialog;
import cz.jbenak.npos.pos.objekty.sezeni.Pokladni;
import cz.jbenak.npos.pos.objekty.sezeni.SkupinaUzivatele;
import cz.jbenak.npos.pos.system.Pos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Třída pro provedení přihlášení do hlavní obrazovky.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-03
 */
public class PrihlaseniPokladni extends Task<Pokladni> {

    public final static int MAX_POCET_POKUSU = 3;
    private final static Logger LOGER = LogManager.getLogger(PrihlaseniPokladni.class);
    private final Connection SPOJENI = Pos.getInstance().getSpojeni();
    private final ProgresDialog PROGRES = new ProgresDialog(Pos.getInstance().getAplikacniOkno());
    private final int id;
    private final int pin;
    private Pokladni pokladni;

    public PrihlaseniPokladni(int id, int pin) {
        this.id = id;
        this.pin = pin;
    }

    @Override
    protected Pokladni call() throws Exception {
        LOGER.info("Ověřovací proces pokladního bude nyní zahájen. Bude zobrazen stavový dialog");
        PROGRES.zobrazProgres("Ověřuje se přihlašování pokladní se zadaným uživatelským ID {} v databázi.", new Object[]{id});
        if (overUzivatele()) {
            LOGER.debug("Pokladní se zadaným uživatelským ID {} byl nalezen v databázi. Ověří se platnost.", id);
            if (!pokladni.isBlokovany()) {
                overHeslo();
                if (!pokladni.isHesloPlatne()) {
                    snizPocetPokusu();
                    if (pokladni.getPocetPrihlasovacichPokusu() == 0) {
                        zablokujUzivatele();
                    }
                } else {
                    nactiSkupinu();
                    if (pokladni.getPocetPrihlasovacichPokusu() < MAX_POCET_POKUSU) {
                        resetPocetPokusu();
                    }
                }
            } else {
                LOGER.debug("Pokladní se zadaným uživatelským ID {} je blokovaný.", id);
            }
        } else {
            LOGER.debug("Pokladní se zadaným uživatelským ID {} nebyl nalezen v databázi.", id);
        }
        return pokladni;
    }

    @Override
    protected void done() {
        LOGER.info("Ověřovací proces pokladního byl ukončen.");
        PROGRES.zavriDialog();
    }

    private boolean overUzivatele() throws SQLException {
        boolean existuje = false;
        LOGER.debug("Pokladní s uživatelským ID {} bude ověřen v datbázi.", id);
        final String dotaz = "SELECT skupina, jmeno, prijmeni, blokovany, pocet_prihlasovacich_pokusu FROM "
                + SPOJENI.getCatalog() + ".uzivatele WHERE id_uzivatele = ?;";
        try (PreparedStatement psmt = SPOJENI.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setInt(1, id);
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.first()) {
                    pokladni = new Pokladni();
                    pokladni.setId(id);
                    SkupinaUzivatele skupina = new SkupinaUzivatele();
                    skupina.setId(rs.getInt("skupina"));
                    pokladni.setSkupina(skupina);
                    pokladni.setJmeno(rs.getString("jmeno"));
                    pokladni.setPrijmeni(rs.getString("prijmeni"));
                    pokladni.setBlokovany(rs.getBoolean("blokovany"));
                    pokladni.setPocetPrihlasovacichPokusu(rs.getInt("pocet_prihlasovacich_pokusu"));
                    existuje = true;
                }
            }
        }
        return existuje;
    }

    private void overHeslo() throws SQLException {
        LOGER.info("Heslo pokladního s uživatelským ID {} bude ověřeno v datbázi.", id);
        final String dotaz = "SELECT COUNT(*) FROM " + SPOJENI.getCatalog() + ".uzivatele WHERE id_uzivatele = ? AND heslo = crypt(?, '$1$p.t2nIOB');";
        try (PreparedStatement psmt = SPOJENI.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setInt(1, id);
            psmt.setString(2, Integer.toString(pin));
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.first()) {
                    if (rs.getInt(1) == 1) {
                        pokladni.setHesloPlatne(true);
                    }
                }
            }
        }
    }

    private void snizPocetPokusu() throws Exception {
        LOGER.warn("Zadané heslo pokladního s uživatelským ID {} není správné. Počet pokusů bude snížen o 1", id);
        pokladni.setPocetPrihlasovacichPokusu(pokladni.getPocetPrihlasovacichPokusu() - 1);
        final String dotaz = "UPDATE " + SPOJENI.getCatalog() + ".uzivatele SET pocet_prihlasovacich_pokusu = ? WHERE id_uzivatele = ?;";
        try (PreparedStatement psmt = SPOJENI.prepareStatement(dotaz, Statement.RETURN_GENERATED_KEYS)) {
            psmt.setInt(1, pokladni.getPocetPrihlasovacichPokusu());
            psmt.setInt(2, id);
            if (psmt.executeUpdate() != 1) {
                throw new Exception("Aktualizační dotaz vrátil nesprávné hodnoty.");
            }
        }
    }

    private void zablokujUzivatele() throws Exception {
        LOGER.warn("Počet přihlašovacích pokusů pokladního s uživatelským ID {} byl vyčerpán. Uživatel bude zablokován.", id);
        pokladni.setBlokovany(true);
        final String dotaz = "UPDATE " + SPOJENI.getCatalog() + ".uzivatele SET blokovany = true WHERE id_uzivatele = ?;";
        try (PreparedStatement psmt = SPOJENI.prepareStatement(dotaz, Statement.RETURN_GENERATED_KEYS)) {
            psmt.setInt(1, id);
            if (psmt.executeUpdate() != 1) {
                throw new Exception("Aktualizační dotaz vrátil nesprávné hodnoty.");
            }
        }
    }

    private void resetPocetPokusu() throws Exception {
        LOGER.info("Pokladní s uživatelským ID {} již zadal správné heslo. Počet pokusů bude resetován na {}.", id, MAX_POCET_POKUSU);
        pokladni.setPocetPrihlasovacichPokusu(MAX_POCET_POKUSU);
        final String dotaz = "UPDATE " + SPOJENI.getCatalog() + ".uzivatele SET pocet_prihlasovacich_pokusu = ? WHERE id_uzivatele = ?;";
        try (PreparedStatement psmt = SPOJENI.prepareStatement(dotaz, Statement.RETURN_GENERATED_KEYS)) {
            psmt.setInt(1, MAX_POCET_POKUSU);
            psmt.setInt(2, id);
            if (psmt.executeUpdate() != 1) {
                throw new Exception("Aktualizační dotaz vrátil nesprávné hodnoty.");
            }
        }
    }

    private void nactiSkupinu() throws SQLException {
        // práva budou přidána později.
        LOGER.debug("Bude načtena skupina uživatelů pro pokladního s uživatelským ID {}.", id);
        final String dotaz = "SELECT oznaceni_skupiny, poznamka FROM " + SPOJENI.getCatalog() + ".uzivatelske_skupiny WHERE id_uzivatelske_skupiny = ?;";
        try (PreparedStatement psmt = SPOJENI.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setInt(1, pokladni.getSkupina().getId());
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.first()) {
                    pokladni.getSkupina().setOznaceni(rs.getString("oznaceni_skupiny"));
                    pokladni.getSkupina().setOznaceni(rs.getString("poznamka"));
                }
            }
        }
    }

}
