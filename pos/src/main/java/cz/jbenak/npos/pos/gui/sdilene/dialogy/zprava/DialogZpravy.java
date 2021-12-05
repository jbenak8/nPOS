package cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import javafx.stage.Modality;

/**
 * Třída pro zobrazení jednoduchého informačního dialogu. Pozor! Volat normálně
 * - tj. ne přes Platform.runLater() - pak nefunguje showAndWait()
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-01-31
 */
public final class DialogZpravy extends Stage {

    /**
     * Typy dialogů
     */
    public enum TypZpravy {
        CHYBA, VAROVANI, OK, INFO
    }

    private static final Logger LOGER = LogManager.getLogger(DialogZpravy.class);
    private final TypZpravy typ;
    private final String nadpis;
    private final String zprava;
    private boolean zobrazitPotvrzovaciTlacitko = true;
    private Object[] argumenty;

    /**
     * Konstruktor pro vytvoření vlastního dialogu. Již se předává obsah
     *
     * @param typ typ zprávy
     * @param nadpis nadpis okna
     * @param zprava text zprávy
     * @param vlastnik rodičovské okno pro inicializaci modality
     */
    public DialogZpravy(TypZpravy typ, String nadpis, String zprava, Window vlastnik) {
        this.typ = typ;
        this.nadpis = nadpis;
        this.zprava = zprava;
        this.initOwner(vlastnik);
        this.initModality(Modality.WINDOW_MODAL);
    }

    /**
     * Konstruktor pro vytvoření vlastního dialogu. Již se předává obsah a je
     * možné také přidat argumenty ve stylu log4j pro snadnější tvorbu zprávy -
     * eliminuje vícenásobné zřetězování zprávy.
     *
     * @param typ typ zprávy
     * @param nadpis nadpis okna
     * @param argumenty argumenty pro nahrazení v placeholderech
     * @param zprava text zprávy
     * @param vlastnik rodičovské okno pro inicializaci modality
     */
    public DialogZpravy(TypZpravy typ, String nadpis, String zprava, Object[] argumenty, Window vlastnik) {
        this.typ = typ;
        this.nadpis = nadpis;
        this.zprava = zprava;
        this.argumenty = argumenty;
        this.initOwner(vlastnik);
    }

    /**
     * Zobrazí dialog paralelně s volajícím procesem - nedojde k jeho
     * pozastavení.
     *
     * @param zobrazitPotvrzovaciTlacitko jestli se má zobrazit tlačítko OK
     * (true), nebo nechat uzavření programu na volajícím procesu (false). Pak
     * je nutné implementovat volání metody zavriDialog();
     */
    public void zobrazDialog(boolean zobrazitPotvrzovaciTlacitko) {
        this.zobrazitPotvrzovaciTlacitko = zobrazitPotvrzovaciTlacitko;
        nastartujDialog(false);
    }

    /**
     * Zobrazí dialog a pozastaví volající proces. Z tohoto důvodu se
     * potvrzovací tlačítko zobrazí vždy.
     */
    public void zobrazDialogAPockej() {
        nastartujDialog(true);
    }

    /**
     * Zavře dialog. Použít pouze pokud není zobrazeno potvrzovací tlačítko.
     */
    public void zavriDialog() {
        if (!zobrazitPotvrzovaciTlacitko) {
            LOGER.debug("Dialog bude uzavřen na základě pokynu programu.");
            this.close();
        }
    }

    /**
     * Vlastní start dialogu.
     *
     * @param pockat jestli se má čekat, nebo ne.
     */
    private void nastartujDialog(boolean pockat) {
        LOGER.debug("Bude zobrazen informační dialog. Tlačítka budou zobrazena: {}.", zobrazitPotvrzovaciTlacitko);
        //Toolkit.getDefaultToolkit().beep();
        try {
            this.initStyle(StageStyle.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DialogZpravy.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            DialogZpravyKontroler kontroler = loader.getController();
            kontroler.setDialog(this);
            kontroler.setNadpis(nadpis);
            kontroler.setPopis(sestavZpravu(zprava));
            kontroler.setTypIkony(typ);
            kontroler.zobrazPotvrzovaciTlacitko(zobrazitPotvrzovaciTlacitko);
            this.initModality(Modality.WINDOW_MODAL);
            this.centerOnScreen();
            if (pockat) {
                this.showAndWait();
            } else {
                this.show();
            }
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit dialog s textem zprávy: ", e.getLocalizedMessage());
        }
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
