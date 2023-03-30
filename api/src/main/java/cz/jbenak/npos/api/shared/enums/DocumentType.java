package cz.jbenak.npos.api.shared.enums;

import java.util.Arrays;
import java.util.Optional;

public enum DocumentType {
    INVOICE("Faktura"),
    RECEIPT("Paragon"),
    DELIVERY_NOTE("Dodací list"),
    CREDIT_NOTE("Dobropis"),
    RETURN("Vratka"),
    CASH_DEPOSIT("Vklad hotovosti"),
    CASH_DISBURSEMENT("Výběr hotovosti"),
    WAREHOUSE_RECEIPT("Příjemka"),
    WAREHOUSE_ISSUE("Výdejka");

    private final String label;

    DocumentType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static DocumentType get(String label) {
        Optional<DocumentType> found = Arrays.stream(DocumentType.values()).filter(type -> type.label.equals(label)).findFirst();
        return found.orElse(null);
    }
}
