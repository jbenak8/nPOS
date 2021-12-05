/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.utility.kalkulacka;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.system.Pos;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Třída pro zobrazení obchodnické kalkulačky.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-10
 */
public class Kalkulacka extends Stage {

    private static final Logger LOGER = LogManager.getLogger(Kalkulacka.class);

    /**
     * Konstruktor třídy. Inicializuje se vlastník objektu pro modalitu.
     *
     * @param vlastnik Rodičovské okno
     */
    public Kalkulacka(Window vlastnik) {
        initOwner(vlastnik);
    }

    /**
     * Zobrazí kalkulačku.
     */
    public void zobrazDialog() {
        LOGER.info("Bude zobrazeno okno obchodnického kalkulátoru.");
        try {
            this.initStyle(StageStyle.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Kalkulacka.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            KalkulackaKontroler kontroler = loader.getController();
            kontroler.setKalkulacka(this);
            this.initModality(Modality.WINDOW_MODAL);
            this.centerOnScreen();
            this.show();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit hlavní okno: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit hlavní okno: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }
}
