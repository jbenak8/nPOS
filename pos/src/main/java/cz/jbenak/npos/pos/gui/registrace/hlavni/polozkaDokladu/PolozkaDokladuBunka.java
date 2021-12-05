/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni.polozkaDokladu;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.slevy.Sleva;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;
import cz.jbenak.npos.pos.procesory.ManazerSlev;
import cz.jbenak.npos.pos.system.Pos;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Buňka pro zobrazení položky dokladu.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-07
 */
public class PolozkaDokladuBunka extends ListCell<Sortiment> {

    private final static Logger LOGER = LogManager.getLogger(PolozkaDokladuBunka.class);
    private PolozkaDokladuKontroler kontroler;
    private VBox bunka;

    /**
     * Přepracovat - zdá se mi, že se při každé úpravě objektu znovu načítá FXML
     *
     * @param item
     * @param empty
     */
    @Override
    protected void updateItem(Sortiment item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            nactiGUI();
            nastavPolozky(item);
            setGraphic(bunka);
        }
    }

    private void nactiGUI() {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("PolozkaDokladu.fxml"));
            bunka = loader.load();
            kontroler = loader.getController();
        } catch (IOException e) {
            LOGER.error("Nebylo možné načíst GUI jednotlivé položky dokladu.", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit položku dokladu: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }

    private void nastavPolozky(Sortiment polozka) {
        if (polozka.jePrazdny()) {
            kontroler.setPrazdnaPolozka(true);
            kontroler.setCelkem(BigDecimal.ZERO, polozka.isVracena());
            kontroler.setMnozstvi(polozka.getMnozstvi());
        } else {
            kontroler.setMnozstviAJednotka(polozka.getMnozstvi(), polozka.getMj().getJednotka());
            kontroler.setNazev(polozka.isVracena() ? "VRATKA " + polozka.getNazev() : polozka.getNazev());
            kontroler.setJednotkovaCena(polozka.getJednotkovaCena());
            kontroler.setCelkem(polozka.getCenaCelkemBezeSlev(), polozka.isVracena());
            if (polozka.isVracena()) {
                kontroler.zobrazJakoVratku();
            }
            if (polozka.getSlevy() != null) {
                Optional<Sleva> akce = polozka.getSlevy().parallelStream().filter(pol -> pol.getTyp() == Sleva.TypSlevy.AKCE).findFirst();
                Optional<Sleva> slRucni = polozka.getSlevy().parallelStream().filter(pol -> pol.getPuvod() == Sleva.TypPuvodu.RUCNI).findFirst();
                Optional<Sleva> slZakaznicka = polozka.getSlevy().parallelStream().filter(pol -> pol.getPuvod() == Sleva.TypPuvodu.ZAKAZNICKA).findFirst();
                if (akce.isPresent()) {
                    kontroler.zobrazAkci(true);
                    kontroler.setAkce(akce.get().getPopis());
                    kontroler.setAkcniSleva(ManazerSlev.spoctiHodnotuSlevy(polozka, akce.get()));
                }
                if (slRucni.isPresent()) {
                    kontroler.zobrazSlevu(true);
                    String velikost = slRucni.get().getTyp() == Sleva.TypSlevy.HODNOTOVA
                            ? polozka.getMnozstvi().toPlainString() + " × " + Pomocnici.formatujNaDveMista(slRucni.get().getHodnota())
                            : slRucni.get().getHodnota().toPlainString().replace(".", ",") + " %";
                    kontroler.setVelikostSlevy(velikost);
                    kontroler.setHodnotuSlevy(ManazerSlev.spoctiHodnotuSlevy(polozka, slRucni.get()));
                }
                if (slZakaznicka.isPresent()) {
                    kontroler.zobrazZakaznickouSlevu(true);
                    kontroler.setHodnotuZakaznickeSlevy(ManazerSlev.spoctiHodnotuSlevy(polozka, slZakaznicka.get()));
                }
            }
        }
    }
}
