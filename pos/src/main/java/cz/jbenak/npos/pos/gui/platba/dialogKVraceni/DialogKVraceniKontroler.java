package cz.jbenak.npos.pos.gui.platba.dialogKVraceni;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.objekty.doklad.Doklad;
import cz.jbenak.npos.pos.objekty.meny.Mena;
import cz.jbenak.npos.pos.procesory.DokladProcesor;
import cz.jbenak.npos.pos.procesory.PlatebniProcesor;
import cz.jbenak.npos.pos.system.Pos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Kontrolér dialogu pro vrácení peněz.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-02-20
 */
public class DialogKVraceniKontroler implements Initializable {

    private DialogKVraceni dialog;
    @FXML
    private Button tlOk;
    @FXML
    private Label nadpis;
    @FXML
    private Label popis_hodnota;
    @FXML
    private Label popis_zaplaceno;
    @FXML
    private Label popis_vratit;
    @FXML
    private Label popis_zavritSuplik;

    protected void setDialog(DialogKVraceni dialog) {
        this.dialog = dialog;
    }

    protected void zobrazPrvky() {
        tlOk.setVisible(dialog.isZobrazitPotvrzovaciTlacitko());
        popis_zavritSuplik.setVisible(!dialog.isZobrazitPotvrzovaciTlacitko());
        if (dialog.isZobrazitPotvrzovaciTlacitko()) {
            popis_zavritSuplik.setMaxWidth(0);
        } else {
            tlOk.setMaxWidth(0);
        }
        Doklad doklad = DokladProcesor.getInstance().getDoklad();
        Mena hl = Pos.getInstance().getHlavniMena();
        popis_hodnota.setText(Pomocnici.formatujNaDveMista(PlatebniProcesor.getInstance().zaokrouhliVeVybraneMene(hl, doklad.getCenaDokladuCelkem())) + " " + hl.getNarodniSymbol());
        popis_zaplaceno.setText(Pomocnici.formatujNaDveMista(doklad.getCelkemZaplaceno()) + " " + hl.getNarodniSymbol());
        popis_vratit.setText(Pomocnici.formatujNaDveMista(doklad.getCastkaKVraceni()) + " " + hl.getNarodniSymbol());
    }

    @FXML
    private void tlOkStisknuto() {
        dialog.zavriDialog();
    }

    @FXML
    private void klavesaStisknuta(KeyEvent evt) {
        if (dialog.isZobrazitPotvrzovaciTlacitko() && (evt.getCode() == KeyCode.ENTER || evt.getCode() == KeyCode.ESCAPE)) {
            tlOkStisknuto();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nadpis.setText("Hotovost k vrácení".toUpperCase(Locale.ROOT));
        popis_zavritSuplik.setText("Po vyplacení vracené částky zavřete zásuvku.");
    }
}
