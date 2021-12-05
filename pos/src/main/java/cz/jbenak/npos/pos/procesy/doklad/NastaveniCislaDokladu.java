package cz.jbenak.npos.pos.procesy.doklad;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.doklad.Doklad;
import cz.jbenak.npos.pos.procesory.DokladProcesor;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

/**
 * Nastaví účetní číslo dokladu dle typu a číselné řady.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-02-03
 */
public class NastaveniCislaDokladu extends Task<Boolean> {

    private final static Logger LOGER = LogManager.getLogger(NastaveniCislaDokladu.class);
    private final Connection spojeni = Pos.getInstance().getSpojeni();
    private final Doklad doklad = DokladProcesor.getInstance().getDoklad();
    private String rada;
    private int posledniPoradoveCislo;
    private int delkaPoradovehoCisla;
    private boolean procesOK = true;

    @Override
    protected Boolean call() throws Exception {
        try {
            nactiCiselnouRadu();
            if (procesOK) {
                nactiPosledniPoradoveCislo();
                if (procesOK) {
                    sestavCisloDokladu();
                }
            }
        } catch (Exception e) {
            LOGER.error("Nastala vnitřní chyba v procesu načítání platné číselné řady dokladu.", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Nemohu připravit doklad",
                    "Nastala chyba v procesu načítání platné číselné řady:\n\n" + e.getLocalizedMessage(),
                    Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            procesOK = false;
        }
        return procesOK;
    }

    private void nactiCiselnouRadu() throws Exception {
        LOGER.debug("Bude načtena platná číselná řada pro doklad id {}, typ {}.", doklad.getId(), doklad.getTyp().name());
        String dotaz = "SELECT rada, delka_poradoveho_cisla FROM " + spojeni.getCatalog() + ".ciselne_rady " +
                "WHERE typ_dokladu = ? AND platnost_od <= ? AND (platnost_do >= ? OR platnost_do IS NULL);";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setString(1, doklad.getTyp().name());
            LocalDate dnes = LocalDate.now();
            psmt.setDate(2, Date.valueOf(dnes));
            psmt.setDate(3, Date.valueOf(dnes));
            ResultSet rs = psmt.executeQuery();
            if (rs != null && rs.last()) {
                rs.first();
                rada = rs.getString("rada");
                delkaPoradovehoCisla = rs.getInt("delka_poradoveho_cisla");
            } else {
                LOGER.error("Platná číselná řada dokladu nenalezena!");
                procesOK = false;
            }
        }
    }

    private void nactiPosledniPoradoveCislo() throws Exception {
        LOGER.debug("Zjistí se poslední pořadové číslo pro vybraný typ dokladu.");
        LOGER.debug("Poslední pořadové číslo pro typ dokladu {} se nepodařilo zjistit. Bude načteno z lokální databáze.", doklad.getTyp().name());
        final String dotaz = "SELECT poradove_cislo FROM " + spojeni.getCatalog()
                + ".doklady WHERE typ = ? AND datum_vystaveni BETWEEN ? AND ? ORDER BY poradove_cislo DESC LIMIT 1;";
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setString(1, doklad.getTyp().name());
            LocalDate dnes = LocalDate.now();
            psmt.setDate(2, Date.valueOf(dnes));
            psmt.setDate(3, Date.valueOf(dnes.plusDays(1)));
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    rs.first();
                    posledniPoradoveCislo = rs.getInt("poradove_cislo");
                } else {
                    if (!Pos.getInstance().getInicialniPoradovaCisla().isEmpty()
                            && Pos.getInstance().getInicialniPoradovaCisla().containsKey(doklad.getTyp())) {
                        posledniPoradoveCislo = Pos.getInstance().getInicialniPoradovaCisla().get(doklad.getTyp());
                    } else {
                        LOGER.error("Tabulka s iniciálními pořadovými čísly a lokální databáze neobsahuje žádné pořadové číslo pro daný doklad!");
                        procesOK = false;
                    }
                }
            } else {
                LOGER.error("Nemohu zjistit poslední pořadové číslo z lokální databáze!");
                procesOK = false;
            }
        }
    }

    private void sestavCisloDokladu() {
        LOGER.debug("Sestaví se a nastaví číslo dokladu {} dle číselné řady a pořadového čísla.", doklad.getId());
        doklad.setPoradoveCislo(posledniPoradoveCislo + 1);
        if (rada.contains("pokladna")) {
            rada = rada.replace("pokladna", Integer.toString(Pos.getInstance().getCisloPokladny()));
        }
        if (rada.contains("cislo")) {
            rada = rada.replace("cislo", String.format("%0" + delkaPoradovehoCisla + "d", posledniPoradoveCislo + 1));
        }
        if (rada.contains("yyyy") || rada.contains("yy") || rada.contains("mm") || rada.contains("dd")) {
            LocalDate dnes = LocalDate.now();
            rada = rada.replace("dd", String.format("%02d", dnes.getDayOfMonth()));
            rada = rada.replace("mm", String.format("%02d", dnes.getMonthValue()));
            rada = rada.replace("yyyy", Integer.toString(dnes.getYear()));
            rada = rada.replace("yy", Integer.toString(dnes.getYear()).substring(2));
        }
        doklad.setCislo(rada);
    }
}
