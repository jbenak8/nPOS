package cz.jbenak.npos.pos.gui.sdilene.zadavaciDialog;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.gui.registrace.hlavni.HlavniOkno;
import cz.jbenak.npos.pos.gui.sdilene.dialogy.zprava.DialogZpravy;
import cz.jbenak.npos.pos.objekty.slevy.Sleva;
import cz.jbenak.npos.pos.objekty.sortiment.Sortiment;
import cz.jbenak.npos.pos.procesory.DokladProcesor;
import cz.jbenak.npos.pos.procesory.ManazerSlev;
import cz.jbenak.npos.pos.system.Pos;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Třída pro zobrazení dialogu pro zadání jedné číselné hodnoty.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-02-26
 */
public class ZadavaciDialog extends Stage {

    public enum Typ {
        MNOZSTVI, CENA, SLEVA_PROCENTNI, SLEVA_HODNOTA
    }

    private final static Logger LOGER = LogManager.getLogger(ZadavaciDialog.class);
    private final Typ typ;
    private final Sortiment polozka;
    private boolean predRegistraci = false;
    private ZadavaciDialogKontroler kontroler;
    private boolean zadaniZamitnuto = true;

    /**
     * Konstruktor dialogu,
     *
     * @param typ     Typ dialogu
     * @param polozka Položka, jejíž atribut se má upravit.
     */
    public ZadavaciDialog(Typ typ, Sortiment polozka) {
        this.typ = typ;
        this.polozka = polozka;
        this.initOwner(Pos.getInstance().getAplikacniOkno());
    }

    /**
     * Zobrazí zadávací dialog.
     */
    public void zobrazDialog() {
        LOGER.info("Zadávací dialog typu {} se zobrazí pro položku sortimentu s registrem {}.", typ.toString(), polozka.getRegistr());
        try {
            if (this.getStyle() != StageStyle.TRANSPARENT) {
                this.initStyle(StageStyle.TRANSPARENT);
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ZadavaciDialog.fxml"));
            this.setScene(new Scene(loader.load(), Color.TRANSPARENT));
            kontroler = loader.getController();
            kontroler.setDialog(this);
            kontroler.nastavDialog();
            this.centerOnScreen();
            if (this.getModality() != Modality.WINDOW_MODAL) {
                this.initModality(Modality.WINDOW_MODAL);
            }
            this.showAndWait();
        } catch (IOException e) {
            LOGER.error("Nepodařilo se zobrazit zadávací dialog: ", e);
            new DialogZpravy(DialogZpravy.TypZpravy.CHYBA, "Chyba systému", "Nepodařilo se zobrazit zadávací dialog: \n\n" + e.getLocalizedMessage(), Pos.getInstance().getAplikacniOkno()).zobrazDialogAPockej();
        }
    }

    /**
     * Nastaví, že se zobrazuje dialog před vlastní registrací položky v případě zadání množství nebo ceny.
     */
    public void setPredRegistraci() {
        this.predRegistraci = true;
    }

    /**
     * Zjistí, zdali bylo zadání hodnoty zamítnuto (zavírací tlačítko stisknuto).
     *
     * @return zamítnutí zadaní v případě nutného zadání ceny, nebo množství před registrací.
     */
    public boolean isZadaniZamitnuto() {
        return zadaniZamitnuto;
    }

    /**
     * Uzavře progres dialog.
     */
    protected void zavriDialog() {
        LOGER.info("Zadávací dialog bude uzavřen");
        this.close();
    }

    /**
     * Zjistí typ dialogu.
     *
     * @return Typ dialogu, který se má zobrazit.
     */
    protected Typ getTyp() {
        return typ;
    }

    /**
     * Vrátí položku sortimentu.
     *
     * @return Položka sortimentu, která se má upravit.
     */
    protected Sortiment getPolozka() {
        return polozka;
    }

    /**
     * Zadá nebo změní do vybrané položky cenu.
     *
     * @param cena zadaná cena
     */
    protected void zadejCenu(BigDecimal cena) {
        LOGER.info("U položky {} zadána cena {}.", polozka.getRegistr(), cena);
        if (!polozka.isNutnoZadatCenu()) {
            polozka.setPuvodniCena(polozka.getJednotkovaCena());
        }
        polozka.setJednotkovaCena(cena);
        zadaniZamitnuto = false;
        if (!predRegistraci) {
            HlavniOkno.getInstance().getKontroler().setPolozky(DokladProcesor.getInstance().getDoklad().getPolozky());
        }
        zavriDialog();
    }

    /**
     * Zadá, nebo změní do vybrané položky množství.
     *
     * @param mnozstvi zadané množství.
     */
    protected void zadejMnozstvi(BigDecimal mnozstvi) {
        if (mnozstvi.equals(BigDecimal.ZERO)) {
            LOGER.warn("U položky {} zadáno množství 0!", polozka.getRegistr());
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Nulu nelze zadat", "Nemohu zadat nulové množství.", this).zobrazDialogAPockej();
            kontroler.fokus();
        } else if (polozka.getMaxProdejneMnozstvi() != null && polozka.getMaxProdejneMnozstvi().compareTo(mnozstvi) < 0) {
            LOGER.warn("U položky {} je zadané množství {} větší, než maximálně prodejné {}.", polozka.getRegistr(), mnozstvi, polozka.getMaxProdejneMnozstvi());
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Moc vysoké množství",
                    "Pro tuto položku jste zadali příliš vysoké  množství. Maximum je {} {}.",
                    new Object[]{Pomocnici.formatujNaDveMista(polozka.getMaxProdejneMnozstvi()), polozka.getMj().getJednotka()}, this).zobrazDialogAPockej();
            kontroler.fokus();
        } else if (polozka.getMinProdavaneMnozstvi() != null && polozka.getMinProdavaneMnozstvi().compareTo(mnozstvi) > 0) {
            LOGER.warn("U položky {} je zadané množství {} menší, než minimálně prodejné {}.", polozka.getRegistr(), mnozstvi, polozka.getMinProdavaneMnozstvi());
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Moc malé množství",
                    "U této položky nelze tak málo prodat. Prodejní minimum je {} {}.",
                    new Object[]{Pomocnici.formatujNaDveMista(polozka.getMinProdavaneMnozstvi()), polozka.getMj().getJednotka()}, this).zobrazDialogAPockej();
            kontroler.fokus();
        } else if (polozka.isNedelitelne() && mnozstvi.scale() > 0) {
            LOGER.warn("U nedělitelné položky {} zadáno desetinné množství!", polozka.getRegistr());
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Položku nelze rozdělit", "Tuto položku nelze rozdělit. Zadejte prosím celé číslo.", this).zobrazDialogAPockej();
            kontroler.fokus();
        } else {
            LOGER.info("U položky {} zadáno množství {}.", polozka.getRegistr(), mnozstvi);
            polozka.setMnozstvi(mnozstvi);
            zadaniZamitnuto = false;
            if (!predRegistraci) {
                HlavniOkno.getInstance().getKontroler().setPolozky(DokladProcesor.getInstance().getDoklad().getPolozky());
            }
            zavriDialog();
        }
    }

    /**
     * Zadá, nebo změní do vybrané položky hodnotovou slevu.
     *
     * @param sleva zadaná hodnotová sleva.
     */
    protected void zadejHodnotovouSlevu(BigDecimal sleva) {
        if (sleva.compareTo(polozka.getJednotkovaCena()) > 0) {
            LOGER.warn("U položky {} byla zadána větší sleva, než je její prodejní cena!", polozka.getRegistr());
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Sleva překročila prodejní cenu", "Slevu, která je větší, než prodejní cena položky opravdu nelze zadat. Prosím opravte.", this).zobrazDialogAPockej();
            kontroler.fokus();
        } else {
            LOGER.info("U položky {} zadána hodnotová sleva {}.", polozka.getRegistr(), sleva);
            ManazerSlev.zaregistrujSlevuNaPolozku(Sleva.TypSlevy.HODNOTOVA, Sleva.TypPuvodu.RUCNI, sleva, polozka);
            zavriDialog();
        }
    }

    /**
     * Zadá, nebo změní do vybrané položky procentní slevu.
     *
     * @param sleva zadaná procentní sleva.
     */
    protected void zadejProcentniSlevu(BigDecimal sleva) {
        if (sleva.compareTo(new BigDecimal(100)) > 0) {
            LOGER.warn("U položky {} byla zadána větší sleva, než je 100 %!", polozka.getRegistr());
            new DialogZpravy(DialogZpravy.TypZpravy.VAROVANI, "Sleva více než 100 %", "Slevu, která je větší, než je 100 % opravdu nelze zadat. Prosím opravte.", this).zobrazDialogAPockej();
            kontroler.fokus();
        } else {
            LOGER.info("U položky {} zadána procentní sleva {}.", polozka.getRegistr(), sleva);
            ManazerSlev.zaregistrujSlevuNaPolozku(Sleva.TypSlevy.PROCENTNI, Sleva.TypPuvodu.RUCNI, sleva, polozka);
            zavriDialog();
        }
    }
}
