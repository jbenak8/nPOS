package cz.jbenak.npos.pos.procesy.platba;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.meny.Mena;
import cz.jbenak.npos.pos.objekty.meny.ObrazekNaTlacitku;
import cz.jbenak.npos.pos.system.Pos;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NacteniObrazkuNaTlacitka extends Task<List<ObrazekNaTlacitku>> {

    private static final Logger LOGER = LogManager.getLogger(NacteniObrazkuNaTlacitka.class);
    private final Connection spojeni = Pos.getInstance().getSpojeni();
    private final List<Mena> vybraneMeny;
    private final List<ObrazekNaTlacitku> obrazky = new ArrayList<>();
    private final List<Integer> vybraneDenominace = new ArrayList<>();

    public NacteniObrazkuNaTlacitka(List<Mena> vybraneMeny) {
        this.vybraneMeny = vybraneMeny;
    }

    @Override
    protected List<ObrazekNaTlacitku> call() throws Exception {
        LOGER.info("Budou načteny obrázky na hotovostní tlačítka.");
        String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".obrazky_platidel_na_tlacitka WHERE denominace = ANY (?) order by denominace ASC;";
        sestavSeznamDenominaci();
        try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            psmt.setArray(1, spojeni.createArrayOf("integer", vybraneDenominace.toArray()));
            ResultSet rs = psmt.executeQuery();
            if (rs != null) {
                if (rs.last()) {
                    int radek = rs.getRow();
                    rs.first();
                    while (rs.getRow() <= radek) {
                        ObrazekNaTlacitku obr = new ObrazekNaTlacitku();
                        obr.setCislo(rs.getInt("cislo_zaznamu"));
                        obr.setCisloDenominace(rs.getInt("denominace"));
                        obr.setObrazek(new Image(rs.getBinaryStream("obrazek")));
                        obrazky.add(obr);
                        if (rs.isLast()) {
                            break;
                        }
                        rs.next();
                    }
                }
            }
        }
         catch (Exception e) {
            LOGER.error("Nastala vnitřní chyba v procesu načítání obrázků na tlačítka hotovosti.", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Nemohu připravit platbu",
                    "Nastala chyba v procesu načítání obrázků na tlačítka hotovosti:\n\n" + e.getLocalizedMessage(),
                    Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            failed();
        }
        return obrazky;
    }

    private void sestavSeznamDenominaci() {
        vybraneMeny.forEach(mena -> mena.getDenominace().forEach(denominace -> {
            vybraneDenominace.add(denominace.getCislo());
        }));
    }
}
