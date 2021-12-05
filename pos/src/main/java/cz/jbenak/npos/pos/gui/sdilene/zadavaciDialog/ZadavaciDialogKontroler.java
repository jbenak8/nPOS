/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.sdilene.zadavaciDialog;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.sdilene.ciselnaKlavesnice.CiselnaKlavesniceKontroler;
import cz.jbenak.npos.pos.gui.sdilene.ciselnaKlavesnice.ICiselnaKlavesnice;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-27
 */
public class ZadavaciDialogKontroler implements Initializable, ICiselnaKlavesnice {

    private ZadavaciDialog dialog;
    private boolean initText;
    @FXML
    private ImageView obrazek;
    @FXML
    private Label popis;
    @FXML
    private TextField pole;
    @FXML
    private CiselnaKlavesniceKontroler ciselnaKlavesniceController;
    @FXML
    private Label popisZadani;

    protected void setDialog(ZadavaciDialog dialog) {
        this.dialog = dialog;
        Pomocnici.setCiselneOmezeni(pole, Pomocnici.REGEX_KLADNE_CISLO,
                "Do tohoto pole lze zadat pouze celé nebo desetinné kladné číslo max. se dvěma desetinnými místy.");
    }

    protected void nastavDialog() {
        switch (dialog.getTyp()) {
            case CENA:
                obrazek.setImage(new Image(getClass().getResourceAsStream("img/zmena_ceny_velke.png")));
                pole.setPromptText("Nová cena");
                if (dialog.getPolozka().getJednotkovaCena() != null) {
                    pole.setText(Pomocnici.formatujNaDveMista(dialog.getPolozka().getJednotkovaCena()));
                }
                popis.setText("Zadejte prosím cenu pro položku " + dialog.getPolozka().getRegistr() + " - " + dialog.getPolozka().getNazev());
                popisZadani.setText("");
                break;
            case MNOZSTVI:
                obrazek.setImage(new Image(getClass().getResourceAsStream("img/zbozi_mnozstvi_velke.png")));
                pole.setPromptText("Nové množství");
                if (dialog.getPolozka().getMnozstvi() != null) {
                    pole.setText(Pomocnici.formatujNaDveMista(dialog.getPolozka().getMnozstvi()));
                }
                popis.setText("Zadejte prosím množství pro položku " + dialog.getPolozka().getRegistr() + " - " + dialog.getPolozka().getNazev());
                popisZadani.setText(dialog.getPolozka().getMj().getJednotka());
                break;
            case SLEVA_HODNOTA:
                obrazek.setImage(new Image(getClass().getResourceAsStream("img/zbozi_sleva_hodnota_velke.png")));
                pole.setPromptText("Hodnotová sleva");
                //pokud má slevu
                popis.setText("Zadejte prosím hodnotu korunové slevy pro položku " + dialog.getPolozka().getRegistr() + " - " + dialog.getPolozka().getNazev());
                popisZadani.setText("");
                break;
            case SLEVA_PROCENTNI:
                Pomocnici.setCiselneOmezeni(pole, Pomocnici.REGEX_PROCENTA,
                        "Do tohoto pole lze zadat pouze číslo odpovídající procentům s max. dvěma desetinnými místy.");
                obrazek.setImage(new Image(getClass().getResourceAsStream("img/zbozi_sleva_proc_velke.png")));
                pole.setPromptText("Procentní sleva");
                //pokud má slevu
                popis.setText("Zadejte prosím hodnotu procentní slevy pro položku " + dialog.getPolozka().getRegistr() + " - " + dialog.getPolozka().getNazev());
                popisZadani.setText("%");
                break;
            default:
                break;
        }
        pole.requestFocus();
        pole.selectAll();
        initText = true;
    }

    protected void fokus() {
        pole.requestFocus();
        pole.selectAll();
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
        if (evt.getCode() == KeyCode.ENTER) {
            potvrdVstup();
        }
        if(!pole.isFocused()) {
            predejZnak(evt.getText());
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ciselnaKlavesniceController.setPrijemce(this);
    }

    @Override
    public void predejZnak(String znak) {
        if (initText) {
            pole.clear();
            initText = false;
        }
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
        if (pole.getText().isEmpty()) {
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Zadejte hodnotu", "Nemohu pracovat s ničím. Zadejte prosím požadovanou hodnotu.", dialog).zobrazDialogAPockej();
            pole.requestFocus();
        } else {
            BigDecimal vstup = new BigDecimal(pole.getText().replace(",", "."));
            switch (dialog.getTyp()) {
                case CENA:
                    dialog.zadejCenu(vstup);
                    break;
                case MNOZSTVI:
                    dialog.zadejMnozstvi(vstup);
                    break;
                case SLEVA_HODNOTA:
                    dialog.zadejHodnotovouSlevu(vstup);
                    break;
                case SLEVA_PROCENTNI:
                    dialog.zadejProcentniSlevu(vstup);
                    break;
                default:
                    break;
            }
        }
    }
}
