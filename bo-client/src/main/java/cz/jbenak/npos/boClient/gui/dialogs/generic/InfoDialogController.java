package cz.jbenak.npos.boClient.gui.dialogs.generic;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoDialogController implements Initializable {

    @FXML
    private FontIcon titleIcon;
    @FXML
    private Label dialogTitle;
    @FXML
    private Label dialogSubtitle;
    @FXML
    private TextArea dialogMessage;
    @FXML
    private MFXButton btnOK;
    private InfoDialog dialog;
    private final Clipboard clipboard = Clipboard.getSystemClipboard();

    protected void setDialog(InfoDialog dialog) {
        this.dialog = dialog;
        btnOK.requestFocus();
    }

    protected void setInfoIcon() {
        titleIcon.setIconLiteral("fth-info");
        titleIcon.setIconColor(Color.web("#2196f3"));
    }

    protected void setWarnIcon() {
        titleIcon.setIconLiteral("fth-alert-triangle");
        titleIcon.setIconColor(Color.web("#ffd200"));
    }

    protected void setErrorIcon() {
        titleIcon.setIconLiteral("fth-x-circle");
        titleIcon.setIconColor(Color.web("#c30000"));
    }

    protected void setOfflineIcon() {
        titleIcon.setIconLiteral("fltrmz-plug-disconnected-24");
        titleIcon.setIconColor(Color.web("#c30000"));
    }

    protected void setOKIcon() {
        titleIcon.setIconLiteral("fth-check-circle");
        titleIcon.setIconColor(Color.web("#008a1a"));
    }

    protected void setTitle(String title) {
        dialogTitle.setText(title);
    }

    protected void setSubtitle(String subtitle) {
        if (subtitle != null && !subtitle.isEmpty()) {
            dialogSubtitle.setText(subtitle);
            dialogSubtitle.setVisible(true);
        }
    }

    protected void setMessage(String message) {
        dialogMessage.setText(message);
    }

    public void btnOkPressed() {
        dialog.closeDialog();
    }

    public void btnCopyPressed() {
        String subtitle = dialogSubtitle.getText() == null ? "" : dialogSubtitle.getText() + "\n\n";
        String text = dialogTitle.getText() + "\n\n" + subtitle + dialogMessage.getText();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }

    public void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER) {
            dialog.closeDialog();
        }
        if (event.isControlDown() && event.getCode() == KeyCode.C) {
            btnCopyPressed();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dialogSubtitle.setVisible(false);
    }
}
