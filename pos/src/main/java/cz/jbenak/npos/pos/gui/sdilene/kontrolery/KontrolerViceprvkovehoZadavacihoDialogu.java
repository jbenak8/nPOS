package cz.jbenak.npos.pos.gui.sdilene.kontrolery;

import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.ITextovaKlavesnice;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.PoleKVyplneni;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.TextovaKlavesnice;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Třída pro vytvoření základního kontroléru pro dialogy vyžadující zadávání, např vyhledávání sortimentu, nebo zákazníka
 *
 * @param <T> Hlavní třída, která se stará o zobrazení dialogu.
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-06-18
 */
public abstract class KontrolerViceprvkovehoZadavacihoDialogu<T extends Stage> implements Initializable, ITextovaKlavesnice {

    protected final static Logger LOGER = LogManager.getLogger(KontrolerViceprvkovehoZadavacihoDialogu.class);
    protected T dialog;
    protected Node[] policka = null;
    protected Node fokusovany = null;
    @FXML
    protected Button tlOk;
    @FXML
    protected Button tlZobrazitKlavesnici;
    @FXML
    protected Button tlVymazatPole;
    @FXML
    protected Button tlResetFormulare;
    @FXML
    protected ComboBox<String> vyberPocetZaznamu;
    @FXML
    protected CheckBox zatrJenNeblokovane;

    /**
     * Nastaví okno dialogu
     *
     * @param dialog třída okna dialogu obsluhující funkce.
     */
    public final void setDialog(T dialog) {
        this.dialog = dialog;
    }

    /**
     * Inicializace GUI po konstrukci dialogu (např. zobrazení určitých dat v již vytvořené komponentě)
     */
    protected abstract void initGUI();

    /**
     * Označení (fokus) první komponenty.
     *
     * @param prvni první komponenta, co se má označit.
     */
    protected void oznacPrvni(TextField prvni) {
        prvni.requestFocus();
        fokusovany = prvni;
        if (!prvni.getText().isEmpty()) {
            prvni.selectAll();
        }
    }

    /**
     * Posun fokusu na další fokusovatelnou komponentu (co je fokusovatelné se definuje v implementaci inicializujKontroler();
     */
    private void posunDolu() {
        uzavriTooltip();
        for (int i = 0; i < policka.length; i++) {
            if (policka[i].isFocused()) {
                if (i < policka.length - 1) {
                    policka[i + 1].requestFocus();
                    fokusovany = policka[i + 1];
                } else {
                    tlOk.requestFocus();
                    //tlHledatStisknuto(); - na vyžádání
                }
                break;
            }
        }
    }

    /**
     * Uzavře právě zobrazovaný tooltip komponenty.
     */
    private void uzavriTooltip() {
        if (fokusovany instanceof TextField) {
            TextField field = (TextField) fokusovany;
            if (field.getTooltip() != null && field.getTooltip().isShowing()) {
                field.getTooltip().hide();
            }
        }
    }

    /**
     * Akce hlavního potvrzovacího tlačítka
     */
    @FXML
    protected abstract void tlOkStisknuto();

    /**
     * Resetuje všechny zadávací komponenty do výchozího stavu.
     */
    @FXML
    protected void tlResetFormulareStisknuto() {
        LOGER.debug("Všechna  pole se smažou a nastaví na výchozí hodnoty");
        Arrays.stream(policka).forEach(pole -> {
            if (pole instanceof TextField) {
                ((TextField) pole).clear();
            }
        });
        zatrJenNeblokovane.setSelected(false);
        vyberPocetZaznamu.getSelectionModel().selectFirst();
        oznacPrvni();
    }

    /**
     * Uzavře dialog.
     */
    @FXML
    protected void tlZavritStisknuto() {
        LOGER.info("Zadávací/vyhledávací okno bylo uzavřeno.");
        dialog.close();
    }

    /**
     * Smaže obsah právě označeného pole.
     */
    @FXML
    protected void tlSmazatPoleStisknuto() {
        if (fokusovany instanceof TextField) {
            ((TextField) fokusovany).clear();
        }
    }

    /**
     * Zpracuje kliknutí do pole či jiného zadávacího komponentu. Zde se uzavírá tooltip.
     *
     * @param evt Událost kliknutí.
     */
    @FXML
    protected void poleKliknuto(MouseEvent evt) {
        fokusovany = (Node) evt.getSource();
        uzavriTooltip();
    }

    /**
     * Zpracuje stisk klávesy. Zde se nabízí posuv po komponentech (Tab, Enter, šipky), potvrzení (Ctrl+Enter), uzavření (Esc), zobrazení textové klávesnice (F2), smazání aktuálního pole (F8) a reset formuláře (Ctrl+F8).
     * Lze rozšířit implementací metody zpracujRozsireneKlavesy.
     *
     * @param evt Událost stisknutí klávesy.
     */
    @FXML
    protected void klavesaStisknuta(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ENTER) {
            if (evt.getSource() instanceof TextField || evt.getSource() instanceof ComboBox) {
                posunDolu();
            }
        }
        if (evt.getCode() == KeyCode.TAB && evt.getSource().getClass() == TextField.class) {
            for (int i = 0; i < policka.length; i++) {
                if (policka[i] == evt.getSource()) {
                    if (evt.isShiftDown()) {
                        fokusovany = (i == 0) ? policka[0] : policka[i - 1];
                    } else {
                        fokusovany = policka[i + 1];
                    }
                }
            }
        }
        if (evt.getCode() == KeyCode.UP) {
            for (int i = 0; i < policka.length; i++) {
                if (policka[i].isFocused()) {
                    if (i > 0) {
                        policka[i - 1].requestFocus();
                        fokusovany = policka[i - 1];
                    }
                    break;
                }
            }
        }
        if (evt.getCode() == KeyCode.DOWN) {
            posunDolu();
        }
        if (evt.isControlDown() && evt.getCode() == KeyCode.ENTER) {
            tlOkStisknuto();
        }
        if (evt.getCode() == KeyCode.ESCAPE) {
            tlZavritStisknuto();
        }
        if (evt.getCode() == KeyCode.F2) {
            tlZobrazitKlavesniciStisknuto();
        }
        if (evt.getCode() == KeyCode.F8) {
            tlSmazatPoleStisknuto();
        }
        if (evt.isControlDown() && evt.getCode() == KeyCode.F8) {
            tlResetFormulareStisknuto();
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
     * Vytvoří seznam vstupních polí pro textovou klávesnici.
     *
     * @return seznam vstupních polí, které se mají vyplnit textovou klávesnicí.
     */
    protected abstract List<PoleKVyplneni> setVstupy();

    /**
     * Implementace zobrazení textové klávesnice.
     */
    @Override
    public void tlZobrazitKlavesniciStisknuto() {
        List<PoleKVyplneni> vstupy = setVstupy();
        TextovaKlavesnice klavesnice;
        if (fokusovany != null && fokusovany instanceof TextField) {
            int index = 0;
            for (PoleKVyplneni vstup : vstupy) {
                if (vstup.getPole() == fokusovany) {
                    break;
                }
                index++;
            }
            klavesnice = new TextovaKlavesnice(this, dialog, vstupy, index);
        } else {
            klavesnice = new TextovaKlavesnice(this, dialog, vstupy);
        }
        klavesnice.zobrazKlavesnici();
    }

    /**
     * Implementace události uzavření textové klávesnice.
     */
    @Override
    public void editaceUkoncena() {
    }

    /**
     * Implementace inicializace FX kontroléru.
     *
     * @param location  url
     * @param resources zdroje
     * @see Initializable
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vyberPocetZaznamu.getItems().add("100");
        vyberPocetZaznamu.getItems().add("200");
        vyberPocetZaznamu.getItems().add("500");
        vyberPocetZaznamu.getItems().add("Vše");
        vyberPocetZaznamu.getSelectionModel().selectFirst();
        inicializujKontroler();
        tlZobrazitKlavesnici.setTooltip(new Tooltip("Zobrazit textovou klávesnici (F2)"));
        tlVymazatPole.setTooltip(new Tooltip("Smazat vybrané pole (F8)"));
        tlResetFormulare.setTooltip(new Tooltip("Resetovat formulář (Ctrl+F8)"));
    }

    /**
     * Rozšíření výchozí inicializace pomocí initialize.
     */
    protected abstract void inicializujKontroler();

    /**
     * Označí první zadávácí komponentu.
     */
    public abstract void oznacPrvni();
}
