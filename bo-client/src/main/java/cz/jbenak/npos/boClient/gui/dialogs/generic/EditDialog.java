package cz.jbenak.npos.boClient.gui.dialogs.generic;

import cz.jbenak.npos.boClient.BoClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * A generic definition of dialog, which creates or edits some data.
 * This parent class will provide necessary functions for initializing the dialog ad closing it including save check.
 *
 * @param <T> a type of data, which are used to be edited or added.
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2022-10-02
 */
public class EditDialog<T, C extends EditDialogController<T>> extends Stage {

    private final static Logger LOGGER = LogManager.getLogger(EditDialog.class);
    private final String fxml;
    private boolean cancelled;
    private boolean edited;
    private boolean saved;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    /**
     * Create a new dialog class.
     *
     * @param title title to be shown in the header
     */
    public EditDialog(String fxml, String title) {
        this.fxml = fxml;
        this.initModality(Modality.APPLICATION_MODAL);
        this.initStyle(StageStyle.DECORATED);
        this.initOwner(BoClient.getInstance().getMainStage());
        this.setTitle(title);
    }

    /**
     * Show a new entry dialog. It preloads a dialog.
     *
     * @return controller, which can be used to pass any additional data (i.e. combobox values, etc.)
     */
    public C preloadNewDialog() {
        LOGGER.info("A dialog for enter a new value will be shown.");
        this.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cz/jbenak/npos/boClient/gui/img/edit-dialog-new.png"))));
        return preloadDialog(null);
    }

    /**
     * Show an existing entry edition.
     *
     * @param data data, which have to be edited.
     * @return controller, which can be used to pass any additional data (i.e. combobox values, etc.)
     */
    public C preloadEditDialog(T data) {
        LOGGER.info("A dialog for edit  a value of type {} will be shown.", data.getClass().getName());
        this.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cz/jbenak/npos/boClient/gui/img/edit-dialog-edit.png"))));
        return preloadDialog(data);
    }

    /**
     * Opens already preloaded dialog.
     *
     * @return if the data in source content have to be reloaded.
     */
    public boolean openDialog() {
        this.centerOnScreen();
        this.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::onEditDialogWindowClose);
        this.showAndWait();
        return cancelled;
    }

    private C preloadDialog(T data) {
        C controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            this.setScene(new Scene(loader.load()));
            controller = loader.getController();
            controller.setDialog(this);
            if (data != null) {
                controller.setDataEdited(data);
            }
        } catch (Exception e) {
            LOGGER.error("During preloading of the edit dialog a serious error occurred:", e);
            InfoDialog dialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
            dialog.setDialogTitle("Nelze zobrazit dialog.");
            dialog.setDialogMessage("Nelze zobrazit požadovaný dialog pro úpravu nebo zadání nové položky z důvodu závažné chyby.\n" +
                    "Následující text zašlete aministrátorovi: \n\n" + e);
            dialog.showDialog();
        }
        return controller;
    }

    private void onEditDialogWindowClose(WindowEvent event) {
        event.consume();
        closeDialog();
    }

    /**
     * Used for closing of the dialog. When edited and non-saved data occurs during closing, question dialog will be shown
     */
    public void closeDialog() {
        if (edited && (cancelled || !saved)) {
            LOGGER.info("Close of edit/new dialog requested. Data has been edited but not saved. A question dialog to really close will be shown.");
        } else {
            LOGGER.info("Edit/new dialog will be closed.");
            this.close();
        }
    }
}
