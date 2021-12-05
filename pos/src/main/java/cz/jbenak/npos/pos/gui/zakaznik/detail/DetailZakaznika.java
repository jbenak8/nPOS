package cz.jbenak.npos.pos.gui.zakaznik.detail;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
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

/**
 * Třída pro zobrazení dialogu s detailními informacemi vybraného zákazníka. Pouze informační dialog.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @see cz.jbenak.npos.pos.objekty.partneri.Zakaznik
 * @since 2019-09-10
 */
public class DetailZakaznika extends Stage {

    private final static Logger LOGER = LogManager.getLogger(DetailZakaznika.class);
    private final Zakaznik zakaznik;

    /**
     * Konstruktor objektu dialogu.
     *
     * @param zakaznik Zákazník s kompletně načtenými daty.
     */
    public DetailZakaznika(Zakaznik zakaznik, Stage vlastnik) {
        this.zakaznik = zakaznik;
        initOwner(vlastnik);
    }

    /**
     * Zobrazí dialog vybraného zákazníka
     *
     * @param pockej zdali má volající dialog počkat s další akcí než se uzavře tento.
     */
    public void zobrazDialog(boolean pockej) {
        LOGER.info("Bude zobrazen dialog s detaily vybraného zákazníka s číslem {}.", zakaznik.getCislo());
        //Toolkit.getDefaultToolkit().beep();
        try {
            this.initStyle(StageStyle.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailZakaznika.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            DetailZakaznikaKontroler kontroler = loader.getController();
            kontroler.setDialog(this);
            kontroler.setZakaznik(zakaznik);
            this.centerOnScreen();
            this.initModality(Modality.WINDOW_MODAL);
            if (pockej) {
                this.showAndWait();
            } else {
                this.show();
            }
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit dialog pro zobrazení detailu vybraného zákazníka: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit dialog s detailem vybraného zákazníka: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            Platform.exit();
        }
    }
}
