package cz.jbenak.npos.pos.system;

import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.gui.start.stav.StavovyKontroler;
import cz.jbenak.npos.pos.objekty.adresy.Stat;
import cz.jbenak.npos.pos.objekty.filialka.Filialka;
import cz.jbenak.npos.pos.objekty.meny.Mena;
import cz.jbenak.npos.pos.procesory.KomunikacniManazer;
import cz.jbenak.npos.pos.procesory.ManazerZarizeni;
import cz.jbenak.npos.pos.procesy.platba.NacteniDenominaci;
import cz.jbenak.npos.pos.procesy.platba.NacteniPravidelZaokrouhlovani;
import cz.jbenak.npos.pos.system.util.Sifrovani;
import cz.jbenak.npos.pos.system.util.SifrovaniException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Starter extends Task<Void> {

    public static final String CESTA_NASTAVENI = "nastaveni/pos.properties";
    private final static Logger LOGER = LogManager.getLogger(Starter.class);
    private final StavovyKontroler kontroler;
    private Properties nastaveni;
    private Connection spojeni;
    private boolean inicializaceOK = false;

    public Starter(StavovyKontroler kontroler) {
        this.kontroler = kontroler;
    }

    @Override
    protected Void call() throws Exception {
        LOGER.info("Proces inicializace aplikace byl spuštěn.");
        if (nactiNastaveni()) {
            if (nactiDBSPojeni()) {
                startKomunikaciSBO();
                zkontrolujDatabazi();
                inicializujZarizeni();
                if (nactiDataFilialky() && nactiHlavniMenu() && nactiDataHlavniMeny()) {
                    inicializaceOK = true;
                }
            }
        }
        return null;
    }

    @Override
    protected void succeeded() {
        if (inicializaceOK) {
            LOGER.info("Spouštění systému bylo úspěšné. Systém bude pokračovat do přihlašovacího okna.");
            Pos.getInstance().startLogin();
        }
    }

    @Override
    protected void failed() {
        LOGER.error("Spouštění systému bylo neúspěšné. Program se ukončí.", this.getException());
        Platform.exit();
    }

    private boolean nactiNastaveni() {
        LOGER.info("Načítá se systémové nastavení.");
        kontroler.setPopisStavu("Načítám systémová nastavení...");
        this.nastaveni = new Properties();
        try (FileInputStream fis = new FileInputStream(CESTA_NASTAVENI)) {
            this.nastaveni.load(fis);
            if (!this.nastaveni.isEmpty()) {
                LOGER.info("Systémová nastavení byla načtena.");
                kontroler.setPopisStavu("Systémová nastavení načtena.");
                kontroler.setStav(0.1);
                Pos.getInstance().setNastaveni(this.nastaveni);
                Pos.getInstance().setCisloPokladny(Integer.parseInt(nastaveni.getProperty("pos.cislo")));
                return true;
            } else {
                LOGER.error("Systémové nastavení je prázdné.");
                kontroler.setPopisStavu("Nastavení je prázdné!");
                new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Problém s nastavením", "Soubor s nastavením systému je prázdný. Systém nemůže pokračovat.", Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
                failed();
                return false;
            }
        } catch (IOException e) {
            LOGER.error("Nastala chyba při načítání souboru s nastavením systému: ", e);
            kontroler.setPopisStavu("Načítám systémová nastavení - krok selhal!");
            this.nastaveni = null;
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Problém s nastavením", "Nebylo možné načíst systémová nastavení: \n\n" + e.getLocalizedMessage() + "\n\nProgram se ukončí.", Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            failed();
            return false;
        }
    }

    private boolean nactiDBSPojeni() {
        LOGER.info("Nyní bude načteno spojení do databáze.");
        kontroler.setPopisStavu("Načítám připojení do databáze...");
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            final String url = "jdbc:postgresql://" + this.nastaveni.getProperty("db.server.adresa").trim() + ":"
                    + this.nastaveni.getProperty("db.server.port").trim() + "/"
                    + this.nastaveni.getProperty("db.schema").trim();
            final Properties connectionProps = new Properties();
            connectionProps.put("user", Sifrovani.decodeString(this.nastaveni.getProperty("db.uzivatel").trim()));
            connectionProps.put("password", Sifrovani.decodeString(this.nastaveni.getProperty("db.heslo").trim()));
            connectionProps.put("currentSchema", this.nastaveni.getProperty("db.schema").trim());
            this.spojeni = DriverManager.getConnection(url, connectionProps);
            this.spojeni.setAutoCommit(true); // transakce budou řešeny individuálně
            this.spojeni.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            if (spojeni.isValid(1000)) {
                LOGER.info("Pripojení do databáze bylo načteno.");
                kontroler.setPopisStavu("Databáze pokladny je připojena.");
                kontroler.setStav(0.2);
                Pos.getInstance().setSpojeni(spojeni);
                return true;
            } else {
                LOGER.error("Spojení do databáze není platné.");
                kontroler.setPopisStavu("Načítám připojení do databáze - spojení není platné!");
                new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Problém s databází", "Připojení do databáze není platné.\n\nProgram se ukončí.", Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
                failed();
                return false;
            }
        } catch (SQLException | SifrovaniException | NullPointerException e) {
            this.setException(e);
            LOGER.error("Nastal problém při navazování spojení do databáze: ", e);
            kontroler.setPopisStavu("Načítám připojení do databáze - krok selhal!");
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Problém s databází", "Nebylo možné se připojit do databáze:\n\n" + e.getLocalizedMessage() + "\n\nProgram se ukončí.", Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            this.spojeni = null;
            failed();
            return false;
        }
    }

    private void startKomunikaciSBO() {
        LOGER.info("Nyním bude inicilaizována komunikace s BO.");
        kontroler.setPopisStavu("Inicializuji komunikaci s BO...");
        KomunikacniManazer manazer = new KomunikacniManazer();
        Pos.getInstance().setManazerKomunikace(manazer);
        LOGER.info("Proces komunikace s BO byl inicializován.");
        kontroler.setPopisStavu("Proces komunikace s BO byl inicializován.");
        kontroler.setStav(0.3);
    }

    private void zkontrolujDatabazi() {

    }

    private void inicializujZarizeni() {
        LOGER.info("Budou načtena a inicializována pokladní zařízení.");
        kontroler.setPopisStavu("Inicializuji zařízení...");
        ManazerZarizeni.getInstance().inicializujZarizeni();
    }

    private boolean nactiDataFilialky() {
        LOGER.info("Budou načtena data filiálky (obchodního místa).");
        kontroler.setPopisStavu("Načítám data filiálky...");
        boolean nacteniOK = false;
        Filialka filialka = null;
        try {
            final String dotaz = "SELECT filialka.*, staty.* FROM " + spojeni.getCatalog() + ".filialka "
                    + "INNER JOIN " + spojeni.getCatalog() + ".staty ON filialka.stat = staty.iso_kod;";
            try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                ResultSet rs = psmt.executeQuery();
                if (rs != null) {
                    if (rs.first()) {
                        filialka = new Filialka();
                        filialka.setOznaceni(rs.getString("oznaceni"));
                        filialka.setNazev(rs.getString("nazev"));
                        filialka.setPopis(rs.getString("popis"));
                        filialka.setCp_cor(rs.getString("cp_cor"));
                        filialka.setUlice(rs.getString("ulice"));
                        filialka.setPsc(rs.getString("psc"));
                        filialka.setMesto(rs.getString("mesto"));
                        filialka.setTelefon(rs.getString("telefon"));
                        filialka.setMobil(rs.getString("mobil"));
                        filialka.setEmail(rs.getString("email"));
                        filialka.setOdpovednyVedouci(rs.getString("odpovedny_vedouci"));
                        filialka.setIc(rs.getString("ic"));
                        filialka.setDic(rs.getString("dic"));
                        filialka.setDanovyKodDph(rs.getString("danovy_kod_dph"));
                        filialka.setNazevSpolecnosti(rs.getString("nazev_spolecnosti"));
                        filialka.setAdresaSpolecnosti(rs.getString("adresa_spolecnosti"));
                        filialka.setZapisOr(rs.getString("zapis_or"));
                        filialka.setUrl(rs.getString("url"));
                        filialka.setEetRezim(rs.getInt("eet_rezim"));
                        filialka.setEetIdProvozovny(rs.getInt("eet_id_provozovny"));
                        filialka.setEetPoverujiciDic(rs.getString("eet_poverujici_dic"));
                        Stat stat = new Stat();
                        stat.setIsoKod(rs.getString("iso_kod"));
                        stat.setBeznyNazev(rs.getString("bezny_nazev"));
                        stat.setUplnyNazev(rs.getString("uplny_nazev"));
                        stat.setHlavni(rs.getBoolean("hlavni"));
                        stat.setKodMeny(rs.getString("mena"));
                        filialka.setStat(stat);
                        Pos.getInstance().setFilialka(filialka);
                        nacteniOK = true;
                        kontroler.setPopisStavu("Data filiálky byla ússpěšně načtena.");
                        kontroler.setStav(0.9);
                    }
                }
            }
            if (filialka == null) {
                LOGER.error("Nepodařilo se načíst data filiálky. Data v databázi neexistují!");
                new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Filiálka nebyla načtena", "Nepodařilo se načíst data obchodu! Data v databázi neexistují!\n\nProgram se ukončí.",
                        Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
                kontroler.setPopisStavu("Načítám data filiálky - krok selhal!");
                failed();
            }
        } catch (SQLException e) {
            this.setException(e);
            LOGER.error("Nastal problém při získávání dat filiálky: ", e);
            kontroler.setPopisStavu("Načítám data filiálky - krok selhal!");
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Problém s databází", "Nebylo možné načíst data filiálky:\n\n" + e.getLocalizedMessage() + "\n\nProgram se ukončí.", Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            failed();
            return false;
        }
        return nacteniOK;
    }

    private boolean nactiHlavniMenu() {
        LOGER.info("Bude načtena hlavní měna.");
        kontroler.setPopisStavu("Načítám hlavní měnu...");
        Mena hlMena = null;
        try {
            final String dotaz = "SELECT * FROM " + spojeni.getCatalog() + ".mena WHERE kmenova = true;";
            try (PreparedStatement psmt = spojeni.prepareStatement(dotaz, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                ResultSet rs = psmt.executeQuery();
                if (rs != null) {
                    if (rs.first()) {
                        hlMena = new Mena();
                        hlMena.setKmenova(true);
                        hlMena.setAkceptovatelna(rs.getBoolean("akceptovatelna"));
                        hlMena.setIsoKod(rs.getString("iso_kod"));
                        hlMena.setNarodniSymbol(rs.getString("narodni_symbol"));
                        hlMena.setOznaceni(rs.getString("oznaceni"));
                        Pos.getInstance().setHlavniMena(hlMena);
                        kontroler.setStav(1);
                        kontroler.setPopisStavu("Hlavní měna byla úspěšně načtena. Systém je plně spuštěn.");
                    }
                }
            }
            if (hlMena == null) {
                LOGER.error("Nepodařilo se načíst hlavní měnu. Data v databázi neexistují!");
                kontroler.setPopisStavu("Načítám hlavní měnu - krok selhal!");
                new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Hlavní měna nebyla načtena", "Nepodařilo se načíst hlavní měnu! Data v databázi neexistují!\n\nProgram se ukončí.",
                        Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
                return false;
            } else if (!hlMena.isAkceptovatelna()) {
                LOGER.error("Hlavní měna není akceptovatelná!");
                kontroler.setPopisStavu("Načítám hlavní měnu - krok selhal!");
                new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Hlavní měna neakceptovatelná", "Hlavní měna {} - {} není nastavena jako akceptovatelná!\n\nProgram se ukončí.",
                        new Object[]{hlMena.getIsoKod(), hlMena.getOznaceni()}, Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
                return false;
            }
        } catch (SQLException e) {
            this.setException(e);
            LOGER.error("Nastal problém při získávání dat hlavní měny: ", e);
            kontroler.setPopisStavu("Načítám hlavní měnu - krok selhal!");
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Problém s databází", "Nebylo možné načíst data hlavní měny:\n\n" + e.getLocalizedMessage() + "\n\nProgram se ukončí.", Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            failed();
            return false;
        }
        return true;
    }

    private boolean nactiDataHlavniMeny() {
        LOGER.info("Budou načteny výčtové tabulky a pravidla zaokrouhlování pro vybranou kmenovou měnu.");
        try {
            List<Mena> meny = new ArrayList<>();
            meny.add(Pos.getInstance().getHlavniMena());
            NacteniDenominaci nacteniDenominaci = new NacteniDenominaci(meny);
            NacteniPravidelZaokrouhlovani nacteniPravidelZaokrouhlovani = new NacteniPravidelZaokrouhlovani(meny);
            nacteniDenominaci.run();
            nacteniPravidelZaokrouhlovani.run();
            Pos.getInstance().setHlavniMena(meny.get(0));
        } catch (Exception e) {
            LOGER.error("Nastala vnitřní chyba v procesu načítání denominací pro vybrané měny.", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Nemohu připravit platbu",
                    "Nastala chyba v procesu načítání denominací pro vybrané měny:\n\n" + e.getLocalizedMessage(),
                    Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
            return false;
        }
        return true;
    }
}
