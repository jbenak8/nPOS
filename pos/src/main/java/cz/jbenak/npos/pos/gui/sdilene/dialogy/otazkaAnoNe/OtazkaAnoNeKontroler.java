/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.sdilene.dialogy.otazkaAnoNe;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-02
 */
public class OtazkaAnoNeKontroler implements Initializable {
    
    private static final Logger LOGER = LogManager.getLogger(OtazkaAnoNeKontroler.class);
    private OtazkaAnoNe dialog;
    @FXML
    private Label nadpis;
    @FXML
    private Label popis;
    
    public void setNadpis(String nadpis) {
        this.nadpis.setText(nadpis.toUpperCase());
    }

    public void setPopis(String popisChyby) {
        this.popis.setText(popisChyby);
    }

    protected void setDialog(OtazkaAnoNe dialog) {
        this.dialog = dialog;
    }
        
    @FXML
    private void tlZavritStisknuto() {
        tlNeStisknuto();
    }

    @FXML
    private void tlAnoStisknuto() {
        LOGER.debug("Volba byla potvrzena.");
        dialog.setVolba(OtazkaAnoNe.Volby.ANO);
        dialog.close();
    }
    
    @FXML
    private void tlNeStisknuto() {
        LOGER.debug("Volba byla odmítnuta.");
        dialog.setVolba(OtazkaAnoNe.Volby.NE);
        dialog.close();
    }

    @FXML
    private void klavesaStisknuta(KeyEvent evt) {
        if (evt.getCode() == KeyCode.ENTER) {
            tlAnoStisknuto();
        }
        if(evt.getCode() == KeyCode.ESCAPE) {
            tlNeStisknuto();
        }
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
