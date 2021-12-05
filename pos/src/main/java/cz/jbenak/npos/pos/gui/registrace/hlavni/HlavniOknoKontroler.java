/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.registrace.hlavni.menu.dokladOtevren.MenuDokladOtevrenKontroler;
import cz.jbenak.npos.pos.gui.registrace.hlavni.menu.registracePripravena.MenuRegistracePripravenaKontroler;
import cz.jbenak.npos.pos.gui.registrace.hlavni.polozkaDokladu.PolozkaDokladuBunka;
import cz.jbenak.npos.pos.gui.sdilene.ciselnaKlavesnice.CiselnaKlavesniceKontroler;
import cz.jbenak.npos.pos.gui.sdilene.ciselnaKlavesnice.ICiselnaKlavesnice;
import cz.jbenak.npos.pos.objekty.adresy.Adresa;
import cz.jbenak.npos.pos.objekty.partneri.Zakaznik;
import cz.jbenak.npos.pos.objekty.sezeni.Pokladni;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;
import cz.jbenak.npos.pos.procesory.DokladProcesor;
import cz.jbenak.npos.pos.procesory.RegistracniProcesor;
import cz.jbenak.npos.pos.system.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-04
 */
public class HlavniOknoKontroler implements Initializable, ICiselnaKlavesnice {

    private final Image obrBoOnline = new Image(getClass().getResourceAsStream("/cz/jbenak/npos/pos/gui/registrace/hlavni/img/spojeni_ok.png"));
    private final Image obrBoOffline = new Image(getClass().getResourceAsStream("/cz/jbenak/npos/pos/gui/registrace/hlavni/img/spojeni_neni.png"));
    private final ObservableList<Sortiment> polozky = FXCollections.observableArrayList();
    private HlavniOkno okno;
    @FXML
    private CiselnaKlavesniceKontroler ciselnaKlavesniceController;
    @FXML
    private MenuRegistracePripravenaKontroler menuRegistracePripravenaController;
    @FXML
    private MenuDokladOtevrenKontroler menuDokladOtevrenController;
    @FXML
    private ListView<Sortiment> lvDoklad;
    @FXML
    private TextField poleVstup;
    @FXML
    private Label popisRezimVratky;
    @FXML
    private Label popisPokladni;
    @FXML
    private Label popisCisloPokladny;
    @FXML
    private Label popisZakaznik;
    @FXML
    private ImageView ivZakaznik;
    @FXML
    private ImageView ivParkovani;
    @FXML
    private ImageView ivBoOnline;
    @FXML
    private Label popisDatum;
    @FXML
    private Label popisCas;
    @FXML
    private Label celkem;
    @FXML
    private ImageView ivDoklad;
    @FXML
    private Label popisDokladNazev;
    @FXML
    private Label popisPocetPolozek;
    @FXML
    private ImageView ivSkoleni;

    /**
     * Zobrazí ikonu zaparkovaného dokladu.
     *
     * @param zaparkovano zobrazit ikonu
     */
    void setZaparkovanyDoklad(boolean zaparkovano) {
        ivParkovani.setVisible(zaparkovano);
    }

    /**
     * Nastaví seznam položek dokladu a zároveň zobrazí celkovou částku. Zároveň zobrazí správné menu.
     *
     * @param polozky Položky dokladu
     */
    public void setPolozky(List<Sortiment> polozky) {
        this.polozky.clear();
        this.polozky.addAll(polozky);
        celkem.setText(Pomocnici.formatujNaDveMista(DokladProcesor.getInstance().getDoklad().getCenaDokladuCelkem()) + " " + Pos.getInstance().getHlavniMena().getNarodniSymbol());
        if (polozky.isEmpty()) {
            zobrazVychoziMenu();
        } else {
            zobrazRegistracniMenu();
        }
        if (!polozky.isEmpty()) {
            lvDoklad.scrollTo(lvDoklad.getSelectionModel().getSelectedIndex());
        }
    }

    /**
     * Smaže položky v dokladu a vyresetuje okno. Tato metoda se používá při zahazování rozpracovaného dokladu.
     */
    public void smazPolozky() {
        BigDecimal cena = BigDecimal.ZERO;
        this.polozky.clear();
        celkem.setText(Pomocnici.formatujNaDveMista(cena) + " " + Pos.getInstance().getHlavniMena().getNarodniSymbol());
        if (ivZakaznik.isVisible()) {
            smazZakaznika();
        }
        zobrazVychoziMenu();
    }

    /**
     * Zobrazí ikonu stavu připojení na BO.
     *
     * @param online true - online, false - offline
     */
    public void setBoOnline(boolean online) {
        ivBoOnline.setImage(online ? obrBoOnline : obrBoOffline);
    }

    /**
     * Aktivuje ikonu školícího režimu.
     *
     * @param zapnuto ikona aktivní.
     */
    public void setSkoliciRezim(boolean zapnuto) {
        ivSkoleni.setVisible(zapnuto);
        okno.setSkoliciRezimZapnut(zapnuto);
    }

    /**
     * Aktivuje informaci o aktivním režimu vratky.
     *
     * @param zapnuto režim vratky aktivní.
     */
    public void setRezimVratky(boolean zapnuto) {
        popisRezimVratky.setVisible(zapnuto);
    }

    /**
     * Zobrazí info o zaparkovaném dokladu.
     *
     * @param zaparkovano doklad zaparkován.
     */
    public void setParkovano(boolean zaparkovano) {
        ivParkovani.setVisible(zaparkovano);
    }

    /**
     * Vrátí instanci zadávacího pole pro použití v menu.
     *
     * @return hlavní zadávací pole.
     */
    public TextField getPoleVstup() {
        return poleVstup;
    }

    /**
     * Zobrazí registrační menu, které je platné pro otevřený doklad.
     */
    public void zobrazRegistracniMenu() {
        menuRegistracePripravenaController.zobrazMenu(false);
        menuDokladOtevrenController.zobrazPanel(true);
    }

    /**
     * Zobrazí menu, které je výchozí - dokud doklad není otevřen.
     */
    public void zobrazVychoziMenu() {
        menuRegistracePripravenaController.zobrazMenu(true);
        menuDokladOtevrenController.zobrazPanel(false);
    }

    /**
     * Nastaví instanci hlavního okna.
     *
     * @param okno hlavní okno
     */
    void setOkno(HlavniOkno okno) {
        this.okno = okno;
    }

    /**
     * Nastaví pokladního a číslo pokladny ve stavovém panelu.
     */
    void setPokladniData() {
        Pokladni pokladni = HlavniOkno.getInstance().getPrihlasenaPokladni();
        popisPokladni.setText(pokladni.getId() + " - "
                + ((pokladni.getJmeno() == null) ? "" : pokladni.getJmeno())
                + " " + ((pokladni.getPrijmeni() == null) ? "" : pokladni.getPrijmeni()));
        popisCisloPokladny.setText(Pos.getInstance().getNastaveni().getProperty("pos.cislo"));
    }

    /**
     * Metoda pro získání právě vybrané položky v registrovaném seznamu položek.
     *
     * @return Vybraná položka.
     */
    public Sortiment getVybranaPolozka() {
        return lvDoklad.getSelectionModel().getSelectedItem();
    }

    /**
     * Zobrazí informaci o zaregistrovaném zákazníkovi a aktualizuje zákaznický displej.
     *
     * @param zakaznik vybraný registrovaný zákazník.
     */
    public void zobrazZakaznika(Zakaznik zakaznik) {
        ivZakaznik.setVisible(true);
        String prvniRadka = (zakaznik.getNazev() != null ? zakaznik.getNazev() : ((zakaznik.getJmeno() == null ? "" :
                zakaznik.getJmeno()) + (zakaznik.getPrijmeni() == null ? "" : " " + zakaznik.getPrijmeni())));
        Adresa a = null;
        for (Adresa adr : zakaznik.getAdresy()) {
            if (adr.isHlavni()) {
                a = adr;
                break;
            }
        }
        String druhyRadek = "";
        String tretiRadek = "";
        if (a != null) {
            druhyRadek = a.getUlice() == null ? "" : a.getUlice() +" ";
            druhyRadek += (a.getCp() == null ? "" : a.getCp()) + (a.getCor() == null ? "" : "/" + a.getCor());
            tretiRadek = (a.getPsc() == null ? "" : a.getPsc()) + (a.getMesto() == null ? "" : " " + a.getMesto());
        }
        popisZakaznik.setText(prvniRadka + "\n" + druhyRadek + "\n" + tretiRadek);
        zobrazRegistracniMenu();

    }

    /**
     * Odebere (zruší zobrazení) registrovaného zákazníka. Zárověň aktualizuje zákaznický displej.
     */
    public void smazZakaznika() {
        ivZakaznik.setVisible(false);
        popisZakaznik.setText("");
    }

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ciselnaKlavesniceController.setPrijemce(this);
        menuRegistracePripravenaController.setHlKontroler(this);
        menuDokladOtevrenController.setHlKontroler(this);
        poleVstup.setPromptText("Registr, EAN nebo název");
        ImageView logo = new ImageView(getLogo());
        lvDoklad.setPlaceholder(logo);
        popisZakaznik.setText("");
        ivZakaznik.setVisible(false);
        ivDoklad.setVisible(false);
        popisDokladNazev.setVisible(false);
        popisPocetPolozek.setText("");
        celkem.setText(Pomocnici.formatujNaDveMista(BigDecimal.ZERO) + " " + Pos.getInstance().getHlavniMena().getNarodniSymbol());
        popisRezimVratky.setVisible(false);
        ivSkoleni.setVisible(false);
        zobrazDatumACas();
        lvDoklad.setCellFactory(bunka -> new PolozkaDokladuBunka());
        lvDoklad.setItems(this.polozky);
        setPolozkovyListener();
        if (DokladProcesor.getInstance().jeOtevrenDoklad()) {
            zobrazRegistracniMenu();
        }
    }

    @Override
    public void predejZnak(String znak) {
        poleVstup.setText(poleVstup.getText() + znak);
    }

    @Override
    public void umazZnak() {
        if (!poleVstup.getText().isEmpty()) {
            poleVstup.setText(poleVstup.getText().substring(0, poleVstup.getText().length() - 1));
        }
    }

    @Override
    public void smazVstup() {
        poleVstup.clear();
    }

    @Override
    public void potvrdVstup() {
        if (!poleVstup.getText().trim().isEmpty()) {
            RegistracniProcesor.getInstance().zaregistrujPolozkuVolne(poleVstup.getText().trim());
            poleVstup.clear();
        } else {
            menuDokladOtevrenController.tlHledatSortimnentStisknuto();
        }
    }

    private Image getLogo() {
        File logo = new File(Pos.getInstance().getNastaveni().getProperty("logo.zobrazeni"));
        if (logo.exists()) {
            return new Image("file:" + Pos.getInstance().getNastaveni().getProperty("logo.zobrazeni"));
        } else {
            return new Image(getClass().getResourceAsStream("/cz/jbenak/npos/pos/gui/img/ikona.png"));
        }
    }

    private synchronized void zobrazDatumACas() {
        Service<Void> zobrazovacData = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new DatumCasTask(popisCas, popisDatum);
            }
        };
        zobrazovacData.start();
    }

    private void setPolozkovyListener() {
        polozky.addListener((ListChangeListener.Change<? extends Sortiment> c) -> {
            if (polozky.isEmpty()) {
                ivDoklad.setVisible(false);
                popisDokladNazev.setVisible(false);
                popisPocetPolozek.setText("");
            } else {
                ivDoklad.setVisible(true);
                popisDokladNazev.setVisible(true);
                popisPocetPolozek.setText(Integer.toString(polozky.size()));
                lvDoklad.getSelectionModel().selectLast();
            }
        });
    }

    @FXML
    private void poleVstupKlavesaStisknuta(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ENTER) {
            potvrdVstup();
        }
        if (evt.getCode() == KeyCode.ESCAPE) {
            smazVstup();
        }
        if (evt.getCode() == KeyCode.MULTIPLY) {
            menuRegistracePripravenaController.tlMnozstviStisknuto();
            smazVstup();
        }
    }

    @FXML
    private void znakNapsan(KeyEvent evt) {
        if (poleVstup.getText().contains("*")) {
            smazVstup();
        }
    }

    @FXML
    private void klavesaObecneStisknuta(KeyEvent evt) {
        if (evt.getCode() == KeyCode.UP || evt.getCode() == KeyCode.KP_UP) {
            tlPolozkaNahoruStisknuto();
        }
        if (evt.getCode() == KeyCode.DOWN || evt.getCode() == KeyCode.KP_DOWN) {
            tlPolozkaDoluStisknuto();
        }
        if (evt.getCode() == KeyCode.PAGE_UP) {
            tlStranaNahoruStisknuto();
        }
        if (evt.getCode() == KeyCode.PAGE_DOWN) {
            tlStranaDoluStisknuto();
        }
        if (lvDoklad.isFocused() && !polozky.isEmpty()) {
            if (evt.getCode() == KeyCode.MULTIPLY) {
                menuDokladOtevrenController.tlMnozstviStisknuto();
            }
            if (evt.getCode() == KeyCode.F8 || evt.getCode() == KeyCode.DELETE) {
                menuDokladOtevrenController.tlStornoPolozkyStisknuto();
            }
            if (evt.getCode() == KeyCode.F5) {
                menuDokladOtevrenController.tlOpakovatPolozkuStisknuto();
            }
        }
        if (!poleVstup.isFocused()) {
            poleVstup.requestFocus();
        }
    }

    @FXML
    private void tlPolozkaNahoruStisknuto() {
        if (polozky.size() > 0) {
            if (lvDoklad.getSelectionModel().getSelectedIndex() > -1) {
                lvDoklad.getSelectionModel().selectPrevious();
            } else {
                lvDoklad.getSelectionModel().selectLast();
            }
            lvDoklad.scrollTo(lvDoklad.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void tlPolozkaDoluStisknuto() {
        if (polozky.size() > 0) {
            if (lvDoklad.getSelectionModel().getSelectedIndex() > -1) {
                lvDoklad.getSelectionModel().selectNext();
            } else {
                lvDoklad.getSelectionModel().selectFirst();
            }
            lvDoklad.scrollTo(lvDoklad.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void tlStranaNahoruStisknuto() {
        if (polozky.size() > 0) {
            if ((lvDoklad.getSelectionModel().getSelectedIndex() - 5) >= 0) {
                lvDoklad.getSelectionModel().select(lvDoklad.getSelectionModel().getSelectedIndex() - 5);
            } else {
                lvDoklad.getSelectionModel().selectFirst();
            }
            lvDoklad.scrollTo(lvDoklad.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void tlStranaDoluStisknuto() {
        if (polozky.size() > 0) {
            if ((lvDoklad.getSelectionModel().getSelectedIndex() + 4) < polozky.size()) {
                lvDoklad.getSelectionModel().select(lvDoklad.getSelectionModel().getSelectedIndex() + 4);
            } else {
                lvDoklad.getSelectionModel().selectLast();
            }
            lvDoklad.scrollTo(lvDoklad.getSelectionModel().getSelectedIndex());
        }
    }

}
