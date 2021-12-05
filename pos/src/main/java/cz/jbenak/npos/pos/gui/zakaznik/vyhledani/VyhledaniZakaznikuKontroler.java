package cz.jbenak.npos.pos.gui.zakaznik.vyhledani;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.gui.sdilene.kontrolery.KontrolerViceprvkovehoZadavacihoDialogu;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.PoleKVyplneni;
import cz.jbenak.npos.pos.objekty.partneri.KriteriaVyhledavaniZakaznika;
import cz.jbenak.npos.pos.procesory.AutorizacniManazer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-07-14
 * <p>
 * Kontroler vyhledávacího dialogu pro seznam zákazníků.
 */

public final class VyhledaniZakaznikuKontroler extends KontrolerViceprvkovehoZadavacihoDialogu<VyhledaniZakazniku> {

    public static final Logger LOGER = LogManager.getLogger(VyhledaniZakaznikuKontroler.class);
    @FXML
    private TextField poleCisloZakaznika;
    @FXML
    private TextField poleJmeno;
    @FXML
    private TextField polePrijmeni;
    @FXML
    private TextField poleNazev;
    @FXML
    private TextField poleIC;
    @FXML
    private TextField poleDIC;
    @FXML
    private TextField poleUlice;
    @FXML
    private TextField poleCp;
    @FXML
    private TextField poleCor;
    @FXML
    private TextField poleMesto;
    @FXML
    private TextField polePSC;
    @FXML
    private Button tlPridatZakaznia;

    @Override
    protected void initGUI() {
        oznacPrvni();
        Pomocnici.setOmezeniMaxDelky(poleCisloZakaznika, 45, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(poleJmeno, 100, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(polePrijmeni, 100, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(poleNazev, 254, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(poleIC, 15, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(poleDIC, 20, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(poleUlice, 100, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(poleCp, 10, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(poleCor, 10, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(polePSC, 15, Pomocnici.TypyVstupnichHodnot.TEXT);
        Pomocnici.setOmezeniMaxDelky(poleMesto, 100, Pomocnici.TypyVstupnichHodnot.TEXT);
    }

    @Override
    public void oznacPrvni() {
        super.oznacPrvni(poleCisloZakaznika);
    }

    @Override
    protected void tlOkStisknuto() {
        LOGER.debug("Tlačítko pro vyhledávání zákazníků stisknuto.");
        final KriteriaVyhledavaniZakaznika filtr = new KriteriaVyhledavaniZakaznika();
        if (!poleCisloZakaznika.getText().trim().isEmpty()) {
            filtr.setCislo(poleCisloZakaznika.getText().trim());
        }
        if (!poleJmeno.getText().trim().isEmpty()) {
            filtr.setJmeno(poleJmeno.getText().trim());
        }
        if (!polePrijmeni.getText().trim().isEmpty()) {
            filtr.setPrijmeni(polePrijmeni.getText().trim());
        }
        if (!poleNazev.getText().trim().isEmpty()) {
            filtr.setNazev(poleNazev.getText().trim());
        }
        if (!poleIC.getText().trim().isEmpty()) {
            filtr.setIc(poleIC.getText().trim());
        }
        if (!poleDIC.getText().trim().isEmpty()) {
            filtr.setDic(poleDIC.getText().trim());
        }
        if (!poleCp.getText().trim().isEmpty()) {
            filtr.setCp(poleCp.getText().trim());
        }
        if (!poleCor.getText().trim().isEmpty()) {
            filtr.setCor(poleCor.getText().trim());
        }
        if (!poleUlice.getText().trim().isEmpty()) {
            filtr.setUlice(poleUlice.getText().trim());
        }
        if (!poleMesto.getText().trim().isEmpty()) {
            filtr.setMesto(poleMesto.getText().trim());
        }
        if (!polePSC.getText().trim().isEmpty()) {
            filtr.setPsc(polePSC.getText().trim());
        }
        filtr.setBlokovany(zatrJenNeblokovane.isSelected());
        int zaznamu = vyberPocetZaznamu.getSelectionModel().getSelectedItem().compareToIgnoreCase("Vše") == 0
                ? -1 : Integer.parseInt(vyberPocetZaznamu.getSelectionModel().getSelectedItem());
        dialog.nactiSeznamZakazniku(filtr, zaznamu);
    }

    @Override
    protected void zpracujRozsireneKlavesy(KeyEvent evt) {
    }


    @FXML
    private void tlPridatZakaznikaStisknuto() {
        LOGER.info("Tlačítko pro založení nového zákazníka bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(AutorizacniManazer.Funkce.ZALOZENI_ZAKAZNIKA)) {
            //TODO
            new DialogZpravy(DialogZpravy.TypZpravy.INFO, "Založení nového zákazníka", "Tato funkce zatím není naprogramována. Založení bude možné jen v online režimu.", dialog).zobrazDialogAPockej();
        }
    }

    @Override
    protected void inicializujKontroler() {
        tlOk.setTooltip(new Tooltip("Vyhledat zákazníka"));
        tlPridatZakaznia.setTooltip(new Tooltip("Založit nového zákazníka"));
        poleCisloZakaznika.setPromptText("Číslo zákazníka");
        poleJmeno.setPromptText("Jméno");
        polePrijmeni.setPromptText("Příjmení");
        poleNazev.setPromptText("Název firmy");
        poleIC.setPromptText("IČ");
        poleDIC.setPromptText("DIČ");
        poleUlice.setPromptText("Ulice");
        poleCp.setPromptText("Č. p.");
        poleCor.setPromptText("Č. or.");
        polePSC.setPromptText("PSČ");
        poleMesto.setPromptText("Město");
        poleCisloZakaznika.requestFocus();
        policka = new Node[]{poleCisloZakaznika, poleJmeno, polePrijmeni, poleNazev, poleIC, poleDIC, poleUlice, poleCp, poleCor, polePSC, poleMesto, zatrJenNeblokovane};
    }

    @Override
    protected List<PoleKVyplneni> setVstupy() {
        List<PoleKVyplneni> vstupy = new LinkedList<>();
        vstupy.add(new PoleKVyplneni(poleCisloZakaznika, Pomocnici.TypyVstupnichHodnot.TEXT, 45));
        vstupy.add(new PoleKVyplneni(poleJmeno, Pomocnici.TypyVstupnichHodnot.TEXT, 100));
        vstupy.add(new PoleKVyplneni(polePrijmeni, Pomocnici.TypyVstupnichHodnot.TEXT, 100));
        vstupy.add(new PoleKVyplneni(poleNazev, Pomocnici.TypyVstupnichHodnot.TEXT, 254));
        vstupy.add(new PoleKVyplneni(poleIC, Pomocnici.TypyVstupnichHodnot.TEXT, 15));
        vstupy.add(new PoleKVyplneni(poleDIC, Pomocnici.TypyVstupnichHodnot.TEXT, 20));
        vstupy.add(new PoleKVyplneni(poleUlice, Pomocnici.TypyVstupnichHodnot.TEXT, 100));
        vstupy.add(new PoleKVyplneni(poleCp, Pomocnici.TypyVstupnichHodnot.TEXT, 10));
        vstupy.add(new PoleKVyplneni(poleCor, Pomocnici.TypyVstupnichHodnot.TEXT, 10));
        vstupy.add(new PoleKVyplneni(polePSC, Pomocnici.TypyVstupnichHodnot.TEXT, 15));
        vstupy.add(new PoleKVyplneni(poleMesto, Pomocnici.TypyVstupnichHodnot.TEXT, 100));
        return vstupy;
    }

}
