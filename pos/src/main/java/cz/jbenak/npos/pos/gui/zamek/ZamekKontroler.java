/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.zamek;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.sdilene.ciselnaKlavesnice.CiselnaKlavesniceKontroler;
import cz.jbenak.npos.pos.gui.sdilene.ciselnaKlavesnice.ICiselnaKlavesnice;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-03-01
 */
public class ZamekKontroler implements Initializable, ICiselnaKlavesnice {

    private Zamek dialog;
    @FXML
    private Label nadpis;
    @FXML
    private PasswordField pole;
    @FXML
    private CiselnaKlavesniceKontroler ciselnaKlavesniceController;

    protected void setDialog(Zamek dialog) {
        this.dialog = dialog;
        Pomocnici.setCiselneOmezeni(pole, Pomocnici.REGEX_CELE_KLADNE_CISLO, "Lze zadat pouze číslice.");
        nadpis.setText("Systém zamknut. Pro odemčení prosím zadejte heslo uživatele " + HlavniOkno.getInstance().getPrihlasenaPokladni().getId());
        fokus();
    }

    void fokus() {
        pole.requestFocus();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ciselnaKlavesniceController.setPrijemce(this);
        pole.setPromptText("Heslo uživatele");
    }

    @Override
    public void predejZnak(String znak) {
        pole.appendText(znak);
    }

    @Override
    public void umazZnak() {
        if (!pole.getText().isEmpty()) {
            pole.setText(pole.getText(0, pole.getText().length() - 1));
        }
    }

    @Override
    public void smazVstup() {
        pole.clear();
    }

    @Override
    public void potvrdVstup() {
        if(pole.getText().isEmpty()) {
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Zadejte hodnotu", "Nemohu pracovat s ničím. Zadejte prosím Heslo aktuálně přihlášeného uživatele.", dialog).zobrazDialogAPockej();
            pole.requestFocus();
        } else {
            dialog.odemkniProgram(Integer.parseInt(pole.getText()));
            pole.clear();
        }
    }

    @FXML
    private void klavesaStisknuta(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ENTER) {
            potvrdVstup();
        }
        if(evt.getText().matches("\\d+")) {
            if(!pole.isFocused()) {
                predejZnak(evt.getText());
            }
        }
    }
}
