package cz.jbenak.npos.pos.gui.platba.platebniOkno.polozka;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.doklad.UplatnenyPoukaz;
import cz.jbenak.npos.pos.objekty.platba.PolozkaPlatby;
import cz.jbenak.npos.pos.procesory.DokladProcesor;
import cz.jbenak.npos.pos.system.Pos;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PolozkaPlatbyBunka extends ListCell<PolozkaPlatby> {

    private static final Logger LOGER = LogManager.getLogger(PolozkaPlatbyBunka.class);
    private PolozkaPlatbyBunkaKontroler kontroler;
    private BorderPane bunka;

    @Override
    protected void updateItem(PolozkaPlatby polozkaPlatby, boolean prazdne) {
        super.updateItem(polozkaPlatby, prazdne);
        if (prazdne) {
            setGraphic(null);
        } else {
            nactiGUI();
            nastavPolozky(polozkaPlatby);
            setGraphic(bunka);
        }
    }

    private void nactiGUI() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("PolozkaPlatbyBunka.fxml"));
            bunka = loader.load();
            kontroler = loader.getController();
        } catch (Exception e) {
            LOGER.error("Nebylo možné načíst GUI jednotlivé položky platby.", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit položku platby: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }

    private void nastavPolozky(PolozkaPlatby polozka) {
        kontroler.setCastka(polozka.getCastkaVKmenoveMene(), Pos.getInstance().getHlavniMena().getNarodniSymbol());
        switch (polozka.getPlatebniProstredek().getTyp()) {
            case KARTA -> {
                kontroler.setNazevPolozky(polozka.getPlatebniProstredek().getOznaceni());
                kontroler.setPopisPolozky("");
            }
            case POUKAZ -> {
                kontroler.setNazevPolozky(polozka.getPlatebniProstredek().getOznaceni());
                for (UplatnenyPoukaz poukaz: DokladProcesor.getInstance().getDoklad().getUplatnenePoukazy()) {
                    if (poukaz.getVazanaPolozkaPlatby() == polozka){
                        kontroler.setPopisPolozky("číslo " + poukaz.getCisloPoukazu());
                    }
                }
            }
            case HOTOVOST -> {
                String nazev = polozka.getPlatebniProstredek().getOznaceni();
                if (!polozka.getMena().isKmenova()) {
                    nazev += (" " + Pomocnici.formatujNaDveMista(polozka.getCastka()) + " " + polozka.getMena().getNarodniSymbol());
                }
                kontroler.setNazevPolozky(nazev);
                if (polozka.getKurzMeny() != null) {
                    kontroler.setPopisPolozky("Platný kurz: 1 "+Pos.getInstance().getHlavniMena().getIsoKod() + " = "
                    + Pomocnici.formatujNaTriMista(polozka.getKurzMeny().getNakup()) + " " + polozka.getMena().getIsoKod());
                }
            }
        }
    }
}
