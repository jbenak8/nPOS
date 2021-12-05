package cz.jbenak.npos.pos.gui.sdilene.kontrolery;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.registrace.hlavni.zobrazeniSkupinSortimentu.modely.PolozkaSeznamuSkupinSortimentu;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.ITextovaKlavesnice;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.PoleKVyplneni;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.TextovaKlavesnice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Třída pro vytvoření základního kontroléru pro dialogy zobrazující data v tabulce.
 *
 * @param <T> zobrazující dialog - třída, která se stará o zobrazení dialogu.
 * @param <K> typ zobrazovaných dat (např. sortiment, zákazník,...)
 * @param <V> typ tabulkového položkového modelu (objekt zobrazující data v jednotlivém řádku tabulky)
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-09-19
 */

@SuppressWarnings("unchecked")
public abstract class KontrolerTabulkovehoDialogu<T extends Stage, K, V> implements Initializable, ITextovaKlavesnice {

    protected static final Logger LOGER = LogManager.getLogger(KontrolerTabulkovehoDialogu.class);
    protected T dialog;
    protected final ObservableList<V> polozky = FXCollections.observableArrayList();
    protected int filtrPodleSloupecku = 0;
    private SortedList<V> vybranaData;
    @FXML
    protected TextField poleFiltr;
    @FXML
    protected TableView<V> tabulka;
    @FXML
    protected Label popisPocetPolozek;
    @FXML
    protected Label popisFiltrDle;

    /**
     * Nastaví okno dialogu
     *
     * @param dialog třída okna dialogu obsluhující funkce.
     */
    public final void setDialog(T dialog) {
        this.dialog = dialog;
    }

    /**
     * Nastaví položky zobrazovaných dat do položek tabulky
     *
     * @param data data k zobrazení.
     */
    public void setPolozky(List<K> data) {
        data.forEach(pol -> polozky.add(getPolozkaTabulky(pol)));
        tabulka.getSelectionModel().select(0);
        popisPocetPolozek.setText("Nalezeno " + polozky.size() + " položek.");
    }

    /**
     * Provede vlastní přesun datové položky do položky tabulky. Je zde možné upravovat vlastní zobrazení.
     *
     * @param polozka položka dat
     * @return položka řádku tabulky
     */
    protected abstract V getPolozkaTabulky(K polozka);

    /**
     * Akce hlavního potvrzovacího tlačítka
     */
    @FXML
    protected abstract void tlOkStisknuto();

    /**
     * Uzavře dialog.
     */
    @FXML
    protected void tlNeStisknuto() {
        LOGER.info("Volba byla odmítnuta. Dialog bude uzavřen.");
        dialog.close();
    }

    /**
     * Uzavře dialog.
     */
    @FXML
    protected void tlZavritStisknuto() {
        LOGER.info("Výběrový dialog bude uzavřen.");
        dialog.close();
    }

    /**
     * Vymaže pole s filtračním kritériem a resetuje tím tabulku.
     */
    @FXML
    protected void tlSmazatStisknuto() {
        poleFiltr.clear();
    }

    /**
     * Obsluhuje kliknutí na tlačítko přesun o jednu pozici nahoru.
     */
    @FXML
    protected void tlPolozkaNahoruStisknuto() {
        if (vybranaData.size() > 0) {
            if (tabulka.getSelectionModel().getSelectedIndex() > -1) {
                tabulka.getSelectionModel().selectPrevious();
            } else {
                tabulka.getSelectionModel().selectLast();
            }
            tabulka.scrollTo(tabulka.getSelectionModel().getSelectedIndex());
        }
    }

    /**
     * Obsluhuje kliknutí na tlačítko přesun o jednu pozici dolu.
     */
    @FXML
    protected void tlPolozkaDoluStisknuto() {
        if (vybranaData.size() > 0) {
            if (tabulka.getSelectionModel().getSelectedIndex() > -1) {
                tabulka.getSelectionModel().selectNext();
            } else {
                tabulka.getSelectionModel().selectFirst();
            }
            tabulka.scrollTo(tabulka.getSelectionModel().getSelectedIndex());
        }
    }

    /**
     * Obsluhuje kliknutí na tlačítko přesun o jednu stranu nahoru.
     */
    @FXML
    protected void tlStranaNahoruStisknuto() {
        if (vybranaData.size() > 0) {
            if ((tabulka.getSelectionModel().getSelectedIndex() - 4) >= 0) {
                tabulka.getSelectionModel().select(tabulka.getSelectionModel().getSelectedIndex() - 4);
            } else {
                tabulka.getSelectionModel().selectFirst();
            }
            tabulka.scrollTo(tabulka.getSelectionModel().getSelectedIndex());
        }
    }

    /**
     * Obsluhuje kliknutí na tlačítko přesun o jednu stranu dolu.
     */
    @FXML
    protected void tlStranaDoluStisknuto() {
        if (vybranaData.size() > 0) {
            if ((tabulka.getSelectionModel().getSelectedIndex() + 4) < vybranaData.size()) {
                tabulka.getSelectionModel().select(tabulka.getSelectionModel().getSelectedIndex() + 4);
            } else {
                tabulka.getSelectionModel().selectLast();
            }
            tabulka.scrollTo(tabulka.getSelectionModel().getSelectedIndex());
        }
    }

    /**
     * Implementace zobrazení textové klávesnice.
     */
    @Override
    public void tlZobrazitKlavesniciStisknuto() {
        List<PoleKVyplneni> pole = new LinkedList<>();
        pole.add(new PoleKVyplneni(poleFiltr, Pomocnici.TypyVstupnichHodnot.TEXT));
        TextovaKlavesnice klavesnice = new TextovaKlavesnice(this, dialog, pole);
        klavesnice.zobrazKlavesnici();
    }

    /**
     * Implementace události uzavření textové klávesnice.
     */
    @Override
    public void editaceUkoncena() {

    }

    /**
     * Zpracuje stisk klávesy. Zde se nabízí  potvrzení (Enter), uzavření (Esc), zobrazení textové klávesnice (F2) a obnovení filtru (F5).
     * Lze rozšířit implementací metody zpracujRozsireneKlavesy.
     *
     * @param evt Událost stisknutí klávesy.
     */
    @FXML
    protected void klavesaStisknuta(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ENTER) {
            tlOkStisknuto();
        }
        if (evt.getCode() == KeyCode.ESCAPE) {
            tlZavritStisknuto();
        }
        if (evt.getCode() == KeyCode.F5) {
            tlSmazatStisknuto();
        }
        if (evt.getCode() == KeyCode.F2) {
            tlZobrazitKlavesniciStisknuto();
        }
        zpracujRozsireneKlavesy(evt);
    }

    /**
     * Rozšiřuje základní události definované v metodě klavesaStisknuta.
     *
     * @param evt Událost stisku klávesy.
     */
    protected abstract void zpracujRozsireneKlavesy(KeyEvent evt);

    /**
     * Reaguje na kliknutí  tabulce.
     *
     * @param evt Událost myši.
     */
    @FXML
    protected void tabulkaKliknuto(MouseEvent evt) {//jiný typ eventu?
        if (this.vybranaData.size() > 0) {
            if (tabulka.getFocusModel().getFocusedCell().getColumn() >= 0) {
                nastavFiltrPodle(tabulka.getFocusModel().getFocusedCell().getColumn());
            }
        }
    }

    /**
     * Nastaví filtraci tabulky - zobrazí se již filtrovaná data.
     *
     * @param filtrovanaData filtrovaná data k zobrazení.
     */
    private void nastavFiltraci(FilteredList<V> filtrovanaData) {
        poleFiltr.textProperty().addListener((observable, puvodni, nove) -> filtrovanaData.setPredicate(predikat -> {
            boolean predikatOK = false;
            if (nove == null || nove.isEmpty()) {
                predikatOK = true;
            }
            String filtr = (nove == null) ? "" : nove.toLowerCase();
            return jePredikatOK(predikat, filtr, predikatOK);
        }));
    }

    /**
     * Stanovení jednotlivých predikátů pomocí switche pro jednotlivé sloupečky a rozhnodnutí o splnění.
     *
     * @param predikat   predikát - testovaná hodnota
     * @param filtr      text z pole filtru
     * @param predikatOK původní rozhodnutí
     * @return rozhodnutí o platnosti predikátu
     */
    protected abstract boolean jePredikatOK(V predikat, String filtr, boolean predikatOK);

    /**
     * Nastaví typ filtru podle označeného sloupku.
     *
     * @param vybranySloupek číslo vybraného sloupku
     */
    private void nastavFiltrPodle(int vybranySloupek) {
        TableColumn<PolozkaSeznamuSkupinSortimentu, String> sloupek = (TableColumn<PolozkaSeznamuSkupinSortimentu, String>) this.tabulka.getColumns().get(vybranySloupek);
        poleFiltr.setPromptText("Vyhledejte podle " + sloupek.getText());
        filtrPodleSloupecku = vybranySloupek;
    }

    /**
     * Implementace inicializace FX kontroléru.
     *
     * @param url url
     * @param rb  zdroje
     * @see Initializable
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poleFiltr.setPromptText("Vyhledejte podle ");
        tabulka.setPlaceholder(new Label("Tabulka neobsahuje žádný záznam."));
        polozky.clear();
        popisFiltrDle.setText("");
        tabulka.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        final FilteredList<V> filtrovanaData = new FilteredList<>(polozky, predikat -> true);
        inicializujSloupecky();
        vybranaData = new SortedList<>(filtrovanaData);
        vybranaData.comparatorProperty().bind(tabulka.comparatorProperty());
        tabulka.setItems(vybranaData);
        nastavFiltrPodle(0);
        nastavFiltraci(filtrovanaData);
    }

    /**
     * Rozšíření metody initialize() - zde pro definici dat ve sloupečcích.
     */
    protected abstract void inicializujSloupecky();
}
