package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.data.Country;
import cz.jbenak.npos.api.data.Currency;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialogController;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.controls.cell.MFXComboBoxCell;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.materialfx.i18n.Language;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.Locale;
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

    public void setCurrencies(List<Currency> currencies) {
        allCurrencies = FXCollections.observableArrayList(currencies);
        currencySelector.setItems(allCurrencies);
    }
    @Override
    public void setDataEdited(Country dataEdited) {

    }

    @Override
    protected void save() {

    }

    @Override
    protected void savePressed() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StringConverter<Currency> converter = FunctionalStringConverter.to(currency -> currency == null ? "" : currency.getName() + " (" + currency.getIsoCode() + ")");
        Function<String, Predicate<Currency>> filterFunction = s -> currency -> StringUtils.containsIgnoreCase(converter.toString(currency), s);
        currencySelector.setConverter(converter);
        currencySelector.setFilterFunction(filterFunction);
    }
}
