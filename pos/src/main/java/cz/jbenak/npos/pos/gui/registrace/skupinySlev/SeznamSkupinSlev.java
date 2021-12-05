package cz.jbenak.npos.pos.gui.registrace.skupinySlev;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.progres.ProgresDialog;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.slevy.SkupinaSlev;
import cz.jbenak.npos.pos.procesy.slevy.NacteniSeznamuSlev;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SeznamSkupinSlev extends Stage {

    private final static Logger LOGER = LogManager.getLogger(SeznamSkupinSlev.class);
    private final ProgresDialog progres;
    private final boolean jeVPlatbe;
    private final Stage vlastnik;
    private List<SkupinaSlev> skupiny;

    public SeznamSkupinSlev(Stage vlastnik, boolean jeVPlatbe) {
        initOwner(vlastnik);
        this.progres = new ProgresDialog(vlastnik);
        this.vlastnik = vlastnik;
        this.jeVPlatbe = jeVPlatbe;
    }

    public void zobrazDialog() {
        LOGER.debug("zahájeno načítání dat skupin slev pro zobrazení v dialogu.");
        NacteniSeznamuSlev nacteni = new NacteniSeznamuSlev(jeVPlatbe);
        progres.zobrazProgres("Načítám seznam aplikovatelných skupin slev.");
        nacteni.run();
        try {
            skupiny = nacteni.get();
            progres.zavriDialog();
            if (skupiny.isEmpty()) {
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nic jsem nenašel", "Bohužel jsem nenašel ani jednu použitelnou skupinu slev.", vlastnik).zobrazDialog(true);
            } else {
                zobrazGUI();
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGER.error("Nepodařilo se načíst seznam aplikovatelných skupin slev.", e);
            if (progres.isShowing()) {
                progres.zavriDialog();
            }
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se načíst seznam skupin slev \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }

    private void zobrazGUI() {
        LOGER.info("Bude zobrazeno GUI dialogu pro zobrazení seznamu aplikovatelných skupin slev");
        //Toolkit.getDefaultToolkit().beep();
        try {
            this.initStyle(StageStyle.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SeznamSkupinSlev.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            SeznamSkupinSlevKontroler kontroler = loader.getController();
            kontroler.setDialog(this);
            kontroler.setPolozky(skupiny);
            this.centerOnScreen();
            this.initModality(Modality.WINDOW_MODAL);
            this.show();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit dialog pro zobrazení načtených položek seznamu zákazníků: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit seznam načtených skupin slev \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            Platform.exit();
        }
    }

    protected void zaregistrujSkupinu(SkupinaSlev skupina) {

    }
}
