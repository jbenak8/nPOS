package cz.jbenak.npos.pos.gui.zakaznik.registracniFunkce;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.otazkaAnoNe.OtazkaAnoNe;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.gui.zakaznik.detail.DetailZakaznika;
import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
import cz.jbenak.npos.pos.procesory.RegistracniProcesor;
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

import static cz.jbenak.npos.pos.gui.sdilene.dialogy.otazkaAnoNe.OtazkaAnoNe.Volby.ANO;


/**
 * Třída pro zobrazení funkcí pro registrovaného zákazníka
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-09-12
 */
public class FunkceRegistrovanehoZakaznika extends Stage {

    private static final Logger LOGER = LogManager.getLogger(FunkceRegistrovanehoZakaznika.class);
    private final Zakaznik registrovanyZakaznik;

    /**
     * Konstruktor dialogu. Předá registrovaného zákazníka a nastaví hlavní okno jako vlastníka.
     *
     * @param registrovanyZakaznik registrovaný zákazník v dokladu.
     */
    public FunkceRegistrovanehoZakaznika(Zakaznik registrovanyZakaznik) {
        this.registrovanyZakaznik = registrovanyZakaznik;
        initOwner(Pos.getInstance().getAplikacniOkno());
    }

    public void zobrazDialog() {
        LOGER.info("Bude zobrazen funkční panel pro registrovaného zákazníka s číslem {}", registrovanyZakaznik.getCislo());
        //Toolkit.getDefaultToolkit().beep();
        try {
            this.initStyle(StageStyle.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FunkceRegistrovanehoZakaznika.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            FunkceRegistrovanehoZakaznikaKontroler kontroler = loader.getController();
            kontroler.setDialog(this);
            kontroler.setZakaznik(registrovanyZakaznik);
            kontroler.zobrazZakaznika();
            this.centerOnScreen();
            this.initModality(Modality.WINDOW_MODAL);
            this.show();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit dialog pro zobrazení funkcí pro vybraného zákazníka: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit dialog s funkcemi pro vybraného zákazníka: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            Platform.exit();
        }
    }

    void zobrazInfoOZakaznikovi() {
        LOGER.info("Bude zobrazen dialog s detailem registrovaného zákazníka s číslem {}.", registrovanyZakaznik.getCislo());
        DetailZakaznika detailDialog = new DetailZakaznika(registrovanyZakaznik, this);
        detailDialog.zobrazDialog(true);
        this.close();
    }

    void odregistrujZakaznika() {
        LOGER.info("Bude zobrazen dotaz zdali registrovaného zákazníka s číslem {} opravdu odebrat z dokladu.", registrovanyZakaznik.getCislo());
        OtazkaAnoNe potvrzeni = new OtazkaAnoNe("Odebrat zákazníka?", "Opravdu chcete odebrat tohoto zákazníka z aktuálního dokladu?", this);
        potvrzeni.zobrazDialog();
        if (potvrzeni.getVolba() == ANO) {
            LOGER.info("Odebrání zákazníka potvrzeno. Zákazník bude odregistrován a dialog bude uzavřen.");
            RegistracniProcesor.getInstance().odregistrujZakaznika();
            this.close();
        } else {
            LOGER.info("Volba na odregistraci zákazníka z aktuálního dokladu byla zamítnuta.");
            this.close();
        }
    }
}
