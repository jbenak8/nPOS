package cz.jbenak.npos.boClient.gui.dialogs.generic;

import javafx.fxml.Initializable;

public abstract class EditDialogController<T> implements Initializable {

    private EditDialog dialog;
    private T dataEdited = null;

    public void setDialog(EditDialog dialog) {
        this.dialog = dialog;
    }

    public void setDataEdited(T dataEdited) {
        this.dataEdited = dataEdited;
    }

    public abstract void save(T data);
}
