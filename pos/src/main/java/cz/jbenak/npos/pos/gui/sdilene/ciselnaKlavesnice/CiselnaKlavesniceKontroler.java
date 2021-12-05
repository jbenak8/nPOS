/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.sdilene.ciselnaKlavesnice;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Kontroler pro číselnou klávesnici.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-01
 */
public class CiselnaKlavesniceKontroler implements Initializable {
    
    private ICiselnaKlavesnice iCiselnaKlavesniceImpl;
    
    /**
     * Nastaví příjemce událostí a dat z klávesnice.
     * @param prijemce Příjemce dat. Implementace {@code ICiselnaKlavesnice}.
     */
    public void setPrijemce(ICiselnaKlavesnice prijemce) {
        this.iCiselnaKlavesniceImpl = prijemce;
    }
    
    @FXML
    private void tl_1_stisknuto() {
        iCiselnaKlavesniceImpl.predejZnak("1");
    }
    
    @FXML
    private void tl_2_stisknuto() {
        iCiselnaKlavesniceImpl.predejZnak("2");
    }
    
    @FXML
    private void tl_3_stisknuto() {
        iCiselnaKlavesniceImpl.predejZnak("3");
    }
    
    @FXML
    private void tl_4_stisknuto() {
        iCiselnaKlavesniceImpl.predejZnak("4");
    }
    
    @FXML
    private void tl_5_stisknuto() {
        iCiselnaKlavesniceImpl.predejZnak("5");
    }
    
    @FXML
    private void tl_6_stisknuto() {
        iCiselnaKlavesniceImpl.predejZnak("6");
    }
    
    @FXML
    private void tl_7_stisknuto() {
        iCiselnaKlavesniceImpl.predejZnak("7");
    }
    
    @FXML
    private void tl_8_stisknuto() {
        iCiselnaKlavesniceImpl.predejZnak("8");
    }
    
    @FXML
    private void tl_9_stisknuto() {
        iCiselnaKlavesniceImpl.predejZnak("9");
    }
    
    @FXML
    private void tl_0_stisknuto() {
        iCiselnaKlavesniceImpl.predejZnak("0");
    }
    
    @FXML
    private void tl_00_stisknuto() {
        iCiselnaKlavesniceImpl.predejZnak("00");
    }
    
    @FXML
    private void tl_carka_stisknuto() {
        iCiselnaKlavesniceImpl.predejZnak(",");
    }
    
    @FXML
    private void tlPotvrdStisknuto() {
        iCiselnaKlavesniceImpl.potvrdVstup();
    }
    
    @FXML
    private void tlSmazatStisknuto() {
        iCiselnaKlavesniceImpl.smazVstup();
    }
    
    @FXML
    private void tlBackspaceStisknuto() {
        iCiselnaKlavesniceImpl.umazZnak();
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
