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

    protected void showCRUDresultErrorDialog(String message) {
        InfoDialog CRUDErrorDialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
        CRUDErrorDialog.setDialogTitle("Chyba uložení");
        CRUDErrorDialog.setDialogSubtitle("Nastala chyba při ukládání dat.");
        CRUDErrorDialog.setDialogMessage(message);
    }

    protected void showSaveErrorDialog(Throwable e) {
        Utils.showGenricErrorDialog(e, "Chyba uložení", "Data nebylo možné uložit.", "Data nebylo možné uložit z důvodu jiné chyby:");
    }
}
