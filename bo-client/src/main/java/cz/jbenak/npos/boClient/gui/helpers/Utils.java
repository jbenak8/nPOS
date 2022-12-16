package cz.jbenak.npos.boClient.gui.helpers;

import cz.jbenak.npos.boClient.BoClient;
import cz.jbenak.npos.boClient.exceptions.ClientException;
import cz.jbenak.npos.boClient.gui.dialogs.generic.InfoDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Class with some useful utilities for rendering values - e.g. number formatting
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2022-09-23
 */
public class Utils {

    /**
     * Formats a number to Czech decimal form with given rounding.
     *
     * @param toFormat       number to format.
     * @param decimalNumbers how many decimal points to be shown.
     * @return formatted number string.
     */
    public static String formatDecimalCZWithRounding(BigDecimal toFormat, int decimalNumbers) {
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("cs", "CZ"));
        format.setMinimumFractionDigits(decimalNumbers);
        format.setMinimumFractionDigits(decimalNumbers);
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(toFormat);
    }

    /**
     * Formats a number to Czech decimal form as is without rounding.
     *
     * @param toFormat number to format.
     * @return formatted number string.
     */
    public static String formatDecimalCZPlain(BigDecimal toFormat) {
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("cs", "CZ"));
        format.setMinimumFractionDigits(toFormat.scale());
        return format.format(toFormat.doubleValue());
    }

    /**
     * Format a date to a localized string.
     * @param date date to format.
     * @return Regular date string.
     */
    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        return formatter.format(date);
    }

    /**
     * Show a dialog when some server operation foils.
     *
     * @param e                     an exception, which should be displayed.
     * @param title                 title of the dialog.
     * @param supplementarySubtitle a supplementary dialog to show with HTTP error.
     * @param alternativeSubtitle   alternative subtitle, e.g. Caused by other error.
     */
    public static void showGenricErrorDialog(Throwable e, String title, String supplementarySubtitle, String alternativeSubtitle) {
        InfoDialog errorDialog;
        if (e.getCause() instanceof ConnectException) {
            errorDialog = new InfoDialog(InfoDialog.InfoDialogType.OFFLINE, BoClient.getInstance().getMainStage(), false);
        } else {
            errorDialog = new InfoDialog(InfoDialog.InfoDialogType.ERROR, BoClient.getInstance().getMainStage(), false);
            errorDialog.setDialogTitle(title);
            errorDialog.setDialogMessage(e.getMessage());
            if (e.getCause() instanceof ClientException) {
                errorDialog.setDialogSubtitle(supplementarySubtitle + " HTTP status " + ((ClientException) e.getCause()).getStatus());
            } else {
                errorDialog.setDialogSubtitle(alternativeSubtitle);
            }
        }
        errorDialog.showDialog();
    }
}
