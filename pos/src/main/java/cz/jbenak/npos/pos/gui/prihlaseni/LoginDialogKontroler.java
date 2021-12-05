/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.prihlaseni;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.sdilene.ciselnaKlavesnice.CiselnaKlavesniceKontroler;
import cz.jbenak.npos.pos.gui.sdilene.ciselnaKlavesnice.ICiselnaKlavesnice;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.system.Pos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @since 2018-02-02
 * @version 1.0.0.0
 */
public class LoginDialogKontroler implements Initializable, ICiselnaKlavesnice {

    private enum Pole {
        ID, HESLO
    }

    private LoginDialog dialog;
    private Pole oznacenePole;
    @FXML
    private CiselnaKlavesniceKontroler ciselnaKlavesniceController;
    @FXML
    private TextField poleID;
    @FXML
    private PasswordField poleHeslo;

    protected void setDialog(LoginDialog dialog) {
        this.dialog = dialog;oznacID();
    }
    
    void oznacID() {
        oznacenePole = Pole.ID;
        poleID.requestFocus();
    }

    @FXML
    private void tlZavritStisknuto() {
        dialog.zavriDialog();
    }

    @FXML
    private void klavesaStisknuta(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ESCAPE) {
            tlZavritStisknuto();
        }
        if (evt.getCode() == KeyCode.TAB) {
            poleOznaceno();
        }
        if (evt.getCode() == KeyCode.ENTER) {
            potvrdVstup();
        }
        if(evt.getText().matches(Pomocnici.REGEX_CELE_KLADNE_CISLO)) {
            if(!poleID.isFocused() || !poleHeslo.isFocused()) {
                predejZnak(evt.getText());
            }
        }
    }

    @FXML
    private void poleOznaceno() {
        if (poleID.isFocused()) {
            oznacenePole = Pole.ID;
        }
        if (poleHeslo.isFocused()) {
            oznacenePole = Pole.HESLO;
        }
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
        poleID.setPromptText("Sem zadejte Uživatelské ID");
        poleHeslo.setPromptText("Sem zadejte Heslo uživatele");
        Pomocnici.setCiselneOmezeni(this.poleHeslo, Pomocnici.REGEX_CELE_KLADNE_CISLO, "Lze zadat pouze číslice.");
        Pomocnici.setCiselneOmezeni(this.poleID, Pomocnici.REGEX_CELE_KLADNE_CISLO, "Lze zadat pouze číslice.");
        oznacenePole = Pole.ID;
        poleID.requestFocus();
    }

    @Override
    public void predejZnak(String znak) {
        if (oznacenePole == Pole.ID) {
            poleID.setText(poleID.getText() + znak);
        }
        if (oznacenePole == Pole.HESLO) {
            poleHeslo.setText(poleHeslo.getText() + znak);
        }
    }

    @Override
    public void umazZnak() {
        if (oznacenePole == Pole.ID) {
            if (!poleID.getText().isEmpty()) {
                poleID.setText(poleID.getText().substring(0, poleID.getText().length() - 1));
            }
        }
        if (oznacenePole == Pole.HESLO) {
            if (!poleHeslo.getText().isEmpty()) {
                poleHeslo.setText(poleHeslo.getText().substring(0, poleHeslo.getText().length() - 1));
            }
        }
    }

    @Override
    public void smazVstup() {
        if (oznacenePole == Pole.ID) {
            poleID.setText("");
        }
        if (oznacenePole == Pole.HESLO) {
            poleHeslo.setText("");
        }
    }

    @Override
    public void potvrdVstup() {
        if (oznacenePole == Pole.ID) {
            poleHeslo.requestFocus();
            oznacenePole = Pole.HESLO;
        } else if (this.poleID.getText().isEmpty()) {
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Chybí ID uživatele", "ID uživatele není vyplněno. Vyplňte prosím ID uživatele.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
            oznacenePole = Pole.ID;
            poleID.requestFocus();
        } else if (this.poleHeslo.getText().isEmpty()) {
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Chybí heslo uživatele", "Heslo uživatele není vyplněno. Vyplňte prosím heslo uživatele.", Pos.getInstance().getAplikacniOkno()).zobrazDialog(true);
            oznacenePole = Pole.HESLO;
            this.poleHeslo.requestFocus();
        } else {
            dialog.prihlasUzivatele(Integer.parseInt(poleID.getText()), Integer.parseInt(poleHeslo.getText()));
            poleHeslo.setText("");
            poleID.setText("");
        }
    }

}
