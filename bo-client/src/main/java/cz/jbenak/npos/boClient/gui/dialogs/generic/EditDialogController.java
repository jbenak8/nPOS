package cz.jbenak.npos.boClient.gui.dialogs.generic;

import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.gui.helpers.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;

public abstract class EditDialogController<T> implements Initializable {

    protected EditDialog<T, ? extends EditDialogController<T>> dialog;
    protected T dataEdited = null;

    public void setDialog(EditDialog<T, ? extends EditDialogController<T>> dialog) {
        this.dialog = dialog;
    }

    public void setDataEdited(T dataEdited) {
        this.dataEdited = dataEdited;
    }

    protected abstract void save();

    @FXML
    protected abstract void keyPressed(KeyEvent evt);

    protected void showDataNotSameDialog(Object saved, Object toSave) {
        InfoDialog notSameInfoDialog = new InfoDialog(InfoDialog.InfoDialogType.WARNING, BoClient.getInstance().getMainStage(), false);
        notSameInfoDialog.setDialogTitle("Chyba uložení");
        notSameInfoDialog.setDialogSubtitle("Uložená a ukládaná data nejsou konzistentní!");
        notSameInfoDialog.setDialogMessage("Uložená data (" + saved.toString() + ") se liší od dat k uložení (" + toSave.toString() + ").\n" +
                "Prosím zkontrolujte protokoly klienta a serveru a ohlaste toto adaministrátorovi.");
    }

    protected void showSaveErrorDialog(Throwable e) {
        Utils.showGenricErrorDialog(e, "Chyba uložení", "Data nebylo možné uložit.", "Data nebylo možné uložit z důvodu jiné chyby:");
    }
}
