package cz.jbenak.npos.api.shared.enums;

import java.util.Arrays;
import java.util.Optional;

public enum VATType {
    BASE("Základní"),
    LOWERED_1("Snížená 1"),
    LOWERED_2("Snížená 2"),
    ZERO("Nulová");

    private final String label;

    VATType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static VATType get(String label) {
        Optional<VATType> found = Arrays.stream(VATType.values()).filter(type -> type.label.equals(label)).findFirst();
        return found.orElse(null);
    }
}
