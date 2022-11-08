package cz.jbenak.npos.boClient.gui.dialogs.generic;

import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.gui.helpers.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class EditDialogController<T> implements Initializable {

    protected EditDialog<T, ? extends EditDialogController<T>> dialog;
    protected T dataEdited = null;
    @FXML
    protected Label title;

    public void setDialog(EditDialog<T, ? extends EditDialogController<T>> dialog) {
        this.dialog = dialog;
    }

    public abstract void setDataEdited(T dataEdited);

    protected abstract void save();

    @FXML
    protected void keyPressed(KeyEvent evt) {
        if (evt.isControlDown() && evt.getCode() == KeyCode.ENTER) {
            savePressed();
        }
        if (evt.getCode() == KeyCode.ESCAPE) {
            dialog.closeDialog();
        }
    }

    @FXML
    protected abstract void savePressed();

    @FXML
    protected void cancelPressed() {
        dialog.setCancelled(true);
        dialog.closeDialog();
    }

    protected void showCRUDresultErrorDialog(String message) {
        InfoDialog CRUDErrorDialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
        CRUDErrorDialog.setDialogTitle("Chyba uložení");
        CRUDErrorDialog.setDialogSubtitle("Nastala chyba při ukládání dat.");
        CRUDErrorDialog.setDialogMessage(message);
        CRUDErrorDialog.showDialog();
    }

    protected void showSaveErrorDialog(Throwable e) {
        Utils.showGenricErrorDialog(e, "Chyba uložení", "Data nebylo možné uložit.", "Data nebylo možné uložit z důvodu jiné chyby:");
    }
}
