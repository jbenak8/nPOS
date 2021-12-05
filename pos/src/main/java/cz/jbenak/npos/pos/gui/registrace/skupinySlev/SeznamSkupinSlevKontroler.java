package cz.jbenak.npos.pos.gui.registrace.skupinySlev;

import cz.jbenak.npos.pos.gui.registrace.skupinySlev.modely.PolozkaSeznamuSlev;
import cz.jbenak.npos.pos.gui.sdilene.kontrolery.KontrolerTabulkovehoDialogu;
import cz.jbenak.npos.pos.objekty.slevy.SkupinaSlev;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

public class SeznamSkupinSlevKontroler extends KontrolerTabulkovehoDialogu<SeznamSkupinSlev, SkupinaSlev, PolozkaSeznamuSlev> {

    private static final Logger LOGER = LogManager.getLogger(SeznamSkupinSlevKontroler.class);
    @FXML
    private TableColumn<PolozkaSeznamuSlev, Integer> slCislo;
    @FXML
    private TableColumn<PolozkaSeznamuSlev, String> slNazev;
    @FXML
    private TableColumn<PolozkaSeznamuSlev, String> slCil;
    @FXML
    private TableColumn<PolozkaSeznamuSlev, String> slRozsah;
    @FXML
    private TableColumn<PolozkaSeznamuSlev, String> slHodnota;

    @FXML
    private void tlInfoOPolozceStisknuto() {
        LOGER.debug("Tlačítko pro informace o vybrané skupině slev bylo stisknuto.");
    }

    @Override
    protected PolozkaSeznamuSlev getPolozkaTabulky(SkupinaSlev polozka) {
        return new PolozkaSeznamuSlev(polozka);
    }

    @Override
    protected void tlOkStisknuto() {
        LOGER.debug("Tlačítko pro registraci vybrané skupiny slev bylo stisknuto.");
        if (tabulka.getSelectionModel().getSelectedIndex() > -1) {
            dialog.zaregistrujSkupinu(polozky.get(tabulka.getSelectionModel().getSelectedIndex()).getSkupina());
        }
    }

    @Override
    protected void zpracujRozsireneKlavesy(KeyEvent evt) {
        if (evt.getCode() == KeyCode.F3) {
            tlInfoOPolozceStisknuto();
        }
    }

    @Override
    protected boolean jePredikatOK(PolozkaSeznamuSlev predikat, String filtr, boolean predikatOK) {
        switch (filtrPodleSloupecku) {
            case 0:
                if (Integer.toString(predikat.getCislo().getValue()).contains(filtr)) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Čísla.");
                }
                break;
            case 1:
                if (predikat.getNazev().getValueSafe().toLowerCase().contains(filtr.toLowerCase(Locale.ROOT))) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Názvu.");
                }
                break;
            case 2:
                if (predikat.getTypCile().getValueSafe().toLowerCase().contains(filtr.toLowerCase(Locale.ROOT))) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Cíle.");
                }
                break;
            case 3:
                if (predikat.getRozsah().getValueSafe().toLowerCase().contains(filtr.toLowerCase(Locale.ROOT))) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Kdy.");
                }
                break;
            case 4:
                if (predikat.getHodnota().getValueSafe().toLowerCase().contains(filtr.toLowerCase(Locale.ROOT))) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Hodnoty.");
                }
                break;
            default:
                predikatOK = false;
                popisFiltrDle.setText("");
                break;
        }
        return predikatOK;
    }

    @Override
    protected void inicializujSloupecky() {
        slCislo.setCellValueFactory(data -> data.getValue().getCislo().asObject());
        slNazev.setCellValueFactory(data -> data.getValue().getNazev());
        slCil.setCellValueFactory(data -> data.getValue().getTypCile());
        slRozsah.setCellValueFactory(data -> data.getValue().getRozsah());
        slHodnota.setCellValueFactory(data -> data.getValue().getHodnota());
    }
}
