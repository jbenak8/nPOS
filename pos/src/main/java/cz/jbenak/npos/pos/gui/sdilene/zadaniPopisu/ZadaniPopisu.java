/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.sdilene.zadaniPopisu;

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

/**
 * Třída pro zobrazení zadávacího dialogu pro zadání popisu.
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-03-04
 */
public class ZadaniPopisu extends Stage {

    private static final Logger LOGER = LogManager.getLogger(ZadaniPopisu.class);
    private String popis;
    private boolean zadaniOdmitnuto;

    public ZadaniPopisu(Window vlastnik) {
        initOwner(vlastnik);
    }
    
    public void zobrazDialog() {
        LOGER.info("Zadávací dialog pro zadání popisu se zobrazí.");
        try {
            if (this.getStyle() != StageStyle.TRANSPARENT) {
                this.initStyle(StageStyle.TRANSPARENT);
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ZadaniPopisu.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            ZadaniPopisuKontroler kontroler = loader.getController();
            kontroler.setDialog(this);
            this.centerOnScreen();
            if (this.getModality() != Modality.WINDOW_MODAL) {
                this.initModality(Modality.WINDOW_MODAL);
            }
            this.showAndWait();
            kontroler.fokus();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit zadávací dialog: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit zadávací dialog: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }

    public void zavriDialog() {
        LOGER.info("Zadávací dialog bude uzavřen");
        this.close();
    }

    public String getPopis() {
        return popis;
    }

    protected void setPopis(String popis) {
        this.popis = popis;
    }

    public boolean isZadaniOdmitnuto() {
        return zadaniOdmitnuto;
    }

    protected void setZadaniOdmitnuto(boolean zadaniOdmitnuto) {
        this.zadaniOdmitnuto = zadaniOdmitnuto;
    }
}
