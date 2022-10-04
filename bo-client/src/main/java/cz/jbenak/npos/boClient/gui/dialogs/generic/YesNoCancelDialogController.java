package cz.jbenak.npos.boClient.gui.dialogs.generic;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class YesNoCancelDialogController implements Initializable {

    private YesNoCancelDialog dialog;
    @FXML
    private Label question;
    @FXML
    private Label title;

    protected void setDialog(YesNoCancelDialog dialog) {
        this.dialog = dialog;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    protected void setQuestion(String question) {
        this.question.setText(question);
    }

    @FXML
    private void btnOkPressed() {
        dialog.setChoice(YesNoCancelDialog.Choice.YES);
        dialog.closeDialog();
    }

    @FXML
    private void btnNoPressed() {
        dialog.setChoice(YesNoCancelDialog.Choice.NO);
        dialog.closeDialog();
    }

    @FXML
    private void btnCancelPressed() {
        dialog.setChoice(YesNoCancelDialog.Choice.CANCEL);
        dialog.closeDialog();
    }

    @FXML
    private void keyPressed(KeyEvent event) {
        if ((event.getCode() == KeyCode.A) ||(event.getCode() == KeyCode.Y) || (event.getCode() == KeyCode.ENTER)) {
            btnOkPressed();
        }
        if (event.getCode() == KeyCode.N) {
            btnNoPressed();
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            btnCancelPressed();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
