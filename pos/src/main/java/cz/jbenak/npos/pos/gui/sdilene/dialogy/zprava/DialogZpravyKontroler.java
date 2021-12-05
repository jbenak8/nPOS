package cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-01-30
 */
public class DialogZpravyKontroler implements Initializable {

    private DialogZpravy dialog;
    private boolean tlAktivni = true;
    @FXML
    private Label nadpis;
    @FXML
    private Label popis;
    @FXML
    private ImageView ikona;
    @FXML
    private Button tlOK;
    @FXML
    private Button tlZavrit;

    protected void setDialog(DialogZpravy dialog) {
        this.dialog = dialog;
    }

    public void zobrazPotvrzovaciTlacitko(boolean zobrazit) {
        this.tlOK.setVisible(zobrazit);
        this.tlZavrit.setVisible(zobrazit);
        this.tlAktivni = zobrazit;
    }

    public void setNadpis(String nadpis) {
        this.nadpis.setText(nadpis.toUpperCase());
    }

    public void setPopis(String popisChyby) {
        this.popis.setText(popisChyby);
    }

    public void setTypIkony(DialogZpravy.TypZpravy typ) {
        switch (typ) {
            case OK:
                ikona.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cz/jbenak/npos/pos/gui/sdilene/dialogy/zprava/img/ok_v2.png"))));
                //Platform.runLater((Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default"));
                break;
            case INFO:
                ikona.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cz/jbenak/npos/pos/gui/sdilene/dialogy/zprava/img/info_v2.png"))));
                //Platform.runLater((Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default"));
                break;
            case VAROVANI:
                ikona.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cz/jbenak/npos/pos/gui/sdilene/dialogy/zprava/img/varovani_v2.png"))));
                //Platform.runLater((Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation"));
                break;
            case CHYBA:
                ikona.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cz/jbenak/npos/pos/gui/sdilene/dialogy/zprava/img/chyba_v2.png"))));
                //Platform.runLater((Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.asterisk"));
                break;
            default:
                break;
        }
    }

    @FXML
    private void tlZavritStisknuto() {
        tlOKStisknuto();
    }

    @FXML
    private void tlOKStisknuto() {
        dialog.close();
    }

    @FXML
    private void klavesaStisknuta(KeyEvent evt) {
        if (tlAktivni && (evt.getCode() == KeyCode.ENTER || evt.getCode() == KeyCode.ESCAPE)) {
            tlOKStisknuto();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tlOK.setDefaultButton(true);
    }
}
