package cz.jbenak.npos.pos.gui.platba.dialogKVraceni;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
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

/**
 * Zobrazí dialog pro zobrazení částky k vrácení.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-02-19
 */
public class DialogKVraceni extends Stage {

    private static final Logger LOGER = LogManager.getLogger(DialogKVraceni.class);
    private final boolean zobrazitPotvrzovaciTlacitko;

    /**
     * Inicializace dialogu pro vrácení. Jako vlastník je nastaveno hlavní okno.
     *
     * @param zobrazitPotvrzovaciTlacitko určuje, zdali bude zobrazeno potvrzovací tlačítko dialogu.
     */
    public DialogKVraceni(boolean zobrazitPotvrzovaciTlacitko) {
        this.zobrazitPotvrzovaciTlacitko = zobrazitPotvrzovaciTlacitko;
        initOwner(Pos.getInstance().getAplikacniOkno());
    }

    /**
     * Zobrazí dialog v režimu "Zobraz a počkej".
     */
    public void zobrazDialog() {
        LOGER.info("Zobrazí se dialog pro vrácení. Zobrazovací tlačítku {} zobrazeno.", zobrazitPotvrzovaciTlacitko ? "bude" : "nebude");
        //Toolkit.getDefaultToolkit().beep(); TODO: nahradit přehrávání zvuku
        try {
            initStyle(StageStyle.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DialogKVraceni.fxml"));
            setScene(new Scene(loader.load(), Color.TRANSPARENT));
            DialogKVraceniKontroler kontroler = loader.getController();
            kontroler.setDialog(this);
            centerOnScreen();
            initModality(Modality.WINDOW_MODAL);
            kontroler.zobrazPrvky();
            showAndWait();
        } catch (Exception e) {
            LOGER.error("Nepodařilo se zobrazit dialog pro vrácení hotovosti: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit dialog pro vrácení hotovosti: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            Platform.exit();
        }
    }

    /**
     * Zavře dialog a umožní pokračování návazných procesů.
     */
    public void zavriDialog() {
        LOGER.info("Dialog k vrácení bude uzavřen.");
        close();
    }

    /**
     * Určí, zdali zobrazit potvrzovací tlačítko pro zavření dialogu.
     *
     * @return zobrazit potvrzovací tlačítko.
     */
    protected boolean isZobrazitPotvrzovaciTlacitko() {
        return zobrazitPotvrzovaciTlacitko;
    }
}
