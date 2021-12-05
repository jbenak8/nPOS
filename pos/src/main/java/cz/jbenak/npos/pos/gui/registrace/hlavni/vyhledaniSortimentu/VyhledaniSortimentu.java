/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni.vyhledaniSortimentu;

import cz.jbenak.npos.pos.gui.registrace.hlavni.seznamSortimentu.SeznamSortimentu;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.progres.ProgresDialog;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.sortiment.KriteriaVyhledavani;
import cz.jbenak.npos.pos.objekty.sortiment.SeznamRegistrace;
import cz.jbenak.npos.pos.procesy.polozka.vyhledavani.NacteniSeznamuSortimentu;
import cz.jbenak.npos.pos.system.Pos;
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
 * Třída pro zobrazení vyhledávacího dialogu pro sortiment.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-03-04
 */
public class VyhledaniSortimentu extends Stage {

    private final static Logger LOGER = LogManager.getLogger(VyhledaniSortimentu.class);
    private final boolean zobrazitInfo;
    private final KriteriaVyhledavani kriteria;
    private final ProgresDialog progres;
    private VyhledaniSortimentuKontroler kontroler;

    public VyhledaniSortimentu(boolean zobrazitInfo, Stage vlastnik) {
        this.zobrazitInfo = zobrazitInfo;
        this.kriteria = new KriteriaVyhledavani();
        this.progres = new ProgresDialog(vlastnik);
        initOwner(vlastnik);
    }

    public void zobrazDialog() {
        LOGER.info("Dialog pro vyhledání sortimentu bude zobrazen.");
        try {
            if (this.getStyle() != StageStyle.TRANSPARENT) {
                this.initStyle(StageStyle.TRANSPARENT);
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VyhledaniSortimentu.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            kontroler = loader.getController();
            kontroler.setDialog(this);
            this.centerOnScreen();
            if (this.getModality() != Modality.WINDOW_MODAL) {
                this.initModality(Modality.WINDOW_MODAL);
            }
            kontroler.initGUI();
            kontroler.setInfo(zobrazitInfo);
            this.showAndWait();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit zadávací dialog: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit zadávací dialog: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }

    protected void zavriDialog() {
        LOGER.info("Dialog pro vyhledávání sortimentu bude uzavřen.");
        close();
    }

    protected KriteriaVyhledavani getKriteria() {
        return kriteria;
    }

    protected void provedVyhledavani() {
        LOGER.info("Provede se vyhledávání dle zadaných kritérií.");
        NacteniSeznamuSortimentu nacteni = new NacteniSeznamuSortimentu(kriteria);
        progres.zobrazProgres("Nyní vyhledám položky sortimentu dle zadaných kritérii a zobrazím je v seznamu.");
        nacteni.run();
        try {
            List<SeznamRegistrace> sortiment = nacteni.get();
            progres.zavriDialog();
            if (sortiment.isEmpty()) {
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nic jsem nenašel", "Bohužel jsem pro zadaná kritéria nenašel ani jednu položku sortimentu. " +
                        "Zkuste upravit kritéria hledání a pak hledat znovu.\nTIP: Pokud necháte vše prázdné, pokusím se zobrazit vše, co je uloženo.", this).zobrazDialogAPockej();
                kontroler.oznacPrvni();
            } else {
                SeznamSortimentu seznamDialog = new SeznamSortimentu(sortiment, Pos.getInstance().getAplikacniOkno(), true);
                seznamDialog.zobrazSeznam();
                LOGER.info("Okno pro vyhledávání sortimentu se uzavře.");
                this.close();
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGER.error("Nepodařilo se načíst seznam sortimentu.", e);
            if (progres.isShowing()) {
                progres.zavriDialog();
            }
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se načíst seznam sortimentu \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }
}
