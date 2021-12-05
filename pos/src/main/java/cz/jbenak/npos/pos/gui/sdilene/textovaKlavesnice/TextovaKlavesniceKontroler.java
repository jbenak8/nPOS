/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.rozvrzeni.CS_CZ;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.rozvrzeni.DE_DE;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.rozvrzeni.EN_US;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.rozvrzeni.Rozvrzeni;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.List;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 */
public class TextovaKlavesniceKontroler implements Initializable {

    private final ArrayList<Rozvrzeni> SADY = new ArrayList<>();
    private final HashMap<Integer, Button> MAPY_TLACITEK = new HashMap<>();
    private final EnterListener enterListener = new EnterListener();
    private final List<OmezeniDelkyListener> delkoveListenery = new LinkedList<>();
    private final List<OmezeniHodnotyListener> hodnotoveListenery = new LinkedList<>();
    private final Tooltip ttip = new Tooltip();
    private Rozvrzeni vybraneRozvrzeni;
    private ITextovaKlavesnice prijemce;
    private TextovaKlavesnice dialog;
    private Set<Integer> klice;
    private List<PoleKVyplneni> poleKVyplneni;
    private int indexPoleKVyplneni;
    @FXML
    private TextArea editacniPole;
    @FXML
    private Label titulekPole;
    @FXML
    private Button tl_lang;
    @FXML
    private Button tl_strednik;
    @FXML
    private Button tl_1;
    @FXML
    private Button tl_2;
    @FXML
    private Button tl_3;
    @FXML
    private Button tl_4;
    @FXML
    private Button tl_5;
    @FXML
    private Button tl_6;
    @FXML
    private Button tl_7;
    @FXML
    private Button tl_8;
    @FXML
    private Button tl_9;
    @FXML
    private Button tl_0;
    @FXML
    private Button tl_rovno;
    @FXML
    private Button tl_akcent;
    @FXML
    private Button tl_backspace;
    @FXML
    private Button tl_tab;
    @FXML
    private ToggleButton tl_caps;
    @FXML
    private ToggleButton tl_lShift;
    @FXML
    private ToggleButton tl_lCtrl;
    @FXML
    private Button tl_zavorka;
    @FXML
    private Button tl_dlU;
    @FXML
    private Button tl_p;
    @FXML
    private Button tl_o;
    @FXML
    private Button tl_i;
    @FXML
    private Button tl_u;
    @FXML
    private Button tl_z;
    @FXML
    private Button tl_t;
    @FXML
    private Button tl_r;
    @FXML
    private Button tl_e;
    @FXML
    private Button tl_w;
    @FXML
    private Button tl_q;
    @FXML
    private Button tl_umlaut;
    @FXML
    private Button tl_a;
    @FXML
    private Button tl_s;
    @FXML
    private Button tl_d;
    @FXML
    private Button tl_f;
    @FXML
    private Button tl_g;
    @FXML
    private Button tl_h;
    @FXML
    private Button tl_j;
    @FXML
    private Button tl_k;
    @FXML
    private Button tl_l;
    @FXML
    private Button tl_uKrouzek;
    @FXML
    private Button tl_paragraf;
    @FXML
    private Button tl_enter;
    @FXML
    private Button tl_y;
    @FXML
    private Button tl_x;
    @FXML
    private Button tl_v;
    @FXML
    private Button tl_c;
    @FXML
    private Button tl_b;
    @FXML
    private Button tl_n;
    @FXML
    private Button tl_m;
    @FXML
    private Button tl_carka;
    @FXML
    private Button tl_tecka;
    @FXML
    private Button tl_minus;
    @FXML
    private ToggleButton tl_pShift;
    @FXML
    private ToggleButton tl_alt;
    @FXML
    private Button tl_del;
    @FXML
    private Button tl_end;
    @FXML
    private Button tl_home;
    @FXML
    private ToggleButton tl_pCtrl;
    @FXML
    private ToggleButton tl_altGr;
    @FXML
    private Button tl_mezernik;
    @FXML
    private Button tl_predchozi;
    @FXML
    private Button tl_dalsi;
    @FXML
    private Button tl_sipkaNahoru;
    @FXML
    private Button tl_sipkaDolu;
    @FXML
    private Button tl_sipkaDoleva;
    @FXML
    private Button tl_sipkaDoprava;

    void setPrijemce(ITextovaKlavesnice prijemce) {
        this.prijemce = prijemce;
    }

    void setDialog(TextovaKlavesnice dialog) {
        this.dialog = dialog;
    }

    void initGUI() {
        if (this.poleKVyplneni.size() == 1) {
            tl_dalsi.setVisible(false);
            tl_predchozi.setVisible(false);
        }
        setVlastnostiEditace();
    }

    void setPoleKVyplneni(List<PoleKVyplneni> poleKVyplneni) {
        this.poleKVyplneni = poleKVyplneni;
        poleKVyplneni.forEach(pol -> {
            delkoveListenery.add(pol.getMaxDelka() <= 0 ? null : new OmezeniDelkyListener(pol.getTyp(), pol.getMaxDelka()));
            hodnotoveListenery.add(pol.getRegexOmezeni() == null ? null : new OmezeniHodnotyListener(pol.getRegexOmezeni(), pol.getPopisOmezeni()));
        });
    }

    void setIndexPoleKVyplneni(int indexPoleKVyplneni) {
        this.indexPoleKVyplneni = indexPoleKVyplneni;
    }

    private void setVlastnostiEditace() {
        if (ttip.isShowing()) {
            ttip.hide();
        }
        if (this.indexPoleKVyplneni == 0) {
            this.tl_predchozi.setVisible(false);
        } else {
            this.tl_predchozi.setVisible(true);
        }
        if (this.indexPoleKVyplneni == this.poleKVyplneni.size() - 1) {
            this.tl_dalsi.setVisible(false);
        } else {
            this.tl_dalsi.setVisible(true);
        }
        TextInputControl aktualniPole = this.poleKVyplneni.get(this.indexPoleKVyplneni).getPole();
        this.titulekPole.setText(aktualniPole.getPromptText());
        editacniPole.setText(aktualniPole.getText());
        if (delkoveListenery.get(indexPoleKVyplneni) != null) {
            editacniPole.textProperty().addListener(delkoveListenery.get(indexPoleKVyplneni));
        }
        if (hodnotoveListenery.get(indexPoleKVyplneni) != null) {
            editacniPole.textProperty().addListener(hodnotoveListenery.get(indexPoleKVyplneni));
        }
        if (aktualniPole instanceof TextField) {
            editacniPole.textProperty().addListener(enterListener);
            tl_sipkaNahoru.setVisible(false);
            tl_sipkaDolu.setVisible(false);
        } else {
            editacniPole.textProperty().removeListener(enterListener);
            tl_sipkaNahoru.setVisible(true);
            tl_sipkaDolu.setVisible(true);
        }
        this.editacniPole.requestFocus();
        this.editacniPole.end();
    }

    @FXML
    private void tl_escapeStisknuto() {
        this.poleKVyplneni.get(this.indexPoleKVyplneni).getPole().setText(this.editacniPole.getText());
        prijemce.editaceUkoncena();
        this.dialog.zavriDialog();
    }

    private void initPismenoveKlavesy() {
        this.MAPY_TLACITEK.put(Rozvrzeni.E_CARKA, this.tl_0);
        this.MAPY_TLACITEK.put(Rozvrzeni.PLUS, this.tl_1);
        this.MAPY_TLACITEK.put(Rozvrzeni.E_HACEK, this.tl_2);
        this.MAPY_TLACITEK.put(Rozvrzeni.S_HACEK, this.tl_3);
        this.MAPY_TLACITEK.put(Rozvrzeni.C_HACEK, this.tl_4);
        this.MAPY_TLACITEK.put(Rozvrzeni.R_HACEK, this.tl_5);
        this.MAPY_TLACITEK.put(Rozvrzeni.Z_HACEK, this.tl_6);
        this.MAPY_TLACITEK.put(Rozvrzeni.Y_CARKA, this.tl_7);
        this.MAPY_TLACITEK.put(Rozvrzeni.A_CARKA, this.tl_8);
        this.MAPY_TLACITEK.put(Rozvrzeni.I_CARKA, this.tl_9);
        this.MAPY_TLACITEK.put(Rozvrzeni.STREDNIK, this.tl_strednik);
        this.MAPY_TLACITEK.put(Rozvrzeni.ROVNA_SE, this.tl_rovno);
        this.MAPY_TLACITEK.put(Rozvrzeni.CARKA_NAD_PISMENY, this.tl_akcent);
        this.MAPY_TLACITEK.put(Rozvrzeni.Q, this.tl_q);
        this.MAPY_TLACITEK.put(Rozvrzeni.W, this.tl_w);
        this.MAPY_TLACITEK.put(Rozvrzeni.E, this.tl_e);
        this.MAPY_TLACITEK.put(Rozvrzeni.R, this.tl_r);
        this.MAPY_TLACITEK.put(Rozvrzeni.T, this.tl_t);
        this.MAPY_TLACITEK.put(Rozvrzeni.Z, this.tl_z);
        this.MAPY_TLACITEK.put(Rozvrzeni.U, this.tl_u);
        this.MAPY_TLACITEK.put(Rozvrzeni.I, this.tl_i);
        this.MAPY_TLACITEK.put(Rozvrzeni.O, this.tl_o);
        this.MAPY_TLACITEK.put(Rozvrzeni.P, this.tl_p);
        this.MAPY_TLACITEK.put(Rozvrzeni.A, this.tl_a);
        this.MAPY_TLACITEK.put(Rozvrzeni.S, this.tl_s);
        this.MAPY_TLACITEK.put(Rozvrzeni.D, this.tl_d);
        this.MAPY_TLACITEK.put(Rozvrzeni.F, this.tl_f);
        this.MAPY_TLACITEK.put(Rozvrzeni.G, this.tl_g);
        this.MAPY_TLACITEK.put(Rozvrzeni.H, this.tl_h);
        this.MAPY_TLACITEK.put(Rozvrzeni.J, this.tl_j);
        this.MAPY_TLACITEK.put(Rozvrzeni.K, this.tl_k);
        this.MAPY_TLACITEK.put(Rozvrzeni.L, this.tl_l);
        this.MAPY_TLACITEK.put(Rozvrzeni.Y, this.tl_y);
        this.MAPY_TLACITEK.put(Rozvrzeni.X, this.tl_x);
        this.MAPY_TLACITEK.put(Rozvrzeni.C, this.tl_c);
        this.MAPY_TLACITEK.put(Rozvrzeni.V, this.tl_v);
        this.MAPY_TLACITEK.put(Rozvrzeni.B, this.tl_b);
        this.MAPY_TLACITEK.put(Rozvrzeni.N, this.tl_n);
        this.MAPY_TLACITEK.put(Rozvrzeni.M, this.tl_m);
        this.MAPY_TLACITEK.put(Rozvrzeni.CARKA, this.tl_carka);
        this.MAPY_TLACITEK.put(Rozvrzeni.TECKA, this.tl_tecka);
        this.MAPY_TLACITEK.put(Rozvrzeni.POMLCKA, this.tl_minus);
        this.MAPY_TLACITEK.put(Rozvrzeni.U_S_KROUZKEM, this.tl_uKrouzek);
        this.MAPY_TLACITEK.put(Rozvrzeni.PARAGRAF, this.tl_paragraf);
        this.MAPY_TLACITEK.put(Rozvrzeni.U_DLOUHE, this.tl_dlU);
        this.MAPY_TLACITEK.put(Rozvrzeni.ZAVORKY, this.tl_zavorka);
        this.MAPY_TLACITEK.put(Rozvrzeni.UMLAUT, this.tl_umlaut);
    }

    private void initStdKlavesy() {
        this.klice = this.MAPY_TLACITEK.keySet();
        klice.forEach((klic) -> this.MAPY_TLACITEK.get(klic).setText(this.vybraneRozvrzeni.getStandardniSadu().getOrDefault(klic, ' ').toString()));
        this.tl_lang.setText(this.vybraneRozvrzeni.getZkraktuNaKlavesu());
    }

    private void zjistiAPredejPismenko(Integer klic) {
        char pismenko;
        if (this.tl_pShift.isSelected() || this.tl_lShift.isSelected()) {
            pismenko = this.vybraneRozvrzeni.getShiftSadu().get(klic);
            odznacShift();
            klice.forEach((tl) -> this.MAPY_TLACITEK.get(tl).setText(this.vybraneRozvrzeni.getStandardniSadu().getOrDefault(tl, ' ').toString()));
        } else if (this.tl_caps.isSelected()) {
            pismenko = this.vybraneRozvrzeni.getCapsSadu().get(klic);
        } else if (this.tl_altGr.isSelected()) {
            pismenko = this.vybraneRozvrzeni.getAltGrSadu().get(klic);
        } else {
            pismenko = this.vybraneRozvrzeni.getStandardniSadu().get(klic);
        }
        zapisPismenkoDoPole(pismenko);
        this.editacniPole.requestFocus();
    }

    //TODO
    private void zapisPismenkoDoPole(char znak) {
        if (!this.editacniPole.getSelectedText().isEmpty()) {
            String a = this.editacniPole.getText(0, this.editacniPole.getSelection().getStart());
            String c = this.editacniPole.getText(this.editacniPole.getSelection().getEnd(), this.editacniPole.getText().length() - 1);
            this.editacniPole.setText(a + znak + c);
            this.editacniPole.positionCaret(this.editacniPole.getSelection().getStart() + 1);
        } else if (this.editacniPole.getCaretPosition() == 0) {
            this.editacniPole.setText(znak + this.editacniPole.getText());
            this.editacniPole.end();
        } else if (this.editacniPole.getCaretPosition() == this.editacniPole.getText().length()) {
            this.editacniPole.appendText(Character.toString(znak));
            this.editacniPole.end();
        } else {
            String prviPulka = this.editacniPole.getText(0, this.editacniPole.getCaretPosition());
            String druhaPulka = this.editacniPole.getText(this.editacniPole.getCaretPosition(), this.editacniPole.getText().length() - 1);
            this.editacniPole.setText(prviPulka + znak + druhaPulka);
            this.editacniPole.positionCaret(this.editacniPole.getSelection().getStart() + 1);
        }
    }

    @FXML
    private void tl_smazVseStisknuto() {
        this.editacniPole.clear();
        this.editacniPole.requestFocus();
    }

    private void odznacShift() {
        this.tl_pShift.setSelected(false);
        this.tl_lShift.setSelected(false);
    }

    @FXML
    private void tl_capsLockStisknuto() {
        if (this.tl_altGr.isSelected()) {
            this.tl_altGr.setSelected(false);
        }
        if (this.tl_caps.isSelected()) {
            klice.forEach((klic) -> this.MAPY_TLACITEK.get(klic).setText(this.vybraneRozvrzeni.getCapsSadu().getOrDefault(klic, ' ').toString()));
            if (this.tl_lShift.isSelected() || this.tl_pShift.isSelected()) {
                klice.forEach((klic) -> this.MAPY_TLACITEK.get(klic).setText(this.vybraneRozvrzeni.getStandardniSadu().getOrDefault(klic, ' ').toString()));
            }
        } else {
            klice.forEach((klic) -> this.MAPY_TLACITEK.get(klic).setText(this.vybraneRozvrzeni.getStandardniSadu().getOrDefault(klic, ' ').toString()));
            if (this.tl_lShift.isSelected() || this.tl_pShift.isSelected()) {
                odznacShift();
            }
        }
    }

    @FXML
    private void tl_LevyShiftStisknuto() {
        if (this.tl_lShift.isSelected()) {
            this.tl_pShift.setSelected(true);
            klice.forEach((klic) -> this.MAPY_TLACITEK.get(klic).setText(this.vybraneRozvrzeni.getShiftSadu().getOrDefault(klic, ' ').toString()));
        } else {
            this.tl_pShift.setSelected(false);
            if (this.tl_caps.isSelected()) {
                klice.forEach((klic) -> this.MAPY_TLACITEK.get(klic).setText(this.vybraneRozvrzeni.getCapsSadu().getOrDefault(klic, ' ').toString()));
            } else {
                klice.forEach((klic) -> this.MAPY_TLACITEK.get(klic).setText(this.vybraneRozvrzeni.getStandardniSadu().getOrDefault(klic, ' ').toString()));
            }
        }
    }

    @FXML
    private void tl_PravyShiftStisknuto() {
        if (this.tl_altGr.isSelected()) {
            this.tl_altGr.setSelected(false);
        }
        if (this.tl_pShift.isSelected()) {
            this.tl_lShift.setSelected(true);
            klice.forEach((klic) -> this.MAPY_TLACITEK.get(klic).setText(this.vybraneRozvrzeni.getShiftSadu().getOrDefault(klic, ' ').toString()));
        } else {
            this.tl_lShift.setSelected(false);
            klice.forEach((klic) -> this.MAPY_TLACITEK.get(klic).setText(this.vybraneRozvrzeni.getStandardniSadu().getOrDefault(klic, ' ').toString()));
        }
    }

    @FXML
    private void tl_AltGrStisknuto() {
        this.tl_caps.setSelected(false);
        odznacShift();
        if (this.tl_altGr.isSelected()) {
            klice.forEach((klic) -> this.MAPY_TLACITEK.get(klic).setText(this.vybraneRozvrzeni.getAltGrSadu().getOrDefault(klic, ' ').toString()));
        } else {
            this.tl_altGr.setSelected(false);
            klice.forEach((klic) -> this.MAPY_TLACITEK.get(klic).setText(this.vybraneRozvrzeni.getStandardniSadu().getOrDefault(klic, ' ').toString()));
        }
    }

    @FXML
    private void zmenJazyk() {
        odznacShift();
        if (this.tl_altGr.isSelected()) {
            this.tl_altGr.setSelected(false);
        }
        if (this.tl_caps.isSelected()) {
            this.tl_caps.setSelected(false);
        }
        int index = this.SADY.lastIndexOf(this.vybraneRozvrzeni);
        if ((index + 1) < this.SADY.size()) {
            this.vybraneRozvrzeni = this.SADY.get(index + 1);
        } else {
            this.vybraneRozvrzeni = this.SADY.get(0);
        }
        initStdKlavesy();
    }

    @FXML
    private void tl_qStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.Q);
    }

    @FXML
    private void tl_wStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.W);
    }

    @FXML
    private void tl_eStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.E);
    }

    @FXML
    private void tl_rStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.R);
    }

    @FXML
    private void tl_tStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.T);
    }

    @FXML
    private void tl_zStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.Z);
    }

    @FXML
    private void tl_uStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.U);
    }

    @FXML
    private void tl_iStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.I);
    }

    @FXML
    private void tl_oStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.O);
    }

    @FXML
    private void tl_pStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.P);
    }

    @FXML
    private void tl_aStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.A);
    }

    @FXML
    private void tl_sStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.S);
    }

    @FXML
    private void tl_dStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.D);
    }

    @FXML
    private void tl_fStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.F);
    }

    @FXML
    private void tl_gStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.G);
    }

    @FXML
    private void tl_hStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.H);
    }

    @FXML
    private void tl_jStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.J);
    }

    @FXML
    private void tl_kStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.K);
    }

    @FXML
    private void tl_lStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.L);
    }

    @FXML
    private void tl_yStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.Y);
    }

    @FXML
    private void tl_xStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.X);
    }

    @FXML
    private void tl_cStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.C);
    }

    @FXML
    private void tl_vStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.V);
    }

    @FXML
    private void tl_bStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.B);
    }

    @FXML
    private void tl_nStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.N);
    }

    @FXML
    private void tl_mStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.M);
    }

    @FXML
    private void tl_mezeraStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.MEZERA);
    }

    @FXML
    private void tl_carkaStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.CARKA);
    }

    @FXML
    private void tl_teckaStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.TECKA);
    }

    @FXML
    private void tl_pomlckaStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.POMLCKA);
    }

    @FXML
    private void tl_uKrStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.U_S_KROUZKEM);
    }

    @FXML
    private void tl_paragrafStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.PARAGRAF);
    }

    @FXML
    private void tl_uDlStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.U_DLOUHE);
    }

    @FXML
    private void tl_zavorkaStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.ZAVORKY);
    }

    @FXML
    private void tl_umlautStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.UMLAUT);
    }

    @FXML
    private void tl_strednikStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.STREDNIK);
    }

    @FXML
    private void tl_1Stisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.PLUS);
    }

    @FXML
    private void tl_2Stisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.E_HACEK);
    }

    @FXML
    private void tl_3Stisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.S_HACEK);
    }

    @FXML
    private void tl_4Stisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.C_HACEK);
    }

    @FXML
    private void tl_5Stisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.R_HACEK);
    }

    @FXML
    private void tl_6Stisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.Z_HACEK);
    }

    @FXML
    private void tl_7Stisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.Y_CARKA);
    }

    @FXML
    private void tl_8Stisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.A_CARKA);
    }

    @FXML
    private void tl_9Stisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.I_CARKA);
    }

    @FXML
    private void tl_0Stisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.E_CARKA);
    }

    @FXML
    private void tl_rovnoStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.ROVNA_SE);
    }

    @FXML
    private void tl_akcentStisknuto() {
        zjistiAPredejPismenko(Rozvrzeni.CARKA_NAD_PISMENY);
    }

    @FXML
    private void tl_num1Stisknuto() {
        zapisPismenkoDoPole('1');
    }

    @FXML
    private void tl_num2Stisknuto() {
        zapisPismenkoDoPole('2');
    }

    @FXML
    private void tl_num3Stisknuto() {
        zapisPismenkoDoPole('3');
    }

    @FXML
    private void tl_num4Stisknuto() {
        zapisPismenkoDoPole('4');
    }

    @FXML
    private void tl_num5Stisknuto() {
        zapisPismenkoDoPole('5');
    }

    @FXML
    private void tl_num6Stisknuto() {
        zapisPismenkoDoPole('6');
    }

    @FXML
    private void tl_num7Stisknuto() {
        zapisPismenkoDoPole('7');
    }

    @FXML
    private void tl_num8Stisknuto() {
        zapisPismenkoDoPole('8');
    }

    @FXML
    private void tl_num9Stisknuto() {
        zapisPismenkoDoPole('9');
    }

    @FXML
    private void tl_num0Stisknuto() {
        zapisPismenkoDoPole('0');
    }

    @FXML
    private void tl_numCarkaStisknuto() {
        zapisPismenkoDoPole(',');
    }

    @FXML
    private void tl_numPlusStisknuto() {
        zapisPismenkoDoPole('+');
    }

    @FXML
    private void tl_numMinusStisknuto() {
        zapisPismenkoDoPole('-');
    }

    @FXML
    private void tl_numKratStisknuto() {
        zapisPismenkoDoPole('*');
    }

    @FXML
    private void tl_numDelenoStisknuto() {
        zapisPismenkoDoPole('/');
    }

    @FXML
    private void tl_VybratVseStisknuto() {
        this.editacniPole.selectAll();
    }

    @FXML
    private void tl_backspaceStisknuto() {
        predejAkcniKlavesu(KeyCode.BACK_SPACE);
    }

    @FXML
    private void tl_endStisknuto() {
        if (tl_lShift.isSelected() || tl_pShift.isSelected()) {
            this.editacniPole.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.CHAR_UNDEFINED, KeyEvent.CHAR_UNDEFINED, KeyCode.END, true, false, false, false));
        } else if (tl_lCtrl.isSelected() || tl_pCtrl.isSelected()) {
            this.editacniPole.end();
            this.editacniPole.requestFocus();
        } else {
            predejAkcniKlavesu(KeyCode.END);
        }
    }

    @FXML
    private void tl_homeStisknuto() {
        if (tl_lShift.isSelected() || tl_pShift.isSelected()) {
            this.editacniPole.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.CHAR_UNDEFINED, KeyEvent.CHAR_UNDEFINED, KeyCode.HOME, true, false, false, false));
        } else if (tl_lCtrl.isSelected() || tl_pCtrl.isSelected()) {
            this.editacniPole.home();
            this.editacniPole.requestFocus();
        } else {
            predejAkcniKlavesu(KeyCode.HOME);
        }
    }

    @FXML
    private void tl_sipkaDolevaStisknuto() {
        predejAkcniKlavesu(KeyCode.LEFT);
    }

    @FXML
    private void tl_sipkaDopravaStisknuto() {
        predejAkcniKlavesu(KeyCode.RIGHT);
    }

    @FXML
    private void tl_sipkaDoluStisknuto() {
        predejAkcniKlavesu(KeyCode.DOWN);
    }

    @FXML
    private void tl_sipkaNahoruStisknuto() {
        predejAkcniKlavesu(KeyCode.UP);
    }

    @FXML
    private void tl_enterStisknuto() {
        if (this.poleKVyplneni.get(this.indexPoleKVyplneni).getPole() instanceof TextArea) {
            editacniPole.appendText("\n");
            this.editacniPole.requestFocus();
        } else if (this.tl_dalsi.isVisible()) {
            tl_dalsiStisknuto();
        } else {
            prijemce.editaceUkoncena();
            dialog.zavriDialog();
        }
    }

    @FXML
    private void tl_deleteStisknuto() {
        predejAkcniKlavesu(KeyCode.DELETE);
    }

    @FXML
    private void tl_kopirovatStisknuto() {
        if (this.editacniPole.getSelection().getLength() > 0) {
            Map<DataFormat, Object> obsah = new HashMap<>();
            obsah.put(DataFormat.PLAIN_TEXT, this.editacniPole.getSelectedText());
            Clipboard.getSystemClipboard().setContent(obsah);
        }
        this.editacniPole.requestFocus();
    }

    @FXML
    private void tl_vlozitStisknuto() {
        String obsah = Clipboard.getSystemClipboard().getContent(DataFormat.PLAIN_TEXT).toString();
        if (this.editacniPole.getSelection().getLength() > 0) {
            //TODO
        } else {
            this.editacniPole.appendText(obsah);
        }
        this.editacniPole.requestFocus();
    }

    @FXML
    private void tl_predchoziStisknuto() {
        if (tl_predchozi.isVisible()) {
            this.poleKVyplneni.get(this.indexPoleKVyplneni).getPole().setText(this.editacniPole.getText());
            odeberListenery();
            if (this.indexPoleKVyplneni - 1 >= 0) {
                this.indexPoleKVyplneni--;
                this.editacniPole.clear();
                setVlastnostiEditace();
            }
        }
    }

    @FXML
    private void tl_dalsiStisknuto() {
        if (this.tl_dalsi.isVisible()) {
            this.poleKVyplneni.get(this.indexPoleKVyplneni).getPole().setText(this.editacniPole.getText());
            odeberListenery();
            if (this.indexPoleKVyplneni + 1 < this.poleKVyplneni.size()) {
                this.indexPoleKVyplneni++;
                this.editacniPole.clear();
                setVlastnostiEditace();
            }
        }
    }

    private void odeberListenery() {
        if (hodnotoveListenery.get(indexPoleKVyplneni) != null) {
            this.editacniPole.textProperty().removeListener(hodnotoveListenery.get(indexPoleKVyplneni));
        }
        if (delkoveListenery.get(indexPoleKVyplneni) != null) {
            this.editacniPole.textProperty().removeListener(delkoveListenery.get(indexPoleKVyplneni));
        }
    }

    private void predejAkcniKlavesu(KeyCode kod) {
        this.editacniPole.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.CHAR_UNDEFINED, KeyEvent.CHAR_UNDEFINED, kod, false, false, false, false));
        this.editacniPole.requestFocus();
    }

    /**
     * Initializes the controller class.
     *
     * @param url url
     * @param rb  resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.SADY.add(new CS_CZ());
        this.SADY.add(new DE_DE());
        this.SADY.add(new EN_US());
        this.SADY.stream().filter(Rozvrzeni::jeVychozi).forEach((r) -> this.vybraneRozvrzeni = r);
        if (this.vybraneRozvrzeni == null) {
            this.vybraneRozvrzeni = new CS_CZ();
        }
        initPismenoveKlavesy();
        initStdKlavesy();
        editacniPole.setTooltip(ttip);
    }

    private class EnterListener implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (newValue.contains("\n")) {
                if (oldValue.isEmpty()) {
                    editacniPole.setText("");
                } else {
                    editacniPole.setText(oldValue);
                }
            }
        }
    }

    private class OmezeniDelkyListener implements ChangeListener<String> {

        private final Pomocnici.TypyVstupnichHodnot typ;
        private final int maxDelka;

        OmezeniDelkyListener(Pomocnici.TypyVstupnichHodnot typ, int maxDelka) {
            this.typ = typ;
            this.maxDelka = maxDelka;
        }

        @Override
        public void changed(ObservableValue<? extends String> observable, String stare, String nove) {
            switch (typ) {
                case CISLO:
                    ttip.setText("Do tohoto pole lze napsat celé číslo mezi -" + maxDelka + " a " + maxDelka);
                    if (!((nove.matches("\\d") && (Long.parseLong(nove) >= maxDelka && Long.parseLong(nove) <= maxDelka)) || nove.isEmpty())) {
                        defaultAkce(stare, ttip);
                    } else {
                        if (ttip.isShowing()) {
                            ttip.hide();
                        }
                    }
                    break;
                case DECIMAL:
                    ttip.setText("Do tohoto pole lze napsat desetinné číslo mezi " + maxDelka + " a " + maxDelka);
                    if (!((nove.matches("(\\d+)|(\\d+([.|,])?)") && (Double.parseDouble(nove) >= maxDelka && Double.parseDouble(nove) <= maxDelka)) || nove.isEmpty())) {
                        defaultAkce(stare, ttip);
                    } else {
                        if (ttip.isShowing()) {
                            ttip.hide();
                        }
                    }
                    break;
                default:
                    ttip.setText("Do tohoto pole lze napsat text dlouhý max. " + maxDelka + " znaků.");
                    if (!(nove.length() <= maxDelka || nove.isEmpty())) {
                        defaultAkce(stare, ttip);
                    } else {
                        if (ttip.isShowing()) {
                            ttip.hide();
                        }
                    }
                    break;
            }
        }
    }

    private class OmezeniHodnotyListener implements ChangeListener<String> {
        private final String regex;

        OmezeniHodnotyListener(String regex, String textTooltipu) {
            this.regex = regex;
            ttip.setText(textTooltipu);
        }

        @Override
        public void changed(ObservableValue<? extends String> observable, String stare, String nove) {
            if (!(nove.matches(regex) || nove.isEmpty())) {
                defaultAkce(stare, ttip);
            } else {
                if (ttip.isShowing()) {
                    ttip.hide();
                }
            }
        }
    }

    private void defaultAkce(String stare, Tooltip ttip) {
        //Toolkit.getDefaultToolkit().beep();
        if (stare.isEmpty()) {
            editacniPole.setText("");
        } else {
            editacniPole.setText(stare);
        }
        ttip.show(editacniPole, dialog.getX() + editacniPole.getLayoutX(), dialog.getY() + editacniPole.getLayoutY() + 50);
    }
}
