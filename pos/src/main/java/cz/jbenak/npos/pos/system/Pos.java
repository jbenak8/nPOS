package cz.jbenak.npos.pos.system;

import cz.jbenak.npos.pos.gui.prihlaseni.LoginDialog;
import cz.jbenak.npos.pos.objekty.doklad.TypDokladu;
import cz.jbenak.npos.pos.objekty.filialka.Filialka;
import cz.jbenak.npos.pos.objekty.meny.Mena;
import cz.jbenak.npos.pos.procesory.KomunikacniManazer;
import cz.jbenak.npos.pos.procesory.ManazerReplikace;
import cz.jbenak.npos.pos.procesory.TiskovyProcesor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-01-22
 */
public class Pos extends Application {

    public static final String VERZE = "1.0.0.0";
    private static final Logger LOGER = LogManager.getLogger(Pos.class);
    private static volatile Pos instance;
    private final Map<TypDokladu, Integer> inicialniPoradovaCisla = new HashMap<>();
    private Stage aplikacniOkno;
    private Connection spojeni;
    private Properties nastaveni;
    private KomunikacniManazer manazerKomunikace;
    private ManazerReplikace manazerReplikace;
    private TiskovyProcesor tiskovyProcesor;
    private Filialka filialka;
    private Mena hlavniMena;
    private int cisloPokladny;

    public Pos() {
        super();
        synchronized (Pos.class) {
            if (instance != null) {
                throw new UnsupportedOperationException("Nepovolená operace - hlavní třída aplikace " + Pos.class.getCanonicalName() + " je inicializována vícekrát.");
            }
            instance = this;
        }
    }

    public static Pos getInstance() {
        return instance;
    }

    public Stage getAplikacniOkno() {
        return aplikacniOkno;
    }

    public ManazerReplikace getManazerReplikace() {
        return manazerReplikace;
    }

    public Connection getSpojeni() {
        return spojeni;
    }

    protected void setSpojeni(Connection spojeni) {
        this.spojeni = spojeni;
    }

    public Properties getNastaveni() {
        return nastaveni;
    }

    public int getCisloPokladny() {
        return cisloPokladny;
    }

    public void setCisloPokladny(int cisloPokladny) {
        this.cisloPokladny = cisloPokladny;
    }

    protected void setNastaveni(Properties nastaveni) {
        this.nastaveni = nastaveni;
    }

    public KomunikacniManazer getManazerKomunikace() {
        return manazerKomunikace;
    }

    public void setManazerKomunikace(KomunikacniManazer manazerKomunikace) {
        this.manazerKomunikace = manazerKomunikace;
    }

    public TiskovyProcesor getTiskovyProcesor() {
        return tiskovyProcesor;
    }

    public void setTiskovyProcesor(TiskovyProcesor tiskovyProcesor) {
        this.tiskovyProcesor = tiskovyProcesor;
    }

    public Filialka getFilialka() {
        return filialka;
    }

    public void setFilialka(Filialka filialka) {
        this.filialka = filialka;
    }

    public Mena getHlavniMena() {
        return hlavniMena;
    }

    public void setHlavniMena(Mena hlavniMena) {
        this.hlavniMena = hlavniMena;
    }

    public Map<TypDokladu, Integer> getInicialniPoradovaCisla() {
        return inicialniPoradovaCisla;
    }

    protected void startLogin() {
        LOGER.info("Nyní bude proveden start funkce pro přihlášení po startu systému.");
        new LoginDialog().zobrazDialog();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        aplikacniOkno = primaryStage;
        LOGER.info("Aplikace nPOS verze {} právě startuje.", VERZE);
        //aplikacniOkno.initStyle(StageStyle.UNDECORATED);
        aplikacniOkno.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cz/jbenak/npos/pos/gui/img/ikona.png"))));
        aplikacniOkno.setTitle("nPOS verze " + VERZE);
        aplikacniOkno.setResizable(false);
        // prevence zavření - jako DO_NOTHING_ON_CLOSE
        aplikacniOkno.setOnCloseRequest(Event::consume);
        try {
            LOGER.info("Připravuje se zobrazení okna procesu startu.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/jbenak/npos/pos/gui/start/stav/Stav.fxml"));
            aplikacniOkno.initStyle(StageStyle.TRANSPARENT);
            aplikacniOkno.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            aplikacniOkno.show();
            aplikacniOkno.centerOnScreen();
            new Starter(loader.getController()).run();
        } catch (IOException e) {
            LOGER.fatal("Nastal problém při inicializaci aplikace. Aplikace bude ukončena.", e);
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        LOGER.info("Ukončovací sekvence aplikace nPOS verze {} byla spuštěna. Program se ukončí.", VERZE);
        if (this.nastaveni != null) {
            LOGER.info("Načtená systémová nastavení se mažou.");
            nastaveni = null;
        }
        if (this.spojeni != null && !this.spojeni.isClosed()) {
            LOGER.info("Databázové spojení bude uzavřeno.");
            this.spojeni.close();
        }
        LOGER.info("Aplikace nPOS verze {} byla ukončena.", VERZE);
    }

    public static void main(String args[]) {
        LOGER.info("Aplikace nPOS verze {} právě startuje. Start proveden z metody main.", VERZE);
        Pos.launch(args);
    }
}
