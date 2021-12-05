package cz.jbenak.npos.pos.gui.zakaznik.vyhledani;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.progres.ProgresDialog;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.gui.zakaznik.seznam.SeznamZakazniku;
import cz.jbenak.npos.pos.objekty.partneri.KriteriaVyhledavaniZakaznika;
import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
import cz.jbenak.npos.pos.procesy.zakaznik.NacteniSeznamuZakazniku;
import cz.jbenak.npos.pos.system.Pos;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-07-14
 * <p>
 * Třída pro zobrazení vyhledávacího dialogu pro seznam zákazníků.
 */

public class VyhledaniZakazniku extends Stage {

    private static final Logger LOGER = LogManager.getLogger(VyhledaniZakazniku.class);
    private final ProgresDialog progres;
    private VyhledaniZakaznikuKontroler kontroler;

    public VyhledaniZakazniku(Stage vlastnik) {
        initOwner(vlastnik);
        this.progres = new ProgresDialog(vlastnik);
    }

    public void zobrazDialog() {
        LOGER.info("Bude zobrazen dialog pro vyhledávání zákazníků.");
        //Toolkit.getDefaultToolkit().beep();
        try {
            this.initStyle(StageStyle.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VyhledaniZakazniku.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            kontroler = loader.getController();
            kontroler.setDialog(this);
            this.centerOnScreen();
            this.initModality(Modality.WINDOW_MODAL);
            kontroler.initGUI();
            this.showAndWait();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit dialog pro vyhledávání zákazníků: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit pro vyhledávání zákazníků \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            Platform.exit();
        }
    }

    void nactiSeznamZakazniku(KriteriaVyhledavaniZakaznika kriteria, int maxZaznamu) {
        LOGER.info("Načte se seznam zákazníků");
        NacteniSeznamuZakazniku nacteni = new NacteniSeznamuZakazniku(kriteria, maxZaznamu);
        progres.zobrazProgres("Nyní vyhledám zákazníky dle zadaných kritérii a zobrazím je v seznamu.");
        nacteni.run();
        try {
            List<Zakaznik> zakaznici = nacteni.get();
            progres.zavriDialog();
            if (zakaznici.isEmpty()) {
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nikoho jsem nenašel", "Bohužel jsem pro zadaná kritéria nenašel ani jednoho zákazníka. " +
                        "Zkuste upravit kritéria hledání a pak hledat znovu.\nTIP: Pokud necháte vše prázdné, pokusím se zobrazit vše, co je uloženo.", this).zobrazDialogAPockej();
                kontroler.oznacPrvni();
            } else {
                SeznamZakazniku seznamDialog = new SeznamZakazniku(zakaznici, Pos.getInstance().getAplikacniOkno());
                seznamDialog.zobrazDialog();
                LOGER.info("Okno pro vyhledávání zákazníků se uzavře.");
                this.close();
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGER.error("Nepodařilo se načíst seznam zákazníků.", e);
            if (progres.isShowing()) {
                progres.zavriDialog();
            }
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se načíst seznam zákazníků \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }
}
