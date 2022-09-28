package cz.jbenak.npos.boClient.gui.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
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
}
