/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni.seznamSortimentu;

import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.registrace.hlavni.kartaSortimentu.KartaSortimentu;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.sortiment.SeznamRegistrace;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;
import cz.jbenak.npos.pos.procesory.RegistracniProcesor;
import cz.jbenak.npos.pos.procesy.registrace.NacteniPolozky;
import cz.jbenak.npos.pos.system.Pos;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static cz.jbenak.npos.pos.procesory.AutorizacniManazer.Funkce.INFO_O_SORTIMENTU;

/**
 * Třída pro zobrazení výsledků vyhledávání sortimentu
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-13
 */
public class SeznamSortimentu extends Stage {

    private static final Logger LOGER = LogManager.getLogger(SeznamSortimentu.class);
    private final List<SeznamRegistrace> nalezenePolozky;
    private final boolean registrovatPrimo;
    private Sortiment polozka;

    /**
     * Konstruktor dialogu seznamu.
     *
     * @param nalezenePolozky  Seznam registračních čísel nalezených pomocí
     *                         vyhledávání.
     * @param vlastnik         Rodičovské okno.
     * @param registrovatPrimo zdali není vyžadováno vrácení vybraného registru, ale přímá registrace.
     */
    public SeznamSortimentu(List<SeznamRegistrace> nalezenePolozky, Window vlastnik, boolean registrovatPrimo) {
        this.nalezenePolozky = nalezenePolozky;
        this.registrovatPrimo = registrovatPrimo;
        this.initOwner(vlastnik);
    }

    /**
     * Vlastní zobrazení seznamu.
     */
    public void zobrazSeznam() {
        LOGER.info("Bude zobrazen dialog pro výběr načtených položek sortimentu.");
        //Toolkit.getDefaultToolkit().beep();
        try {
            this.initStyle(StageStyle.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SeznamSortimentu.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            SeznamSortimentuKontroler kontroler = loader.getController();
            kontroler.setDialog(this);
            kontroler.setPolozky(nalezenePolozky);
            this.centerOnScreen();
            this.initModality(Modality.WINDOW_MODAL);
            this.showAndWait();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit dialog pro zobrazení načtených položek seznamu sortimentu: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit seznam načtencýh položek sortimentu \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            Platform.exit();
        }
    }

    /**
     * Metoda pro vrácení položky sortimentu s registračním číslem. Položka nic
     * jiného neobsahuje a je určena k dalšímu doplnění v rámci registrace.
     *
     * @return vybraná položka (registr).
     */
    public Sortiment getPolozka() {
        return polozka;
    }

    /**
     * Kontrolér nastaví vybranou položku sortimentu. Pokud je vyžadována přímá registrace, rovnou provede registraci.
     *
     * @param polozka prázdná položka sortimentu obsahující registrační číslo.
     */
    void setPolozka(Sortiment polozka) {
        this.polozka = polozka;
        if (registrovatPrimo && polozka != null) {
            RegistracniProcesor.getInstance().zaregistrujPolozku(polozka);
        }
    }

    void zobrazInfoOPolozce(SeznamRegistrace vybrano) {
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(INFO_O_SORTIMENTU)) {
            LOGER.info("Zobrazí se karta vybrané položky s registračním číslem {}.", vybrano.getRegistr());
            Sortiment polozka = new Sortiment();
            polozka.setRegistr(vybrano.getRegistr());
            NacteniPolozky np = new NacteniPolozky(polozka);
            np.run();
            try {
                KartaSortimentu karta = new KartaSortimentu(np.get(), registrovatPrimo);
                karta.zobrazDialog();
                if(karta.isZavrenoPotvrzenim() && registrovatPrimo) {
                    LOGER.info("Byla provedena registrace vybrané položky sortimentu s registrem {} přímo z karty sortimentu. Tento dialog se uzavře.", vybrano.getRegistr());
                    close();
                }
            } catch (InterruptedException | ExecutionException e) {
                LOGER.error("Nepodařilo se načíst data vybrané položky.", e);
                new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se načíst data vybrané položky sortimentu \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            }
        }
    }
}
