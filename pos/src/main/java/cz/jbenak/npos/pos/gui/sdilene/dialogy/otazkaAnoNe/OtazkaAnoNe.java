/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.sdilene.dialogy.otazkaAnoNe;

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
 * Třída pro zobrazení rozhodovacího dialogu s volbami Ano nebo Ne (potvrdit
 * nebo storno).
 *
 * @author Jan Benák
 * @since 2018-02-02
 * @version 1.0.0.0
 */
public class OtazkaAnoNe extends Stage {

    /**
     * Volby Ano, nebo ne
     */
    public enum Volby {
        ANO, NE;
    }

    private final static Logger LOGER = LogManager.getLogger(OtazkaAnoNe.class);
    private final String nadpis;
    private final String zprava;
    private Volby volba;
    private Object[] argumenty;

    /**
     * Konstruktor dialogu otázky.
     *
     * @param nadpis nadpis dialogu.
     * @param zprava text zprávy
     * @param vlastnik rodičovské okno - to, co dialog vyvolalo
     */
    public OtazkaAnoNe(String nadpis, String zprava, Window vlastnik) {
        this.nadpis = nadpis;
        this.zprava = zprava;
        this.initOwner(vlastnik);
    }

    /**
     * Konstruktor pro vytvoření vlastního dialogu. Již se předává obsah a je
     * možné také přidat argumenty ve stylu log4j pro snadnější tvorbu zprávy -
     * eliminuje vícenásobné zřetězování zprávy.
     *
     * @param nadpis nadpis okna
     * @param argumenty argumenty pro nahrazení v placeholderech
     * @param zprava text zprávy
     * @param vlastnik rodičovské okno pro inicializaci modality
     */
    public OtazkaAnoNe(String nadpis, String zprava, Object[] argumenty, Window vlastnik) {
        this.nadpis = nadpis;
        this.zprava = zprava;
        this.argumenty = argumenty;
        this.initOwner(vlastnik);
    }

    /**
     * Vlastní zobrazení okna dialogu. Pozastaví volající vlákno.
     */
    public void zobrazDialog() {
        LOGER.info("Bude zobrazen dialog s volbou Ano nebo Ne.");
        //Toolkit.getDefaultToolkit().beep();
        try {
            this.initStyle(StageStyle.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OtazkaAnoNe.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            OtazkaAnoNeKontroler kontroler = loader.getController();
            kontroler.setDialog(this);
            kontroler.setNadpis(nadpis);
            kontroler.setPopis(sestavZpravu(zprava));
            this.centerOnScreen();
            this.initModality(Modality.WINDOW_MODAL);
            this.showAndWait();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit dialog pro zobrazení voleb Ano a Ne: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit dialog voleb Ano a Ne: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            Platform.exit();
        }
    }

    /**
     * Vrátí rozhodnutí uživatele.
     *
     * @return buď ano, nebo ne.
     */
    public Volby getVolba() {
        return volba;
    }

    /**
     * Nastaví volbu při stisku tlačítka.
     *
     * @param volba Volba uživatele - buď potvrdit, nebo stornovat.
     */
    protected void setVolba(Volby volba) {
        this.volba = volba;
    }

    /**
     * Přepíše placeholdery ve zprávě argumenty.
     *
     * @param telo zpráva z placeholdery.
     * @return výsledná zpráva.
     */
    private String sestavZpravu(String telo) {
        if (argumenty != null) {
            for (Object arg : argumenty) {
                telo = telo.replaceFirst("[{][}]", arg.toString());
            }
        }
        return telo;
    }

}
