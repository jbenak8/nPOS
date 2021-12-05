package cz.jbenak.npos.pos.gui.zakaznik.seznam;

import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.gui.sdilene.kontrolery.KontrolerTabulkovehoDialogu;
import cz.jbenak.npos.pos.gui.zakaznik.seznam.modely.PolozkaSeznamuZakazniku;
import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cz.jbenak.npos.pos.procesory.AutorizacniManazer.Funkce.*;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-12-30
 * <p>
 * Třída pro obsluhu okna se seznamem zákazníků.
 */

public class SeznamZakaznikuKontroler extends KontrolerTabulkovehoDialogu<SeznamZakazniku, Zakaznik, PolozkaSeznamuZakazniku> {

    public static final Logger LOGER = LogManager.getLogger(SeznamZakaznikuKontroler.class);
    @FXML
    private TableColumn<PolozkaSeznamuZakazniku, String> slCislo;
    @FXML
    private TableColumn<PolozkaSeznamuZakazniku, String> slBlokovany;
    @FXML
    private TableColumn<PolozkaSeznamuZakazniku, String> slJmeno;
    @FXML
    private TableColumn<PolozkaSeznamuZakazniku, String> slNazev;
    @FXML
    private TableColumn<PolozkaSeznamuZakazniku, String> slAdresa;

    @FXML
    private void tlInfoOPolozceStisknuto() {
        LOGER.debug("Tlačítko pro informace o vybraném zákazníkovi bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(INFO_O_ZAKAZNIKOVI)) {
            if (tabulka.getSelectionModel().getSelectedIndex() > -1) {
                dialog.zobrazDetailZakaznika(polozky.get(tabulka.getSelectionModel().getSelectedIndex()).getZakaznik());
            }
        }
    }

    private void zaregistrovatZakaznika() {
        LOGER.debug("Tlačítko pro registraci vybraného zákazníka bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(REGISTRACE_ZAKAZNIKA)) {
            if (tabulka.getSelectionModel().getSelectedIndex() > -1) {
                dialog.zaregistrujZakaznika(polozky.get(tabulka.getSelectionModel().getSelectedIndex()).getZakaznik());
            }
        }
    }

    @FXML
    private void tlPridatZakaznikaStisknuto() {
        LOGER.info("Tlačítko pro přidání nového zákazníka bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(ZALOZENI_ZAKAZNIKA)) {
            new DialogZpravy(DialogZpravy.TypZpravy.INFO, "Založení nového zákazníka", "Tato funkce zatím není naprogramována. Založení bude možné jen v online režimu.", dialog).zobrazDialogAPockej();
        }
    }

    @Override
    protected PolozkaSeznamuZakazniku getPolozkaTabulky(Zakaznik polozka) {
        return new PolozkaSeznamuZakazniku(polozka);
    }

    @Override
    protected void tlOkStisknuto() {
        zaregistrovatZakaznika();
    }

    @Override
    protected void zpracujRozsireneKlavesy(KeyEvent evt) {
        if (evt.getCode() == KeyCode.F3) {
            tlInfoOPolozceStisknuto();
        }
        if (evt.getCode() == KeyCode.F7) {
            tlPridatZakaznikaStisknuto();
        }
    }

    @Override
    protected boolean jePredikatOK(PolozkaSeznamuZakazniku predikat, String filtr, boolean predikatOK) {
        switch (filtrPodleSloupecku) {
            case 0:
                if (predikat.getCisloZakaznika().getValueSafe().toLowerCase().contains(filtr)) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Čísla.");
                }
                break;
            case 1: //TODO - booleanový komparátor
                if (predikat.getTextBlokovany().getValueSafe().toLowerCase().contains(filtr)) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Blokace.");
                }
                break;
            case 2:
                if (predikat.getJmeno().getValueSafe().toLowerCase().contains(filtr)) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Jména.");
                }
                break;
            case 3:
                if (predikat.getNazev().getValueSafe().toLowerCase().contains(filtr)) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Názvu.");
                }
                break;
            case 4:
                if (predikat.getAdresa().getValueSafe().toLowerCase().contains(filtr)) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Města.");
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
        slCislo.setCellValueFactory(data -> data.getValue().getCisloZakaznika());
        slBlokovany.setCellValueFactory(data -> data.getValue().getTextBlokovany());
        slNazev.setCellValueFactory(data -> data.getValue().getNazev());
        slJmeno.setCellValueFactory(data -> data.getValue().getJmeno());
        slAdresa.setCellValueFactory(data -> data.getValue().getAdresa());
    }
}
