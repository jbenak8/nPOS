package cz.jbenak.npos.api.shared.enums;

import java.util.Arrays;
import java.util.Optional;

public enum FinanceOperationType {
    DEPOSIT("Vklad"),
    DISBURSEMENT("Výběr");

    private final String label;

    FinanceOperationType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static FinanceOperationType get(String label) {
        Optional<FinanceOperationType> found = Arrays.stream(FinanceOperationType.values()).filter(type -> type.label.equals(label)).findFirst();
        return found.orElse(null);
    }
}
