package cz.jbenak.npos.pos.gui.platba.platebniOkno.polozka;

import cz.jbenak.npos.pos.gui.Pomocnici;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Kontroler buňky v seznamu plateb.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2021-01-29
 */
public class PolozkaPlatbyBunkaKontroler implements Initializable {

    @FXML
    private Label nazevPolozky;
    @FXML
    private Label popisPolozky;
    @FXML
    private Label castka;

    public void setNazevPolozky(String text) {
        nazevPolozky.setText(text);
    }

    public void setPopisPolozky(String text) {
        popisPolozky.setText(text);
    }

    public void setCastka(BigDecimal hodnota, String symbol) {
        castka.setText(Pomocnici.formatujNaDveMista(hodnota) + " " + symbol);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nazevPolozky.setText("");
        popisPolozky.setText("");
        castka.setText("0,00");
    }
}
