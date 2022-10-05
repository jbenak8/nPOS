package cz.jbenak.npos.boClient.gui.helpers;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * General UI components helper class with static methods. It provides e.g. fields values validation methods.
 *
 * @author Jan Benák
 * @version 1.0.0.0
 * @since 2022-01-27
 */
public class Helpers {

    public static void getEmptyTextConstraint(MFXTextField field, String errorText, Label... errorTextInfoLabels) {
        Constraint emptyTextConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage(errorText)
                .setCondition(Bindings.createBooleanBinding(() -> !field.getText().isBlank(), field.textProperty()))
                .get();
        field.getValidator().constraint(emptyTextConstraint);
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
