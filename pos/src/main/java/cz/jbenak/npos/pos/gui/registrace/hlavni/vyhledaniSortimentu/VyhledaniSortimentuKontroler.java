/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni.vyhledaniSortimentu;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.registrace.hlavni.zobrazeniSkupinSortimentu.ZobrazeniSkupinSortimentu;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.progres.ProgresDialog;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.gui.sdilene.kontrolery.KontrolerViceprvkovehoZadavacihoDialogu;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.PoleKVyplneni;
import cz.jbenak.npos.pos.objekty.sortiment.SkupinaSortimentu;
import cz.jbenak.npos.pos.procesy.polozka.vyhledavani.NacteniSeznamuSkupinSortimentu;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-03-04
 */
public class VyhledaniSortimentuKontroler extends KontrolerViceprvkovehoZadavacihoDialogu<VyhledaniSortimentu> {

    private static final Logger LOGER = LogManager.getLogger(VyhledaniSortimentuKontroler.class);
    @FXML
    private TextField poleRegistr;
    @FXML
    private TextField poleSkupinaID;
    @FXML
    private TextField poleSkupinaNazev;
    @FXML
    private TextField poleCenaOd;
    @FXML
    private TextField poleNazev;
    @FXML
    private TextField poleCenaDo;
    @FXML
    private TextField poleCarovyKod;
    @FXML
    private Button tlHledatSkupinu;
    @FXML
    private Label nadpis;
    @FXML
    private ImageView ikona;

    @Override
    protected void initGUI() {
        oznacPrvni();
        Pomocnici.setOmezeniMaxDelky(poleRegistr, 45, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(poleSkupinaID, 45, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(poleSkupinaNazev, 80, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(poleNazev, 255, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(poleCenaOd, 15, Pomocnici.TypyVstupnichHodnot.DECIMAL);
        Pomocnici.setOmezeniMaxDelky(poleCenaDo, 15, Pomocnici.TypyVstupnichHodnot.DECIMAL);
        Pomocnici.setOmezeniMaxDelky(poleCarovyKod, 255, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setCiselneOmezeni(poleCenaOd, Pomocnici.REGEX_DVOUMISTNE_DESETINNE_CISLO,
                "Do toho pole lze zadat pouze desetinné číslo s max. dvěma místy");
        Pomocnici.setCiselneOmezeni(poleCenaDo, Pomocnici.REGEX_DVOUMISTNE_DESETINNE_CISLO,
                "Do toho pole lze zadat pouze desetinné číslo s max. dvěma místy");
    }

    void setInfo(boolean info) {
        if (info) {
            nadpis.setText("INFORMACE O SORTIMENTU");
            String ikonaInfo = "../menu/registracePripravena/img/zbozi_info.png";
            ikona.setImage(new Image(getClass().getResourceAsStream(ikonaInfo)));
        } else {
            nadpis.setText("VYHLEDAT SORTIMENT");
            String ikonaHledat = "../menu/registracePripravena/img/zbozi_hledat.png";
            ikona.setImage(new Image(getClass().getResourceAsStream(ikonaHledat)));
        }
    }

    @Override
    public void oznacPrvni() {
        super.oznacPrvni(poleRegistr);
    }

    @Override
    protected void tlSmazatPoleStisknuto() {
        if (fokusovany instanceof TextField) {
            if (fokusovany == poleSkupinaNazev || fokusovany == poleSkupinaID) {
                poleSkupinaNazev.clear();
                poleSkupinaID.clear();
                poleSkupinaNazev.setDisable(false);
                poleSkupinaID.setDisable(false);
                dialog.getKriteria().setSkupina(null);
            } else {
                ((TextField) fokusovany).clear();
            }
        }
    }

    @FXML
    private void tlHledatSkupinuStisknuto() {
        LOGER.debug("Tlačítko pro vyhledání skupiny sortimentu bylo stisknuto.");
        if (dialog.getKriteria().getSkupina() != null) {
            dialog.getKriteria().setSkupina(null);
            poleSkupinaID.clear();
            poleSkupinaID.setDisable(false);
            poleSkupinaNazev.clear();
            poleSkupinaNazev.setDisable(false);
        }
        ProgresDialog progres = new ProgresDialog(dialog);
        progres.zobrazProgres("Vyhledávám skupiny sortimentu...");
        NacteniSeznamuSkupinSortimentu nacteni = new NacteniSeznamuSkupinSortimentu();
        nacteni.setId(poleSkupinaID.getText().isEmpty() ? null : poleSkupinaID.getText().trim());
        nacteni.setNazev(poleSkupinaNazev.getText().isEmpty() ? null : poleSkupinaNazev.getText().trim());
        nacteni.run();
        try {
            List<SkupinaSortimentu> skupiny = nacteni.get();
            progres.zavriDialog();
            if (skupiny.isEmpty()) {
                LOGER.info("Nebyly nalezeny žádné skupiny sortimentu.");
                new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nenašel jsem ani jednu skupinu",
                        "Bohužel jsem na základě zadaných dat nenašel žádné skupiny sortimentu." +
                                "\n\nZkuste změnit vyhledávací kritéria a pak to zkuste znovu.", dialog).zobrazDialogAPockej();
            } else if (skupiny.size() == 1) {
                SkupinaSortimentu skupina = skupiny.get(0);
                poleSkupinaID.setText(skupina.getId());
                poleSkupinaID.setDisable(true);
                poleSkupinaNazev.setText(skupina.getNazev());
                poleSkupinaNazev.setDisable(true);
                dialog.getKriteria().setSkupina(skupina);
            } else {
                ZobrazeniSkupinSortimentu vyber = new ZobrazeniSkupinSortimentu(skupiny, dialog);
                vyber.zobrazDialog();
                SkupinaSortimentu skupina = vyber.getVybranaSkupina();
                if (skupina != null) {
                    poleSkupinaID.setText(skupina.getId());
                    poleSkupinaID.setDisable(true);
                    poleSkupinaNazev.setText(skupina.getNazev());
                    poleSkupinaNazev.setDisable(true);
                    dialog.getKriteria().setSkupina(skupina);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGER.error("Nastala chyba při načítání seznamu skupin sortimentu. ", e);
            if (progres.isShowing()) {
                progres.zavriDialog();
            }
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému",
                    "Nastala chyba při načítání seznamu skupin sortimentu\n\n" + e.getLocalizedMessage(), dialog).zobrazDialogAPockej();
        }
    }

    @Override
    protected void tlOkStisknuto() {
        LOGER.debug("Tlačítko pro vyhledávání sortimentu bylo stisknuto.");
        if (!(poleCenaOd.getText().isEmpty() || poleCenaDo.getText().isEmpty())
                && ((new BigDecimal(poleCenaOd.getText().trim().replace(",", ".")))
                .compareTo(new BigDecimal(poleCenaDo.getText().trim().replace(",", "."))) > 0)) {
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Tohle nemůžu", "'Cena od' je větší než 'Cena do', což logicky nejde" +
                    " a nedovolí mě získat smysluplný výsledek.\n\nProsím opravte.", dialog).zobrazDialogAPockej();
            poleCenaOd.requestFocus();
            poleCenaOd.selectAll();
        } else if (!(poleSkupinaNazev.getText().isEmpty() && poleSkupinaID.getText().isEmpty()) && dialog.getKriteria().getSkupina() == null) {
            tlHledatSkupinuStisknuto();
        } else {
            if (!poleRegistr.getText().isEmpty()) {
                dialog.getKriteria().setRegistr(poleRegistr.getText().trim());
            }
            if (!poleNazev.getText().isEmpty()) {
                dialog.getKriteria().setNazev(poleRegistr.getText().trim());
            }
            if (!poleCarovyKod.getText().isEmpty()) {
                dialog.getKriteria().setCarovyKod(poleCarovyKod.getText().trim());
            }
            if (!poleCenaOd.getText().isEmpty()) {
                dialog.getKriteria().setCenaOd(new BigDecimal(poleCenaOd.getText().trim().replace(",", ".")));
            }
            if (!poleCenaDo.getText().isEmpty()) {
                dialog.getKriteria().setCenaDo(new BigDecimal(poleCenaDo.getText().trim().replace(",", ".")));
            }
            dialog.getKriteria().setJenNeblokovane(zatrJenNeblokovane.isSelected());
            int zaznamu = vyberPocetZaznamu.getSelectionModel().getSelectedItem().compareToIgnoreCase("Vše") == 0
                    ? -1 : Integer.parseInt(vyberPocetZaznamu.getSelectionModel().getSelectedItem());
            dialog.getKriteria().setMaxZazanmu(zaznamu);
            dialog.provedVyhledavani();
        }
    }

    @Override
    protected void tlResetFormulareStisknuto() {
        LOGER.debug("Všechna  pole se smažou a nastaví na výchozí hodnoty");
        Arrays.stream(policka).forEach(pole -> {
            if (pole instanceof TextField) {
                ((TextField) pole).clear();
            }
        });
        zatrJenNeblokovane.setSelected(false);
        vyberPocetZaznamu.getSelectionModel().selectFirst();
        dialog.getKriteria().setSkupina(null);
        dialog.getKriteria().setCarovyKod(null);
        dialog.getKriteria().setCenaDo(null);
        dialog.getKriteria().setCenaOd(null);
        dialog.getKriteria().setNazev(null);
        dialog.getKriteria().setRegistr(null);
        poleSkupinaNazev.setDisable(false);
        poleSkupinaID.setDisable(false);
        oznacPrvni();
    }

    @Override
    protected void zpracujRozsireneKlavesy(KeyEvent evt) {

    }

    @Override
    protected List<PoleKVyplneni> setVstupy() {
        List<PoleKVyplneni> vstupy = new LinkedList<>();
        vstupy.add(new PoleKVyplneni(poleRegistr, Pomocnici.TypyVstupnichHodnot.TEXT, 45));
        vstupy.add(new PoleKVyplneni(poleNazev, Pomocnici.TypyVstupnichHodnot.TEXT, 255));
        vstupy.add(new PoleKVyplneni(poleCarovyKod, Pomocnici.TypyVstupnichHodnot.TEXT, 255));
        vstupy.add(new PoleKVyplneni(poleSkupinaID, Pomocnici.TypyVstupnichHodnot.TEXT, 45));
        vstupy.add(new PoleKVyplneni(poleSkupinaNazev, Pomocnici.TypyVstupnichHodnot.TEXT, 80));
        vstupy.add(new PoleKVyplneni(poleCenaOd, Pomocnici.TypyVstupnichHodnot.DECIMAL, 15));
        vstupy.add(new PoleKVyplneni(poleCenaDo, Pomocnici.TypyVstupnichHodnot.DECIMAL, 15));
        return vstupy;
    }

    @Override
    protected void inicializujKontroler() {
        tlHledatSkupinu.setTooltip(new Tooltip("Vyhledat skupinu sortimentu"));
        tlOk.setTooltip(new Tooltip("Vyhledat sortiment"));
        poleRegistr.setPromptText("Registr");
        poleSkupinaID.setPromptText("Číslo skupiny");
        poleSkupinaNazev.setPromptText("Název skupiny");
        poleCenaOd.setPromptText("Cena od");
        poleNazev.setPromptText("Název sortimentu");
        poleCenaDo.setPromptText("Cena do");
        poleCarovyKod.setPromptText("Čárový kód");
        poleRegistr.requestFocus();
        policka = new Node[]{poleRegistr, poleNazev, poleCarovyKod, poleSkupinaID, poleSkupinaNazev, poleCenaOd, poleCenaDo, zatrJenNeblokovane};
    }

}
