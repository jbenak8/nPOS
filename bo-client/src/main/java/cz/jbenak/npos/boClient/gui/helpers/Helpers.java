package cz.jbenak.npos.boClient.gui.helpers;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.List;

/**
 * General UI components helper class with static methods. It provides e.g. fields values validation methods.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2022-01-27
 */
public class Helpers {

    /**
     * #REGEX_POSITIVE_NUMBER Only positive integers and decimals can be entered.
     * #REGEX_PERCENT percent number (integer or decimal) in range 0 - 100.
     * #REGEX_POSITIVE_INTEGER Only positive integers can be entered.
     * #REGEX_DECIMAL_TWO_PLACES two places decimal number.
     * #REGEX_DECIMAL_THREE_PLACES three places decimal number - used e.g. for measure units.
     * #REGEX_DECIMAL_FIVE_PLACES five places decimal number - good for entering e.g. quantity.
     */
    public static final String REGEX_POSITIVE_NUMBER = "(\\d+)|(\\d+([.|,])?)|(\\d+([.|,]\\d{1,2})?)";
    public static final String REGEX_PERCENT = "([0-9]{1,2}|100([.|,]0{1,2})?)|([0-9]{1,2}([.|,])?)|([0-9]{1,2}([.|,]\\d{1,2})?)";
    public static final String REGEX_POSITIVE_INTEGER = "\\d+";
    public static final String REGEX_DECIMAL_TWO_PLACES = "(\\d+)|(\\d+([.|,])?)|(\\d+([.|,]\\d{1,2})?)";
    public static final String REGEX_DECIMAL_THREE_PLACES = "(\\d+)|(\\d+([.|,])?)|(\\d+([.|,]\\d{1,3})?)";
    public static final String REGEX_DECIMAL_FIVE_PLACES = "(\\d+)|(\\d+([.|,])?)|(\\d+([.|,]\\d{1,5})?)";

    public static void getRegexConstraint(MFXTextField field, boolean allowEmpty, String regex, String errorText, Label... errorTextInfoLabels) {
        Constraint regexConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(errorText)
                .setCondition(Bindings.createBooleanBinding(() -> {
                    boolean matches = field.getText().matches(regex);
                    if (allowEmpty && field.getText().isEmpty()) {
                        return true;
                    }
                    return matches;
                }, field.textProperty()))
                .get();
        field.getValidator().constraint(regexConstraint);
        setShowErrorListener(field, errorTextInfoLabels);
        setHideErrorListener(field, errorTextInfoLabels);
    }

    public static void getEmptyTextConstraint(MFXTextField field, boolean allowEmpty, String errorText, Label... errorTextInfoLabels) {
        Constraint emptyTextConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(errorText)
                .setCondition(Bindings.createBooleanBinding(() -> (!allowEmpty && !field.getText().isBlank()), field.textProperty()))
                .get();
        field.getValidator().constraint(emptyTextConstraint);
        setShowErrorListener(field, errorTextInfoLabels);
        setHideErrorListener(field, errorTextInfoLabels);
    }

    //TODO kontrola funkce
    public static void getActualDateConstraint(MFXDatePicker picker, boolean allowEmpty, String errorText, Label... errorTextInfoLabels) {
        Constraint actuaDateConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(errorText)
                .setCondition(Bindings.createBooleanBinding(() -> {
                            if (picker.valueProperty().getValue() == null && allowEmpty) {
                                return true;
                            }
                            if (picker.valueProperty().getValue() == null && !allowEmpty) {
                                return false;
                            }
                            return picker.valueProperty().getValue().isEqual(LocalDate.now()) || picker.valueProperty().getValue().isAfter(LocalDate.now());
                        }
                )).get();
        picker.getValidator().constraint(actuaDateConstraint);
        setShowErrorListener(picker, errorTextInfoLabels);
        setHideErrorListener(picker, errorTextInfoLabels);
    }

    public static void getDecimalLengthConstraint(MFXTextField field, boolean allowEmpty, int maxIntegerLength, int maxDecimalLength, String errorText, Label... errorTextInfoLabels) {
        Constraint decimalLengthConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(errorText)
                .setCondition(Bindings.createBooleanBinding(() -> {
                    if (field.getText().isEmpty() && allowEmpty) {
                        return true;
                    } else {
                        final String regex = "(\\d{1," + maxIntegerLength + "})|(\\d{1," + maxIntegerLength
                                + "}([.|,])?)|(\\d{1," + maxIntegerLength + "}([.|,]\\d{1," + maxDecimalLength + "})?)";
                        return field.getText().matches(regex);
                    }
                }, field.textProperty()))
                .get();
        field.getValidator().constraint(decimalLengthConstraint);
        setShowErrorListener(field, errorTextInfoLabels);
        setHideErrorListener(field, errorTextInfoLabels);
    }

    public static void getNoItemSelectedConstraint(MFXComboBox<?> field, String errorText, Label... errorTextInfoLabels) {
        Constraint noItemSelectedConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(errorText)
                .setCondition(Bindings.createBooleanBinding(() -> field.getSelectedIndex() >= 0, field.selectedIndexProperty()))
                .get();
        field.getValidator().constraint(noItemSelectedConstraint);
        setShowErrorListener(field, errorTextInfoLabels);
        setHideErrorListener(field, errorTextInfoLabels);
    }

    private static void setShowErrorListener(MFXTextField field, Label... errorTextInfoLabels) {
        field.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                List<Constraint> constraints = field.validate();
                if (!constraints.isEmpty()) {
                    field.pseudoClassStateChanged(PseudoClass.getPseudoClass("invalid"), true);
                    for (Label label : errorTextInfoLabels) {
                        label.setText(constraints.get(0).getMessage());
                        label.setVisible(true);
                    }
                }
            }
        });
    }

    private static void setHideErrorListener(MFXTextField field, Label... errorTextInfoLabels) {
        field.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                for (Label label : errorTextInfoLabels) {
                    label.setVisible(false);
                }
                field.pseudoClassStateChanged(PseudoClass.getPseudoClass("invalid"), false);
            }
        });
    }
}
