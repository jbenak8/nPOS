/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.platba.platebniOkno;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.platba.platebniOkno.polozka.PolozkaPlatbyBunka;
import cz.jbenak.npos.pos.gui.sdilene.ciselnaKlavesnice.CiselnaKlavesniceKontroler;
import cz.jbenak.npos.pos.gui.sdilene.ciselnaKlavesnice.ICiselnaKlavesnice;
import cz.jbenak.npos.pos.objekty.meny.Denominace;
import cz.jbenak.npos.pos.objekty.meny.ObrazekNaTlacitku;
import cz.jbenak.npos.pos.objekty.platba.PlatebniProstredek;
import cz.jbenak.npos.pos.objekty.platba.PolozkaPlatby;
import cz.jbenak.npos.pos.procesory.DokladProcesor;
import cz.jbenak.npos.pos.procesory.PlatebniProcesor;
import cz.jbenak.npos.pos.system.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author jbena
 */
public class PlatbaKontroler implements Initializable, ICiselnaKlavesnice {

    private Platba dialog;
    private final Map<Integer, Denominace> denominaceNaTlacitkachHotovosti = new HashMap<>();
    private final ObservableList<PolozkaPlatby> polozky = FXCollections.observableArrayList();
    @FXML
    private CiselnaKlavesniceKontroler ciselnaKlavesniceController;
    private boolean vratka;
    @FXML
    private Label celkem;
    @FXML
    private Label nadpisZaplatit;
    @FXML
    private Label popisSleva;
    @FXML
    private Label hodnotaSleva;
    @FXML
    private ListView<PolozkaPlatby> lvPolozky;
    @FXML
    private TextField poleVstup;
    @FXML
    private Button tlZmenaMeny;
    @FXML
    private Button tlPoukaz;
    @FXML
    private Button tlPridatSlevu;
    @FXML
    private Button tlOdebratSlevu;


    public void zobrazCelkem() {
        String rezim = "Zaplatit:";
        if (vratka) {
            rezim = "Vrátit:";
        }
        celkem.setText(Pomocnici.formatujNaDveMista(PlatebniProcesor.getInstance().getKZaplaceniVeVybraneMene()) + " " + PlatebniProcesor.getInstance().getVybranaMena().getNarodniSymbol());
        if (PlatebniProcesor.getInstance().getVybranaMena().getKurzy().isEmpty()) {
            nadpisZaplatit.setText(rezim);
        } else {
            nadpisZaplatit.setText(rezim + " (1 " + PlatebniProcesor.getInstance().getVybranaMena().getNarodniSymbol()
                    + " = " + Pomocnici.formatujNaTriMista(PlatebniProcesor.getInstance().getVybranaMena().getKurzy().get(0).getNakup())
                    + " " + Pos.getInstance().getHlavniMena().getNarodniSymbol() + "):");
        }
    }

    public void setPolozky() {
        polozky.clear();
        polozky.addAll(DokladProcesor.getInstance().getDoklad().getPlatby());
        if (!polozky.isEmpty()) {
            lvPolozky.getSelectionModel().selectLast();
            lvPolozky.scrollTo(lvPolozky.getSelectionModel().getSelectedIndex());
        }
    }

    public void setVratka(boolean vratka) {
        this.vratka = vratka;
        zobrazCelkem();
    }

    protected void setDialog(Platba dialog) {
        this.dialog = dialog;
    }

    protected void zobrazTlacitkaPlatebnichProstredku() {

    }

    protected void setTlacitkaHotovosti() {
        List<Button> tlacitkaHotovosti = new ArrayList<>();
        List<ImageView> obrazkyHotovosti = new ArrayList<>();
        denominaceNaTlacitkachHotovosti.clear();
        for (int i = 1; i <= 8; i++) {
            Button tl = (Button) dialog.getScene().lookup("#tlHotovost" + i);
            ImageView obr = (ImageView) dialog.getScene().lookup("#obrHotovost" + i);
            if (tl != null) {
                tlacitkaHotovosti.add(tl);
            }
            if (obr != null) {
                obrazkyHotovosti.add(obr);
            }
        }
        tlacitkaHotovosti.forEach(tl -> tl.setVisible(false));
        obrazkyHotovosti.forEach(obr -> {
            obr.setImage(null);
            obr.setFitHeight(0);
            obr.setFitWidth(0);
        });
        if (!PlatebniProcesor.getInstance().jeVratka() && PlatebniProcesor.getInstance().getVybranyPlatebniProstredek().getTyp() == PlatebniProstredek.TypPlatebnihoProstredku.HOTOVOST) {
            int index = 0;
            for (Denominace d : PlatebniProcesor.getInstance().getVybranaMena().getDenominace()) {
                if (d.isZobrazitNaTlacitku() && index < tlacitkaHotovosti.size()) {
                    Button tl = tlacitkaHotovosti.get(index);
                    tl.setVisible(true);
                    tl.setText(d.getHodnota().compareTo(BigDecimal.ONE) < 0 ? "0," + d.getJednotka() : Integer.toString(d.getJednotka()));
                    Tooltip ttip = new Tooltip(tl.getText() + " " + PlatebniProcesor.getInstance().getVybranaMena().getNarodniSymbol());
                    tl.setTooltip(ttip);
                    tl.setOnAction(actionEvent -> tlHotovostHodnotaStisknuto(d.getHodnota()));
                    for (ObrazekNaTlacitku obr : PlatebniProcesor.getInstance().getObrazkyNaTlacitkach()) {
                        ImageView iv = obrazkyHotovosti.get(index);
                        if (obr.getCisloDenominace() == d.getCislo()) {
                            iv.setFitHeight(60);
                            iv.setImage(obr.getObrazek());
                            iv.setSmooth(true);
                            tl.setText("");
                            break;
                        }
                    }
                    denominaceNaTlacitkachHotovosti.put(index + 1, d);
                    index++;
                }
            }
        } else {
            tlacitkaHotovosti.forEach(tl -> tl.setVisible(false));
        }
    }

    private void tlHotovostHodnotaStisknuto(BigDecimal hodnota) {
        for (PlatebniProstredek pp : PlatebniProcesor.getInstance().getPlatebniProstredky()) {
            if (pp.getTyp() == PlatebniProstredek.TypPlatebnihoProstredku.HOTOVOST) {
                PlatebniProcesor.getInstance().setVybranyPlatebniProstredek(pp);
                break;
            }
        }
        if (PlatebniProcesor.getInstance().lzePlatebniProstredekZaregistrovat()) {
            PlatebniProcesor.getInstance().zaregistrujPolozkuPlatby(hodnota);
            PlatebniProcesor.getInstance().setVybranaMena(PlatebniProcesor.getInstance().getMeny().get(0));
            zobrazCelkem();
            setTlacitkaHotovosti();
        }
    }

    @FXML
    private void tlKartaStisknuto() {
        PlatebniProcesor.getInstance().zaplatKartou();
    }

    @FXML
    private void tlOdebratPolozkuStisknuto() {
        if (polozky.size() > 0) {
            PlatebniProcesor.getInstance().odeberPolozkuPlatby(lvPolozky.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void tlZavritStisknuto() {
        dialog.zavriDialog(false);
    }

    @FXML
    private void tlZmenaMenyStisknuto() {
        int poziceVybraneMeny = PlatebniProcesor.getInstance().getMeny().indexOf(PlatebniProcesor.getInstance().getVybranaMena());
        int index = poziceVybraneMeny + 1;
        if ((index) <= (PlatebniProcesor.getInstance().getMeny().size() - 1)) {
            PlatebniProcesor.getInstance().setVybranaMena(PlatebniProcesor.getInstance().getMeny().get(index));
            if (PlatebniProcesor.getInstance().getMeny().get(poziceVybraneMeny + 1).getKurzy().isEmpty()) {
                tlZmenaMenyStisknuto();
            }
        } else {
            PlatebniProcesor.getInstance().setVybranaMena(PlatebniProcesor.getInstance().getMeny().get(0));
        }
        zobrazCelkem();
        setTlacitkaHotovosti();
    }

    @FXML
    private void klavesaStisknuta(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ESCAPE) {
            dialog.zavriDialog(false);
        }
    }

    @FXML
    private void tlPolozkaNahoruStisknuto() {
        if (polozky.size() > 0) {
            if (lvPolozky.getSelectionModel().getSelectedIndex() > -1) {
                lvPolozky.getSelectionModel().selectPrevious();
            } else {
                lvPolozky.getSelectionModel().selectLast();
            }
            lvPolozky.scrollTo(lvPolozky.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void tlPolozkaDoluStisknuto() {
        if (polozky.size() > 0) {
            if (lvPolozky.getSelectionModel().getSelectedIndex() > -1) {
                lvPolozky.getSelectionModel().selectNext();
            } else {
                lvPolozky.getSelectionModel().selectFirst();
            }
            lvPolozky.scrollTo(lvPolozky.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void tlStranaNahoruStisknuto() {
        if (polozky.size() > 0) {
            if ((lvPolozky.getSelectionModel().getSelectedIndex() - 5) >= 0) {
                lvPolozky.getSelectionModel().select(lvPolozky.getSelectionModel().getSelectedIndex() - 5);
            } else {
                lvPolozky.getSelectionModel().selectFirst();
            }
            lvPolozky.scrollTo(lvPolozky.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void tlStranaDoluStisknuto() {
        if (polozky.size() > 0) {
            if ((lvPolozky.getSelectionModel().getSelectedIndex() + 4) < polozky.size()) {
                lvPolozky.getSelectionModel().select(lvPolozky.getSelectionModel().getSelectedIndex() + 4);
            } else {
                lvPolozky.getSelectionModel().selectLast();
            }
            lvPolozky.scrollTo(lvPolozky.getSelectionModel().getSelectedIndex());
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ciselnaKlavesniceController.setPrijemce(this);
        Pomocnici.setCiselneOmezeni(poleVstup, Pomocnici.REGEX_DVOUMISTNE_DESETINNE_CISLO, "Může být zadáno jen dvoumístné desetinné číslo.");
        zobrazCelkem();
        popisSleva.setText("");
        hodnotaSleva.setText("");
        poleVstup.setPromptText("Částka");
        polozky.addListener((ListChangeListener.Change<? extends PolozkaPlatby> zmena) -> zobrazCelkem());
        lvPolozky.setCellFactory(bunka -> new PolozkaPlatbyBunka());
        lvPolozky.setItems(polozky);
        if (PlatebniProcesor.getInstance().jeVratka()){
            tlZmenaMeny.setVisible(false);
            tlPoukaz.setVisible(false);
            tlOdebratSlevu.setVisible(false);
            tlPridatSlevu.setVisible(false);
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
        PlatebniProstredek prvniPP = PlatebniProcesor.getInstance().getPlatebniProstredky().get(0);
        if (!poleVstup.getText().trim().isEmpty()) {
            switch (prvniPP.getTyp()) {
                case HOTOVOST -> tlHotovostHodnotaStisknuto(new BigDecimal(poleVstup.getText().replace(',', '.')));
                case KARTA -> tlKartaStisknuto();
            }
            poleVstup.clear();
        } else {
            switch (prvniPP.getTyp()) {
                case HOTOVOST -> tlHotovostHodnotaStisknuto(PlatebniProcesor.getInstance().getKZaplaceniVeVybraneMene());
                case KARTA -> tlKartaStisknuto();
            }
        }
    }
}
