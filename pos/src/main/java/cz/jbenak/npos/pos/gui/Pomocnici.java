package cz.jbenak.npos.pos.gui;

import javafx.geometry.Bounds;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Třída pomocných funckí.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2018-01-29
 */
public class Pomocnici {

    /**
     * Definuje, jakého typu jsou vstupní hodnoty.
     */
    public enum TypyVstupnichHodnot {
        CISLO, DECIMAL, TEXT
    }

    /**
     * #REGEX_KLADNE_CISLO Omezení na kladná celá nebo desetinná čísla s oddělovačem čárkou nebo tečkou.
     * #REGEX_PROCENTA Omezení na zadání procent - 0-100 včetně.
     * #REGEX_CELE_KLADNE_CISLO Omezení na celá kladná čísla.
     * #REGEX_DVOUMISTNE_DESETINNE_CISLO desetinné číslo na dvě místa.
     * #REGEX_PETIMISTNE_DESETINNE_CISLO Desetinné číslo na pět míst - zejména pro zadávání množství.
     */
    public static final String REGEX_KLADNE_CISLO = "(\\d+)|(\\d+([.|,])?)|(\\d+([.|,]\\d{1,2})?)";
    public static final String REGEX_PROCENTA = "([0-9]{1,2}|100([.|,]0{1,2})?)|([0-9]{1,2}([.|,])?)|([0-9]{1,2}([.|,]\\d{1,2})?)";
    public static final String REGEX_CELE_KLADNE_CISLO = "\\d+";
    public static final String REGEX_DVOUMISTNE_DESETINNE_CISLO = "(\\d+)|(\\d+([.|,])?)|(\\d+([.|,]\\d{1,2})?)";
    public static final String REGEX_PETIMISTNE_DESETINNE_CISLO = "(\\d+)|(\\d+([.|,])?)|(\\d+([.|,]\\d{1,5})?)";

    /**
     * Zformátuje zadaný vstup na dvě desetinná místa se správným zaokrouhlením.
     *
     * @param hodnota číselná hodnota k formátování.
     * @return textová zformátovaná hodnota.
     */
    public static String formatujNaDveMista(BigDecimal hodnota) {
        Locale lokalizace = new Locale("cs", "CZ");
        DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(lokalizace);
        format.applyPattern("###,##0.00"); //správná maska
        return format.format(hodnota.setScale(2, RoundingMode.HALF_UP));
    }

    /**
     * Zformátuje zadaný vstup na tři desetinná místa se správným zaokrouhlením - zejména pro reprezentaci kurzů měn.
     *
     * @param hodnota číselná hodnota k formátování.
     * @return textová zformátovaná hodnota.
     */
    public static String formatujNaTriMista(BigDecimal hodnota) {
        Locale lokalizace = new Locale("cs", "CZ");
        DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(lokalizace);
        format.applyPattern("###,###0.000"); //správná maska
        return format.format(hodnota.setScale(3, RoundingMode.HALF_UP));
    }

    /**
     * Zformátuje zadaný vstup na celé číslo se správným zaokrouhlením - zejména pro procenta.
     *
     * @param hodnota číselná hodnota k formátování
     * @return textová zformátovaná hodnota
     */
    public static String formatujNaCele(BigDecimal hodnota) {
        Locale lokalizace = new Locale("cs", "CZ");
        DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(lokalizace);
        format.applyPattern("###"); //správná maska
        return format.format(hodnota.setScale(0, RoundingMode.HALF_UP));
    }

    /**
     * Nastaví na daném textovém poli vstupní omezení vkládaných hodnot.
     *
     * @param komponenta   komponenta, na kterou se má omezení vztahovat.
     * @param regex        regulární výraz definující omezení.
     * @param textTooltipu text nápovědy v případě vložení nesprávného znaku.
     */
    public static void setCiselneOmezeni(TextInputControl komponenta, String regex, String textTooltipu) {
        Tooltip ttip = new Tooltip(textTooltipu);
        ttip.setHideOnEscape(true);
        ttip.setAutoHide(true);
        komponenta.setTooltip(ttip);
        komponenta.textProperty().addListener((vkladana, stare, nove) -> {
            if (!(nove.matches(regex) || nove.isEmpty())) {
                defaultAkce(komponenta, stare, ttip);
            } else {
                if (ttip.isShowing()) {
                    ttip.hide();
                }
            }
        });
    }

    /**
     * Metoda, která nastaví poli maximální možnou délku (nebo velikost v případě číselných hodnot), která lze vložit.
     *
     * @param komponenta komponenta, na kterou se pravidlo aplikuje.
     * @param maxDelka   maximální dléka, nebo velikost
     * @param typ        Typ vkládané hodnoty.
     * @see TypyVstupnichHodnot
     */
    public static void setOmezeniMaxDelky(TextInputControl komponenta, int maxDelka, TypyVstupnichHodnot typ) {
        final Tooltip ttip = new Tooltip();
        ttip.setHideOnEscape(true);
        ttip.setAutoHide(true);
        switch (typ) {
            case CISLO:
                ttip.setText("Do tohoto pole lze napsat celé číslo mezi o celkové znakové délce " + maxDelka + " znaků.");
                komponenta.setTooltip(ttip);
                komponenta.textProperty().addListener((vkladana, stare, nove) -> {
                    if (!((nove.matches("\\d") && nove.length() <= maxDelka) || nove.isEmpty())) {
                        defaultAkce(komponenta, stare, ttip);
                    } else {
                        if (ttip.isShowing()) {
                            ttip.hide();
                        }
                    }
                });
                break;
            case DECIMAL:
                ttip.setText("Do tohoto pole lze napsat desetinné číslo mezi o celkové znakové délce " + maxDelka + " znaků včetně desetinné čárky.");
                komponenta.setTooltip(ttip);
                komponenta.textProperty().addListener((vkladana, stare, nove) -> {
                    if (!((nove.matches("(\\d+)|(\\d+([.|,])?)|(\\d+([.|,])\\d+)") && nove.length() <= maxDelka) || nove.isEmpty())) {
                        defaultAkce(komponenta, stare, ttip);
                    } else {
                        if (ttip.isShowing()) {
                            ttip.hide();
                        }
                    }
                });
                break;
            default:
                ttip.setText("Do tohoto pole lze napsat text dlouhý max. " + maxDelka + " znaků.");
                komponenta.setTooltip(ttip);
                komponenta.textProperty().addListener((vkladana, stare, nove) -> {
                    if (!(nove.length() <= maxDelka || nove.isEmpty())) {
                        defaultAkce(komponenta, stare, ttip);
                    } else {
                        if (ttip.isShowing()) {
                            ttip.hide();
                        }
                    }
                });
                break;
        }
    }

    private static void defaultAkce(TextInputControl komponenta, String stare, Tooltip ttip) {
        //Toolkit.getDefaultToolkit().beep();
        if (stare.isEmpty()) {
            komponenta.setText("");
        } else {
            komponenta.setText(stare);
        }
        Bounds bounds = komponenta.localToScreen(komponenta.getBoundsInLocal());
        ttip.show(komponenta, bounds.getMinX(), bounds.getMaxY());
    }

    /**
     * Zformátuje datum do formátu užívaného v ČR (DD.MM.RRRR)
     *
     * @param datum datum k zformátování.
     * @return formátované datum.
     */
    public static String formatujDatum(Date datum) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(datum);
    }

    /**
     * Zformátuje datum a čas do formátu užívaného v ČR (DD.MM.RRRR HH.MM.SS)
     *
     * @param datum prostý objekt data.
     * @return zformátovaný text.
     */
    public static String formatujDatumACas(Date datum) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return sdf.format(datum);
    }
}
