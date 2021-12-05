package cz.jbenak.npos.pos.gui.zakaznik.seznam;


import cz.jbenak.npos.pos.gui.sdilene.dialogy.progres.ProgresDialog;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.gui.zakaznik.detail.DetailZakaznika;
import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
import cz.jbenak.npos.pos.procesory.RegistracniProcesor;
import cz.jbenak.npos.pos.procesy.zakaznik.NacteniDetailuVybranehoZakaznika;
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

public class SeznamZakazniku extends Stage {

    private static final Logger LOGER = LogManager.getLogger(SeznamZakazniku.class);
    private final List<Zakaznik> zakaznici;
    private final ProgresDialog progres;

    public SeznamZakazniku(List<Zakaznik> zakaznici, Stage vlastnik) {
        initOwner(vlastnik);
        this.progres = new ProgresDialog(vlastnik);
        this.zakaznici = zakaznici;
    }

    /**
     * Vlastní zobrazení seznamu.
     */
    public void zobrazDialog() {
        LOGER.info("Bude zobrazen dialog pro výběr načtených položek sortimentu.");
        //Toolkit.getDefaultToolkit().beep();
        try {
            this.initStyle(StageStyle.TRANSPARENT);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SeznamZakazniku.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            SeznamZakaznikuKontroler kontroler = loader.getController();
            kontroler.setDialog(this);
            kontroler.setPolozky(zakaznici);
            this.centerOnScreen();
            this.initModality(Modality.WINDOW_MODAL);
            this.show();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit dialog pro zobrazení načtených položek seznamu zákazníků: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit seznam načtených zákazníků \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            Platform.exit();
        }
    }

    void zobrazDetailZakaznika(Zakaznik vybranyZakaznik) {
        LOGER.info("Bude zobrazen dialog s detailem vybraného zákazníka s číslem {}.", vybranyZakaznik.getCislo());
        vybranyZakaznik = nactiDetailZakaznika(vybranyZakaznik);
        if (progres.isShowing()) {
            progres.zavriDialog();
        }
        if (vybranyZakaznik != null) {
            DetailZakaznika detailDialog = new DetailZakaznika(vybranyZakaznik, this);
            detailDialog.zobrazDialog(false);
        }
    }

    void zaregistrujZakaznika(Zakaznik vybranyZakaznik) {
        LOGER.info("Bude zaregistrován vybraný zákazník s číslem {} do aktuálního dokladu.", vybranyZakaznik.getCislo());
        if (vybranyZakaznik.isBlokovan()) {
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Blokovaného zákazníka neberu", """
                    Nemohu zaregistrovat zákazníka, který byl nějakým způsobem zablokován.

                    Vyberte prosím jiného, nebo požádejte vedení o odblokování.""", this).zobrazDialogAPockej();
        } else if (vybranyZakaznik.isDetailNacten()) {
            vybranyZakaznik = nactiDetailZakaznika(vybranyZakaznik);
            if (vybranyZakaznik != null) {
                progres.setText("Vybraný zákazník s číslem {} je registrován do aktuálního dokladu.", new String[]{vybranyZakaznik.getCislo()});
                RegistracniProcesor.getInstance().zaregistrujZakaznika(vybranyZakaznik);
                if (progres.isShowing()) {
                    progres.zavriDialog();
                }
                LOGER.info("Proces registrace vybraného zákazníka s číslem {} do aktuálního dokladu byl dokončen. Dialog bude nyní uzavřen.", vybranyZakaznik.getCislo());
                this.close();
            }
        } else {
            vybranyZakaznik = nactiDetailZakaznika(vybranyZakaznik);
            assert vybranyZakaznik != null;
            progres.setText("Vybraný zákazník s číslem {} je registrován do aktuálního dokladu.", new String[]{vybranyZakaznik.getCislo()});
            RegistracniProcesor.getInstance().zaregistrujZakaznika(vybranyZakaznik);
            progres.zavriDialog();
            LOGER.info("Proces registrace vybraného zákazníka s číslem {} do aktuálního dokladu byl dokončen. Dialog bude nyní uzavřen.", vybranyZakaznik.getCislo());
            this.close();
        }
    }

    private Zakaznik nactiDetailZakaznika(Zakaznik vybranyZakaznik) {
        NacteniDetailuVybranehoZakaznika nacteni = new NacteniDetailuVybranehoZakaznika(vybranyZakaznik);
        progres.zobrazProgres("Data vybraného zákazníka s číslem {} se načítají", new String[]{vybranyZakaznik.getCislo()});
        nacteni.run();
        try {
            if (nacteni.get()) {
                return vybranyZakaznik;
            } else {
                throw new ExecutionException(new Throwable("Detail vybraného zákazníka s číslem" + vybranyZakaznik.getCislo() + " nebylo možno načíst."));
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGER.error("Nepodařilo se načíst detail vybraného zákazníka.", e);
            if (progres.isShowing()) {
                progres.zavriDialog();
            }
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se načíst detail vybraného zákazníka: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            return null;
        }
    }
}
