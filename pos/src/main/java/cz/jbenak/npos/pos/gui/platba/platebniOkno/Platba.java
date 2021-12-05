package cz.jbenak.npos.pos.gui.platba.platebniOkno;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.otazkaAnoNe.OtazkaAnoNe;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.procesory.DokladProcesor;
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

public class Platba extends Stage {

    private static final Logger LOGER = LogManager.getLogger(Platba.class);
    private PlatbaKontroler kontroler;

    public Platba() {
        initOwner(Pos.getInstance().getAplikacniOkno());
    }

    public void zobrazDialog() {
        LOGER.info("Bude zobrazeno platební okno");
        //Toolkit.getDefaultToolkit().beep();
        try {
            this.initStyle(StageStyle.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Platba.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            kontroler = loader.getController();
            kontroler.setDialog(this);
            this.centerOnScreen();
            this.initModality(Modality.WINDOW_MODAL);
            this.show();
            kontroler.zobrazTlacitkaPlatebnichProstredku();
            kontroler.setTlacitkaHotovosti();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit dialog pro placení dokladu: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit platební okno: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            Platform.exit();
        }
    }

    public void zavriDialog(boolean probihaUzavreni) {
        if (!probihaUzavreni && !DokladProcesor.getInstance().getDoklad().getPlatby().isEmpty()) {
            LOGER.info("Byl zaregistrován požadavek na uzavření platebního okna. Existují platby - bude zobrazen potvrzovací dialog, jelikož platby budou při zavření smazány.");
            OtazkaAnoNe platbyExistuji = new OtazkaAnoNe("Odejít z platby?", "Přejete si odejít z platby? Veškeré registrované platby budou odebrány!", this);
            platbyExistuji.zobrazDialog();
            if (platbyExistuji.getVolba() == OtazkaAnoNe.Volby.ANO) {
                LOGER.info("Dialog byl potvrzen. Platební okno bude uzavřeno.");
                DokladProcesor.getInstance().getDoklad().getDataPlatbyKartou().clear();
                DokladProcesor.getInstance().getDoklad().getPlatby().clear();
                DokladProcesor.getInstance().aktualizujBinarniKopii();
                close();
            }
        } else {
            LOGER.info("Platební okno bude uzavřeno.");
            close();
        }
    }

    public PlatbaKontroler getKontroler() {
        return kontroler;
    }
}
