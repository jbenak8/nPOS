/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.jbenak.npos.pos.gui.registrace.hlavni.seznamSortimentu;

import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.registrace.hlavni.seznamSortimentu.modely.PolozkaTabulkySortimentu;
import cz.jbenak.npos.pos.gui.sdilene.kontrolery.KontrolerTabulkovehoDialogu;
import cz.jbenak.npos.pos.objekty.sortiment.SeznamRegistrace;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cz.jbenak.npos.pos.procesory.AutorizacniManazer.Funkce.INFO_O_SORTIMENTU;

/**
 * FXML Controller class
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-13
 */
public class SeznamSortimentuKontroler extends KontrolerTabulkovehoDialogu<SeznamSortimentu, SeznamRegistrace, PolozkaTabulkySortimentu> {

    private static final Logger LOGER = LogManager.getLogger(SeznamSortimentuKontroler.class);
    @FXML
    private TableColumn<PolozkaTabulkySortimentu, String> slRegistr;
    @FXML
    private TableColumn<PolozkaTabulkySortimentu, String> slNazev;
    @FXML
    private TableColumn<PolozkaTabulkySortimentu, String> slSkupina;
    @FXML
    private TableColumn<PolozkaTabulkySortimentu, String> slCena;

    @Override
    protected PolozkaTabulkySortimentu getPolozkaTabulky(SeznamRegistrace polozka) {
        return new PolozkaTabulkySortimentu(polozka);
    }

    @Override
    protected void tlOkStisknuto() {
        LOGER.debug("Volba byla potvrzena.");
        Sortiment polozka = new Sortiment();
        polozka.setRegistr(polozky.get(tabulka.getSelectionModel().getSelectedIndex()).getPolozka().getRegistr());
        dialog.setPolozka(polozka);
        dialog.close();
    }

    @Override
    protected void zpracujRozsireneKlavesy(KeyEvent evt) {
        if (evt.getCode() == KeyCode.F3) {
            tlInfoOPolozceStisknuto();
        }
    }

    @Override
    protected boolean jePredikatOK(PolozkaTabulkySortimentu predikat, String filtr, boolean predikatOK) {
        switch (filtrPodleSloupecku) {
            case 0:
                if (predikat.getRegistr().getValueSafe().toLowerCase().contains(filtr)) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Registru.");
                }
                break;
            case 1:
                if (predikat.getNazev().getValueSafe().toLowerCase().contains(filtr)) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Názvu.");
                }
                break;
            case 2:
                if (predikat.getSkupina().getValueSafe().toLowerCase().contains(filtr)) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Skupiny.");
                }
                break;
            case 3:
                if (predikat.getCena().getValueSafe().toLowerCase().contains(filtr)) {
                    predikatOK = true;
                    popisFiltrDle.setText("Filtr dle Ceny.");
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
        slRegistr.setCellValueFactory(data -> data.getValue().getRegistr());
        slNazev.setCellValueFactory(data -> data.getValue().getNazev());
        slSkupina.setCellValueFactory(data -> data.getValue().getSkupina());
        slCena.setCellValueFactory(data -> data.getValue().getCena());
        slSkupina.setCellFactory(this::getZarovnanaBunka);
        slNazev.setCellFactory(this::getZarovnanaBunka);
    }

    @Override
    protected void tlNeStisknuto() {
        LOGER.debug("Volba byla odmítnuta.");
        dialog.setPolozka(null);
        dialog.close();
    }

    @FXML
    private void tlInfoOPolozceStisknuto() {
        LOGER.debug("Tlačítkto pro informace o vybrané položce sortimentu bylo stisknuto.");
        if (HlavniOkno.getInstance().getAutorizacniManazer().autorizujFunkci(INFO_O_SORTIMENTU)) {
            dialog.zobrazInfoOPolozce(((PolozkaTabulkySortimentu) tabulka.getSelectionModel().getSelectedItem()).getPolozka());
        }
    }

    private TableCell<PolozkaTabulkySortimentu, String> getZarovnanaBunka(TableColumn<PolozkaTabulkySortimentu, String> sloupecek) {
        TableCell<PolozkaTabulkySortimentu, String> bunka = new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    Label text = new Label(item);
                    this.setMinHeight(80);
                    text.setWrapText(true);
                    text.setMinHeight(USE_COMPUTED_SIZE);
//                    text.wrappingWidthProperty().bind(sloupecek.widthProperty());
                    text.textProperty().bind(this.itemProperty());
                    this.setGraphic(text);

                }
            }
        };
        return bunka;
    }
}
