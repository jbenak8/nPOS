/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.sdilene.dialogy.progres;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.system.Pos;

import java.io.IOException;

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

/**
 * Třída pro zobrazení modálního progres dialogu.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-02
 */
public class ProgresDialog extends Stage {

    private final static Logger LOGER = LogManager.getLogger(ProgresDialog.class);
    private ProgresDialogKontroler kontroler;

    /**
     * Konstruktor progres dialogu.
     *
     * @param vlastnik Volající okno.
     */
    public ProgresDialog(Window vlastnik) {
        this.initOwner(vlastnik);
    }

    /**
     * Zobrazí dialog s předanou zprávou.
     *
     * @param initText Zpráva, co se má zobrazit uživateli.
     */
    public synchronized void zobrazProgres(String initText) {
        LOGER.info("Progres dialog bude zobrazen.");
        try {
            if (this.getStyle() != StageStyle.TRANSPARENT) {
                this.initStyle(StageStyle.TRANSPARENT);
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProgresDialog.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            kontroler = loader.getController();
            kontroler.setText(initText);
            this.centerOnScreen();
            if (this.getModality() != Modality.WINDOW_MODAL) {
                this.initModality(Modality.WINDOW_MODAL);
            }
            this.show();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit progres dialog: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit progres dialog: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            Platform.exit();
        }
    }

    /**
     * Zobrazí dialog s předanou zprávou, která obsahuje placeholdery {} a
     * argumenty pro snandnější tvorbu zprávy. Eliminuje se několikanásobné
     * řetězení argumentů v retězci.
     *
     * @param initText  zpráva s placeholdery
     * @param argumenty argumenty, které mají nahradit placeholdery.
     */
    public synchronized void zobrazProgres(String initText, Object[] argumenty) {
        zobrazProgres(sestavZpravu(initText, argumenty));
    }

    /**
     * Změní zobrazovanou zprávu.
     *
     * @param textZpravy Nová zpráva.
     */
    public void setText(String textZpravy) {
        if (kontroler != null) {
            kontroler.setText(textZpravy);
        }
    }

    /**
     * Umožňuje zobrazit zprávu s placeholdery pro jednotlivé argumenty podobně
     * jako log4j
     *
     * @param zprava    tělo zpravy s {} jako placeholder pro argumenty.
     * @param argumenty zadané argumenty.
     */
    public void setText(String zprava, Object[] argumenty) {
        if (kontroler != null) {
            kontroler.setText(sestavZpravu(zprava, argumenty));
        }
    }

    /**
     * Uzavře progres dialog.
     */
    public void zavriDialog() {
        LOGER.info("Progres dialog bude uzavřen");
        this.close();
    }

    /**
     * Přepíše placeholdery argumenty.
     *
     * @param telo      zpráva z placeholdery
     * @param argumenty argumenty pro dosazení za placehodery
     * @return výsledná zpráva
     */
    private String sestavZpravu(String telo, Object[] argumenty) {
        for (Object arg : argumenty) {
            telo = telo.replaceFirst("[{][}]", arg.toString());
        }
        return telo;
    }
}
