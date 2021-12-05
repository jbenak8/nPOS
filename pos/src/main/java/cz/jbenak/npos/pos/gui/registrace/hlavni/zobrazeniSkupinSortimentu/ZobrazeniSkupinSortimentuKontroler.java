/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni.zobrazeniSkupinSortimentu;

import cz.jbenak.npos.pos.gui.registrace.hlavni.zobrazeniSkupinSortimentu.modely.PolozkaSeznamuSkupinSortimentu;
import cz.jbenak.npos.pos.gui.sdilene.kontrolery.KontrolerTabulkovehoDialogu;
import cz.jbenak.npos.pos.objekty.sortiment.SkupinaSortimentu;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2019-09-16
 */
public class ZobrazeniSkupinSortimentuKontroler extends KontrolerTabulkovehoDialogu<ZobrazeniSkupinSortimentu, SkupinaSortimentu, PolozkaSeznamuSkupinSortimentu> {

    private final static Logger LOGER = LogManager.getLogger(ZobrazeniSkupinSortimentuKontroler.class);
    @FXML
    private TableColumn<PolozkaSeznamuSkupinSortimentu, String> slId;
    @FXML
    private TableColumn<PolozkaSeznamuSkupinSortimentu, String> slSubId;
    @FXML
    private TableColumn<PolozkaSeznamuSkupinSortimentu, String> slNazev;

    @Override
    protected PolozkaSeznamuSkupinSortimentu getPolozkaTabulky(SkupinaSortimentu polozka) {
        return new PolozkaSeznamuSkupinSortimentu(polozka);
    }

    @Override
    protected void tlOkStisknuto() {
        LOGER.debug("Potvrzovací tlačítko stisknuto.");
        if (tabulka.getSelectionModel().getSelectedIndex() > -1) {
            dialog.setVybranaSkupina(polozky.get(tabulka.getSelectionModel().getSelectedIndex()).getSkupina());
            tlZavritStisknuto();
        }
    }

    @Override
    protected void zpracujRozsireneKlavesy(KeyEvent evt) {

    }

    @Override
    protected boolean jePredikatOK(PolozkaSeznamuSkupinSortimentu predikat, String filtr, boolean predikatOK) {
        switch (filtrPodleSloupecku) {
            case 0:
                if (predikat.getID().getValueSafe().toLowerCase().contains(filtr)) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle ID skupiny.");
                }
                break;
            case 1:
                if (predikat.getIdNadrazeneSkupiny().getValueSafe().toLowerCase().contains(filtr)) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle ID nadřazené skupiny.");
                }
                break;
            case 2:
                if (predikat.getNazev().getValueSafe().toLowerCase().contains(filtr)) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Názvu.");
                }
                break;
            default:
                predikatOK = false;
                popisFiltrDle.setText("");
                break;
        }
        return predikatOK;
    }

    @Override
    protected void inicializujSloupecky() {
        slId.setCellValueFactory(data -> data.getValue().getID());
        slSubId.setCellValueFactory(data -> data.getValue().getIdNadrazeneSkupiny());
        slNazev.setCellValueFactory(data -> data.getValue().getNazev());
    }


}
