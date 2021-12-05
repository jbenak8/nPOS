/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.utility.kalkulacka;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import static cz.jbenak.npos.pos.gui.utility.kalkulacka.KalkulackaKontroler.Operace.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-10
 */
public class KalkulackaKontroler implements Initializable {
    
    enum Operace {
        PLUS, MINUS, KRAT, DELENO, PROCENTA, NIC;
    }
    
    private final DecimalFormat DF = new DecimalFormat("#0.00");
    private Kalkulacka kalkulacka;
    private double data;
    private Operace operand;
    private boolean carkaStisknuta;
    private boolean moznoVymazat;
    @FXML
    private Label displej;
    @FXML
    private Label znamenko;

    protected void setKalkulacka(Kalkulacka kalkulacka) {
        this.kalkulacka = kalkulacka;
    }

    private void zobrazVysledek(double vysledek) {
        this.displej.setText(this.DF.format(vysledek));
    }

    private void zadejZnak(String znakKPridani) {
        if (this.moznoVymazat) {
            this.displej.setText("0,00");
            this.moznoVymazat = false;
        }
        if (this.displej.getText().isEmpty() || this.displej.getText().length() < 12) {
            if (this.displej.getText().isEmpty() || (this.displej.getText().equals("0,00") || this.displej.getText().equals("-0,00"))) {
                if (znakKPridani.equals(",")) {
                    this.displej.setText("0,");
                } else {
                    this.displej.setText(znakKPridani);
                }
            } else if (!(this.displej.getText().contains(",") && znakKPridani.equals(","))) {
                this.displej.setText(this.displej.getText() + znakKPridani);
            }
            if (this.carkaStisknuta && !znakKPridani.equals(",")) {
                if (this.displej.getText().equals("0,00") || this.displej.getText().equals("-0,00")) {
                    this.displej.setText(this.displej.getText() + znakKPridani);
                    this.carkaStisknuta = false;
                }
            }
        }
    }

    @FXML
    private void tlZavritStisknuto() {
        this.kalkulacka.close();
    }

    @FXML
    private void tl_1_stisknuto() {
        zadejZnak("1");
    }

    @FXML
    private void tl_2_stisknuto() {
        zadejZnak("2");
    }

    @FXML
    private void tl_3_stisknuto() {
        zadejZnak("3");
    }

    @FXML
    private void tl_4_stisknuto() {
        zadejZnak("4");
    }

    @FXML
    private void tl_5_stisknuto() {
        zadejZnak("5");
    }

    @FXML
    private void tl_6_stisknuto() {
        zadejZnak("6");
    }

    @FXML
    private void tl_7_stisknuto() {
        zadejZnak("7");
    }

    @FXML
    private void tl_8_stisknuto() {
        zadejZnak("8");
    }

    @FXML
    private void tl_9_stisknuto() {
        zadejZnak("9");
    }

    @FXML
    private void tl_0_stisknuto() {
        zadejZnak("0");
    }

    @FXML
    private void tl_00_stisknuto() {
        zadejZnak("00");
    }

    @FXML
    private void tl_carka_stisknuto() {
        this.carkaStisknuta = true;
        zadejZnak(",");
    }

    @FXML
    private void tlUmazatStistknuto() {
        if (this.displej.getText().length() == 1) {
            this.displej.setText("0,00");
        }
        if (this.displej.getText().length() > 1) {
            this.displej.setText(this.displej.getText().substring(0, this.displej.getText().length() - 1));
        }
    }

    @FXML
    private void tlSmazatStistknuto() {
        zobrazVysledek(0);
    }

    @FXML
    private void tlSmazatVseStistknuto() {
        zobrazVysledek(0);
        this.data = 0;
        this.operand = NIC;
        this.znamenko.setText("");
    }

    @FXML
    private void tlPlusMinusStistknuto() {
        if (!this.displej.getText().isEmpty()) {
            if (this.displej.getText().contains("-")) {
                this.displej.setText(this.displej.getText().substring(1));
            } else {
                this.displej.setText("-" + this.displej.getText());
            }
        }
    }

    @FXML
    private void tlOdmocninaStistknuto() {
        if (this.data != 0 && this.operand != NIC) {
            tlRovnaSeStistknuto();
        }
        this.data = Double.parseDouble(this.displej.getText().replace(",", "."));
        this.displej.setText(this.DF.format(Math.sqrt(data)));
        this.operand = NIC;
        this.data = 0;
        this.znamenko.setText("");
    }

    @FXML
    private void tlProcentaStistknuto() {
        if (this.data != 0 && this.operand != NIC) {
            tlRovnaSeStistknuto();
        }
        this.data = Double.parseDouble(this.displej.getText().replace(",", "."));
        this.displej.setText("0,00");
        this.operand = PROCENTA;
        this.znamenko.setText("%");
        if (this.data != 0) {
            this.znamenko.setText(this.DF.format(data) + " %");
        }
    }

    @FXML
    private void tlPlusStistknuto() {
        if (this.data != 0 && this.operand != NIC) {
            tlRovnaSeStistknuto();
        }
        this.data = Double.parseDouble(this.displej.getText().replace(",", "."));
        this.displej.setText("0,00");
        this.operand = PLUS;
        this.znamenko.setText("+");
        if (this.data != 0) {
            this.znamenko.setText(this.DF.format(data) + " +");
        }
    }

    @FXML
    private void tlMinusStistknuto() {
        if (this.data != 0 && this.operand != NIC) {
            tlRovnaSeStistknuto();
        }
        this.data = Double.parseDouble(this.displej.getText().replace(",", "."));
        this.displej.setText("0,00");
        this.operand = MINUS;
        this.znamenko.setText("-");
        if (this.data != 0) {
            this.znamenko.setText(this.DF.format(data) + " -");
        }
    }

    @FXML
    private void tlKratStistknuto() {
        if (this.data != 0 && this.operand != NIC) {
            tlRovnaSeStistknuto();
        }
        this.data = Double.parseDouble(this.displej.getText().replace(",", "."));
        this.displej.setText("0,00");
        this.operand = KRAT;
        this.znamenko.setText("×");
        if (this.data != 0) {
            this.znamenko.setText(this.DF.format(data) + " ×");
        }
    }

    @FXML
    private void tlDelenoStistknuto() {
        if (this.data != 0 && this.operand != NIC) {
            tlRovnaSeStistknuto();
        }
        this.data = Double.parseDouble(this.displej.getText().replace(",", "."));
        this.displej.setText("0,00");
        this.operand = DELENO;
        this.znamenko.setText("÷");
        if (this.data != 0) {
            this.znamenko.setText(this.DF.format(data) + " ÷");
        }
    }

    @FXML
    private void tlRovnaSeStistknuto() {
        double aktData = Double.parseDouble(this.displej.getText().replace(",", "."));
        switch (this.operand) {
            case PLUS:
                this.displej.setText(this.DF.format(this.data + aktData));
                break;
            case MINUS:
                this.displej.setText(this.DF.format(this.data - aktData));
                break;
            case KRAT:
                this.displej.setText(this.DF.format(this.data * aktData));
                break;
            case DELENO:
                if(aktData == 0) {
                    new DialogZpravy(DialogZpravy.TypZpravy.INFO, "Neplatná operace", "Nulou nelze dělit :)", kalkulacka).zobrazDialog(true);
                } else {
                this.displej.setText(this.DF.format(this.data / aktData));
                }
                break;
            case PROCENTA:
                this.displej.setText(this.DF.format((this.data / 100) * aktData));
                break;
            default:
                break;
        }
        this.operand = NIC;
        this.data = 0;
        this.znamenko.setText("");
        this.moznoVymazat = true;
    }

    /**
     * Bloky Key Eventů
     */
    @FXML
    private void klavesaStisknuta(KeyEvent evt) {

        switch (evt.getCode()) {
            case NUMPAD0:
                this.tl_0_stisknuto();
                break;
            case NUMPAD1:
                this.tl_1_stisknuto();
                break;
            case NUMPAD2:
                this.tl_2_stisknuto();
                break;
            case NUMPAD3:
                this.tl_3_stisknuto();
                break;
            case NUMPAD4:
                this.tl_4_stisknuto();
                break;
            case NUMPAD5:
                this.tl_5_stisknuto();
                break;
            case NUMPAD6:
                this.tl_6_stisknuto();
                break;
            case NUMPAD7:
                this.tl_7_stisknuto();
                break;
            case NUMPAD8:
                this.tl_8_stisknuto();
                break;
            case NUMPAD9:
                this.tl_9_stisknuto();
                break;
            case DIGIT0:
                this.tl_0_stisknuto();
                break;
            case DIGIT1:
                this.tl_1_stisknuto();
                break;
            case DIGIT2:
                this.tl_2_stisknuto();
                break;
            case DIGIT3:
                this.tl_3_stisknuto();
                break;
            case DIGIT4:
                this.tl_4_stisknuto();
                break;
            case DIGIT5:
                this.tl_5_stisknuto();
                break;
            case DIGIT6:
                this.tl_6_stisknuto();
                break;
            case DIGIT7:
                this.tl_7_stisknuto();
                break;
            case DIGIT8:
                this.tl_8_stisknuto();
                break;
            case DIGIT9:
                this.tl_9_stisknuto();
                break;
            case PERIOD:
                this.tl_carka_stisknuto();
                break;
            case DECIMAL:
                this.tl_carka_stisknuto();
                break;
            case ADD:
                this.tlPlusStistknuto();
                break;
            case SUBTRACT:
                this.tlMinusStistknuto();
                break;
            case DIVIDE:
                this.tlDelenoStistknuto();
                break;
            case MULTIPLY:
                this.tlKratStistknuto();
                break;
            case ENTER:
                this.tlRovnaSeStistknuto();
                break;
            case ESCAPE:
                this.kalkulacka.close();
                break;
            case BACK_SPACE:
                this.tlUmazatStistknuto();
                break;
            case DELETE:
                this.tlUmazatStistknuto();
                break;
            case P:
                this.tlProcentaStistknuto();
                break;
            case S:
                this.tlOdmocninaStistknuto();
                break;
            case C:
                this.tlSmazatStistknuto();
                break;
            case E:
                this.tlSmazatVseStistknuto();
                break;
            case M:
                this.tlPlusMinusStistknuto();
                break;
            default:
                break;
        }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        znamenko.setText("");
        displej.setText("0,00");
    }    
    
}
