package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.Country;
import cz.jbenak.npos.api.data.Currency;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialogController;
import cz.jbenak.npos.boClient.gui.helpers.Helpers;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

public class CountryEditingController extends EditDialogController<Country> {

    @FXML
    private Label validationLabel;
    @FXML
    private MFXTextField fieldIsoCode;
    @FXML
    private MFXTextField fieldCommonName;
    @FXML
    private MFXTextField fieldFullName;
    @FXML
    private MFXToggleButton btnIsMain;
    @FXML
    private MFXFilterComboBox<Currency> currencySelector;
    private ObservableList<Currency> allCurrencies = FXCollections.emptyObservableList();
    private static final Logger LOGGER = LogManager.getLogger(CountryEditingController.class);

    public void setCurrencies(List<Currency> currencies) {
        allCurrencies = FXCollections.observableArrayList(currencies);
        currencySelector.setItems(allCurrencies);
    }
    @Override
    public void setDataEdited(Country dataEdited) {
        this.dataEdited = dataEdited;
        title.setText("Úprava státu " + dataEdited.getCommonName());
        fieldIsoCode.setText(dataEdited.getIsoCode());
        fieldIsoCode.setDisable(true);
        fieldCommonName.setText(dataEdited.getCommonName());
        fieldFullName.setText(dataEdited.getFullName());
        btnIsMain.setSelected(dataEdited.isMain());
        Optional<Currency> selectedCurrency = allCurrencies.stream().filter(currency -> currency.getIsoCode().equalsIgnoreCase(dataEdited.getCurrencyIsoCode())).findFirst();
        selectedCurrency.ifPresent(currency -> currencySelector.selectItem(currency));
    }

    private boolean validateFields() {
        if (!fieldCommonName.getValidator().isValid()) {
            return false;
        }
        if (!currencySelector.isValid()) {
            return false;
        }
        if (!fieldFullName.getValidator().isValid()) {
            return false;
        }
        return fieldIsoCode.getValidator().isValid();
    }

    @Override
    protected void save() {
        LOGGER.info("Country {} will be saved.", dataEdited);
        Task<CRUDResult> saveUnitTask = new Task<>() {
            @Override
            protected CRUDResult call() {
                DataOperations operations = new DataOperations();
                return operations.storeCountry(dataEdited).join();
            }
        };
        saveUnitTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Ukládám stát...");
        });
        saveUnitTask.setOnSucceeded(evt -> {
            CRUDResult result = (CRUDResult) evt.getSource().getValue();
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            if (result.getResultType() == CRUDResult.ResultType.OK) {
                LOGGER.info("Country data has been saved.");
                BoClient.getInstance().getMainController().setSystemStatus("Stát uložen");
                dialog.setSaved(true);
                dialog.setEdited(false);
                dialog.setCancelled(false);
                dialog.closeDialog();
            }
            else {
                BoClient.getInstance().getMainController().setSystemStatus("Chyba při ukládání státu.");
                showCRUDresultErrorDialog(result.getMessage());
            }
        });
        saveUnitTask.setOnFailed(evt -> {
            LOGGER.error("There was an error during saving of country data.", evt.getSource().getException());
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při ukládání státu.");
            showSaveErrorDialog(evt.getSource().getException());
        });
        BoClient.getInstance().getTaskExecutor().submit(saveUnitTask);
    }

    @Override
    protected void savePressed() {
        if (validateFields()) {
            dataEdited = new Country();
            dataEdited.setIsoCode(fieldIsoCode.getText().trim());
            dataEdited.setCommonName(fieldCommonName.getText().trim());
            dataEdited.setFullName(fieldFullName.getText().trim());
            dataEdited.setMain(btnIsMain.isSelected());
            if (currencySelector.getSelectedIndex() >= 0) {
                dataEdited.setCurrencyIsoCode(currencySelector.getSelectedItem().getIsoCode());
            }
            save();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title.setText("Nový stát");
        StringConverter<Currency> converter = FunctionalStringConverter.to(currency -> currency == null ? "" : currency.getName() + " (" + currency.getIsoCode() + ")");
        Function<String, Predicate<Currency>> filterFunction = s -> currency -> StringUtils.containsIgnoreCase(converter.toString(currency), s);
        currencySelector.setConverter(converter);
        currencySelector.setFilterFunction(filterFunction);
        fieldIsoCode.setTextLimit(3);
        Helpers.getEmptyTextConstraint(fieldIsoCode, false, "Vyplňte ISO kód země.", validationLabel);
        fieldIsoCode.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (dataEdited.getIsoCode().compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        fieldCommonName.setTextLimit(45);
        Helpers.getEmptyTextConstraint(fieldCommonName, false, "Vyplňte běžný název země.", validationLabel);
        fieldCommonName.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (dataEdited.getCommonName().compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        fieldFullName.setTextLimit(150);
        Helpers.getEmptyTextConstraint(fieldFullName, false, "Vyplňte úplný název země.", validationLabel);
        fieldFullName.textProperty().addListener((observable, oldVal, newVal) -> {
            if (dataEdited == null || (dataEdited.getFullName().compareTo(newVal) != 0)) {
                dialog.setEdited(true);
            }
        });
        Helpers.getNoItemSelectedConstraint(currencySelector,  "Vyberte měnu používanou v zemi.", validationLabel);
        currencySelector.selectedItemProperty().addListener((observable, oldVal, newVal) -> {
            String currencyIsoCode = "";
            if (dataEdited != null) {
                currencyIsoCode = dataEdited.getCurrencyIsoCode() == null ? "" : dataEdited.getCurrencyIsoCode();
            }
            if (dataEdited == null || (currencyIsoCode.compareTo(newVal.getIsoCode()) != 0)) {
                dialog.setEdited(true);
            }
        });
    }
}
