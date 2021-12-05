/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.system.Pos;
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

/**
 * Třída pro zobrazení grafické textové klávesnice.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-14
 */
public class TextovaKlavesnice extends Stage {

    private final static Logger LOGER = LogManager.getLogger(TextovaKlavesnice.class);
    private final ITextovaKlavesnice prijemce;
    private final List<PoleKVyplneni> poleKVyplneni;
    private final int indexPoleKVyplneni;

    /**
     * Konstruktor pro zobrazení dialogu Textové klávesnice.
     *
     * @param prijemce      volající objekt implementující ITextovaKlavesnice
     * @param vlastnik      rodičovské okno vlastnící tento dialog
     * @param poleKVyplneni seznam s poli (obsahující veškeré vlastnosti) které chceme vyplnit
     */
    public TextovaKlavesnice(ITextovaKlavesnice prijemce, Window vlastnik, List<PoleKVyplneni> poleKVyplneni) {
        this.prijemce = prijemce;
        this.poleKVyplneni = poleKVyplneni;
        this.indexPoleKVyplneni = 0;
        initOwner(vlastnik);
    }

    /**
     * Konstruktor pro zobrazení dialogu Textové klávesnice. Zde se zadáním vybraného pole.
     *
     * @param prijemce           volající objekt implementující ITextovaKlavesnice
     * @param vlastnik           rodičovské okno vlastnící tento dialog
     * @param poleKVyplneni      seznam s poli (obsahující veškeré vlastnosti) které chceme vyplnit
     * @param indexPoleKVyplneni vybrané pole, kterým bude zahájeno vyplňování
     */
    public TextovaKlavesnice(ITextovaKlavesnice prijemce, Window vlastnik, List<PoleKVyplneni> poleKVyplneni, int indexPoleKVyplneni) {
        this.prijemce = prijemce;
        this.poleKVyplneni = poleKVyplneni;
        this.indexPoleKVyplneni = indexPoleKVyplneni;
        initOwner(vlastnik);
    }

    public void zobrazKlavesnici() {
        LOGER.info("Bude zobrazena textová klávesnice.");
        try {
            if (this.getStyle() != StageStyle.TRANSPARENT) {
                this.initStyle(StageStyle.TRANSPARENT);
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TextovaKlavesnice.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            TextovaKlavesniceKontroler kontroler = loader.getController();
            kontroler.setDialog(this);
            kontroler.setPrijemce(prijemce);
            kontroler.setPoleKVyplneni(poleKVyplneni);
            kontroler.setIndexPoleKVyplneni(indexPoleKVyplneni);
            if (this.getModality() != Modality.WINDOW_MODAL) {
                this.initModality(Modality.WINDOW_MODAL);
            }
            this.centerOnScreen();
            this.show();
            kontroler.initGUI();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit textovou klávesnici: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit textovou klávesnici: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }

    protected void zavriDialog() {
        LOGER.info("Okno obsahující textovou klávesnici bude uzvařeno.");
        prijemce.editaceUkoncena();
        close();
    }
}
