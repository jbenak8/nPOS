package cz.jbenak.npos.pos.gui.registrace.hlavni.zobrazeniSkupinSortimentu;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.sortiment.SkupinaSortimentu;
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
import java.util.List;

/**
 * Třída dialogu zobrazující seznam načtených skupin sortimentu. Zde se pak provede výběr jedné skupiny.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-09-16
 */
public class ZobrazeniSkupinSortimentu extends Stage {

    private final static Logger LOGER = LogManager.getLogger(ZobrazeniSkupinSortimentu.class);
    private final List<SkupinaSortimentu> skupiny;
    private SkupinaSortimentu vybranaSkupina;

    /**
     * Konstruktor dialogu pro výběr skupiny sortimentu ze seznamu
     *
     * @param skupiny  seznam skupin sortimentu získaný pomocí procesu načení skupin sortimentu
     * @param vlastnik okno rodičovského dialogu (vlastníka)
     * @see SkupinaSortimentu
     * @see cz.jbenak.npos.pos.procesy.polozka.vyhledavani.NacteniSeznamuSkupinSortimentu
     */
    public ZobrazeniSkupinSortimentu(List<SkupinaSortimentu> skupiny, Window vlastnik) {
        this.initOwner(vlastnik);
        this.skupiny = skupiny;
    }

    /**
     * Zobrazí dialog pro výběr skupiny sortimentu.
     */
    public void zobrazDialog() {
        LOGER.info("Zobrazí se seznam skupin sortimentu.");
        try {
            if (this.getStyle() != StageStyle.TRANSPARENT) {
                this.initStyle(StageStyle.TRANSPARENT);
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ZobrazeniSkupinSortimentu.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            ZobrazeniSkupinSortimentuKontroler kontroler = loader.getController();
            kontroler.setDialog(this);
            kontroler.setPolozky(skupiny);
            this.centerOnScreen();
            if (this.getModality() != Modality.WINDOW_MODAL) {
                this.initModality(Modality.WINDOW_MODAL);
            }
            this.showAndWait();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit dialog se seznamem skupin sortimentu: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit dialog se seznamem skupin sortimentu: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }

    /**
     * Nastaví vybranou skupinu sortimentu.
     *
     * @param skupina vybraná skupina ze seznamu.
     */
    void setVybranaSkupina(SkupinaSortimentu skupina) {
        this.vybranaSkupina = skupina;
    }

    /**
     * Vrátí vybranou skupinu sortimentu, nebo null, pokud skupina nebyla vybrána.
     *
     * @return vybraná skupina sortimentu nebo null.
     */
    public SkupinaSortimentu getVybranaSkupina() {
        return vybranaSkupina;
    }
}
