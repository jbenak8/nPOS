/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.sdilene.textovaKlavesnice;

import javafx.fxml.FXML;

/**
 * Rozhraní pro použití klasické grafické textové klávesnice s numerickým blokem.
 *
 * @author Jan Benák
 * @version 1.1.0.0
 * @since 2019-07-24
 */
public interface ITextovaKlavesnice {

    /**
     * Metoda pro oznámení, že editace polí je ukončena a klávesnice se zavře.
     */
    void editaceUkoncena();

    /**
     * Metoda pro obsluhu stisku tlačítka
     */
    @FXML
    void tlZobrazitKlavesniciStisknuto();
}
