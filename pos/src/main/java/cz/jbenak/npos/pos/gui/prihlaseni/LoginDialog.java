/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.prihlaseni;

import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.otazkaAnoNe.OtazkaAnoNe;
import static cz.jbenak.npos.pos.gui.sdilene.dialogy.otazkaAnoNe.OtazkaAnoNe.Volby.*;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy.TypZpravy;
import cz.jbenak.npos.pos.objekty.sezeni.Pokladni;
import cz.jbenak.npos.pos.procesy.prihlaseni.PrihlaseniPokladni;
import cz.jbenak.npos.pos.system.Pos;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Třída pro zobrazení přihlašovacího dialogu. Zde vždy jako samostatné okno,
 * které při úspěšném přihlášení spustí hlavní okno.
 *
 * @author Jan Benák
 * @since 2018-02-02
 * @version 1.0.0.0
 */
public class LoginDialog {

    private static final Logger LOGER = LogManager.getLogger(LoginDialog.class);
    private LoginDialogKontroler kontroler;

    /**
     * Metoda pro zobrazení dialogu v hlavním aplikačním okně. - Pouze se vymění
     * obsah z inicializačního okna na login okno.
     */
    public void zobrazDialog() {
        LOGER.info("Bude zobrazen přihlašovací dialog.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginDialog.fxml"));
            Scene loginScena = new Scene(loader.load(), Color.TRANSPARENT);
            kontroler = loader.getController();
            kontroler.setDialog(this);
            //Toolkit.getDefaultToolkit().beep();
            Pos.getInstance().getAplikacniOkno().setScene(loginScena);
            Pos.getInstance().getAplikacniOkno().centerOnScreen();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit dialog pro přihlášení: ", e);
            new DialogZpravy(TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit dialog pro přihlášení: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            Platform.exit();
        }
    }

    /**
     * Spustí proces přihlášení uživatele.
     *
     * @param id zadané ID uživatele.
     * @param heslo zadané číslelné heslo (PIN) uživatele.
     */
    protected void prihlasUzivatele(int id, int heslo) {
        LOGER.info("Uživatelské ID a PIN bylo zadáno. Proces přihlášení bude spuštěn.");
        //PrihlaseniPokladni prihlaseni = new PrihlaseniPokladni(id, heslo);
        PrihlaseniPokladni prihlaseni = new PrihlaseniPokladni(id, heslo);
        try {
            prihlaseni.run();
            Pokladni pokladni = prihlaseni.get();
            if (pokladni == null) {
                LOGER.warn("Pokladní se zadaným id {} nebyl nalezen v databázi.", id);
                new DialogZpravy(TypZpravy.VAROVANI, "Pokladní nenalezen", "Pokladní se zadaným uživatelským ID {} nebyl nalezen v databázi. "
                        + "Opakujte prosím zadání.", new Object[]{id}, Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                kontroler.oznacID();
            } else if (pokladni.isBlokovany()) {
                LOGER.warn("Pokladní se zadaným id {} byl již zablokován.", id);
                new DialogZpravy(TypZpravy.CHYBA, "Pokladní blokován", "Pokladní se zadaným uživatelským ID {} byl již zablokován. "
                        + "Zkuste se prosím přihlásit s jiným uživatelem.", new Object[]{id}, Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                kontroler.oznacID();
            } else if (!pokladni.isHesloPlatne() && pokladni.getPocetPrihlasovacichPokusu() > 0) {
                LOGER.warn("Pokladní se zadaným id {} zadal nesprávné heslo. Počet pokusů byl již snížen na {}.", id, pokladni.getPocetPrihlasovacichPokusu());
                new DialogZpravy(TypZpravy.VAROVANI, "Zadali jste špatné heslo", "Pro pokladního se zadaným uživatelským ID {} jste zadali nesprávné heslo. "
                        + "Opakujte prosím zadání.\n\nUpozorňuji, že máte {}", new Object[]{id, getTextPocetPokusu(pokladni.getPocetPrihlasovacichPokusu())},
                        Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
                kontroler.oznacID();
            } else {
                LOGER.info("Pokladní s uživatelským ID {} zadal již správné údaje a bude pokračovat do hlavního okna systému. Neúspěšné pokusy byly již resetovány.", id);
                HlavniOkno.getInstance().zobrazHlavniOkno(pokladni);
            }
        } catch (InterruptedException | ExecutionException ex) {
            LOGER.error("Nebylo možno získat výsledek ověření uživatele.", ex);
            new DialogZpravy(TypZpravy.CHYBA, "Chyba systému", "Nebylo možno získat výsledek ověření uživatele: \n\n" + ex.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
        }
    }

    /**
     * Metoda pro uzavření login dialogu. Prakticky se jedná o ukončení
     * programu.
     */
    protected void zavriDialog() {
        LOGER.info("Bylo stisknuto tlačítko k uzavření okna. Potvrzovací otázka bude zobrazena.");
        OtazkaAnoNe zavrit = new OtazkaAnoNe("Odejít?", "Opravdu si přejete ukončit přihlašování a tím odejít z programu?", Pos.getInstance().getAplikacniOkno());
        zavrit.zobrazDialog();
        if (zavrit.getVolba() == ANO) {
            LOGER.info("Volba byla potvrzena. Program se ukončí.");
            Platform.exit();
        } else {
            LOGER.info("Volba nebyla potvrzena. Program zůstane v okně přihlášení.");
        }
    }

    /**
     * Metoda pro sestavení správné české věty na zbývající počet pokusů
     *
     * @param pocetPokusu zbývající počet pokusů.
     * @return správná česká věta pro informační dialot.
     */
    public static String getTextPocetPokusu(int pocetPokusu) {
        String veta = "";
        switch (pocetPokusu) {
            case 3:
                veta = "pouze tři pokusy.";
                break;
            case 2:
                veta = "už jen dva pokusy.";
                break;
            case 1:
                veta = "již pouze jeden pokus! \n\nZadáte li jej špatně, tento užvatel bude zablokován!";
                break;
            default:
                break;
        }
        return veta;
    }
}
