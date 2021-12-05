/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.sdilene.zadaniPopisu;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.ITextovaKlavesnice;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.PoleKVyplneni;
import cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice.TextovaKlavesnice;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-03-04
 */
public class ZadaniPopisuKontroler implements Initializable, ITextovaKlavesnice {
    
    private ZadaniPopisu dialog;
    @FXML
    private Label nadpis;
    @FXML
    private TextArea pole;

    protected void setDialog(ZadaniPopisu dialog) {
        this.dialog = dialog;
    }

    void fokus() {
        pole.requestFocus();
    }

    /**
     * Initializes the controller class.
     * @param url url volající
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nadpis.setText("Zadejte prosím popis");
        pole.setPromptText("Popis");
    }    

    public void tlZobrazitKlavesniciStisknuto() {
        List<PoleKVyplneni> vstupy = new LinkedList<>();
        vstupy.add(new PoleKVyplneni(pole, Pomocnici.TypyVstupnichHodnot.TEXT));
        TextovaKlavesnice klavesnice = new TextovaKlavesnice(this, dialog, vstupy);
        klavesnice.zobrazKlavesnici();
    }

    @FXML
    private void tlZavritStisknuto() {
        dialog.setZadaniOdmitnuto(true);
        dialog.zavriDialog();
    }

    @FXML
    private void tlOkStisknuto() {
        if (pole.getText().trim().isEmpty()) {
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nic není zadáno", "Prosím zadejte aspoň nějaký popis. Pouze mezery a odřádkování neberu.", dialog).zobrazDialogAPockej();
            pole.requestFocus();
        } else {
            dialog.setZadaniOdmitnuto(false);
            dialog.setPopis(pole.getText());
            dialog.zavriDialog();
        }
    }

    @FXML
    private void tlSmazatStisknuto() {
        pole.clear();
    }

    @FXML
    private void klavesaStisknuta(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ESCAPE) {
            tlZavritStisknuto();
        }
        if (evt.isControlDown() && evt.getCode() == KeyCode.ENTER) {
            tlOkStisknuto();
        }
        if ((evt.getCode() == KeyCode.F8)) {
            tlSmazatStisknuto();
        }
        if(!pole.isFocused()) {
            pole.appendText(evt.getText());
        }
        if (!pole.isFocused() && evt.getCode() == KeyCode.ENTER) {
            pole.appendText("\n");
        }
    }

    @Override
    public void editaceUkoncena() {

    }
}
