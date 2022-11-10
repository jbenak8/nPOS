package cz.jbenak.npos.boClient.gui.panels.data;

import cz.jbenak.npos.api.client.CRUDResult;
import cz.jbenak.npos.api.data.Country;
import cz.jbenak.npos.api.data.Currency;
import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.api.DataOperations;
import cz.jbenak.npos.boClient.gui.dialogs.generic.EditDialog;
import cz.jbenak.npos.boClient.gui.dialogs.generic.InfoDialog;
import cz.jbenak.npos.boClient.gui.dialogs.generic.YesNoDialog;
import cz.jbenak.npos.boClient.gui.helpers.Utils;
import cz.jbenak.npos.boClient.gui.panels.AbstractPanelContentController;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class CountryController extends AbstractPanelContentController {

    private static final Logger LOGGER = LogManager.getLogger(CountryController.class);
    private ObservableList<Country> countryList = FXCollections.emptyObservableList();
    private List<Country> allCountries = new ArrayList<>();
    private List<Currency> allCurrencies = new ArrayList<>();
    private final MFXTableView<Country> table = new MFXTableView<>();

    @Override
    @SuppressWarnings("unchecked")
    public void loadData() {
        prepareTable();
        LOGGER.debug("Process of loading countries from server will be prepared.");
        Task<List<Country>> loadTask = new Task<>() {
            @Override
            protected List<Country> call() {
                DataOperations dataOperations = new DataOperations();
                return dataOperations.getAllCountries().join();
            }
        };
        loadTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Načítám seznam států...");
        });
        loadTask.setOnSucceeded(evt -> {
            LOGGER.info("Countries has been successfully loaded.");
            if (allCountries != null && !allCountries.isEmpty()) {
                allCountries.clear();
                countryList.clear();
            }
            allCountries = (List<Country>) evt.getSource().getValue();
            countryList = FXCollections.observableArrayList(allCountries);
            prepareTable();
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Načteno " + countryList.size() + " záznamů.");
        });
        loadTask.setOnFailed(evt -> {
            Throwable e = evt.getSource().getException();
            LOGGER.error("Countries list loading failed.", e);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při načítání seznamu států");
            Utils.showGenricErrorDialog(e, "Státy nelze načíst",
                    "Nastala chyba při načítání seznamu států.",
                    "Státy nebyly načteny z důvodu jiné chyby.");
            BoClient.getInstance().getMainController().showProgressIndicator(false);
        });
        BoClient.getInstance().getTaskExecutor().submit(loadTask);
    }

    @FXML
    private void btnNewPressed() {
        prepareEditDialog(false);
    }

    @FXML
    private void btnEditPressed() {
        if (table.getSelectionModel().getSelectedValues().size() == 1) {
            prepareEditDialog(true);
        }
    }

    @SuppressWarnings("unchecked")
    private void prepareEditDialog(boolean edit) {
        LOGGER.debug("Process of loading currencies for country list from server will be prepared.");
        Task<List<cz.jbenak.npos.api.data.Currency>> loadTask = new Task<>() {
            @Override
            protected List<cz.jbenak.npos.api.data.Currency> call() {
                DataOperations dataOperations = new DataOperations();
                return dataOperations.getAllCurrencies().join();
            }
        };
        loadTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Připravuji data pro editaci...");
        });
        loadTask.setOnSucceeded(evt -> {
            LOGGER.info("Currencies has been successfully loaded.");
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Editace státu");
            if (allCurrencies != null && !allCurrencies.isEmpty()) {
                allCurrencies.clear();
            }
            allCurrencies = (List<Currency>) evt.getSource().getValue();
            if (allCurrencies.isEmpty()) {
                InfoDialog warnDialog = new InfoDialog(InfoDialog.InfoDialogType.WARNING, BoClient.getInstance().getMainStage(), false);
                warnDialog.setDialogTitle("Měny nejsou k dispozici");
                warnDialog.setDialogMessage("Pro uložení státu prosím nejprve vytvořte měnu.");
                warnDialog.showDialog();
            } else {
                if (edit) {
                    EditDialog<Country, CountryEditingController> dialogNew = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/countries-edit-dialog.fxml", "Nový stát", this);
                    CountryEditingController controller = dialogNew.preloadDialog();
                    controller.setCurrencies(allCurrencies);
                    if (dialogNew.openEditDialog(table.getSelectionModel().getSelectedValues().get(0))) {
                        loadData();
                    }
                } else {
                    EditDialog<Country, CountryEditingController> dialogEdit = new EditDialog<>("/cz/jbenak/npos/boClient/gui/panels/data/countries-edit-dialog.fxml", "Úprava státu", this);
                    CountryEditingController controller = dialogEdit.preloadDialog();
                    controller.setCurrencies(allCurrencies);
                    if (dialogEdit.openNewDialog()) {
                        loadData();
                    }
                }
            }
        });
        loadTask.setOnFailed(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Příprava editace státu selhala");
            Utils.showGenricErrorDialog(evt.getSource().getException(),
                    "Chyba načtení seznamu měn", "Měny pro editaci/vložení státu nebyly načteny",
                    "Seznam měn nebylo možno načíst z důvodu jiné chyby:");
        });
        BoClient.getInstance().getTaskExecutor().submit(loadTask);
    }

    @FXML
    private void btnDeletePressed() {
        if (table.getSelectionModel().getSelectedValues().size() == 1) {
            Country selected = table.getSelectionModel().getSelectedValues().get(0);
            YesNoDialog question = new YesNoDialog(BoClient.getInstance().getMainStage());
            YesNoDialog.Choice answer = question.showDialog("Smazat stát?", "Přejete si opravdu smazat vybraný stát \"" + selected.getCommonName() + "\"?");
            if (answer == YesNoDialog.Choice.YES) {
                deleteCountry(selected.getIsoCode());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void deleteCountry(String isoCode) {
        LOGGER.info("Selected country with ISO code '{}' will be deleted.", isoCode);
        Task<CRUDResult> deleteTask = new Task<>() {
            @Override
            protected CRUDResult call() {
                DataOperations operations = new DataOperations();
                return operations.deleteCountry(isoCode).join();
            }
        };
        deleteTask.setOnRunning(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(true);
            BoClient.getInstance().getMainController().setSystemStatus("Mažu vybraný stát...");
        });
        deleteTask.setOnSucceeded(evt -> {
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            CRUDResult result = (CRUDResult) evt.getSource().getValue();
            if (result.getResultType() == CRUDResult.ResultType.OK) {
                LOGGER.info("Country'{}' has been deleted successfully.", isoCode);
                BoClient.getInstance().getMainController().setSystemStatus("Měna smazána");
            }
            if (result.getResultType() == CRUDResult.ResultType.HAS_BOUND_RECORDS) {
                LOGGER.error("Country {} could not be deleted because it has bound records.", isoCode);
                BoClient.getInstance().getMainController().setSystemStatus("Vybraný stát nebyl smazán");
                InfoDialog warnDialog = new InfoDialog(InfoDialog.InfoDialogType.WARNING, BoClient.getInstance().getMainStage(), false);
                warnDialog.setDialogTitle("Stát nelze smazat");
                warnDialog.setDialogMessage("Na vybraný stát \"" + isoCode + "\" odkazují jiné záznamy v databázi, tedy tento stát je používán.");
                warnDialog.showDialog();
            }
            if (result.getResultType() == CRUDResult.ResultType.GENERAL_ERROR) {
                LOGGER.error("There was a general error during deletion of selected country data: {}", evt.getSource().getMessage());
                BoClient.getInstance().getMainController().setSystemStatus("Stát nebyl smazán");
                InfoDialog errorDialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
                errorDialog.setDialogTitle("Chyba při mazání státu " + isoCode);
                errorDialog.setDialogMessage(result.getMessage());
                errorDialog.showDialog();
            }
            loadData();
        });
        deleteTask.setOnFailed(evt -> {
            LOGGER.error("There was an error during deletion of selected country data.", evt.getSource().getException());
            BoClient.getInstance().getMainController().showProgressIndicator(false);
            BoClient.getInstance().getMainController().setSystemStatus("Chyba při mazání státu.");
            Utils.showGenricErrorDialog(evt.getSource().getException(),
                    "Chyba mazání", "Vybraný stát nebylo možno smazat.",
                    "Vybraný stát nebylo možné smazat z důvodu jiné chyby:");
        });
        BoClient.getInstance().getTaskExecutor().submit(deleteTask);
    }

    private void prepareTable() {
        ObservableList<MFXTableColumn<Country>> columns = table.getTableColumns();

        if (columns.isEmpty()) {
            MFXTableColumn<Country> isoCodeColumn = new MFXTableColumn<>("ISO kód", true, Comparator.comparing(Country::getIsoCode));
            MFXTableColumn<Country> commonNameColumn = new MFXTableColumn<>("Běžný název", true, Comparator.comparing(Country::getCommonName));
            MFXTableColumn<Country> fullNameColumn = new MFXTableColumn<>("Úplný název", true, Comparator.comparing(Country::getFullName));
            MFXTableColumn<Country> currencyColumn = new MFXTableColumn<>("Měna", true, Comparator.comparing(Country::getIsoCode));
            MFXTableColumn<Country> isMainColumn = new MFXTableColumn<>("Je hlavní", true, Comparator.comparing(Country::isMain));

            isoCodeColumn.setRowCellFactory(country -> new MFXTableRowCell<>(Country::getIsoCode));
            currencyColumn.setRowCellFactory(row -> new MFXTableRowCell<>(Country::getCurrencyIsoCode) {{
                setAlignment(Pos.CENTER);
            }});
            isMainColumn.setRowCellFactory(row -> new MFXTableRowCell<>(country -> country.isMain() ? "Ano" : "Ne") {{
                setAlignment(Pos.CENTER);
            }});
            commonNameColumn.setRowCellFactory(country -> new MFXTableRowCell<>(Country::getCommonName));
            fullNameColumn.setRowCellFactory(country -> new MFXTableRowCell<>(Country::getFullName));

            isoCodeColumn.setMinWidth(160);
            fullNameColumn.setMinWidth(300);
            commonNameColumn.setMinWidth(200);
            isMainColumn.setMinWidth(70);
            currencyColumn.setMinWidth(70);

            table.autosizeColumnsOnInitialization();
            columns.add(isoCodeColumn);
            columns.add(commonNameColumn);
            columns.add(fullNameColumn);
            columns.add(currencyColumn);
            columns.add(isMainColumn);

            table.setFooterVisible(false);
            table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            table.getStyleClass().add("content-panel");
            table.getSelectionModel().setAllowsMultipleSelection(false);
            mainPane.setCenter(table);
        }
        table.setItems(countryList);
    }
}
