package cz.jbenak.npos.boClient.gui.dialogs.generic;

import cz.jbenak.npos.boClient.BoClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YesNoDialog extends Stage {

    public enum Choice {
        YES, NO
    }

    private static final Logger LOGGER = LogManager.getLogger(YesNoDialog.class);
    private Choice choice;

    public YesNoDialog(Window owner) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.initStyle(StageStyle.UTILITY);
        this.initOwner(owner);
    }

    protected void setChoice(Choice choice) {
        this.choice = choice;
    }

    protected void closeDialog() {
        LOGGER.debug("A YES/NO dialog will be closed with choice {}.", choice);
        this.close();
    }

    private void onDialogWindowClose(WindowEvent event) {
        event.consume();
        choice = Choice.NO;
        closeDialog();
    }

    public Choice showDialog(String title, String question) {
        LOGGER.debug("Will be shown a YES/NO choice dialog.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/jbenak/npos/boClient/gui/dialogs/generic/yes-no-dialog.fxml"));
            this.setScene(new Scene(loader.load()));
            YesNoDialogController controller = loader.getController();
            controller.setDialog(this);
            controller.setTitle(title);
            controller.setQuestion(question);
            this.centerOnScreen();
            this.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::onDialogWindowClose);
            this.showAndWait();
        } catch (Exception e) {
            LOGGER.error("During opening of the YES/NO dialog a serious error occurred:", e);
            InfoDialog dialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
            dialog.setDialogTitle("Nelze zobrazit dialog.");
            dialog.setDialogMessage("Nelze zobrazit požadovaný dialog pro zvolení voleb ANO/NE z důvodu závažné chyby.\n" +
                    "Následující text zašlete aministrátorovi: \n\n" + e);
            dialog.showDialog();
        }
        return choice;
    }
}
